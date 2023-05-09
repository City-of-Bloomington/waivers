<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#entitiesTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>ID</b></th>
			<th align="center"><b>Name</b></th>
			<th align="center"><b>Title</b></th>			
			<th align="center"><b>Business?</b></th>
			<th align="center"><b>Trust?</b></th>			
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#entities">
			<tr>
				<td>
				<s:if test="#noEdit">
					<s:property value="id" />
				</s:if>
				<s:else>
					<a href="<s:property value='#application.url' />entity.action?id=<s:property value='id' />">Edit</a>
				</s:else>
				</td>
				<td><s:property value="name" /></td>
				<td><s:property value="title" /></td>				
				<td><s:if test="isBusiness">Yes</s:if></td>
				<td><s:if test="isTrust">Yes</s:if></td>							
			</tr>
		</s:iterator>
	</tbody>
</table>
