<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
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
<h3>Task: <s:property value='task.name' /></h3>
<details>
	<summary>Instructions</summary>
	<ul>
		<li>If this task is completed, you may click on next task (if allowed)</li>
		<li>If the task is still going on, you may edit by clicking on 'Edit' button</li>
		<li>If the task is still going on, you can add attachment by clicking on 'New Attachment'</li>
	</ul>
</details>
<s:if test="task.require_upload && !task.canBeCompleted()">
		<P>Requirement: You need to upload a file to complete this task </p>
</s:if>		
<div class="tt-row-container">
	<div class="tt-split-container">
		<dl class="fn1-output-field">
			<dt>Related Waiver </dt>
			<dd><a href="<s:property value='#application.url' />waiver.action?id=<s:property value='task.waiver_id' />"><s:property value="task.waiver.waiverNum" /></a>
		</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Waiver Info </dt>
			<dd><s:property value="task.waiver.basicInfo" /></dd>
			<dd><s:property value="task.waiver.basicInfo2" /></dd>
			<dd><s:property value="task.waiver.basicInfo3" /></dd>
		</dl>				
	</div>
	<div class="tt-split-container">	
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
		<s:if test="task.require_upload && !task.canBeCompleted()">
			<dl class="fn1-output-field">
				<dt>Requirment </dt>
				<dd>You need to attach a file to complete this task </dd>
			</dl>
		</s:if>			
		<s:if test="task.hasSecondField()">
			<s:if test="task.field2_value">		
				<dl class="fn1-output-field">
					<dt><s:property value="%{task.field2_name}" /> </dt>
					<dd><s:property value="%{task.field2_value}" /> </dd>
				</dl>
			</s:if>
		</s:if>
	</div>
</div>
<s:if test="task.hasPart()">
	<s:if test="task.part_name == 'legal'">
		<s:set var="waiver" value="%{task.waiver}" />
		<s:include value="../waivers/legalViewPart.jsp" />
	</s:if>
	<s:elseif test="task.part_name == 'recorder'">
		<s:set var="waiver" value="%{task.waiver}" />		
		<s:include value="../waivers/recorderViewPart.jsp" />
	</s:elseif>
	<s:elseif test="task.part_name == 'gis'">
		<s:set var="waiver" value="%{task.waiver}" />
		<s:include value="../waivers/gisViewPart.jsp" />
	</s:elseif>
</s:if>
<s:if test="task.isCompleted()">
	<div class="tt-row-container">
		<div class="tt-split-container">	
			<dl class="fn1-output-field">
				<dt>Status </dt>
				<dd>Completed on <s:property value="%{task.completed_date}" /> 
					</dd>
			</dl>
		</div>
	</div>
</s:if>
<s:if test="task.waiver.isOpen()">
	<a href="<s:property value='#application.url'/>task.action?task_id=<s:property value='task_id' />&action=Edit" class="fn1-btn">Edit: <s:property value='task.name' /></a>
	<s:if test="task.waiver.canBePrinted()">
		<a href="<s:property value='#application.url' />WaiverRtf?id=<s:property value='task.waiver_id' />" class="fn1-btn">Printable Waiver</a>
	</s:if>
	<a href="<s:property value='#application.url' />doUpload.action?waiver_id=<s:property value='task.waiver_id' />&task_id=<s:property value='task.task_id' />" class="fn1-btn">New Attachments</a>	
	<s:if test="task.isCompleted()">
		<s:if test="task.waiver.hasMoreTasks()">
			<s:iterator var="one" value="task.waiver.tasks">
				<a href="<s:property value='#application.url'/>task.action?task_id=<s:property value='task_id' />&action=Edit" class="fn1-btn">Next: <s:property value="alias" /> (<s:property value="group" />)</a>
			</s:iterator>
		</s:if>
	</s:if>
	<s:else>
		<s:if test="task.canBeCompleted()">
			<a href="<s:property value='#application.url'/>task.action?task_id=<s:property value='task_id' />&action=Task+Completed" class="fn1-btn">Task Completed </a>		
		</s:if>		
	</s:else>
</s:if>
<s:if test="task.waiver.hasCompletedTasks()" >
	<s:set var="tasksTitle" value="'Completed Tasks'" />
	<s:set var="tasks" value="%{task.waiver.completedTasks}" />
	<%@  include file="tasks.jsp" %>			
</s:if>
<s:if test="task.waiver.hasUploads()">
	<s:set var="attachmentsTitle" value="'Attachments'" />
	<s:set var="uploads" value="%{task.waiver.uploads}" />
	<%@  include file="../attachments/fileUploads.jsp" %>			
</s:if>
<s:if test="hasEmailLogs()">
	<s:set var="logsTitle" value="'Email Logs'" />
	<s:set var="emailLogs" value="%{emailLogs}" />
	<%@  include file="../logs/emailLogs.jsp" %>				
</s:if>
<%@  include file="../gui/footer.jsp" %>

