<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="workflow" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="workFlow.id == ''">
		<h1>New Workflow </h1>
	</s:if>
	<s:else>
		<h1>Edit Workflow <s:property value="workFlow.id" /></h1>
		<s:hidden id="workFlow.id" name="workFlow.id" value="%{workFlow.id}" />
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
			To create a new workflow follow the following rules
			<ul>
				<li>You need to choose two workflow steps, the starting step and the next step. </li>
				<li>If you need to add a new workflow step, go to 'Workflow Steps' menu item first</li>
				<li>If the workflow step has no next step, no next task is needed</li>
				<li>Start step can not have previous step.</li>
			</ul>
		</s:else>
	</p>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
		<s:if test="workFlow.id != ''">
			<dl class="fn1-output-field">
				<dt>ID </dt>
				<dd><s:property value="workFlow.id" /> </dd>
			</dl>
		</s:if>		
		<dl class="fn1-output-field">
			<dt>Start Step </dt>
			<dd><s:select name="workFlow.step_id" value="%{workFlow.step_id}" list="steps" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Start Step" /> </dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Next Step </dt>
			<dd><s:select name="workFlow.next_step_id" value="%{workFlow.next_step_id}" list="nextSteps" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Next Step" /> </dd>
		</dl>		
		<s:if test="workFlow.id == ''">
			<s:submit name="action" type="button" value="Save" class="fn1-btn"/></dd>
		</s:if>
		<s:else>
			<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
		</s:else>
	</div>
</s:form>
<s:if test="workFlows != null">
	<s:set var="workFlows" value="workFlows" />
	<s:set var="workFlowsTitle" value="workFlowsTitle" />
	<%@  include file="workflows.jsp" %>
</s:if>
<%@  include file="../gui/footer.jsp" %>


