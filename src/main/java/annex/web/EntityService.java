package annex.web;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.annotation.WebServlet;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

@WebServlet(urlPatterns = {"/EntityService"})
public class EntityService extends TopServlet{

    static final long serialVersionUID = 2210L;
    static Logger logger = LogManager.getLogger(EntityService.class);
		
    public void doGet(HttpServletRequest req,
		      HttpServletResponse res)
	throws ServletException, IOException {
	doPost(req,res);
    }

    /**
     * @param req The request input stream
     * @param res The response output stream
     */
    public void doPost(HttpServletRequest req,
		       HttpServletResponse res)
	throws ServletException, IOException {

	//
	String message="", action="";
	res.setContentType("application/json");
	PrintWriter out = res.getWriter();
	String name, value;
	String term ="", type="", department_id="";
	boolean success = true;
	HttpSession session = null;
	Enumeration<String> values = req.getParameterNames();
	String [] vals = null;
	while (values.hasMoreElements()){
	    name = values.nextElement().trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("term")) { // this is what jquery sends
		term = value;
	    }
	    else if (name.equals("action")){
		action = value;
	    }
	    else{
		// System.err.println(name+" "+value);
	    }
	}
	logger.debug(" entity service ");
	EntityList olist =  null;
	List<Entity> entities = null;
	if(!term.equals("")){
	    //
	    olist = new EntityList(debug, term);
	    String back = olist.find();
	    if(back.equals("")){
		entities = olist.getEntities();
	    }
	    else{
		logger.error(back);
	    }
	}
	if(entities != null && entities.size() > 0){
	    String json = writeJson(entities);
	    out.println(json);
	}
	out.flush();
	out.close();
    }

    /**
     * Creates a JSON array string for a list of users
     *
     * @param users The users
     * @param type unused
     * @return The json string
     */
    String writeJson(List<Entity> entities){
	String json="";
	for(Entity one:entities){
	    if(!json.equals("")) json += ",";
	    json += "{\"id\":\""+one.getId()+"\",\"value\":\""+one.toString()+"\"}";
	}
	json = "["+json+"]";
	System.err.println(json);
	return json;
    }
}
