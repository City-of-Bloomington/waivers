<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#addressesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Address</b></th>
			<th align="center"><b>Invalid?</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#addresses">
			<tr>
				<td>
				<s:if test="#noEdit">
					<s:property value="id" />
				</s:if>
				<s:else>
					<a href="<s:property value='#application.url' />address.action?id=<s:property value='id' />">Edit</a>
				</s:else>
				</td>
				<td><s:property value="streetAddress" /></td>
				<td><s:if test="invalid">Yes</s:if></td>				
			</tr>
		</s:iterator>
	</tbody>
</table>
