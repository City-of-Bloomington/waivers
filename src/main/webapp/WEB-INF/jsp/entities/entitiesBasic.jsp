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
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#entities">
			<tr>
				<td><s:property value="id" /></td>
				<td><s:property value="name" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
