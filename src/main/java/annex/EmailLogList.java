package annex;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.log4j.Logger;


public class EmailLogList extends CommonInc{

		static Logger logger = Logger.getLogger(EmailLogList.class);
		static final long serialVersionUID = 260L;
		String waiver_id = "", task_id=""; 
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
				if(val != null)
						waiver_id = val;
		}
		public void setTask_id(String val){
				if(val != null)
						task_id = val;
		}
		public String find(){
		
				String back = "";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				Connection con = Helper.getConnection();
				String qq = "select t.id,t.waiver_id,t.task_id,date_format(t.date,'%m/%d/%Y'),t.to_user,t.from_user,t.subject,t.msg,t.email_errors from email_logs t ";
				if(con == null){
						back = "Could not connect to DB";
						addError(back);
						return back;
				}
				String qw = "";
				try{
						if(!waiver_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " t.waiver_id = ? ";
						}
						if(!task_id.equals("")){
								if(!qw.equals("")) qw += " and ";
								qw += " t.task_id = ? ";
						}						
						if(!qw.equals("")){
								qq += " where "+qw;
						}
						qq += " order by t.id desc ";
						if(debug){
								logger.debug(qq);
						}
						pstmt = con.prepareStatement(qq);
						if(!waiver_id.equals("")){
								pstmt.setString(1,waiver_id);
						}
						if(!task_id.equals("")){
								pstmt.setString(2,task_id);
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
																 rs.getString(9));
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






















































