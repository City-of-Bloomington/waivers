package annex.model;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class GroupUser extends CommonInc {

    String group_id="", dept="";
    String[] del_users = null, add_users=null;
    List<User> group_users = null;
    List<User> other_users = null;
    static final long serialVersionUID = 285L;
    static Logger logger = LogManager.getLogger(GroupUser.class);
    //
    public GroupUser(){
	super();
    }
    public GroupUser(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public GroupUser(String val){
	//
	setGroup_id(val);
    }
    public GroupUser(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setGroup_id(val);
    }


    public String getGroup_id(){
	return group_id;
    }

    public String getDept(){
	return dept;
    }				

    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }

    public void setDept(String val){
	if(val != null && !val.equals("-1"))
	    dept = val;
    }
    public void setDel_users(String[] vals){
	if(vals != null)
	    del_users = vals;
    }
    public void setAdd_users(String[] vals){
	if(vals != null)
	    add_users = vals;
    }
    public boolean hasGroupUsers(){
	getGroup_users();
	return group_users != null && group_users.size() > 0;
    }
    public boolean hasOtherUsers(){
	getOther_users();
	return other_users != null && other_users.size() > 0;
    }		
    public List<User> getGroup_users(){
	logger.debug(" get group users");
	if(group_users == null && !group_id.equals("")){
	    UserList ul = new UserList();
	    ul.setGroup_id(group_id);
	    String back = ul.find();
	    if(back.equals("")){
		group_users = ul.getUsers();
	    }
	}
	return group_users;
    }
    public List<User> getOther_users(){
	logger.debug(" get other users");
	if(other_users == null){
	    UserList ul = new UserList();
	    if(!group_id.equals("")){
		ul.setExclude_group_id(group_id);
	    }
	    if(!dept.equals("")){
		ul.setDept(dept);
	    }
	    String back = ul.find();
	    if(back.equals("")){
		other_users = ul.getUsers();
	    }
	}
	return other_users;
    }		
    //
    public String doAdd(){
	String msg = "";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String qq = " insert into user_groups values(?,?) "; // user_id, group_id
	if(add_users == null || add_users.length < 1){
	    msg = "users not set ";
	    addError(msg);
	    return msg;
	}
	else if(group_id.equals("")){
	    msg = "group not set ";
	    addError(msg);
	    return msg;
	}
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    addError(msg);
	    return msg;
	}			
	try{
	    stmt = con.prepareStatement(qq);
	    for(String user_id:add_users){
		stmt.setString(1, user_id);
		stmt.setString(2, group_id);								
		stmt.executeUpdate();
	    }
	}catch(Exception ex){
	    logger.error(ex+" : "+qq);
	    msg += " "+ex;
	    addError(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }
    //
    public String doRemove(){
	String msg = "";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String qq = " delete from user_groups where user_id=? and group_id=? "; // user_id, group_id
	if(group_id.equals("")){
	    msg = "group not set ";
	    addError(msg);
	    return msg;
	}
	else if(del_users == null || del_users.length < 1){
	    msg = "no user set to be deleted ";
	    addError(msg);
	    return msg;
	}
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    addError(msg);
	    return msg;
	}			
	try{
	    stmt = con.prepareStatement(qq);
	    for(String user_id:del_users){
		stmt.setString(1, user_id);
		stmt.setString(2, group_id);								
		stmt.executeUpdate();
	    }						
	}catch(Exception ex){
	    logger.error(ex+" : "+qq);
	    msg += " "+ex;
	    addError(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }
		
		
}
