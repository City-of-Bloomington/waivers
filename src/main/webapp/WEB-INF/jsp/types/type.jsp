<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="type" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="id == ''">
		<h1>New <s:property value="selection" /></h1>
	</s:if>
	<s:else>
		<h1>Edit <s:property value="selection" /></h1>
		<s:hidden id="type_id" name="type.id" value="%{type.id}" />
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
  <p>* Required field <br />
		<s:if test="id != ''">
			If you make any change, please hit the 'Save Changes' button
		</s:if>
		<s:else>
			Pick a collection from list below to add or update.<br />
			You must hit 'Save' button to save data.
		</s:else>
	</p>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Collection Name </dt>
			<dd><s:select id="selection_id" name="selection" value="%{selection}" list="#{'category':'categories','organ':'Organizations','location':'Locations','recycle':'Recycle Location'}" /> </dd>
		</dl>		
		<s:if test="type.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="type.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="type.name" value="%{type.name}" size="30" maxlength="70" requiredLabel="true" required="true" id="type_name_id" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Inactive?</dt>
			<dd><s:checkbox name="type.inactive" value="%{type.inactive}" fieldValue="true" />Yes </dd>
		</dl>
		<s:if test="type.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		</s:else>
	</div>
</s:form>
<s:if test="types != null">
	<s:set var="types" value="types" />
	<s:set var="typesTitle" value="typesTitle" />
	<%@  include file="types.jsp" %>
</s:if>
<%@  include file="../gui/footer.jsp" %>


