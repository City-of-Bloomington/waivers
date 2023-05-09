package annex.action;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;  
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.list.*;
import annex.utils.*;

public class EntityAction extends TopAction{

    static final long serialVersionUID = 315L;	
    static Logger logger = LogManager.getLogger(EntityAction.class);
    //
    String name = "", type="", waiver_id="";
    Entity entity = null;
    List<Entity> entities = null;
    String entitiesTitle = "Entities";
    public String execute(){
	String ret = SUCCESS;
	String back = doPrepare();
	if(!back.equals("")){
	    try{
		HttpServletResponse res = ServletActionContext.getResponse();
		String str = url+"Login";
		res.sendRedirect(str);
		return super.execute();
	    }catch(Exception ex){
		System.err.println(ex);
		logger.error(ex);
	    }	
	}
	if(!type.equals("")){
	    ret = "popup";
	}
	if(action.equals("Save")){
	    logger.debug(" action save ");
	    back = entity.doSave();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		id = entity.getId();
		addActionMessage("Saved Successfully");
	    }
	}				
	else if(action.equals("Save Changes")){
	    logger.debug(" action save changes ");
	    back = entity.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		addActionMessage("Updated Successfully");
	    }
	}
	else if(action.equals("Remove")){
	    logger.debug(" action remove ");
	    back = entity.doRemove();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		addActionMessage("Entity removed Successfully");
		try{
		    HttpServletResponse res = ServletActionContext.getResponse();
		    String str = url+"waiver.action?action=Edit&id"+waiver_id;
		    res.sendRedirect(str);
		    return super.execute();
		}catch(Exception ex){
		    logger.error(ex);
		}									
	    }
	}				
	else if(action.equals("Delete")){
	    /*
	    // no delete for now
	    back = entity.doDelete();
	    if(!back.equals("")){
	    addActionError(back);
	    }
	    else{
	    addActionMessage("Deleted Successfully");								
	    ret = "search";
	    }
	    */
	}
	else if(action.equals("Edit")){
	    getEntity();
	}
	else if(!id.equals("")){
	    ret = "view";
	    getEntity();
	}
	else{
	    getEntity();
	}
	return ret;
    }
    public Entity getEntity(){ 
	if(entity == null){
	    logger.debug(" get entity");	
	    if(!id.equals("")){
		entity = new Entity(debug, id);
		String back = entity.doSelect();
		if(!back.equals("")){
		    addActionError(back);
		    logger.error(back);	
		}
	    }
	    else{
		entity = new Entity();
	    }
	}
	if(!waiver_id.equals(""))
	    entity.setWaiver_id(waiver_id);
	return entity;
    }

    public void setEntity(Entity val){
	if(val != null)
	    entity = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setWaiver_id(String val){
	if(val != null)
	    waiver_id = val;
    }		
    public String getEntitiesTitle(){
	return entitiesTitle;
    }		
    public void setAction2(String val){
	if(val != null && !val.equals(""))		
	    action = val;
    }
    public void setType(String val){
	if(val != null && !val.equals(""))		
	    type = val;
    }		
    //
    // we can use to get entity list for auto_complete
    public List<Entity> getEntities(){ 
	if(entities == null){
	    logger.debug(" get entities");						
	    EntityList dl = new EntityList();
	    String back = dl.find();
	    if(back.equals("")){
		entities = dl.getEntities();
	    }
	    else{
		logger.error(back);
	    }
	}		
	return entities;
    }


}





































