<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="task" id="form_id" method="post">
    <s:hidden name="action2" id="action2" value="" />
    <h2>Perform Task: <s:property value="task.name" /></h2>
    <s:hidden name="task.task_id" value="%{task.task_id}" />
    <s:hidden name="task.step_id" value="%{task.step_id}" />
    <s:hidden name="waiver_id" value="%{task.waiver_id}" />
    <s:hidden name="task.waiver_id" value="%{task.waiver_id}" />
    <s:if test="task.claimed_by !=''">
	<s:hidden name="task.claimed_by" value="%{task.claimed_by}" />		
    </s:if>
    <s:if test="hasActionErrors()">
	<div class="errors">
	    <s:actionerror/>
	</div>
    </s:if>
  <s:elseif test="hasActionMessages()">
      <div class="welcome">
	  <s:actionmessage/>
      </div>
  </s:elseif>
  <p>* Required field <br />
  </p>
  <details>
      <summary>Instructions</summary>
      <ul>
	  <li>If you make any change, please hit the 'Save Changes' button</li>
	  <li>If all the requirements for this task are met, click on 'Completed' button so that the workflow will move to next task (if any).</li>
	  <li>You can add files by clicking on 'New Attachment'</li>
      </ul>
  </details>
  <s:if test="task.require_upload && !task.canBeCompleted()">
      <P>Requirement: You need to upload a file to complete this task </p>
	</s:if>		
	<div class="tt-row-container">
	    <dl class="fn1-output-field">
		<dt>Related Waiver</dt>
		<dd><a href="<s:property value='#application.url' />waiver.action?id=<s:property value='task.waiver_id' />"><s:property value="task.waiver.waiverNum" /></a></dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>Waiver Info </dt>
		<dd><s:property value="task.waiver.basicInfo" /></dd>
		<dd><s:property value="task.waiver.basicInfo2" /></dd>
		<dd><s:property value="task.waiver.basicInfo3" /></dd>
	    </dl>		
	    <dl class="fn1-output-field">
		<dt>Start Date </dt>
		<dd><s:property value="task.start_date" />
		</dd>
	    </dl>
	    <s:if test="task.isClaimed()">
		<dl class="fn1-output-field">
		    <dt>Claimed by </dt>
		    <dd><s:property value="task.claimed_user" /> </dd>
		</dl>
	    </s:if>
	    <s:if test="task.hasSecondField()">
		<dl class="fn1-output-field">
		    <dt>Task <s:property value="%{task.field2_name}" /> </dt>
		    <dd><s:textarea name="task.field2_value" value="%{task.field2_value}" rows="10" cols="70" /> </dd>
		</dl>
	    </s:if>
	    <s:if test="task.hasPartName()">
		<s:if test="task.part_name == 'legal'">
		    <s:include value="../waivers/legalPart.jsp" />
		</s:if>
		<s:elseif test="task.part_name == 'recorder'">
		    <s:include value="../waivers/recorderPart.jsp" />
		</s:elseif>
		<s:elseif test="task.part_name == 'gis'">
		    <s:include value="../waivers/gisPart.jsp" />
		</s:elseif>
	    </s:if>
	    <dl class="fn1-output-field">
		<dt>Related Group </dt>
		<dd><s:property value="task.groupName" /> </dd>
	    </dl>
	    <s:if test="task.isCompleted()">
		<dl class="fn1-output-field">
		    <dt>Status </dt>
		    <dd>Completed on <s:property value="%{task.completed_date}" /> 
		    </dd>
		</dl>
	    </s:if>		
	</div>
	<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
	<s:if test="task.canBeCompleted()">
	    <s:submit name="action" type="button" value="Task Completed" class="fn1-btn" title="If no more actions is needed for this task click completed to move to next task"/>
	</s:if>
	<s:if test="task.waiver.canBePrinted()">
	    <a href="<s:property value='#application.url' />WaiverRtf?id=<s:property value='task.waiver_id' />" class="fn1-btn">Printable Waiver</a>
	</s:if>
	<a href="<s:property value='#application.url' />doUpload.action?waiver_id=<s:property value='task.waiver_id' />&task_id=<s:property value='task.task_id' />" class="fn1-btn">New Attachment</a>
	<s:if test="task.waiver.hasCompletedTasks()" >
	    <s:set var="tasksTitle" value="'Completed Tasks'" />
	    <s:set var="tasks" value="%{task.waiver.completedTasks}" />
	    <%@  include file="../tasks/tasks.jsp" %>			
	</s:if>
	<s:if test="task.waiver.hasUploads()">
	    <s:set var="attachmentsTitle" value="'Attachments'" />
	    <s:set var="uploads" value="%{task.waiver.uploads}" />
	    <%@  include file="../attachments/fileUploads.jsp" %>			
	</s:if>
</s:form>
<s:if test="hasEmailLogs()">
	<s:set var="logsTitle" value="'Email Logs'" />
	<s:set var="emailLogs" value="%{emailLogs}" />
	<%@  include file="../logs/emailLogs.jsp" %>				
</s:if>

<%@  include file="../gui/footer.jsp" %>

