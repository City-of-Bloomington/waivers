/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package annex.list;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.utils.*;

public class FileUploadList{

    String id="", user_id="", date="", date_from="", date_to="";
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    String type = "", waiver_id="", task_id="";
    String limit = " limit 30 ";
    static final long serialVersionUID = 150L;		
    static Logger logger = LogManager.getLogger(FileUploadList.class);
    List<FileUpload> uploads = null;
    public FileUploadList(){
		
    }		
    //
    // getters
    //
    public String getId(){
	return id;
    }	
    public String getUser_id(){
	return user_id;
    }
    public String getDate_from(){
	return date_from;
    }
    public String getDate_to(){
	return date_to;
    }
    public String getType(){
	if(type.equals("")){
	    return "-1";
	}
	return type;
    }
    public String getWaiver_id(){
	return waiver_id;
    }
    public String getTask_id(){
	return task_id;
    }		
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }
    public void setType(String val){
	if(val != null && !val.equals("-1"))
	    type = val;
    }
    public void setWaiver_id(String val){
	if(val != null)
	    waiver_id = val;
    }
    public void setTask_id(String val){
	if(val != null)
	    task_id = val;
    }		
    public void setNoLimit(){
	limit = "";
    }
    public List<FileUpload> getUploads(){
	return uploads;
    }
    public FileUpload getLastUpload(){
	if(uploads == null){
	    String back = find();
	}
	if(uploads != null){
	    return uploads.get(0);
	}
	return null;
    }
    public void setId(String val){
	if(val != null){
	    id = val;
	    setNoLimit();
	}
    }	
    public void setUser_id (String val){
	if(val != null)
	    user_id = val;
    }

    public String find(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "select u.id id,u.waiver_id,u.task_id,u.type,u.file_name,u.old_file_name,u.hardcopy_location,date_format(u.date,'%m/%d/%Y'),u.notes,u.user_id from attachments u ";
	boolean unionNeeded = false;
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    String qw = "", qw2="";
	    if(!id.equals("")){
		qw += " u.id = ? ";
	    }
	    else{
		if(!type.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " u.type = ? ";
		}
		if(!waiver_id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " u.waiver_id = ? ";
		}
		if(!task_id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " u.task_id = ? ";
		}								
		if(!date_from.equals("")){
		    if(!qw.equals("")) qw += " and ";								
		    qw += " u.date >= str_to_date('"+date_from+"','%m/%d/%Y')";
		    if(!qw2.equals("")) qw2 += " and ";								
		    qw2 += " u.date >= str_to_date('"+date_from+"','%m/%d/%Y')";										
		}
		if(!date_to.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " date <= str_to_date('"+date_to+"','%m/%d/%Y')";
		    if(!qw2.equals("")) qw2 += " and ";
		    qw2 += " date2 <= str_to_date('"+date_to+"','%m/%d/%Y')";
		}
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    qq += " order by id desc "+limit;
	    // System.err.println(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.equals("")){
		pstmt.setString(jj++, id);
	    }
	    else{
		if(!type.equals("")){
		    pstmt.setString(jj++, type);
		}
		if(!waiver_id.equals("")){
		    pstmt.setString(jj++, waiver_id);
		}
		if(!task_id.equals("")){
		    pstmt.setString(jj++, task_id);
		}								
	    }
	    rs = pstmt.executeQuery();
	    uploads = new ArrayList<>();
	    while(rs.next()){
		FileUpload one = new FileUpload(rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9),
						rs.getString(10));
		uploads.add(one);
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
	
}
