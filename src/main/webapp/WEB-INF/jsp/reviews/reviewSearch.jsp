<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="reviewSearch" id="form_id" method="post">
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
  <h3> Search Waiver Reviews </h3>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Review ID </dt>
			<dd><s:textfield name="reviewList.id" value="%{reviewList.id}" size="20" maxlength="20" /> 
			</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Waiver ID </dt>
			<dd><s:textfield name="reviewList.waiver_id" value="%{reviewList.waiver_id}" size="20" maxlength="20" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Verified? </dt>
			<dd><s:radio name="reviewList.verified" value="%{reviewList.verified}" list="#{'-1':'All','y':'Yes','n':'No'}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Include </dt>
			<dd><s:checkbox name="reviewList.need_review" value="%{reviewList.need_review}" /> waivers that need review</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Which Date </dt>
			<dd><s:radio name="reviewList.which_date" value="%{reviewList.which_date}" list="#{'r.date':'Review Date','w.date':'Waiver Date'}" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt> Date </dt>
			<dd>from: <s:textfield name="reviewList.date_from" value="%{reviewList.date_from}" size="10" maxlength="10" cssClass="date" /> to:
				<s:textfield name="reviewList.date_to" value="%{reviewList.date_to}" size="10" maxlength="10" cssClass="date" />					
			</dd>
		</dl>
	</div>	
	<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
</s:form>
<s:if test="reviews != null && reviews.size() > 0">
	<s:set var="reviews" value="reviews" />	
	<s:set var="reviewsTitle" value="reviewsTitle" />
	<%@  include file="reviews.jsp" %>
</s:if>
<s:if test="reviewList.waivers != null && reviewList.waivers.size() > 0">
	<s:set var="waivers" value="reviewList.waivers" />	
	<s:set var="waiversTitle" value="'Waivers that need review'" />
	<%@  include file="../waivers/waiverBasics.jsp" %>
</s:if>
<%@  include file="../gui/footer.jsp" %>

