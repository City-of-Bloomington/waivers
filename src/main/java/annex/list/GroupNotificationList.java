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

public class GroupNotificationList extends CommonInc{

    static Logger logger = LogManager.getLogger(GroupNotificationList.class);
    static final long serialVersionUID = 260L;
    String completed_step_id = "", group_id=""; 
    List<GroupNotification> groupNotifications = null;
	
    public GroupNotificationList(){
    }
    public GroupNotificationList(boolean deb){
	super(deb);
    }
    public GroupNotificationList(boolean deb, String val){
	super(deb);
	setStep_id(val);
    }
    public List<GroupNotification> getGroupNotifications(){
	return groupNotifications;
    }
		
    public void setStep_id(String val){
	if(val != null)
	    completed_step_id = val;
    }
    public void setGroup_id(String val){
	if(val != null)
	    group_id = val;
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select t.id,t.group_id,t.completed_step_id,t.inactive from group_notifications t ";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qw = "";
	if(!completed_step_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " t.completed_step_id = ? ";
	}
	if(!group_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " t.group_id = ? ";
	}						
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by t.id ";
	if(debug){
	    logger.debug(qq);
	}
				
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!completed_step_id.equals("")){
		pstmt.setString(jj++,completed_step_id);
	    }
	    if(!group_id.equals("")){
		pstmt.setString(jj++,group_id);
	    }						
	    rs = pstmt.executeQuery();
	    if(groupNotifications == null)
		groupNotifications = new ArrayList<GroupNotification>();
	    while(rs.next()){
		GroupNotification one =
		    new GroupNotification(debug,
					  rs.getString(1),
					  rs.getString(2),
					  rs.getString(3),
					  rs.getString(4)!= null);
		if(!groupNotifications.contains(one))
		    groupNotifications.add(one);
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






















































