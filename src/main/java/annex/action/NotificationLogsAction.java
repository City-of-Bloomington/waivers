package annex.action;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

public class NotificationLogsAction extends TopAction{

    static final long serialVersionUID = 290L;	
    static Logger logger = LogManager.getLogger(NotificationLogsAction.class);
    //
    String waiver_id="", waiver_num="", task_id="";
    User user = null;
    List<EmailLog> emailLogs = null;
    String logsTitle = "Latest notification logs";
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
	getEmailLogs();
	return ret;
    }

    public String getLogsTitle(){
	return logsTitle;
    }
		
    public void setAction2(String val){
	if(val != null && !val.equals(""))		
	    action = val;
    }

    public void setWaiver_id(String val){
	if(val != null && !val.equals(""))		
	    waiver_id = val;
    }
    public void setWaiver_num(String val){
	if(val != null && !val.equals(""))		
	    waiver_num = val;
    }		
		
    public void setTask_id(String val){
	if(val != null && !val.equals(""))		
	    task_id = val;
    }
    public String getWaiver_id(){
	return waiver_id;
    }
    public String getWaiver_num(){
	return waiver_num;
    }		
		
    public String getTask_id(){
	return task_id;
    }				
		
    public List<EmailLog> getEmailLogs(){
	logger.debug(" get email logs ");
	if(emailLogs == null){
	    EmailLogList tl = new EmailLogList(debug);
	    tl.setWaiver_id(waiver_id);
	    tl.setWaiver_num(waiver_num);
	    tl.setTask_id(task_id);
	    String back = tl.find();
	    if(back.equals("")){
		List<EmailLog> ones = tl.getEmailLogs();
		if(ones != null && ones.size() > 0){
		    emailLogs = ones;
		}
	    }
	    else{
		logger.error(back);
	    }
	    if(emailLogs != null && emailLogs.size() > 0)
		addActionMessage(" Found "+emailLogs.size()+" records");
	    else
		addActionMessage(" No records found ");								
	}
	return emailLogs;
    }
    public boolean hasEmailLogs(){
	getEmailLogs();
	return emailLogs != null && emailLogs.size() > 0;
    }

}





































