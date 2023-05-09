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

public class GroupAction extends TopAction{

    static final long serialVersionUID = 290L;	
    static Logger logger = LogManager.getLogger(GroupAction.class);
    //
    User user = null;
    Type group = null;
    List<Type> groups = null;
    String groupsTitle = "Current groups";
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
	    back = group.doSave();
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
	    back = group.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);									
	    }
	    else{
		addActionMessage("Updated Successfully");
	    }
	}
	else{		
	    getGroup();
	    if(!id.equals("")){
		back = group.doSelect();
		if(!back.equals("")){
		    addActionError(back);
		}
		else{
		    logger.error(back);	
		}
	    }
	}
	return ret;
    }
    public Type getGroup(){ 
	if(group == null){
	    group = new Type();
	    group.setId(id);
	    group.setTable_name("groups");
	}		
	return group;
    }

    public void setGroup(Group val){
	if(val != null){
	    group = val;
	    group.setTable_name("groups");
	}
    }

    public String getGroupsTitle(){
	return groupsTitle;
    }
    public void setAction2(String val){
	if(val != null && !val.equals(""))		
	    action = val;
    }
    public List<Type> getGroups(){
	logger.debug(" get groups ");
	if(groups == null){
	    TypeList tl = new TypeList(debug, null, "groups");
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

}





































