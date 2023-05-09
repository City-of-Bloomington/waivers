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

public class UserAction extends TopAction{

    static final long serialVersionUID = 290L;	
    static Logger logger = LogManager.getLogger(UserAction.class);
    //
    User user = null;
    List<User> users = null;
    String usersTitle = "Current Users";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.equals("")){
	    logger.error(back);
	    try{
		HttpServletResponse res = ServletActionContext.getResponse();
		String str = url+"Login";
		res.sendRedirect(str);
		return super.execute();
	    }catch(Exception ex){
		logger.error(back);
	    }	
	}
	if(action.equals("Save")){
	    logger.debug("save");
	    back = user.doSave();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		id = user.getId();
		addActionMessage("Saved Successfully");
	    }
	}				
	else if(action.equals("Save Changes")){
	    logger.debug("update");
	    back = user.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		addActionMessage("Updated Successfully");
		id = user.getId();
	    }
	}
	else if(action.equals("Delete")){
	    logger.debug("delete");
	    back = user.doDelete();
	    if(!back.equals("")){
		// back to the same page 
		addActionError(back);
		logger.error(back);								
	    }
	    else{
		user = new User();
		addActionMessage("Deleted Successfully");
		id="";
	    }
	}
	else if(action.equals("Edit")){
	    logger.debug("Edit");
	    user = new User(id);
	    back = user.doSelect();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	}
	else if(action.startsWith("New")){ 
	    user = new User();
	}				
	else if(!id.equals("")){ 
	    getUser();
	    back = user.doSelect();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	}
	else{		
	    getUser();
	}
	return ret;
    }
    public User getUser(){ 
	if(user == null){
	    if(!id.equals("")){
		user = new User(id);
	    }
	    else{
		user = new User();
	    }
	}		
	return user;
    }

    public void setUser(User val){
	if(val != null){
	    user = val;
	    if(!id.equals("")){
		user.setId(id);
	    }
	}
    }

    public String getUsersTitle(){
	return usersTitle;
    }		
    public void setAction2(String val){
	if(val != null && !val.equals(""))		
	    action = val;
    }
    public void setYear(String val){
	if(val != null && !val.equals(""))		
	    id = val; // id also
    }		
    public String getId(){
	if(id.equals("") && user != null){
	    id = user.getId();
	}
	return id;
    }
    public String getYear(){
	if(id.equals("") && user != null){
	    id = user.getId();
	}
	return id;
    }		
    // most recent
    public List<User> getUsers(){
	logger.debug("get users");
	if(users == null){
	    UserList dl = new UserList();
	    String back = dl.find();
	    if(back.equals(""))
		users = dl.getUsers();
	    else
		logger.error(back);
	}		
	return users;
    }

}





































