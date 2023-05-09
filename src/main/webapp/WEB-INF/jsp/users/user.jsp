<%@  include file="../gui/header.jsp" %>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->

<s:form action="user" id="form_id" method="post">
	<s:hidden name="action2" id="action2" value="" />
	<s:if test="user.id == ''">
		<h1>New User</h1>
	</s:if>
	<s:else>
		<h1>User <s:property value="user.fullname" /></h1>
		<s:hidden name="user.id" value="%{user.id}" />
	</s:else>
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
	<s:if test="user.id != ''">
		<dl class="fn1-output-field">
			<dt>ID</dt>
			<dd><s:property value="%{user.id}" /></dd>
		</dl>
	</s:if>
	<dl class="fn1-output-field">
		<dt>Username</dt>
		<dd><s:textfield name="user.username" size="10" value="%{user.username}" /></dd>
	</dl>	
	<dl class="fn1-output-field">
		<dt>Full Name </dt>
		<dd><s:textfield name="user.fullName" value="%{user.fullName}" size="30" maxlength="70" /> </dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Roles</dt>
		<dd><s:select name="user.role" value="%{user.role}" list="#{'View':'View Only','Edit':'Edit','Edit:Admin':'All (Admin)'}" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Department</dt>
		<dd><s:select name="user.dept" value="%{user.dept}" list="#{'Controller':'Controller','ITS':'ITS','Legal':'Legal','Utilities':'Utilities'}" /></dd>
	</dl>
	<dl class="fn1-output-field">
		<dt>Active Email</dt>
		<dd><s:checkbox name="user.activeMail" value="%{user.activeMail}" /> Yes</dd> 
	</dl>
	<dl class="fn1-output-field">
		<dt>Inactive </dt>
		<dd><s:checkbox name="user.inactive" value="%{user.inactive}" /> Yes</dd> 
	</dl>		
	<s:if test="user.id == ''">
		<s:submit name="action" type="button" value="Save" class="fn1-btn"/>
	</s:if>
	<s:else>
		<s:submit name="action" type="button" value="Save Changes" class="fn1-btn"/>
	</s:else>
	<a href="<s:property value='#application.url' />groupUser.action?" class="fn1-btn"> Manage Users in Groups</a>
</s:form>
<s:if test="users != null && users.size() > 0">
	<s:set var="users" value="%{users}" />
	<s:set var="usersTitle" value="usersTitle" />
	<%@  include file="users.jsp" %>
</s:if>

<%@  include file="../gui/footer.jsp" %>


