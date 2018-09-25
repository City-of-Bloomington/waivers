package annex;
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
import org.apache.log4j.Logger;

public class Address extends CommonInc{

		String street_address = "";
		// street_num ex: (10), (10-15), (10,12,14)
		String street_num="", street_name="";
		String id = "", waiver_id="", invalid="";
		String address_to_verify = "";
		static Logger logger = Logger.getLogger(Address.class);
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
						if(debug){
								logger.debug(qq);
						}
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
		/**
			 asm report, audit, unused owner records;

SELECT DISTINCT o.ID, o.OwnerName,o.OwnerAddress, o.OwnerTown, o.OwnerCounty, o.OwnerPostcode, 
o.HomeTelephone, o.WorkTelephone, o.MobileTelephone, o.EmailAddress, o.OwnerTitle, o.OwnerForenames,
o.OwnerSurname, o.MembershipExpiryDate, o.AdditionalFlags
FROM owner o
WHERE o.ID in 
NOT EXISTS(SELECT ID FROM adoption WHERE OwnerID = o.ID OR ReturnedByOwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM animal WHERE OriginalOwnerID = o.ID OR BroughtInByOwnerID = o.ID OR CurrentVetID = o.ID OR OwnersVetID = o.ID 
    OR AdoptionCoordinatorID = o.ID OR NeuteredByVetID = o.ID)
AND NOT EXISTS(SELECT ID FROM animalcontrol WHERE OwnerID = o.ID OR Owner2ID = o.ID OR Owner3ID = o.ID)
AND NOT EXISTS(SELECT ID FROM animalfound WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM animallost WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM animalwaitinglist WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM animaltransport WHERE DriverOwnerID = o.ID OR PickupOwnerID = o.ID OR DropoffOwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM animalmedicaltreatment WHERE AdministeringVetID = o.ID)
AND NOT EXISTS(SELECT ID FROM animaltest WHERE AdministeringVetID = o.ID)
AND NOT EXISTS(SELECT ID FROM animalvaccination WHERE AdministeringVetID = o.ID)
AND NOT EXISTS(SELECT ID FROM clinicappointment WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM ownercitation WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM ownerdonation WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM ownerinvestigation WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM ownerlicence WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM ownerrota WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM ownertraploan WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM ownervoucher WHERE OwnerID = o.ID)
AND NOT EXISTS(SELECT ID FROM users WHERE OwnerID = o.ID)
ORDER BY o.OwnerName

=============HTML====

$$HEADER
<table border="1">
<tr>
<th>Name</th>
<th>Address</th>
<th>Home</th>
<th>Work</th>
<th>Mobile</th>
<th>Email</th>
<th>Flags</th>
</tr>
HEADER$$

$$BODY
<tr>
<td><a target="_blank" href="person?id=$ID">$OWNERNAME</a></td>
<td>$OWNERADDRESS<br/>
$OWNERTOWN, $OWNERCOUNTY $OWNERPOSTCODE</td>
<td>$HOMETELEPHONE</td>
<td>$WORKTELEPHONE</td>
<td>$MOBILETELEPHONE</td>
<td>$EMAILADDRESS</td>
<td>$ADDITIONALFLAGS</td>
</tr>
BODY$$

$$FOOTER
</table>
FOOTER$$

			 
			 SELECT DISTINCT o.ID FROM owner o WHERE 
     NOT EXISTS(SELECT ID FROM adoption WHERE OwnerID = o.ID OR ReturnedByOwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM animal WHERE OriginalOwnerID = o.ID OR BroughtInByOwnerID = o.ID OR CurrentVetID = o.ID OR OwnersVetID = o.ID 
    OR AdoptionCoordinatorID = o.ID OR NeuteredByVetID = o.ID)
    AND NOT EXISTS(SELECT ID FROM animalcontrol WHERE OwnerID = o.ID OR Owner2ID = o.ID OR Owner3ID = o.ID)
    AND NOT EXISTS(SELECT ID FROM animalfound WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM animallost WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM animalwaitinglist WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM animaltransport WHERE DriverOwnerID = o.ID OR PickupOwnerID = o.ID OR DropoffOwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM animalmedicaltreatment WHERE AdministeringVetID = o.ID)
    AND NOT EXISTS(SELECT ID FROM animaltest WHERE AdministeringVetID = o.ID)
    AND NOT EXISTS(SELECT ID FROM animalvaccination WHERE AdministeringVetID = o.ID)
    AND NOT EXISTS(SELECT ID FROM clinicappointment WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM ownercitation WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM ownerdonation WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM ownerinvestigation WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM ownerlicence WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM ownerrota WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM ownertraploan WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM ownervoucher WHERE OwnerID = o.ID)
    AND NOT EXISTS(SELECT ID FROM users WHERE OwnerID = o.ID)
    ORDER BY o.ID

			 SELECT DISTINCT o.ID FROM owner o WHERE  NOT EXISTS(SELECT ID FROM adoption WHERE OwnerID = o.ID OR ReturnedByOwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM animal WHERE OriginalOwnerID = o.ID OR BroughtInByOwnerID = o.ID OR CurrentVetID = o.ID OR OwnersVetID = o.ID  OR AdoptionCoordinatorID = o.ID OR NeuteredByVetID = o.ID)  AND NOT EXISTS(SELECT ID FROM animalcontrol WHERE OwnerID = o.ID OR Owner2ID = o.ID OR Owner3ID = o.ID)  AND NOT EXISTS(SELECT ID FROM animalfound WHERE OwnerID = o.ID)   AND NOT EXISTS(SELECT ID FROM animallost WHERE OwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM animalwaitinglist WHERE OwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM animaltransport WHERE DriverOwnerID = o.ID OR PickupOwnerID = o.ID OR DropoffOwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM animalmedicaltreatment WHERE AdministeringVetID = o.ID) AND NOT EXISTS(SELECT ID FROM animaltest WHERE AdministeringVetID = o.ID)  AND NOT EXISTS(SELECT ID FROM animalvaccination WHERE AdministeringVetID = o.ID) AND NOT EXISTS(SELECT ID FROM clinicappointment WHERE OwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM ownercitation WHERE OwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM ownerdonation WHERE OwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM ownerinvestigation WHERE OwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM ownerlicence WHERE OwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM ownerrota WHERE OwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM ownertraploan WHERE OwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM ownervoucher WHERE OwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM users WHERE OwnerID = o.ID)

 SELECT count(o.ID) FROM owner o WHERE  NOT EXISTS(SELECT ID FROM adoption WHERE OwnerID = o.ID OR ReturnedByOwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM animal WHERE OriginalOwnerID = o.ID OR BroughtInByOwnerID = o.ID OR CurrentVetID = o.ID OR OwnersVetID = o.ID  OR AdoptionCoordinatorID = o.ID OR NeuteredByVetID = o.ID)  AND NOT EXISTS(SELECT ID FROM animalcontrol WHERE OwnerID = o.ID OR Owner2ID = o.ID OR Owner3ID = o.ID)  AND NOT EXISTS(SELECT ID FROM animalfound WHERE OwnerID = o.ID)   AND NOT EXISTS(SELECT ID FROM animallost WHERE OwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM animalwaitinglist WHERE OwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM animaltransport WHERE DriverOwnerID = o.ID OR PickupOwnerID = o.ID OR DropoffOwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM animalmedicaltreatment WHERE AdministeringVetID = o.ID) AND NOT EXISTS(SELECT ID FROM animaltest WHERE AdministeringVetID = o.ID)  AND NOT EXISTS(SELECT ID FROM animalvaccination WHERE AdministeringVetID = o.ID) AND NOT EXISTS(SELECT ID FROM clinicappointment WHERE OwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM ownercitation WHERE OwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM ownerdonation WHERE OwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM ownerinvestigation WHERE OwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM ownerlicence WHERE OwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM ownerrota WHERE OwnerID = o.ID)  AND NOT EXISTS(SELECT ID FROM ownertraploan WHERE OwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM ownervoucher WHERE OwnerID = o.ID) AND NOT EXISTS(SELECT ID FROM users WHERE OwnerID = o.ID)			 

 select count(oo.id) from (
 select o.ID as id FROM owner o left join adoption a  on a.OwnerID = o.ID where a.OwnerId is null
 intersect
 select o.ID as id FROM owner o left join adoption a  on a.ReturnedByOwnerID = o.ID where a.ReturnedByOwnerId is null
 intersect
 select o.ID as id FROM owner o left join animalfound a on a.OwnerID = o.ID where a.OwnerID is null
 intersect 
 select o.ID as id FROM owner o left join animallost a on a.OwnerID = o.ID where a.OwnerID is null
  intersect 
	select o.ID as id FROM owner o left join animalwaitinglist a on a.OwnerID = o.ID where a.OwnerID is null
  intersect 
	select o.ID as id FROM owner o left join animaltest a on a.AdministeringVetID = o.ID where a.AdministeringVetID is null
	intersect 
	select o.ID as id FROM owner o left join animalmedicaltreatment a on a.AdministeringVetID = o.ID where a.AdministeringVetID is null
  intersect 
	select o.ID as id FROM owner o left join animalvaccination a on a.AdministeringVetID = o.ID	where a.AdministeringVetID is null
	intersect 
	select o.ID as id FROM owner o left join clinicappointment a on a.OwnerID = o.ID where a.OwnerID is null
	intersect 
	select o.ID as id FROM owner o left join ownercitation a on a.OwnerID = o.ID where a.OwnerID is null
	intersect 
	select o.ID as id FROM owner o left join 	ownerdonation a on a.OwnerID = o.ID and a.OwnerID  is null
	intersect 
	select o.ID as id FROM owner o left join ownerinvestigation a on a.OwnerID = o.ID where a.OwnerID is null
	intersect 
	select o.ID as id FROM owner o left join ownerlicence a on a.OwnerID = o.ID where a.OwnerID is null
	intersect 
	select o.ID as id FROM owner o left join ownerrota a on a.OwnerID = o.ID where a.OwnerID is null
	intersect
	select o.ID as id FROM owner o left join ownertraploan a on a.OwnerID = o.ID where a.OwnerID is null
	intersect
	select o.ID as id FROM owner o left join 	ownervoucher a on a.OwnerID = o.ID where a.OwnerID is null
	intersect
	select o.ID as id FROM owner o left join	users a  on a.OwnerID = o.ID where a.OwnerID is null
	intersect
	select o.ID as id FROM owner o left join	animal a on a.OriginalOwnerID = o.ID
	where a.OriginalOwnerID is null
	intersect
	select o.ID as id FROM owner o left join	animal a on a.BroughtInByOwnerID = o.ID where a.BroughtInByOwnerID is null
	intersect
	select o.ID as id FROM owner o left join	animal a on a.CurrentVetID = o.ID where a.CurrentVetID is null
	intersect
	select o.ID as id FROM owner o left join	animal a on a.OwnersVetID = o.ID where a.OwnersVetID is null
	intersect
	select o.ID as id FROM owner o left join	animal a on a.AdoptionCoordinatorID = o.ID where a.AdoptionCoordinatorID is null
	intersect
	select o.ID as id FROM owner o left join	animal a on a.NeuteredByVetID = o.ID where a.NeuteredByVetID is null
	intersect
	select o.ID as id FROM owner o left join	animalcontrol a on a.OwnerID = o.ID where a.OwnerID is null
	intersect
	select o.ID as id FROM owner o left join	animalcontrol a on a.Owner2ID = o.ID
  where a.Owner2ID is null
	intersect
	select o.ID as id FROM owner o left join	animalcontrol a on a.Owner3ID = o.ID
  where a.Owner3ID is null
	intersect
	select o.ID as id FROM owner o left join animaltransport a on a.DriverOwnerID = o.ID where a.DriverOwnerID is null
		intersect
	select o.ID as id FROM owner o left join animaltransport a on a.PickupOwnerID = o.ID where a.PickupOwnerID is null
		 intersect
		 select o.ID as id FROM owner o left join animaltransport a on a.DropoffOwnerID = o.ID where a.DropoffOwnerID is null) oo


		 
SELECT DISTINCT o.ID, o.OwnerName,o.OwnerAddress, o.OwnerTown, o.OwnerCounty, o.OwnerPostcode, o.HomeTelephone, o.WorkTelephone, o.MobileTelephone, o.EmailAddress, o.OwnerTitle, o.OwnerForenames,o.OwnerSurname, o.MembershipExpiryDate, o.AdditionalFlags
FROM owner o
WHERE o.ID in (
select o.ID as id FROM owner o left join adoption a  on a.OwnerID = o.ID where a.OwnerId is null
intersect
select o.ID as id FROM owner o left join adoption a  on a.ReturnedByOwnerID = o.ID where a.ReturnedByOwnerId is null
intersect
select o.ID as id FROM owner o left join animalfound a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join animallost a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join animalwaitinglist a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join animaltest a on a.AdministeringVetID = o.ID where a.AdministeringVetID is null
intersect
select o.ID as id FROM owner o left join animalmedicaltreatment a on a.AdministeringVetID = o.ID where a.AdministeringVetID is null
intersect
select o.ID as id FROM owner o left join animalvaccination a on a.AdministeringVetID = o.ID	where a.AdministeringVetID is null
intersect
select o.ID as id FROM owner o left join clinicappointment a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join ownercitation a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join 	ownerdonation a on a.OwnerID = o.ID and a.OwnerID  is null
intersect
select o.ID as id FROM owner o left join ownerinvestigation a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join ownerlicence a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join ownerrota a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join ownertraploan a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join 	ownervoucher a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join	users a  on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join	animal a on a.OriginalOwnerID = o.ID where a.OriginalOwnerID is null
intersect
select o.ID as id FROM owner o left join	animal a on a.BroughtInByOwnerID = o.ID where a.BroughtInByOwnerID is null
intersect
select o.ID as id FROM owner o left join	animal a on a.CurrentVetID = o.ID where a.CurrentVetID is null
intersect
select o.ID as id FROM owner o left join	animal a on a.OwnersVetID = o.ID where a.OwnersVetID is null
intersect
select o.ID as id FROM owner o left join	animal a on a.AdoptionCoordinatorID = o.ID where a.AdoptionCoordinatorID is null
intersect
select o.ID as id FROM owner o left join	animal a on a.NeuteredByVetID = o.ID where a.NeuteredByVetID is null
intersect
select o.ID as id FROM owner o left join	animalcontrol a on a.OwnerID = o.ID where a.OwnerID is null
intersect
select o.ID as id FROM owner o left join	animalcontrol a on a.Owner2ID = o.ID  where a.Owner2ID is null
intersect
select o.ID as id FROM owner o left join	animalcontrol a on a.Owner3ID = o.ID  where a.Owner3ID is null
intersect
select o.ID as id FROM owner o left join animaltransport a on a.DriverOwnerID = o.ID where a.DriverOwnerID is null
intersect
select o.ID as id FROM owner o left join animaltransport a on a.PickupOwnerID = o.ID where a.PickupOwnerID is null
intersect
select o.ID as id FROM owner o left join animaltransport a on a.DropoffOwnerID = o.ID where a.DropoffOwnerID is null)
ORDER BY o.OwnerName		 


SELECT DISTINCT o.ID, o.OwnerName,o.OwnerAddress, o.OwnerTown, o.OwnerCounty, o.OwnerPostcode, o.HomeTelephone, o.WorkTelephone, o.MobileTelephone, o.EmailAddress, o.OwnerTitle, o.OwnerForenames,o.OwnerSurname, o.MembershipExpiryDate, o.AdditionalFlags,
a1.OwnerID,
a2.ReturnedByOwnerID,
a3.OwnerID,
a4.OwnerID,
a5.OwnerID,
a6.AdministeringVetID,
a7.AdministeringVetID,
a8.AdministeringVetID,
a9.OwnerID,
a10.OwnerID,
a11.OwnerID,
a12.OwnerID,
a13.OwnerID,
a14.OwnerID,
a15.OwnerID,
a16.OwnerID,
a17.OwnerID,
a18.OriginalOwnerID,
a19.BroughtInByOwnerID,
a20.CurrentVetID,
a21.OwnersVetID,
a22.AdoptionCoordinatorID,
a23.NeuteredByVetID,
a24.OwnerID,
a25.Owner2ID,
a26.Owner3ID,
a27.DriverOwnerID,
a28.PickupOwnerID,
a29.DropoffOwnerID 
FROM owner o
left join adoption a1  on a1.OwnerID = o.ID 
left join adoption a2  on a2.ReturnedByOwnerID = o.ID 
left join animalfound a3 on a3.OwnerID = o.ID 
left join animallost a4 on a4.OwnerID = o.ID 
left join animalwaitinglist a5 on a5.OwnerID = o.ID 
left join animaltest a6 on a6.AdministeringVetID = o.ID 
left join animalmedicaltreatment a7 on a7.AdministeringVetID = o.ID 
left join animalvaccination a8 on a8.AdministeringVetID = o.ID	
left join clinicappointment a9 on a9.OwnerID = o.ID 
left join ownercitation a10 on a10.OwnerID = o.ID 
left join ownerdonation a11 on a11.OwnerID = o.ID 
left join ownerinvestigation a12 on a12.OwnerID = o.ID 
left join ownerlicence a13 on a13.OwnerID = o.ID 
left join ownerrota a14 on a14.OwnerID = o.ID 
left join ownertraploan a15 on a15.OwnerID = o.ID 
left join ownervoucher a16 on a16.OwnerID = o.ID 
left join	users a17  on a17.OwnerID = o.ID 
left join	animal a18 on a18.OriginalOwnerID = o.ID 
left join	animal a19 on a19.BroughtInByOwnerID = o.ID 
left join	animal a20 on a20.CurrentVetID = o.ID 
left join	animal a21 on a21.OwnersVetID = o.ID 
left join	animal a22 on a22.AdoptionCoordinatorID = o.ID 
left join	animal a23 on a23.NeuteredByVetID = o.ID 
left join	animalcontrol a24 on a24.OwnerID = o.ID 
left join	animalcontrol a25 on a25.Owner2ID = o.ID 
left join	animalcontrol a26 on a26.Owner3ID = o.ID  
left join animaltransport a27 on a27.DriverOwnerID = o.ID 
left join animaltransport a28 on a28.PickupOwnerID = o.ID 
left join animaltransport a29 on a29.DropoffOwnerID = o.ID
where
a1.OwnerID is null and
a2.ReturnedByOwnerID is null and
a3.OwnerID is null and
a4.OwnerID is null and
a5.OwnerID is null and
a6.AdministeringVetID is null and
a7.AdministeringVetID is null and
a8.AdministeringVetID is null and
a9.OwnerID is null and
a10.OwnerID is null and
a11.OwnerID is null and
a12.OwnerID is null and
a13.OwnerID is null and
a14.OwnerID is null and
a15.OwnerID is null and 
a16.OwnerID is null and
a17.OwnerID is null and
a18.OriginalOwnerID is null and
a19.BroughtInByOwnerID is null and
a20.CurrentVetID is null and 
a21.OwnersVetID is null and
a22.AdoptionCoordinatorID is null and
a23.NeuteredByVetID is null and
a24.OwnerID is null and
a25.Owner2ID is null and
a26.Owner3ID is null and
a27.DriverOwnerID is null and
a28.PickupOwnerID is null and
a29.DropoffOwnerID is null 
ORDER BY o.OwnerName		 

		 */
}
