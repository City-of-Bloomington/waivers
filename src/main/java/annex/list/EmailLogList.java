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

public class EmailLogList extends CommonInc{

    static Logger logger = LogManager.getLogger(EmailLogList.class);
    static final long serialVersionUID = 260L;
    String waiver_id = "", waiver_num="", task_id="", limit="limit 12"; 
    List<EmailLog> emailLogs = null;
	
    public EmailLogList(){
    }
    public EmailLogList(boolean deb){
	super(deb);
    }
    public EmailLogList(boolean deb, String val){
	super(deb);
	setWaiver_id(val);
    }
    public EmailLogList(boolean deb, String val, String val2){
	super(deb);
	setWaiver_id(val);
	setTask_id(val2);
    }		
    public List<EmailLog> getEmailLogs(){
	return emailLogs;
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
    public void setLimit(String val){
	if(val != null && !val.equals("")){
	    limit = val;
	}
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	logger.debug(" in find ");
	String qq = "select t.id,t.waiver_id,t.task_id,date_format(t.date,'%m/%d/%Y'),t.to_user,t.from_user,t.cc_users,t.subject,t.msg,t.email_errors from email_logs t ";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    logger.error(back);
	    return back;
	}
	String qw = "";
	try{
	    if(!waiver_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " t.waiver_id = ? ";
	    }
	    if(!waiver_num.equals("")){
		qq += " join waivers w on w.id=t.waiver_id ";
		if(!qw.equals("")) qw += " and ";
		qw += " w.waiver_num = ? ";
	    }						
	    if(!task_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " t.task_id = ? ";
	    }						
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    qq += " order by t.id desc ";
	    qq += " "+limit;
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!waiver_id.equals("")){
		pstmt.setString(jj++,waiver_id);
	    }
	    if(!waiver_num.equals("")){
		pstmt.setString(jj++,waiver_num);
	    }						
	    if(!task_id.equals("")){
		pstmt.setString(jj++,task_id);
	    }						
	    rs = pstmt.executeQuery();
	    if(emailLogs == null)
		emailLogs = new ArrayList<EmailLog>();
	    while(rs.next()){
		EmailLog one =
		    new EmailLog(debug,
				 rs.getString(1),
				 rs.getString(2),
				 rs.getString(3),
				 rs.getString(4),
				 rs.getString(5),
				 rs.getString(6),
				 rs.getString(7),
				 rs.getString(8),
				 rs.getString(9),
				 rs.getString(10));
		if(!emailLogs.contains(one))
		    emailLogs.add(one);
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






















































