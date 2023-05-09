<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<details>
  <summary><s:property value="attachmentsTitle" /></summary>
	<table border="1" width="95%">
		<tr>
			<td align="center"><b>File</b></td>
			<td align="center"><b>Type</b></td>		
			<td align="center"><b>Date</b></td>
			<td align="center"><b>Notes</b></td>
			<s:if test="#session.user.canDelete()">
				<td align="center"><b>Action</b></td>
			</s:if>
		</tr>
		<s:iterator value="#uploads">
			<tr>
				<td><a href="<s:property value='#application.url' />doDownload?id=<s:property value='id' />"><s:property value="old_file_name" /></a></td>
				<td><s:property value="type" /></td>				
				<td><s:property value="date" /></td>
				<td><s:property value="notes" /></td>
				<s:if test="#session.user.canDelete()">
					<td><a href="<s:property value='#application.url' />doUpload.action?id=<s:property value='id' />&action=Delete" onclick="return verifyDelete();">Delete File</a></td>
				</s:if>
			</tr>
		</s:iterator>
	</table>
</details>






































