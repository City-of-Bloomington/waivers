package annex;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.sql.*;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 * for testing login without cas, not intended for production
 */

public class AdLogin extends HttpServlet{

		static final long serialVersionUID = 190L;	
    String url="";
    boolean debug = false;
		static Logger logger = Logger.getLogger(Login.class);
	
    /**
     * Generates the login form for all users.
     *
     * @param req the request 
     * @param res the response
     */
    public void doGet(HttpServletRequest req, 
											HttpServletResponse res) 
				throws ServletException, IOException {
				String name, value, id="";
				res.setContentType("text/html");
				PrintWriter out = res.getWriter();
				Enumeration<String> values = req.getParameterNames();
				while (values.hasMoreElements()){
						name = values.nextElement().trim();
						value = req.getParameter(name).trim();
						if (name.equals("id")) {
								id = value;
						}
				}

				out.println("<html><head><title>User Login</title>");
				out.println("<script>");
				//
				out.println(" function submitForm() {");
				out.println("	 if(document.myForm.userid.value == \"\")( ");
				out.println("	 alert(\"Please enter username\")");
        out.println("    return false;");
				out.println("  } ");
				out.println(" return true;");
				out.println(" }");
				out.println("</script>");
				out.println("</head><body onload=\"document.userid.focus();\">");
				out.println("<br /><br />");
				out.println("<center><h2>Welcome to Waivers </h2>");
				out.println("<form name=\"myForm\" method=\"post\" onsubmit=\"return submitForm();\" >");
				out.println("<table border=\"0\">");
        out.println("<tr><td>Username</td><td><input name=\"userid\" value=\"\"  size=\"10\" type=\"text\"></td><tr>");
				out.println("<tr><td>&nbsp;</td><td><input type=\"submit\" value=\"Submit\" /></td></tr>");
				out.println("</table> ");
				out.println("</form> ");	
				out.println("</body></html>");
				out.close();
    }									
    public void doPost(HttpServletRequest req, HttpServletResponse res) 
				throws ServletException, IOException{

				String username = "";
				PrintWriter os = null;
				boolean connectDbOk = false, success=true;
				res.setStatus(HttpServletResponse.SC_OK);
				res.setContentType("text/html");
				os = res.getWriter();
				// 
				if(url.equals("")){
						url    = getServletContext().getInitParameter("url");
						String str = getServletContext().getInitParameter("debug");
						if(str != null && str.equals("true")) debug = true;
				}
				Enumeration<String> values = req.getParameterNames();
				String name, value, id="";
				os.println("<html>");

				while (values.hasMoreElements()) {
						name = values.nextElement().trim();
						value = req.getParameter(name).trim();
						if (name.equals("userid")) {
								username = value.toLowerCase();
						}
				}
				try {
						//
						User user = getUser(username);
						//
						// add the user to the session
						//
						if(user != null){
								HttpSession session = req.getSession(true);
								if(session != null){
										session.setAttribute("user",user);
								}
								os.println("<head><title></title><META HTTP-EQUIV=\""+
														"refresh\" CONTENT=\"0; URL=" + url +
														"welcome.action" + 
														"\"></head>");								
								os.println("<body>");
						}
						else{
								os.println("<head><title></title><body>");
								os.println("<h2>Unauthorized access </h2>");
								os.println("<h3>Please try again </h3>");
						}
						os.println("</body>");
						os.println("</html>");
				}
				catch (Exception ex) {
						System.err.println(""+ex);
						os.println(ex);
				}
				os.flush();
				//
    }
		
		/*
     * Procesesses the login and check for authontication.
     * @param req
     * @param res
     */		
    User getUser(String username){

				boolean success = true;
				User user = null;
				String fullName="",role="",dept="", message="";
				user = new User(debug, null, username);
				String back = user.doSelect();
				if(!back.equals("")){
						message += back;
						logger.error(back);
						return null;
				}
				return user;
    }

}

