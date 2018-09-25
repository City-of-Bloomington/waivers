package annex;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.log4j.Logger;

public class Group extends Type{

		static final long serialVersionUID = 160L;	
		static Logger logger = Logger.getLogger(Group.class);
		List<User> users = null;
		//
		public Group(){
				super();
				setTable_name("groups");
		}
		public Group(boolean deb, String val){
				//
				// initialize
				//
				super(deb, val);
				setTable_name("groups");
    }		
		public Group(boolean deb, String val, String val2){
				//
				// initialize
				//
				super(deb, val, val2);
				setTable_name("groups");
    }
		public Group(boolean deb, String val, String val2, boolean val3){
				//
				// initialize
				//
				super(deb, val, val2, val3);
				setTable_name("groups");
    }		
		public List<User> getUsers(){
				if(!id.equals("") && users == null){
						UserList ul = new UserList(debug);
						ul.setGroup_id(id);
						String back = ul.find();
						if(back.equals("")){
								List<User> ones = ul.getUsers();
								if(ones != null && ones.size() > 0){
										users = ones;
								}
						}
				}
				return users;
		}
		/**
		 * check if a given user is in the group
		 */
		public boolean isUserInGroup(User one){
				getUsers();
				return users != null && users.contains(one);
		}


}
