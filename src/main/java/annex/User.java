package annex;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

public class User extends CommonInc implements java.io.Serializable{

    String id="", username="", role="user", // user, admin
				dept="", inactive="";
		String full_name="";
		static final long serialVersionUID = 280L;
		static Logger logger = Logger.getLogger(User.class);
		static Map<String, String> rolesMap = null;
		//
		public User(){
				super();
		}
		public User(boolean deb){
				//
				// initialize
				//
				super(deb);
    }
		public User(String val){
				//
				setId(val);
    }
		public User(boolean deb, String val){
				//
				// initialize
				//
				super(deb);
				setId(val);
    }
		public User(boolean deb, String val, String val2){
				//
				// initialize
				//
				debug = deb;
				setId(val);
				setUsername(val2);
    }
		public User(boolean deb,
								String val,
								String val2,
								String val3,
								String val4,
								String val5,
								boolean val6
								){
				//
				// initialize
				//
				debug = deb;
				setId(val);
				setUsername(val2);
				setFullName(val3);
				setDept(val4);
				setRole(val5);
				setInactive(val6);
    }
		public String getId(){
				return id;
		}
		public String getUsername(){
				return username;
		}		
		public String getRole(){
				return role;
		}
		public String getRoleInfo(){
				String ret = "";
				setRoleMap();
				if(rolesMap != null && rolesMap.containsKey(role)){
						ret = rolesMap.get(role);
				}
				return ret; 
		}
		public String getDept(){
				return dept;
		}		
		public String getFullName(){
				return full_name;
		}
		public boolean getInactive(){
				return !inactive.equals("");
		}

		public void setId(String val){
				if(val != null)
						id = val;
		}
		public void setUsername(String val){
				if(val != null)
						username = val;
		}		
		public void setRole(String val){
				if(val != null)
						role = val;
		}
		public void setDept(String val){
				if(val != null)
						dept = val;
		}		
		public void setFullName(String val){
				if(val != null)
						full_name = val;
		}
		public void setInactive(boolean val){
				if(val)
						inactive = "y";
		}		
		//
		public boolean userExists(){
				return !full_name.equals("");
		}
		    //
    public boolean hasRole(String val){
				return role != null && role.indexOf(val) > -1;
    }
		public boolean canEdit(){
				return hasRole("Edit");
		}
		public boolean canDelete(){
				return hasRole("Delete");
		}
		public boolean isAdmin(){
				return hasRole("Admin");
		}
    //
    public String toString(){
				return full_name;
    }
		private void setRoleMap(){
				if(rolesMap == null){
						rolesMap = new HashMap<>();
						rolesMap.put("View","View");
						rolesMap.put("Edit","Edit");
						rolesMap.put("Edit:Delete","Edit and Delete");						
						rolesMap.put("Edit:Delete:Admin","Admin (all)");
				}
		}
		public boolean equals(Object obj){
				if(obj instanceof User){
						User one =(User)obj;
						return id.equals(one.getId());
				}
				return false;				
		}
		public int hashCode(){
				int seed = 37;
				if(!id.equals("")){
						try{
								seed += Integer.parseInt(id);
						}catch(Exception ex){
						}
				}
				return seed;
		}		
		//
    public String doSelect(){
				String msg = "";
				Connection con = null;
				PreparedStatement stmt = null;
				ResultSet rs = null;		
				String qq = " select id,username,full_name,dept,role,inactive from users where  ";
				if(!id.equals("")){
						qq += " id = ? ";
				}
				else if(!username.equals("")){
						qq += " username = ? ";
				}
				else {
						msg = " User id or username not set";
						addError(msg);
						return msg;
				}
				if(debug){
						logger.debug(qq);
				}
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						addError(msg);
						return msg;
				}			
				try{
						stmt = con.prepareStatement(qq);
						if(!id.equals(""))
								stmt.setString(1, id);
						else
								stmt.setString(1, username);								
						rs = stmt.executeQuery();
						if(rs.next()){
								setId(rs.getString(1));
								setUsername(rs.getString(2));
								setFullName(rs.getString(3));
								setDept(rs.getString(4));
								setRole(rs.getString(5));
								setInactive(rs.getString(6) != null);
						}
						else{
								msg = " No such user";
								message = msg;
						}
				}catch(Exception ex){
						logger.error(ex+" : "+qq);
						msg += " "+ex;
						addError(msg);
				}
				finally{
						Helper.databaseDisconnect(con, stmt, rs);
				}
				return msg;
    }
		//
    public String doSave(){
		
				Connection con = null;
				PreparedStatement stmt = null;
				ResultSet rs = null;		
		
				String str="", msg="";
				String qq = "";
				if(username.equals("") || full_name.equals("")){
						msg = "username or  full name not set";
						return msg;
				}
				qq = "insert into users values(0,?,?,?,?,?)";
				//
				if(debug){
						logger.debug(qq);
				}
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						addError(msg);
						return msg;
				}			
				try{
						stmt = con.prepareStatement(qq);
						stmt.setString(1, username);
						if(full_name.equals(""))
								stmt.setNull(2,Types.VARCHAR);
						else
								stmt.setString(2,full_name);
						if(dept.equals(""))
								stmt.setNull(3,Types.VARCHAR);
						else
								stmt.setString(3, dept);
						stmt.setString(4, role);
						if(inactive.equals(""))
								stmt.setNull(5,Types.CHAR);
						else
								stmt.setString(5, "y");						
						stmt.executeUpdate();
						qq = "select LAST_INSERT_ID() ";
						if(debug){
								logger.debug(qq);
						}
						stmt = con.prepareStatement(qq);				
						rs = stmt.executeQuery();
						if(rs.next())
								id = rs.getString(1);
				}
				catch(Exception ex){
						msg = ex+": "+qq;
						logger.error(msg);
						addError(msg);
						return msg;
				}
				finally{
						Helper.databaseDisconnect(con, stmt, rs);
				}
				return msg; // success
    }
    public String doUpdate(){
		
				Connection con = null;
				PreparedStatement stmt = null;
				ResultSet rs = null;		
		
				String str="", msg="";
				String qq = "";
				qq = "update users set username=?,full_name=?,dept=?,role=?,inactive=? where id=?";
				//
				if(id.equals("") || username.equals("") || full_name.equals("")){
						msg = "User id, username or full name not set";
						return msg;
				}				
				if(debug){
						logger.debug(qq);
				}
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						addError(msg);
						return msg;
				}			
				try{
						stmt = con.prepareStatement(qq);
						stmt.setString(1, username);
						stmt.setString(2, full_name);
						if(dept.equals(""))
								stmt.setNull(3,Types.VARCHAR);
						else
								stmt.setString(3, dept);
						if(role.equals(""))
								stmt.setNull(4,Types.VARCHAR);
						else
								stmt.setString(4, role);
						if(inactive.equals(""))
								stmt.setNull(5,Types.VARCHAR);
						else
								stmt.setString(5, "y");						
						stmt.setString(6, id);
						stmt.executeUpdate();
				}
				catch(Exception ex){
						msg = ex+": "+qq;
						logger.error(msg);
						addError(msg);
						return msg;
				}
				finally{
						Helper.databaseDisconnect(con, stmt, rs);
				}
				return msg; // success
    }		
		//
    String doDelete(){

				String qq = "",msg="";
				Connection con = null;
				PreparedStatement stmt = null;
				ResultSet rs = null;			
				qq = "delete from  users where id=?";
				con = Helper.getConnection();
				if(con == null){
						msg = "Could not connect to DB";
						addError(msg);
						return msg;
				}			
				try{
						stmt = con.prepareStatement(qq);
						stmt.setString(1,id);
						stmt.executeUpdate();
						message="Deleted Successfully";
						//
				}
				catch(Exception ex){
						msg = ex+" : "+qq;
						logger.error(msg);
						addError(msg);
				}
				finally{
						Helper.databaseDisconnect(con, stmt, rs);
				}			
				return msg; 
    }


}
