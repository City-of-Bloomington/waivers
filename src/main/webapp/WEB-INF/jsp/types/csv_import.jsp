<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="importCsv" id="form_id" method="post" >
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
	<h3>Import Old Data</h3>
	Important: Running this form will cause to delete all data and start 
	reading the old data again (import). This should be done when it is really needed.
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt> </dt>
			<dd> </dd>
		</dl>
		<s:submit name="action" type="button" value="Submit" class="fn1-btn"/></dd>
	</div>

</s:form>
<%@  include file="../gui/footer.jsp" %>


