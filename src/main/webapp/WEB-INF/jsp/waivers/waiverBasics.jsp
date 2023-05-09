<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<table class="fn1-table">	
	<caption><s:property value="#waiversTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>Waiver #</b></th>			
			<th align="center"><b>Deed Instrument #</b></th>
			<th align="center"><b>Entity(ies)</b></th>
			<th align="center"><b>Legal Desc</b></th>
			<th align="center"><b>Parcel Tax ID/PIN</b></th>			
			<th align="center"><b>Service Address(es)</b></th>
			<th align="center"><b>In/Out City</b></th>
			<th align="center"><b>In GIS</b></th>
			<th align="center"><b>Date</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#waivers">
			<tr>
				<td><a href="<s:property value='#application.url' />waiver.action?id=<s:property value='id' />"><s:property value="waiverNum" /></a></td>
				<td><s:property value="deedInstrumentNum" /></td>
				<td><s:property value="entitiesInfo" /></td>
				<td><s:property value="legalDescription" /></td>
				<td><s:property value="parcelTaxId" /></td>
				<td><s:property value="addressInfo" /></td>
				<td><s:property value="inOutCity" /></td>				
				<td><s:if test="inGis">Yes</s:if><s:else>No</s:else></td>
				<td><s:property value="date" /></td>
				
			</tr>
		</s:iterator>
	</tbody>
</table>
