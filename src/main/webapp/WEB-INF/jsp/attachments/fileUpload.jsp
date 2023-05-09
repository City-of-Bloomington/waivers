<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="doUpload" method="post" onsubmit="return checkTypeSelection();" enctype="multipart/form-data">
	<s:hidden name="waiver_id" value="%{waiver_id}" />
	<s:if test="hasTask()">
		<s:hidden name="task_id" value="%{task_id}" />
	</s:if>
  <h3>Upload New File</h3>
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
  <p>* indicate a required field</p>

	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Waiver #:</dt>
			<dd>
				<a href="<s:property value='#application.url' />waiver.action?id=<s:property value='waiver_id' />"><s:property value="waiver.waiverNum" /></a>
			</dd>
		</dl>
		<s:if test="hasTask()">
			<dl class="fn1-output-field">
				<dt>Task:</dt>
				<dd>
					<a href="<s:property value='#application.url' />task.action?task_id=<s:property value='task_id' />"><s:property value="task.name" /></a></dd>
			</dl>
		</s:if>
		<dl class="fn1-output-field">
			<dt>File:</dt>
			<dd><input type="file" name="upload" value="Pick File" />*</dd>
		</dl>
		<dl class="fn1-output-field">
			<dt>Document Type:</dt>
			<dd><s:select name="type" value="%{type}" list="#{'Application':'Application','Deed':'Deed','Recorded Waiver':'Recorded Waiver','Map':'Map','Other':'Other'}"  headerKey="-1" headerValue="Pick Document Type" id="attach_type_id" /></dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt>Notes:</dt>
			<dd><s:textarea name="notes" value="%{notes}" row="5" cols="50" /></dd>
		</dl>		
		<dl class="fn1-output-field">
			<dt></dt>
			<dd>
				<s:submit name="action" type="button" value="Save" /></dd>
		</dl>			
	</div>
</s:form>
<s:if test="uploads != null">
  <s:set var="uploads" value="uploads" />
	<s:set var="attachmentsTitle" value="'Most Recent Attachments'" />
  <%@  include file="fileUploads.jsp" %>	
</s:if>
<%@  include file="../gui/footer.jsp" %>	






































