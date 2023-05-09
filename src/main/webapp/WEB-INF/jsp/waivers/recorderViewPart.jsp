<%@ taglib prefix="s" uri="/struts-tags" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="tt-row-container">
	<dl class="fn1-output-field">
		<dt>Waiver Instrum. # </dt>
		<dd><s:property value="#waiver.waiverInstrumentNum" />
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Waiver Book/Page </dt>
		<dd><s:property value="#waiver.waiverBookPage" />
		</dd>
	</dl>		
	<dl class="fn1-output-field">
		<dt>Recorded Date </dt>
		<dd><s:property value="#waiver.recorderDate" />
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Waiver Notes </dt>
		<dd><s:property value="#waiver.notes" />
		</dd>
	</dl>
	
</div>

