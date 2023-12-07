package annex.web;
/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */
import java.net.URI;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.utils.*;

public class TopServlet extends HttpServlet {
    static String url = "";
    static String cookieName="", cookieValue="";
    static String server_path="", attorneyFullName="", paraLegalName="";
    static boolean debug = false, production=false;
    static Configuration config = null;
    static Logger logger = LogManager.getLogger(TopServlet.class);
    static ServletContext context = null;
    static String endpoint_logout_uri = ""; // https://auth.bloomington.in.gov/adfs/oauth2/logout?post_logout_redirect_uri=https%3A%2F%2Foutlaw.bloomington.in.gov%2Fwaivers
    //
    // id_token_hint = id_token
    // or logout_hint = sid session id from aoth
    //
    String end_point_session_id = ""; // sid from token info
    public void init(ServletConfig conf){
	try{
	    context = conf.getServletContext();
	    url = context.getInitParameter("url");
	    String str = context.getInitParameter("debug");
	    if(str != null && str.equals("true")) debug = true;
	    str = context.getInitParameter("server_path");
	    if(str != null) server_path = str;	    
	    str = context.getInitParameter("cookieName");
	    if(str != null)
		cookieName = str;
	    str = context.getInitParameter("cookieValue");
	    if(str != null)
		cookieValue = str;
	    str = context.getInitParameter("attorneyFullName");
	    if(str != null)
		attorneyFullName = str;
	    str = context.getInitParameter("paraLegalName");
	    if(str != null)
		paraLegalName = str;	    
	    String username = context.getInitParameter("adfs_username");
	    String auth_end_point = context.getInitParameter("auth_end_point");
	    String token_end_point = context.getInitParameter("token_end_point");
	    String callback_uri = context.getInitParameter("callback_uri");
	    String client_id = context.getInitParameter("client_id");
	    String client_secret = context.getInitParameter("client_secret");
	    String scope = context.getInitParameter("scope");
	    String discovery_uri = context.getInitParameter("discovery_uri");
	    str  = context.getInitParameter("endpoint_logout_uri");
	    if(str != null)
		endpoint_logout_uri = str;
	    config = new
		Configuration(auth_end_point, token_end_point, callback_uri, client_id, client_secret, scope, discovery_uri, username);
	    // System.err.println(config.toString());
	}catch(Exception ex){
	    System.err.println(" top init "+ex);
	    logger.error(" "+ex);
	}
    }

}
