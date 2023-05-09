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

public class WorkFlowAction extends TopAction{

    static final long serialVersionUID = 290L;	
    static Logger logger = LogManager.getLogger(WorkFlowAction.class);
    //
    User user = null;
    List<Step> steps = null;
    List<Step> nextSteps = null;		
    List<WorkFlow> workFlows = null;
    WorkFlow workFlow = null;
    String workFlowsTitle = "Current workflows";
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
		logger.error(ex);
	    }	
	}
	if(action.equals("Save")){
	    logger.debug("save");
	    back = workFlow.doSave();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		addActionMessage("Save Successfully");
	    }
	}				
	else if(action.startsWith("Save")){
	    logger.debug("update");
	    back = workFlow.doUpdate();
	    if(!back.equals("")){
		addActionError(back);
		logger.error(back);
	    }
	    else{
		addActionMessage("Updated Successfully");
	    }
	}
	else{		
	    getWorkFlow();
	    if(!id.equals("")){
		back = workFlow.doSelect();
		if(!back.equals("")){
		    addActionError(back);
		    logger.error(back);
		}		
	    }
	}
	return ret;
    }
    public WorkFlow getWorkFlow(){ 
	if(workFlow == null){
	    workFlow = new WorkFlow(debug, id);
	}		
	return workFlow;
    }

    public void setWorkFlow(WorkFlow val){
	if(val != null){
	    workFlow = val;
	}
    }

    public String getWorkFlowsTitle(){
	return workFlowsTitle;
    }

    public void setAction2(String val){
	if(val != null && !val.equals(""))		
	    action = val;
    }
    public List<WorkFlow> getWorkFlows(){
	logger.debug("workflows list");
	if(workFlows == null){
	    WorkFlowList tl = new WorkFlowList(debug);
	    String back = tl.find();
	    if(back.equals("")){
		List<WorkFlow> ones = tl.getWorkFlows();
		if(ones != null && ones.size() > 0){
		    workFlows = ones;
		}
	    }
	    else{
		logger.error(back);
	    }
	}
	return workFlows;
    }
    // start steps
    public List<Step> getSteps(){
	logger.debug("steps list");
	if(steps == null){
	    StepList tl = new StepList(debug);
	    tl.setExclude_step("Completed");
	    String back = tl.find();
	    if(back.equals("")){
		List<Step> ones = tl.getSteps();
		if(ones != null && ones.size() > 0){
		    steps = ones;
		}
	    }
	    else{
		logger.error(back);
	    }
	}
	return steps;
    }
    public List<Step> getNextSteps(){
	logger.debug(" next steps");
	if(nextSteps == null){
	    StepList tl = new StepList(debug);
	    tl.setExclude_step("Start");
	    String back = tl.find();
	    if(back.equals("")){
		List<Step> ones = tl.getSteps();
		if(ones != null && ones.size() > 0){
		    steps = ones;
		}
	    }
	    else{
		logger.error(back);
	    }
	}
	return steps;
    }		

}





































