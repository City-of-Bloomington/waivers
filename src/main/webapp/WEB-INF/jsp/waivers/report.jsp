<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="report" id="form_id" method="post">
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
  <h3> Waivers Report </h3>
	<ul>
		<li> You can get the list of waivers based on selection of the year or date range but not both</li>
		<li> The output could be html (web format) or csv that you can open with spread sheet app such as Excel</li>
	</ul>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Status </dt>
			<dd><s:radio name="waiverList.status" value="%{waiverList.status}" list="#{'-1':'All','Open':'Open','Closed':'Closed','Completed':'Completed'}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Year </dt>
			<dd><s:select name="waiverList.year" value="%{waiverList.year}" list="years" headerKey="-1" headerValue="All" /></dd>
		</dl>
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
		<dt> Output Format:</dt>
		<dd>
			<s:radio name="outputType" value="%{outputType}" list="#{'html':'HTML','csv':'CSV (spread sheet)'}" />
		</dd>
	</dl>
	<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
</s:form>
<s:if test="hasWaivers()">
	<s:set var="waivers" value="waivers" />	
	<s:set var="waiversTitle" value="reportTitle" />
	<%@  include file="waivers.jsp" %>
</s:if>
<%@  include file="../gui/footer.jsp" %>

