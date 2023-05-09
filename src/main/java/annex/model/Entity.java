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
/**
 * Entity class
 *
 * holds info about owners, businesses, trusts, signers
 */
	 
public class Entity extends CommonInc{

    static Logger logger = LogManager.getLogger(Entity.class);
    static final long serialVersionUID = 220L;		
    String name = "", is_business="", is_trust="",
	title=""; // for business rep or trust executives
    String id = "", waiver_id=""; 
    final static String[] businessTypes = {"LLC","INC","CORP","LLP","CO.","LTD.","TRUST","ENTERPRISE","CONSTRUCTION","CHURCH","DEVELOPMENT"};
    final static String[] businessTitles = {"PRESIDENT","TRUSTEE","DIRECTOR","MANAGER","CHANCELLOR","SECRETARY"};
    //
    public Entity(){
	super(Helper.debug);
    }
    public Entity(boolean deb){
	//
	super(deb);
    }				
    public Entity(boolean deb, String val){
	//
	super(deb);
	setId(val);
    }		
    public Entity(boolean deb,
		  String val,
		  String val2, // name 
		  String val3, // title
		  boolean val4, // is business
		  boolean val5 // is trust
		  ){
	super(deb);
	setVals(val, val2, val3, val4, val5);
    }			
    void setVals(String val, String val2, String val3, boolean val4, boolean val5){
	setId(val);
	setName(val2);
	setTitle(val3);
	setIsBusiness(val4);
	setIsTrust(val5);
    }
	
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getName(){
	return name;
    }
    public String getTitle(){
	return title;
    }
    public boolean getIsBusiness(){
	return !is_business.equals("");
    }
    public boolean getIsTrust(){
	return !is_trust.equals("");
    }		
    public String getWaiver_id(){
	return waiver_id;
    }
    public String getInfo(){
	String ret = name;
	if(!title.equals("")){
	    ret += " ("+title+")";
	}
	else if(getIsBusiness()){
	    ret += " (Business)";
	}
	else if(getIsTrust()){
	    ret += " (Trust)";
	}
	return ret;
    }
    //		
    // setters
    //
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setTitle(String val){
	if(val != null)
	    title = val;
    }
    public void setIsBusiness(boolean val){
	if(val)
	    is_business="y";
    }
    public void setIsTrust(boolean val){
	if(val)
	    is_trust="y";
    }		
    public void setId(String val){
	if(val != null && !val.equals("-1"))
	    id = val;
    }
    public void setWaiver_id(String val){
	if(val != null && !val.equals("-1"))
	    waiver_id = val;
    }
    public boolean hasWaiver(){
	return !waiver_id.equals("");
    }
    public String toString(){
	return getName();
    }
    // remove entity from a waiver
    // also in waiver class
    public String doRemove(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
				
	String qq = " delete from entity_waivers where entity_id=? and waiver_id=? ";
	logger.debug("do remove ");
	if(id.equals("")){
	    back = " Entity ID not provided ";
	    return back;
	}
	if(waiver_id.equals("")){
	    back = " Waiver ID not provided ";
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

	    pstmt.setString(1, id);
	    pstmt.setString(2, waiver_id);
	    pstmt.executeUpdate();
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
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String qq = "insert into entities values(0,?,?,?,?)";
	String qq2 = "insert into entity_waivers values(?,?)";
	logger.debug("do save");
	if(name.trim().equals("")){
	    back = "name not set ";
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
	    if(name.equals(""))
		pstmt.setNull(1, Types.VARCHAR);
	    else
		pstmt.setString(1, name.trim());
	    if(title.equals(""))
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, title.trim());
	    if(is_business.equals(""))
		pstmt.setNull(3, Types.CHAR);
	    else
		pstmt.setString(3, "y");
	    if(is_trust.equals(""))
		pstmt.setNull(4, Types.CHAR);
	    else
		pstmt.setString(4, "y");						
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
	    if(!waiver_id.equals("")){
		qq = qq2;
		pstmt3 = con.prepareStatement(qq);
		if(debug){
		    logger.debug(qq);
		}
		pstmt3.setString(1, id);
		pstmt3.setString(2, waiver_id);
		pstmt3.executeUpdate();
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, rs,  pstmt, pstmt2, pstmt3);
	}
	return back;

    }
    public String doUpdate(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String str="";
	String qq = "", qq2="insert into entity_waivers values(?,?)";
	logger.debug("do update");		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	else{
	    logger.debug(qq);						
	    try{
		qq = "update entities set name=?,title=?,is_business=?,is_trust=? where id=?";
		pstmt = con.prepareStatement(qq);
		if(name.equals(""))
		    pstmt.setNull(1,Types.VARCHAR);
		else
		    pstmt.setString(1, name.trim());
		if(title.equals(""))
		    pstmt.setNull(2,Types.VARCHAR);
		else
		    pstmt.setString(2, title.trim());
		if(is_business.equals(""))
		    pstmt.setNull(3, Types.CHAR);
		else
		    pstmt.setString(3, "y");
		if(is_trust.equals(""))
		    pstmt.setNull(4, Types.CHAR);
		else
		    pstmt.setString(4, "y");									
		pstmt.setString(5, id);
		pstmt.executeUpdate();
		if(!waiver_id.equals("")){
		    qq = qq2;
		    pstmt2 = con.prepareStatement(qq);
		    if(debug){
			logger.debug(qq);
		    }
		    pstmt2.setString(1, id);
		    pstmt2.setString(2, waiver_id);
		    pstmt2.executeUpdate();
		}
	    }
	    catch(Exception ex){
		back += ex+":"+qq;
		logger.error(qq);
		addError(back);
	    }
	    finally{
		Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	    }
	}
	return back;

    }
    public String doSelect(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select name,title,is_business,is_trust "+
	    "from entities where id=?";
	logger.debug("do select");		
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
		setVals(id,
			rs.getString(1),
			rs.getString(2),
			rs.getString(3) != null,
			rs.getString(4) != null);
	    }
	    else{
		back = "entity "+id+" not found";
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
