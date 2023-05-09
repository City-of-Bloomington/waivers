<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="reviewInit" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<h1>Initialize Review Data </h1>
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
	<p>This operation need to be done only once to initialize the reviews table
		with waivers that need to be reviewed. If this operation is done before, no need to redo again. Operation will be ignored.<br />
	Critical date: is the date that will be used to find all waivers that were imported and issued before that date</p>
	<div class="tt-row-container">
		<dl class="fn1-output-field">
			<dt>Critical Date </dt>
			<dd><s:textfield name="date" value="%{date}" size="10" maxlength="10" class="date" />(all waivers before this date will be included </dd>
		</dl>
	</di>
	<s:submit name="action" type="button" value="Initialize" class="fn1-btn"/>		
</s:form>

<%@  include file="../gui/footer.jsp" %>

