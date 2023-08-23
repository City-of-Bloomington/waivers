package annex.web;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import javax.naming.directory.*;
import java.net.URL;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.annotation.WebServlet;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

@WebServlet(urlPatterns = {"/Login"})
public class Login extends TopServlet{

    static final long serialVersionUID = 190L;
    static int count = 0;
    String cookieName = ""; // "cas_session";
    String cookieValue = ""; // ".bloomington.in.gov";
    static Logger logger = LogManager.getLogger(Login.class);
	
    /**
     * Generates the login form for all users.
     *
     * @param req the request 
     * @param res the response
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	String username = "", ipAddress = "", message="", id="";
	boolean found = false;
	
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	HttpSession session = null;
	AttributePrincipal principal = null;
	logger.debug(" login ");
	if (req.getUserPrincipal() != null) {
	    principal = (AttributePrincipal) req.getUserPrincipal();
	    username = principal.getName();
	}
	String host_forward = req.getHeader("X-Forwarded-Host");
	String host = req.getHeader("host");	
	if(host_forward != null){
	    url = host_forward+"/waivers/";
	}
	else if(host != null){
	    if(host.indexOf("waivers") > -1){
		url = host;
	    }
	    else{
		url = host+"/waivers/";
	    }
	}
	else{
	    url  = getServletContext().getInitParameter("url");
	}
	if(username != null){
	    session = req.getSession(true);			
	    User user = getUser(username);
	    if(user != null && session != null){
		session.setAttribute("user",user);
		out.println("<head><title></title><META HTTP-EQUIV=\""+
			    "refresh\" CONTENT=\"0; URL=" + url +
			    "welcome.action" + 
			    "\"></head>");
		out.println("<body>");
		out.println("</body>");
		out.println("</html>");
		out.flush();
		return;
	    }
	    else{
		message = " Unauthorized access";
	    }
	}
	else{
	    count++;
	    if(count < 3){
		String str = url+"Login";
		res.sendRedirect(str);
	    }
	    message += " You can not access this system, check with IT or try again later";
	}
	out.println("<head><title></title><body>");
	out.println("<p><font color=red>");
	out.println(message);
	out.println("</p>");
	out.println("</body>");
	out.println("</html>");
	out.flush();	
    }
	
    void setCookie(HttpServletRequest req, 
		   HttpServletResponse res){ 
	Cookie cookie = null;
	boolean found = false;
	Cookie[] cookies = req.getCookies();
	if(cookies != null){
	    for(int i=0;i<cookies.length;i++){
		String name = cookies[i].getName();
		if(name.equals(cookieName)){
		    found = true;
		}
	    }
	}
	//
	// if not found create one with 0 time to live;
	//
	// System.err.println(" cookie found ? "+found);
	if(!found){
	    cookie = new Cookie(cookieName,cookieValue);
	    res.addCookie(cookie);
	}
    }
    /**
     * Procesesses the login and check for authontication.
     * @param req
     * @param res
     */		
    User getUser(String username){

	boolean success = true;
	User user = null;
	String fullName="",role="",dept="", message="";
	User one = new User(debug, null, username);
	String back = one.doSelect();
	if(!back.equals("")){
	    message += back;
	    logger.error(back);
	}
	else{
	    user = one;
	}
	return user;
    }

}

