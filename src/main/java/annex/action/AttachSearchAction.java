/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package annex.action;

import java.io.File;
import java.util.*;
import java.nio.file.*;
import org.apache.commons.io.FileUtils;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

public class AttachSearchAction extends TopAction{

    static final long serialVersionUID = 120L;	
    static Logger logger = LogManager.getLogger(AttachSearchAction.class);	

    private List<FileUpload> uploads = null;
    private FileUploadList uploadList = null;
    private String attachmentsTitle = "Most recent attachments ";
    public String execute() {
	String ret = INPUT;		
	String back = doPrepare();
	if(!back.equals("")){
	    logger.error(back);
	    try{
		HttpServletResponse res = ServletActionContext.getResponse();
		String str = url+"Login";
		res.sendRedirect(str);
		return super.execute();
	    }catch(Exception ex){
		logger.error(ex);
	    }
	}		
	if(!action.equals("")){
	    // since we are searching we want all that match
	    uploadList.setNoLimit(); 
	    back = uploadList.find();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		uploads = uploadList.getUploads();
		if(uploads == null || uploads.size() == 0){
		    addActionMessage("No match found");
		}
		else{
		    attachmentsTitle = "Found "+uploads.size()+" records";
		}
	    }
	}
	else {
	    getUploadList();
	}
	return ret;
    }
    public void setUploadList(FileUploadList val){
	if(val != null)
	    uploadList = val;
    }
    public FileUploadList getUploadList(){
	if(uploadList == null){
	    uploadList = new FileUploadList();
	}
	return uploadList;
    }
    public String getAttachmentsTitle(){
	return attachmentsTitle;
    }
    public List<FileUpload> getUploads(){
	if(uploads == null){
	    String back = uploadList.find();
	    if(back.equals("")){
		List<FileUpload> list = uploadList.getUploads();
		if(list != null && list.size() > 0){
		    uploads = list;
		}
	    }
	}
	return uploads;
    }

}





































