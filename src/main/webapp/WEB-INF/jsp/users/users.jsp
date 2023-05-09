<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#usersTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Username</b></th>
			<th align="center"><b>Full Name</b></th>
			<th align="center"><b>Role</b></th>
			<th align="center"><b>Dept</b></th>
			<th align="center"><b>Active Email</b></th>
			<th align="center"><b>Active</b></th>			
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#users">
			<tr>
				<td><a href="<s:property value='#application.url' />user.action?id=<s:property value='id' />"> <s:property value="id" /></a></td>
				<td><s:property value="username" /></td>				
				<td><s:property value="fullName" /></td>
				<td><s:property value="roleInfo" /></td>
				<td><s:property value="dept" /></td>
				<td><s:if test="activeMail">Yes</s:if><s:else>No</s:else></td>
				<td><s:if test="inactive">No</s:if><s:else>Yes</s:else></td>				
			</tr>
		</s:iterator>
	</tbody>
</table>
