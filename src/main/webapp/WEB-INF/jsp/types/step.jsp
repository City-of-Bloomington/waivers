<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="step" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="step.id == ''">
		<h1>New Workflow Step</h1>
	</s:if>
	<s:else>
		<h1>Edit <s:property value="step.name" /></h1>
		<s:hidden id="step.id" name="step.id" value="%{step.id}" />
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
			If you make any change, please hit the 'Save Changes' button <br />
		</s:if>
		<s:else>
			You must hit 'Save' button to save data. <br />
		</s:else>
		You can assign the workflow to a specific group by selecting the a group from the assignment groups list
	</p>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
		<s:if test="step.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="step.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Name </dt>
			<dd><s:textfield name="step.name" value="%{step.name}" size="30" maxlength="70" requiredLabel="true" required="true" />* </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Field Name</dt>
			<dd><s:textfield name="step.field_name" value="%{step.field_name}" size="30" maxlength="70" />(date type) </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Field Name</dt>
			<dd><s:textfield name="step.field2_name" value="%{step.field2_name}" size="30" maxlength="70" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Part Name</dt>
			<dd><s:textfield name="step.part_name" value="%{step.part_name}" size="30" maxlength="70" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Assignment</dt>
			<dd><s:select name="step.group_id" value="%{step.group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick group" /> </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Attachment Required?</dt>
			<dd><s:checkbox name="step.require_upload" value="%{step.require_upload}" fieldValue="true" />Yes </dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Suggested Upload Type</dt>
			<dd><s:select name="step.suggested_upload_type" value="%{step.suggested_upload_type}" list="#{'Application':'Application','Deed':'Deed','Recorded Waiver':'Recorded Waiver'}" headerKey="-1" headerValue="Pick suggest upload type" /> </dd> (optional)
		</dl>		
		<dl class="fn1-output-field">
			<dt>Inactive?</dt>
			<dd><s:checkbox name="step.inactive" value="%{step.inactive}" fieldValue="true" />Yes </dd>
		</dl>		
		<s:if test="step.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		</s:else>
	</div>
</s:form>
<s:if test="steps != null">
	<s:set var="steps" value="steps" />
	<s:set var="stepsTitle" value="stepsTitle" />
	<%@  include file="steps.jsp" %>
</s:if>
<%@  include file="../gui/footer.jsp" %>


