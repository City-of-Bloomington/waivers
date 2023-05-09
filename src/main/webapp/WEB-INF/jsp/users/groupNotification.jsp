<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="groupNotification" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="groupNotification.id != ''">
		<s:hidden name="groupNotification.id" value="groupNotification.id" />
	</s:if>
	<h2>User Group Notification </h2>
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
	<p>If you want certain group to be Not notified, make that group as inactive</p>
	<dl class="fn1-output-field">
		<dt>On Completed Workflow Step</dt>
		<dd><s:select name="groupNotification.step_id" value="%{groupNotification.step_id}" list="steps" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Step" /></dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>Group to Be Notified</dt>
		<dd><s:select name="groupNotification.group_id" value="%{groupNotification.group_id}" list="groups" listKey="id" listValue="name" headerKey="-1" headerValue="Pick Group" /></dd>
	</dl>
	<s:if test="groupNotification.id == ''">
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
	</s:if>
	<s:else>
		<dl class="fn1-output-field">
			<dt>Inactive</dt>
			<dd><s:checkbox name="groupNotification.inactive" value="%{groupNotification.inactive}" />? Yes</dd>
		</dl>		
		<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
	</s:else>
	<s:if test="hasGroupNotifications()">
		<s:set var="groupNotifications" value="groupNotifications" />
		<%@  include file="groupNotifications.jsp" %>
	</s:if>
</s:form>

<%@  include file="../gui/footer.jsp" %>


