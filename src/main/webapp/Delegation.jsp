<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta content="Delegation" name="DESCRIPTION">
<meta content="Delegation" name="KEYWORDS">
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
<title>Delegation</title>

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

	$(function() {
		//For Sidebar active menu
		$('.acitem').find('a').eq(1).prop("class", "active");
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

<script type="text/javascript" src="js/modules/Delegation.js"></script>

<link type="text/css" rel="stylesheet"
	href="css/Templates/jquery-ui.css" />

<link type="text/css" rel="stylesheet" href="css/MessageBox/style.css" />

<link type="text/css" rel="stylesheet" href="css/GridView/tablesort.css" />
<link type="text/css" rel="stylesheet" href="css/Templates/grid.css" />
<link type="text/css" rel="stylesheet"
	href="css/Templates/topstickybar.css" />
<link type="text/css" rel="stylesheet" href="css/Templates/admin.css" />
</head>
<body>
	<form enctype="multipart/form-data" action="Delegation.jsp"
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
									<div id="divDelegationGrid" style="display: none">
										<div class="cssClassCommonBox Curve">
											<div class="cssClassHeader">
												<h1>
													<span>Manage Your Delegations</span>
												</h1>
												<div class="cssClassHeaderRight">
													<div class="sfButtonwrapper">
														<p>
															<button title="Add New Delegation" type="button"
																id="btnAddNew" class="sfBtn">
																<span class="icon-addnew">Add New Delegation</span>
															</button>
														</p>
														<p>
															<button title="Export to Excel" type="button"
																id="btnExportToExcel" class="sfBtn">
																<span class="icon-excel">Export to Excel</span>
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
																	<td><label class="cssClassLabel">Delegated
																			To:</label> <input title="Delegated To" type="text"
																		class="sfTextBox" id="txtSearchDelegatee"
																		placeholder="Delegated To" /></td>
																	<td style="width: 180px; float: left;"><label
																		class="cssClassLabel">Delegation Starts:</label>
																		<div>
																			<span class="cssClassLabel">From:</span> <input
																				type="text" title="Delegated From"
																				id="txtSearchDelegatedFrom" class="sfTextBoxFix"
																				placeholder="From">
																		</div>
																		<div>
																			<span class="cssClassLabel">To:</span> <input
																				type="text" title="Delegated To"
																				id="txtSearchDelegatedTo" class="sfTextBoxFix"
																				placeholder="To">
																		</div></td>
																	<td><label class="cssClassLabel">Delegated
																			Action:</label> <select title="Choose Delegated Action"
																		id="ddlSearchDelegatedAction" class="sfListmenu"
																		style="width: 80px;">
																			<option value="0">--All--</option>
																			<option value="Approve">Approve</option>
																			<option value="Disapprove">Disapprove</option>
																	</select></td>
																	<td><label class="cssClassLabel">Is
																			Revoked?</label> <select title="Choose Is Revoked?"
																		id="ddlSearchIsRevoked" class="sfListmenu"
																		style="width: 80px;">
																			<option value="0">--All--</option>
																			<option value="True">True</option>
																			<option value="False">False</option>
																	</select></td>
																	<td><label class="cssClassLabel">&nbsp;</label>
																		<button title="Search Delegation" class="sfBtn"
																			id="btnSearchDelegation" type="button">
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
													<table id="gdvDelegations" cellspacing="0" cellpadding="0"
														border="0" width="100%"></table>
												</div>
											</div>
										</div>
									</div>
									<!-- End of Grid -->
									<!-- form -->
									<div id="divDelegationForm" style="display: none;">
										<div class="cssClassCommonBox Curve">
											<div class="cssClassHeader">
												<h1>
													<span id="lblFormHeading">New Delegation Details</span>
												</h1>
												<div>
													<span class="cssClassRequired">*</span> <span
														class="cssClassLabelTitle">indicates required
														fields</span>
												</div>
											</div>
											<div class="sfFormwrapper">
												<table cellspacing="0" cellpadding="0" border="0"
													id="tblDeletationDetails">
													<tbody>
														<tr>
															<td><span class="cssClassLabel"
																id="lblDelegateAction">Delegate Action:</span> <span
																class="cssClassRequired">*</span></td>
															<td><select title="Choose Delegate Action"
																class="sfListmenu" id="ddlDelegateAction"
																name="ddlDelegateAction" required="true">
																	<option value="Approve">Approve</option>
																	<option value="Disapprove">Disapprove</option>
															</select></td>
														</tr>
														<tr>
															<td><span class="cssClassLabel" id="lblDelegateTo">Delegate
																	To:</span> <span class="cssClassRequired">*</span></td>
															<td><select title="Choose Delegate To"
																class="sfListmenu" id="ddlDelegateTo"
																name="ddlDelegateTo" required="true">
															</select></td>
														</tr>
														<tr>
															<td><span id="lblDelegationFrom"
																class="cssClassLabel">Delegation Start From:</span> <span
																class="cssClassRequired">*</span></td>
															<td class="cssClassTableRightCol"><input
																title="Delegation Start From" type="text"
																id="txtDelegationFrom" class="sfInputbox"
																name="delegationFrom"
																placeholder="Delegation Start From" /></td>
															<td><span id="lblDelegationTo" class="cssClassLabel">Delegation
																	Start To:</span> <span class="cssClassRequired">*</span></td>
															<td class="cssClassTableRightCol"><input
																title="Delegation Start To" type="text"
																id="txtDelegationTo" class="sfInputbox"
																name="delegationTo" placeholder="Delegation Start To" /></td>
														</tr>
														<tr>
															<td><span class="cssClassLabel" id="lblReason">Reason:</span>
																<span class="cssClassRequired">*</span></td>
															<td><textarea class="cssClassTextArea"
																	title="Delegation Reason" id="txtDelegationReason"
																	name="delegationReason" cols="26" rows="2"></textarea></td>
														</tr>
														<tr id="trAddedOn">
															<td><span class="cssClassLabel">Added On:</span></td>
															<td class="cssClassTableRightCol"><span
																id="lblDelegationDateCreated"></span></td>
														</tr>
													</tbody>
												</table>
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
													<button class="sfBtn" id="btnSaveDelegation" type="button"
														title="Save Delegation">
														<span class="icon-edit">Save</span>
													</button>
												</p>
												<p>
													<button class="sfBtn" id="btnRevokeDelegation"
														type="button" title="Revoke Delegation">
														<span class="icon-delete">Revoke</span>
													</button>
												</p>
											</div>
										</div>
									</div>
									<!-- End form -->

									<!-- Start Change Log Grid -->
									<div id="divDelegationAuditGrid" style="display: none">
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
																			id="btnSearchDelegationAuditLog" type="button">
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
													<table id="gdvDelegationsAuditLog" cellspacing="0"
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