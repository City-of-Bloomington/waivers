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

public class EntityList extends CommonInc{

    static Logger logger = LogManager.getLogger(EntityList.class);
    static final long serialVersionUID = 230L;
    String name = "", id="", waiver_id="", limit="limit 30";
    boolean businessOnly=false, trustOnly=false, peopleOnly=false;
    List<Entity> entities = null;
    public EntityList(){
	super();
    }
    public EntityList(boolean deb){
	super(deb);
    }		
    public EntityList(boolean deb, String val){
	super(deb);
	setName(val);
    }
    public List<Entity> getEntities(){
	return entities;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
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
    public String getName(){
	return name;
    }
    public void setBusinessOnly(){
	businessOnly = true;
    }
    public void setTrustOnly(){
	trustOnly = true;
    }
    public void setPeopleOny(){
	peopleOnly = true;
    }
    public void setNoLimit(){
	limit = "";
    }
    /*
      select u.id,u.name,u.title,u.is_business,u.is_trust from entities u where (is_business is not null or is_trust is not null) union  select u.id,u.name,u.title,u.is_business,u.is_trust from entities u where (is_business is null and is_trust is null and title is not null) union select u.id,u.name,u.title,u.is_business,u.is_trust from entities u where (is_business is null and is_trust is null and title is null) 			
      *
      */
    public String findForWaiver(){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select u.id,u.name,u.title,u.is_business,u.is_trust from entities u, entity_waivers ow where ow.entity_id=u.id and ow.waiver_id=? ";
	logger.debug("find for waiver");
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	// business or trust
	String qq2 = qq;
	qq2 += " and (is_business is not null or is_trust is not null) "; 

	// manager or executer
	String qq3 = qq; // with title
	qq3 += " and is_business is null and is_trust is null and title is not null ";
	// all others
	String qq4 = qq; // with title
	qq4 += " and is_business is null and is_trust is null and title is null order by name ";				
	qq = "("+qq2+") union ("+qq3+") union ("+qq4+")";
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(jj++, waiver_id);
	    pstmt.setString(jj++, waiver_id);
	    pstmt.setString(jj++, waiver_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(entities == null)
		    entities = new ArrayList<>();
		Entity one =
		    new Entity(debug,
			       rs.getString(1),
			       rs.getString(2),
			       rs.getString(3),
			       rs.getString(4) != null,
			       rs.getString(5) != null);

		entities.add(one);
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
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select u.id,u.name,u.title,u.is_business,u.is_trust from entities u ", qw ="";
	logger.debug("find ");
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
		qw += " u.name like ?  ";
	    }
	    if(businessOnly){
		if(!qw.equals("")) qw += " and ";
		qw += " u.is_business != null ";
	    }
	    else if(trustOnly){
		if(!qw.equals("")) qw += " and ";
		qw += " u.is_trust != null ";
	    }
	    else if(peopleOnly){
		if(!qw.equals("")) qw += " and ";								
		qw += " u.is_business is null and u.is_trust is null";
	    }
	}
	if(!waiver_id.equals("")){
	    qq += ", entity_waivers ow ";
	    if(!qw.equals("")) qw += " and ";
	    qw += " ow.entity_id=u.id and ow.waiver_id=? ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by u.name ";
	if(!limit.equals("")){
	    qq += limit;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.equals("")){
		pstmt.setString(jj++, id);
	    }
	    else {
		if(!name.equals("")){
		    pstmt.setString(jj++,"%"+name+"%");
		}
	    }
	    if(!waiver_id.equals("")){
		pstmt.setString(jj++, waiver_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(entities == null)
		    entities = new ArrayList<>();
		Entity one =
		    new Entity(debug,
			       rs.getString(1),
			       rs.getString(2),
			       rs.getString(3),
			       rs.getString(4) != null,
			       rs.getString(5) != null);

		entities.add(one);
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






















































