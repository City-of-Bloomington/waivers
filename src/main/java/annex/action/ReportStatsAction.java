/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package annex.action;

import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts2.ServletActionContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import annex.model.*;
import annex.list.*;
import annex.utils.*;

public class ReportStatsAction extends TopAction{

    static final long serialVersionUID = 180L;	
   
    static Logger logger = LogManager.getLogger(ReportStatsAction.class);
    ReportStats report = null;
    List<String> years = null;
    public String execute(){
	String ret = INPUT;
	doPrepare(); 
	if(action.equals("Submit")){
	    ret = SUCCESS;
	    logger.debug(" report stats action ");
	    String back = report.find();
	    if(!back.equals("")){
		addActionError(back);
		ret = INPUT;
		logger.debug(" report stats action "+back);
	    }
	}
	return ret;
    }			 
    public ReportStats getReport(){
	if(report == null){
	    report = new ReportStats();
	}
	return report;
    }
    public void setReport(ReportStats val){
	if(val != null)
	    report = val;
    }
    public List<String> getYears(){
	if(years == null){
	    logger.debug(" report stats get years ");
	    int yy = Helper.getCurrentYear();
	    years = new ArrayList<String>(11);
	    years.add("");
	    for(int i=0;i<3;i++){
		int y2 = yy - i;
		years.add(""+y2);
	    }
	}
	return years;
    }

}





































