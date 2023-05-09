package annex.action;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

public class AddressAction extends TopAction{

    static final long serialVersionUID = 315L;	
    static Logger logger = LogManager.getLogger(AddressAction.class);
    //
    String waiver_id="", type="";
    Address address = null;
    List<Address> addresses = null;
    String addressesTitle = "Addresses";
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
	if(action.endsWith("Way")){ // Save any Way
	    logger.debug("Save Any Way action" );
	    if(address.hasId()){
		back = doVerify();								
		back += address.doUpdate();								
	    }
	    else{
		back = doVerify();
		back += address.doSave();
	    }
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		id = address.getId();
		addActionMessage("Saved Successfully");
	    }
	}					
	if(action.startsWith("Save")){
	    logger.debug("Adddress Save action ");
	    if(address.hasId()){
		back = doVerify();								
		back += address.doUpdate();								
	    }
	    else{
		back = doVerify();
		if(address.isValid()){
		    back += address.doSave();
		}
		else{
		    back = "Address is invalid, please check the address and click on 'Save' again, or click on 'Save any Way'";
		}
	    }
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		id = address.getId();
		addActionMessage("Saved Successfully");
	    }
	}				
	else if(action.startsWith("Remove")){
	    logger.debug("address action remove");
	    back = address.doRemove();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Removed Successfully");
		try{
		    HttpServletResponse res = ServletActionContext.getResponse();
		    String str = url+"waiver.action?action=Edit&id"+waiver_id;
		    res.sendRedirect(str);
		    return super.execute();
		}catch(Exception ex){
		    System.err.println(ex);
		    logger.error(" address action "+ex);
		}										
	    }
	}
	else if(action.equals("Edit")){
	    getAddress();
	}
	else if(!id.equals("")){
	    getAddress();						
	    ret = "view";
	}
	if(!type.equals("")){
	    getAddress();
	    ret = "popup";
	}
	else{
	    getAddress();
	}
	return ret;
    }
    public Address getAddress(){ 
	if(address == null){
	    logger.debug("get address");
	    if(!id.equals("")){
		address = new Address(debug, id);
		String back = address.doSelect();
		if(!back.equals("")){
		    addActionError(back);
		}
	    }
	    else{
		address = new Address();
	    }
	}
	if(!waiver_id.equals("")){
	    address.setWaiver_id(waiver_id);

	}
	return address;
    }

    public void setAddress(Address val){
	if(val != null)
	    address = val;
    }
    public void setWaiver_id(String val){
	if(val != null)
	    waiver_id = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }
		
    public String getWaiver_id(){
	if(waiver_id.equals("")){
	    getAddress();
	    waiver_id = address.getWaiver_id();
	}
	return waiver_id;
    }
    public String getAddressesTitle(){
	return addressesTitle;
    }		
    public void setAction2(String val){
	if(val != null && !val.equals(""))		
	    action = val;
    }
    private String doVerify(){
	String back = "";
	logger.debug(" do verify");
	if(address != null && address.canVerify()){
	    AddressList adl = new AddressList();
	    back = adl.findSimilarAddr(addrUrl, address.getAddressToVerify());
	    if(back.equals("")){
		List<Address> addrs = adl.getAddresses();
		if(addrs == null || addrs.size() == 0){
		    address.setInvalid(true);
		}
	    }
	}
	return back;
    }		
    //
    // we can use to get addresss list for auto_complete
    public List<Address> getAddresses(){ 
	if(addresses == null){
	    logger.debug(" get addresses ");
	    AddressList dl = new AddressList(debug, waiver_id);
	    String back = dl.find();
	    if(back.equals("")){
		addresses = dl.getAddresses();
	    }
	    else{
		logger.error(back);
	    }
	}		
	return addresses;
    }


}





































