package annex;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import org.apache.log4j.Logger;

public class TaskAction extends TopAction{

		static final long serialVersionUID = 315L;	
		static Logger logger = Logger.getLogger(TaskAction.class);
		//
		Task task = null;
		Waiver waiver = null;
		List<Task> tasks = null;
		List<EmailLog> emailLogs = null;
		String task_id = "", waiver_id="";
		String tasksTitle = " Most recent tasks";
		List<Group> toBeNotifiedGroups = null;
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!back.equals("")){
						try{
								HttpServletResponse res = ServletActionContext.getResponse();
								String str = url+"Login";
								res.sendRedirect(str);
								return super.execute();
						}catch(Exception ex){
								System.err.println(ex);
						}	
				}
				if(action.equals("Save")){
						task.setClaimedByIfNotSet(user.getId());
						back = task.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								id = task.getId();
								if(task.hasPartName()){
										getWaiver();
										handleWaiverUpdate();
								}								
								addActionMessage("Saved Successfully");
								ret = "view";
						}
				}				
				else if(action.equals("Save Changes")){
						task.setClaimedByIfNotSet(user.getId());	 // we needed for actions					
						back = task.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								if(task.hasPartName()){
										getWaiver();
										handleWaiverUpdate();
								}
								addActionMessage("Updated Successfully");
								ret = "view";
						}
				}
				else if(action.equals("Delete")){ 
						back = task.doDelete();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Deleted Successfully");								
								ret = "search";
						}
				}
				else if(action.endsWith("Completed")){
						getTask();
						if(!task.isCompleted()){
								task.setClaimedByIfNotSet(user.getId());
								task.setCompleted(true);
								back = task.doUpdate();
								if(!back.equals("")){
										addActionError(back);
								}
								else{
										waiver = task.getWaiver();								
										if(task.hasPartName()){
												handleWaiverUpdate();
										}
										if(!task.hasNextTask()){
												if(!waiver.hasMoreTasks()){
														back = waiver.doComplete();
												}
										}
										//
										// check if we need to email
										//
										if(task.isNotificationRequired()){
												List<GroupNotification> groupNotifications =
														task.getGroupNotifications();
												if(groupNotifications != null && groupNotifications.size() > 0){
														for(GroupNotification one:groupNotifications){
																Group gg = one.getGroup();
																System.err.println(" group "+gg);
																if(gg != null){
																		if(toBeNotifiedGroups == null)
																				toBeNotifiedGroups = new ArrayList<>();
																		toBeNotifiedGroups.add(gg);
																}
														}
												}
												back = processEmails();
										}
								}
						}
						ret = "view";
						addActionMessage("Completed Successfully");				
				}
				else if(action.equals("Edit")){
						task = new Task(debug, task_id);
						back = task.doSelect();
						if(!back.equals("")){
								addActionError(back);
						}
				}
				else if(!task_id.equals("")){
						ret = "view";
						getTask();
				}
				else{
						getTask();
				}
				return ret;
		}
		public Waiver getWaiver(){
				if(waiver == null){
						if(!waiver_id.equals("")){
								waiver = new Waiver(debug, waiver_id);
								waiver.doSelect();
						}
						else {
								getTask();
								waiver = task.getWaiver();
						}
				}		
				return waiver;

		}
		private void handleWaiverUpdate(){
				String back = waiver.updateRelatedPart(task.getPart_name());
				if(!back.equals("")){
						addActionError(back);
				}
		}
		public Task getTask(){ 
				if(task == null){
						if(!task_id.equals("")){
								task = new Task(debug, task_id);
								String back = task.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}
						}
						else{
								task = new Task();
						}
				}		
				return task;
		}

		public void setTask(Task val){
				if(val != null)
						task = val;
		}

		public String getTasksTitle(){
				return tasksTitle;
		}		
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public void setTask_id(String val){
				if(val != null && !val.equals(""))		
						task_id = val;
		}		
		public String getTask_id(){
				if(task_id.equals("") && task != null){
						task_id = task.getTask_id();
				}
				return task_id;
		}
		public void setWaiver_id(String val){
				if(val != null && !val.equals(""))		
						waiver_id = val;
		}		
		public String getWaiver_id(){
				if(waiver_id.equals("") && waiver != null){
						waiver_id = waiver.getId();
				}
				else{
						getTask();
						waiver_id= task.getWaiver_id();
				}
				return waiver_id;
		}		
		// most recent
		public List<Task> getTasks(){ 
				if(tasks == null){
						TaskList dl = new TaskList();
						String back = dl.find();
						tasks = dl.getTasks();
				}		
				return tasks;
		}
		public boolean hasEmailLogs(){
				getTask();
				EmailLogList ell = new EmailLogList(debug, task.getWaiver_id(), task.getTask_id());
				String back = ell.find();
				if(back.equals("")){
						List<EmailLog> logs = ell.getEmailLogs();
						if(logs != null && logs.size() > 0){
								emailLogs = logs;
						}
				}
				return emailLogs != null && emailLogs.size() > 0;
		}
		public List<EmailLog> getEmailLogs(){
				return emailLogs;
		}		
		private String processEmails(){
				String back = "";
				String subject = "", msg = "";
				String from = user.getUsername()+city_email;
				String to = "", cc = null;
				if(waiver == null){
						back = "No waiver available";
						return back;
				}
				if(toBeNotifiedGroups != null){
						for(Group gg:toBeNotifiedGroups){
								to="";cc=null;								
								List<User> users = gg.getUsers();
								for(User one:users){
										if(one.hasActiveMail() && one.isActive()){
												if(to.equals("")){
														to = one.getUsername()+city_email;
												}
												else{
														if(cc == null || cc.equals("")){
																cc = one.getUsername()+city_email;
														}
														else{
																if(!cc.equals("")) cc +=",";
																cc += one.getUsername()+city_email;
														}
												}
										}
								}
								if(gg.getName().equals("Legal")){
										subject = " Waiver application and deed received ";
										msg = " Hi test please ignore\n\n";
										msg += " We would like to inform you that the following waiver is\n "+
												" ready to be prepared and signed \n"+
												" Waiver ID = "+waiver.getId()+"\n"+
												" Address(s): "+waiver.getBasicInfo()+"\n"+
												" Owner(s): "+waiver.getBasicInfo2()+"\n"+
												" Other Waiver Info: "+waiver.getBasicInfo3()+"\n\n"+
												" Thanks\n\n";
										back = sendEmails(to, from, cc, subject, msg);
								}
								else if(gg.getName().equals("Utilities")){
										subject = " Waiver ready to be recorded and service connection to proceed ";
										msg = " Hi test please ignore\n\n";
										msg += " We would like to inform you that the following waiver is\n "+
												" ready to be recorded and service connection for \n"+
												" address below to proceed \n\n"+
												" Waiver ID = "+waiver.getId()+"\n"+
												" Address(s): "+waiver.getBasicInfo()+"\n"+
												" Owner(s): "+waiver.getBasicInfo2()+"\n"+
												" Other Waiver Info: "+waiver.getBasicInfo3()+"\n\n"+
												" Thanks\n\n";
										back = sendEmails(to, from, cc, subject, msg);
								}
								else if(gg.getName().equals("GIS")){
										subject = " Waiver ready to be added to GIS map ";
										msg = " Hi test pleas ignore\n\n";
										msg += " We would like to inform you that the following waiver is\n "+
												" ready to be added to GIS map. \n"+
												" See waiver info below \n\n"+
												" waiver ID = "+waiver.getId()+"\n"+
												" Address(s): "+waiver.getBasicInfo()+"\n"+
												" Owner(s):"+waiver.getBasicInfo2()+"\n"+
												"Other waiver info: "+waiver.getBasicInfo3()+"\n\n"+
												" Thanks\n\n";
										back = sendEmails(to, from, cc, subject, msg);
								}
								if(!back.equals("")){
										logger.error(back);
										System.err.println(back);
								}
						}
				}
				return back;
		}
		String sendEmails(String to,
											String from,
											String cc,
											String subject,
											String msg){
				String back = "";
				if(to.equals("")) return msg;
				EmailHandle mail = new EmailHandle(to, from, cc, subject, msg, debug);
				if(activeMail){
						System.err.println("Email will be sent ");
						back = mail.send();
						EmailLog elog = new EmailLog(debug, task.getWaiver_id(), task.getTask_id(), to, from, cc, subject, msg, back);
						back += elog.doSave();
				}
				return back;
		}

}





































