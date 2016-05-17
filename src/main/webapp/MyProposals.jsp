<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta content="My Proposals" name="DESCRIPTION">
<meta content="My Proposals" name="KEYWORDS">
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
<title>My Proposals</title>

<script type="text/javascript" src="js/jQuery/jquery-1.11.3.min.js"></script>

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

	$(function() {
		//For Sidebar active menu
		$('.acitem').find('a').eq(0).prop("class", "active");
	});

	//]]>
</script>

<script type="text/javascript" src="js/jQuery/jquery-ui.js"></script>

<script type="text/javascript" src="js/core/gpmscore.js"></script>
<script type="text/javascript" src="js/core/encoder.js"></script>

<script type="text/javascript" src="js/core/jquery.disable_with.js"></script>

<!-- For Side Bar Navigation -->
<script type="text/javascript" src="js/core/dashboard.js"></script>
<script type="text/javascript" src="js/sidebar_accordian.js"></script>
<script type="text/javascript" src="js/superfish.js"></script>

<script type="text/javascript"
	src="js/FormValidation/jquery.validate.js"></script>
<script type="text/javascript"
	src="js/FormValidation/jquery.ui.datepicker.validation.js"></script>
<script type="text/javascript"
	src="js/FormValidation/jquery.maskedinput.js"></script>
<script type="text/javascript" src="js/FormValidation/autoNumeric.js"></script>

<script type="text/javascript" src="js/core/json2.js"></script>

<script type="text/javascript" src="js/jquery-browser.js"></script>
<script type="text/javascript" src="js/jquery.uniform.js"></script>

<script type="text/javascript" src="js/GridView/jquery.tablesorter.js"></script>
<script type="text/javascript" src="js/GridView/jquery.grid.js"></script>
<script type="text/javascript" src="js/GridView/SagePaging.js"></script>
<script type="text/javascript" src="js/GridView/jquery.global.js"></script>
<script type="text/javascript" src="js/GridView/jquery-dateFormat.js"></script>

<script type="text/javascript" src="js/MessageBox/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="js/MessageBox/alertbox.js"></script>
<script type="text/javascript" src="js/modules/MyProposals.js"></script>

<script type="text/javascript" src="js/Uploader/jquery.uploadfile.js"></script>

<link type="text/css" rel="stylesheet"
	href="css/Templates/jquery-ui.css" />

<link type="text/css" rel="stylesheet"
	href="css/Templates/uploadfile.css">

<link type="text/css" rel="stylesheet" href="css/MessageBox/style.css" />

<link type="text/css" rel="stylesheet" href="css/GridView/tablesort.css" />
<link type="text/css" rel="stylesheet" href="css/Templates/grid.css" />
<link type="text/css" rel="stylesheet"
	href="css/Templates/topstickybar.css" />
<link type="text/css" rel="stylesheet" href="css/Templates/admin.css" />
</head>
<body>
	<form enctype="multipart/form-data" action="MyProposals.jsp"
		method="post" name="form1" id="form1">
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
							<div style="display: block" class="sfCpanel sfInnerwrapper"
								id="divBottompanel">
								<div class="sfModulecontent clearfix">
									<!-- Grid -->
									<div id="divProposalGrid">
										<div class="cssClassCommonBox Curve">
											<div class="cssClassHeader">
												<h1>
													<span>Manage Your Proposals</span>
												</h1>
												<div class="cssClassHeaderRight">
													<div class="sfButtonwrapper">
														<p>
															<button title="Add New Proposal" type="button"
																id="btnAddNew" class="sfBtn">
																<span class="icon-addnew">Add New Proposal</span>
															</button>
														</p>
														<!-- 														<p> -->
														<!-- 															<button title="Delete All Selected" type="button" -->
														<!-- 																id="btnDeleteSelected" class="sfBtn"> -->
														<!-- 																<span class="icon-delete">Delete All Selected</span> -->
														<!-- 															</button> -->
														<!-- 														</p> -->
														<p>
															<button title="Export to Excel" type="button"
																id="btnExportToExcel" class="sfBtn">
																<span class="icon-excel">Export to Excel</span>
															</button>
														</p>
														<!-- <p>
															<button title="Export to CSV" type="button"
																id="btnExportToCSV" class="sfBtn">
																<span class="icon-excel">Export to CSV</span>
															</button>
														</p> -->

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
																	<td><label class="cssClassLabel">Project
																			Title:</label> <input title="Project Title" type="text"
																		class="sfTextBoxFix" id="txtSearchProjectTitle"
																		placeholder="Project Title" /></td>
																	<td><label class="cssClassLabel">User
																			Name:</label> <input title="User Name" id="txtSearchUserName"
																		class="sfTextBoxFix" type="text"
																		placeholder="User Name" /></td>

																	<!-- 																		<td><label class="cssClassLabel"> -->
																	<!-- 																				Project Type:</label> <select title="Choose Project Type" id="ddlProjectType" -->
																	<!-- 																			class="sfListmenu" style="width: 100px;"> -->
																	<!-- 																				<option value="0" >--All--</option> -->
																	<!-- 																		</select></td> -->
																	<!-- 																		<td><label class="cssClassLabel"> -->
																	<!-- 																				Type of Request:</label> <select title="Choose Type of Request" id="ddlTypeOfRequest" -->
																	<!-- 																			class="sfListmenu" style="width: 100px;"> -->
																	<!-- 																				<option value="0" >--All--</option> -->
																	<!-- 																		</select></td> -->
																	<!-- 																		<td><label class="cssClassLabel"> -->
																	<!-- 																				Location of Project:</label> <select  title="Choose Location of Project" id="ddlLocationOfProject" -->
																	<!-- 																			class="sfListmenu" style="width: 100px;"> -->
																	<!-- 																				<option value="0" >--All--</option> -->
																	<!-- 																		</select></td> -->

																	<td style="width: 180px; float: left;"><label
																		class="cssClassLabel">Recieved On:</label>
																		<div>
																			<span class="cssClassLabel">From:</span> <input
																				type="text" title="Received On From"
																				id="txtSearchSubmittedOnFrom" class="sfTextBoxFix"
																				placeholder="From">
																		</div>
																		<div>
																			<span class="cssClassLabel">To:</span> <input
																				type="text" title="Received On To"
																				id="txtSearchSubmittedOnTo" class="sfTextBoxFix"
																				placeholder="To">
																		</div></td>

																	<td style="width: 180px;"><label
																		class="cssClassLabel">Total Costs:</label>
																		<div>
																			<span class="cssClassLabel">From:</span> <input
																				type="text" title="Total Costs From"
																				id="txtSearchTotalCostsFrom"
																				name="searchTotalCostsFrom" class="sfTextBoxFix"
																				placeholder="From">
																		</div>
																		<div>
																			<span class="cssClassLabel">To:</span> <input
																				type="text" title="Total Costs To"
																				id="txtSearchTotalCostsTo" name="searchTotalCostsTo"
																				class="sfTextBoxFix" placeholder="To">
																		</div></td>

																	<td><label class="cssClassLabel">Proposal
																			Status:</label> <select title="Choose Proposal Status"
																		id="ddlSearchProposalStatus" class="sfListmenu"
																		style="width: 80px;">
																			<option value="0">--All--</option>
																	</select></td>

																	<td><label class="cssClassLabel">As:</label> <select
																		title="Choose User Role" id="ddlSearchUserRole"
																		class="sfListmenu" style="width: 58px;">
																			<option value="0">--All--</option>
																			<option value="PI">PI</option>
																			<option value="Co-PI">Co-PI</option>
																			<option value="Senior Personnel">Senior Personnel</option>
																	</select></td>

																	<td><label class="cssClassLabel">&nbsp;</label>
																		<button title="Search Proposal" class="sfBtn"
																			id="btnSearchProposal" type="button">
																			<span class="icon-search">Search</span>
																		</button></td>
																</tr>
															</tbody>
														</table>
													</div>
													<div class="loading">
														<img id="ajaxLoader" src="" alt="Loading..."
															title="Loading..." />
													</div>
													<div class="log"></div>
													<table id="gdvProposals" cellspacing="0" cellpadding="0"
														border="0" width="100%"></table>
												</div>
											</div>
										</div>
									</div>
									<!-- End of Grid -->
									<!-- form -->
									<div id="divProposalForm" style="display: none">
										<div class="cssClassCommonBox Curve">
											<div class="cssClassHeader">
												<h1>
													<span id="lblFormHeading">New Proposal Details</span>
												</h1>
												<div class="cssClassLeft">
													<img src="images/bsu_logo.png" alt="Boise State University"
														title="Boise State University">
												</div>
												<div class="cssClassMiddle"
													style="text-align: center; width: 60%;">
													<div class="cssClassLabelHeader">Office of Sponsored
														Programs</div>
													<div class="cssClassLabelHeader">Proposal Data Sheet</div>
													<span class="cssClassLabelTitle">Proposals must be
														submitted to OSP <u>3 working days prior</u> to the
														proposal submission deadline.
													</span>
												</div>
											</div>
											<div>
												<span class="cssClassRequired">*</span> <span
													class="cssClassLabelTitle">indicates required fields</span>
												<div id="accordion-expand-holder" style="display: none;">
													<button type="button" class="expandAll sfBtn">Expand
														all</button>
													<button type="button" class="collapseAll sfBtn">Collapse
														all</button>
												</div>
											</div>
											<!-- 										<form enctype="multipart/form-data" action="MyProposals.jsp" -->
											<!-- 											method="post" name="form1" id="form1"> -->
											<div id="accordion">
												<h3>
													<span id="lblSection1">Investigator Information</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0"
														id="dataTable">
														<thead>
															<tr>
																<th><span class="cssClassLabelTitle">Role:</span> <span
																	class="cssClassRequired">*</span></th>
																<th><span class="cssClassLabelTitle">Name:</span> <span
																	class="cssClassRequired">*</span></th>
																<th><span class="cssClassLabelTitle">College:</span> <span
																	class="cssClassRequired">*</span></th>
																<th><span class="cssClassLabelTitle">Department:</span>
																	<span class="cssClassRequired">*</span></th>
																<th><span class="cssClassLabelTitle">Position
																		Type:</span> <span class="cssClassRequired">*</span></th>
																<th><span class="cssClassLabelTitle">Position
																		Title:</span> <span class="cssClassRequired">*</span></th>
																<th><span class="cssClassLabelTitle">Phone #:</span> <span
																	class="cssClassRequired">*</span></th>
																<th></th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<td><select title="Choose Role" class="sfListmenu"
																	name="ddlRole" style="width: 55px;" required="true">
																		<option value="0">PI</option>
																		<option value="1">Co-PI</option>
																		<option value="2">Senior Personnel</option>
																</select></td>
																<td><select title="Choose Full Name"
																	class="sfListmenu" name="ddlName" style="width: 140px;"
																	required="true">
																</select></td>
																<td><select title="Choose College Name"
																	class="sfListmenu" name="ddlCollege"
																	style="width: 90px;" required="true">
																</select></td>
																<td><select title="Choose Department Name"
																	class="sfListmenu" name="ddlDepartment"
																	style="width: 126px;" required="true">
																</select></td>
																<td><select title="Choose Position Type"
																	class="sfListmenu" name="ddlPositionType"
																	style="width: 122px;" required="true">
																</select></td>
																<td><select title="Choose Position Title"
																	class="sfListmenu" name="ddlPositionTitle"
																	style="width: 169px;" required="true">
																</select></td>
																<td><input title="Phone #" type="text"
																	readonly="readonly" class="sfTextBoxSmall"
																	name="txtPhoneNo" placeholder="Phone #"
																	style="width: 90px !important" required="true" /></td>
																<td><input type="Button" value="Add Co-PI"
																	name="AddCoPI" class="AddCoPI cssClassButtonSubmit" /></td>
																<td><input type="Button" value="Add Senior Personnel"
																	name="AddSenior" class="AddSenior cssClassButtonSubmit" /></td>
															</tr>
														</tbody>
													</table>
												</div>

												<h3>
													<span id="lblSection2">Project Information</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabelTitle"
																	id="lblProjectTitle">Project Title:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol" colspan="3"><textarea
																		title="Project Title" class="cssClassTextArea"
																		cols="26" rows="2" name="projectTitle"
																		id="txtProjectTitle" placeholder="Project Title"></textarea><span
																	class="cssClassRight"> <img
																		src="./images/right.jpg" class="cssClassSuccessImg"
																		height="13" width="18" alt="Right" title="Right" />
																</span></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel" id="lblProjectType">Project
																		Type:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Project Type" id="ddlProjectType"
																	name="projectType">
																		<option value="">Choose Project Type</option>
																		<option value="1">Research-Basic</option>
																		<option value="2">Research-Applied</option>
																		<option value="3">Research-Development</option>
																		<option value="4">Instruction</option>
																		<option value="5">Other Sponsored Activity</option>
																</select></td>
																<td><span class="cssClassLabel"
																	id="lblTypeOfRequest">Type of Request:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Type of Request" id="ddlTypeOfRequest"
																	name="typeOfRequest">
																		<option value="">Choose Type of Request</option>
																		<option value="1">Pre-Proposal</option>
																		<option value="2">New Proposal</option>
																		<option value="3">Continuation</option>
																		<option value="4">Supplement</option>
																</select></td>
															</tr>
															<tr>
																<td><span id="lblDueDate" class="cssClassLabel">Due
																		Date:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="Due Date" type="text" id="txtDueDate"
																	class="sfInputbox" name="dueDate"
																	placeholder="Due Date" /></td>

																<td><span id="lblLocationOfProject"
																	class="cssClassLabel">Location of Project:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Location of Project"
																	id="ddlLocationOfProject" name="locationOfProject">
																		<option value="">Choose Location of Project</option>
																		<option value="1">Off-campus</option>
																		<option value="2">On-campus</option>
																</select></td>
															</tr>
															<tr>
																<td><span id="lblProjectPeriod"
																	class="cssClassLabel">Project Period: From:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="Project Period From" type="text"
																	id="txtProjectPeriodFrom" class="sfInputbox"
																	name="projectPeriodFrom" placeholder="From" /></td>

																<td><span id="lblProjectPeriodTo"
																	class="cssClassLabel">To:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="Project Period To" type="text"
																	id="txtProjectPeriodTo" class="sfInputbox"
																	name="projectPeriodTo" placeholder="To" /></td>
															</tr>
														</tbody>
													</table>
												</div>

												<h3>
													<span id="lblSection3">Sponsor and Budget Information</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabel"
																	id="lblNameOfGrantingAgency">Name of Granting
																		Agency:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol" colspan="3"><input
																	title="Name of Granting Agency" type="text"
																	class="sfInputbox" id="txtNameOfGrantingAgency"
																	name="nameOfGrantingAgency"
																	placeholder="Name of Granting Agency" /> <span
																	class="cssClassLabel cssClassInfo">Enter comma
																		separated names.</span></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel" id="lblDirectCosts">Direct
																		Costs:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="Direct Costs" type="text" class="sfInputbox"
																	id="txtDirectCosts" name="directCosts"
																	placeholder="Direct Costs" style="text-align: right;" /></td>

																<td><span class="cssClassLabel" id="lblFACosts">F&A
																		Costs:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="F&A Costs" type="text" class="sfInputbox"
																	id="txtFACosts" name="FACosts" placeholder="F&A Costs"
																	style="text-align: right;" /></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel" id="lblTotalCosts">Total
																		Costs:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="Total Costs" type="text" class="sfInputbox"
																	id="txtTotalCosts" name="totalCosts"
																	placeholder="Total Costs" style="text-align: right;" /></td>
																<td><span class="cssClassLabel" id="lblFARate">F&A
																		Rate:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="F&A Rate" type="text" class="sfInputbox"
																	id="txtFARate" name="FARate" placeholder="F&A Rate"
																	style="text-align: right;" /></td>
															</tr>
														</tbody>
													</table>
												</div>

												<h3>
													<span id="lblSection4">Cost Share Information</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabel">Is
																		Institutional committed cost share included in the
																		proposal?</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Institutional Commitment Cost"
																	class="sfListmenu" id="ddlInstitutionalCommitmentCost"
																	name="institutionalCommitmentCost">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select> <span class="cssClassLabel cssClassInfo"
																	id="lblConfirmCommitment">Complete the OSP Cost
																		Share Form</span></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Is Third
																		Party committed cost share included in the proposal?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Third Party commitment Cost"
																	class="sfListmenu" id="ddlThirdPartyCommitmentCost"
																	name="thirdPartyCommitmentCost">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select></td>
															</tr>
														</tbody>
													</table>
												</div>

												<h3>
													<span id="lblSection5">University Commitments</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabel">Will new or
																		renovated space/facilities be required?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose New Space" class="sfListmenu"
																	id="ddlNewSpaceRequired" name="newSpaceRequired">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Will rental
																		space be required?</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Rental Space Required" class="sfListmenu"
																	id="ddlRentalSpaceRequired" name="rentalSpaceRequired">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Does this
																		project require institutional commitments beyond the
																		end date of the project?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Institutional Commitments Required"
																	class="sfListmenu"
																	id="ddlInstitutionalCommitmentsRequired"
																	name="institutionalCommitmentsRequired">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select> <span class="cssClassLabel cssClassInfo"
																	id="lblCommitmentsRequired">Please refer to the
																		OSP Proposal Data Sheet Instructions for required
																		documents.</span></td>
															</tr>
														</tbody>
													</table>
												</div>

												<h3>
													<span id="lblSection6">Conflict of Interest and Commitment Information</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabel">Is there a
																		financial conflict of interest <b>related to this
																			proposal</b>?
																</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Financial Conflict of Interest"
																	class="sfListmenu" id="ddlFinancialCOI"
																	name="financialCOI">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Has the
																		financial conflict been disclosed?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Financial Conflict been Disclosed?"
																	class="sfListmenu" id="ddlDisclosedFinancialCOI"
																	name="disclosedFinancialCOI">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select> <span class="cssClassLabel cssClassInfo"
																	id="lblDisclosureRequired">Your disclosure must
																		be updated.</span></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Has there
																		been a material change to your annual disclosure form?
																</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Material changed to Annual Disclosure Form?"
																	class="sfListmenu" id="ddlMaterialChanged"
																	name="materialChanged">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select> <span class="cssClassLabel cssClassInfo"
																	id="lblMaterialChanged">Your disclosure must be
																		updated.</span></td>
															</tr>
														</tbody>
													</table>
												</div>

												<h3>
													<span id="lblSection7">Compliance Information</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabel">Does this
																		project involve the use of Human Subjects? </span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Involves Use of Human Subjects?"
																	class="sfListmenu" id="ddlUseHumanSubjects"
																	name="useHumanSubjects">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select> <span class="cssClassLabel cssClassInfo"
																	id="lblUseHumanSubjects">Provide IRB # or
																		indicate pending.</span></td>

																<td id="tdHumanSubjectsOption"><span
																	class="cssClassLabel">Choose Option?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol" id="tdIRBOption"><select
																	title="Choose IRB Option" class="sfListmenu ignore"
																	style="width: 100px;" id="ddlIRBOptions"
																	name="IRBOptions">
																		<option value="">Select Option</option>
																		<option value="1">IRB #</option>
																		<option value="2">Pending</option>
																</select></td>

																<td class="cssClassTableRightCol" id="tdIRBtxt"><span
																	class="cssClassRequired">*</span> <input title="IRB #"
																	type="text" class="sfTextBoxSmall ignore" id="txtIRB"
																	name="IRB" placeholder="IRB #" /></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Does this
																		project involve the use of Vertebrate Animals?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Involves Use of Vertebrate Animals?"
																	class="sfListmenu" id="ddlUseVertebrateAnimals"
																	name="useVertebrateAnimals">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select> <span class="cssClassLabel cssClassInfo"
																	id="lblUseVertebrateAnimals">Provide IACUC # or
																		indicate pending.</span></td>

																<td id="tdVertebrateAnimalsOption"><span
																	class="cssClassLabel">Choose Option?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol" id="tdIACUCOption"><select
																	title="Choose IACUC Option" class="sfListmenu ignore"
																	style="width: 100px;" id="ddlIACUCOptions"
																	name="IACUCOptions">
																		<option value="">Select Option</option>
																		<option value="1">IACUC #</option>
																		<option value="2">Pending</option>
																</select></td>

																<td class="cssClassTableRightCol" id="tdIACUCtxt"><span
																	class="cssClassRequired">*</span> <input
																	title="IACUC #" type="text"
																	class="sfTextBoxSmall ignore" id="txtIACUC"
																	name="IACUC" placeholder="IACUC #" /></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Does this
																		project involve Biosafety concerns?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Involves Biosafety Concerns?"
																	class="sfListmenu" id="ddlInvovleBioSafety"
																	name="invovleBioSafety">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select> <span class="cssClassLabel cssClassInfo"
																	id="lblHasBiosafetyConcerns">Provide IBC # or
																		indicate pending.</span></td>

																<td id="tdBiosafetyOption"><span
																	class="cssClassLabel">Choose Option?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol" id="tdIBCOption"><select
																	title="Choose IBC Option" class="sfListmenu ignore"
																	style="width: 100px;" id="ddlIBCOptions"
																	name="IBCOptions">
																		<option value="">Select Option</option>
																		<option value="1">IBC #</option>
																		<option value="2">Pending</option>
																</select></td>

																<td class="cssClassTableRightCol" id="tdIBCtxt"><span
																	class="cssClassRequired">*</span> <input title="IBC #"
																	type="text" class="sfTextBoxSmall ignore" id="txtIBC"
																	name="IBC" placeholder="IBC #" /></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Does this
																		project have Environmental Health & Safety concerns?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol" colspan="4"><select
																	title="Choose Have Environmental Health & Safety Concerns?"
																	class="sfListmenu" id="ddlEnvironmentalConcerns"
																	name="environmentalConcerns">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select></td>
															</tr>
														</tbody>
													</table>
												</div>

												<h3>
													<span id="lblSection8">Additional Information</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabel">Do you
																		anticipate payment(s) to foreign nationals or on
																		behalf of foreign nationals?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Anticipate Payment to/on behalf Foreign Nationals?"
																	class="sfListmenu" id="ddlAnticipateForeignNationals"
																	name="anticipateForeignNationals">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Do you
																		anticipate course release time?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Anticipate Course Release Time?"
																	class="sfListmenu" id="ddlAnticipateReleaseTime"
																	name="anticipateReleaseTime">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Are the
																		proposed activities related to Center for Advanced
																		Energy Studies?</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Related to Center for Advanced
																					Energy Studies?"
																	class="sfListmenu" id="ddlRelatedToEnergyStudies"
																	name="relatedToEnergyStudies">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select></td>
															</tr>
														</tbody>
													</table>
												</div>

												<h3>
													<span id="lblSection9">Collaboration Information</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabel">Does this
																		project involve non-funded collaborations? </span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Involves Non-funded Collaborations?"
																	class="sfListmenu" id="ddlInvolveNonFundedCollabs"
																	name="involveNonFundedCollabs">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select> <span class="cssClassLabel cssClassInfo"
																	id="lblInvolveNonFundedCollabs">Provide list
																		collaborating institutions/organizations below.</span></td>
															</tr>
															<tr id="trInvolveNonFundedCollabs">
																<td><span class="cssClassLabel">Collaborators:</span>
																	<span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="Collaborators" type="text"
																	class="sfInputbox ignore" id="txtCollaborators"
																	name="collaborators" placeholder="Collaborators" /> <span
																	class="cssClassLabel cssClassInfo">Enter comma
																		separated names.</span></td>
															</tr>
														</tbody>
													</table>
												</div>

												<h3>
													<span id="lblSection10">Proprietary/Confidential Information</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabel">Does this
																		proposal contain any confidential information which is
																		Proprietary that should not be publicly released?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Contains Confidential Information Which
																					Is Proprietary?"
																	class="sfListmenu" style="width: 105px;"
																	id="ddlProprietaryInformation"
																	name="proprietaryInformation">
																		<option value="">Select Option</option>
																		<option value="1">Yes, on pages</option>
																		<option value="2">No</option>
																</select></td>
																<td id="tdPagesWithProprietaryInfo"><span
																	class="cssClassRequired">*</span> <input
																	title="Pages With Proprietary/Confidential Information"
																	type="text" class="sfInputbox ignore"
																	style="width: 210px;" id="txtPagesWithProprietaryInfo"
																	name="pagesWithProprietaryInfo"
																	placeholder="Pages Containing the Information" /> <span
																	class="cssClassLabel cssClassInfo">Enter comma
																		separated page numbers.</span></td>
																</td>
															</tr>
															<tr id="trTypeOfProprietaryInfo">
																<td></td>
																<td class="cssClassTableRightCol"><input
																	title="Patentable" type="checkbox" name="patentable"
																	id="chkPatentable" class="cssClassCheckBox" /> <label
																	class="cssClassLabel" for="chkPatentable">Patentable</label></td>
																<td class="cssClassTableRightCol"><input
																	title="Copyrightable" type="checkbox"
																	name="copyrightable" id="chkCopyrightable"
																	class="cssClassCheckBox" /> <label
																	class="cssClassLabel" for="chkCopyrightable">Copyrightable</label></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Will this
																		project involve intellectual property in which the
																		University may own or have an interest?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol" colspan="2"><select
																	title="Choose Own Intellectual Property?"
																	class="sfListmenu" style="width: 105px;"
																	id="ddlOwnIntellectualProperty"
																	name="ownIntellectualProperty">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select></td>
															</tr>
														</tbody>
													</table>

													<span class="cssClassFooter"> <span
														class="cssClassLabelTitle"><strong>Note:</strong>
															Contact the Office of Technology Transfer for additional
															assistance on proprietary and patentable information at
															208-426-5765.</span>
													</span>
												</div>

												<h3>
													<span id="lblSection11">Certification/Signatures</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabel"><b>Investigators,
																			department chairs, directors, deans</b> certify that 1)
																		the proposed activities are appropriate to the
																		research, instruction and public service mission of
																		the University; 2) if funded all necessary resources
																		as proposed will be provided for the project (i.e.,
																		cost share, personnel, facilities), and project
																		expenditures that exceed the sponsor's award and/or
																		payment upon completion of the project will be charged
																		to the department account that you will identify at
																		the time of award setup.</span></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel"><b>Principal
																			or Co-Principal Investigators</b>b> certify that 1) the
																		information submitted within the application is true,
																		complete and accurate to the best of the
																		Investigator's knowledge; 2) all necessary resources
																		to successfully complete the proposed project have
																		been identified in the proposal; 3) the application is
																		true, complete and accurate to the best of my
																		knowledge; 4) any false, fictitious or fraudulent
																		statements or claims may subject the PI to criminal,
																		civil or administrative penalties; 5) the PI agrees to
																		accept responsibility for the scientific and
																		programmatic conduct and financial oversight of the
																		project and to provide the required progress reports;
																		and 6) the PI shall use all reasonable and best
																		efforts to comply with the terms, conditions and
																		policies of both the sponsor and the University. PIs
																		should refer to <a
																		href="http://web1.boisestate.edu/research/osp/standard-compliance.shtml"
																		target="_blank">http://web1.boisestate.edu/research/osp/standard-compliance.shtml</a>
																		for a list of responsibilities.</span></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel"><b>Department
																			chairs and deans</b> acknowledge that facilities &
																		Administrative costs for projects involving more than
																		one college will be distributed in accordance with
																		University policy 6100 unless otherwise directed in
																		writing with approval from all deans involved.</span></td>
															</tr>
														</tbody>
													</table>

													<table cellspacing="0" cellpadding="0" border="0"
														id="trSignPICOPI">
														<thead>
															<tr>
																<th class="cssClassSignName"><span class="cssClassLabelTitle">Principal/Co-Investigator(s)</span></th>
																<th><span class="cssClassLabelTitle">Signature(s)</span></th>
																<th><span class="cssClassLabelTitle">Date</span></th>
																<th><span class="cssClassLabelTitle">Note</span></th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>

													<table cellspacing="0" cellpadding="0" border="0"
														id="trSignChair">
														<thead>
															<tr>
																<th class="cssClassSignName"><span class="cssClassLabelTitle">Department
																		Chair(s)</span></th>
																<th><span class="cssClassLabelTitle">Signature(s)</span></th>
																<th><span class="cssClassLabelTitle">Date</span></th>
																<th><span class="cssClassLabelTitle">Note</span></th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>

													<table cellspacing="0" cellpadding="0" border="0"
														id="trSignDean">
														<thead>
															<tr>
																<th class="cssClassSignName"><span class="cssClassLabelTitle">Dean(s)</span></th>
																<th><span class="cssClassLabelTitle">Signature(s)</span></th>
																<th><span class="cssClassLabelTitle">Date</span></th>
																<th><span class="cssClassLabelTitle">Note</span></th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>

													<table cellspacing="0" cellpadding="0" border="0"
														id="trSignAdministrator">
														<thead>
															<tr>
																<th class="cssClassSignName"><span class="cssClassLabelTitle">Research
																		Administrator(s)</span></th>
																<th><span class="cssClassLabelTitle">Signature(s)</span></th>
																<th><span class="cssClassLabelTitle">Date</span></th>
																<th><span class="cssClassLabelTitle">Note</span></th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>

													<table cellspacing="0" cellpadding="0" border="0"
														id="trSignDirector">
														<thead>
															<tr>
																<th class="cssClassSignName"><span class="cssClassLabelTitle">Research
																		Director(s)</span></th>
																<th><span class="cssClassLabelTitle">Signature(s)</span></th>
																<th><span class="cssClassLabelTitle">Date</span></th>
																<th><span class="cssClassLabelTitle">Note</span></th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>

													<table cellspacing="0" cellpadding="0" border="0"
														id="trSignBusinessManager">
														<thead>
															<tr>
																<th class="cssClassSignName"><span class="cssClassLabelTitle">Business
																		Manager(s)</span></th>
																<th><span class="cssClassLabelTitle">Signature(s)</span>
																<th><span class="cssClassLabelTitle">Date</span></th>
																<th><span class="cssClassLabelTitle">Note</span></th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>

													<table cellspacing="0" cellpadding="0" border="0"
														id="trSignIRB">
														<thead>
															<tr>
																<th class="cssClassSignName"><span class="cssClassLabelTitle">IRB(s)</span></th>
																<th><span class="cssClassLabelTitle">Signature(s)</span>
																<th><span class="cssClassLabelTitle">Date</span></th>
																<th><span class="cssClassLabelTitle">Note</span></th>
															</tr>
														</thead>
														<tbody>
														</tbody>
													</table>
												</div>

												<h3>
													<span id="lblSection12">OSP Section</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<div class="cssClassHeader">
														<span class="cssClassLabelTitle">Office of Sponsored Programs Administrative Use Only</span>
													</div>
													<table cellspacing="0" cellpadding="0" border="0">
														<tbody>
															<tr>
																<td><span class="cssClassLabel">Flow-Through,
																		List Agency</span> <span class="cssClassRequired">*</span></td>
																<td><input title="Flow-Through, List Agency"
																	type="text" class="sfInputbox" id="txtAgencyList"
																	name="agencyList"
																	placeholder="Flow-Through, List Agency" /> <span
																	class="cssClassLabel cssClassInfo">Enter comma
																		separated names.</span></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Funding
																		Source:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><div
																		class="cssClassCheckBox">
																		<input title="Federal" type="checkbox"
																			class="cssClassCheckBox" id="chkFederal"
																			name="federal" value="Federal" /><label
																			class="cssClassLabel" for="chkFederal">Federal</label><input
																			title="Federal Flow-Through" type="checkbox"
																			class="cssClassCheckBox" id="chkFederalFlowThrough"
																			name="federalFlowThrough"
																			value="Federal Flow-Through" /><label
																			class="cssClassLabel" for="chkFederalFlowThrough">Federal
																			Flow-Through</label><input title="State of Idaho Entity"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkStateOfIdahoEntity" name="stateOfIdahoEntity"
																			value="State of Idaho Entity" /><label
																			class="cssClassLabel" for="chkStateOfIdahoEntity">State
																			of Idaho Entity</label> <input title="Private For Profit"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkPrivateForProfit" name="privateForProfit"
																			value="Private For Profit" /><label
																			class="cssClassLabel" for="chkPrivateForProfit">Private
																			For Profit</label><input title="Non-Profit Organization"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkNonProfitOrganization"
																			name="nonProfitOrganization"
																			value="Non-Profit Organization" /><label
																			class="cssClassLabel" for="chkNonProfitOrganization">Non-Profit
																			Organization</label><input title="Non-Idaho State Entity"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkNonIdahoStateEntity"
																			name="nonIdahoStateEntity"
																			value="Non-Idaho State Entity" /><label
																			class="cssClassLabel" for="chkNonIdahoStateEntity">Non-Idaho
																			State Entity</label> <input title="College/University"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkCollegeUniversity" name="collegeUniversity"
																			value="College/University" /><label
																			class="cssClassLabel" for="chkCollegeUniversity">College/University</label><input
																			title="Local Entity" type="checkbox"
																			class="cssClassCheckBox" id="chkLocalEntity"
																			name="localEntity" value="Local Entity" /><label
																			class="cssClassLabel" for="chkLocalEntity">Local
																			Entity</label><input title="Non-Idaho Local Entity"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkNonIdahoLocalEntity"
																			name="nonIdahoLocalEntity"
																			value="Non-Idaho Local Entity" /><label
																			class="cssClassLabel" for="chkNonIdahoLocalEntity">Non-Idaho
																			Local Entity</label> <input title="Tribal Government"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkTribalGovernment" name="tribalGovernment"
																			value="Tribal Government" /><label
																			class="cssClassLabel" for="chkTribalGovernment">Tribal
																			Government</label><input title="Foreign" type="checkbox"
																			class="cssClassCheckBox" id="chkForeign"
																			name="foreign" value="Foreign" /><label
																			class="cssClassLabel" for="chkForeign">Foreign</label>
																	</div></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">CFDA No.:</span> <span
																	class="cssClassRequired">*</span></td>
																<td><input title="CFDA No." type="text"
																	class="sfInputbox" id="txtCFDANo" name="CFDANo"
																	placeholder="CFDA No." /></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Program No.:</span>
																	<span class="cssClassRequired">*</span></td>
																<td><input title="Program No." type="text"
																	class="sfInputbox" id="txtProgramNo" name="programNo"
																	placeholder="Program No." /></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Program/Solicitation
																		Title:</span> <span class="cssClassRequired">*</span></td>
																<td><input title="Program/Solicitation Title"
																	type="text" class="sfInputbox" id="txtProgramTitle"
																	name="programTitle"
																	placeholder="Program/Solicitation Title" /></td>
															</tr>
															<tr>
																<td>-----------------------------------------------------------</td>
																<td>-----------------------------------------------------------</td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Recovery:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><div
																		class="cssClassCheckBox">
																		<input title="Full Recovery" type="checkbox"
																			class="cssClassCheckBox" id="chkFullRecovery"
																			name="fullRecovery" value="Full Recovery" /><label
																			class="cssClassLabel" for="chkFullRecovery">Full
																			Recovery</label><input
																			title="No Recovery-Normal Sponsor Policy"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkNoRecoveryNormal" name="noRecoveryNormal"
																			value="No Recovery-Normal Sponsor Policy" /><label
																			class="cssClassLabel" for="chkNoRecoveryNormal">No
																			Recovery-Normal Sponsor Policy</label><input
																			title="No Recovery-Institutional Waiver"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkNoRecoveryInstitutional"
																			name="noRecoveryInstitutional"
																			value="No Recovery-Institutional Waiver" /><label
																			class="cssClassLabel"
																			for="chkNoRecoveryInstitutional">No
																			Recovery-Institutional Waiver</label> <input
																			title="Limited Recovery-Normal Sponsor Policy"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkLimitedRecoveryNormal"
																			name="limitedRecoveryNormal"
																			value="Limited Recovery-Normal Sponsor Policy" /><label
																			class="cssClassLabel" for="chkLimitedRecoveryNormal">Limited
																			Recovery-Normal Sponsor Policy</label><input
																			title="Limited Recovery-Institutional Waiver"
																			type="checkbox" class="cssClassCheckBox"
																			id="chkLimitedRecoveryInstitutional"
																			name="limitedRecoveryInstitutional"
																			value="Limited Recovery-Institutional Waiver" /><label
																			class="cssClassLabel"
																			for="chkLimitedRecoveryInstitutional">Limited
																			Recovery-Institutional Waiver</label>
																	</div></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Base:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><div
																		class="cssClassCheckBox">
																		<input title="MTDC" type="checkbox"
																			class="cssClassCheckBox" id="chkMTDC" name="MTDC"
																			value="MTDC" /><label class="cssClassLabel"
																			for="chkMTDC">MTDC</label><input title="TDC"
																			type="checkbox" class="cssClassCheckBox" id="chkTDC"
																			name="TDC" value="TDC" /><label
																			class="cssClassLabel" for="chkTDC">TDC</label><input
																			title="TC" type="checkbox" class="cssClassCheckBox"
																			id="chkTC" name="TC" value="TC" /><label
																			class="cssClassLabel" for="chkTC">TC</label> <input
																			title="Other" type="checkbox"
																			class="cssClassCheckBox" id="chkOther" name="other"
																			value="Other" /><label class="cssClassLabel"
																			for="chkOther">Other</label><input title="N/A"
																			type="checkbox" class="cssClassCheckBox" id="chkNA"
																			name="nA" value="N/A" /><label class="cssClassLabel"
																			for="chkNA">N/A</label>
																	</div></td>
															</tr>
															<tr>
																<td>-----------------------------------------------------------</td>
																<td>-----------------------------------------------------------</td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Is PI salary
																		included in the proposal?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Is PI salary included in the proposal?"
																	class="sfListmenu" id="ddlPISalaryIncluded"
																	name="PISalaryIncluded">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select> <span class="cssClassLabel cssClassInfo"
																	id="lblPISalaryIncluded">Provide a Department ID
																		for 1% minimun.</span></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">PI Salary:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="PI Salary" type="text" class="sfInputbox"
																	id="txtPISalary" name="PISalary"
																	placeholder="PI Salary" style="text-align: right;" /></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">PI Fringe:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="PI Fringe" type="text" class="sfInputbox"
																	id="txtPIFringe" name="PIFringe"
																	placeholder="PI Fringe" style="text-align: right;" /></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Department
																		ID:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="Department ID" type="text" class="sfInputbox"
																	id="txtDepartmentID" name="departmentID"
																	placeholder="Department ID" /></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Institutional
																		Cost Share Documented:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Is Institutional Cost Share Documented?"
																	class="sfListmenu" id="ddlInstitutionalCostDocumented"
																	name="institutionalCostDocumented">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																		<option value="3">N/A</option>
																</select></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Third Party
																		Cost Share Documented:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Is Third Party Cost Share Documented?"
																	class="sfListmenu" id="ddlThirdPartyCostDocumented"
																	name="thirdPartyCostDocumented">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																		<option value="3">N/A</option>
																</select></td>
															</tr>

															<tr>
																<td>-----------------------------------------------------------</td>
																<td>-----------------------------------------------------------</td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Are
																		subrecipients(subcontracts/subawards) anticipated?</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Are subrecipients (subcontracts/subawards) anticipated?"
																	class="sfListmenu" id="ddlSubrecipients"
																	name="subrecipients">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																</select></td>
															</tr>
															<tr id="trSubrecipientsNames">
																<td><span class="cssClassLabel">Names of
																		subrecipients:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><input
																	title="Names of subrecipients" type="text"
																	class="sfInputbox" id="txtNamesSubrecipients"
																	name="namesSubrecipients"
																	placeholder="Names of subrecipients" /></td>
															</tr>

															<tr>
																<td>-----------------------------------------------------------</td>
																<td>-----------------------------------------------------------</td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">PI
																		Eligibility Waiver on File:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose PI Eligibility Waiver on File"
																	class="sfListmenu" id="ddlPIEligibilityWaiver"
																	name="PIEligibilityWaiver">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																		<option value="3">N/A</option>
																		<option value="4">This Proposal Only</option>
																		<option value="5">Blanket</option>
																</select></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Conflict of
																		Interest Forms on File:</span> <span class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Conflict of Interest Forms on File"
																	class="sfListmenu" id="ddlCOIForms" name="COIForms">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																		<option value="3">N/A</option>
																</select></td>
															</tr>
															<tr>
																<td><span class="cssClassLabel">Excluded
																		party list has been checked:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol"><select
																	title="Choose Excluded party list has been checked"
																	class="sfListmenu" id="ddlCheckedExcludedPartyList"
																	name="checkedExcludedPartyList">
																		<option value="">Select Option</option>
																		<option value="1">Yes</option>
																		<option value="2">No</option>
																		<option value="3">N/A</option>
																</select></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel">Proposal
																		Date Received:</span></td>
																<td class="cssClassTableRightCol"><span
																	id="lblProposalDateReceived" class="cssClassLabel"></span>
																	<span id="lblHiddenDateReceived" style="display: none;"></span></td>
															</tr>

															<tr>
																<td><span class="cssClassLabel"
																	id="lblProposalStatus">Proposal Status:</span> <span
																	class="cssClassRequired">*</span></td>
																<td class="cssClassTableRightCol" colspan="3"><select
																	title="Choose Proposal Status" name="proposalStatus"
																	id="ddlProposalStatus">
																</select></td>
															</tr>

															<!-- 															<tr> -->
															<!-- 																<td><span class="cssClassLabel">Proposal -->
															<!-- 																		Notes:</span> <span class="cssClassRequired">*</span></td> -->
															<!-- 																<td class="cssClassTableRightCol"><textarea -->
															<!-- 																		title="Proposal Notes" class="cssClassTextArea" -->
															<!-- 																		cols="26" rows="2" name="proposalNotes" -->
															<!-- 																		id="txtProposalNotes" placeholder="Proposal Notes" -->
															<!-- 																		required></textarea></td> -->
															<!-- 							
															<!-- 															<tr> -->
															<!-- 																<td><span class="cssClassLabel">Research -->
															<!-- 																		Administrator:</span> <span class="cssClassRequired">*</span></td> -->
															<!-- 																<td class="cssClassTableRightCol"><div -->
															<!-- 																		class="cssClassCheckBox"> -->
															<!-- 																		<input title="DF" type="checkbox" -->
															<!-- 																			class="cssClassCheckBox" id="chkDF" name="DF" -->
															<!-- 																			value="DF" /><label class="cssClassLabel" -->
															<!-- 																			for="chkDF">DF</label><input title="LG" -->
															<!-- 																			type="checkbox" class="cssClassCheckBox" id="chkLG" -->
															<!-- 																			name="LG" value="LG" /><label class="cssClassLabel" -->
															<!-- 																			for="chkLG">LG</label><input title="LN" -->
															<!-- 																			type="checkbox" class="cssClassCheckBox" id="chkLN" -->
															<!-- 																			name="LN" value="LN" /><label class="cssClassLabel" -->
															<!-- 																			for="chkLN">LN</label> -->
															<!-- 																	</div></td> -->
															<!-- 															</tr> -->
														</tbody>
													</table>

													<div class="cssClassFooter">
														<span class="cssClassLabelTitle">Send Original to Office
															of Sponsored Programs, MS 1135 or osp@boisestate.edu.
															Please Send email to osp@boisestate.edu to request a
															final copy of the Porposal Data Sheet.</span>
													</div>
												</div>

												<h3>
													<span id="lblSection13">Appendices</span>
												</h3>
												<div class="sfFormwrapper ui-tabs-panel">
													<div id="fileuploader">Upload</div>
												</div>
											</div>

											<div class="sfButtonwrapper">
												<p>
													<button class="sfBtn" id="btnBack" type="button"
														title="Go Back">
														<span class="icon-arrow-slim-w">Back</span>
													</button>
												</p>
												<p>
													<button class="sfBtn" id="btnReset" type="button"
														title="Reset">
														<span class="icon-refresh">Reset</span>
													</button>
												</p>
												<p>
													<button class="sfBtn" id="btnSaveProposal" type="button"
														title="Save Proposal">
														<span class="icon-edit">Save</span>
													</button>
												</p>
												<p>
													<button class="sfBtn" id="btnDeleteProposal" type="button"
														title="Delete Proposal">
														<span class="icon-delete">Delete</span>
													</button>
												</p>
												<p>
													<button class="sfBtn" id="btnSubmitProposal" type="button"
														title="Submit Proposal">
														<span class="icon-send">Submit</span>
													</button>
												</p>
												<p>
													<button class="sfBtn" id="btnApproveProposal" type="button"
														title="Approve Proposal">
														<span class="icon-success">Approve</span>
													</button>
												</p>
												<p>
													<button class="sfBtn" id="btnDisapproveProposal"
														type="button" title="Disapprove Proposal">
														<span class="icon-close">Disapprove</span>
													</button>
												</p>
												<p>
													<button class="sfBtn" id="btnWithdrawProposal"
														type="button" title="Withdraw Proposal">
														<span class="icon-event-log">Withdraw</span>
													</button>
												</p>
												<p>
													<button class="sfBtn" id="btnArchiveProposal" type="button"
														title="Archive Proposal">
														<span class="icon-extract">Archive</span>
													</button>
												</p>
											</div>
										</div>
									</div>
									<!-- End form -->

									<!-- Start Change Log Grid -->
									<div id="divProposalAuditGrid" style="display: none">
										<div class="cssClassCommonBox Curve">
											<div class="cssClassHeader">
												<h1>
													<span id="lblLogsHeading"></span>
												</h1>
												<div class="cssClassHeaderRight">
													<div class="sfButtonwrapper">
														<p>
															<button title="Export to Excel" type="button"
																id="btnLogsExportToExcel" class="sfBtn">
																<span class="icon-excel">Export to Excel</span>
															</button>
														</p>
														<!-- <p>
															<button title="Export to CSV" type="button"
																id="btnExportToCSV" class="sfBtn">
																<span class="icon-excel">Export to CSV</span>
															</button>
														</p> -->

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
																	<td><label class="cssClassLabel"> Action:</label>
																		<input title="Action" type="text" class="sfInputbox"
																		id="txtSearchAction" placeholder="Action" /></td>
																	<td><label class="cssClassLabel"> Audited
																			By:</label> <input title="Audited By" type="text"
																		class="sfInputbox" id="txtSearchAuditedBy"
																		placeholder="Audited By" /></td>
																	<td><label class="cssClassLabel"> Activity
																			On From:</label> <input title="Activity On From" type="text"
																		class="sfTextBoxSmall" id="txtSearchActivityOnFrom"
																		placeholder="From" /></td>
																	<td><label class="cssClassLabel"> Activity
																			On To:</label> <input title="Activity On To" type="text"
																		class="sfTextBoxSmall" id="txtSearchActivityOnTo"
																		placeholder="To" /></td>
																	<td><label class="cssClassLabel"> </label>
																		<button title="Search Audit Log" class="sfBtn"
																			id="btnSearchProposalAuditLog" type="button">
																			<span class="icon-search">Search</span>
																		</button></td>
																</tr>
															</tbody>
														</table>
													</div>
													<div class="loading">
														<img id="ajaxLoader" src="" alt="Loading..."
															title="Loading..." />
													</div>
													<div class="log"></div>
													<table id="gdvProposalsAuditLog" cellspacing="0"
														cellpadding="0" border="0" width="100%"></table>
												</div>
											</div>
										</div>
										<table id="tblLastAuditedInfo" cellspacing="0" cellpadding="0"
											border="0">
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
										<div class="sfButtonwrapper">
											<p>
												<button class="sfBtn" id="btnLogsBack" type="button"
													title="Go Back">
													<span class="icon-arrow-slim-w">Back</span>
												</button>
											</p>
										</div>
									</div>
									<!-- End Change Log Grid-->
								</div>
							</div>
							<!-- END sfMaincontent -->
						</div>
					</div>
					<!-- END Body Content sfContentwrapper -->
				</div>
			</div>
		</div>
	</form>
</body>
</html>