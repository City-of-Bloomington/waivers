/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package annex.action;

import java.util.*;
import java.io.*;
import java.text.*;
import com.opensymphony.xwork2.ModelDriven;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

public class ReportAction extends TopAction{

    static final long serialVersionUID = 249L;	

    static Logger logger = LogManager.getLogger(ReportAction.class);
    //
    List<Waiver> waivers = null;
    List<Integer> years = null;
    WaiverList waiverList = null;
    String reportTitle = "Waivers Report";
    String outputType = "html"; // or csv
    String dateFrom = "", dateTo = "";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!action.isEmpty()){
	    logger.debug(" report");
	    getWaiverList();
	    waiverList.setNoLimit();
	    back = waiverList.find();
	    if(!back.equals("")){
		addActionError(back);
		logger.debug(" report "+back);
	    }
	    else{
		List<Waiver> list = waiverList.getWaivers();
		if(list != null && list.size() > 0){
		    waivers = list;
		    if(outputType.equals("csv")){
			ret = "csv";
		    }										
		}
		else{
		    addActionMessage("No match found");
		}
	    }
	}
	return ret;
    }
    public boolean hasWaivers(){
	return waivers != null && waivers.size() > 0;
    }
    public WaiverList getWaiverList(){ 
	if(waiverList == null){
	    waiverList = new WaiverList();
	}		
	return waiverList;
    }
    public List<Integer> getYears(){
	if(years == null){
	    years = Helper.getYears(10);
	}
	return years;
    }
    public List<Waiver> getWaivers(){
	return waivers;
    }
    public String getOutputType(){
	return outputType;
    }
    public void setOutputType(String val){
	if(val != null)
	    outputType = val;
    }
    //

    public String getReportTitle(){
	return reportTitle;
    }

    public String populate(){
	String ret = SUCCESS;
	return ret;
    }

}





































