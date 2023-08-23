package annex.model;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class EmailLog extends CommonInc implements java.io.Serializable{

    String id="",
	waiver_id="", date="",
	task_id="",
	cc_users="",
	to_user="",
	from_user="",
	subject="",
	msg="",
	email_errors="";
    static final long serialVersionUID = 250L;	
    static Logger logger = LogManager.getLogger(EmailLog.class);
    //
    public EmailLog(){

    }	
    public EmailLog(boolean deb,
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6,
		    String val7,
		    String val8,
		    String val9,
		    String val10){
	super(deb);				
	setValues(val, val2, val3, val4, val5, val6, val7, val8, val9, val10);

    }
    // for new records
    public EmailLog(boolean deb,
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6,
		    String val7,
		    String val8){
	super(deb);				
	setValues(null, val, val2, null, val3, val4, val5, val6, val7, val8);
    }		
    void setValues(String val, String val2, String val3, String val4, String val5, String val6, String val7, String val8, String val9, String val10){
	setId(val);
	setWaiver_id(val2);
	setTask_id(val3);
	setDate(val4);
	setToUser(val5); // to
	setFromUser(val6); // from 
	setCcUsers(val7); // cc
	setSubject(val8);
	setMsg(val9);
	setEmailErrors(val10);
    }
		
    public EmailLog(String val){
	setId(val);
    }
    public boolean equals(Object obj){
	if(obj instanceof EmailLog){
	    EmailLog one =(EmailLog)obj;
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
    public String getWaiver_id(){
	return waiver_id;
    }
    public String getTask_id(){
	return task_id;
    }
    public String getDate(){
	return date;
    }		
    public String getToUser(){
	return to_user;
    }
    public String getCcUsers(){
	return cc_users;
    }		
    public String getFromUser(){
	return from_user;
    }
    public String getSubject(){
	return subject;
    }
    public String getMsg(){
	return msg;
    }
    public String getEmailErrors(){
	return email_errors;
    }
    public boolean isSuccess(){
	return email_errors.equals("");
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setWaiver_id(String val){
	if(val != null)
	    waiver_id = val;
    }
    public void setTask_id(String val){
	if(val != null)
	    task_id = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }		
    public void setToUser(String val){
	if(val != null)
	    to_user = val;
    }
    public void setCcUsers(String val){
	if(val != null)
	    cc_users = val;
    }		
    public void setFromUser(String val){
	if(val != null)
	    from_user = val;
    }
    public void setSubject(String val){
	if(val != null)
	    subject = val;
    }		
    public void setMsg(String val){
	if(val != null)
	    msg = val;
    }
    public void setEmailErrors(String val){
	if(val != null)
	    email_errors = val;
    }
    public String toString(){
	return subject;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,waiver_id,task_id,date_format(t.date,'%m/%d/%Y'),to_user,from_user,cc_users,subject, msg, email_errors "+
	    "from email_logs where id=?";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setValues(id,
			  rs.getString(2),
			  rs.getString(3),
			  rs.getString(4),
			  rs.getString(5),
			  rs.getString(6),
			  rs.getString(7),
			  rs.getString(8),
			  rs.getString(9),
			  rs.getString(10));
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
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String qq = "insert into email_logs values(0,?,?,now(),?, ?,?,?,?,?)";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,waiver_id);
						
	    if(task_id.equals(""))
		pstmt.setNull(2, Types.INTEGER);
	    else
		pstmt.setString(2, task_id);
	    if(to_user.equals(""))
		pstmt.setNull(3, Types.VARCHAR);
	    else
		pstmt.setString(3, to_user);
	    if(from_user.equals(""))
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4, from_user);
	    if(cc_users.equals(""))
		pstmt.setNull(5, Types.VARCHAR);
	    else
		pstmt.setString(5, from_user);						
	    if(subject.equals(""))
		pstmt.setNull(6, Types.VARCHAR);
	    else
		pstmt.setString(6, subject);
	    if(msg.equals(""))
		pstmt.setNull(7, Types.VARCHAR);
	    else
		pstmt.setString(7, msg);
	    if(email_errors.equals(""))
		pstmt.setNull(8, Types.VARCHAR);
	    else
		pstmt.setString(8, email_errors);						
	    pstmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt2 = con.prepareStatement(qq);				
	    rs = pstmt2.executeQuery();
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
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}
	return back;

    }

}
