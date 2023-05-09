<%@  include file="../gui/header.jsp" %>
<!-- 
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:iterator value="report.all" status="allStatus">
  <table border="1" width="60%">
		<s:iterator status="rowStatus">
			<s:if test="#rowStatus.index == 0">
				<caption><s:property value="second" /></caption>
			</s:if>
			<s:elseif test="#rowStatus.index == 1">
				<tr>
					<td align="center"><label><s:property value="first" /></label></td>
					<td align="center"><label><s:property value="second" /></label></td>
					<s:if test="size > 2">
						<td align="center"><label><s:property value="third" /></label></td>
					</s:if>
					<s:if test="size > 3">
						<td align="center"><label><s:property value="forth" /></label></td>
					</s:if>			
				</tr>
			</s:elseif>
			<s:else>
				<tr>
					<td><s:property value="first" /></td>
					<td align="right"><s:property value="second" /></td>
					<s:if test="size > 2">
						<td align="right"><s:property value="third" /></td>
					</s:if>
					<s:if test="size > 3">
						<td align="right"><s:property value="forth" /></td>
					</s:if>			
				</tr>
			</s:else>
		</s:iterator>
  </table>
</s:iterator>
<%@  include file="../gui/footer.jsp" %>























































