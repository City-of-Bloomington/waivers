/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package annex.model;
import java.io.*;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.nio.charset.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class FileUpload implements java.io.Serializable{

    boolean debug = false;
    String id="", file_name="", old_file_name="", user_id="", date="";
    String waiver_id="", type="", notes="", hardcopy_location="";
    String task_id="";
    static final long serialVersionUID = 140L;		
    static Logger logger = LogManager.getLogger(FileUpload.class);
    User user = null;
    private Waiver waiver = null;
    private Task task = null;
    public FileUpload(){
    }		
    public FileUpload(String val){
	setId(val);
    }	
    public FileUpload(
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
	setValues(val,
		  val2,
		  val3,
		  val4,
		  val5,
		  val6,
		  val7,
		  val8,
		  val9,
		  val10);

    }
    void setValues(String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6,
		   String val7,
		   String val8,
		   String val9,
		   String val10){
	setId(val);
	setWaiver_id(val2);
	setTask_id(val3);
	setType(val4);				
	setFile_name(val5);
	setOld_file_name(val6);
	setHardcopy_location(val7);
	setDate(val8);
	setNotes(val9);
	setUser_id(val10);
    }
    //
    //
    // getters
    //
    public String getId(){
	return id;
    }	
    public String getUser_id(){
	return user_id;
    }
    public String getWaiver_id(){
	return waiver_id;
    }
    public String getTask_id(){
	return task_id;
    }		
    public String getType(){
	if(type.equals(""))
	    return "-1";
	return type;
    }
    public boolean hasType(){
	return !type.equals("");
    }
    public String getFile_name(){
	return file_name;
    }
    public String getOld_file_name(){
	return old_file_name;
    }	
    public String getDate(){
	return date;
    }
    public String getHardcopy_location(){
	return hardcopy_location;
    }		
    //
    // assuming date is in mm/dd/yyyy format
    //
    public String getYear(){
	String year = "";
	if(!date.equals("")){
	    int index = date.lastIndexOf("/");
	    if(index > 0){
		year = date.substring(index+1);
	    }
	}
	return year;
    }
    public String getNotes(){
	return notes;
    }
		
    public User getUser(){
	if(user == null && !user_id.equals("")){
	    User one = new User(false, user_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		user = one;
	    }
	}
	return user;
    }
    public Waiver getWaiver(){
	if(waiver == null && !waiver_id.equals("")){
	    Waiver one = new Waiver(debug, waiver_id);
	    String back = one.doSelect();
	    if(back.equals(""))
		waiver = one;
	}
	return waiver;
    }
    public Task getTask(){
	if(task == null && !task_id.equals("")){
	    Task one = new Task(debug, task_id);
	    String back = one.doSelect();
	    if(back.equals(""))
		task = one;
	}
	return task;
    }				
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }	
    public void setFile_name (String val){
	if(val != null)
	    file_name = val;
    }
    public void setOld_file_name (String val){
	if(val != null)
	    old_file_name = val;
    }	
    public void setDate (String val){
	if(val != null)
	    date = val;
    }
    public void setHardcopy_location(String val){
	if(val != null){
	    hardcopy_location = val;
	}
    }		
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }		
    public void setWaiver_id (String val){
	if(val != null)
	    waiver_id = val;
    }
    public void setTask_id (String val){
	if(val != null)
	    task_id = val;
    }		
    public void setType(String val){
	if(val != null && !val.equals("-1"))
	    type = val;
    }		
    public void setUser_id (String val){
	if(val != null)
	    user_id = val;
    }

    public boolean hasTask(){
	return !task_id.equals("");
    }
    public String toString(){
	return old_file_name;
    }
    public String genNewFileName(String file_ext){
	String str = "";
	findNewId(); // first get the next id
	getWaiver();
	if(waiver != null){
	    str = waiver.getWaiverNum();
	}
	if(file_ext == null)
	    file_ext="";
	if(!id.equals("")){
	    file_name = "waiver_"+str+"_"+id+"."+file_ext; 
	}
	return file_name;
    }
    @Override
    public int hashCode() {
	int hash = 7, id_int = 0;
	if(!id.equals("")){
	    try{
		id_int = Integer.parseInt(id);
	    }catch(Exception ex){}
	}
	hash = 67 * hash + id_int;
	return hash;
    }
    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final FileUpload other = (FileUpload) obj;
	return this.id.equals(other.id);
    }
    public String findNewId(){
	String msg="";
	PreparedStatement pstmt = null, pstmt2=null;
	Connection con = null;
	ResultSet rs = null;
	String qq = "insert into attachment_seq values(0)";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    pstmt2 = con.prepareStatement(qq);
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}
	return msg;		

    }
    public String doSelect(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "select id,waiver_id,task_id,type,file_name,old_file_name,hardcopy_location,date_format(date,'%m/%d/%Y'),notes,user_id from attachments where id=?";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);				
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setValues(rs.getString(1),
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
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
    public String doSave(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;
	if(file_name.equals("")){
	    msg = "File name is not set ";
	    return msg;
	}
	String qq = "insert into attachments values(?,?,?,?,?, ?,?,now(),?,?)";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    int jj=1;
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(jj++, id);
	    pstmt.setString(jj++, waiver_id);
	    if(task_id.equals(""))
		pstmt.setNull(jj++, Types.INTEGER);
	    else
		pstmt.setString(jj++, task_id);						
						
	    pstmt.setString(jj++, file_name);
	    pstmt.setString(jj++, old_file_name);
	    if(hardcopy_location.equals(""))
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, hardcopy_location);
	    if(type.equals(""))
		type="Other";
	    pstmt.setString(jj++, type);						
	    if(notes.equals(""))
		pstmt.setNull(jj++, Types.VARCHAR);
	    else
		pstmt.setString(jj++, notes);
	    pstmt.setString(jj++, user_id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
    public String doDelete(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;
	if(file_name.equals("")){
	    msg = "File name is not set ";
	    return msg;
	}
	String qq = "delete from attachments where id=?";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    int jj=1;
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(jj++, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }		

}
