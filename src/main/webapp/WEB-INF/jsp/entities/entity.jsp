<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="entity" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="entity.hasWaiver()">
		<s:hidden name="entity.waiver_id" value="%{entity.waiver_id}" />
	</s:if>
	<s:if test="entity.id == ''">
		<h1>New Entity/Business</h1>
	</s:if>
	<s:else>
		<h1>Edit Entity <s:property value="id" /></h1>
		<s:hidden name="entity.id" value="%{entity.id}" />
	</s:else>
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
  <p>* Name is required <br />
		 For people names use 'last name, first name' format. <br />
		Check if this entity is a business or a trust. <br />
		For business managers and trust executers you may add signers title <br />
		<s:if test="id != ''">
			If you make any change, please hit the 'Save Changes' button
		</s:if>
		<s:else>
			You must hit 'Save' button to save data.
		</s:else>
	</p>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="entity.name" value="%{entity.name}" size="30" maxlength="70" required="true" placeholder="last name, first name or business name"/> *</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Title </dt>
			<dd><s:textfield name="entity.title" value="%{entity.title}" size="30" maxlength="30" placeholder="Owner, Agent, Manager, etc" /> </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Is Business? </dt>
			<dd><s:checkbox name="entity.isBusiness" value="%{entity.isBusiness}" /> Yes </dd>
		<dl class="fn1-output-field">
			<dt>Is Trust? </dt>
			<dd><s:checkbox name="entity.isTrust" value="%{entity.isTrusts}" /> Yes </dd>			
		</dl>
		<s:if test="entity.hasWaiver()">
		<dl class="fn1-output-field">
			<dt>Related Waiver </dt>
			<dd><a href="<s:property value='#application.url'/>waiver.action?id=<s:property value='entity.waiver_id' />"> Back To Waiver</a>	
		</dl>
		</s:if>
		<s:if test="entity.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		</s:else>
	</div>
</s:form>
<s:if test="entity.hasWaivers()">
	<s:set var="waivers" value="entity.waivers" />
	<s:set var="waiversTitle" value="'Entity Waiver(s)'" />
	<%@  include file="../waivers/waivers.jsp" %>
</s:if>
<s:elseif test="entities != null">
	<s:set var="entities" value="entities" />
	<s:set var="entitiesTitle" value="entitiesTitle" />
	<%@  include file="entities.jsp" %>
</s:elseif>
<%@  include file="../gui/footer.jsp" %>


