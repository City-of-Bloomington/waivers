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
/**
 * this code probably is not needed anymore, we are using AddressAction
 * class instead
 */

public class AddressVerifyAction extends TopAction{

    static final long serialVersionUID = 315L;	
    static Logger logger = LogManager.getLogger(AddressVerifyAction.class);
    //
    String waiver_id="";
    Address address = null;
    String[] addrCombs = null; 
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
	else if(action.equals("Save")){
	    logger.debug(" action save ");
	    back = doVerify();
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		back = doSave();
		if(!back.equals("")){
		    addActionError(back);
		    logger.error(back);
		}
		else{
		    addActionMessage("Saved Successfully");
		}
	    }
	}
	getWaiver_id();
	if(!waiver_id.equals("")){
	    try{
		HttpServletResponse res = ServletActionContext.getResponse();
		String str = url+"waiver.action?action=Edit&id="+getWaiver_id();
		res.sendRedirect(str);
		return super.execute();
	    }catch(Exception ex){
		System.err.println(ex);
	    }											
	}
	else{
	    getAddress();
	}
	return ret;
    }
    public Address getAddress(){
	logger.debug(" get address ");
	if(address == null){
	    if(!id.equals("")){
		address = new Address(debug, id);
		String back = address.doSelect();
		if(!back.equals("")){
		    addActionError(back);
		    logger.error(back);
		}
	    }
	    else{
		address = new Address();
	    }
	}
	if(!waiver_id.equals(""))
	    address.setWaiver_id(waiver_id);
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
    public void setAddrCombos(String[] vals){
	if(vals != null){
	    addrCombs = vals;
	}
    }
    // 
    private String doVerify(){
	String back = "";
	logger.debug(" do verify ");
	if(address != null && address.canVerify()){
	    AddressList adl = new AddressList();
	    back = adl.findSimilarAddr(addrUrl, address.getAddressToVerify());
	    if(back.equals("")){
		addresses = adl.getAddresses();
		if(addresses == null || addresses.size() == 0){
		    address.setInvalid(true);
		}
	    }
	    else{
		logger.error(back);								
	    }
	}
	return back;
    }
    private String doSave(){
	String ret = "";
	if(addrCombs != null){
	    logger.debug(" action save ");
	    for(String str:addrCombs){
		Address addr = new Address();
		addr.setWaiver_id(waiver_id);
		addr.setAddrCombo(str);
		ret += addr.doSave();
	    }
	}
	return ret;
    }
    public boolean hasAddresses(){
	return addresses != null && addresses.size() > 0;
    }
    public List<Address> getAddresses(){
	return addresses;
    }


}





































