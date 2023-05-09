<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="waiver" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<h1>Edit Waiver <s:property value="waiver.waiverNum" /></h1>
	<s:hidden name="waiver.id" value="%{waiver.id}" />
	<s:hidden name="waiver.date" value="%{waiver.date}" />
	<s:hidden name="waiver.status" value="%{waiver.status}" />
	<s:hidden name="waiver.addedBy" value="%{waiver.addedBy}" />
	<s:hidden name="waiver.addAddrIds" id="add_addr_ids" value="" />
	<s:hidden name="waiver.addEntityIds" id="entity_ids" value="" />	
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
			<li>You can remove one or more of current entities (if any) from the waiver by clicking on remove button next to their name</li>
			<li>You can edit entity name and type by clicking on 'Edit' next to their name</li>
			<li>You can add a new address to the waiver by clicking on 'New Address'</li>
			<li>You can edit an address by clicking on 'Edit' button next to the address</li>
			<li>You may delete and address by clicking on 'Remove' button next to the address</li>
			
			<li>You can add attachment files by clicking on 'Attachment' </li>
			<li>You may Close this waiver if it is not valid or no more progress can be made by clicking on 'Close This Waiver' button </li>
			<li>To work on next task, click on 'Next' button </li>			
			<li>After you are done with data entry, hit the 'Save Changes' button</li>			
		</ul>
	</details>	
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt> </dt>
			<dd><b>Entity(ies) </b></dd>					
			<s:if test="waiver.hasEntities()">
				<dd>
					<table width="80%" border="1">
						<s:iterator value="waiver.entities">
							<tr>
								<td><s:property value="info" /></td>
								<td><a href="<s:property value='#application.url' />entity.action?id=<s:property value='id' />&waiver_id=<s:property value='waiver.id' />" class="fn1-btn">Edit</a>
								</td>
								<td><a href="<s:property value='#application.url'/>waiver.action?action=Remove+Entity&entityId=<s:property value='id' />&id=<s:property value='waiver.id' />" class="fn1-btn">Remove</a></td>
							</tr>
						</s:iterator>
					</table>
				</dd>
			</s:if>
			<dt> </dt>
			<dd>To add a new entity click on 'New Entity' or to add an existing entity use 'Search Entity' </dd>
			<dt>Add Entity</dt>
			<dd><button onclick="windowOpener('<s:property value='#application.url' />entity.action?type=popup&waiver_id=<s:property value='waiver.id' />','_blank', 'menubar=no,toolbar=no,location=no,toolbar=no,scrollbars=no,resizable=yes,top=500,left=500,width=500,height=500');return false;">New Entity</button>	</dd>
			<dt> </dt>			
			<dd>To search for an existing entity, start typing the name to pick from the list.</dd>
			<dt>Search Entity</dt>						
			<dd><s:textfield name="waiver.entityName" value="" size="25" maxlength="80" id="entity_name" /> Entity ID:
				<s:textfield name="waiver.addEntityId" value="" size="10" maxlength="10" id="entity_id" /> 
			</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt> </dt>
			<dd> <b>Service Address(es)</b></dd>			
			<s:if test="waiver.hasAddresses()">
				<dd>
					<table width="80%" border="1">
						<s:iterator value="waiver.addresses">
							<tr>
								<td><s:property value="addressInfo" /></td>
								<td><a href="<s:property value='#application.url' />address.action?id=<s:property value='id' />&action=Edit" class="fn1-btn">Edit</a></td>
								<td><a href="<s:property value='#application.url'/>waiver.action?addressId=<s:property value='id' />&id=<s:property value='waiver.id' />&action=Remove+Address" class="fn1-btn">Remove</a></td>
							</tr>
						</s:iterator>
					</table>
				</dd>
			</s:if>
			<dt>Add Address</dt>
			<dd><button onclick="windowOpener('<s:property value='#application.url' />address.action?type=popup&waiver_id=<s:property value='waiver.id' />','_blank', 'menubar=no,toolbar=no,location=no,toolbar=no,scrollbars=no,resizable=yes,top=500,left=500,width=500,height=500');return false;">New Address</button>
			</dd>
		</dl>
		<div class="tt-split-container">
			<dl class="fn1-output-field">
				<dt>Waiver Num </dt>
				<dd><s:textfield name="waiver.waiverNum" value="%{waiver.waiverNum}" size="10" maxlength="15"  /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Waiver Instrument # </dt>
				<dd><s:textfield name="waiver.waiverInstrumentNum" value="%{waiver.waiverInstrumentNum}" size="20" maxlength="70" /></dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Deed Instrument # </dt>
				<dd><s:textfield name="waiver.deedInstrumentNum" value="%{waiver.deedInstrumentNum}" size="20" maxlength="70" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Deed Book </dt>
				<dd><s:textfield name="waiver.deedBook" value="%{waiver.deedBook}" size="10" maxlength="30" />Deed Page <s:textfield name="waiver.deedPage" value="%{waiver.deedPage}" size="10" maxlength="30" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Waiver Book </dt>
				<dd><s:textfield name="waiver.waiverBook" value="%{waiver.waiverBook}" size="10" maxlength="30" />Waiver Page <s:textfield name="waiver.waiverPage" value="%{waiver.waiverPage}" size="10" maxlength="30" /> </dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Parcel PIN</dt>
				<dd>
					<input type="text" name="waiver.parcelPin2" value="" size="2" maxlength="2" id="pin_id_1" placeHolder="xx" class="pin_number"/>-
					<input type="text" name="waiver.parcelPin2" value="" size="2" maxlength="2" id="pin_id_2" placeHolder="xx" class="pin_number"/>-
					<input type="text" name="waiver.parcelPin2" value="" size="2" maxlength="2" id="pin_id_3" placeHolder="xx" class="pin_number"/>-
					<input type="text" name="waiver.parcelPin2" value="" size="3" maxlength="3" id="pin_id_4" placeHolder="xxx" class="pin_number"/>-
					<input type="text" name="waiver.parcelPin2" value="" size="3" maxlength="3" id="pin_id_5" placeHolder="xxx" class="pin_number"/>.
					<input type="text" name="waiver.parcelPin2" value="" size="3" maxlength="3" id="pin_id_6" placeHolder="xxx" class="pin_number"/>-
					<input type="text" name="waiver.parcelPin2" value="" size="3" maxlength="3" id="pin_id_7" placeHolder="xxx" class="pin_number"/>
				</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Parcel PIN </dt>
				<dd><s:textarea name="waiver.parcelPin" value="%{waiver.parcelPin}" rows="3" cols="25" id="pin_id_multiple" title="For parcel state pin use space to separate" /> </dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Parcel tax ID </dt>
				<dd>
					<input type="text" name="waiver.parcelTaxId2" value="" size="3" maxlength="3" id="tax_id_1" class="tax_number" placeHolder="xxx"/> -
					<input type="text" name="waiver.parcelTaxId2" value="" size="5" maxlength="5" id="tax_id_2" class="tax_number" placeHolder="xxxxx" /> -
					<input type="text" name="waiver.parcelTaxId2" value="" size="2" maxlength="2" id="tax_id_3" class="tax_number" placeHolder="xx"/> (enter one at a time)
				</dd>
			</dl>		
			<dl class="fn1-output-field">
				<dt>Parcel tax ID </dt>
				<dd><s:textarea name="waiver.parcelTaxId" value="%{waiver.parcelTaxId}" rows="3" id="tax_id_multiple" title="For multiple tax ids use space to separate"/> </dd>
			</dl>
		</div>
		<div class="tt-split-container">
			<dl class="fn1-output-field">
				<dt>Legal description </dt>
				<dd><s:textarea name="waiver.legalDescription" value="%{waiver.legalDescription}" rows="5" cols="50"  required="required" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Township Section Range </dt>
				<dd><s:textfield name="waiver.secTwpRangeDir" size="30" maxlength="50" value="%{waiver.secTwpRangeDir}" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Lot </dt>
				<dd><s:textfield name="waiver.lot" size="30" maxlength="50" value="%{waiver.lot}" /> </dd>
			</dl>
			<dl class="fn1-output-field">			
				<dt>Acreage</dt>
				<dd> <s:textfield name="waiver.acreage" size="20" maxlength="10" value="%{waiver.acreage}" />	</dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Develop/Subdiv </dt>
				<dd><s:textfield name="waiver.developmentSubdivision" size="30" maxlength="10" value="%{waiver.developmentSubdivision}" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>In/Out City</dt>
				<dd><s:radio name="waiver.inOutCity" value="%{waiver.inOutCity}" list="#{'IN':'IN','OUT':'OUT'}" /> 
				</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Waiver Notes </dt>
				<dd><s:textarea name="waiver.notes" value="%{waiver.notes}" rows="5" cols="50"  /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>GIS Notes </dt>
				<dd><s:textarea name="waiver.gisNotes" value="%{waiver.gisNotes}" rows="5" cols="50"  /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Signed Date </dt>
				<dd><s:textfield name="waiver.signedDate" value="%{waiver.signedDate}" size="10"  class="date" maxlength="10" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Recorded Date </dt>
				<dd><s:textfield name="waiver.recorderDate" value="%{waiver.recorderDate}" size="10"  class="date" maxlength="10" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Mapped Date </dt>
				<dd><s:textfield name="waiver.mappedDate" value="%{waiver.mappedDate}" size="10"  class="date" maxlength="10" /> </dd>
			</dl>
			<s:if test="waiver.needExpireDate()">			
				<dl class="fn1-output-field">
					<dt>Expire Date </dt>
					<dd><s:textfield name="waiver.expireDate" value="%{waiver.expireDate}" size="10"  class="date" maxlength="10" /> </dd>
				</dl>
			</s:if>				
			<s:if test="waiver.imported" > 		
				<dl class="fn1-output-field">
					<td></td>
					<dd>Imported from Spreadsheet </dd>
				</dl>
			</s:if>		
			<s:if test="waiver.hasAdded_by()">
				<dl class="fn1-output-field">
					<dt>Added by </dt>
					<dd><s:property value="%{waiver.addedByUser}" /> 
					</dd>
				</dl>
			</s:if>
			<dl class="fn1-output-field">
				<dt>Added date </dt>
				<dd><s:property value="%{waiver.date}" /> 
				</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Status </dt>
				<dd><s:property value="%{waiver.status}" /> 
				</dd>
			</dl>
		</div>
	</div>
	<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
	<a href="<s:property value='#application.url' />doUpload.action?waiver_id=<s:property value='waiver.id' />" class="fn1-btn">Attachments</a>
	<s:if test="waiver.canBePrinted()">
		<a href="<s:property value='#application.url'/>WaiverRtf?id=<s:property value='%{waiver.id}' />" class="fn1-btn">Printable Waiver </a>
	</s:if>
	<s:if test="waiver.status == 'Open'">
		<s:if test="waiver.hasMoreTasks()">
			<s:iterator var="one" value="waiver.tasks">
				<s:if test="canBeClaimedBy(#session.user)"> 
					<a href="<s:property value='#application.url'/>task.action?task_id=<s:property value='task_id' />" class="fn1-btn">Next: <s:property value="alias" /> (<s:property value="group" />)</a>
				</s:if>
				<s:else>
					<a href="<s:property value='#application.url'/>task.action?task_id=<s:property value='task_id' />&action=Edit" id="a_disabled"  disabled="disabled" class="fn1-btn">Next: <s:property value='alias' /> (<s:property value="group" /> disabled)</a>
				</s:else>
			</s:iterator>
		</s:if>
		<a href="<s:property value='#application.url'/>waiver.action?id=<s:property value='id' />&action=Close" class="fn1-btn" title="If you close this waiver no more changes can be done to it">Close This Waiver</a>		
	</s:if>
	<s:if test="waiver.hasCompletedTasks()" >
		<s:set var="tasksTitle" value="'Completed Tasks'" />
		<s:set var="tasks" value="%{waiver.completedTasks}" />
		<%@  include file="../tasks/tasks.jsp" %>			
	</s:if>
	<s:if test="waiver.hasUploads()">
		<s:set var="attachmentsTitle" value="'Attachments'" />
		<s:set var="uploads" value="%{waiver.uploads}" />
		<%@  include file="../attachments/fileUploads.jsp" %>			
	</s:if>
	<s:if test="hasEmailLogs()">
		<s:set var="logsTitle" value="'Email Logs'" />
		<s:set var="emailLogs" value="%{emailLogs}" />
		<%@  include file="../logs/emailLogs.jsp" %>	
	</s:if>
</s:form>
<%@  include file="../gui/footer.jsp" %>

