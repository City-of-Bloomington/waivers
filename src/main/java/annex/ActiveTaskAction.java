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

public class ActiveTaskAction extends TopAction{

		static final long serialVersionUID = 317L;	
		static Logger logger = Logger.getLogger(ActiveTaskAction.class);
		//
		List<Task> tasks = null;
		String task_id = "";
		String tasksTitle = " Most recent active tasks";
		TaskList taskList = null;
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
				back = prepareTaskList();
				if(!back.equals("")){
						addActionError(back);
				}
				else{
						if(tasks == null){
								tasksTitle = "No active tasks found ";
						}
						else{
								tasksTitle = "Found "+tasks.size()+" active task(s) ";
						}
				}
				return ret;
		}
		public String prepareTaskList(){
				String back = "";
				if(taskList == null){
						taskList = new TaskList(debug);
						taskList.setUser_can_claim(user.getId()); // my action list
						taskList.setActiveWaiversOnly();
						back = taskList.find();
						if(back.equals("")){
								List<Task> ones = taskList.getTasks();
								if(ones != null && ones.size() > 0){
										tasks = ones;
								}
						}
				}
				return back;
		}

		public String getTasksTitle(){
				return tasksTitle;
		}		
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		
		public List<Task> getTasks(){ 

				return tasks;
		}
		
}





































