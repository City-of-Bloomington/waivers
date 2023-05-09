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

public class StepList extends CommonInc{

    static Logger logger = LogManager.getLogger(StepList.class);
    static final long serialVersionUID = 230L;
    String field_name = "", id="", name="", limit="limit 30";
    String group_id = "", exclude_step=""; // by name
    List<Step> steps = null;
    public StepList(){
	super();
    }
    public StepList(boolean deb){
	super(deb);
    }		
    public StepList(boolean deb, String val){
	super(deb);
	setName(val);
    }
    public List<Step> getSteps(){
	return steps;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setField_name(String val){
	if(val != null)
	    field_name = val;
    }		
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setGroup_id(String val){
	if(val != null)
	    group_id = val;
    }
    public void setExclude_step(String val){
	if(val != null)
	    exclude_step = val;
    }		
    public String getId(){
	return id;
    }
    public String getGroup_id(){
	return group_id;
    }		
    public String getName(){
	return name;
    }
    public String getField_name(){
	return field_name;
    }		

    public void setNoLimit(){
	limit = "";
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select u.id,u.name,u.alias,u.inactive,u.field_name,u.field2_name,u.part_name,require_upload,suggested_upload_type from steps u ", qw ="";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	if(!id.equals("")){
	    qw += " u.id = ? ";
	}
	else{
	    if(!name.equals("")){
		qw += " u.name = ? ";
	    }
	    if(!exclude_step.equals("")){
		qw += " u.name != ? ";
	    }						
	    if(!field_name.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " u.field_name = ? ";
	    }
						
	    if(!group_id.equals("")){
		qq += ", group_steps gt ";
		if(!qw.equals("")) qw += " and ";
		qw += " u.id=gt.step_id and gt.group_id=? ";								
	    }
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by u.id ";
	if(!limit.equals("")){
	    qq += limit;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    if(!id.equals("")){
		pstmt.setString(1, id);
	    }
	    else {
		int jj=1;
		if(!name.equals("")){
		    pstmt.setString(jj++,name);
		}
		if(!exclude_step.equals("")){
		    pstmt.setString(jj++, exclude_step);
		}
		if(!field_name.equals("")){
		    pstmt.setString(jj++,field_name);		
		}
		if(!group_id.equals("")){
		    pstmt.setString(jj++, group_id);
		}								
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(steps == null)
		    steps = new ArrayList<Step>();
		Step one =
		    new Step(debug,
			     rs.getString(1),
			     rs.getString(2),
			     rs.getString(3),
			     rs.getString(4) != null,
			     rs.getString(5),
			     rs.getString(6),
			     rs.getString(7),
			     rs.getString(8) != null,
			     rs.getString(9));

		steps.add(one);
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
    /**
     * find startStep which is the first in the list and is active
     */
    public Step findStartStep(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Step step = null;
	Connection con = Helper.getConnection();
	String qq = "select u.id,u.name,u.alias,u.inactive,u.field_name,u.field2_name,u.part_name,u.require_upload,u.suggested_upload_type from steps u  where u.inactive is null order by u.id asc ";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return null;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		Step one =
		    new Step(debug,
			     rs.getString(1),
			     rs.getString(2),
			     rs.getString(3),
			     rs.getString(4) != null,
			     rs.getString(5),
														 
			     rs.getString(6),
			     rs.getString(7),
			     rs.getString(8) != null,
			     rs.getString(9));

		step = one;
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
	return step;
    }		
}






















































