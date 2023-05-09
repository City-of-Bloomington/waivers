<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="review" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<h1>Review Annexation Waiver <s:property value="review.waiver_id" /></h1>
	<s:hidden name="review.id" value="%{review.id}" />	
	<s:hidden name="waiver.id" value="%{review.waiver_id}" />
	<s:hidden name="review.waiver_id" value="%{review.waiver_id}" />	
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
  * Required field <br />
	** For multiple parcel tax ID use comma or space to separate them <br />
	<s:if test="waiver.hasOwners()">
		*** Mark checkbox and click on 'Remove Owner' to remove the owner from this waiver <br />
	</s:if>
	<div id="hide_info" style="display:block">
		<button name="hide_info" id="hide_info_button">Show Instruction</button>
	</div>
	<div id="show_info" style="display:none">
		<ul id="show_info">
			<li>If you make any change, please hit the 'Save Changes' button </li>
			<li>To add a new owner to the waiver, you need to add the owner to the system first</li>
			<li>To add an existing owner to this waiver start typing in the owner field to pick from the list</li>
			<li>You can remove one or more of current owners (if any) from the waiver by marking the checkbox infront of their name</li>
			<li>You can add attachment files by clicking on 'Attachment' </li>
		</ul>
		<button name="show_info" id="show_info_button">Hide Instruction</button>		
	</div>
	<div class="tt-row-container">
		<s:if test="waiver.hasOwners()">
			<dl class="fn1-output-field">
				<dt> </dt>
				<dd>
					<table width="60%" border="1"> <caption>Owner(s)</caption>
						<s:iterator value="waiver.owners">
							<tr>
								<td><input type="checkbox" name="waiver.del_owner" value="<s:property value='id' />" />*** </td>
								<td><s:property value="full_name" /></td>
								<td><a href="<s:property value='#application.url' />owner.action?id=<s:property value='id' />">Edit</a></td>
							</tr>
						</s:iterator>
					</table>
				</dd>
			</dl>
		</s:if>
		<dl class="fn1-output-field">
			To add an existing owner to this waiver, start typing the name to pick from the list. To add a new owner click on 'New Owner' <br /> 		
			<dt>Add Owner </dt>
			<dd><s:textfield name="waiver.owner_name" value="%{waiver.owner_name}" size="25" maxlength="80" id="owner_name" /> Owner ID:
				<s:textfield name="waiver.add_owner_id" value="%{waiver.add_owner_id}" size="10" maxlength="10" id="owner_id" /> or 
				<button onclick="windowOpener('<s:property value='#application.url' />owner.action?type=popup','_blank', 'menubar=no,toolbar=no,location=no,toolbar=no,scrollbars=no,resizable=yes,top=500,left=500,width=500,height=500');return false;">New Owner</button>				
			</dd>
		</dl>					
		<div class="tt-split-container">		
			<dl class="fn1-output-field">
				<dt>Instrument num </dt>
				<dd><s:textfield name="waiver.instrument_num" value="%{waiver.instrument_num}" size="12" maxlength="70" /> (Deed book)</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Legal description </dt>
				<dd><s:textarea name="waiver.legal_description" value="%{waiver.legal_description}" rows="5" cols="50"  /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Parcel tax ID **</dt>
				<dd><s:textarea name="waiver.parcel_tax_id" value="%{waiver.parcel_tax_id}" rows="3" maxlength="25" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Hookup address </dt>
				<dd><s:textarea name="waiver.hookup_address" value="%{waiver.hookup_address}" rows="3" cols="50" required="true" />* </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Invalid Address? </dt>
				<dd><s:checkbox name="waiver.invalid_addr" value="%{waiver.invalid_addr}" /> </dd> 
			</dl>
			<dl class="fn1-output-field">
				<dt>Is Business? </dt>
				<dd><s:checkbox name="waiver.isBusiness" value="%{waiver.isBusiness}" /> </dd> 
			</dl>			
			<dl class="fn1-output-field">
				<dt>Notes </dt>
				<dd><s:textarea name="waiver.notes" value="%{waiver.notes}" rows="5" cols="50"  /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Signed Date </dt>
				<dd><s:textfield name="waiver.signed_date" value="%{waiver.signed_date}" size="10" maxlength="10" class="date" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Recorder ID </dt>
				<dd><s:textfield name="waiver.recorder_id" value="%{waiver.recorder_id}" size="20" maxlength="20" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Recorder Date </dt>
				<dd><s:textfield name="waiver.recorder_date" value="%{waiver.recorder_date}" size="10" maxlength="10" class="date" /> </dd>
			</dl>
		</div>
		<div class="tt-split-container">
			<dl class="fn1-output-field">
				<dt>Recorder Notes </dt>
				<dd><s:textarea name="waiver.recorder_notes" value="%{waiver.recorder_notes}" rows="5" cols="50"  /> </dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Paper Verified Date </dt>
				<dd><s:textfield name="waiver.paper_verified_date" value="%{waiver.paper_verified_date}" size="10" maxlength="10" class="date" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Controller Notes </dt>
				<dd><s:textarea name="waiver.controller_notes" value="%{waiver.controller_notes}" rows="5" cols="50"  /> </dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>IN/OUT City </dt>
				<dd><s:radio name="waiver.in_out_city" value="%{waiver.in_out_city}" list="#{'IN':'IN','OUT':'OUT'}" /> 
				</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>IN GIS? </dt>
				<dd><s:checkbox name="waiver.in_gis" value="%{waiver.in_gis}" /> Yes 
				</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Mapped Date </dt>
				<dd><s:textfield name="waiver.mapped_date" value="%{waiver.mapped_date}" size="10" maxlength="10" class="date" /> 
				</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>GIS Notes </dt>
				<dd><s:textarea name="waiver.gis_notes" value="%{waiver.gis_notes}" rows="5" cols="50"  /> </dd>
			</dl>						
			<s:if test="waiver.imported" > 		
				<dl class="fn1-output-field">
					<dt>Imported from Excel </dt>
					<dd>Yes</dd> 
				</dl>
			</s:if>		
			<s:if test="waiver.hasAdded_by()">
				<dl class="fn1-output-field">
					<dt>Added by </dt>
					<dd><s:property value="%{waiver.added_by_user}" /> 
					</dd>
				</dl>
			</s:if>
			<dl class="fn1-output-field">
				<dt>Added date </dt>
				<dd><s:property value="%{waiver.date}" /> 
				</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Expire date </dt>
				<dd><s:textfield name="waiver.expire_date" value="%{waiver.expire_date}" size="10" maxlength="10" class="date" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Status </dt>
				<dd><s:radio name="waiver.status" value="%{waiver.status}" list="#{'Open':'Open','Closed':'Closed','Completed':'Completed'}" /></dd>
			</dl>
		</div>
	</div>
	<dl class="fn1-output-field">
		You can add your review notes here (if any)<br />
		<dt>Review Notes </dt>
		<dd><s:textarea name="review.notes" value="%{review.notes}" rows="5" cols="50"  /> </dd>
	</dl>
	<dl class="fn1-output-field">
		If no more review is needed click on the 'Verified' checkbox below<br />
		<dt>Verified? </dt>
		<dd><s:checkbox name="review.verified" value="%{review.verified}" /> Yes 
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Review Date </dt>
		<dd><s:property value="review.date" />
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Reviewed By </dt>
		<dd><s:property value="review.reviewer" />
		</dd>
	</dl>
	<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>		
	<s:if test="waiver.hasOwners()">
		<s:submit name="action" type="button" value="Remove Owner" class="fn1-btn"/>
	</s:if>
	<a href="<s:property value='#application.url'/>owner.action?waiver_id=<s:property value='waiver.id' />" class="fn1-btn">Add/Edit Owners</a>				
	<a href="<s:property value='#application.url' />doUpload.action?obj_id=<s:property value='waiver.id' />obj_type=Waiver" class="fn1-btn">Attachments</a>
	<s:if test="waiver.hasUploads()">
		<s:set var="attachmentsTitle" value="attachmentsTitle" />
		<s:set var="uploads" value="%{waiver.uploads}" />
		<%@  include file="../attachments/fileUploads.jsp" %>			
	</s:if>
</s:form>

<%@  include file="../gui/footer.jsp" %>

