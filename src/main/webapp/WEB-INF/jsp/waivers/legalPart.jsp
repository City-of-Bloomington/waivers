<%@ taglib prefix="s" uri="/struts-tags" %>
<dl class="fn1-output-field">
	<dt>Signed Date </dt>
	<dd><s:textfield name="waiver.signedDate" value="%{waiver.signedDate}" size="10" maxlength="10" class="date" required="true" />
	</dd>
</dl>
<dl class="fn1-output-field">
	<dt>Waiver Notes </dt>
	<dd><s:textarea name="waiver.notes" value="%{waiver.notes}" rows="5" cols="50" />
	</dd>
</dl>	


