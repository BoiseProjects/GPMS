<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta content="Home - GPMS" name="DESCRIPTION">
<meta content="Home - GPMS" name="KEYWORDS">
<meta content="@GPMS" name="COPYRIGHT">
<meta content="GENERATOR" name="GENERATOR">
<meta content="Author" name="AUTHOR">
<meta content="DOCUMENT" name="RESOURCE-TYPE">
<meta content="GLOBAL" name="DISTRIBUTION">
<meta content="INDEX, FOLLOW" name="ROBOTS">
<meta content="1 DAYS" name="REVISIT-AFTER">
<meta content="GENERAL" name="RATING">
<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
<!-- Mimic Internet Explorer 7 -->
<meta content="IE=EmulateIE7" http-equiv="X-UA-Compatible">
<meta content="RevealTrans(Duration=0,Transition=1)"
	http-equiv="PAGE-ENTER">
<link type="icon shortcut" media="icon" href="favicon.ico">
<!--[if IE 9]>
        <link rel="stylesheet" href="css/ie9.css" type="text/css" media="screen"/><![endif]-->
<!--[if lt IE 9]>
        <link rel="stylesheet" href="css/IE.css" type="text/css" media="screen"/><![endif]-->
<!--[if lt IE 7]>
        <script type="text/javascript" src="js/core/IE8.js"></script>
    <![endif]-->
<title>Home - GPMS</title>

<script src="js/jQuery/jquery-1.11.3.min.js" type="text/javascript"></script>

<script type="text/javascript">
	var _aspx_token = "NWExODgyNDctMzA2OS00MWNhLWJjOWEtNGEyODI5N2FiZWJjOlNhZ2VGcmFtZS5BVVRIanhyMzB3eWNqenZwcWQwanYzdmt5Yng0WkFESlg5U0xPQzE6MjAxNTA2MzAxNTA2NTg5NDM5";
	$.ajaxSetup({
		'beforeSend' : function(xhr) {
			xhr.setRequestHeader("ASPX-TOKEN", _aspx_token);
		}
	});
</script>

<script type="text/javascript">
	//<![CDATA[
	var gpmsAppPath = "";
	
	var userProfileId = '<%=session.getAttribute("userProfileId")%>';
	var gpmsUserName = '<%=session.getAttribute("gpmsUserName")%>';
	var isAdmin = '<%=session.getAttribute("isAdmin")%>';
	var userPositionType = '<%=session.getAttribute("userPositionType")%>';
	var	userPositionTitle = '<%=session.getAttribute("userPositionTitle")%>';
	var userDepartment = '<%=session.getAttribute("userDepartment")%>';
	var userCollege = '<%=session.getAttribute("userCollege")%>';

	var gpmsServicePath = "REST/";
	var gpmsRootPath = "http://localhost:8181/GPMS/";

	//]]>
</script>

<script type="text/javascript" src="js/core/gpmscore.js"></script>

<!-- For Side Bar Navigation -->
<script type="text/javascript" src="js/core/dashboard.js"></script>
<script type="text/javascript" src="js/sidebar_accordian.js"></script>
<script type="text/javascript" src="js/superfish.js"></script>

<script type="text/javascript" src="js/core/json2.js"></script>

<script type="text/javascript" src="js/jquery-browser.js"></script>
<script type="text/javascript" src="js/jquery.uniform.js"></script>

<script type="text/javascript" src="js/MessageBox/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="js/MessageBox/alertbox.js"></script>

<script type="text/javascript" src="js/modules/Home.js"></script>

<link type="text/css" rel="stylesheet" href="css/MessageBox/style.css" />

<link type="text/css" rel="stylesheet"
	href="css/Templates/topstickybar.css" />
<link type="text/css" rel="stylesheet" href="css/Templates/admin.css" />
</head>
<body>
	<form enctype="multipart/form-data" action="Home.jsp" method="post"
		name="form1" id="form1">
		<div style="display: none;" id="UpdateProgress1">
			<div class="sfLoadingbg">&nbsp;</div>
			<div class="sfLoadingdiv">
				<img id="imgProgress" src="./images/ajax-loader.gif"
					style="border-width: 0px;" alt="Loading..." title="Loading..." />
				<br> <span id="lblPrgress">Please wait...</span>
			</div>
		</div>
		<div id="divAdminControlPanel">
			<%@ include file="TopStickyBar.jsp"%>
		</div>
		<noscript>
			<span>This page requires java-script to be enabled. Please
				adjust your browser-settings.</span>
		</noscript>

		<div id="sfOuterwrapper">
			<div class="sfSagewrapper">
				<!--Body Content-->
				<div class="sfContentwrapper clearfix">
					<div id="divCenterContent">
						<!-- Side Bar Starts-->
						<div class="sideBarLeft" id="divSideBar">
							<%@ include file="UserSideBar.jsp"%>
						</div>
						<!-- Side Bar Ends -->
						<div class="sfMaincontent">
							<div class="welcome-msg">
								<h2>
									Welcome, <%=session.getAttribute("gpmsUserName")%>
								</h2>
								<p>From your Account Dashboard you have the ability to view
									a snapshot of your recent account activities and update your
									personal account information. Lorem Ipsum is simply dummy text
									of the printing and typesetting industry. Lorem Ipsum has been
									the industry's standard dummy text ever since the 1500s, when
									an unknown printer took a galley of type and scrambled it to
									make a type specimen book. It has survived not only five
									centuries, but also the leap into electronic typesetting,
									remaining essentially unchanged. It was popularised in the
									1960s with the release of Letraset sheets containing Lorem
									Ipsum passages, and more recently with desktop publishing
									software like Aldus PageMaker including versions of Lorem
									Ipsum.</p>
							</div>
							<div class="recent-activity">
								<h2>Overall Activities</h2>
								<ul>
									<li>Total: <a href="./MyProposals.jsp"
										id="recentProposalCount"><span>100 proposals</span></a></li>
									<li>As PI: <a href="#"><span id="recentPICount">100
												proposals</span></a></li>
									<li>As CO-PI: <a href="#"><span id="recentCoPICount">100
												proposals</span></a></li>
									<li>As Senior Personnel: <a href="#"><span
											id="recentSeniorCount">100 proposals</span></a></li>
								</ul>
							</div>
							<div class="welcomeWrap clearfix">
								<h1>Hi! Welcome to GPMS User Control Panel.</h1>
								<h2>Proposal Workflow Management System - A web-based
									application for replacing the manual approval process of grant
									submission. You can take a quick tour to GPMS, on how to run
									and operate the Grant Proposal Workflow Management Framework.
									Get acquainted with the GPMS Dashboard. Take the tour.</h2>
							</div>
							<!-- END sfMaincontent -->
						</div>
					</div>
				</div>
				<!-- END Body Content sfContentwrapper -->
			</div>
		</div>
	</form>
</body>
</html>