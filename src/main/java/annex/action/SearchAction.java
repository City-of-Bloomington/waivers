package annex.action;
/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
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

public class SearchAction extends TopAction{

    static final long serialVersionUID = 215L;	
    static Logger logger = LogManager.getLogger(SearchAction.class);
    //
    boolean outputCsv = false;
    List<Waiver> waivers = null;
    WaiverList waiverList = null;
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
	if(!action.equals("")){
	    logger.debug(" search action ");
	    getWaiverList();
	    waiverList.setNoLimit();
	    back = waiverList.find();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		waivers = waiverList.getWaivers();
		if(outputCsv){
		    return "csv";
		}
		else if(waivers != null && waivers.size() > 0){
		    if(waivers.size() == 1){
			Waiver one = waivers.get(0);
			try{
			    HttpServletResponse res = ServletActionContext.getResponse();
			    String str = url+"waiver.action?id="+one.getId();
			    res.sendRedirect(str);
			    return super.execute();
			}catch(Exception ex){
			    System.err.println(ex);
			    logger.error(ex);
			}						
		    }
		    waiversTitle = "Found "+waivers.size()+" records";
		}
		else{
		    addActionMessage("No match found");
		    waiversTitle = "No match found";
		}
	    }
	}				
	else{
	    logger.debug(" search waiver list ");						
	    getWaiverList();
	    WaiverList ml = new WaiverList(debug);
	    back = ml.find();
	    if(back.equals(""))
		waivers = ml.getWaivers();
	    else{
		logger.error(back);
	    }
						
	}
	return ret;
    }
    public WaiverList getWaiverList(){
	if(waiverList == null){
	    waiverList = new WaiverList(debug); 
	}
	return waiverList;
    }

    public String getWaiversTitle(){
	return waiversTitle;
    }
    public void setOutputCsv(boolean val){
	outputCsv = val;
    }
    public boolean getOutputCsv(){
	return outputCsv;
    }
    public void setAction2(String val){
	if(val != null && !val.equals(""))		
	    action = val;
    }		
    // most recent
    public List<Waiver> getWaivers(){ 
	return waivers;
    }

}





































