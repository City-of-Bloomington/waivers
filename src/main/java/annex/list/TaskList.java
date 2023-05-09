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
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.utils.*;

public class TaskList extends CommonInc{

    static Logger logger = LogManager.getLogger(TaskList.class);
    static final long serialVersionUID = 110L;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    String date_from = "", date_to="",  id="", waiver_id="", limit="limit 30";
    String group_id = "", step_id="", task_id="", which_date="start_date";
    String claimed_by = ""; // user_id
    boolean completed = false;
    boolean uncompleted = false, activeWaiversOnly=false;
    String user_can_claim = ""; // user id that can claim a non complete task
    List<Task> tasks = null;
    public TaskList(){
	super();
    }
    public TaskList(boolean deb){
	super(deb);
    }		
    public TaskList(boolean deb, String val){
	super(deb);
	setWaiver_id(val);
    }
    public List<Task> getTasks(){
	return tasks;
    }
    public void setWaiver_id(String val){
	if(val != null)
	    waiver_id = val;
    }
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }		
    public void setStep_id(String val){
	if(val != null)
	    step_id = val;
    }
    public void setTask_id(String val){
	if(val != null)
	    task_id = val;
    }		
    public void setGroup_id(String val){
	if(val != null)
	    group_id = val;
    }
    public void setClaimed_id(String val){
	if(val != null)
	    claimed_by = val;
    }
    public void setWhich_date(String val){
	if(val != null)
	    which_date = val;
    }
    public void setUser_can_claim(String val){
	if(val != null && !val.equals("")){
	    user_can_claim = val;
	    setUncompleted();
	    setNoLimit();
	}
    }
    public void setCompleted(){
	completed = true;
    }
    public void setUncompleted(){
	uncompleted = true;
    }
    public void setActiveOnly(){
	setUncompleted();
    }
    public void setActiveWaiversOnly(){
	activeWaiversOnly = true;
    }
    public String getTask_id(){
	return task_id;
    }
    public String getStep_id(){
	return step_id;
    }		
    public String getGroup_id(){
	return group_id;
    }
    public String getWhich_date(){
	return which_date;
    }		
    public void setNoLimit(){
	limit = "";
    }

    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select t.id,"+
	    " t.name,"+
	    " t.alias,"+
	    " t.inactive,"+
	    " t.field_name,"+
						
	    " t.field2_name,"+
	    " t.part_name,"+
	    " t.require_upload,"+
	    " t.suggested_upload_type,"+
	    " a.task_id,"+
						
	    " a.waiver_id,"+
	    " date_format(a.start_date,'%m/%d/%Y'),  "+
	    " date_format(a.completed_date,'%m/%d/%Y'),"+
	    " a.claimed_by,"+
	    " date_format(a.field_value,'%m/%d/%Y'), "+
						
	    " a.field2_value "+
	    " from  steps t, tasks a left join waivers w on w.id=a.waiver_id";
	String qw = " t.id=a.step_id ";				

	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	if(!task_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " a.task_id = ? ";
	}
	else{
	    if(!waiver_id.equals("")){
		if(!qw.equals("")) qw += " and ";								
		qw += " a.waiver_id = ? ";
	    }
	    if(activeWaiversOnly){
		if(!qw.equals("")) qw += " and ";								
		qw += " w.status = 'Open' ";
	    }
	    if(!step_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " a.step_id = ? ";
	    }
	    if(!user_can_claim.equals("")){
		qq += ", group_steps gt, user_groups gu ";
		if(!qw.equals("")) qw += " and ";
		qw += " a.step_id=gt.step_id and gt.group_id=gu.group_id and gu.user_id=? ";			
	    }
	    else if(!group_id.equals("")){
		qq += ", group_steps gt ";
		if(!qw.equals("")) qw += " and ";
		qw += " a.step_id=gt.step_id and gt.group_id=? ";								
	    }
	    if(!date_from.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " a."+which_date+" >= ? ";
	    }
	    if(!date_to.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " a."+which_date+" <= ? ";
	    }						
	    if(completed){
		if(!qw.equals("")) qw += " and ";
		qw += " a.completed_date is not null ";
	    }
	    else if(uncompleted){
		if(!qw.equals("")) qw += " and ";
		qw += " a.completed_date is null ";
	    }
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by a.task_id desc ";
	if(!limit.equals("")){
	    qq += limit;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    if(!task_id.equals("")){
								
		pstmt.setString(1, task_id);
	    }
	    else {
		int jj=1;
		if(!waiver_id.equals("")){
		    pstmt.setString(jj++,waiver_id);
		}
		if(!step_id.equals("")){
		    pstmt.setString(jj++,step_id);
		}
		if(!user_can_claim.equals("")){
		    pstmt.setString(jj++, user_can_claim);
		}
		else if(!group_id.equals("")){
		    pstmt.setString(jj++, group_id);
		}
		if(!date_from.equals("")){
		    java.util.Date  dateTmp = df.parse(date_from);
		    pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
		}
		if(!date_to.equals("")){
		    java.util.Date  dateTmp = df.parse(date_to);
		    pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
		}								
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(tasks == null)
		    tasks = new ArrayList<Task>();
		Task one =
		    new Task(debug,
			     rs.getString(1),
			     rs.getString(2),
			     rs.getString(3),
			     rs.getString(4) != null,
			     rs.getString(5),
			     rs.getString(6),
			     rs.getString(7),
			     rs.getString(8) != null,
			     rs.getString(9),
														 
			     rs.getString(10),
			     rs.getString(11),
			     rs.getString(12),
			     rs.getString(13),
			     rs.getString(14),
			     rs.getString(15),
			     rs.getString(16));
		if(!tasks.contains(one))
		    tasks.add(one);
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






















































