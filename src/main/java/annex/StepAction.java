package annex;
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
import org.apache.log4j.Logger;

public class StepAction extends TopAction{

		static final long serialVersionUID = 290L;	
		static Logger logger = Logger.getLogger(StepAction.class);
		//
		User user = null;
		Step step = null;
		List<Step> steps = null;
		List<Type> groups = null;
		String stepsTitle = "Current Workflow Steps";
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
						}	
				}
				if(action.equals("Save")){
						back = step.doSave();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Added Successfully");
						}
				}				
				else if(action.startsWith("Save")){ 
						back = step.doUpdate();
						if(!back.equals("")){
								addActionError(back);
						}
						else{
								addActionMessage("Updated Successfully");
						}
				}
				else{		
						getStep();
						if(!id.equals("")){
								back = step.doSelect();
								if(!back.equals("")){
										addActionError(back);
								}								
						}
				}
				return ret;
		}
		public Step getStep(){ 
				if(step == null){
						step = new Step();
						step.setId(id);
				}		
				return step;
		}

		public void setStep(Step val){
				if(val != null){
						step = val;
				}
		}

		public String getStepsTitle(){
				return stepsTitle;
		}
		public void setAction2(String val){
				if(val != null && !val.equals(""))		
						action = val;
		}
		public List<Step> getSteps(){
				if(steps == null){
						StepList tl = new StepList(debug);
						String back = tl.find();
						if(back.equals("")){
								List<Step> ones = tl.getSteps();
								if(ones != null && ones.size() > 0){
										steps = ones;
								}
						}
				}
				return steps;
		}
		public List<Type> getGroups(){
				if(groups == null){
						TypeList tl = new TypeList(debug, null, "groups");
						String back = tl.find();
						if(back.equals("")){
								List<Type> ones = tl.getTypes();
								if(ones != null && ones.size() > 0){
										groups = ones;
								}
						}
				}
				return groups;
		}		

}





































