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
import com.opensymphony.xwork2.ModelDriven;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;  
import org.apache.struts2.dispatcher.SessionMap;  
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.util.ServletContextAware;  
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

public abstract class TopAction extends ActionSupport implements SessionAware, ServletContextAware{

    static final long serialVersionUID = 240L;
    static Logger logger = LogManager.getLogger(TopAction.class);		
    boolean debug = false, activeMail=false;
    String action="", url="", addrUrl="", id="";
    String server_path="";
    String city_email="", legal_username = "", utility_username="",
	gis_username="";
    User user = null;
    ServletContext ctx;
    Map<String, Object> sessionMap;

    public void setAction(String val){
	if(val != null)
	    action = val;
    }
    public void setAction2(String val){
	if(val != null && !val.equals(""))
	    action = val;
    }		
    public String getAction(){
	return action;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public String getId(){
	return id;
    }
    public User getUser(){
	return user;
    }		
    String doPrepare(){
	String back = "";
	try{
	    user = (User)sessionMap.get("user");
	    if(user == null){
		back = LOGIN;
	    }
	    if(url.equals("")){
		String val = ctx.getInitParameter("url");
		if(val != null)
		    url = val;
		val = ctx.getInitParameter("addrUrl");
		if(val != null)
		    addrUrl = val;								
		val = ctx.getInitParameter("server_path");
		if(val != null)
		    server_path = val;
		val = ctx.getInitParameter("city_email");
		if(val != null)
		    city_email = "@"+val;								
		val = ctx.getInitParameter("legal_username");
		if(val != null)
		    legal_username = val;
		val = ctx.getInitParameter("utility_username");
		if(val != null)
		    utility_username = val;
		val = ctx.getInitParameter("gis_username");
		if(val != null)
		    gis_username = val;
		val = ctx.getInitParameter("activeMail");
		if(val != null && val.equals("true"))
		    activeMail = true;								
	    }
	}catch(Exception ex){
	    System.out.println(ex);
	}		
	return back;
    }		
    @Override  
    public void setSession(Map<String, Object> map) {  
	sessionMap=map;  
    }
    @Override  	
    public void setServletContext(ServletContext ctx) {  
        this.ctx = ctx;  
    }  	
}





































