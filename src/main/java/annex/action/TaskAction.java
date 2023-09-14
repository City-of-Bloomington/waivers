package annex.action;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

public class TaskAction extends TopAction{

    static final long serialVersionUID = 315L;	
    static Logger logger = LogManager.getLogger(TaskAction.class);
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
	    logger.debug(" action save ");
	    getUser();
	    System.err.println(" user "+user);
	    System.err.println(" task "+task);
	    task.setClaimedByIfNotSet(user.getId());
	    back = task.doSave();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
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
	    logger.debug(" action update ");
	    getUser();
	    task.setClaimedByIfNotSet(user.getId());	 // we needed for actions					
	    back = task.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);								
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
	    logger.debug(" action delete ");
	    back = task.doDelete();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		addActionMessage("Deleted Successfully");								
		ret = "search";
	    }
	}
	else if(action.endsWith("Completed")){
	    logger.debug(" action completed ");
	    getTask();
	    getUser();
	    if(!task.isCompleted()){
		task.setClaimedByIfNotSet(user.getId());
		task.setCompleted(true);
		back = task.doUpdate();
		if(!back.equals("")){
		    addActionError(back);
		    logger.error(back);
		}
		else{
		    waiver = task.getWaiver();
		    List<Task> nextTasks = null;
		    if(task.hasPartName()){
			handleWaiverUpdate();
		    }
		    if(!task.hasNextTask()){
			if(!waiver.hasMoreTasks()){
			    back = waiver.doComplete();
			}
		    }
		    else{
			nextTasks = task.getNextTasks();
		    }
		    //
		    // check if we need to email
		    //
		    if(nextTasks != null && nextTasks.size() > 0){
			for(Task nextTask: nextTasks){
			    // System.err.println(" next task "+nextTask);
			    if(nextTask.isNotificationRequired()){
				System.err.println(" notify required ");
				List<GroupNotification> groupNotifications =
				    nextTask.getGroupNotifications();
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
				// System.err.println(" process email ");
				back = processEmails();
			    }
			    else{
				System.err.println(" notify not required ");
			    }
			}
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
		logger.error(back);
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
	logger.debug(" action get waiver ");
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
	logger.debug(" get task ");
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
	    if(back.equals(""))
		tasks = dl.getTasks();
	    else
		logger.error(back);
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
	else{
	    logger.error(back);
	}
	return emailLogs != null && emailLogs.size() > 0;
    }
    public List<EmailLog> getEmailLogs(){
	return emailLogs;
    }		
    private String processEmails(){
	String back = "";
	String subject = "", msg = "";
	getUser();
	String from = user.getUsername()+city_email;
	String to = "", cc = null;
	logger.debug(" process emails ");
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
			String receiver = one.getUsername()+city_email;
			// we do not want to send email to himself
			if(from.indexOf(receiver) > -1) continue;
			if(to.equals("")){
			    to = receiver;
			}
			else{
			    if(cc == null || cc.equals("")){
				cc = receiver;
			    }
			    else{
				if(!cc.equals("")) cc +=",";
				cc += receiver;
			    }
			}
		    }
		}
		if(gg.getName().equals("Legal")){
		    subject = " Waiver application and deed received ";
		    msg = " Hi \n\n";
		    msg += " We would like to inform you that the following waiver is\n "+
			" ready to be prepared and signed \n"+
			" Waiver # = "+waiver.getWaiverNum()+"\n"+
			" Address(s): "+waiver.getBasicInfo()+"\n"+
			" Owner(s): "+waiver.getBasicInfo2()+"\n"+
			" Other Waiver Info: "+waiver.getBasicInfo3()+"\n\n"+
			" Thanks\n\n";
		    if(!to.isEmpty()){
			back = sendEmails(to, from, cc, subject, msg);
		    }
		}
		else if(gg.getName().equals("Utilities")){
		    subject = " Waiver ready to be recorded and service connection to proceed ";
		    msg = " Hi \n\n";
		    msg += " We would like to inform you that the following waiver is\n "+
			" ready to be recorded and service connection for \n"+
			" address below to proceed \n\n"+
			" Waiver # = "+waiver.getWaiverNum()+"\n"+
			" Address(s): "+waiver.getBasicInfo()+"\n"+
			" Owner(s): "+waiver.getBasicInfo2()+"\n"+
			" Other Waiver Info: "+waiver.getBasicInfo3()+"\n\n"+
			" Thanks\n\n";
		    if(!to.isEmpty()){
			back = sendEmails(to, from, cc, subject, msg);
		    }
		}
		else if(gg.getName().equals("GIS")){
		    subject = " Waiver ready to be added to GIS map "+waiver.getWaiverNum();
		    msg = " Hi \n\n";
		    msg += " We would like to inform you that the following waiver is\n "+
			" ready to be added to GIS map. \n"+
			" See waiver info below \n\n"+
			" waiver # = "+waiver.getWaiverNum()+"\n"+
			" Address(s): "+waiver.getBasicInfo()+"\n"+
			" Owner(s):"+waiver.getBasicInfo2()+"\n"+
			"Other waiver info: "+waiver.getBasicInfo3()+"\n\n"+
			" Thanks\n\n";
		    //
		    // user does not want to receive emails from helpdesk
		    // so we are hardwiring it
		    to = "helpdesk@bloomington.in.gov";
		    from = to;
		    cc = null;
		    if(!to.isEmpty()){
			back = sendEmails(to, from, cc, subject, msg);
		    }
		}
		if(!to.isEmpty()){
		    if(!back.equals("")){
			logger.error(back);
			System.err.println(back);
		    }
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
	    back = mail.send();
	    EmailLog elog = new EmailLog(debug, task.getWaiver_id(), task.getTask_id(), to, from, cc, subject, msg, back);
	    back += elog.doSave();
	}
	return back;
    }

}





































