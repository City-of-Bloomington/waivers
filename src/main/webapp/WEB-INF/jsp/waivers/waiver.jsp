<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="waiver" id="form_id" method="post" tooltipDelay="500">
	<s:hidden name="waiver.addAddrIds" id="add_addr_ids" value="" />
	<s:hidden name="waiver.addEntityIds" id="entity_ids" value="" />	
	<h1>New Waiver</h1>
	<s:if test="hasActionErrors()">
		<div class="errors">
			<s:actionerror/>
		</div>
	</s:if>
	<s:elseif test="hasActionMessages()">
		<div class="welcome">
			<s:actionmessage/>
		</div>
	</s:elseif>
	<details>
		<summary>Instructions</summary>
		<ul>
			<li>* Required field </li>
			<li>For single parcel tax ID (xx-xx-xx-xxx-xxx.xxx-xxx format) </li>
			<li>For multiple parcel tax ID add one at a time in this box, it will be automatically appended to the box below. Multiple tax id's should be space separated.</li>
			<li>Entity name is either a person, business or trust</li>
			<li>You can add a new entity to the waiver by clicking on 'New Entity'</li>
			<li>Or you can add an existing entity to the waiver by using 'Search Entiy' field by start typing the entity name and then select from the list</li>
			<li>You can add a new address to the waiver by clicking on 'New Address'</li>			
			<li>After you are done with data entry, hit the 'Save' button</li>
		</ul>
	</details>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			To add a new entity click on 'New Entity' or to add an existing entity use 'Search Entity' 
			<dt>Add Entity</dt>
			<dd><button onclick="windowOpener('<s:property value='#application.url' />entity.action?type=popup','_blank', 'menubar=no,toolbar=no,location=no,toolbar=no,scrollbars=no,resizable=yes,top=500,left=500,width=500,height=500');return false;">New Entity</button></dd>
		</dl>
		<dl class="fn1-output-field">		
		 To add an existing entity to this waiver, start typing the name to pick from the list. To add a new entity click on 'New Entity' <br /> 			
			<dt>Search Entity </dt>
			<dd>Entity Name:<s:textfield name="waiver.entityName" value="%{waiver.entityName}" size="20" maxlength="80" id="entity_name" title="start typing entity name" /> Entity ID: <s:textfield name="waiver.addEntityId" value="%{waiver.addEntityId}" size="10" maxlength="10" id="entity_id" /></dd>
			<dd id="entity_names">&nbsp;</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Service address </dt>
			<dd><button onclick="windowOpener('<s:property value='#application.url' />address.action?type=popup','_blank', 'menubar=no,toolbar=no,location=no,toolbar=no,scrollbars=no,resizable=yes,top=500,left=500,width=500,height=500');return false;">New Address</button>
			</dd>
			<dd id="addr_bulk_id"></dd>
		</dl>
		<div class="tt-split-container">
			<dl class="fn1-output-field">
				<dt>Waiver # </dt>
				<dd><s:textfield name="waiver.waiverNum" value="%{waiver.waiverNum}" size="10" maxlength="15"  required="required" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Waiver Instrument # </dt>
				<dd><s:textfield name="waiver.waiverInstrumentNum" value="%{waiver.instrumentNum}" size="10" maxlength="15" /></dd>
			</dl>						
			<dl class="fn1-output-field">
				<dt>Deed Instrument # </dt>
				<dd><s:textfield name="waiver.deedInstrumentNum" value="%{waiver.deedInstrumentNum}" size="10" maxlength="70" title="Enter instrument number, deed book number" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Deed Book </dt>
				<dd><s:textfield name="waiver.deedBook" size="5" maxlength="10" value="%{waiver.deedBook}" />Page:<s:textfield name="waiver.deedPage" size="5" maxlength="10" value="%{waiver.deedPage}" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Parcel PIN</dt>
				<dd><input type="text" name="waiver.parcelPin2" value="" size="2" maxlength="2" id="pin_id_1" placeHolder="xx" class="pin_number"/>-
					<input type="text" name="waiver.parcelPin2" value="" size="2" maxlength="2" id="pin_id_2" placeHolder="xx" class="pin_number"/>-
					<input type="text" name="waiver.parcelPin2" value="" size="2" maxlength="2" id="pin_id_3" placeHolder="xx" class="pin_number"/>-
					<input type="text" name="waiver.parcelPin2" value="" size="3" maxlength="3" id="pin_id_4" placeHolder="xxx" class="pin_number"/>-
					<input type="text" name="waiver.parcelPin2" value="" size="3" maxlength="3" id="pin_id_5" placeHolder="xxx" class="pin_number"/>.
					<input type="text" name="waiver.parcelPin2" value="" size="3" maxlength="3" id="pin_id_6" placeHolder="xxx" class="pin_number"/>-
					<input type="text" name="waiver.parcelPin2" value="" size="3" maxlength="3" id="pin_id_7" placeHolder="xxx" class="pin_number"/></dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Parcel PIN </dt>
				<dd><s:textarea name="waiver.parcelPin" value="%{waiver.parcelPin}" rows="3" cols="25" id="pin_id_multiple" title="For parcel state pin use space to separate" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Parcel Tax ID</dt>
				<dd>
					<input type="text" name="waiver.parcelTaxId2" value="" size="3" maxlength="3" id="tax_id_1" class="tax_number" placeHolder="xxx"/> -
					<input type="text" name="waiver.parcelTaxId2" value="" size="5" maxlength="5" id="tax_id_2" class="tax_number" placeHolder="xxxxx" /> -
					<input type="text" name="waiver.parcelTaxId2" value="" size="2" maxlength="2" id="tax_id_3" class="tax_number" placeHolder="xx"/>
					(enter one at a time) </dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Parcel Tax ID </dt>
				<dd><s:textarea name="waiver.parcelTaxId" value="%{waiver.parcelTaxId}" rows="3" cols="25" id="tax_id_multiple" title="For multiple tax ids use space to separate" />(old records) </dd>
			</dl>
		</div>
		<div class="tt-split-container">
			<dl class="fn1-output-field">
				<dt>Legal description </dt>
				<dd><s:textarea name="waiver.legalDescription" value="%{waiver.legalDescription}" rows="5" cols="50"  required="required" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Township Sec. Range </dt>
				<dd><s:textfield name="waiver.secTwpRangeDir" size="30" maxlength="50" value="%{waiver.secTwpRangeDir}" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Lot </dt>
				<dd><s:textfield name="waiver.lot" size="15" maxlength="30" value="%{waiver.lot}" /></dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Acreage </dt>
				<dd><s:textfield name="waiver.acreage" size="10" maxlength="10" value="%{waiver.acreage}" />	</dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Develop./Subdiv. </dt>
				<dd><s:textfield name="waiver.developmentSubdivision" size="30" maxlength="128" value="%{waiver.developmentSubdivision}" /> </dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>In City Limits</dt>
				<dd><s:radio name="waiver.inOutCity" value="%{waiver.inOutCity}" list="#{'IN':'IN','OUT':'OUT'}" /> 
				</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Waiver Notes </dt>
				<dd><s:textarea name="waiver.notes" value="%{waiver.notes}" rows="5" cols="50"  /> </dd>
			</dl>
		</div>
	</div>
	<dl class="fn1-output-field">
		<dt></dt>
		<dd><s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
	</dl>
</s:form>
<s:if test="waivers != null && waivers.size() > 0">
	<s:set var="waiversTitle" value="waiversTitle" />
	<s:set var="waivers" value="waivers" />
	<%@  include file="waivers.jsp" %>			
</s:if>
<%@  include file="../gui/footer.jsp" %>

