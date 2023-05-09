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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.utils.*;

public class AddressList extends CommonInc{

    static Logger logger = LogManager.getLogger(AddressList.class);
    static final long serialVersionUID = 230L;
    String street_address = "", id="", waiver_id="", limit="limit 30";
    List<Address> addresses = null;
    public AddressList(){
	super();
    }
    public AddressList(boolean deb){
	super(deb);
    }		
    public AddressList(boolean deb, String val){
	super(deb);
	setWaiver_id(val);
    }
    public List<Address> getAddresses(){
	return addresses;
    }
    public void setStreetAddress(String val){
	if(val != null)
	    street_address = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setWaiver_id(String val){
	if(val != null)
	    waiver_id = val;
    }		
    public String getId(){
	return id;
    }
    public String getWaiver_id(){
	return waiver_id;
    }		
    public String getStreetAddress(){
	return street_address;
    }
    public void setNoLimit(){
	limit = "";
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	logger.debug(" in find ");
	String qq = "select u.id,u.street_address,u.waiver_id,u.invalid from addresses u ", qw ="";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	if(!id.equals("")){
	    qw += " u.id = ? ";
	}
	else{
	    if(!street_address.equals("")){
		qw += " u.street_address like ? ";
	    }
	}
	if(!waiver_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " u.waiver_id=? ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by u.street_address ";
	if(!limit.equals("")){
	    qq += limit;
	}
	try{
	    logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.equals("")){
		pstmt.setString(jj++, id);
	    }
	    else {
		if(!street_address.equals("")){
		    pstmt.setString(jj++,"%"+street_address+"%");
		}
	    }
	    if(!waiver_id.equals("")){
		pstmt.setString(jj++, waiver_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(addresses == null)
		    addresses = new ArrayList<>();
		Address one =
		    new Address(debug,
				rs.getString(1),
				rs.getString(2),
				rs.getString(3),
				rs.getString(4) != null);
		addresses.add(one);
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
    /* 
     * given certain address, find similar addresses in master_address app
     */
    public String findSimilarAddr(String url, String addr){
	//
	String back = "";
	String urlStr = url+"/?";
	String query="format=json;queryType=address;query=";
	logger.debug(" find similar addresses ");
	if(addr == null || addr.equals("")){
	    back = " No address set ";
	    return back;
	}
	DefaultHttpClient httpclient = new DefaultHttpClient();		
	ResponseHandler<String> responseHandler = new BasicResponseHandler();
	try{
	    query += java.net.URLEncoder.encode(addr, "UTF-8");
	    query +="+Bloomington;";
	    urlStr += query;
	    HttpGet httpget = new HttpGet(urlStr);
	    if(debug){
		logger.debug(urlStr);
	    }						
            String responseBody = httpclient.execute(httpget, responseHandler);
	    System.err.println(" response "+responseBody);
            logger.debug("----------------------------------------");
            logger.debug(responseBody);
            logger.debug("----------------------------------------");
	    JSONArray jArray = new JSONArray(responseBody);
	    addresses = new ArrayList<>();
	    for (int i = 0; i < jArray.length(); i++) {
		JSONObject jObj = jArray.getJSONObject(i);
		if(jObj.has("street_id")){
		    String street_id = jObj.getString("street_id");
		    String street = "";
		    String full_addr="";
		    if(!jObj.isNull("streetAddress")){
			street = jObj.getString("streetAddress");
			full_addr = street;
		    }
		    if(!jObj.isNull("subunits")){
			JSONArray jArr2 = jObj.getJSONArray("subunits");
			if(jArr2 != null){
			    for(int j=0;j<jArr2.length();j++){
				JSONObject jObj2 = jArr2.getJSONObject(j);
				if(!jObj2.isNull("id")){
				    full_addr = street;
				    Address one = new Address();
				    String type = jObj2.getString("type");;
				    String ident=jObj2.getString("identifier");
				    full_addr += " "+type;
				    full_addr += " "+ident;
				    one.setStreetAddress(full_addr);
				    addresses.add(one);
				}
			    }
			}
		    }
		    else{ // no subunit
			Address one = new Address();
			one.setStreetAddress(full_addr);
			addresses.add(one);
		    }
		}
	    }
	}
	catch(Exception ex){
	    back = ex+" "+urlStr;
	    logger.error(back);
	}
	finally{
	    // 
	    httpclient.getConnectionManager().shutdown();
	}
	return back;
    }
		
}






















































