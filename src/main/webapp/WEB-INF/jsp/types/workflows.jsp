<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<table class="fn1-table">
	<caption><s:property value="#workFlowsTitle" /></caption>
	<thead>
		<tr>
			<th align="center"><b>Workflow </b></th>
			<th align="center"><b>Step Name </b></th>
			<th align="center"><b>Next Step Name</b></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator var="one" value="#workFlows">
			<tr>
				<td><a href="<s:property value='#application.url' />workflow.action?id=<s:property value='id' />">Edit </a></td>
				<td><s:property value="step.name" /></td>
				<td><s:property value="nextStep.name" /></td>				
			</tr>
		</s:iterator>
	</tbody>
</table>
