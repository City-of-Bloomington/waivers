<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="address" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="address.waiver_id" value="%{address.waiver_id}" />
	<s:if test="address.id == ''">
		<h1>New Address</h1>
	</s:if>
	<s:else>
		<h1>Edit Address <s:property value="%{address.streetAddress}" /></h1>
		<s:hidden name="address.id" value="%{address.id}" />
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
	<details>
		<summary>Instructions</summary>
		<ul>
			<li>Street # could be one or multiple or number range such as (10), (10,12,14) or (10-30)</li>
			<li>Street name has to be one name at a time</li>
			<li>For multiple street names use this form multiple times</li>
		</ul>
	</details>
	<p>* Street Name is required <br />
		After you click on 'Save' please wait couple seconds for the address to
		be verified with our master address and this may take some time. <br />
	</p>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Street Num </dt>
			<dd><s:textfield name="address.streetNum" value="%{address.streetNum}" size="30" maxlength="50" title="you can enter one value, or multiple separated by comma or number range such as (401), (401,403,405) or (401-409)" placeHolder="401 or 401,403 or 401-409"/>
			</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Street Name </dt>
			<dd><s:textfield name="address.streetName" value="%{address.streetName}" size="30" maxlength="50" title="include direction and street type such as N Morton St" placeHolder="N Morton St"/>*
			</dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Address </dt>
			<dd><s:textfield name="address.streetAddress" value="%{address.streetAddress}" size="30" maxlength="150" />
			</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Related Waiver </dt>
			<dd><a href="<s:property value='#application.url'/>waiver.action?id=<s:property value='%{address.waiver_id}' />&action=Edit" > <s:property value="%{address.waiver_id}" /></a></dd>
		</dl>
		<s:if test="address.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<dl class="fn1-output-field">
				<dt>Valid? </dt>
				<dd><s:if test="address.invalid">No</s:if><s:else>Yes</s:else> </dd>
			</dl>			
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		</s:else>
	</div>
</s:form>

<%@  include file="../gui/footer.jsp" %>


