<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
	<h1>Review Annexation Waiver <s:property value="review.waiver_id" /></h1>
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
	<div class="tt-row-container">
		<s:if test="waiver.hasOwners()">
			<dl class="fn1-output-field">
				<dt> </dt>
				<dd>
					<table width="60%" border="1"> <caption>Owner(s)</caption>
						<s:iterator value="waiver.owners">
							<tr>
								<td><s:property value="full_name" /></td>
							</tr>
						</s:iterator>
					</table>
				</dd>
			</dl>
		</s:if>
		<div class="tt-split-container">		
			<dl class="fn1-output-field">
				<dt>Instrument num </dt>
				<dd><s:property value="%{waiver.instrument_num}" /> (Deed book)</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Legal description </dt>
				<dd><s:property value="%{waiver.legal_description}"  /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Parcel tax ID </dt>
				<dd><s:property value="%{waiver.parcel_tax_id}" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Hookup address </dt>
				<dd><s:property value="%{waiver.hookup_address}" /> </dd>
			</dl>
			<s:if test="waiver.invalid_addr" > 
				<dl class="fn1-output-field">
					<dt>Invalid Address? </dt>
					<dd> Yes </dd> 
				</dl>
			</s:if>
			<s:if test="waiver.notes" > 			
				<dl class="fn1-output-field">
					<dt>Notes </dt>
					<dd><s:property value="%{waiver.notes}" /> </dd>
				</dl>
			</s:if>
			<dl class="fn1-output-field">
				<dt>Signed Date </dt>
				<dd><s:property value="%{waiver.signed_date}" /> </dd>
			</dl>
		</div>
		<div class="tt-split-container">
			<dl class="fn1-output-field">
				<dt>Recorder ID </dt>
				<dd><s:property value="%{waiver.recorder_id}" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Recorder Date </dt>
				<dd><s:property value="%{waiver.recorder_date}" /> </dd>
			</dl>
			<s:if test="waiver.recorder_notes" > 				
				<dl class="fn1-output-field">
					<dt>Recorder Notes </dt>
					<dd><s:property value="%{waiver.recorder_notes}"  /> </dd>
				</dl>
			</s:if>
			<s:if test="waiver.paper_verified_date" > 					
				<dl class="fn1-output-field">
					<dt>Paper Verified Date </dt>
					<dd><s:property value="%{waiver.paper_verified_date}" /> </dd>
				</dl>
			</s:if>
			<s:if test="waiver.controller_notes" > 					
				<dl class="fn1-output-field">
					<dt>Controller Notes </dt>
					<dd><s:property value="%{waiver.controller_notes}"  /> </dd>
				</dl>
			</s:if>				
			<dl class="fn1-output-field">
				<dt>IN/OUT City </dt>
				<dd><s:property value="%{waiver.in_out_city}" /> 
				</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>IN GIS? </dt>
				<dd><s:if test="waiver.in_gis"> Yes </s:if><s:else>No</s:else> 
				</dd>
			</dl>
			<s:if test="waiver.imported" > 		
				<dl class="fn1-output-field">
					<dt>Imported? </dt>
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
			<s:if test="waiver.expire_date">			
				<dl class="fn1-output-field">
					<dt>Expire date </dt>
					<dd><s:property value="%{waiver.expire_date}" /> 
					</dd>
				</dl>
			</s:if>
			<dl class="fn1-output-field">
				<dt>Status </dt>
				<dd><s:property value="%{waiver.status}" /> 
				</dd>
			</dl>
		</div>
	</div>
	<dl class="fn1-output-field">
		<dt>Review Notes </dt>
		<dd><s:property value="%{review.notes}"  /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Verified? </dt>
		<dd><s:if test="review.verified"> Yes </s:if><s:else> No </s:else> 
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
	<s:if test="!review.verified">
		<a href="<s:property value='#application.url'/>review.action?id=<s:property value='id' />&action=Edit" class="fn1-btn">Edit Review</a>
	</s:if>
<%@  include file="../gui/footer.jsp" %>

