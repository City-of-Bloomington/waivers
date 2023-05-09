package annex.model;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class Type extends CommonInc implements java.io.Serializable{

    String id="", name="", inactive="", table_name="waivers.groups";
    static final long serialVersionUID = 250L;	
    static Logger logger = LogManager.getLogger(Type.class);
    //
    public Type(){
	super();
    }	
    public Type(boolean deb, String val, String val2){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
    }
    public Type(boolean deb, String val, String val2, boolean val3){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
	setInactive(val3);
    }		
    public Type(boolean deb, String val, String val2, boolean val3, String val4){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
	setInactive(val3);
	setTable_name(val4);
    }		
		
    public Type(boolean deb, String val){
	//
	// initialize
	//
	debug = deb;
	setId(val);
    }
    public Type(String val){
	//
	setId(val);
    }
		
    //
    public Type(boolean deb){
	//
	// initialize
	//
	debug = deb;
    }
    public boolean equals(Object obj){
	if(obj instanceof Type){
	    Type one =(Type)obj;
	    return id.equals(one.getId());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 17;
	if(!table_name.equals("")){
	    seed += table_name.hashCode();
	}
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
    public String getName(){
	return name;
    }
    public boolean getInactive(){
	return !inactive.equals("");
    }
    public boolean isInactive(){
	return !inactive.equals("");
    }
    public boolean isActive(){
	return inactive.equals("");
    }		
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val.trim();
    }
    public void setInactive(boolean val){
	if(val)
	    inactive = "y";
    }		
    public void setTable_name(String val){
	if(val != null)
	    table_name = val;
    }		
    public String toString(){
	return name;
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,name,inactive "+
	    "from "+table_name+" where id=?";
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
		setName(rs.getString(2));
		setInactive(rs.getString(3) != null && !rs.getString(3).equals(""));
	    }
	    else{
		back ="Record "+id+" Not found";
		message = back;
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
	String qq = "insert into "+table_name+" values(0,?,null)";
	if(name.equals("")){
	    back = " name not set ";
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
	qq = "update "+table_name+" set name=?,inactive=? where id=?";
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,name);
	    if(inactive.equals(""))
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, "y");
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
	qq = "delete from "+table_name+" where id=?";
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    message="Deleted Successfully";
	    id="";
	    name="";
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
