<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="group" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="group.id == ''">
		<h1>New group</h1>
	</s:if>
	<s:else>
		<h1>Edit <s:property value="group.name" /></h1>
		<s:hidden id="group.id" name="group.id" value="%{group.id}" />
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
			You must hit 'Save' button to save data.
		</s:else>
	</p>
	<div class="tt-row-container">
		<s:if test="type.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="group.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="group.name" value="%{group.name}" size="30" maxlength="70" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Inactive?</dt>
			<dd><s:checkbox name="group.inactive" value="%{group.inactive}" fieldValue="true" />Yes </dd>
		</dl>
		<s:if test="group.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		</s:else>
	</div>
</s:form>
<s:if test="groups != null">
	<s:set var="groups" value="groups" />
	<s:set var="groupsTitle" value="groupsTitle" />
	<%@  include file="groups.jsp" %>
</s:if>
<%@  include file="../gui/footer.jsp" %>


