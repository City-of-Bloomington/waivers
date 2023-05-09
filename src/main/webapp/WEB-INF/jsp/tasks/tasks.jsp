<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
<table class="stat" border="1" width="95%">
	-->

<details>
	<summary><s:property value="#tasksTitle" /></summary>	
	<table class="fn1-table">	
		<thead>
			<tr>
				<th align="center"><b>Task </b></th>
				<s:if test="#showWaiver">
					<th align="center"><b>Waiver #</b></th>
				</s:if>
				<th align="center"><b>Start Date</b></th>
				<th align="center"><b>Completed Date</b></th>			
				<th align="center"><b>Claimed By</b></th>
				<th align="center"><b>Notes</b></th>
			</tr>
		</thead>
		<tbody>
			<s:iterator var="one" value="#tasks">
				<tr>
					<td><a href="<s:property value='#application.url' />task.action?task_id=<s:property value='task_id' />"><s:property value="name" /></a></td>
					<s:if test="#showWaiver">
						<td><a href="<s:property value='#application.url' />waiver.action?id=<s:property value='waiver_id' />"><s:property value="waiver.waiverNum" /></a></td>
					</s:if>
					<td><s:property value="start_date" /></td>				
					<td><s:property value="completed_date" /></td>
					<td><s:property value="claimed_user" /></td>
					<td><s:property value="field2_value" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</details>
