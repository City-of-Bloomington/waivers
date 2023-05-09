<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<h1>Waiver <s:property value="waiver.waiverNum" /></h1>
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
<s:if test="waiver.isOpen()">
    <details>
	<summary>Instructions</summary>
	<ul>
	    <li>To make changes click on 'Edit' button </li>		
	    <li>To work on next task, click on 'Next' button </li>
	    <li>You can add attachment files by clicking on 'Attachment' </li>
	    <li>You may Close this waiver if it is not valid or no more progress can be made by clicking on 'Close This Waiver' button </li>
	</ul>
    </details>
</s:if>
<div class="tt-row-container">
    <s:if test="waiver.isBusiness" >
	<dl class="fn1-output-field">
	    <dt>Is Business? </dt>
	    <dd>Yes</dd>
	</dl>
    </s:if>
    <s:if test="waiver.isTrust" >
	<dl class="fn1-output-field">
	    <dt>Is Trust? </dt>
	    <dd>Yes</dd>
	</dl>
    </s:if>	
    <s:if test="waiver.hasEntities()">
	<dl class="fn1-output-field">
	    <s:if test="waiver.isBusiness" >			
		<dt>Entity(ies)</dt>
	    </s:if>
	    <s:else>
		<dt>Owner(s)</dt>				
	    </s:else>
	    <dd><s:property value="waiver.entitiesInfo" /></dd>
	</dl>
	</s:if>
	<dl class="fn1-output-field">
	    <dt>Service address(es) </dt>
	    <dd><s:property value="%{waiver.addressInfo}" /> <s:if test="waiver.invalidAddr" >(Invalid) </s:if> </dd>
	</dl>	
	<div class="tt-split-container">
	    <dl class="fn1-output-field">
		<dt>Waiver Instrument # </dt>
		<dd><s:property value="%{waiver.waiverInstrumentNum}" /> </dd>
	    </dl>		
	    <dl class="fn1-output-field">
		<dt>Deed Instrument # </dt>
		<dd><s:property value="%{waiver.deedInstrumentNum}" /> </dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>Deed Book/Page </dt>
		<dd><s:property value="%{waiver.deedBookPage}" /></dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>Parcel Pin </dt>
		<dd><s:property value="%{waiver.parcelPin}" /> </dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>Parcel Tax ID </dt>
		<dd><s:property value="%{waiver.parcelTaxId}" /> </dd>
	    </dl>		
	    <dl class="fn1-output-field">
		<dt>Waiver Instrument # </dt>
		<dd><s:property value="%{waiver.waiverInstrumentNum}" /> </dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>Waiver Book/Page </dt>
		<dd><s:property value="%{waiver.waiverBookPage}" /></dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>Sec, Township, range, dir </dt>
		<dd><s:property value="%{waiver.secTwpRangeDir}" /> </dd>
	    </dl>
	</div>
	<div class="tt-split-container">
	    <dl class="fn1-output-field">
		<dt>Legal description </dt>
		<dd><s:property value="%{waiver.legalDescription}"  /> </dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>Development </dt>
		<dd><s:property value="%{waiver.developmentSubdivision}" /> </dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>Lot </dt>
		<dd><s:property value="%{waiver.lot}"  /></dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>Acreage </dt>
		<dd><s:property value="%{waiver.acreage}"  /></dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>IN/OUT City? </dt>
		<dd><s:property value="%{waiver.inOutCity}" /></dd>
	    </dl>
	    <s:if test="waiver.hasNotes()">
		<dl class="fn1-output-field">
		    <dt>Waiver Notes </dt>
		    <dd><s:property value="%{waiver.notes}"  /> </dd>
		</dl>
	    </s:if>
	    <dl class="fn1-output-field">
		<dt>Request date </dt>
		<dd><s:property value="%{waiver.date}" /> 
		</dd>
	    </dl>
	    <dl class="fn1-output-field">
		<dt>Signed date </dt>
		<dd><s:property value="%{waiver.signedDate}"  /> </dd>
	    </dl>
	    <s:if test="waiver.needExpireDate()">		
		<dl class="fn1-output-field">
		    <dt>Expire date </dt>
		    <dd><s:property value="%{waiver.expireDate}" /> 
		    </dd>
		</dl>
	    </s:if>
	    <dl class="fn1-output-field">
		<dt>Recorded date </dt>
		<dd><s:property value="%{waiver.recorderDate}"  /> </dd>
		</dl>
		<dl class="fn1-output-field">
		    <dt>Mapped date </dt>
		    <dd><s:property value="%{waiver.mappedDate}"  /> </dd>
		</dl>
		<s:if test="waiver.hasGisNotes()">
		    <dl class="fn1-output-field">
			<dt>GIS notes </dt>
			<dd><s:property value="%{waiver.gisNotes}"  /> </dd>
		    </dl>
		</s:if>
		<dl class="fn1-output-field">
		    <dt>Status </dt>
		    <dd><s:property value="%{waiver.status}" /> </dd>
		</dl>
		<s:if test="waiver.imported" > 		
		    <dl class="fn1-output-field">
			<dt>Imported </dt>
			<dd>Yes</dd> 
		    </dl>
		</s:if>
		<s:if test="waiver.hasAddedBy()">
		    <dl class="fn1-output-field">
			<dt>Added by </dt>
			<dd><s:property value="%{waiver.addedByUser}" /> </dd>
		    </dl>
		</s:if>
		<s:if test="waiver.isClosed()">
		    <dl class="fn1-output-field">
			<dt>Closed date </dt>
			<dd><s:property value="%{waiver.closedDate}" /> </dd>
		    </dl>
		    <dl class="fn1-output-field">
			<dt>Closed by </dt>
			<dd><s:property value="%{waiver.closedByUser}" /></dd>
		    </dl>		
		</s:if>
	</div>
</div>
<a href="<s:property value='#application.url'/>waiver.action?id=<s:property value='id' />&action=Edit" class="fn1-btn">Edit</a>
<s:if test="waiver.isOpen()">
    <s:if test="waiver.canBePrinted()">
	<a href="<s:property value='#application.url'/>WaiverRtf?id=<s:property value='%{waiver.id}' />" class="fn1-btn">Printable Waiver </a>
    </s:if>
    <s:if test="waiver.hasMoreTasks()">
	<s:iterator var="one" value="waiver.tasks">
	    <s:if test="canBeClaimedBy(#session.user)"> 
		<a href="<s:property value='#application.url'/>task.action?task_id=<s:property value='task_id' />&action=Edit" class="fn1-btn">Next: <s:property value='alias' /> (<s:property value="group" />)</a>
	    </s:if>
	    <s:else>
		<a href="<s:property value='#application.url'/>task.action?task_id=<s:property value='task_id' />&action=Edit" id="a_disabled"  disabled="disabled" class="fn1-btn">Next: <s:property value='alias' /> (<s:property value="group" /> disabled)</a>
	    </s:else>
	</s:iterator>
    </s:if>
    <a href="<s:property value='#application.url' />doUpload.action?waiver_id=<s:property value='waiver.id' />" class="fn1-btn">Attachments</a>
    <a href="<s:property value='#application.url'/>waiver.action?id=<s:property value='id' />&action=Close" class="fn1-btn" title="If you close this waiver no more changes can be made">Close This Waiver</a>	
</s:if>
<s:if test="waiver.hasCompletedTasks()" >
    <s:set var="tasksTitle" value="'Completed Actions'" />
    <s:set var="tasks" value="%{waiver.completedTasks}" />
	<%@  include file="../tasks/tasks.jsp" %>			
</s:if>
<s:if test="waiver.hasUploads()">
    <s:set var="attachmentsTitle" value="'Attachments'" />
    <s:set var="uploads" value="%{waiver.uploads}" />
    <%@  include file="../attachments/fileUploads.jsp" %>			
</s:if>
<s:if test="hasEmailLogs()">
    <s:set var="logsTitle" value="'Notification Logs'" />
    <s:set var="emailLogs" value="%{emailLogs}" />
    <%@  include file="../logs/emailLogsShort.jsp" %>	
</s:if>
<%@  include file="../gui/footer.jsp" %>

