<%@ taglib prefix="s" uri="/struts-tags" %>
<dl class="fn1-output-field">
	<dt>Paper Verified Date </dt>
	<dd><s:textfield name="waiver.paperVerifiedDate" value="%{waiver.paperVerifiedDate}" size="10" maxlength="10" class="date" />
	</dd>
</dl>			
<dl class="fn1-output-field">
	<dt>Controller Notes </dt>
	<dd><s:textarea name="waiver.controllerNotes" value="%{waiver.controllerNotes}" rows="5" cols="70" />
	</dd>
</dl>
<dl class="fn1-output-field">
	<dt>Waiver Notes </dt>
	<dd><s:textarea name="waiver.notes" value="%{waiver.notes}" rows="5" cols="50" />
	</dd>
</dl>	
