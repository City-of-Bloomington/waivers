<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#stepsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b></b></th>
			<th align="center"><b>Name</b></th>
			<th align="center"><b>Field Name</b></th>
			<th align="center"><b>Field Name</b></th>
			<th align="center"><b>Part Name</b></th>
			<th align="center"><b>Group Assignment</b></th>
			<th align="center"><b>Attachment Required</b></th>
			<th align="center"><b>Suggested Upload Type</b></th>
			<th align="center"><b>Inactive</b></th>						
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#steps" status="stat" >
			<tr>
				<td><a href="<s:property value='#application.url' />step.action?id=<s:property value='id' />"><s:property value="#stat.count" />-Edit </a></td>
				<td><s:property value="name" /></td>
				<td><s:property value="field_name" /></td>
				<td><s:property value="field2_name" /></td>
				<td><s:property value="part_name" /></td>
				<td><s:property value="group" /></td>
				<td><s:property value="require_upload" /></td>
				<td><s:property value="suggested_upload_type" /></td>				
				<td><s:property value="inactive" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
