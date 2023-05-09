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
import javax.naming.*;
import javax.naming.directory.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class Step extends Type{

    static Logger logger = LogManager.getLogger(Step.class);
    static final long serialVersionUID = 220L;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
    String field_name = "",
	field2_name="",
	part_name="",
	require_upload="",
	suggested_upload_type=""; // Application, Deed, Recorded Waiver, Map
    String group_id = "", alias=""; // when assign step to group
    Group group = null;
    List<Step> nextSteps = null;
    //
    public Step(){
	super(Helper.debug);
    }
    public Step(boolean deb){
	//
	super(deb);
    }				
    public Step(boolean deb, String val){
	//
	super(deb, val);
    }		
    public Step(boolean deb, String val, String val2){
	//
	super(deb, val, val2);
    }
    public Step(boolean deb, String val, String val2, boolean val3){
	//
	// initialize
	//
	super(deb, val, val2, val3);
    }			

    public Step(boolean deb,
		String val,
		String val2,
		String val3,								
		boolean val4,

		String val5,
		String val6,
		String val7,
		boolean val8,
		String val9){
	//
	// initialize
	//
	super(deb, val, val2, val4);
	setAlias(val3);
	setField_name(val5);
	setField2_name(val6);
	setPart_name(val7);
	setRequire_upload(val8);
	setSuggested_upload_type(val9);
    }		
	
    //
    // getters
    //
    public String getField_name(){
	return field_name;
    }
    public String getField2_name(){
	return field2_name;
    }
    public String getPart_name(){
	return part_name;
    }
    public String getSuggested_upload_type(){
	return suggested_upload_type;
    }		
    public String getAlias(){
	return alias;
    }		
		
    public String getGroup_id(){
	if(group_id.equals("")){
	    getGroup();
	    if(group != null){
		group_id = group.getId();
	    }
	}
	return group_id;
    }
    // require attachment to upload
    public boolean getRequire_upload(){
	return !require_upload.equals("");
    }
    public boolean hasSuggested_upload_type(){
	return !suggested_upload_type.equals("");
    }
    //
    // setters
    //
    public void setField_name(String val){
	if(val != null)
	    field_name = val;
    }
    public void setField2_name(String val){
	if(val != null)
	    field2_name = val;
    }
    public void setPart_name(String val){
	if(val != null)
	    part_name = val;
    }
    public void setAlias(String val){
	if(val != null)
	    alias = val;
    }
    public void setSuggested_upload_type(String val){
	if(val != null && !val.equals("-1"))
	    suggested_upload_type = val;
    }
    public void setGroup_id(String val){
	if(val != null && !val.equals("-1"))
	    group_id = val;
    }
    public void setRequire_upload(boolean val){
	if(val){
	    require_upload="y";
	}
    }
    public boolean hasFirstField(){
	return !field_name.equals("");
    }
    public boolean hasSecondField(){
	return !field2_name.equals("");
    }
    public boolean hasGroup(){
	return getGroup() != null;
    }
    public String toString(){
	return name;
    }
    public Group getGroup(){
	if(group == null && !id.equals("")){
	    findGroup();
	}
	return group;
    }
    public List<Step> getNextSteps(){
	if(nextSteps == null && !id.equals("")){
	    findNextSteps();
	}
	return nextSteps;
    }
    public boolean hasOneOnlyNextStep(){
	if(nextSteps == null){
	    getNextSteps();
	}
	return nextSteps != null && nextSteps.size() == 1;
    }
    public String getGroupName(){
	String ret = "";
	getGroup();
	if(group != null){
	    ret = group.getName();
	}
	return ret;
    }
    //
    // check if there are more steps
    // if not, we are done with this work_flow
    // but other steps may still have other work_flow
    //
    public boolean isFinal(){
	if(nextSteps == null){
	    getNextSteps();
	}
	return nextSteps == null || nextSteps.size() == 0;
    }
    public String findNextSteps(){
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = " select f.next_step_id, "+
	    " t.name,t.alias,t.inactive,t.field_name,t.field2_name,t.part_name,t.require_upload,t.suggested_upload_type "+
	    " from work_flows f, steps t "+
	    " where f.step_id=? and t.id=f.next_step_id ";
	if(id.equals("")){
	    back = "step id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
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
	    while(rs.next()){
		Step one = new Step(debug,
				    rs.getString(1),
				    rs.getString(2),
				    rs.getString(3),
				    rs.getString(4) != null,
				    rs.getString(5),
				    rs.getString(6),
				    rs.getString(7),
				    rs.getString(8) != null,
				    rs.getString(9));
		if(nextSteps == null)
		    nextSteps = new ArrayList<Step>();
		nextSteps.add(one);
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
    public String findGroup(){
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = " select  g.id,g.name,g.inactive "+
	    " from waivers.groups g join waivers.group_steps t on t.group_id=g.id "+
	    " where t.step_id=? ";
	if(id.equals("")){
	    back = "step id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
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
		group = new Group(debug,
				  rs.getString(1),
				  rs.getString(2),
				  rs.getString(3) != null);
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
    @Override
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into steps values(0,?,?,?,?,?,?,?,null)";
	if(name.equals("")){
	    back = "organization name not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,name);
	    if(alias.equals("")){
		if(name.length() > 20)
		    alias = name.substring(0,20);
		else
		    alias = name;
	    }
	    pstmt.setString(2,alias);						
	    if(field_name.equals(""))
		pstmt.setNull(3,Types.VARCHAR);
	    else
		pstmt.setString(3, field_name);
	    if(field2_name.equals(""))
		pstmt.setNull(4,Types.VARCHAR);
	    else
		pstmt.setString(4, field2_name);
	    if(part_name.equals(""))
		pstmt.setNull(5,Types.VARCHAR);
	    else
		pstmt.setString(5, part_name);
	    if(require_upload.equals(""))
		pstmt.setNull(6,Types.CHAR);
	    else
		pstmt.setString(6, "y");
	    if(suggested_upload_type.equals(""))
		pstmt.setNull(7, Types.VARCHAR);
	    else
		pstmt.setString(7, suggested_upload_type);						
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
	    logger.error(ex);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(!group_id.equals("")){
	    back += assignToGroup(true); // for new
	}
	return back;

    }
    public String doUpdate(){
		
	String back = "";
	if(name.equals("")){
	    back = " name not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="";
	String qq = "";
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	else{
	    qq = "update steps set name=?,alias=?,inactive=?,field_name=?,field2_name=?,part_name=?,require_upload=?,suggested_upload_type=?,inactive=? where id=?";
	    logger.debug(qq);
	    try{
		pstmt = con.prepareStatement(qq);
		pstmt.setString(1,name);
		if(alias.equals("")){
		    if(name.length() > 20)
			alias = name.substring(0,20);
		    else
			alias = name;
		}
		pstmt.setString(2, alias);								
		if(inactive.equals(""))
		    pstmt.setNull(3,Types.CHAR);
		else
		    pstmt.setString(3, "y");
		if(field_name.equals(""))
		    pstmt.setNull(4,Types.VARCHAR);
		else
		    pstmt.setString(4, field_name);
		if(field2_name.equals(""))
		    pstmt.setNull(5,Types.VARCHAR);
		else
		    pstmt.setString(5, field2_name);
		if(part_name.equals(""))
		    pstmt.setNull(6,Types.VARCHAR);
		else
		    pstmt.setString(6, part_name);
		if(require_upload.equals(""))
		    pstmt.setNull(7,Types.CHAR);
		else
		    pstmt.setString(7, "y");
		if(suggested_upload_type.equals(""))
		    pstmt.setNull(8, Types.VARCHAR);
		else
		    pstmt.setString(8, suggested_upload_type);
		if(inactive.equals(""))
		    pstmt.setNull(9,Types.CHAR);
		else
		    pstmt.setString(9, "y");								
		pstmt.setString(10, id);
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
	    if(!group_id.equals("")){
		back += assignToGroup(false);
	    }						
	}
	return back;

    }
    private String assignToGroup(boolean isNew){
	String back = "";
	if(id.equals("")){
	    back = " id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="";
	String qq = "";
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(isNew)
		qq = "insert into group_steps (group_id,step_id) values(?,?)";
	    else
		qq = "update group_steps set group_id=? where step_id=?";
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, group_id);
	    pstmt.setString(2, id);
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
	return back;

    }
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
	    qq = "delete from steps where id=?";
	    logger.debug(qq);
	    try{
		pstmt = con.prepareStatement(qq);
		pstmt.setString(1, id);
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
	
    //
    @Override
    public String doSelect(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select name,alias,inactive,field_name,field2_name,part_name,require_upload,suggested_upload_type  "+
	    "from steps where id=?";
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
		setName(rs.getString(1));
		setAlias(rs.getString(2));
		setInactive(rs.getString(3) != null);
		setField_name(rs.getString(4));
		setField2_name(rs.getString(5));
		setPart_name(rs.getString(6));
		setRequire_upload(rs.getString(7) != null);
		setSuggested_upload_type(rs.getString(8));
	    }
	    else{
		back = "Record "+id+" not found";
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
