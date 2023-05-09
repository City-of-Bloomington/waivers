package annex.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.utils.*;

public class UserList extends CommonInc{

    static Logger logger = LogManager.getLogger(UserList.class);
    static final long serialVersionUID = 300L;
    String name = "", id="", username="", role="", limit="limit 30";
    String group_id = "", exclude_group_id="", dept="";
    boolean active_only = false, active_mail = false;
    List<User> users = null;
    public UserList(){
	super();
    }
    public UserList(boolean deb){
	super(deb);
    }		
    public UserList(boolean deb, String val){
	super(deb);
	setName(val);
    }
    public List<User> getUsers(){
	return users;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setUsername(String val){
	if(val != null)
	    username = val;
    }		
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setGroup_id(String val){
	if(val != null)
	    group_id = val;
    }
    public void setDept(String val){
	if(val != null)
	    dept = val;
    }		
    public void setExclude_group_id(String val){
	if(val != null)
	    exclude_group_id = val;
    }		
    public void setRole(String val){
	if(val != null && !val.equals("-1")){
	    role = val;
	}
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void hasActiveMail(){
	active_mail = true;
    }
    public String getId(){
	return id;
    }
    public String getGroup_id(){
	return group_id;
    }
    public String getDept(){
	return dept;
    }		
    public String getName(){
	return name;
    }
    public String getUsername(){
	return username;
    }		
    public String getRole(){
	if(role.equals("")){
	    return "-1";
	}
	return role;
    }
    public void setNoLimit(){
	limit = "";
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select u.id,u.username,u.full_name,u.dept,u.role,u.activeMail,u.inactive from users u ", qw ="";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	if(!id.equals("")){
	    qw += " u.id = ? ";
	}
	else{
	    if(!name.equals("")){
		qw += " u.full_name like ? ";
	    }
	    if(!username.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " u.username like ? ";
	    }
	    if(active_only){
		if(!qw.equals("")) qw += " and ";
		qw += " u.inactive is null ";
	    }
	    if(active_mail){
		if(!qw.equals("")) qw += " and ";
		qw += " u.active_mail is not null ";
	    }
	    if(!role.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " u.role=? ";
	    }
	    if(!dept.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " u.dept=? ";
	    }						
	    if(!group_id.equals("")){
		qq += ", user_groups ug ";
		if(!qw.equals("")) qw += " and ";
		qw += " ug.user_id=u.id and ug.group_id=?";								
	    }
	    else if(!exclude_group_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " not u.id in (select ug.user_id from user_groups ug where ug.group_id = ?)";			
								
	    }
						
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by u.full_name ";
				
	if(!limit.equals("")){
	    qq += limit;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    if(!id.equals("")){
		pstmt.setString(1, id);
	    }
	    else {
		int jj=1;
		if(!name.equals("")){
		    pstmt.setString(jj++,"%"+name+"%");
		}
		if(!username.equals("")){
		    pstmt.setString(jj++,username+"%");		
		}
		if(!role.equals("")){
		    pstmt.setString(jj++,role);		
		}
		if(!dept.equals("")){
		    pstmt.setString(jj++,dept);		
		}								
		if(!group_id.equals("")){
		    pstmt.setString(jj++,group_id);		
		}
		else if(!exclude_group_id.equals("")){
		    pstmt.setString(jj++,exclude_group_id);		
		}
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(users == null)
		    users = new ArrayList<User>();
		User one =
		    new User(debug,
			     rs.getString(1),
			     rs.getString(2),
			     rs.getString(3),
			     rs.getString(4),
			     rs.getString(5),
			     rs.getString(6) != null,
			     rs.getString(7) != null);
		users.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
}






















































