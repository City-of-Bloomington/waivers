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
import javax.activation.*; 
import org.apache.struts2.ServletActionContext;
import org.apache.tika.Tika;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

public class UploadAction extends TopAction{

    static final long serialVersionUID = 270L;
    static Logger logger = LogManager.getLogger(UploadAction.class);			
    String waiver_id="", task_id="", notes="", hardcopy_location="",
	type="";
    private File file;
    private String contentType, saveDir="";
    private String filename;
    private String url = "", upload="";
    private List<FileUpload> uploads = null;
    private String[] types = {"Application","Warranty Deed","Recorded Waiver","Map","Other"};
    static private Map<String, String> mimeTypes = null;
    private Waiver waiver = null;
    private Task task = null;
    public void setUpload(File file) {
	this.file = file;
    }
    public void setSaveDir(String str) {
	if(str != null)
	    saveDir = str;
    }		
	
    public void setUploadContentType(String contentType) {
	this.contentType = contentType;
	System.err.println(" content type "+contentType);
    }
	
    public void setUploadFileName(String val) {
	if(val != null)
	    this.filename = val;

    }

    public void setAction(String val){
	action = val;
    }	
    public String execute() {
	String ret = INPUT;		
	String back = doPrepare();
	prepareMimeTypesMap();
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
	    logger.debug(" action upload save ");
	    if(!hasType()){
		back = "You need to choose file type ";
		addActionError(back);
	    }
	    else if(file != null){
		try{
		    //
		    Tika tika = new Tika();
		    String mimeType = tika.detect(file);
		    String file_ext = "";
		    if(mimeTypes.containsKey(mimeType)){
			file_ext = mimeTypes.get(mimeType);
		    }
		    FileUpload upload = new FileUpload();
		    upload.setWaiver_id(waiver_id);
		    upload.setTask_id(task_id);
		    upload.setType(type);
		    upload.setNotes(notes);
		    upload.setHardcopy_location(hardcopy_location);
		    String new_file_name = upload.genNewFileName(file_ext);
		    upload.setOld_file_name(filename);
		    upload.setUser_id(user.getId());
		    String year = Helper.getThisYear();
		    //
		    // String filePath = ctx.getRealPath("/") +"WEB-INF"+File.separator+"files"+File.separator+year+File.separator;
		    //
		    String filePath = server_path+File.separator+year+File.separator;
		    File new_file = new File(filePath, new_file_name);
		    FileUtils.copyFile(file, new_file);
		    back = upload.doSave();
		    if(back.equals("")){
			ret = SUCCESS;
			addActionMessage("Save successfully");
			String str = "waiver.action?id="+waiver_id;
			if(!task_id.equals("")){
			    str ="task.action?task_id="+task_id;
			}
			try{
			    HttpServletResponse res = ServletActionContext.getResponse();
			    str = url+str;
			    res.sendRedirect(str);
			    return super.execute();
			}catch(Exception ex){
			    System.err.println(ex);
			}
		    }
		    else{
			addActionError(back);
		    }
		}catch(Exception ex){
		    logger.error(ex);
		    addActionError(""+ex);
		}
	    }
	}
	else if(action.equals("Delete")){
	    FileUpload fup = new FileUpload();
	    fup.setId(id);
	    back = fup.doSelect();
	    if(back.equals("")){
		waiver_id=fup.getWaiver_id();
		task_id = fup.getTask_id();
		back = fup.doDelete();
		if(back.equals("")){
		    addActionMessage("Deleted successfully");
		    String str = "waiver.action?id="+waiver_id;
		    if(!task_id.equals("")){
			str ="task.action?task_id="+task_id;
		    }
		    try{
			HttpServletResponse res = ServletActionContext.getResponse();
			str = url+str;
			res.sendRedirect(str);
			return super.execute();
		    }catch(Exception ex){
			logger.error(ex);
		    }
		}
	    }
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	}
	return ret;
    }
    /**
     * to get file extension from mime type we are building
     * the mimeTypes hashmap
     */
    private void prepareMimeTypesMap(){
	if(mimeTypes == null){
	    mimeTypes = new HashMap<>();
	    mimeTypes.put("image/gif","gif");
	    mimeTypes.put("image/jpeg","jpg");
	    mimeTypes.put("image/png","png");
	    mimeTypes.put("image/tiff","tiff");
	    mimeTypes.put("image/bmp","bmp");
	    mimeTypes.put("text/plain","txt");
	    mimeTypes.put("audio/x-wav","wav");
	    mimeTypes.put("application/pdf","pdf");
	    mimeTypes.put("audio/midi","mid");
	    mimeTypes.put("video/mpeg","mpeg");
	    mimeTypes.put("video/mp4","mp4");
	    mimeTypes.put("video/x-ms-asf","asf");
	    mimeTypes.put("video/x-ms-wmv","wmv");
	    mimeTypes.put("video/x-msvideo","avi");
	    mimeTypes.put("text/html","html");
						
	    mimeTypes.put("application/mp4","mp4");
	    mimeTypes.put("application/x-shockwave-flash","swf");
	    mimeTypes.put("application/msword","doc");
	    mimeTypes.put("application/xml","xml");
	    mimeTypes.put("application/vnd.ms-excel","xls");
	    mimeTypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document","docx");
	    mimeTypes.put("application/vnd.ms-powerpoint","ppt");
	}

    }
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }
    public void setWaiver_id(String val){
	if(val != null)
	    waiver_id = val;
    }
    public void setTask_id(String val){
	if(val != null)
	    task_id = val;
    }		
    public void setType(String val){
	if(val != null && !val.equals("-1"))
	    type = val;
    }
    public void setHardcopy_location(String val){
	if(val != null)
	    hardcopy_location = val;
    }		
    public String getNotes(){
	return notes;
    }
    public String getWaiver_id(){
	return waiver_id;
    }
    public String getTask_id(){
	return task_id;
    }
    public String getType(){
	logger.debug(" get type");
	if(type.equals("") && hasTask()){
	    getTask();
	    type = task.getSuggested_upload_type();
	    if(task.hasSuggested_upload_type()){
		type = task.getSuggested_upload_type();
	    }
	}
	if(type.equals(""))
	    return "-1";
	return type;
    }
    public boolean hasType(){
	return !type.equals("");
    }
    public String getHardcopy_location(){
	return hardcopy_location;
    }		
    public String[] getTypes(){
	return types;
    }
    public boolean hasTask(){
	return !task_id.equals("");
    }
    public Waiver getWaiver(){
	logger.debug(" get waiver");
	if(waiver == null && !waiver_id.equals("")){
	    Waiver one = new Waiver(debug, waiver_id);
	    String back = one.doSelect();
	    if(back.equals(""))
		waiver = one;
	    else{
		logger.error(back);
	    }
	}
	return waiver;
    }
    public Task getTask(){
	logger.debug(" get task ");
	if(task == null && !task_id.equals("")){
	    Task one = new Task(debug, task_id);
	    String back = one.doSelect();
	    if(back.equals(""))
		task = one;
	    else{
		logger.error(back);
	    }
	}
	return task;
    }		
    public List<FileUpload> getUploads(){
	logger.debug(" get uploads ");
	if(uploads == null){
	    FileUploadList fl = new FileUploadList();
	    if(!waiver_id.equals(""))
		fl.setWaiver_id(waiver_id);						
	    String back = fl.find();
	    if(back.equals("")){
		List<FileUpload> list = fl.getUploads();
		if(list != null && list.size() > 0){
		    uploads = list;
		}
	    }
	    else{
		logger.error(back);
	    }
	}
	return uploads;
    }

}





































