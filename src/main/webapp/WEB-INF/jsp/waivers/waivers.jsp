<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
<table class="stat" border="1" width="95%">
	-->

<table class="fn1-table">	
	<caption><s:property value="#waiversTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>Waiver #</b></th>			
			<th align="center"><b>Waiver Instrum. #</b></th>
			<th align="center"><b>Deed Instrum. #</b></th>			
			<th align="center"><b>Entity(ies)</b></th>
			<th align="center"><b>Legal Desc</b></th>
			<th align="center"><b>Parcel Tax ID/PIN</b></th>			
			<th align="center"><b>Service Address</b></th>
			<th align="center"><b>In/Out City</b></th>
			<th align="center"><b>Added By</b></th>
			<th align="center"><b>Date</b></th>
			<th align="center"><b>Status</b></th>
			<th align="center"><b>Notes</b></th>			
			<th align="center"><b>Attachments</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#waivers">
			<tr>
				<td><a href="<s:property value='#application.url' />waiver.action?waiverNum=<s:property value='waiverNum' />"><s:property value="waiverNum" /></a></td>
				<td><s:property value="waiverInstrumentNnum" /></td>
				<td><s:property value="deedInstrumentNnum" /></td>				
				<td><s:property value="entitiesInfo" /></td>
				<td><s:property value="legalDescription" /></td>
				<td><s:property value="parcelTaxId" /> <s:property value="parcelPin" /> </td>
				<td><s:property value="addressInfo" /></td>
				<td><s:property value="inOutCity" /></td>
				<td><s:property value="addedByUser" /></td>
				<td><s:property value="date" /></td>
				<td><s:property value="status" /></td>
				<td><s:property value="notes" /></td>				
				<td><s:property value="uploadCount" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
