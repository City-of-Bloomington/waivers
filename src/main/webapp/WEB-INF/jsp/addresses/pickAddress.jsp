<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<s:form action="verify" id="form_id" method="post" >
	<s:hidden name="action2" id="action2" value="" />
	<s:hidden name="waiver_id" value="%{waiver_id}" />	
	<h1>Pick an address</h1>
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
	<p>A number of addresses matched your address, please pick the right one(s) </p>
	<table width="60%" border="0">
		<tr>
			<td>Related Waiver </td>
			<td><a href="<s:property value='#application.url'/>waiver.action?id=<s:property value='%{waiver_id}' />&action=Edit" > <s:property value="%{waiver_id}" /></a> </td>
		</tr>
		<tr>
			<td align="left" width="25%"><b>Select</b></td>			
			<td align="left"><b>Address</b></td>
		</tr>
		<s:if test="hasAddresses()">
			<s:iterator var="one" value="%{addresses}">
				<tr>
					<td align="left"><input type="checkbox" name="addrCombos" value="<s:property value='addrCombo' />" /></td>
					<td align="left"><s:property value="streetAddress" /></td>		
				</tr>
			</s:iterator>
			<tr>
				<td>&nbsp;</td>
				<td align="left">
					<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
				</td>
			</tr>
		</s:if>
	</table>
	
</s:form>
<%@  include file="../gui/footer.jsp" %>


