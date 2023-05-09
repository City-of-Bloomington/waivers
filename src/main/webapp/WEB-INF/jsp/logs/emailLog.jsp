<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="emailLog" id="form_id" method="post">
	<h1>Notification Logs</h1>
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
	<ul>
		<li>You can limit the logs to certain waiver or task by using the waiver #, id or task id </li>
	</ul>
	<dl class="fn1-output-field">
		<dt>Waiver #</dt>
		<dd><s:textfield name="waiver_num" size="10" value="%{waiver_num}" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Waiver ID</dt>
		<dd><s:textfield name="waiver_id" size="10" value="%{waiver_id}" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Task ID</dt>
		<dd><s:textfield name="task_id" size="10" value="%{task_id}" /></dd>
	</dl>	
	<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>

</s:form>
<s:if test="hasEmailLogs()">
	<s:set var="emailLogs" value="emailLogs" />
	<s:set var="logsTitle" value="logsTitle" />
	<%@  include file="emailLogs.jsp" %>
</s:if>

<%@  include file="../gui/footer.jsp" %>	
