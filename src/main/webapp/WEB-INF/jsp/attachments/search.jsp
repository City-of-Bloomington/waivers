<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="attachSearch" method="post">
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
  <h3> Search Attachments </h3>
	<p>To add a new attachment, go to waiver or task and click on the 'Attachments' button.
		</p>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Attachment ID </dt>
			<dd><s:textfield name="uploadList.id" value="%{uploadList.id}" size="10" maxlength="10" /> 
			</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Waiver ID</dt>
			<dd><s:textfield name="uploadList.waiver_id" value="%{uploadList.waiver_id}" size="10" maxlength="10" /> 
			</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Date </dt>
			<dd>from: <s:textfield name="uploadList.date_from" value="%{uploadList.date_from}" size="10" maxlength="10" cssClass="date" /> to:
				<s:textfield name="uploadList.date_to" value="%{uploadList.date_to}" size="10" maxlength="10" cssClass="date" />					
			</dd>
		</dl>
	</div>
	<s:submit name="action" type="button" value="Submit" class="fn1-btn"/>
</s:form>
<s:if test="uploads != null">
  <s:set var="uploads" value="uploads" />
	<s:set var="uploadsTitle" value="uploadsTitle" />
  <%@  include file="fileUploads.jsp" %>	
</s:if>
<%@  include file="../gui/footer.jsp" %>

