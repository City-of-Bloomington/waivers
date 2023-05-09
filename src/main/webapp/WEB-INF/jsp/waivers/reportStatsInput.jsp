<%@  include file="../gui/header.jsp" %>
<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="statsReport" method="post">    
  <h3> Stats Reports </h3>
  <s:if test="hasActionErrors()">
		<div class="errors">
      <s:actionerror/>
		</div>
  </s:if>
  <s:if test="hasActionMessages()">
		<div class="welcome">
      <s:actionmessage/>
		</div>
  </s:if>
	<!-- 
  <p>Select one or more of report types.</p>
	-->
  <table border="1" width="100%">
		<tr>
			<td>
				<table width="100%">
					<tr>
						<td align="left" valign="top" colspan="2"><label>Report type:</label></td>
					</tr>
					<tr>
						<td align="left"><s:checkbox name="report.signed" value="%{report.signed}"  />Signed Waivers</td>
						<td align="left"> Before and after <s:textfield name="report.signed_date" value="%{report.signed_date}" required="required" /> date (mm/dd/yyyy)</td>
					</tr>
					<!--
					<tr>  
						<td align="right"><label>Year</label></td>
						<td align="left"><s:select name="report.year" list="years" value="%{report.year}" /> or</td>
					</tr>
					<tr>
						<td align="right"><label>Date, from:</label></td>
						<td align="left"><s:textfield name="report.date_from" maxlength="10" size="10" value="%{report.date_from}" cssClass="date" /><label> To </label><s:textfield name="report.date_to" maxlength="10" size="10" value="%{report.date_to}" cssClass="date" /></td>
					</tr>
					-->
				</table>
			</td>
		</tr>	
		<tr>
			<td colspan="2" valign="top" align="right">
				<s:submit name="action" type="button" value="Submit" />
			</td>
		</tr>
  </table>
</s:form>  
<%@  include file="../gui/footer.jsp" %>	






































