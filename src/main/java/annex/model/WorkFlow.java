package annex.model;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class WorkFlow extends CommonInc{

    String id="", step_id="", next_step_id="";
    static final long serialVersionUID = 340L;	
    static Logger logger = LogManager.getLogger(WorkFlow.class);
    Step step = null;
    Step nextStep = null;
    //
    public WorkFlow(boolean deb){
	super(deb);
    }
    public WorkFlow(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }				
    public WorkFlow(boolean deb, String val, String val2){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setStep_id(val2);
    }

    public WorkFlow(boolean deb,
		    String val,
		    String val2,
		    String val3,
										
		    String val4,
		    String val5,
		    String val6,
		    String val7,
		    String val8,
		    boolean val9,
		    boolean val10,
		    String val11,

		    String val12,
		    String val13,
		    String val14,
		    String val15,
		    String val16,
		    boolean val17,
		    boolean val18,
		    String val19
		    ){
	super(deb);
	setValues(val, val2, val3,
		  val4, val5, val6, val7, val8, val9, val10, val11,
		  val12, val13, val14, val15, val16,val17, val18, val19);
    }

    private void setValues(
			   String val,
			   String val2,
			   String val3,
										
			   String val4,
			   String val5,
			   String val6,
			   String val7,
			   String val8,
			   boolean val9,
			   boolean val10,
			   String val11,
										
			   String val12,
			   String val13,
			   String val14,
			   String val15,
			   String val16,
			   boolean val17,
			   boolean val18,
			   String val19
			   ){
		
	setId(val);
	setStep_id(val2);
	setNext_step_id(val3);
		
	step =
	    new Step(debug,
		     step_id,
		     val4,
		     val5,
		     val9, // boolean
										 
		     val6,
		     val7,
		     val8,
		     val10, // boolean
		     val11); 

	nextStep =
	    new Step(debug,
		     next_step_id,
		     val12,
		     val13,										 
		     val17, // boolean
										 
		     val14,
		     val15,
		     val16,
		     val18, // boolean
		     val19);
    }
    public boolean equals(Object obj){
	if(obj instanceof WorkFlow){
	    WorkFlow one =(WorkFlow)obj;
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
    public String getStep_id(){
	return step_id;
    }
    public String getNext_step_id(){
	return next_step_id;
    }		
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setStep_id(String val){
	if(val != null)
	    step_id = val;
    }
    public void setNext_step_id(String val){
	if(val != null)
	    next_step_id = val;
    }		
    public String toString(){
	return id;
    }
    public Step getStep(){
	logger.debug(" get step ");
	if(step == null && !step_id.equals("")){
	    Step one = new Step(debug, step_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		step = one;
	    }
	}
	return step;
    }
    public Step getNextStep(){
	logger.debug(" get next step ");
	if(nextStep == null && !next_step_id.equals("")){
	    Step one = new Step(debug, next_step_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		nextStep = one;
	    }
	}
	return nextStep;
    }		
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select f.id,f.step_id,f.next_step_id,"+
	    "t.name,t.alias,t.field_name,t.field2_name,t.part_name,t.inactive,t.require_upload,t.suggested_upload_type, "+
	    "n.name,n.alias,n.field_name,n.field2_name,n.part_name,n.inactive,n.require_upload,n.suggested_upload_type "+
	    " from work_flows f,steps t,steps n where f.id=? "+
	    " and f.step_id=t.id "+
	    " and f.next_step_id = n.id ";
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
		setValues(rs.getString(1),
			  rs.getString(2),
			  rs.getString(3),
													
			  rs.getString(4),
			  rs.getString(5),								
			  rs.getString(6),
			  rs.getString(7),
			  rs.getString(8),
			  rs.getString(9) !=null,
			  rs.getString(10) !=null,
			  rs.getString(12),
													
			  rs.getString(12),
			  rs.getString(13),
			  rs.getString(14),
			  rs.getString(15),
			  rs.getString(16),
			  rs.getString(17) !=null,
			  rs.getString(18) !=null,
			  rs.getString(19)
			  );
	    }
	    else{
		back = " no match found "+id;
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
	String qq = "insert into work_flows values(0,?,?)";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,step_id);
	    pstmt.setString(2,next_step_id);						
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
		
    public String doUpdate(){
		
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
	qq = "update work_flows set step_id=?,next_step_id=? where id=?";
	logger.debug(qq);
	try{						
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,step_id);
	    pstmt.setString(2,next_step_id);
	    pstmt.setString(3,id);
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
	qq = "delete from work_flows where id=?";
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    message="Deleted Successfully";
	    id="";
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
