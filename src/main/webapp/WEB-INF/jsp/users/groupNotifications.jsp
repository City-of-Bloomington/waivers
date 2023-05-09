<table class="fn1-table">
	<caption><s:property value="#groupNotificationsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>When Completed Workflow Step</b></th>
			<th align="center"><b>Group to Be Notified</b></th>
			<th align="center"><b>Active</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#groupNotifications">
			<tr>
				<td><a href="<s:property value='#application.url' />groupNotification.action?id=<s:property value='id' />"> Edit <s:property value="id" /></a></td>
				<td><s:property value="step" /></td>				
				<td><s:property value="group" /></td>				
				<td><s:if test="isActive()">Yes</s:if><s:else>No</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
