<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta content="Manage Users" name="DESCRIPTION">
<meta content="Manage Users" name="KEYWORDS">
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
<title>User Management</title>

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
	var gpmsUserName = "superuser";
	var gpmsCurrentCulture = "en-US";
	var gpmsHostURL = "http://localhost:8181/GPMS/";
	var gpmsSecureToken = "GPMS.AUTHjxr30wycjzvpqd0jv3vkybx4ZADJX9SLOC1";

	var gpmsServicePath = "REST/";
	var gpmsRootPath = "http://localhost:8181/GPMS/";
	var userProfileId = "565de4c154ffd81f18d4e4a0";

	var sessionCode = "jxr30wycjzvpqd0jv3vkybx4";
	var clientIPAddress = "::1";
	var gpmsCountryName = "RESERVED";
	var gpmsRedirectPath = "/";

	var logInURL = "login";
	var pageExtension = ".jsp";

	$(function() {
		$(".sfLocale").localize({
			moduleKey : gpmsUsersManagement
		});
	});
	//]]>
</script>

<script type="text/javascript" src="js/jQuery/jquery-ui.js"></script>

<script type="text/javascript" src="js/core/gpmscore.js"></script>

<script type="text/javascript" src="js/core/jquery.disable_with.js"></script>

<!-- For Side Bar Navigation -->
<script type="text/javascript" src="js/core/dashboard.js"></script>
<script type="text/javascript" src="js/sidebar_accordian.js"></script>
<script type="text/javascript" src="js/superfish.js"></script>

<script type="text/javascript"
	src="js/FormValidation/jquery.form-validation-and-hints.js"></script>
<script type="text/javascript"
	src="js/FormValidation/jquery.validate.js"></script>
<script type="text/javascript"
	src="js/FormValidation/jquery.ui.datepicker.validation.js"></script>

<script type="text/javascript"
	src="js/FormValidation/jquery.maskedinput.js"></script>

<!-- <script type="text/javascript" src="js/SystemLocale/systemlocale.js"></script> -->
<script type="text/javascript"
	src="js/modules/Language/CoreJsLanguage.js"></script>

<script type="text/javascript" src="js/core/json2.js"></script>

<script type="text/javascript" src="js/jquery-browser.js"></script>
<script type="text/javascript" src="js/jquery.uniform.js"></script>

<script type="text/javascript" src="js/jquery.qtip-1.0.0-rc3.js"></script>

<script type="text/javascript" src="js/GridView/jquery.tablesorter.js"></script>
<script type="text/javascript" src="js/GridView/jquery.grid.js"></script>
<script type="text/javascript" src="js/GridView/SagePaging.js"></script>
<script type="text/javascript" src="js/GridView/jquery.global.js"></script>
<script type="text/javascript" src="js/GridView/jquery-dateFormat.js"></script>

<script type="text/javascript" src="js/MessageBox/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="js/MessageBox/alertbox.js"></script>

<script type="text/javascript" src="js/AjaxFileUploader/ajaxupload.js"></script>

<script type="text/javascript"
	src="js/ckeditor_4.5.1_full/ckeditor/ckeditor.js"></script>
<script type="text/javascript"
	src="js/ckeditor_4.5.1_full/ckeditor/adapters/jquery.js"></script>


<!-- <script type="text/javascript" src="js/core/Session.js"></script> -->
<script type="text/javascript" src="js/core/encoder.js"></script>

<script type="text/javascript" src="js/modules/UsersManage.js"></script>
<script type="text/javascript"
	src="js/modules/Language/GPMSUsersManagement.js"></script>
<!-- <script type="text/javascript" src="js/modules/Language/AspxRssFeedLocale.js"></script> -->

<link type="text/css" rel="stylesheet"
	href="css/Templates/jquery-ui.css" />
<link type="text/css" rel="stylesheet" href="css/GridView/tablesort.css" />
<link type="text/css" rel="stylesheet" href="css/MessageBox/style.css" />

<link type="text/css" rel="stylesheet" href="css/Templates/grid.css" />
<link type="text/css" rel="stylesheet"
	href="css/Templates/topstickybar.css" />
<link type="text/css" rel="stylesheet" href="css/Templates/admin.css" />
</head>
<body>
	<form enctype="multipart/form-data" action="Manage-Users.jsp"
		method="post" name="form1" id="form1">
		<div style="display: none;" id="UpdateProgress1">
			<div class="sfLoadingbg">&nbsp;</div>
			<div class="sfLoadingdiv">
				<img id="imgProgress" src="images/ajax-loader.gif"
					style="border-width: 0px;" alt="Loading..." title="Loading..." />
				<br> <span id="lblPrgress">Please wait...</span>
			</div>
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
							<%@ include file="Sidebar.jsp"%>
						</div>
						<!-- Side Bar Ends -->

						<div class="sfMaincontent">
							<div style="display: block" class="sfCpanel sfInnerwrapper"
								id="divBottompanel">
								<div class="sfModulecontent clearfix">
									<!-- Grid -->
									<div id="divUserGrid">
										<div class="cssClassCommonBox Curve">
											<div class="cssClassHeader">
												<h1>
													<span>Manage Users</span>
												</h1>
												<div class="cssClassHeaderRight">
													<div class="sfButtonwrapper">
														<p>
															<button title="Add New User" type="button" id="btnAddNew"
																class="sfBtn">
																<span class="sfLocale icon-addnew">Add New User</span>
															</button>
														</p>
														<p>
															<button title="Delete All Selected" type="button"
																id="btnDeleteSelected" class="sfBtn">
																<span class="sfLocale icon-delete">Delete All
																	Selected</span>
															</button>
														</p>
														<p>
															<button title="Export to Excel" type="button"
																id="btnExportToExcel" class="sfBtn">
																<span class="sfLocale icon-showall">Export to
																	Excel</span>
															</button>
														</p>
														<p>
															<button title="Export to CSV" type="button"
																id="btnExportToCSV" class="sfBtn">
																<span class="sfLocale icon-showall">Export to CSV</span>
															</button>
														</p>

														<div class="cssClassClear"></div>
													</div>
													<div class="cssClassClear"></div>
												</div>
												<div class="cssClassClear"></div>
											</div>
											<div class="sfGridwrapper">
												<div class="sfGridWrapperContent">
													<div class="sfFormwrapper sfTableOption">
														<table width="100%" cellspacing="0" cellpadding="0"
															border="0">
															<tbody>
																<tr>
																	<td><label class="cssClassLabel sfLocale">User
																			Name:</label> <input title="User Name" type="text"
																		class="sfTextBoxFix" id="txtSearchUserName"
																		placeholder="User Name" /></td>

																	<td><label class="cssClassLabel sfLocale">
																			College:</label> <select title="Choose College"
																		id="ddlSearchCollege" class="sfListmenu"
																		style="width: 90px;">
																			<option value="0" class="sfLocale">--All--</option>
																	</select></td>

																	<td><label class="cssClassLabel sfLocale">
																			Department:</label> <select title="Choose Department"
																		id="ddlSearchDepartment" class="sfListmenu"
																		style="width: 143px;">
																			<option value="0" class="sfLocale">--All--</option>
																	</select></td>

																	<td><label class="cssClassLabel sfLocale">
																			Position Type:</label> <select title="Choose Position Type"
																		id="ddlSearchPositionType" class="sfListmenu"
																		style="width: 150px;">
																			<option value="0" class="sfLocale">--All--</option>
																	</select></td>

																	<td><label class="cssClassLabel sfLocale">
																			Position Title:</label> <select title="Choose Position Title"
																		id="ddlSearchPositionTitle" class="sfListmenu"
																		style="width: 150px;">
																			<option value="0" class="sfLocale">--All--</option>
																	</select></td>

																	<!-- <td width="315"><label
																			class="cssClassLabel sfLocale">Added On:</label><br />
																			<span class="label sfLocale">From:</span> <input title="Added On From" 
																			type="text" id="txtSearchAddedOnFrom"
																			class="sfTextBoxSmall"
																			style="width: 80px !important;"/> <span
																			class="label sfLocale">To:</span> <input title="Added On To" 
																			type="text" id="txtSearchAddedOnTo" class="sfTextBoxSmall"
																			style="width: 80px !important;"/></td> -->

																	<td><label class="cssClassLabel sfLocale">
																			Active:</label> <select title="Is Active?"
																		id="ddlSearchIsActive" class="sfListmenu"
																		style="width: 50px;">
																			<option value="" class="sfLocale">--All--</option>
																			<option value="True" class="sfLocale">True</option>
																			<option value="False" class="sfLocale">False</option>
																	</select></td>
																	<td><label class="cssClassLabel">&nbsp;</label>
																		<button title="Search User" class="sfBtn"
																			id="btnSearchUser" type="button">
																			<span class="sfLocale icon-search">Search</span>
																		</button></td>
																</tr>
															</tbody>
														</table>
													</div>
													<div class="loading">
														<img id="ajaxLoader" src="" class="sfLocale"
															alt="Loading..." title="Loading..." />
													</div>
													<div class="log"></div>
													<table id="gdvUsers" cellspacing="0" cellpadding="0"
														border="0" width="100%"></table>
												</div>
											</div>
										</div>
									</div>
									<!-- End of Grid -->
									<!-- form -->
									<div id="divUserForm" style="display: none">
										<div class="cssClassCommonBox Curve">
											<div class="cssClassHeader">
												<h1>
													<span id="lblFormHeading">New User Details</span>
												</h1>
												<div>
													<span class="cssClassRequired">*</span> <span
														class="cssClassLabelTitle">indicates required
														fields</span>
												</div>
											</div>
											<div class="cssClassTabPanelTable">
												<div id="container-7">
													<ul>
														<li><a href="#fragment-1"> <span
																id="lblTabTitle1">General Information</span>
														</a></li>
														<li><a href="#fragment-2"> <span
																id="lblTabTitle2">User Position Details</span>
														</a></li>
														<li><a href="#fragment-3"> <span
																id="lblTabTitle3">User Login Credentials</span>
														</a></li>
														<li id="auditLogTab"><a href="#fragment-4"> <span
																id="lblTabTitle4">Audit Logs</span>
														</a></li>
													</ul>
													<div id="fragment-1">
														<div class="sfFormwrapper">
															<table cellspacing="0" cellpadding="0" border="0">
																<tbody>
																	<tr class="rule dashed">
																		<td colspan="4"><span class="cssClassLabelTitle"
																			id="lblName">User Information</span></td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabel" id="lblUserName">First
																				Name:</span> <span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="First Name" type="text" class="sfInputbox"
																			id="txtFirstName" name="firstName"
																			placeholder="First Name" /></td>
																		<td><span class="cssClassLabel"
																			id="lblMiddleName">Middle Name:</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Middle Name" type="text" class="sfInputbox"
																			id="txtMiddleName" name="middleName"
																			placeholder="Middle Name" /></td>
																	</tr>
																	<tr>
																		<td><span id="lblLastName" class="cssClassLabel">Last
																				Name:</span> <span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol" colspan="3"><input
																			title="Last Name" type="text" id="txtLastName"
																			class="sfInputbox" name="lastName"
																			placeholder="Last Name" /></td>
																	</tr>
																	<tr>
																		<td><span id="lblDOB" class="cssClassLabel">Date
																				of Birth:</span> <span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Date of Birth" type="text" id="txtDOB"
																			class="sfInputbox" name="dob"
																			placeholder="Date of Birth" /></td>
																		<td><span id="lblGender" class="cssClassLabel">Gender:</span>
																			<span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><select
																			title="Choose Gender" id="ddlGender" name="gender">
																				<option value="">Choose Gender</option>
																				<option value="Male">Male</option>
																				<option value="Female">Female</option>
																		</select></td>
																	</tr>
																	<tr class="rule dashed">
																		<td colspan="4"><span id="lblAddress"
																			class="cssClassLabelTitle">Current Address</span></td>
																	</tr>

																	<tr>
																		<td><span class="cssClassLabel" id="lblStreet">Street:</span>
																			<span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Street" type="text" class="sfInputbox"
																			id="txtStreet" name="street" placeholder="Street" /></td>
																		<td><span class="cssClassLabel" id="lblApt">Apt.,
																				Suit, Floor, etc:</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Apt., Suit, Floor, etc. (optional)"
																			type="text" class="sfInputbox" id="txtApt" name="apt"
																			placeholder="Apt., Suite, Floor, etc. (optional)" /></td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabel" id="lblCity">City:</span>
																			<span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="City" type="text" class="sfInputbox"
																			id="txtCity" name="city" placeholder="City" /></td>
																		<td><span class="cssClassLabel" id="lblState">State:</span>
																			<span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><select
																			title="Choose State" id="ddlState" name="state"><option
																					value="">Choose State</option>
																				<option value="1">Alabama</option>
																				<option value="2">Alaska</option>
																				<option value="4">Arizona</option>
																				<option value="5">Arkansas</option>
																				<option value="6">California</option>
																				<option value="7">Colorado</option>
																				<option value="8">Connecticut</option>
																				<option value="10">Delaware</option>
																				<option value="57">District of Columbia</option>
																				<option value="11">Florida</option>
																				<option value="12">Georgia</option>
																				<option value="14">Hawaii</option>
																				<option value="15">Idaho</option>
																				<option value="16">Illinois</option>
																				<option value="17">Indiana</option>
																				<option value="18">Iowa</option>
																				<option value="19">Kansas</option>
																				<option value="20">Kentucky</option>
																				<option value="21">Louisiana</option>
																				<option value="22">Maine</option>
																				<option value="23">Maryland</option>
																				<option value="24">Massachusetts</option>
																				<option value="25">Michigan</option>
																				<option value="26">Minnesota</option>
																				<option value="27">Mississippi</option>
																				<option value="28">Missouri</option>
																				<option value="29">Montana</option>
																				<option value="30">Nebraska</option>
																				<option value="31">Nevada</option>
																				<option value="32">New Hampshire</option>
																				<option value="33">New Jersey</option>
																				<option value="34">New Mexico</option>
																				<option value="35">New York</option>
																				<option value="36">North Carolina</option>
																				<option value="37">North Dakota</option>
																				<option value="39">Ohio</option>
																				<option value="40">Oklahoma</option>
																				<option value="41">Oregon</option>
																				<option value="42">Pennsylvania</option>
																				<option value="58">Puerto Rico</option>
																				<option value="44">Rhode Island</option>
																				<option value="45">South Carolina</option>
																				<option value="46">South Dakota</option>
																				<option value="47">Tennessee</option>
																				<option value="48">Texas</option>
																				<option value="59">U.S. Virgin Islands</option>
																				<option value="49">Utah</option>
																				<option value="50">Vermont</option>
																				<option value="51">Virginia</option>
																				<option value="53">Washington</option>
																				<option value="54">West Virginia</option>
																				<option value="55">Wisconsin</option>
																				<option value="56">Wyoming</option>
																		</select></td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabel" id="lblZip">Zip
																				Code:</span> <span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Zip" type="text" class="sfInputbox"
																			id="txtZip" name="zip" placeholder="Zip" /></td>
																		<td><span class="cssClassLabel" id="lblCountry">Country:</span>
																			<span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><select
																			title="Choose Country" id="ddlCountry" name="country"><option
																					value="">Choose Country</option>
																				<option value="1">United States</option>
																		</select></td>
																	</tr>
																	<tr class="rule dashed">
																		<td colspan="4"><span class="cssClassLabelTitle"
																			id="lblPhone">Phone</span></td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabel"
																			id="lblOfficeNumber">Office Number:</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Office Number" type="text" class="sfInputbox"
																			id="txtOfficeNumber" name="officeNumber"
																			placeholder="Office Number" /></td>
																		<td><span class="cssClassLabel"
																			id="lblMobileNumber">Mobile Number:</span> <span
																			class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Mobile Number" type="text" class="sfInputbox"
																			id="txtMobileNumber" name="mobileNumber"
																			placeholder="Mobile Number" /></td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabel"
																			id="lblHomeNumber">Home Number:</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Home Number" type="text" class="sfInputbox"
																			id="txtHomeNumber" name="homeNumber"
																			placeholder="Home Number" /></td>
																		<td><span class="cssClassLabel"
																			id="lblOtherNumber">Other:</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Other" type="text" class="sfInputbox"
																			id="txtOtherNumber" name="otherNumber"
																			placeholder="Other Number" /></td>
																	</tr>
																	<tr class="rule dashed">
																		<td colspan="4"><span class="cssClassLabelTitle"
																			id="lblEmail">E-mail Address</span></td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabel" id="lblWorkEmail">Work
																				Email:</span> <span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol">
																			<div class="input-group">
																				<div class="input-group-addon">@</div>
																				<input type="email" id="txtWorkEmail"
																					class="sfInputbox" style="width: 160px;"
																					name="workEmail" placeholder="Work Email" /> <span
																					class="cssClassRight"> <img src=""
																					class="cssClassSuccessImg sfLocale" height="13"
																					width="18" alt="Right" title="Right" />
																				</span>
																			</div>
																		</td>
																		<td><span class="cssClassLabel"
																			id="lblPersonalEmail">Personal Email:</span></td>
																		<td class="cssClassTableRightCol">
																			<div class="input-group">
																				<div class="input-group-addon">@</div>
																				<input type="email" class="sfInputbox"
																					style="width: 160px;" id="txtPersonalEmail"
																					name="personalEmail" placeholder="Personal Email" /><span
																					class="cssClassRight"> <img src=""
																					class="cssClassSuccessImg sfLocale" height="13"
																					width="18" alt="Right" title="Right" />
																				</span>
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabel" id="lblActive">Active:</span></td>
																		<td class="cssClassTableRightCol" colspan="3"><input
																			title="Active" type="checkbox" value=""
																			name="chkActive" class="cssClassCheckBox"
																			checked="checked"></td>
																	</tr>
																</tbody>
															</table>
														</div>
													</div>
													<div id="fragment-2">
														<div class="sfFormwrapper">
															<table cellspacing="0" cellpadding="0" border="0">
																<tbody>
																	<tr>
																		<td class="cssClassTableRightCol">
																			<table id="dataTable" cellspacing="0" cellpadding="0"
																				border="0" width="100%">
																				<thead>
																					<tr>
																						<th><span class="cssClassLabel">College:</span>
																							<span class="cssClassRequired">*</span></th>
																						<th><span class="cssClassLabel">Department:</span>
																							<span class="cssClassRequired">*</span></th>
																						<th><span class="cssClassLabel">Position
																								Type:</span> <span class="cssClassRequired">*</span></th>
																						<th><span class="cssClassLabel">Position
																								Title:</span> <span class="cssClassRequired">*</span></th>
																						<th><span class="cssClassLabel">Default:</span>
																							<span class="cssClassRequired">*</span></th>
																						<th></th>
																					</tr>
																				</thead>
																				<tbody>
																					<tr>
																						<td><select title="Choose College Name"
																							class="sfListmenu" name="ddlCollege">
																						</select></td>
																						<td><select title="Choose Department Name"
																							class="sfListmenu" name="ddlDepartment">
																						</select></td>
																						<td><select title="Choose Position Type"
																							class="sfListmenu" name="ddlPositionType">
																						</select></td>
																						<td><select title="Choose Position Title"
																							class="sfListmenu" name="ddlPositionTitle">
																						</select></td>
																						<td><input type="radio"
																							title="Choose Default Position"
																							class="class-isdefault" checked="checked"></td>
																						<td><input type="Button" value="Add More"
																							name="AddMore"
																							class="AddOption cssClassButtonSubmit sfLocale" />
																						</td>
																					</tr>
																				</tbody>
																			</table>
																		</td>
																	</tr>
																</tbody>
															</table>
														</div>
													</div>
													<div id="fragment-3">
														<div class="sfFormwrapper">
															<table cellspacing="0" cellpadding="0" border="0">
																<tbody>
																	<tr>
																		<td><span class="cssClassLabel" id="lblUserName">User
																				Name:</span> <span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="User Name" type="text" class="sfInputbox"
																			id="txtUserName" name="username"
																			placeholder="User Name"> <span
																			class="cssClassRight"> <img src=""
																				class="cssClassSuccessImg sfLocale" height="13"
																				width="18" alt="Right" title="Right" />
																		</span></td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabel" id="lblPassword">Password:</span>
																			<span class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Password" type="password" class="sfInputbox"
																			id="txtPassword" name="password"
																			placeholder="Password" /></td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabel"
																			id="lblConfirmPassword">Confirm Password:</span> <span
																			class="cssClassRequired">*</span></td>
																		<td class="cssClassTableRightCol"><input
																			title="Confirm Password" type="password"
																			class="sfInputbox" id="txtConfirmPassword"
																			name="confirm_password"
																			placeholder="Password (Again)" /></td>
																	</tr>
																</tbody>
															</table>
														</div>
													</div>
													<div id="fragment-4">
														<div id="divUserAuditGrid">
															<div class="cssClassCommonBox Curve">
																<div class="sfGridwrapper">
																	<div class="sfGridWrapperContent">
																		<div class="sfFormwrapper sfTableOption">
																			<table width="100%" cellspacing="0" cellpadding="0"
																				border="0">
																				<tbody>
																					<tr>
																						<td><label class="cssClassLabel sfLocale">
																								Action:</label> <input title="Action" type="text"
																							class="sfInputbox" id="txtSearchAction"
																							placeholder="Action" /></td>
																						<td><label class="cssClassLabel sfLocale">
																								Audited By:</label> <input title="Audited By"
																							type="text" class="sfInputbox"
																							id="txtSearchAuditedBy" placeholder="Audited By" /></td>
																						<td><label class="cssClassLabel sfLocale">
																								Activity On From:</label> <input
																							title="Activity On From" type="text"
																							class="sfTextBoxSmall"
																							id="txtSearchActivityOnFrom" placeholder="From" /></td>
																						<td><label class="cssClassLabel sfLocale">
																								Activity On To:</label> <input title="Activity On To"
																							type="text" class="sfTextBoxSmall"
																							id="txtSearchActivityOnTo" placeholder="To" /></td>

																						<td><label class="cssClassLabel"> </label>
																							<button title="Search Audit Log" class="sfBtn"
																								id="btnSearchUserAuditLog" type="button">
																								<span class="sfLocale icon-search">Search</span>
																							</button></td>
																					</tr>
																				</tbody>
																			</table>
																		</div>
																		<div class="loading">
																			<img id="ajaxLoader" src="" class="sfLocale"
																				alt="Loading..." title="Loading..." />
																		</div>
																		<div class="log"></div>
																		<table id="gdvUsersAuditLog" cellspacing="0"
																			cellpadding="0" border="0" width="100%"></table>
																	</div>
																</div>
															</div>
															<table id="tblLastAuditedInfo" cellspacing="0"
																cellpadding="0" border="0">
																<tbody>
																	<tr>
																		<td><span class="cssClassLabelTitle">Last
																				Audited On:&nbsp;</span></td>
																		<td class="cssClassTableRightCol"><span
																			id="lblLastUpdatedOn" class="cssClassLabel"></span></td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabelTitle">Last
																				Audited By:&nbsp;</span></td>
																		<td class="cssClassTableRightCol"><span
																			id="lblLastUpdatedBy" class="cssClassLabel"></span></td>
																	</tr>
																	<tr>
																		<td><span class="cssClassLabelTitle">Last
																				Activity:&nbsp;</span></td>
																		<td class="cssClassTableRightCol"><span
																			id="lblActivity" class="cssClassLabel"></span></td>
																	</tr>
																</tbody>
															</table>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="sfButtonwrapper">
											<p>
												<button title="Go Back" type="button" id="btnBack"
													class="sfBtn">
													<span class="sfLocale icon-arrow-slim-w">Back</span>
												</button>
											</p>
											<p>
												<button title="Reset" type="button" id="btnReset"
													class="sfBtn">
													<span class="sfLocale icon-refresh">Reset</span>
												</button>
											</p>
											<p>
												<button title="Delete" type="button" class="delbutton sfBtn">
													<span class="sfLocale icon-delete">Delete</span>
												</button>
											</p>
											<p>
												<button title="Save User" type="button" id="btnSaveUser"
													class="sfBtn">
													<span class="sfLocale icon-save">Save</span>
												</button>
											</p>
										</div>
									</div>
									<!-- End form -->
								</div>

							</div>
						</div>
						<!-- END sfMaincontent -->
					</div>
				</div>
				<!-- END Body Content sfContentwrapper -->
			</div>
		</div>
	</form>
</body>
</html>