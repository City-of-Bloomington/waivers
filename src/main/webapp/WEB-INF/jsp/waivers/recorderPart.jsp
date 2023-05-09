<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="tt-row-container">
	<dl class="fn1-output-field">
		<dt>Waiver Instrument # </dt>
		<dd><s:textfield name="waiver.waiverInstrumentNum" value="%{waiver.waiverInstrumentNum}" size="20" maxlength="20" />
		</dd>
	</dl>

	<dl class="fn1-output-field">
		<dt>Recorded Date </dt>
		<dd><s:textfield name="waiver.recorderDate" value="%{waiver.recorderDate}" size="10" maxlength="10" class="date" />
		</dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Waiver Book </dt>
		<dd><s:textfield name="waiver.waiverBook" size="10" maxlength="10" value="%{waiver.waiverBook}" /> Page: <s:textfield name="waiver.waiverPage" size="10" maxlength="10" value="%{waiver.waiverPage}" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Waiver Notes </dt>
		<dd><s:property value="#waiver.notes" />
		</dd>
	</dl>	
</div>


