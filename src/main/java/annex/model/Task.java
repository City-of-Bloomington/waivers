package annex.model;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class Task extends Step{

    static Logger logger = LogManager.getLogger(Task.class);
    static final long serialVersionUID = 100L;			
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    //
    // we are using action_id instead of id as we need step id so that
    // we do not need to override step functions that uses step id
    // to find groups, next step and so on
    //
    String field_value = "", field2_value="";
    String task_id="", waiver_id="", start_date="", completed_date="",
	claimed_by="";

    boolean completed = false, hasNextTask=false;
    Waiver waiver = null;
    List<GroupNotification> groupNotifications = null; 
    User claimed_user = null;
    List<Task> nextTasks = null;    
    //
    public Task(){
	super(Helper.debug);
    }
    public Task(boolean deb, String val){
	//
	super(deb);
	setTask_id(val);
    }
    //
    // new tasks we do not need all fields
    //
			 
    public Task(boolean deb,
		String val, // step_id
		String val2  // waiver_id
									
		){
	//
	super(deb, val);
	setWaiver_id(val2);
    }		

    public Task(boolean deb,
		String val, // step id									
		String val2,  // step name
		String val3,  // alias
		boolean val4, // step inactive
		String val5,  // step field_name
		String val6,  // step field2_name
		String val7,  // step part_name
		boolean val8, // required_upload
		String val9,  // suggested_upload_type
								
		String val01, // task_id
		String val02, // waiver_id
		String val03, // start_date
		String val04, // completed_date
		String val05, // claimed_by
		String val06, // field_value
		String val07 // field2_value

		){
	super(deb, val, val2, val3, val4, val5, val6, val7, val8, val9);
	setTask_id(val01);
	setWaiver_id(val02);
	setStart_date(val03);
	setCompleted_date(val04);
	setClaimed_by(val05);
	setField_value(val06);
	setField2_value(val07);
    }		
		
    //
    // getters
    //
    public String getTask_id(){
	return task_id;
    }
    public String getWaiver_id(){
	return waiver_id;
    }
    public String getStep_id(){
	return id;
    }		
    public String getStart_date(){
	return start_date;
    }
    public String getCompleted_date(){
	return completed_date;
    }
    public String getClaimed_by(){
	return claimed_by;
    }		
    public String getField_value(){
	return field_value;
    }
    public String getField2_value(){
	return field2_value;
    }
    public boolean isCompleted(){
	return !completed_date.isEmpty();
    }
    public boolean isClaimed(){
	return !claimed_by.isEmpty();
    }
    public boolean isNotificationRequired(){
	getGroupNotifications();
	return groupNotifications != null && groupNotifications.size() > 0;
    }
    public List<Task> getNextTasks(){
	return nextTasks;
    }    
    public List<GroupNotification> getGroupNotifications(){
	if(groupNotifications == null && isCompleted()){
	    GroupNotificationList gnl = new GroupNotificationList(debug, id);
	    String back = gnl.find();
	    if(back.isEmpty()){
		List<GroupNotification> ones = gnl.getGroupNotifications();
		if(ones != null && ones.size() > 0){
		    groupNotifications = ones;
		}
	    }
	}
	return groupNotifications;
    }
				
    //
    // setters
    //
    public void setTask_id(String val){
	if(val != null)
	    task_id = val;
    }
    public void setWaiver_id(String val){
	if(val != null)
	    waiver_id = val;
    }
    public void setStep_id(String val){
	if(val != null)
	    id = val;
    }
    public void setStart_date(String val){
	if(val != null)
	    start_date = val;
    }
    public void setCompleted_date(String val){
	if(val != null)
	    completed_date = val;
    }
    public void setClaimed_by(String val){
	if(val != null)
	    claimed_by = val;
    }
    public void setClaimedByIfNotSet(String val){
	if(val != null && claimed_by.isEmpty())
	    claimed_by = val;
    }
    public void setField_value(String val){
	if(val != null)
	    field_value = val;
    }
    public void setField2_value(String val){
	if(val != null)
	    field2_value = val;
    }
    public void setCompleted(boolean val){
	completed = val;
    }
    public boolean hasFirstField(){
	return !field_name.isEmpty();
    }
    public boolean hasSecondField(){
	return !field2_name.isEmpty();
    }
    public boolean hasPartName(){
	return !part_name.isEmpty();
    }
    public boolean hasPart(){
	return !part_name.isEmpty();
    }		
    public boolean hasNextTask(){
	return hasNextTask;
    }
		
    public Waiver getWaiver(){
	if(waiver == null && !waiver_id.isEmpty()){
	    Waiver one = new Waiver(debug, waiver_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		waiver = one;
	    }
	}
	return waiver;
    }
    public User getClaimed_user(){
	if(claimed_user == null && !claimed_by.isEmpty()){
	    User one = new User(debug, claimed_by);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		claimed_user = one;
	    }
	}
	return claimed_user;
    }
    public boolean canBeCompleted(){
	if(!task_id.isEmpty()){
	    if(!isCompleted()){
		if(getRequire_upload()){
		    //
		    // check if we have the required attahment uploaded
		    //
		    FileUploadList ful = new FileUploadList();
		    ful.setTask_id(task_id);
		    String back = ful.find();
		    if(back.isEmpty()){
			List<FileUpload> files = ful.getUploads();
			return files != null && files.size() > 0;
		    }
		    return false;
		}
		else{
		    return true;
		}
	    }
	}
	return false;
    }
    /**
     * a user can claim this task if the user is in the group
     *
     */
    public boolean canBeClaimedBy(User one){
	getGroup();
	return group != null && group.isUserInGroup(one);
    }
    public String toString(){
	return task_id;
    }
    public boolean equals(Object obj){
	if(obj instanceof Task){
	    Task one =(Task)obj;
	    return id.equals(one.getId());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 17;
	if(!id.isEmpty()){
	    try{
		seed += Integer.parseInt(id);
	    }catch(Exception ex){
	    }
	}
	return seed;
    }		
    @Override
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	logger.debug(" save ");
	if(waiver_id.isEmpty() || id.isEmpty()){
	    back = "step id or waiver_id not set ";
	    return back;
	}
	//
	// start task and complete task do not have field values
	// and their start_date, completed_date are the same day
	//
	String qq = "insert into tasks (task_id,waiver_id,step_id,start_date,completed_date,claimed_by) values(0,?,?,now(),?,?)";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(jj++, waiver_id);
	    pstmt.setString(jj++, id); // step_id
	    if(completed_date.isEmpty()){
		pstmt.setNull(jj++,Types.DATE);
	    }
	    else{
		java.util.Date  dateTmp = df.parse(completed_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(claimed_by.isEmpty()){
		pstmt.setNull(jj++,Types.INTEGER);
	    }
	    else{
		pstmt.setString(jj++, claimed_by);
	    }								
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
		task_id = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;

    }
    public String createNextTasks(){
	String back = "";
	List<Step> steps = getNextSteps();
	if(steps != null && steps.size() > 0){
	    for(Step tt:steps){
		Task one = new Task(debug, tt.getId(), waiver_id);
		back = one.doSave();
		if(nextTasks == null) nextTasks = new ArrayList<>();
		nextTasks.add(one); 
		hasNextTask = true;
	    }
	}
	return back;
    }

    @Override
    public String doUpdate(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="";
	String qq = "";
	logger.debug(" update ");		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	if(completed){
	    qq = "update tasks set claimed_by=?, field_value=?,field2_value=?,completed_date=now() where task_id=?";								
	}
	else{
	    qq = "update tasks set claimed_by=?, field_value=?,field2_value=? where task_id=?";
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    if(claimed_by.isEmpty())
		pstmt.setNull(1,Types.INTEGER);
	    else
		pstmt.setString(1, claimed_by);
	    if(field_value.isEmpty())
		pstmt.setNull(2,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(field_value);
		pstmt.setDate(2, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(field2_value.isEmpty())
		pstmt.setNull(3,Types.VARCHAR);
	    else{
		pstmt.setString(3, field2_value);
	    }
	    pstmt.setString(4, task_id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(qq);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(back.isEmpty()){
	    back = doSelect();														
	    if(completed){
		back += createNextTasks();
	    }
	}
	return back;

    }
    @Override
    public String doDelete(){
		
	String back = "", qq = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	else{
	    qq = "delete from tasks where task_id=?";
	    logger.debug(qq);
	    try{
		pstmt = con.prepareStatement(qq);
		pstmt.setString(1,task_id);
		pstmt.executeUpdate();
	    }
	    catch(Exception ex){
		back += ex+":"+qq;
		logger.error(back);
		addError(back);
	    }
	    finally{
		Helper.databaseDisconnect(con, pstmt, rs);
	    }
	}
	return back;

    }
	
    @Override
    public String doSelect(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
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
	    " from tasks a,steps t where t.id=a.step_id and a.task_id=?";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,task_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setId(rs.getString(1));
		setName(rs.getString(2));
		setAlias(rs.getString(3));
		setInactive(rs.getString(4) != null);
		setField_name(rs.getString(5));
		setField2_name(rs.getString(6));
		setPart_name(rs.getString(7));
		setRequire_upload(rs.getString(8) != null);
		setSuggested_upload_type(rs.getString(9));
		setTask_id(rs.getString(10));
		setWaiver_id(rs.getString(11));
		setStart_date(rs.getString(12));
		setCompleted_date(rs.getString(13));
		setClaimed_by(rs.getString(14));
		setField_value(rs.getString(15));
		setField2_value(rs.getString(16));
	    }
	    else{
		back = "Record "+task_id+" not found";
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
	

}
