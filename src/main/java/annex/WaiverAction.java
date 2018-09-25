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

public class WaiverAction extends TopAction{

		static final long serialVersionUID = 315L;	
		static Logger logger = Logger.getLogger(WaiverAction.class);
		//
		Waiver waiver = null;
		List<Waiver> waivers = null;
		List<EmailLog> emailLogs = null;
		String entity_id="", address_id="";
		// List<Type> categories = null;
		// List<Type> depts = null;
		String waiversTitle = " Most recent Waivers";
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
						waiver.setAddedBy(user.getId());
						back = waiver.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								id = waiver.getId();
								addActionMessage("Saved Successfully");
								ret = "view";
						}
				}				
				else if(action.equals("Save Changes")){
						waiver.setUserId(user.getId());	 // we needed for actions					
						back = waiver.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								ret = "view";
								addActionMessage("Updated Successfully");
						}
				}
				else if(action.startsWith("Remove Entity")){
						getWaiver();
						back = waiver.doRemoveEntity();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Deleted Successfully");								
								ret = "edit";
						}
				}
				else if(action.equals("Delete")){
						getWaiver();
						back = waiver.doDelete();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Deleted Successfully");								
						}
				}
				else if(action.startsWith("Close")){
						waiver = new Waiver(debug, id);
						waiver.setClosedBy(user.getId());
						back = waiver.doClose();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Closed Successfully");								
								ret = "view";
						}
				}
				else if(action.startsWith("Remove Entity")){ // remove owner
						getWaiver();
						back = waiver.doRemoveEntity();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Entity removed Successfully");
						}
						ret = "edit";
				}
				else if(action.startsWith("Remove Addr")){ // remove owner
						getWaiver();
						back = waiver.doRemoveAddress();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Address removed Successfully");
						}
						ret = "edit";
				}						
				else if(action.equals("Edit")){ 
						waiver = new Waiver(debug, id);
						back = waiver.doSelect();
						if(!back.equals("")){
								addActionError(back);
						}
						ret = "edit";
				}				
				else if(!id.equals("")){
						ret = "view";
						waiver = new Waiver(debug, id);
						back = waiver.doSelect();
						if(!back.equals("")){
								addActionError(back);
						}
				}
				else{
						getWaiver();
				}
				return ret;
		}
		public Waiver getWaiver(){ 
				if(waiver == null){
						if(!id.equals("")){
								waiver = new Waiver(debug, id);
						}
						else{
								waiver = new Waiver();
						}
						if(!entity_id.equals(""))
								waiver.setEntityId(entity_id);
						if(!address_id.equals(""))
								waiver.setAddressId(address_id);
				}		
				return waiver;
		}

		public void setWaiver(Waiver val){
				if(val != null)
						waiver = val;
		}

		public String getWaiversTitle(){
				return waiversTitle;
		}		
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}		
		public String getId(){
				if(id.equals("") && waiver != null){
						id = waiver.getId();
				}
				return id;
		}
		public void setEntityId(String val){
				if(val != null && !val.equals(""))		
						entity_id = val;
		}
		public void setAddressId(String val){
				if(val != null && !val.equals(""))		
						address_id = val;
		}		
		// most recent
		public List<Waiver> getWaivers(){ 
				if(waivers == null){
						WaiverList dl = new WaiverList();
						dl.setStatus("Open");
						String back = dl.find();
						waivers = dl.getWaivers();
				}		
				return waivers;
		}
		public boolean hasEmailLogs(){
				getWaiver();
				if(!waiver.getId().equals("")){
						EmailLogList ell = new EmailLogList(debug, waiver.getId());
						String back = ell.find();
						if(back.equals("")){
								List<EmailLog> logs = ell.getEmailLogs();
								if(logs != null && logs.size() > 0){
										emailLogs = logs;
								}
						}
						return emailLogs != null && emailLogs.size() > 0;
				}
				return false;
		}
		public List<EmailLog> getEmailLogs(){
				return emailLogs;
		}		

}





































