<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="tt-row-container">
	<s:if test="waiver.isBusiness()" >
		<dl class="fn1-output-field">
			<dt>Is Business? </dt>
			<dd>Yes</dd>
		</dl>
	</s:if>
	<s:if test="waiver.isTrust()" >
		<dl class="fn1-output-field">
			<dt>Is Trust? </dt>
			<dd>Yes</dd>
		</dl>
	</s:if>	
	<s:if test="waiver.hasEntities()">
		<s:set var="entitiesTitle" value="'Entities(s)'" />
		<s:set var="entities" value="%{waiver.entities}" />
		<%@  include file="../entities/entitiesBasic.jsp" %>
	</s:if>	
	<div class="tt-split-container">
		<dl class="fn1-output-field">
			<dt>Waiver County ID </dt>
			<dd><s:property value="%{waiver.countyWaiverId}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Service address(es) </dt>
			<dd><s:property value="%{waiver.addressInfo}" /> <s:if test="waiver.invalidAddr" >(Invalid) </s:if> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Legal description </dt>
			<dd><s:property value="%{waiver.legalDescription}"  /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Parcel Pin </dt>
			<dd><s:property value="%{waiver.parcelPin}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Parcel Tax ID </dt>
			<dd><s:property value="%{waiver.parcelTaxId}" /> </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Waiver Instrument # </dt>
			<dd><s:property value="%{waiver.waiverInstrumentNum}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Deed Instrument # </dt>
			<dd><s:property value="%{waiver.deedInstrumentNum}" /> </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Waiver Book/Page </dt>
			<dd><s:property value="%{waiver.waiverBook}" />/<s:property value="%{waiver.waiverPage}" /></dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Deed Book/Page </dt>
			<dd><s:property value="%{waiver.deedBook}" />/<s:property value="%{waiver.deedPage}" /></dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Township Section Range </dt>
			<dd><s:property value="%{waiver.secTwpRangeDir}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Development </dt>
			<dd><s:property value="%{waiver.developmentSubdivision}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Lot </dt>
			<dd><s:property value="%{waiver.lot}"  /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Acreage </dt>
			<dd><s:property value="%{waiver.acrage}"  /> </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>IN/OUT City? </dt>
			<dd><s:property value="%{waiver.inOutCity}" /></dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>IN GIS? </dt>
			<dd><s:if test="waiver.inGis" > Yes</s:if><s:else> No</s:else></dd> 
		</dl>				
		<s:if test="waiver.hasNotes()">
			<dl class="fn1-output-field">
				<dt>Waiver Notes </dt>
				<dd><s:property value="%{waiver.notes}"  /> </dd>
			</dl>
		</s:if>
		<dl class="fn1-output-field">
			<dt>Request date </dt>
			<dd><s:property value="%{waiver.date}" /> 
			</dd>
		</dl>
		<s:if test="waiver.needExpireDate()">		
			<dl class="fn1-output-field">
				<dt>Expire date </dt>
				<dd><s:property value="%{waiver.expireDate}" /> 
				</dd>
			</dl>
		</s:if>
		<dl class="fn1-output-field">
			<dt>Signed date </dt>
			<dd><s:property value="%{waiver.signedDate}"  /> </dd>
		</dl>		
	</div>
	<div class="tt-split-container">
		<dl class="fn1-output-field">
			<dt>Recorder ID </dt>
			<dd><s:property value="%{waiver.recorderId}"  /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Recorded date </dt>
			<dd><s:property value="%{waiver.recorderDate}"  /> </dd>
		</dl>
		<s:if test="waiver.recorder_notes">
			<dl class="fn1-output-field">
				<dt>Recorder notes </dt>
				<dd><s:property value="%{waiver.recorderNotes}"  /> </dd>
			</dl>
		</s:if>
		<dl class="fn1-output-field">
			<dt>Paper verified </dt>
			<dd><s:property value="%{waiver.paperVerifiedDate}"  /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Controller notes </dt>
			<dd><s:property value="%{waiver.controllerNotes}"  /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Mapped date </dt>
			<dd><s:property value="%{waiver.mappedDate}"  /> </dd>
		</dl>
		<s:if test="waiver.hasGisNotes()">
			<dl class="fn1-output-field">
				<dt>GIS notes </dt>
				<dd><s:property value="%{waiver.gisNotes}"  /> </dd>
			</dl>
		</s:if>
		<details>
			<summary>Other Info</summary>	
			<s:if test="waiver.hasAdded_by()">
				<dl class="fn1-output-field">
					<dt>Added by </dt>
					<dd><s:property value="%{waiver.addedByUser}" /> 
					</dd>
				</dl>
			</s:if>
			<s:if test="waiver.imported" > 		
				<dl class="fn1-output-field">
					<dt>Imported </dt>
					<dd>Yes</dd> 
				</dl>
			</s:if>
			<dl class="fn1-output-field">
				<dt>Status </dt>
				<dd><s:property value="%{waiver.status}" /> 
				</dd>
			</dl>
			<s:if test="waiver.isClosed()">
				<dl class="fn1-output-field">
					<dt>Closed date </dt>
					<dd><s:property value="%{waiver.closedDate}" /> </dd>
				</dl>
				<dl class="fn1-output-field">
					<dt>Closed by </dt>
					<dd><s:property value="%{waiver.closedByUser}" /></dd>
				</dl>		
			</s:if>
		</details>
	</div>
</div>


