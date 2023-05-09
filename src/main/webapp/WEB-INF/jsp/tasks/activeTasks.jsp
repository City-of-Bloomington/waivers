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

<s:if test="tasks != null" >
	<h3> Your active task list </h3>
	<table class="fn1-table">
		<caption><s:property value="tasksTitle" /></caption>				
		<thead>
			<tr>
				<th align="center"><b>Task</b></th>
				<th align="center"><b>Waiver #</b></th>
				<th align="center"><b>Start Date</b></th>
				<th align="center"><b>Claimed By</b></th>
				<th align="center"><b>Notes</b></th>
			</tr>
		</thead>
		<tbody>
			<s:iterator var="one" value="tasks">
				<tr>
					<td><a href="<s:property value='#application.url' />task.action?task_id=<s:property value='task_id' />"><s:property value="name" /></a></td>
					<td><a href="<s:property value='#application.url' />waiver.action?id=<s:property value='waiver_id' />"><s:property value="waiver.waiverNum" /></a></td>
					<td><s:property value="start_date" /></td>				
					<td><s:property value="claimed_user" /></td>
					<td><s:property value="field2_value" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:if>
<s:else>
	<p> You do not have active task list </p>
</s:else>

<%@  include file="../gui/footer.jsp" %>

