<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="search" id="form_id" method="post">
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
  <h3> Search </h3>
	<div class="tt-row-container">
		<div class="tt-split-container">				
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:textfield name="waiverList.id" value="%{waiverList.id}" size="10" maxlength="10" /> 
				</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Waiver #</dt>
				<dd><s:textfield name="waiverList.waiverNum" value="%{waiverList.waiverNum}" size="10" maxlength="10" /> 
				</dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Deed/Waiver Instrumnet # </dt>
				<dd><s:textfield name="waiverList.instrumentNum" value="%{waiverList.instrumentNum}" size="25" maxlength="25" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Service Address </dt>
				<dd><s:textfield name="waiverList.hookupAddress" value="%{waiverList.hookupAddress}" size="25" maxlength="30" /> </dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Entity Name </dt>
				<dd><s:textfield name="waiverList.name" value="%{waiverList.name}" size="25" maxlength="25" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Pin/Tax ID </dt>
				<dd><s:textfield name="waiverList.parcelTaxId" value="%{waiverList.parcelTaxId}" size="25" maxlength="30"  /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Develop. Subdiv.</dt>
				<dd><s:textfield name="waiverList.developmentSubdivision" value="%{waiverList.developmentSubdivision}" size="25" maxlength="25" /> </dd>
			</dl>
		</div>
		<div class="tt-split-container">
			<dl class="fn1-output-field">
				<dt>Legal Desc.</dt>
				<dd><s:textfield name="waiverList.legalDescription" value="%{waiverList.legalDescription}" size="25" maxlength="25" /> </dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Related Entity Type </dt>
				<dd><s:radio name="waiverList.type" value="%{waiverList.type}" list="#{'-1':'All','business':'Business only','individual':'Individuals only','trust':'Trusts only'}"  /> </dd>
			</dl>			
			<dl class="fn1-output-field">
				<dt>Data Entry? </dt>
				<dd><s:radio name="waiverList.imported" value="%{waiverList.imported}" list="#{'-1':'All','y':'Imported','n':'Entered'}" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Status </dt>
				<dd><s:radio name="waiverList.status" value="%{waiverList.status}" list="#{'-1':'All','Open':'Open','Closed':'Closed','Completed':'Completed'}" /> </dd>
			</dl>
			<dl class="fn1-output-field">
				<dt>Records </dt>
				<dd><s:checkbox name="waiverList.noMappedDate" value="%{waiverList.noMappedDate}" /> have no mapped date</dd>
			</dl>
			<dl class="fn1-output-field">
				<dt> In/Out City </dt>
				<dd><s:radio name="waiverList.inOutOption" value="%{waiverList.inOutOption}" list="#{'-1':'All','In':'In','Out':'Out','noInOut':'In/Out City not set'}" /></dd>
			<dl class="fn1-output-field">
				<dt>Show All </dt>
				<dd><s:checkbox name="waiverList.showAll" value="%{waiverList.showAll}" /> Yes</dd>
			</dl>
				
		</div>
	</div>
	<dl class="fn1-output-field">
		<dt> Date Selection</dt>
		<dd><s:radio name="waiverList.whichDate" value="%{waiverList.whichDate}" list="#{'w.date':'Request Date','w.signed_date':'Signed Date','w.recorder_date':'Recorded Date','w.expire_date':'Expire Date','w.mapped_date':'Mapped Date'}" /></dd>
	<dl class="fn1-output-field">
		<dt> Date </dt>
		<dd>from: <s:textfield name="waiverList.dateFrom" value="%{waiverList.dateFrom}" size="10" maxlength="10" cssClass="date" /> to:
			<s:textfield name="waiverList.dateTo" value="%{waiverList.dateTo}" size="10" maxlength="10" cssClass="date" />					
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt> Output Type:</dt>
		<dd>
			<s:checkbox name="outputCsv" value="%{outputCsv}" /> CSV file
		</dd>
	</dl>
	<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
</s:form>
<s:if test="waivers != null && waivers.size() > 0">
	<s:set var="waivers" value="waivers" />	
	<s:set var="waiversTitle" value="waiversTitle" />
	<%@  include file="waivers.jsp" %>
</s:if>
<%@  include file="../gui/footer.jsp" %>

