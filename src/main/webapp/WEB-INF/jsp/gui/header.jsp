<?xml version="1.0" encoding="UTF-8" ?>
<!--
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
	-->
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <s:head />
  <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
  <link rel="SHORTCUT ICON" href="https://apps.bloomington.in.gov/favicon.ico" />
  <link rel="stylesheet" href="<s:property value='#application.url' />css/open-sans/open-sans.css" type="text/css" />

  <link rel="stylesheet" href="//bloomington.in.gov/static/fn1-releases/dev/css/default.css" type="text/css" />
  <link rel="stylesheet" href="//bloomington.in.gov/static/fn1-releases/dev/css/kirkwood.css" type="text/css" />

  <link rel="stylesheet" href="<s:property value='#application.url' />css/ol.css" type="text/css" />
  <link rel="stylesheet" href="<s:property value='#application.url' />css/ol3-popup.css" type="text/css" />
  <link rel="stylesheet" href="<s:property value='#application.url' />css/open-sans/open-sans.css" type="text/css" />
  <link rel="stylesheet" href="<s:property value='#application.url' />css/screen.css" type="text/css" />
	<link rel="stylesheet" href="<s:property value='#application.url' />css/jquery-ui.min-1.13.2.css" type="text/css" media="all" />
  <link rel="stylesheet" href="<s:property value='#application.url' />css/jquery-ui.theme.min-1.13.2.css" type="text/css" media="all" />

  <title>Waivers</title>
  <script type="text/javascript">
    var APPLICATION_URL = '<s:property value='#application.url' />';
  </script>
</head>
<body class="fn1-body">
  <header class="fn1-siteHeader">
    <div class="fn1-siteHeader-container">
      <div class="fn1-site-title">
        <h1 id="application_name"><a href="<s:property value='#application.url'/>">Annexation Waivers</a></h1>
        <div class="fn1-site-location" id="location_name"><a href="<s:property value='#application.url'/>">City of Bloomington, IN</a></div>
      </div>
      <s:if test="#session != null && #session.user != null">
        <div class="fn1-site-utilityBar">
          <nav id="user_menu">
            <div class="menuLauncher"><s:property value='#session.user.fullName' /></div>
            <div class="menuLinks closed" style="background-color:wheat">
							<br />
              <a href="<s:property value='#application.url'/>Logout">Logout</a>
            </div>
          </nav>
          <s:if test="#session.user.isAdmin()">					
	    <nav id="admin_menu">
	      <div class="menuLauncher">Admin</div>
	      <div class="menuLinks closed" style="background-color:wheat">
		<br />
		
		<a href="<s:property value='#application.url'/>user.action">Users</a>
		<a href="<s:property value='#application.url'/>group.action">Groups</a>
		<a href="<s:property value='#application.url'/>groupUser.action">Manage Groups</a>
		<a href="<s:property value='#application.url'/>groupNotification.action">Group Notifications Setting</a>								
		<a href="<s:property value='#application.url'/>step.action">Workflow Steps</a>
		<a href="<s:property value='#application.url'/>workflow.action">Workflows</a>
	      </div>
	    </nav>
          </s:if>
        </div>
	  </s:if>
	</div>
	<div class="fn1-nav1">
      <nav class="fn1-nav1-container">
	<a href="<s:property value='#application.url'/>activeTasks.action">Active Task List</a>				
	<a href="<s:property value='#application.url'/>waiver.action">New Waiver</a>
	<a href="<s:property value='#application.url'/>entity.action">New Entity/Owner</a>				
	<a href="<s:property value='#application.url'/>search.action">Search</a>
	<a href="<s:property value='#application.url'/>report.action">Reports</a>
	<a href="<s:property value='#application.url'/>statsReport.action">Stats Reports</a>																		
	<a href="<s:property value='#application.url'/>emailLog.action">Notification Logs</a>				
      </nav>
    </div>
  </header>
  <main>
    <div class="fn1-main-container">
