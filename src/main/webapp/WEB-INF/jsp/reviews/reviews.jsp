<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
<table class="stat" border="1" width="95%">
	-->

<table class="fn1-table">	
	<caption><s:property value="#reviewsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>Review ID</b></th>			
			<th align="center"><b>Waiver ID</b></th>
			<th align="center"><b>Review Date</b></th>
			<th align="center"><b>Verified?</b></th>			
			<th align="center"><b>Notes</b></th>			
			<th align="center"><b>Reviewed By</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#reviews">
			<tr>
				<td><a href="<s:property value='#application.url' />review.action?id=<s:property value='id' />"><s:property value="id" /></a></td>
				<td><s:property value="waiver_id" /></td>
				<td><s:property value="date" /></td>
				<td><s:if test="verified">Yes</s:if><s:else>No</s:else></td>
				<td><s:property value="notes" /></td>
				<td><s:property value="reviewer" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
