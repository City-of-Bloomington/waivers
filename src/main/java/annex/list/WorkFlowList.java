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

public class WorkFlowList extends CommonInc{

    static Logger logger = LogManager.getLogger(WorkFlowList.class);
    static final long serialVersionUID = 235L;
    String id="", step_id="", next_step_id="",  limit="limit 30";
    List<WorkFlow> workFlows = null;
    public WorkFlowList(){
	super();
    }
    public WorkFlowList(boolean deb){
	super(deb);
    }		
    public WorkFlowList(boolean deb, String val){
	super(deb);
	setId(val);
    }
    public List<WorkFlow> getWorkFlows(){
	return workFlows;
    }
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
    public String getId(){
	return id;
    }
    public String getStep_id(){
	return step_id;
    }		
    public String getNext_step_id(){
	return next_step_id;
    }


    public void setNoLimit(){
	limit = "";
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select f.id,f.step_id,f.next_step_id,"+
	    "t.name,t.alias,t.field_name,t.field2_name,t.part_name,t.inactive,t.require_upload,t.suggested_upload_type, "+
	    "n.name,n.alias,n.field_name,n.field2_name,n.part_name,n.inactive,n.require_upload,n.suggested_upload_type "+
	    " from work_flows f,steps t,steps n where "+
	    " f.step_id=t.id "+
	    " and f.next_step_id = n.id ";				
	Connection con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	if(!id.equals("")){
	    qq += " and f.id = ? ";
	}
	else{
	    if(!step_id.equals("")){
		qq += " and f.step_id = ? ";
	    }
	    if(!next_step_id.equals("")){
		qq += " and f.step_id = ? ";
	    }						
	}
	qq += " order by f.step_id ";
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
		if(!step_id.equals("")){
		    pstmt.setString(jj++,step_id);
		}
		if(!next_step_id.equals("")){
		    pstmt.setString(jj++,next_step_id);		
		}
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(workFlows == null)
		    workFlows = new ArrayList<>();
		WorkFlow one =
		    new WorkFlow(debug,
				 rs.getString(1),
				 rs.getString(2),
				 rs.getString(3),
													
				 rs.getString(4),
				 rs.getString(5),								
				 rs.getString(6),
				 rs.getString(7),
				 rs.getString(8),
				 rs.getString(9) !=null,
				 rs.getString(10) !=null,
				 rs.getString(11),

				 rs.getString(12),
				 rs.getString(13),
				 rs.getString(14),
				 rs.getString(15),
				 rs.getString(16),
				 rs.getString(17) !=null,
				 rs.getString(18) !=null,
				 rs.getString(19)
				 );
		workFlows.add(one);
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






















































