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


public class Logout extends HttpServlet{

    String url = "", cas_url = "";
		static final long serialVersionUID = 200L;	
    public void doGet(HttpServletRequest req, 
											HttpServletResponse res) 
				throws ServletException, IOException{

				res.setContentType("text/html");
				PrintWriter out = res.getWriter();
				Enumeration values = req.getParameterNames();
				String name= "";
				String value = "";
				String id = "";
				while (values.hasMoreElements()) {
						name = ((String)values.nextElement()).trim();
						value = req.getParameter(name).trim();
						if (name.equals("id"))
								id = value;	
				}	
				if(url.equals("")){
						url    = getServletContext().getInitParameter("url");
						cas_url = getServletContext().getInitParameter("cas_url");
				}
				HttpSession session = null;
				session = req.getSession(false);
				if(session != null){
						session.invalidate();
				}
				res.sendRedirect(cas_url);	 
				return;
		}
}






















































