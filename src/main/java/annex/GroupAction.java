package annex;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import org.apache.log4j.Logger;

public class GroupAction extends TopAction{

		static final long serialVersionUID = 290L;	
		static Logger logger = Logger.getLogger(GroupAction.class);
		//
		User user = null;
		Type group = null;
		List<Type> groups = null;
		String groupsTitle = "Current groups";
		public String execute(){
				String ret = SUCCESS;
				String back = doPrepare();
				if(!back.equals("")){
						try{
								HttpServletResponse res = ServletActionContext.getResponse();
								String str = url+"Login";
								res.sendRedirect(str);
								return super.execute();
						}catch(Exception ex){
								System.err.println(ex);
						}	
				}
				if(action.equals("Save")){
						back = group.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){ 
						back = group.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Updated Successfully");
						}
				}
				else{		
						getGroup();
						if(!id.equals("")){
								back = group.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}								
						}
				}
				return ret;
		}
		public Type getGroup(){ 
				if(group == null){
						group = new Type();
						group.setId(id);
						group.setTable_name("groups");
				}		
				return group;
		}

		public void setGroup(Group val){
				if(val != null){
						group = val;
						group.setTable_name("groups");
				}
		}

		public String getGroupsTitle(){
				return groupsTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<Type> getGroups(){
				if(groups == null){
						TypeList tl = new TypeList(debug, null, "groups");
						String back = tl.find();
						if(back.equals("")){
								List<Type> ones = tl.getTypes();
								if(ones != null && ones.size() > 0){
										groups = ones;
								}
						}
				}
				return groups;
		}

}





































