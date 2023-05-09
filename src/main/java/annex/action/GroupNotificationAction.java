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

public class GroupNotificationAction extends TopAction{

    static final long serialVersionUID = 290L;	
    static Logger logger = LogManager.getLogger(GroupNotificationAction.class);
    //
    User user = null;
    GroupNotification groupNotification = null;
    List<Type> groups = null;
    List<Step> steps = null;		
    List<GroupNotification> groupNotifications = null;
    String groupNotificationsTitle = "Current group notfication settings";
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
	    back = groupNotification.doSave();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		addActionMessage("Added Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    logger.debug(" action update ");
	    back = groupNotification.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		addActionMessage("Updated Successfully");
	    }
	}
	else{		
	    getGroupNotification();
	    if(!id.equals("")){
		back = groupNotification.doSelect();
		if(!back.equals("")){
		    addActionError(back);
		    logger.error(back);
		}								
	    }
	}
	return ret;
    }
    public GroupNotification getGroupNotification(){ 
	if(groupNotification == null){
	    groupNotification = new GroupNotification();
	    groupNotification.setId(id);
	}		
	return groupNotification;
    }

    public void setGroupNotification(GroupNotification val){
	if(val != null){
	    groupNotification = val;
	}
    }

    public String getGroupNotificationsTitle(){
	return groupNotificationsTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.equals(""))		
	    action = val;
    }
    public boolean hasGroupNotifications(){
	getGroupNotifications();
	return groupNotifications != null && groupNotifications.size() > 0;
    }
    public List<GroupNotification> getGroupNotifications(){
	logger.debug(" get group notifications ");
	if(groupNotifications == null){
	    GroupNotificationList tl = new GroupNotificationList(debug);
	    String back = tl.find();
	    if(back.equals("")){
		List<GroupNotification> ones = tl.getGroupNotifications();
		if(ones != null && ones.size() > 0){
		    groupNotifications = ones;
		}
	    }
	    else{
		logger.error(back);
	    }
	}
	return groupNotifications;
    }
    public List<Type> getGroups(){
	if(groups == null){
	    logger.debug(" get groups ");
	    TypeList tl = new TypeList(debug);
	    tl.setTable_name("waivers.groups");
	    String back = tl.find();
	    if(back.equals("")){
		List<Type> ones = tl.getTypes();
		if(ones != null && ones.size() > 0){
		    groups = ones;
		}
	    }
	    else{
		logger.error(back);
	    }
	}
	return groups;

    }
    public List<Step> getSteps(){
	if(steps == null){
	    logger.debug(" get steps ");
	    StepList tl = new StepList(debug);
	    tl.setExclude_step("Start");
	    String back = tl.find();
	    if(back.equals("")){
		List<Step> ones = tl.getSteps();
		if(ones != null && ones.size() > 0){
		    steps = ones;
		}
	    }
	    else{
		logger.error(back);
	    }
	}
	return steps;

    }		

}





































