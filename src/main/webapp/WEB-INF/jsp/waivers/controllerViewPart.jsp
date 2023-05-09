<%@ taglib prefix="s" uri="/struts-tags" %>
<!--
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<div class="tt-row-container">
	<dl class="fn1-output-field">
		<dt>Paper Verified Date </dt>
		<dd><s:property value="%{waiver.paperVerifiedDate}" />
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Controller Notes </dt>
		<dd><s:property value="%{waiver.controllerNotes}" />
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Waiver Notes </dt>
		<dd><s:property value="%{waiver.notes}" />
		</dd>
	</dl>
</div>
