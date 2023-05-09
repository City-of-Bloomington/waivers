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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class Address extends CommonInc{

    String street_address = "";
    // street_num ex: (10), (10-15), (10,12,14)
    String street_num="", street_name="";
    String id = "", waiver_id="", invalid="";
    String address_to_verify = "";
    static Logger logger = LogManager.getLogger(Address.class);
    static final long serialVersionUID = 220L;			
    //
    public Address(){
	super(Helper.debug);
    }
    public Address(boolean deb){
	//
	super(deb);
    }				
    public Address(boolean deb, String val){
	//
	super(deb);
	setId(val);
    }
    // we need this for list
    public Address(boolean deb,
		   String val,
		   String val2,
		   String val3,
		   boolean val4){
	//
	// 
	//
	super(deb);
	setVals(val, val2, val3, null, null, val4);
    }					
    public Address(boolean deb,
		   String val, String val2,
		   String val3, String val4,
		   String val5, 
		   boolean val6){
	//
	// initialize
	//
	super(deb);
	setVals(val, val2, val3, val4, val5, val6);
    }			
    void setVals(String val, String val2,
		 String val3, String val4,
		 String val5, 
		 boolean val6){
	setId(val);
	setStreetAddress(val2);
	setWaiver_id(val3);
	setStreetNum(val4);
	setStreetName(val5);
	setInvalid(val6);
    }
	
    //
    // getters
    //
    public String getId(){
	return id;
    }
	 
    public String getStreetAddress(){
	if(street_address.equals("")){
	    if(!street_num.equals(""))
		street_address += street_num;
	    if(!street_name.equals("")){
		if(!street_address.equals("")) street_address += " ";
		street_address += street_name;
	    }
	}
	return street_address;
    }
    public boolean canVerify(){
	prepareAddressToVerify();
	return !address_to_verify.equals("");
    }
    /**
     * we do not verify street number range or list such as
     * 401,403 N Morton St, or 401-405 N Morton St, then we just
     * verify N Morton St instead
     * but we can verify 401 N Morton St
     */
    void prepareAddressToVerify(){
	address_to_verify = "";
	if(!street_num.equals("")){
	    if(street_num.indexOf(",") == -1 &&
	       street_num.indexOf("-") == -1){
		address_to_verify += street_num;
	    }
	}
	if(!street_name.equals("")){
	    if(!address_to_verify.equals(""))
		address_to_verify += " ";
	    address_to_verify += street_name;
	}				
    }
    public String getAddressToVerify(){
	return address_to_verify;
    }
    public String getStreetNum(){
	return street_num;
    }
    public String getStreetName(){
	return street_name;
    }		
    public String getWaiver_id(){
	return waiver_id;
    }
    public boolean getInvalid(){
	return !invalid.equals("");
    }
    public boolean isValid(){
	return invalid.equals("");
    }		
    public boolean hasStreetAddress(){
	getStreetAddress();
	return !street_address.equals("");
    }
    public String getAddrCombo(){
	String ret=street_num+"_"+street_name+"_"+street_address;
	return ret;
    }
    public void setAddrCombo(String val){
	if(val != null && val.indexOf("_") > -1){
	    String[] vals = val.split("_");
	    if(vals != null){
		if(vals.length == 3){
		    street_num = vals[0];
		    street_name = vals[1];
		    street_address = vals[2];
		}
		else if(vals.length == 2){
		    street_name = vals[1];
		    street_address = vals[2];
		}								
		else if(vals.length == 1){
		    street_address = vals[1];
		}
	    }
	}
    }
    //		
    // setters
    //
    public void setStreetAddress(String val){
	if(val != null)
	    street_address = val.trim();
    }
    public void setAddressInfo(String val){
	// needed for interface only
    }		
    public void setStreetNum(String val){
	if(val != null)
	    street_num = val.trim();
    }
    public void setStreetName(String val){
	if(val != null)
	    street_name = val.trim();
    }		
    public void setId(String val){
	if(val != null && !val.equals("-1"))
	    id = val;
    }
    public void setWaiver_id(String val){
	if(val != null && !val.equals("-1"))
	    waiver_id = val;
    }
    public void setInvalid(boolean val){
	if(val)
	    invalid="y";
    }
    public boolean hasWaiver(){
	return !waiver_id.equals("");
    }
    public boolean hasId(){
	return !id.equals("");
    }
    public String getAddressInfo(){
	getStreetAddress();
	String ret = street_address;
	if(getInvalid()) ret += " (Invalid)";
	return ret;
    }
    public String toString(){
	return getStreetAddress();
    }
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	logger.debug(" in do save ");				
	String qq = "insert into addresses values(0,?,?,?,?, ?)";
	if(street_address.equals("")){
	    if(!street_num.equals(""))
		street_address += street_num;
	    if(!street_name.equals("")){
		if(!street_address.equals("")) street_address += " ";
		street_address += street_name;
	    }
	}
	if(street_address.equals("")){
	    back = "address not set set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	try{
	    con = Helper.getConnection();
	    if(con == null){
		back = "Could not connect to DB";
		addError(back);
		return back;
	    }
	    pstmt = con.prepareStatement(qq);
	    logger.debug(qq);
	    if(street_address.equals(""))
		pstmt.setNull(1,Types.VARCHAR);
	    else
		pstmt.setString(1, street_address.trim());
	    if(waiver_id.equals(""))
		pstmt.setNull(2, Types.INTEGER);
	    else								
		pstmt.setString(2, waiver_id);
	    if(street_num.equals(""))
		pstmt.setNull(3,Types.INTEGER);
	    else
		pstmt.setString(3, street_num);
	    if(street_name.equals(""))
		pstmt.setNull(4,Types.INTEGER);
	    else
		pstmt.setString(4, street_name);
	    if(invalid.equals(""))
		pstmt.setNull(5,Types.CHAR);
	    else
		pstmt.setString(5, "y");						
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
	return back;

    }
    public String doUpdate(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="", qq="";
	logger.debug(" in do update ");		
	qq = "update addresses set street_address=?,street_num=?,street_name=?,invalid=? where id=?";				
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	else{
	    try{
		if(debug){
		    logger.debug(qq);
		}
		pstmt = con.prepareStatement(qq);
		if(street_address.equals(""))
		    pstmt.setNull(1,Types.VARCHAR);
		else
		    pstmt.setString(1, street_address.trim());
		if(street_num.equals(""))
		    pstmt.setNull(2,Types.INTEGER);
		else
		    pstmt.setString(2, street_num);
		if(street_name.equals(""))
		    pstmt.setNull(3,Types.INTEGER);
		else
		    pstmt.setString(3, street_name);
		if(invalid.equals(""))
		    pstmt.setNull(4,Types.CHAR);
		else
		    pstmt.setString(4, "y");
		pstmt.setString(5, id);
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
	}
	return back;

    }
    public String doDelete(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="", qq="";
	logger.debug(" in do delete ");		
	qq = "delete from addresses where id=?";				
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	else{
	    try{
		if(debug){
		    logger.debug(qq);
		}
		pstmt = con.prepareStatement(qq);
		pstmt.setString(1, id);
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
	}
	return back;

    }
    /**
     * remove the address from a waiver, but no delete
     */
    public String doRemove(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="", qq="";
	logger.debug(" in do remove ");		
	qq = "update addresses set waiver_id=null where id=? and waiver_id=?";

	if(id.equals("")){
	    back = "address ID not provided";
	    return back;
	}
	if(waiver_id.equals("")){
	    back = "waiver ID not provided";
	    return back;
	}				
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	else{
	    try{
		if(debug){
		    logger.debug(qq);
		}
		pstmt = con.prepareStatement(qq);
		pstmt.setString(1, id);
		pstmt.setString(2, waiver_id);
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
	}
	return back;

    }				
    public String doSelect(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select street_address,waiver_id,street_num,street_name,invalid  "+
	    "from addresses where id=?";
	logger.debug(" in do select ");		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }				
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setVals(id,
			rs.getString(1),
			rs.getString(2),
			rs.getString(3),
			rs.getString(4),
			rs.getString(5) != null);
	    }
	    else{
		back = "address "+id+" not found";
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
    //
    synchronized boolean hasMasterAddressInfo(String url2){

	boolean ret = false;
	String url = "";
	logger.debug(" master address ");		
	DefaultHttpClient httpclient = new DefaultHttpClient();		
	try{
	    url = url2+"/locations/verify.php?format=json&address="+java.net.URLEncoder.encode(street_address, "UTF-8")+"+Bloomington";
	    // System.err.println("url "+url);
	    // System.err.println(getAddress());
	    HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            logger.debug("----------------------------------------");
            logger.debug(responseBody);
            logger.debug("----------------------------------------");
	    JSONObject jObj = new JSONObject(responseBody);
	    if(jObj.has("street_address_id")){
		// ma_address_id = jObj.getString("street_address_id");
		// System.err.println(" street_address_id: "+street_address_id);
		/*
		  if(!jObj.isNull("id")){
		  ma_subunit_id = jObj.getString("id");
		  // System.err.println(" subunit_id: "+subunit_id);
		  }
		*/
		if(!jObj.isNull("addressString")){
		    street_address = jObj.getString("addressString");
		    // System.err.println(" Address: "+streetAddress);
		}
		invalid = "";
		ret = true;
	    }
			
	}
	catch(Exception ex){
	    logger.error(" "+ex+":"+street_address);
	}
	finally{
	    // 
	    // shut down the connection manager to ensure
	    // immediate deallocation of all system resources
	    //
	    httpclient.getConnectionManager().shutdown();
	}
	return ret;
    }		

}
