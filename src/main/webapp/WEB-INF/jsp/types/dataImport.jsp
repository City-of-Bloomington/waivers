<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="dataImport" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<h1>Import CSV data</h1>
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
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Csv File </dt>
			<dd><s:textfield name="file_name" value="%{file_name}" size="50" maxlength="150" required="true" /> </dd>
		</dl>
		<s:submit name="action" type="button" value="Import" class="fn1-btn"/></dd>
	</div>
</s:form>
<%@  include file="../gui/footer.jsp" %>


