<%@ taglib prefix="s" uri="/struts-tags" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="tt-row-container">
	<dl class="fn1-output-field">
		<dt>Mapped Date </dt>
		<dd><s:property value="#waiver.mappedDate" />
		</dd>
	</dl>			
	<dl class="fn1-output-field">
		<dt>In/Out City </dt>
		<dd><s:property value="#waiver.inOutCity" /> 
		</dd>
	</dl>			
	<dl class="fn1-output-field">
		<dt>GIS Notes </dt>
		<dd><s:property value="#waiver.gisNotes" />
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Waiver Notes </dt>
		<dd><s:property value="%{waiver.notes}" />
		</dd>
	</dl>	
</div>
