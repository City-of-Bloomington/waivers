
<details>
	<summary>Related Notifications</summary>	
	<table class="fn1-table">
		<thead>
			<tr>
				<th align="center"><b>Date</b></th>			
				<th align="center"><b>From</b></th>
				<th align="center"><b>To</b></th>
				<th align="center"><b>CC</b></th>				
				<th align="center"><b>Subject</b></th>
				<th align="center"><b>Message</b></th>
				<th align="center"><b>Send Action</b></th>
				<th align="center"><b>Errors (if failed)</b></th>			
			</tr>
		</thead>
		<tbody>
			<s:iterator var="one" value="#emailLogs" status="stat" >
				<tr>
					<td><s:property value="date" /></td>
					<td><s:property value="fromUser" /></td>
					<td><s:property value="toUser" /></td>
					<td><s:property value="ccUsers" /></td>					
					<td><s:property value="subject" /></td>
					<td><s:property value="msg" /></td>
					<td><s:if test="isSuccess()">Success</s:if><s:else>Failed</s:else></td>
					<td><s:property value="emailErrors" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</details>
