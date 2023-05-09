<%@  include file="header.jsp" %>
<h3>Annex Waivers</h3>
<s:if test="hasActionErrors()">
  <div class="errors">
    <s:actionerror/>
  </div>
</s:if>
<h4>Welcome to Annex Waivers</h4>
<p>
You can pick one from the following top menu options
</p>
<ul>
	<li>Active Task List: give you list of active tasks</li>				
	<li>New Waiver: to add a new waiver</li>
	<li>New Entity: add a new owner, business to the system.</li>				
	<li>Search: search for waivers</li>
	<li>Admin: configuration and other system settings</li>
</ul>

<%@  include file="footer.jsp" %>
