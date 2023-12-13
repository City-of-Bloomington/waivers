package annex.action;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;  
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;  
import org.apache.struts2.action.ServletContextAware;  
import javax.servlet.http.HttpServletResponse;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

public class LogoutAction extends ActionSupport implements ServletContextAware{

    private static boolean debug = false;
    private static final long serialVersionUID = 210L;
    private ServletContext ctx;	
    @Override
    public String execute(){
	try{
	    String cas_url = ctx.getInitParameter("cas_url");
	    HttpServletRequest request = ServletActionContext.getRequest();  
	    HttpSession session=request.getSession();
	    if(session != null)
		session.invalidate();
	    HttpServletResponse res = ServletActionContext.getResponse();
	    res.sendRedirect(cas_url);
	    return super.execute();				
	}catch(Exception ex){
	    System.out.println(ex);
	}
	return SUCCESS;
    }     
    @Override  	
    public void withServletContext(ServletContext ctx) {  
        this.ctx = ctx;  
    }      
}



