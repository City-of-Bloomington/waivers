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

public class TypeList extends CommonInc{

    static Logger logger = LogManager.getLogger(TypeList.class);
    static final long serialVersionUID = 260L;
    String table_name = "waivers.groups"; 
    String name = ""; // for service
    String join = "";
    String condition = "";
    boolean active_only = false; // all
    List<Type> types = null;
	
    public TypeList(){
    }
    public TypeList(boolean deb){
	super(deb);
    }
    public TypeList(boolean deb, String val){
	super(deb);
	setName(val);
    }
    public TypeList(boolean deb, String val, String val2){
	super(deb);
	setName(val);
	setTable_name(val2);
    }		
    public List<Type> getTypes(){
	return types;
    }
		
    public void setTable_name(String val){
	if(val != null)
	    table_name = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setJoin(String val){
	if(val != null)
	    join = val;
    }
    public void setCondition(String val){
	if(val != null)
	    condition = val;
    }		
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select t.id,t.name,inactive from "+table_name+" t ";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qw = "";
	if(!join.equals("")){
	    qq += ", "+join;
	    qw += condition;
	}
	if(!name.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " t.name like ? ";
	}
	if(active_only){
	    if(!qw.equals("")) qw += " and ";
	    qw += " t.inactive is null ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by t.name ";
	logger.debug(qq);
	try{						
	    pstmt = con.prepareStatement(qq);
	    if(!name.equals("")){
		pstmt.setString(1,"%"+name+"%");
	    }						
	    rs = pstmt.executeQuery();
	    if(types == null)
		types = new ArrayList<Type>();
	    while(rs.next()){
		Type one =
		    new Type(debug,
			     rs.getString(1),
			     rs.getString(2),
			     rs.getString(3)!=null && !rs.getString(3).equals(""));
		if(!types.contains(one))
		    types.add(one);
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






















































