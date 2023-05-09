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

public class DataImportAction extends TopAction{

    static final long serialVersionUID = 290L;	
    static Logger logger = LogManager.getLogger(DataImportAction.class);
    //
    // old file
    // String file_name = "c:\\webapps\\waivers\\docs\\Waiver_GIS20160816.csv";
    String file_name = "c:\\webapps\\waivers\\docs\\waivers_9_4_18.csv";
		
    DataImport dimport = null;
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
	if(action.equals("Import")){
	    getDimport();
	    back = dimport.doImport(file_name);
	    if(!back.equals("")){
		addActionError(back);
	    }
	    else{
		addActionMessage("Save Successfully");
	    }
	}				
	else{		
	    getDimport();
	}
	return ret;
    }
    public DataImport getDimport(){ 
	if(dimport == null){
	    dimport = new DataImport();
	}		
	return dimport;
    }

    public void setDataImport(DataImport val){
	if(val != null){
	    dimport = val;
	}
    }
    public String getFile_name(){
	return file_name;
    }
    public void setFile_name(String val){
	if(val != null)
	    file_name = val;
    }

}





































