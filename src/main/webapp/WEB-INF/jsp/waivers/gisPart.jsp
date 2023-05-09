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
		<dd><s:textfield name="waiver.mappedDate" value="%{waiver.mappedDate}" size="10" maxlength="10" class="date" required="true" />
		</dd>
	</dl>			
	<dl class="fn1-output-field">
		<dt>In/Out City </dt>
		<dd><s:radio name="waiver.inOutCity" value="%{waiver.inOutCity}" list="#{'IN':'IN','OUT':'OUT'}" /> 
		</dd>
	</dl>			
	<dl class="fn1-output-field">
		<dt>GIS Notes </dt>
		<dd><s:textarea name="waiver.gisNotes" value="%{waiver.gisNotes}" rows="5" cols="50" />
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Waiver Notes </dt>
		<dd><s:textarea name="waiver.notes" value="%{waiver.notes}" rows="5" cols="50" />
		</dd>
	</dl>	
</div>
