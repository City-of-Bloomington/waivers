package annex.model;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class GroupNotification extends CommonInc implements java.io.Serializable{
    static final long serialVersionUID = 250L;	
    static Logger logger = LogManager.getLogger(GroupNotification.class);
    String id="",
	group_id="", completed_step_id="",
	inactive="";
    Group group = null;
    Step step = null;
    //
    public GroupNotification(){

    }	
    public GroupNotification(boolean deb, String val, String val2, String val3, boolean val4){
	super(deb);				
	setValues(val, val2, val3, val4);

    }
    // for new records
    public GroupNotification(boolean deb, String val, String val2, boolean val3){
	super(deb);				
	setValues(null, val, val2, val3);
    }		
    void setValues(String val, String val2, String val3, boolean val4){
	setId(val);
	setGroup_id(val2);
	setStep_id(val3);
	setInactive(val4);
    }
		
    public GroupNotification(String val){
	setId(val);
    }
    public boolean equals(Object obj){
	if(obj instanceof GroupNotification){
	    GroupNotification one =(GroupNotification)obj;
	    return id.equals(one.getId());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 17;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id);
	    }catch(Exception ex){
	    }
	}
	return seed;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getGroup_id(){
	return group_id;
    }
    public String getStep_id(){
	return completed_step_id;
    }

    public boolean getInactive(){
	return !inactive.equals("");
    }
    public boolean isActive(){
	return inactive.equals("");
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }
    public void setStep_id(String val){
	if(val != null)
	    completed_step_id = val;
    }
    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }
    public boolean canReceiveEmail(){
	return isActive() && hasGroup();
    }
    public boolean hasGroup(){
	getGroup();
	return group != null;
    }
    public String toString(){
	return id;
    }
    public Group getGroup(){
	if(group == null && !group_id.equals("")){
	    Group one = new Group(debug, group_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		group  = one;
	    }
	    else{
		logger.error(back);
	    }
	}
	return group;
    }
    public Step getStep(){
	if(step == null && !completed_step_id.equals("")){
	    Step one = new Step(debug, completed_step_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		step  = one;
	    }
	    else{
		logger.error(back);
	    }
	}
	return step;
    }		
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,group_id,completed_step_id,inactive "+
	    "from group_notifications where id=?";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setValues(id,
			  rs.getString(2),
			  rs.getString(3),
			  rs.getString(4) != null);
	    }
	    else{
		back ="Record "+id+" Not found";
		message = back;
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);			
	}
	return back;
    }

    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into group_notifications values(0,?,?,null)";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	if(group_id.equals("")){
	    back = "Group not set";
	    addError(back);
	    return back;
	}
	if(completed_step_id.equals("")){
	    back = "Workflow step not set";
	    addError(back);
	    return back;
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt.setString(1, group_id);
	    pstmt.setString(2, completed_step_id);						
	    pstmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);				
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;

    }
    public String doUpdate(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "update group_notifications set group_id=?,completed_step_id=?,inactive=? where id=?";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	if(group_id.equals("")){
	    back = "Group not set";
	    addError(back);
	    return back;
	}
	if(completed_step_id.equals("")){
	    back = "Workflow step not set";
	    addError(back);
	    return back;
	}						
	try{
	    pstmt = con.prepareStatement(qq);
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt.setString(1, group_id);
	    pstmt.setString(2, completed_step_id);
	    if(inactive.equals("")){
		pstmt.setNull(3, Types.CHAR);
	    }
	    else{
		pstmt.setString(3, "y");
	    }
	    pstmt.setString(4, id);							 
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;

    }		

}
