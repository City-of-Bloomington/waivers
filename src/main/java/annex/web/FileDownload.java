package annex.web;

import java.util.*;
import java.util.regex.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.sql.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.io.*;
import javax.servlet.ServletException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.annotation.WebServlet;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

@WebServlet(urlPatterns = {"/doDownload"})
@SuppressWarnings("unchecked")
public class FileDownload extends TopServlet{

    final static long serialVersionUID = 420L;
    static Logger logger = LogManager.getLogger(FileDownload.class);
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    //
    /**
     * 
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);
    }
    /**
     * @link #doGet
     * @see #doGet
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
    
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;

	boolean success = true;
	String message="";
	// 

	String [] vals;

	//
	String saveDirectory = "";
	String fileName = "";
	String content_type = req.getContentType();
	annex.model.FileUpload fup = new annex.model.FileUpload();
	Enumeration<String> values = req.getParameterNames();
	while (values.hasMoreElements()){
	    name = values.nextElement().trim();
	    vals = req.getParameterValues(name);
	    if(vals == null) continue;
	    value = vals[vals.length-1].trim();	
	    if (name.equals("id")){
		fup.setId(value);
	    }
	}
	User user = null;
	HttpSession session = null;
	session = req.getSession(false);
	if(session != null){
	    user = (User)session.getAttribute("user");
	    if(user == null){
		String str = url+"Login";
		res.sendRedirect(str);
		return; 
	    }
	}
	else{
	    String str = url+"Login";			
	    res.sendRedirect(str);
	    return; 
	}
	String filePath = "";
	if(true){
	    String back = fup.doSelect();
	    if(!back.equals("")){
		message += " could not retrieve file data "+back;
		logger.error(message);
		success = false;
	    }
	    filePath = server_path+fup.getYear()+"/"+fup.getFile_name();
	    try{
		doDownload(req, res, filePath, fup);
		return;
	    }catch(Exception ex){
		message += " file not found "+ex;
		logger.error(message);
	    }
	}
    }

    void doDownload(HttpServletRequest request,
		    HttpServletResponse response,
		    String filePath,
		    annex.model.FileUpload fup){
		
	BufferedInputStream input = null;
	BufferedOutputStream output = null;
	try{
	    //
	    // Decode the file name (might contain spaces and on) and prepare file object.
	    // File file = new File(filePath, URLDecoder.decode(fup, "UTF-8"));
	    File file = new File(filePath);
	    //
	    // Check if file actually exists in filesystem.
	    if (!file.exists()) {
		//
		// Do your thing if the file appears to be non-existing.
		// Throw an exception, or send 404, or show default/warning page, or just ignore it.
		response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		return;
	    }
	    //
	    // Get content type by filename.
	    String contentType = context.getMimeType(file.getName());
	    //
	    // To add new content types, add new mime-mapping entry in web.xml.
	    if (contentType == null) {
		contentType = "application/octet-stream";
	    }
	    //			
	    // Init servlet response.
	    response.reset();
	    response.setBufferSize(DEFAULT_BUFFER_SIZE);
	    response.setContentType(contentType);
	    response.setHeader("Content-Length", String.valueOf(file.length()));
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fup.getOld_file_name() + "\"");
			
	    // Prepare streams.
	    //
            // Open streams.
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
	}
	catch(Exception ex){
	    logger.error(ex);
        } finally {
	    close(output);
            close(input);
        }
    }

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

}






















































