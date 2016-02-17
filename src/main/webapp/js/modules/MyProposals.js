var myProposal = '';

$(function() {

	if (userProfileId == "null") {
		window.location = 'Login.jsp';
	}

	jQuery.fn.exists = function() {
		return this.length > 0;
	}

	$.validator.setDefaults({
		ignore : ".ignore"
	});

	var gpmsCommonObj = function() {
		var gpmsCommonInfo = {
			UserProfileID : GPMS.utils.GetUserProfileID(),
			UserName : GPMS.utils.GetUserName(),
			UserIsAdmin : GPMS.utils.IsAdmin(),
			UserPositionType : GPMS.utils.GetUserPositionType(),
			UserPositionTitle : GPMS.utils.GetUserPositionTitle(),
			UserDepartment : GPMS.utils.GetUserDepartment(),
			UserCollege : GPMS.utils.GetUserCollege()
		};
		return gpmsCommonInfo;
	};

	$.validator
			.addMethod(
					'greaterthan',
					function(value, element, params) {
						if ($(params).autoNumeric('get') != ''
								&& $(element).autoNumeric('get') != '') {
							return isNaN($(element).autoNumeric('get'))
									&& isNaN($(params).autoNumeric('get'))
									|| parseFloat($(element).autoNumeric('get')) > parseFloat($(
											params).autoNumeric('get'));
						}
						return true;
					}, 'Must be greater than Total Costs From');

	$("#txtSearchTotalCostsFrom").keyup(function() {
		$("#txtSearchTotalCostsTo").val('');
		$("#txtSearchTotalCostsTo").removeClass('error');
		$("#txtSearchTotalCostsTo-error").remove();
	});

	/** * Expand all ** */
	$(".expandAll").click(
			function(event) {
				$('#accordion .ui-accordion-header:not(.ui-state-active)')
						.next().slideDown();

				return false;
			});

	/** * Collapse all ** */
	$(".collapseAll").click(function(event) {
		$('#accordion').accordion({
			collapsible : true,
			active : false
		});

		$('#accordion .ui-accordion-header').next().slideUp();

		return false;
	});

	var validator = $("#form1")
			.validate(
					{
						rules : {
							searchTotalCostsTo : {
								greaterthan : "#txtSearchTotalCostsFrom"
							},
							projectTitle : {
								required : true,
								minlength : 5
							},
							projectType : {
								required : true
							},
							typeOfRequest : {
								required : true
							},
							dueDate : {
								required : true,
								dpDate : true
							},
							locationOfProject : {
								required : true
							},
							projectPeriodFrom : {
								required : true,
								dpDate : true
							},
							projectPeriodTo : {
								required : true,
								dpDate : true
							},
							proposalStatus : {
								required : true
							},
							nameOfGrantingAgency : {
								required : true
							},
							directCosts : {
								required : true
							},
							FACosts : {
								required : true
							},
							totalCosts : {
								required : true
							},
							FARate : {
								required : true
							},
							institutionalCommitmentCost : {
								required : true
							},
							thirdPartyCommitmentCost : {
								required : true
							},
							newSpaceRequired : {
								required : true
							},
							rentalSpaceRequired : {
								required : true
							},
							institutionalCommitmentsRequired : {
								required : true
							},
							financialCOI : {
								required : true
							},
							disclosedFinancialCOI : {
								required : true
							},
							materialChanged : {
								required : true
							},
							useHumanSubjects : {
								required : true
							},
							IRBOptions : {
								required : true
							},
							IRB : {
								required : true
							},
							useVertebrateAnimals : {
								required : true
							},
							IACUCOptions : {
								required : true
							},
							IACUC : {
								required : true
							},
							invovleBioSafety : {
								required : true
							},
							IBCOptions : {
								required : true
							},
							IBC : {
								required : true
							},
							environmentalConcerns : {
								required : true
							},
							anticipateForeignNationals : {
								required : true
							},
							anticipateReleaseTime : {
								required : true
							},
							relatedToEnergyStudies : {
								required : true
							},
							involveNonFundedCollabs : {
								required : true
							},
							collaborators : {
								required : true
							},
							proprietaryInformation : {
								required : true
							},
							pagesWithProprietaryInfo : {
								required : true
							},
							ownIntellectualProperty : {
								required : true
							},
							agencyList : {
								required : true
							},
							CFDANo : {
								required : true,
								number : true
							},
							programNo : {
								required : true,
								number : true
							},
							programTitle : {
								required : true
							},
							PISalaryIncluded : {
								required : true
							},
							PISalary : {
								required : true
							},
							PIFringe : {
								required : true
							},
							departmentID : {
								required : true,
								number : true
							},
							institutionalCostDocumented : {
								required : true
							},
							thirdPartyCostDocumented : {
								required : true
							},
							subrecipients : {
								required : true
							},
							namesSubrecipients : {
								required : true
							},
							PIEligibilityWaiver : {
								required : true
							},
							COIForms : {
								required : true
							},
							checkedExcludedPartyList : {
								required : true
							}
						},
						errorElement : "span",
						messages : {
							searchTotalCostsTo : {
								greaterthan : "Must be greater than From"
							},
							projectTitle : {
								required : "Please enter project title.",
								minlength : "Your project title must be at least 5 characters long"
							},
							projectType : {
								required : "Please select your project type"
							},
							typeOfRequest : {
								required : "Please select project type of request"
							},
							dueDate : {
								required : "Please enter due date",
								dpDate : "Please enter valid date"
							},
							locationOfProject : {
								required : "Please enter location of project"
							},
							projectPeriodFrom : {
								required : "Please enter project period from date",
								dpDate : "Please enter valid date"
							},
							projectPeriodTo : {
								required : "Please enter project period to date",
								dpDate : "Please enter valid date"
							},
							proposalStatus : {
								required : "Please select project status"
							},
							nameOfGrantingAgency : {
								required : "Please enter names of granting agencies"
							},
							directCosts : {
								required : "Please enter direct costs for your project"
							},
							FACosts : {
								required : "Please enter F&A costs for your project"
							},
							totalCosts : {
								required : "Please enter total costs for your project"
							},
							FARate : {
								required : "Please enter F&A rate for your project"
							},
							institutionalCommitmentCost : {
								required : "Please select institutional committed cost share included in the proposal"
							},
							thirdPartyCommitmentCost : {
								required : "Please select third party committed committed cost share included in the proposal"
							},
							newSpaceRequired : {
								required : "Please select new or renovated space/facilities required"
							},
							rentalSpaceRequired : {
								required : "Please select rental space be required"
							},
							institutionalCommitmentsRequired : {
								required : "Please select this project require institutional commitments beyond the end date"
							},
							financialCOI : {
								required : "Please select this project has financial conflict of interest"
							},
							disclosedFinancialCOI : {
								required : "Please select this project has disclosed financial conflict of interest"
							},
							materialChanged : {
								required : "Please select this project has a material change to your annual disclosure form"
							},
							useHumanSubjects : {
								required : "Please select this project involves the use of Human Subjects"
							},
							IRBOptions : {
								required : "Please select IRB # or indicate pending"
							},
							IRB : {
								required : "Please enter IRB #"
							},
							useVertebrateAnimals : {
								required : "Please select this project involves the use of Vertebrate Animals"
							},
							IACUCOptions : {
								required : "Please select IACUC # or indicate pending"
							},
							IACUC : {
								required : "Please enter IACUC #"
							},
							invovleBioSafety : {
								required : "Please select this project involves Biosafety concerns"
							},
							IBCOptions : {
								required : "Please select IBC # or indicate pending"
							},
							IBC : {
								required : "Please enter IBC #"
							},
							environmentalConcerns : {
								required : "Please select this project involves Environmental Health & Safety concerns"
							},
							anticipateForeignNationals : {
								required : "Please select if you anticipate payment(s) to foreign nationals or on behalf of foreign nationals"
							},
							anticipateReleaseTime : {
								required : "Please select if you anticipate course release time"
							},
							relatedToEnergyStudies : {
								required : "Please select your proposed activities are related to Center for Advanced Energy Studies"
							},
							involveNonFundedCollabs : {
								required : "Please select this project involves non-funded collaborations"
							},
							collaborators : {
								required : "Please enter list collaborating institutions/organizations"
							},
							proprietaryInformation : {
								required : "Please select this proposal contains any confidential information which is Proprietary that should not be publicly released"
							},
							pagesWithProprietaryInfo : {
								required : "Please enter pages numbers where Proprietary/Confidential Information are"
							},
							ownIntellectualProperty : {
								required : "Please select this project involves intellectual property in which the University may own or have an interest"
							},
							agencyList : {
								required : "Please enter Flow-Through, List Agency"
							},
							CFDANo : {
								required : "Please enter CFDA No."
							},
							programNo : {
								required : "Please enter Program No."
							},
							programTitle : {
								required : "Please enter Program/Solicitation title"
							},
							PISalaryIncluded : {
								required : "Please select this proposal includes PI salary"
							},
							PISalary : {
								required : "Please enter PI salary"
							},
							PIFringe : {
								required : "Please enter PI Fringe"
							},
							departmentID : {
								required : "Please enter Department ID"
							},
							institutionalCostDocumented : {
								required : "Please select if Institutional Cost Share documented"
							},
							thirdPartyCostDocumented : {
								required : "Please select if Third Party Cost Share documented"
							},
							subrecipients : {
								required : "Please select if subrecipients (subcontracts/subawards) anticipated"
							},
							namesSubrecipients : {
								required : "Please enter names of subrecipients"
							},
							PIEligibilityWaiver : {
								required : "Please select if PI Eligibility Waiver on file"
							},
							COIForms : {
								required : "Please select if Conflict of Interest Forms on file"
							},
							checkedExcludedPartyList : {
								required : "Please select if excluded party list has been checked"
							}
						}
					});

	var rowIndex = 0;
	var projectTitleIsUnique = false;
	var signatureInfo = '';

	var positionsDetails = [];

	myProposal = {
		config : {
			isPostBack : false,
			async : false,
			cache : false,
			type : 'POST',
			contentType : "application/json; charset=utf-8",
			data : '{}',
			dataType : 'json',
			rootURL : GPMS.utils.GetGPMSServicePath(),
			baseURL : GPMS.utils.GetGPMSServicePath() + "proposals/",
			method : "",
			url : "",
			ajaxCallMode : 0,
			proposalId : "0",
			proposalRoles : "",
			buttonType : "",
			arguments : [],
			events : "",
			content : [],
			uploadObj : ""
		},

		ajaxCall : function(config) {
			$
					.ajax({
						type : myProposal.config.type,
						beforeSend : function(request) {
							request.setRequestHeader('GPMS-TOKEN', _aspx_token);
							request.setRequestHeader("UName", GPMS.utils
									.GetUserName());
							request.setRequestHeader("PID", GPMS.utils
									.GetUserProfileID());
							request.setRequestHeader("PType", "v");
							request.setRequestHeader('Escape', '0');
						},
						contentType : myProposal.config.contentType,
						cache : myProposal.config.cache,
						async : myProposal.config.async,
						url : myProposal.config.url,
						data : myProposal.config.data,
						dataType : myProposal.config.dataType,
						success : myProposal.ajaxSuccess,
						error : myProposal.ajaxFailure
					});
		},

		CheckUserPermissionWithPositionType : function(buttonType,
				proposalSection, config) {
			var attributeArray = [];

			attributeArray.push({
				attributeType : "Subject",
				attributeName : "position-type",
				attributeValue : GPMS.utils.GetUserPositionType()
			});

			attributeArray.push({
				attributeType : "Resource",
				attributeName : "proposal-section",
				attributeValue : proposalSection
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal-action",
				attributeValue : buttonType
			});

			this.config.url = this.config.baseURL
					+ "CheckPermissionForAProposal";
			this.config.data = JSON2.stringify({
				policyInfo : attributeArray,
				gpmsCommonObj : gpmsCommonObj()
			});

			this.config.buttonType = buttonType;
			this.ajaxCall(this.config);
		},

		CheckUserPermissionWithPositionTitle : function(buttonType,
				proposal_id, proposalSection, config) {
			var attributeArray = [];

			attributeArray.push({
				attributeType : "Subject",
				attributeName : "position-title",
				attributeValue : GPMS.utils.GetUserPositionTitle()
			});

			attributeArray.push({
				attributeType : "Resource",
				attributeName : "proposal-section",
				attributeValue : proposalSection
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal-action",
				attributeValue : buttonType
			});

			this.config.url = this.config.baseURL
					+ "CheckPermissionForAProposal";
			this.config.data = JSON2.stringify({
				policyInfo : attributeArray,
				gpmsCommonObj : gpmsCommonObj(),
				proposalId : proposal_id
			});

			this.config.buttonType = buttonType;
			this.ajaxCall(this.config);
		},

		CheckUserPermissionWithProposalRole : function(buttonType,
				proposal_roles, proposal_id, proposalSection, config) {
			var attributeArray = [];

			attributeArray.push({
				attributeType : "Subject",
				attributeName : "proposal-role",
				attributeValue : proposal_roles
			});

			attributeArray.push({
				attributeType : "Resource",
				attributeName : "proposal-section",
				attributeValue : proposalSection
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal-action",
				attributeValue : buttonType
			});

			this.config.url = this.config.baseURL
					+ "CheckPermissionForAProposal";
			this.config.data = JSON2.stringify({
				policyInfo : attributeArray,
				gpmsCommonObj : gpmsCommonObj(),
				proposalId : proposal_id
			});

			this.config.buttonType = buttonType;
			this.ajaxCall(this.config);
		},

		SearchProposals : function() {
			var projectTitle = $.trim($("#txtSearchProjectTitle").val());
			var usernameBy = $.trim($("#txtSearchUserName").val());
			var submittedOnFrom = $.trim($("#txtSearchSubmittedOnFrom").val());
			var submittedOnTo = $.trim($("#txtSearchSubmittedOnTo").val());
			var totalCostsFrom = $.trim($("#txtSearchTotalCostsFrom")
					.autoNumeric('get'));
			var totalCostsTo = $.trim($("#txtSearchTotalCostsTo").autoNumeric(
					'get'));

			var proposalStatus = $.trim($('#ddlSearchProposalStatus').val()) == "" ? null
					: $.trim($('#ddlSearchProposalStatus').val()) == "0" ? null
							: $.trim($('#ddlSearchProposalStatus').val());

			var userRole = $.trim($('#ddlSearchUserRole').val()) == "" ? null
					: $.trim($('#ddlSearchUserRole').val()) == "0" ? null : $
							.trim($('#ddlSearchUserRole').val());

			if (projectTitle.length < 1) {
				projectTitle = null;
			}
			if (usernameBy.length < 1) {
				usernameBy = null;
			}
			if (totalCostsFrom.length < 1) {
				totalCostsFrom = null;
			}
			if (totalCostsTo.length < 1) {
				totalCostsTo = null;
			}
			if (submittedOnFrom.length < 1) {
				submittedOnFrom = null;
			}
			if (submittedOnTo.length < 1) {
				submittedOnTo = null;
			}

			myProposal.BindProposalGrid(projectTitle, usernameBy,
					submittedOnFrom, submittedOnTo, totalCostsFrom,
					totalCostsTo, proposalStatus, userRole);
		},

		BindProposalGrid : function(projectTitle, usernameBy, submittedOnFrom,
				submittedOnTo, totalCostsFrom, totalCostsTo, proposalStatus,
				userRole) {
			this.config.url = this.config.baseURL;
			this.config.method = "GetUserProposalsList";
			var offset_ = 1;
			var current_ = 1;
			var perpage = ($("#gdvProposals_pagesize").length > 0) ? $(
					"#gdvProposals_pagesize :selected").text() : 10;

			var proposalBindObj = {
				ProjectTitle : projectTitle,
				UsernameBy : usernameBy,
				SubmittedOnFrom : submittedOnFrom,
				SubmittedOnTo : submittedOnTo,
				TotalCostsFrom : totalCostsFrom,
				TotalCostsTo : totalCostsTo,
				ProposalStatus : proposalStatus,
				UserRole : userRole
			};

			this.config.data = {
				proposalBindObj : proposalBindObj,
				gpmsCommonObj : gpmsCommonObj()
			};
			var data = this.config.data;

			$("#gdvProposals")
					.sagegrid(
							{
								url : this.config.url,
								functionMethod : this.config.method,
								colModel : [ {
									display : 'Proposal ID',
									cssclass : 'cssClassHeadCheckBox',
									coltype : 'checkbox',
									align : 'center',
									checkFor : '25',
									elemClass : 'attrChkbox',
									elemDefault : false,
									controlclass : 'attribHeaderChkbox',
									hide : true
								}, {
									display : 'Proposal No',
									name : 'proposal_no',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									hide : true
								}, {
									display : 'Project Title',
									name : 'project_title',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left'
								}, {
									display : 'Project Type',
									name : 'project_type',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									hide : true
								}, {
									display : 'Type of Request',
									name : 'type_of_request',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'array',
									hide : true
								}, {
									display : 'Project Location',
									name : 'project_location',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									hide : true
								}, {
									display : 'Granting Agencies',
									name : 'granting_agencies',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'array'
								}, {
									display : 'Direct Costs',
									name : 'directCosts',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'currency',
									hide : true
								}, {
									display : 'FA Costs',
									name : 'FA_costs',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'currency',
									hide : true
								}, {
									display : 'Total Costs',
									name : 'total_costs',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'currency'
								}, {
									display : 'FA Rate',
									name : 'FA_rate',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'percent',
									hide : true
								}, {
									display : 'Date Created',
									name : 'date_created',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'date',
									format : 'yyyy/MM/dd hh:mm:ss a',
									hide : true
								}, {
									display : 'Date Submitted',
									name : 'date_submitted',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'date',
									format : 'yyyy/MM/dd hh:mm:ss a'
								}, {
									display : 'Due Date',
									name : 'due_date',
									cssclass : 'cssClassHeadDate',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'date',
									format : 'yyyy/MM/dd hh:mm:ss a'
								}, {
									display : 'Project Period From',
									name : 'project_period_from',
									cssclass : 'cssClassHeadDate',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'date',
									format : 'yyyy/MM/dd hh:mm:ss a',
									hide : true
								}, {
									display : 'Project Period To',
									name : 'project_period_to',
									cssclass : 'cssClassHeadDate',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'date',
									format : 'yyyy/MM/dd hh:mm:ss a',
									hide : true
								}, {
									display : 'Status',
									name : 'proposal_status',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'array'
								}, {
									display : 'Last Audited',
									name : 'last_audited',
									cssclass : 'cssClassHeadDate',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'date',
									format : 'yyyy/MM/dd hh:mm:ss a',
									hide : true
								}, {
									display : 'Last Audited By',
									name : 'last_audited_by',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									hide : true
								}, {
									display : 'Last Audited Action',
									name : 'last_audited_action',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									hide : true
								}, {
									display : 'PI User',
									name : 'pi_user',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									hide : true
								}, {
									display : 'Co-PI Users',
									name : 'co_pi_users',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'array',
									hide : true
								}, {
									display : 'Senior Personnel Users',
									name : 'senior_personnel_users',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'array',
									hide : true
								}, {
									display : 'All Involved Users',
									name : 'all_users',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'array',
									hide : true
								}, {
									display : 'Current User Roles',
									name : 'proposal_roles',
									cssclass : '',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'array'
								}, {
									display : 'Is Deleted?',
									name : 'is_deleted',
									cssclass : 'cssClassHeadBoolean',
									controlclass : '',
									coltype : 'label',
									align : 'left',
									type : 'boolean',
									format : 'Yes/No'
								}, {
									display : 'Actions',
									name : 'action',
									cssclass : 'cssClassAction',
									coltype : 'label',
									align : 'center'
								} ],

								buttons : [
										{
											display : 'Edit',
											name : 'edit',
											enable : true,
											_event : 'click',
											trigger : '1',
											callMethod : 'myProposal.EditProposal',
											arguments : '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25'
										},
										{
											display : 'Delete',
											name : 'delete',
											enable : true,
											_event : 'click',
											trigger : '2',
											callMethod : 'myProposal.DeleteProposal',
											arguments : '24,25'
										},
										{
											display : 'View Change Logs',
											name : 'changelog',
											enable : true,
											_event : 'click',
											trigger : '3',
											callMethod : 'myProposal.ViewChangeLogs',
											arguments : '2,17,18,19,24'
										} ],
								rp : perpage,
								nomsg : 'No Records Found!',
								param : data,
								current : current_,
								pnew : offset_,
								sortcol : {
									0 : {
										sorter : false
									},
									26 : {
										sorter : false
									}
								}
							});
		},

		ButtonHideShow : function(currentProposalRoles, proposalStatus,
				proposalId) {
			$("#btnReset").hide();
			$("#btnSaveProposal").hide();
			$("#btnUpdateProposal").hide();
			$("#btnDeleteProposal").hide();
			$("#btnSubmitProposal").hide();
			$("#btnApproveProposal").hide();
			$("#btnDisapproveProposal").hide();
			$("#btnWithdrawProposal").hide();
			$("#btnArchiveProposal").hide();

			var currentPositionTitle = GPMS.utils.GetUserPositionTitle();

			currentProposalRoles = currentProposalRoles.split(', ');

			if (proposalStatus != ""
					&& (($.inArray("PI", currentProposalRoles) !== -1 && (proposalStatus == "Not Submitted by PI"
							|| proposalStatus == "Returned by Chair"
							|| proposalStatus == "Disapproved by Business Manager"
							|| proposalStatus == "Disapproved by IRB"
							|| proposalStatus == "Returned by Dean"
							|| proposalStatus == "Disapproved by Research Administrator" || proposalStatus == "Disapproved by University Research Director")) || (currentPositionTitle == "Research Administrator" && proposalStatus == "Ready for submission"))) {
				$("#btnSubmitProposal").show();
			} else {
				$("#btnSubmitProposal").hide();
			}

			if (proposalStatus != ""
					&& ($.inArray("PI", currentProposalRoles) !== -1
							|| $.inArray("CO-PI", currentProposalRoles) !== -1 || $
							.inArray("Senior", currentProposalRoles) !== -1)
					&& (proposalStatus == "Not Submitted by PI"
							|| proposalStatus == "Waiting for Chair's Approval"
							|| proposalStatus == "Returned by Chair"
							|| proposalStatus == "Disapproved by Business Manager"
							|| proposalStatus == "Disapproved by IRB"
							|| proposalStatus == "Returned by Dean"
							|| proposalStatus == "Disapproved by Research Administrator" || proposalStatus == "Disapproved by University Research Director")) {
				$("#btnUpdateProposal").show();
			} else {
				$("#btnUpdateProposal").hide();
			}

			$
					.each(
							currentProposalRoles,
							function(index, value) {
								if (proposalStatus != ""
										&& (($.inArray("PI",
												currentProposalRoles) !== -1 && (proposalStatus == "Not Submitted by PI"
												|| proposalStatus == "Waiting for Chair's Approval"
												|| proposalStatus == "Returned by Chair"
												|| proposalStatus == "Disapproved by Business Manager"
												|| proposalStatus == "Disapproved by IRB"
												|| proposalStatus == "Returned by Dean"
												|| proposalStatus == "Disapproved by Research Administrator" || proposalStatus == "Disapproved by University Research Director")) || (currentPositionTitle == "University Research Director" && proposalStatus == "Submitted to Research Director"))) {
									$("#btnDeleteProposal").show();
								} else {
									$("#btnDeleteProposal").hide();
								}
							});

			if (proposalStatus != ""
					&& ((currentPositionTitle == "Department Chair" && proposalStatus == "Waiting for Chair's Approval")
							|| (currentPositionTitle == "Business Manager" && (proposalStatus == "Ready for Review" || proposalStatus == "Approved by IRB"))
							|| (currentPositionTitle == "IRB" && (proposalStatus == "Ready for Review" || proposalStatus == "Reviewed by Business Manager"))
							|| (currentPositionTitle == "Dean" && (proposalStatus == "Ready for Review"
									|| proposalStatus == "Reviewed by Business Manager" || proposalStatus == "Approved by IRB"))
							|| (currentPositionTitle == "Research Administrator" && proposalStatus == "Approved by Dean") || (currentPositionTitle == "University Research Director" && proposalStatus == "Submitted to Research Director"))) {
				$("#btnApproveProposal").show();
				$("#btnDisapproveProposal").show();
			} else {
				$("#btnApproveProposal").hide();
				$("#btnDisapproveProposal").hide();
			}

			if (proposalStatus != ""
					&& (currentPositionTitle == "Research Administrator" && proposalStatus == "Approved by Dean")) {
				$("#btnWithdrawProposal").show();
			} else {
				$("#btnWithdrawProposal").hide();
			}

			if (proposalStatus != ""
					&& (currentPositionTitle == "University Research Director" && proposalStatus == "Submitted by University Research Administrator")) {
				$("#btnArchiveProposal").show();
			} else {
				$("#btnArchiveProposal").hide();
			}
		},

		EditProposal : function(tblID, argus) {
			switch (tblID) {
			case "gdvProposals":
				// $('#accordion-expand-holder').show();
				$("#accordion").accordion("option", "active", false);

				$('#lblFormHeading').html(
						'Edit Proposal Details for: ' + argus[2]);

				$("#lblProposalDateReceived").text(argus[13]);

				myProposal.ClearForm();

				myProposal.config.proposalRoles = $.trim(argus[24]);
				myProposal.config.proposalId = argus[0];

				$("#txtNameOfGrantingAgency").val(argus[6]);

				$("#trSignChair").show();
				$("#trSignBusinessManager").show();
				$("#trSignDean").show();
				$("#trSignAdministrator").show();
				$("#trSignDirector").show();

				// OSP Section
				$('#ui-id-25').show();
				if (GPMS.utils.GetUserPositionTitle() == "Research Administrator"
						|| GPMS.utils.GetUserPositionTitle() == "University Research Director") {
					$('#ui-id-26').find('input, select, textarea').each(
							function() {
								// $(this).addClass("ignore");
								$(this).prop('disabled', false);
							});
				} else {
					$('#ui-id-26').find('input, select, textarea').each(
							function() {
								// $(this).addClass("ignore");
								$(this).prop('disabled', true);
							});
				}

				$('#ddlProposalStatus option').length = 0;
				$('#ddlProposalStatus')
						.append(new Option(argus[16], argus[16])).prop(
								'disabled', true);

				myProposal.BindUserPositionDetailsForAProposal(argus[23]);

				myProposal.BindProposalDetailsByProposalId(argus[0]);

				myProposal.ButtonHideShow(myProposal.config.proposalRoles,
						argus[16], argus[0]);

				// Certification/ Signatures Info
				myProposal.BindAllSignatureForAProposal(argus[0]);

				// Delegation Info
				break;
			default:
				break;
			}
		},

		InitializeUploader : function(appendices) {
			// Uploader for Appendix
			var globalSettings = {
				url : GPMS.utils.GetGPMSServicePath() + "files/multiupload",
				multiple : true,
				dragDrop : true,
				fileName : "myfile",
				allowDuplicates : false,
				duplicateStrict : true,
				nestedForms : false,
				fileCounterStyle : ") ",
				// autoSubmit : true,
				// sequential : true,
				// sequentialCount : 1,
				// autoSubmit : false,
				// formData : {
				// "name" : "Milson",
				// "age" : 29
				// },uploadObj
				// acceptFiles : "image/*",
				maxFileCount : 5,
				// maxFileSize : 5*100 * 1024, //5MB
				returnType : "json",
				showDelete : true,
				confirmDelete : true,
				statusBarWidth : 600,
				dragdropWidth : 600,
				uploadQueueOrder : 'top',
				deleteCallback : function(data, pd) {
					pd.statusbar.hide(); // You choice.
				}
			}

			var settings = {
				showDownload : true,
				// deleteCallback : function(data, pd) {
				// $.post(GPMS.utils.GetGPMSServicePath() + "files/delete", {
				// op : "delete",
				// name : data
				// }, function(resp, textStatus, jqXHR) {
				// // Show Message
				// alert("File Deleted");
				// });
				// pd.statusbar.hide(); // You choice.
				// },
				downloadCallback : function(filename, pd) {
					// location.href =
					// GPMS.utils.GetGPMSServicePath()
					// + "download.php?fileName="
					// + filename;
					window.location.href = GPMS.utils.GetGPMSServicePath()
							+ 'files/download?fileName=' + filename;
				}
			}

			myProposal.config.uploadObj = $("#fileuploader").uploadFile(
					globalSettings);

			if (appendices != "") {
				myProposal.config.uploadObj.update(settings);

				$.each(appendices, function(index, value) {
					myProposal.config.uploadObj.createProgress(value.filename,
							value.filepath, value.filesize, value.title);
				});

				myProposal.config.uploadObj.update({
					showDownload : false
				});
			}
		},

		BindUserPositionDetailsForAProposal : function(users) {
			if (users != null) {
				this.config.url = this.config.rootURL + "users/"
						+ "GetUserPositionDetailsForAProposal";
				this.config.data = JSON2.stringify({
					userIds : users
				});
				this.config.ajaxCallMode = 6;
				this.ajaxCall(this.config);
			}
			return false;
		},

		BindProposalDetailsByProposalId : function(proposalId) {
			this.config.url = this.config.baseURL
					+ "GetProposalDetailsByProposalId";
			this.config.data = JSON2.stringify({
				proposalId : proposalId
			});
			this.config.ajaxCallMode = 4;
			this.ajaxCall(this.config);
			return false;
		},

		FillForm : function(response) {
			// TODO
			if ($.inArray("NOTSUBMITTEDBYPI", response.proposalStatus) !== -1) {
				$("#btnSubmitProposal").show();
			} else {
				$("#btnSubmitProposal").hide();
			}
			// Investigator Information
			myProposal.BindInvestigatorInfo(response.investigatorInfo);

			// Project Extra Information
			$("#lblProposalNo").text(response.proposalNo);
			$("#lblHiddenDateReceived").text(response.dateReceived);

			// Project Information
			$("#txtProjectTitle").val(response.projectInfo.projectTitle).prop(
					"disabled", "disabled");

			if (response.projectInfo.projectType.isResearchBasic) {
				$("#ddlProjectType").val(1);
			} else if (response.projectInfo.projectType.isResearchApplied) {
				$("#ddlProjectType").val(2);
			} else if (response.projectInfo.projectType.isResearchDevelopment) {
				$("#ddlProjectType").val(3);
			} else if (response.projectInfo.projectType.isInstruction) {
				$("#ddlProjectType").val(4);
			} else if (response.projectInfo.projectType.isOtherSponsoredActivity) {
				$("#ddlProjectType").val(5);
			} else {
				$("#ddlProjectType").prop("selectedIndex", 0);
			}

			if (response.projectInfo.typeOfRequest.isPreProposal) {
				$("#ddlTypeOfRequest").val(1);
			} else if (response.projectInfo.typeOfRequest.isNewProposal) {
				$("#ddlTypeOfRequest").val(2);
			} else if (response.projectInfo.typeOfRequest.isContinuation) {
				$("#ddlTypeOfRequest").val(3);
			} else if (response.projectInfo.typeOfRequest.isSupplement) {
				$("#ddlTypeOfRequest").val(4);
			} else {
				$("#ddlTypeOfRequest").prop("selectedIndex", 0);
			}

			$("#txtDueDate").val(response.projectInfo.dueDate);

			if (response.projectInfo.projectLocation.offCampus) {
				$("#ddlLocationOfProject").val(1);
			} else if (response.projectInfo.projectLocation.onCampus) {
				$("#ddlLocationOfProject").val(2);
			} else {
				$("#ddlLocationOfProject").prop("selectedIndex", 0);
			}

			$("#txtProjectPeriodFrom").val(
					response.projectInfo.projectPeriod.from);
			$("#txtProjectPeriodTo").val(response.projectInfo.projectPeriod.to);

			// Sponsor And Budget Information
			// for (var int = 0; int <
			// response.sponsorAndBudgetInfo.grantingAgency.length; int++) {
			// var array_element = array[int];
			//				
			// }
			// $("#txtNameOfGrantingAgency").val(
			// response.sponsorAndBudgetInfo.grantingAgency);
			$("#txtDirectCosts").autoNumeric('set',
					response.sponsorAndBudgetInfo.directCosts);
			$("#txtFACosts").autoNumeric('set',
					response.sponsorAndBudgetInfo.FACosts);
			$("#txtTotalCosts").autoNumeric('set',
					response.sponsorAndBudgetInfo.totalCosts);
			$("#txtFARate").autoNumeric('set',
					response.sponsorAndBudgetInfo.FARate);

			// Cost Share Information
			if (response.costShareInfo.institutionalCommitted) {
				$("#ddlInstitutionalCommitmentCost").val(1);
				$("#lblConfirmCommitment").show();
			} else if (!response.costShareInfo.institutionalCommitted) {
				$("#ddlInstitutionalCommitmentCost").val(2);
				$("#lblConfirmCommitment").hide();
			} else {
				$("#ddlInstitutionalCommitmentCost").prop("selectedIndex", 0);
				$("#lblConfirmCommitment").hide();
			}

			if (response.costShareInfo.thirdPartyCommitted) {
				$("#ddlThirdPartyCommitmentCost").val(1);
			} else if (!response.costShareInfo.thirdPartyCommitted) {
				$("#ddlThirdPartyCommitmentCost").val(2);
			} else {
				$("#ddlThirdPartyCommitmentCost").prop("selectedIndex", 0);
			}

			// University Commitments
			if (response.universityCommitments.newRenovatedFacilitiesRequired) {
				$("#ddlNewSpaceRequired").val(1);
			} else if (!response.universityCommitments.newRenovatedFacilitiesRequired) {
				$("#ddlNewSpaceRequired").val(2);
			} else {
				$("#ddlNewSpaceRequired").prop("selectedIndex", 0);
			}

			if (response.universityCommitments.rentalSpaceRequired) {
				$("#ddlRentalSpaceRequired").val(1);
			} else if (!response.universityCommitments.rentalSpaceRequired) {
				$("#ddlRentalSpaceRequired").val(2);
			} else {
				$("#ddlRentalSpaceRequired").prop("selectedIndex", 0);
			}

			if (response.universityCommitments.institutionalCommitmentRequired) {
				$("#ddlInstitutionalCommitmentsRequired").val(1);
				$("#lblCommitmentsRequired").show();
			} else if (!response.universityCommitments.institutionalCommitmentRequired) {
				$("#ddlInstitutionalCommitmentsRequired").val(2);
				$("#lblCommitmentsRequired").hide();
			} else {
				$("#ddlInstitutionalCommitmentsRequired").prop("selectedIndex",
						0);
				$("#lblCommitmentsRequired").hide();
			}

			// Conflict of Interest And Commitment Information
			if (response.conflicOfInterest.financialCOI) {
				$("#ddlFinancialCOI").val(1);
			} else if (!response.conflicOfInterest.financialCOI) {
				$("#ddlFinancialCOI").val(2);
			} else {
				$("#ddlFinancialCOI").prop("selectedIndex", 0);
			}

			if (response.conflicOfInterest.conflictDisclosed) {
				$("#ddlDisclosedFinancialCOI").val(1);
				$("#lblDisclosureRequired").show();
			} else if (!response.conflicOfInterest.conflictDisclosed) {
				$("#ddlDisclosedFinancialCOI").val(2);
				$("#lblDisclosureRequired").hide();
			} else {
				$("#ddlDisclosedFinancialCOI").prop("selectedIndex", 0);
				$("#lblDisclosureRequired").hide();
			}

			if (response.conflicOfInterest.disclosureFormChange) {
				$("#ddlMaterialChanged").val(1);
				$("#lblMaterialChanged").show();
			} else if (!response.conflicOfInterest.disclosureFormChange) {
				$("#ddlMaterialChanged").val(2);
				$("#lblMaterialChanged").hide();
			} else {
				$("#ddlMaterialChanged").prop("selectedIndex", 0);
				$("#lblMaterialChanged").hide();
			}

			// Compliance Information
			if (response.complianceInfo.involveUseOfHumanSubjects) {
				$("#ddlUseHumanSubjects").val(1);
				$("#lblUseHumanSubjects").show();
				$("#tdHumanSubjectsOption").show();
				$("#tdIRBOption").show();
				if (response.complianceInfo.IRBPending) {
					$("#ddlIRBOptions").val(2);
					$("#tdIRBtxt").hide();
				} else if (!response.complianceInfo.IRBPending
						&& response.complianceInfo.IRB != "") {
					$("#ddlIRBOptions").val(1);
					$("#txtIRB").val(response.complianceInfo.IRB);
					$("#tdIRBtxt").show();
				}
			} else if (!response.complianceInfo.involveUseOfHumanSubjects) {
				$("#ddlUseHumanSubjects").val(2);
				$("#lblUseHumanSubjects").hide();
				$("#tdHumanSubjectsOption").hide();
				$("#tdIRBOption").hide();
				$("#tdIRBtxt").hide();
			} else {
				$("#ddlUseHumanSubjects").prop("selectedIndex", 0);
				$("#lblUseHumanSubjects").hide();
				$("#tdHumanSubjectsOption").hide();
				$("#tdIRBOption").hide();
				$("#tdIRBtxt").hide();
			}

			if (response.complianceInfo.involveUseOfVertebrateAnimals) {
				$("#ddlUseVertebrateAnimals").val(1);
				$("#lblUseVertebrateAnimals").show();
				$("#tdVertebrateAnimalsOption").show();
				$("#tdIACUCOption").show();
				if (response.complianceInfo.IACUCPending) {
					$("#ddlIACUCOptions").val(2);
					$("#tdIACUCtxt").hide();
				} else if (!response.complianceInfo.IACUCPending
						&& response.complianceInfo.IACUC != "") {
					$("#ddlIACUCOptions").val(1);
					$("#txtIACUC").val(response.complianceInfo.IACUC);
					$("#tdIACUCtxt").show();
				}
			} else if (!response.complianceInfo.involveUseOfVertebrateAnimals) {
				$("#ddlUseVertebrateAnimals").val(2);
				$("#lblUseVertebrateAnimals").hide();
				$("#tdVertebrateAnimalsOption").hide();
				$("#tdIACUCOption").hide();
				$("#tdIACUCtxt").hide();
			} else {
				$("#ddlUseVertebrateAnimals").prop("selectedIndex", 0);
				$("#lblUseVertebrateAnimals").hide();
				$("#tdVertebrateAnimalsOption").hide();
				$("#tdIACUCOption").hide();
				$("#tdIACUCtxt").hide();
			}

			if (response.complianceInfo.involveBiosafetyConcerns) {
				$("#ddlInvovleBioSafety").val(1);
				$("#lblHasBiosafetyConcerns").show();
				$("#tdBiosafetyOption").show();
				$("#tdIBCOption").show();
				if (response.complianceInfo.IBCPending) {
					$("#ddlIBCOptions").val(2);
					$("#tdIBCtxt").hide();
				} else if (!response.complianceInfo.IBCPending
						&& response.complianceInfo.IBC != "") {
					$("#ddlIBCOptions").val(1);
					$("#txtIBC").val(response.complianceInfo.IBC);
					$("#tdIBCtxt").show();
				}
			} else if (!response.complianceInfo.involveBiosafetyConcerns) {
				$("#ddlInvovleBioSafety").val(2);
				$("#lblHasBiosafetyConcerns").hide();
				$("#tdBiosafetyOption").hide();
				$("#tdIBCOption").hide();
				$("#tdIBCtxt").hide();
			} else {
				$("#ddlInvovleBioSafety").prop("selectedIndex", 0);
				$("#lblHasBiosafetyConcerns").hide();
				$("#tdBiosafetyOption").hide();
				$("#tdIBCOption").hide();
				$("#tdIBCtxt").hide();
			}

			if (response.complianceInfo.involveEnvironmentalHealthAndSafetyConcerns) {
				$("#ddlEnvironmentalConcerns").val(1);
			} else if (!response.complianceInfo.involveEnvironmentalHealthAndSafetyConcerns) {
				$("#ddlEnvironmentalConcerns").val(2);
			} else {
				$("#ddlEnvironmentalConcerns").prop("selectedIndex", 0);
			}

			// Additional Information
			if (response.additionalInfo.anticipatesForeignNationalsPayment) {
				$("#ddlAnticipateForeignNationals").val(1);
			} else if (!response.additionalInfo.anticipatesForeignNationalsPayment) {
				$("#ddlAnticipateForeignNationals").val(2);
			} else {
				$("#ddlAnticipateForeignNationals").prop("selectedIndex", 0);
			}

			if (response.additionalInfo.anticipatesCourseReleaseTime) {
				$("#ddlAnticipateReleaseTime").val(1);
			} else if (!response.additionalInfo.anticipatesCourseReleaseTime) {
				$("#ddlAnticipateReleaseTime").val(2);
			} else {
				$("#ddlAnticipateReleaseTime").prop("selectedIndex", 0);
			}

			if (response.additionalInfo.relatedToCenterForAdvancedEnergyStudies) {
				$("#ddlRelatedToEnergyStudies").val(1);
			} else if (!response.additionalInfo.relatedToCenterForAdvancedEnergyStudies) {
				$("#ddlRelatedToEnergyStudies").val(2);
			} else {
				$("#ddlRelatedToEnergyStudies").prop("selectedIndex", 0);
			}

			// Collaboration Information
			if (response.collaborationInfo.involveNonFundedCollab) {
				$("#ddlInvolveNonFundedCollabs").val(1);
				$("#lblInvolveNonFundedCollabs").show();
				$("#trInvolveNonFundedCollabs").show();
				$("#txtCollaborators").val(
						response.collaborationInfo.involvedCollaborators);

			} else if (!response.collaborationInfo.involveNonFundedCollab) {
				$("#ddlInvolveNonFundedCollabs").val(2);
				$("#lblInvolveNonFundedCollabs").hide();
				$("#trInvolveNonFundedCollabs").hide();
				$("#txtCollaborators").val('');
			} else {
				$("#ddlInvolveNonFundedCollabs").prop("selectedIndex", 0);
				$("#lblInvolveNonFundedCollabs").hide();
				$("#trInvolveNonFundedCollabs").hide();
				$("#txtCollaborators").val('');
			}

			// Proprietary/ Confidential Information
			if (response.confidentialInfo.containConfidentialInformation) {
				$("#ddlProprietaryInformation").val(1);
				$("#txtPagesWithProprietaryInfo").val(
						response.confidentialInfo.onPages);
				$("#tdPagesWithProprietaryInfo").show();
				$("#trTypeOfProprietaryInfo").show();
				$("#chkPatentable").prop("checked",
						response.confidentialInfo.patentable);
				$("#chkCopyrightable").prop("checked",
						response.confidentialInfo.copyrightable);
			} else if (!response.confidentialInfo.containConfidentialInformation) {
				$("#ddlProprietaryInformation").val(2);
				$("#tdPagesWithProprietaryInfo").hide();
				$("#trTypeOfProprietaryInfo").hide();
				$("#txtPagesWithProprietaryInfo").val('');
			} else {
				$("#ddlProprietaryInformation").prop("selectedIndex", 0);
				$("#tdPagesWithProprietaryInfo").hide();
				$("#trTypeOfProprietaryInfo").hide();
				$("#txtPagesWithProprietaryInfo").val('');
			}

			if (response.confidentialInfo.involveIntellectualProperty) {
				$("#ddlOwnIntellectualProperty").val(1);
			} else if (!response.confidentialInfo.involveIntellectualProperty) {
				$("#ddlOwnIntellectualProperty").val(2);
			} else {
				$("#ddlOwnIntellectualProperty").prop("selectedIndex", 0);
			}

			// OSP Section
			$("#txtAgencyList").val(response.oSPSectionInfo.listAgency);

			$("#chkFederal").prop("checked",
					response.oSPSectionInfo.fundingSource.federal);
			$("#chkFederalFlowThrough").prop("checked",
					response.oSPSectionInfo.fundingSource.federalFlowThrough);
			$("#chkStateOfIdahoEntity").prop("checked",
					response.oSPSectionInfo.fundingSource.stateOfIdahoEntity);
			$("#chkPrivateForProfit").prop("checked",
					response.oSPSectionInfo.fundingSource.privateForProfit);
			$("#chkNonProfitOrganization")
					.prop(
							"checked",
							response.oSPSectionInfo.fundingSource.nonProfitOrganization);
			$("#chkNonIdahoStateEntity").prop("checked",
					response.oSPSectionInfo.fundingSource.nonIdahoStateEntity);
			$("#chkCollegeUniversity").prop("checked",
					response.oSPSectionInfo.fundingSource.collegeOrUniversity);
			$("#chkLocalEntity").prop("checked",
					response.oSPSectionInfo.fundingSource.localEntity);
			$("#chkNonIdahoLocalEntity").prop("checked",
					response.oSPSectionInfo.fundingSource.nonIdahoLocalEntity);
			$("#chkTribalGovernment").prop("checked",
					response.oSPSectionInfo.fundingSource.tirbalGovernment);
			$("#chkForeign").prop("checked",
					response.oSPSectionInfo.fundingSource.foreign);

			$("#txtCFDANo").val(response.oSPSectionInfo.CFDANo);
			$("#txtProgramNo").val(response.oSPSectionInfo.programNo);
			$("#txtProgramTitle").val(response.oSPSectionInfo.programTitle);

			$("#chkFullRecovery").prop("checked",
					response.oSPSectionInfo.recovery.fullRecovery);
			$("#chkNoRecoveryNormal")
					.prop(
							"checked",
							response.oSPSectionInfo.recovery.noRecoveryNormalSponsorPolicy);
			$("#chkNoRecoveryInstitutional")
					.prop(
							"checked",
							response.oSPSectionInfo.recovery.noRecoveryInstitutionalWaiver);
			$("#chkLimitedRecoveryNormal")
					.prop(
							"checked",
							response.oSPSectionInfo.recovery.limitedRecoveryNormalSponsorPolicy);
			$("#chkLimitedRecoveryInstitutional")
					.prop(
							"checked",
							response.oSPSectionInfo.recovery.limitedRecoveryInstitutionalWaiver);

			$("#chkMTDC")
					.prop("checked", response.oSPSectionInfo.baseInfo.MTDC);
			$("#chkTDC").prop("checked", response.oSPSectionInfo.baseInfo.TDC);
			$("#chkTC").prop("checked", response.oSPSectionInfo.baseInfo.TC);
			$("#chkOther").prop("checked",
					response.oSPSectionInfo.baseInfo.other);
			$("#chkNA").prop("checked",
					response.oSPSectionInfo.baseInfo.notApplicable);

			if (response.oSPSectionInfo.isPISalaryIncluded) {
				$("#ddlPISalaryIncluded").val(1);
				$("#lblPISalaryIncluded").hide();
			} else if (!response.oSPSectionInfo.isPISalaryIncluded) {
				$("#ddlPISalaryIncluded").val(2);
				$("#lblPISalaryIncluded").show();
			} else {
				$("#ddlPISalaryIncluded").prop("selectedIndex", 0);
				$("#lblPISalaryIncluded").hide();
			}

			$("#txtPISalary").autoNumeric('set',
					response.oSPSectionInfo.PISalary);
			$("#txtPIFringe").autoNumeric('set',
					response.oSPSectionInfo.PIFringe);

			$("#txtDepartmentID").val(response.oSPSectionInfo.departmentId);

			if (response.oSPSectionInfo.institutionalCostDocumented.yes) {
				$("#ddlInstitutionalCostDocumented").val(1);
			} else if (response.oSPSectionInfo.institutionalCostDocumented.no) {
				$("#ddlInstitutionalCostDocumented").val(2);
			} else if (response.oSPSectionInfo.institutionalCostDocumented.notApplicable) {
				$("#ddlInstitutionalCostDocumented").val(3);
			} else {
				$("#ddlInstitutionalCostDocumented").prop("selectedIndex", 0);
			}

			if (response.oSPSectionInfo.thirdPartyCostDocumented.yes) {
				$("#ddlThirdPartyCostDocumented").val(1);
			} else if (response.oSPSectionInfo.thirdPartyCostDocumented.no) {
				$("#ddlThirdPartyCostDocumented").val(2);
			} else if (response.oSPSectionInfo.thirdPartyCostDocumented.notApplicable) {
				$("#ddlThirdPartyCostDocumented").val(3);
			} else {
				$("#ddlThirdPartyCostDocumented").prop("selectedIndex", 0);
			}

			if (response.oSPSectionInfo.isAnticipatedSubRecipients) {
				$("#ddlSubrecipients").val(1);
				$("#txtNamesSubrecipients").removeClass("ignore");
				$("#txtNamesSubrecipients").val(
						response.oSPSectionInfo.anticipatedSubRecipientsNames);
				$("#trSubrecipientsNames").show();
			} else if (!response.oSPSectionInfo.isAnticipatedSubRecipients) {
				$("#ddlSubrecipients").val(2);
				$("#txtNamesSubrecipients").addClass("ignore");
				$("#trSubrecipientsNames").hide();
				$("#txtNamesSubrecipients").val('');
			} else {
				$("#ddlSubrecipients").prop("selectedIndex", 0);
				$("#txtNamesSubrecipients").addClass("ignore");
				$("#trSubrecipientsNames").hide();
				$("#txtNamesSubrecipients").val('');
			}

			if (response.oSPSectionInfo.PIEligibilityWaiver.yes) {
				$("#ddlPIEligibilityWaiver").val(1);
			} else if (response.oSPSectionInfo.PIEligibilityWaiver.no) {
				$("#ddlPIEligibilityWaiver").val(2);
			} else if (response.oSPSectionInfo.PIEligibilityWaiver.notApplicable) {
				$("#ddlPIEligibilityWaiver").val(3);
			} else if (response.oSPSectionInfo.PIEligibilityWaiver.thisProposalOnly) {
				$("#ddlPIEligibilityWaiver").val(4);
			} else if (response.oSPSectionInfo.PIEligibilityWaiver.blanket) {
				$("#ddlPIEligibilityWaiver").val(5);
			} else {
				$("#ddlPIEligibilityWaiver").prop("selectedIndex", 0);
			}

			if (response.oSPSectionInfo.conflictOfInterestForms.yes) {
				$("#ddlCOIForms").val(1);
			} else if (response.oSPSectionInfo.conflictOfInterestForms.no) {
				$("#ddlCOIForms").val(2);
			} else if (response.oSPSectionInfo.conflictOfInterestForms.notApplicable) {
				$("#ddlCOIForms").val(3);
			} else {
				$("#ddlCOIForms").prop("selectedIndex", 0);
			}

			if (response.oSPSectionInfo.excludedPartyListChecked.yes) {
				$("#ddlCheckedExcludedPartyList").val(1);
			} else if (response.oSPSectionInfo.excludedPartyListChecked.no) {
				$("#ddlCheckedExcludedPartyList").val(2);
			} else if (response.oSPSectionInfo.excludedPartyListChecked.notApplicable) {
				$("#ddlCheckedExcludedPartyList").val(3);
			} else {
				$("#ddlCheckedExcludedPartyList").prop("selectedIndex", 0);
			}
		},

		BindInvestigatorInfo : function(investigatorInfo) {
			rowIndex = 0;
			myProposal.BindUserToPositionDetails(investigatorInfo.pi, "PI");

			$.each(investigatorInfo.co_pi, function(i, coPI) {
				myProposal.BindUserToPositionDetails(coPI, "Co-PI");
			});

			$.each(investigatorInfo.seniorPersonnel, function(j, senior) {
				myProposal.BindUserToPositionDetails(senior, "Senior");
			});

			$('#dataTable>tbody tr:first').remove();
		},

		SelectFirstAccordion : function() {
			myProposal.OpenAccordionTab($('#ui-id-2'));
		},

		focusTabWithErrors : function(tabPanelName) {
			$(tabPanelName).find('div.ui-tabs-panel').each(function(index) {
				if ($(this).find("span.error").text() != "") {
					$(tabPanelName).accordion("option", "active", index);
					return false;
				}
			});
		},

		OpenAccordionTab : function(tabContentDiv) {
			var icons = $("#accordion").accordion("option", "icons");
			$tabDiv = tabContentDiv.attr('aria-labelledby');
			$('#' + $tabDiv).removeClass('ui-corner-all').addClass(
					'ui-accordion-header-active ui-state-active ui-corner-top')
					.attr({
						'aria-selected' : 'true',
						'aria-expanded' : 'true',
						'tabindex' : '0'
					});
			$('#' + $tabDiv + ' > .ui-accordion-header-icon').removeClass(
					icons.header).addClass(icons.activeHeader);
			tabContentDiv.addClass('ui-accordion-content-active').attr({
				'aria-hidden' : 'false'
			}).show('blind');
		},

		BindUserToPositionDetails : function(userDetails, userType) {
			if (userDetails != undefined || userDetails != null) {
				var cloneRow = $('#dataTable tbody>tr:first').clone(true);
				$(cloneRow).appendTo("#dataTable");

				rowIndex += 1;
				var btnOption = "[+] Add";
				var btnTitle = "Add More"
				var btnName = "AddMore";
				if (rowIndex > 1) {
					btnOption = "Delete ";
					btnTitle = "Delete";
					btnName = "DeleteOption";
				}

				$('#dataTable tbody>tr:eq(' + rowIndex + ')')
						.find("select")
						.each(
								function(k) {
									if (this.name == "ddlRole") {
										if (userType == "PI") {
											$(this).val(0).prop('selected',
													'selected');
											$(this)
													.prop('disabled',
															'disabled');
										} else if (userType == "Co-PI") {
											$(this).val(1).prop('selected',
													'selected');
											$(this).prop('disabled', true);
											$(this).find('option').not(
													':selected').remove();
										} else if (userType == "Senior") {
											$(this).val(2).prop('selected',
													'selected');
											$(this).prop('disabled', true);
											$(this).find('option').not(
													':selected').remove();
										}
									} else if (this.name == "ddlName") {
										$(this).val(userDetails.userProfileId)
												.prop('selected', 'selected');

										if (userType == "PI") {
											$(this).prop('disabled', true);
										} else if (userType == "Co-PI") {
											$(this).prop('disabled', true);
											$(this).find('option').not(
													':selected').remove();
										} else if (userType == "Senior") {
											$(this).prop('disabled', true);
											$(this).find('option').not(
													':selected').remove();
										}

										myProposal.BindUserMobileNo($(
												'select[name="ddlName"]').eq(
												rowIndex).val());

										myProposal.BindCollegeDropDown($(
												'select[name="ddlName"]').eq(
												rowIndex).val());
									} else if (this.name == "ddlCollege") {
										$(this).val(userDetails.college).prop(
												'selected', 'selected');
										myProposal.BindDepartmentDropDown($(
												'select[name="ddlName"]').eq(
												rowIndex).val(), $(
												'select[name="ddlCollege"]')
												.eq(rowIndex).val());
									} else if (this.name == "ddlDepartment") {
										$(this).val(userDetails.department)
												.prop('selected', 'selected');
										myProposal.BindPositionTypeDropDown($(
												'select[name="ddlName"]').eq(
												rowIndex).val(), $(
												'select[name="ddlCollege"]')
												.eq(rowIndex).val(), $(
												'select[name="ddlDepartment"]')
												.eq(rowIndex).val());
									} else if (this.name == "ddlPositionType") {
										$(this).val(userDetails.positionType)
												.prop('selected', 'selected');
										myProposal
												.BindPositionTitleDropDown(
														$(
																'select[name="ddlName"]')
																.eq(rowIndex)
																.val(),
														$(
																'select[name="ddlCollege"]')
																.eq(rowIndex)
																.val(),
														$(
																'select[name="ddlDepartment"]')
																.eq(rowIndex)
																.val(),
														$(
																'select[name="ddlPositionType"]')
																.eq(rowIndex)
																.val());
									} else if (this.name == "ddlPositionTitle") {
										$(this).val(userDetails.positionTitle)
												.prop('selected', 'selected');
									}
								});

				$('#dataTable tbody>tr:eq(' + rowIndex + ')').find("input")
						.each(function(l) {
							if ($(this).is(".AddOption")) {
								$(this).prop("name", btnName);
								$(this).prop("value", btnOption);
								$(this).prop("title", btnTitle);
							}
						});
			}
		},

		SearchProposalAuditLogs : function() {
			var action = $.trim($("#txtSearchAction").val());
			if (action.length < 1) {
				action = null;
			}

			var auditedBy = $.trim($("#txtSearchAuditedBy").val());
			if (auditedBy.length < 1) {
				auditedBy = null;
			}

			var activityOnFrom = $.trim($("#txtSearchActivityOnFrom").val());
			if (activityOnFrom.length < 1) {
				activityOnFrom = null;
			}

			var activityOnTo = $.trim($("#txtSearchActivityOnTo").val());
			if (activityOnTo.length < 1) {
				activityOnTo = null;
			}

			myProposal.BindProposalAuditLogGrid(myProposal.config.proposalId,
					action, auditedBy, activityOnFrom, activityOnTo);
		},

		BindProposalAuditLogGrid : function(proposalId, action, auditedBy,
				activityOnFrom, activityOnTo) {
			this.config.url = this.config.baseURL;
			this.config.method = "GetProposalAuditLogList";
			var offset_ = 1;
			var current_ = 1;
			var perpage = ($("#gdvProposalsAuditLog_pagesize").length > 0) ? $(
					"#gdvProposalsAuditLog_pagesize :selected").text() : 10;

			var auditLogBindObj = {
				Action : action,
				AuditedBy : auditedBy,
				ActivityOnFrom : activityOnFrom,
				ActivityOnTo : activityOnTo,
			};
			this.config.data = {
				proposalId : proposalId,
				auditLogBindObj : auditLogBindObj
			};
			var data = this.config.data;

			$("#gdvProposalsAuditLog").sagegrid({
				url : this.config.url,
				functionMethod : this.config.method,
				colModel : [ {
					display : 'User Name',
					name : 'user_name',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Full Name',
					name : 'full_name',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Action',
					name : 'action',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Activity On',
					name : 'activity_on',
					cssclass : 'cssClassHeadDate',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					type : 'date',
					format : 'yyyy/MM/dd hh:mm:ss a'
				} ],
				rp : perpage,
				nomsg : 'No Records Found!',
				param : data,
				current : current_,
				pnew : offset_,
				sortcol : {
					4 : {
						sorter : false
					}
				}
			});
		},

		ViewChangeLogs : function(tblID, argus) {
			switch (tblID) {
			case "gdvProposals":
				var proposal_roles = $.trim(argus[5]);
				myProposal.config.proposalId = argus[0];
				myProposal.config.proposalRoles = proposal_roles;
				myProposal.config.ajaxCallMode = 16;
				myProposal.config.arguments = argus;
				if (proposal_roles != "") {
					myProposal.CheckUserPermissionWithProposalRole("View",
							myProposal.config.proposalRoles,
							myProposal.config.proposalId, "Audit Log",
							myProposal.config);
				} else {
					myProposal.CheckUserPermissionWithPositionTitle("View",
							myProposal.config.proposalId, "Audit Log",
							myProposal.config);
				}
				break;
			default:
				break;
			}
		},

		DeleteProposal : function(tblID, argus) {
			switch (tblID) {
			case "gdvProposals":
				var proposal_roles = $.trim(argus[1]);
				if (argus[2].toLowerCase() != "yes") {
					myProposal.config.ajaxCallMode = 10;
					myProposal.config.proposalRoles = proposal_roles;
					myProposal.config.proposalId = argus[0];
					if (proposal_roles != "") {
						myProposal.CheckUserPermissionWithProposalRole(
								"Delete", myProposal.config.proposalRoles,
								myProposal.config.proposalId, "Whole Proposal",
								myProposal.config);
					} else {
						myProposal.CheckUserPermissionWithPositionTitle(
								"Delete", myProposal.config.proposalId,
								"Whole Proposal", myProposal.config);
					}
					return false;
				} else {
					csscody.alert('<h2>' + 'Information Alert' + '</h2><p>'
							+ 'Sorry! this proposal is already deleted.'
							+ '</p>');
				}
				break;
			default:
				break;
			}
		},

		DeleteProposalById : function(_proposalId, _proposalRoles) {
			var properties = {
				onComplete : function(e) {
					myProposal.ConfirmSingleDelete(_proposalId, _proposalRoles,
							e);
				}
			};
			csscody.confirm("<h2>" + 'Delete Confirmation' + "</h2><p>"
					+ 'Are you certain you want to delete this proposal?'
					+ "</p>", properties);
		},

		ConfirmDeleteMultiple : function(proposal_ids, event) {
			if (event) {
				myProposal.DeleteMultipleProposals(proposal_ids);
			}
		},

		// TODO need to remove this multiple delete options otherwise need to
		// alert in each row
		DeleteMultipleProposals : function(_proposalIds) {
			// this.config.dataType = "html";
			this.config.url = this.config.baseURL
					+ "DeleteMultipleProposalsByProposalID";
			this.config.data = JSON2.stringify({
				proposalIds : _proposalIds,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 3;
			this.ajaxCall(this.config);
			return false;
		},

		ConfirmSingleDelete : function(proposal_id, proposalRoles, event) {
			if (event) {
				myProposal.DeleteSingleUser(proposal_id, proposalRoles);
			}
		},

		DeleteSingleUser : function(_proposalId, _proposalRoles) {
			this.config.url = this.config.baseURL
					+ "DeleteProposalByProposalID";
			this.config.data = JSON2.stringify({
				proposalId : _proposalId,
				proposalRoles : _proposalRoles,
				proposalUserTitle : GPMS.utils.GetUserPositionTitle(),
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 2;
			this.ajaxCall(this.config);
			return false;
		},

		ClearForm : function() {
			validator.resetForm();
			// $('#accordion-expand-holder').hide();

			if (this.config.uploadObj != "") {
				this.config.uploadObj.reset(true);
			}

			myProposal.config.proposalId = '0';
			myProposal.config.proposalRoles = "";
			myProposal.config.buttonType = "";
			myProposal.config.arguments = [];
			myProposal.config.events = "";
			myProposal.config.content = [];

			$('.cssClassRight').hide();
			$('.cssClassError').hide();

			// Hide all instrcution information
			$("#lblConfirmCommitment").hide();
			$("#lblCommitmentsRequired").hide();
			$("#lblDisclosureRequired").hide();
			$("#lblMaterialChanged").hide();
			$("#lblUseHumanSubjects").hide();
			$("#tdHumanSubjectsOption").hide();
			$("#tdIRBOption").hide();
			$("#tdIRBtxt").hide();
			$("#lblUseVertebrateAnimals").hide();
			$("#tdVertebrateAnimalsOption").hide();
			$("#tdIACUCOption").hide();
			$("#tdIACUCtxt").hide();
			$("#lblHasBiosafetyConcerns").hide();
			$("#tdBiosafetyOption").hide();
			$("#tdIBCOption").hide();
			$("#tdIBCtxt").hide();
			$("#lblInvolveNonFundedCollabs").hide();
			$("#trInvolveNonFundedCollabs").hide();
			$("#tdPagesWithProprietaryInfo").hide();
			$("#trTypeOfProprietaryInfo").hide();
			$("#lblPISalaryIncluded").hide();
			$("#trSubrecipientsNames").hide();

			// For Signature Section
			$("#trSignChair").hide();
			$("#trSignBusinessManager").hide();
			$("#trSignDean").hide();
			$("#trSignAdministrator").hide();
			$("#trSignDirector").hide();
			signatureInfo = '';
			$("#trSignPICOPI tbody").empty();
			$("#trSignChair tbody").empty();
			$("#trSignBusinessManager tbody").empty();
			$("#trSignDean tbody").empty();
			$("#trSignAdministrator tbody").empty();
			$("#trSignDirector tbody").empty();

			$('#txtProjectTitle').removeAttr('disabled');

			rowIndex = 0;
			$("#dataTable tbody>tr:gt(0)").remove();

			$('select[name=ddlRole]').eq(0).val(0).prop('selected', 'selected')
					.prop('disabled', 'disabled');

			var container = $("#accordion > div").slice(1, 12);
			var inputs = container.find('INPUT, SELECT, TEXTAREA');
			$.each(inputs, function(i, item) {
				$(this).prop('checked', false);
				$(this).val('');
				$(this).val($(this).find('option').first().val());
			});
			$(".AddOption").val("[+] Add");
			return false;
		},

		BindDefaultUserPosition : function(rowIndexVal) {
			// For form Dropdown Binding
			myProposal.BindAllPositionDetailsForAUser($(
					'select[name="ddlName"]').eq(rowIndexVal).val());

			myProposal.BindUserMobileNo($('select[name="ddlName"]').eq(
					rowIndexVal).val());

			myProposal.BindCollegeDropDown($('select[name="ddlName"]').eq(
					rowIndexVal).val());
			myProposal.BindDepartmentDropDown($('select[name="ddlName"]').eq(
					rowIndexVal).val(), $('select[name="ddlCollege"]').eq(
					rowIndexVal).val());
			myProposal.BindPositionTypeDropDown($('select[name="ddlName"]').eq(
					rowIndexVal).val(), $('select[name="ddlCollege"]').eq(
					rowIndexVal).val(), $('select[name="ddlDepartment"]').eq(
					rowIndexVal).val());
			myProposal.BindPositionTitleDropDown($('select[name="ddlName"]')
					.eq(rowIndexVal).val(), $('select[name="ddlCollege"]').eq(
					rowIndexVal).val(), $('select[name="ddlDepartment"]').eq(
					rowIndexVal).val(), $('select[name="ddlPositionType"]').eq(
					rowIndexVal).val());
			return false;
		},

		BindPICoPISignatures : function() {
			var fullName = $('select[name="ddlName"]').eq(0).find(
					"option:selected").text();
			var cloneRow = '<tr allowchange="true" allowsign="true"><td><span class="cssClassLabel" name ="fullname" role="PI" delegated="false">'
					+ fullName
					+ '</span></td><td><input id="pi_signature" data-for="signature" data-value="'
					+ $('select[name="ddlName"]').eq(0).val()
					+ '" title="PI\'s Signature" class="sfInputbox" placeholder="PI\'s Signature" type="text" required="true" name="'
					+ $('select[name="ddlName"]').eq(0).val()
					+ 'PI">'
					+ '</td><td><input id="pi_signaturedate" data-for="signaturedate" name="signaturedate'
					+ $('select[name="ddlName"]').eq(0).val()
					+ 'PI" title="Signed Date" class="sfInputbox" placeholder="Signed Date" type="text" required="true" readonly="true" onfocus="myProposal.BindCurrentDateTime(this);"></td><td><textarea rows="2" cols="26" name="proposalNotes'
					+ $('select[name="ddlName"]').eq(0).val()
					+ 'PI" required="true" title="Proposal Notes" class="cssClassTextArea"></textarea></td></tr>';
			$(cloneRow).appendTo("#trSignPICOPI tbody");
		},

		InitializeAccordion : function() {
			var icons = {
				header : "ui-icon-circle-arrow-e",
				activeHeader : "ui-icon-circle-arrow-s"
			};

			var $accordion = $("#accordion")
					.accordion(
							{
								heightStyle : "content",
								icons : icons,
								active : false,
								collapsible : true,
								activate : function(event, ui) {
									if (myProposal.config.proposalId != "0"
											&& ui.newHeader.length != 0) {
										alert($.trim(ui.newHeader.text()));
										myProposal.config.ajaxCallMode = 15;
										// myProposal.config.event = event;
										myProposal.config.content = ui.newPanel;
										if (myProposal.config.proposalRoles != "") {
											myProposal
													.CheckUserPermissionWithProposalRole(
															"Edit",
															myProposal.config.proposalRoles,
															myProposal.config.proposalId,
															$.trim(ui.newHeader
																	.text()),
															myProposal.config);
										} else {
											myProposal
													.CheckUserPermissionWithPositionTitle(
															"Edit",
															myProposal.config.proposalId,
															$.trim(ui.newHeader
																	.text()),
															myProposal.config);
										}
									}
								},
								beforeActivate : function(event, ui) {
									// Size = 0 --> collapsing
									// Size = 1 --> Expanding
									if (myProposal.config.proposalId != "0"
											&& ui.newHeader.length != 0) {
										alert($.trim(ui.newHeader.text()));
										myProposal.config.ajaxCallMode = 14;
										myProposal.config.event = event;
										if (myProposal.config.proposalRoles != "") {
											myProposal
													.CheckUserPermissionWithProposalRole(
															"View",
															myProposal.config.proposalRoles,
															myProposal.config.proposalId,
															$.trim(ui.newHeader
																	.text()),
															myProposal.config);
										} else {
											myProposal
													.CheckUserPermissionWithPositionTitle(
															"View",
															myProposal.config.proposalId,
															$.trim(ui.newHeader
																	.text()),
															myProposal.config);
										}

									}
								}
							});
			// myProposal.SelectFirstAccordion();
			// $("#accordion").accordion("option", "active", 0);
			return false;
		},

		BindAllSignatureForAProposal : function(proposalId) {
			myProposal.config.url = myProposal.config.baseURL
					+ "GetAllSignatureForAProposal";
			myProposal.config.data = JSON2.stringify({
				proposalId : proposalId
			});
			myProposal.config.ajaxCallMode = 8;
			myProposal.ajaxCall(myProposal.config);
		},

		BindCurrentDateTime : function(obj) {
			$(obj).val($.format.date(new Date(), 'yyyy/MM/dd hh:mm:ss a'));
			return false;
		},

		GetUserSignature : function(obj) {
			var allowedChangeAttr = obj.attr('allowchange');
			var allowedSignAttr = obj.attr('allowsign');

			if (typeof allowedChangeAttr !== typeof undefined
					&& allowedChangeAttr !== false
					&& allowedChangeAttr == "true"
					&& typeof allowedSignAttr !== typeof undefined
					&& allowedSignAttr !== false && allowedSignAttr == "true") {
				obj
						.find("input")
						.each(
								function() {
									var optionsText = $(this).val();
									if (optionsText
											&& $(this).attr("data-for") != "signaturedate") {

										signatureInfo += $(this).attr(
												"data-value")
												+ "!#!"; // UserProfileID

										signatureInfo += optionsText + "!#!"; // Signature
									} else {
										signatureInfo += optionsText + "!#!"; // SignedDate
									}
								});
				obj.find("textarea").each(function() {
					signatureInfo += $(this).val() + "!#!"; // Note
				});

				signatureInfo += obj.find('span.cssClassLabel').text() + "!#!"; // FullName
				signatureInfo += obj.find('span.cssClassLabel').attr("role")
						+ "!#!";
				// PositionTitle
				signatureInfo += obj.find('span.cssClassLabel').attr(
						"delegated")
						+ "#!#";
				// Delegated
			}
		},

		checkUniqueProjectTitle : function(proposal_id, projectTitle,
				textBoxProjectTitle) {
			var errors = '';
			if (!textBoxProjectTitle.hasClass('error')
					&& projectTitle.length > 0) {
				if (!myProposal.isUniqueProjectTitle(proposal_id, projectTitle)) {
					errors += "'" + 'Please enter unique Project Title.' + " '"
							+ projectTitle.trim() + "' "
							+ 'has already been taken.';
					textBoxProjectTitle.addClass("error");
					textBoxProjectTitle.siblings('.cssClassRight').hide();
					if (textBoxProjectTitle.siblings('.error').exists()) {
						textBoxProjectTitle.siblings('.error').html(errors);
					} else {
						$(
								'<span id="txtProjectTitle-error" class="error" for="txtProjectTitle">'
										+ errors + '</span>').insertAfter(
								textBoxProjectTitle);
					}

					textBoxProjectTitle.siblings('.error').show();
					textBoxProjectTitle.focus();
				} else {
					textBoxProjectTitle.removeClass("error");
					textBoxProjectTitle.siblings('.cssClassRight').show();
					textBoxProjectTitle.siblings('.error').hide();
					textBoxProjectTitle.siblings('.error').html('');
				}
			}
			return errors;
		},

		isUniqueProjectTitle : function(proposalId, newProjectTitle) {
			var proposalUniqueObj = {
				ProposalID : proposalId,
				NewProjectTitle : newProjectTitle
			};

			this.config.url = this.config.baseURL + "CheckUniqueProjectTitle";
			this.config.data = JSON2.stringify({
				proposalUniqueObj : proposalUniqueObj
			});
			this.config.ajaxCallMode = 7;
			this.ajaxCall(this.config);
			return projectTitleIsUnique;
		},

		SaveProposal : function(_buttonType, _proposalRoles, _proposalId, _flag) {
			if (validator.form()) {
				var $projectTitle = $('#txtProjectTitle');
				var projectTitle = $.trim($projectTitle.val());
				var validateErrorMessage = myProposal.checkUniqueProjectTitle(
						_proposalId, projectTitle, $projectTitle);

				if (validateErrorMessage == "") {
					var investigatorInfo = '';
					$('#dataTable > tbody  > tr')
							.each(
									function() {
										$(this)
												.find("select")
												.each(
														function() {
															var optionsText = $(
																	this).val();
															if (!optionsText
																	&& $(this)
																			.prop(
																					"name") != "ddlPositionTitle") {
																validateErrorMessage = 'Please select all position details for this user.'
																		+ "<br/>";
																$(this).focus();
															} else if (optionsText
																	&& $(this)
																			.prop(
																					"name") != "ddlPositionTitle") {
																investigatorInfo += optionsText
																		+ "!#!";
															} else {
																investigatorInfo += optionsText
																		+ "!#!";
															}
														});

										investigatorInfo += $(this).find(
												'input[name="txtPhoneNo"]')
												.mask()
												+ "#!#";
									});

					investigatorInfo = investigatorInfo.substring(0,
							investigatorInfo.length - 3);

					signatureInfo = '';

					$(
							'#trSignPICOPI > tbody  > tr, #trSignChair > tbody  > tr, #trSignDean > tbody  > tr, #trSignBusinessManager > tbody  > tr, #trSignAdministrator > tbody  > tr, #trSignDirector > tbody  > tr')
							.each(function() {
								myProposal.GetUserSignature($(this));
							});

					signatureInfo = signatureInfo.substring(0,
							signatureInfo.length - 3);

					var projectInfo = {
						ProjectTitle : $.trim($("#txtProjectTitle").val()),
						ProjectType : $("#ddlProjectType").val(),
						TypeOfRequest : $("#ddlTypeOfRequest").val(),
						ProjectLocation : $("#ddlLocationOfProject").val(),
						DueDate : $("#txtDueDate").val(),
						ProjectPeriodFrom : $("#txtProjectPeriodFrom").val(),
						ProjectPeriodTo : $("#txtProjectPeriodTo").val()
					};

					var sponsorAndBudgetInfo = {
						GrantingAgency : $.trim($("#txtNameOfGrantingAgency")
								.val()),
						DirectCosts : $('#txtDirectCosts').autoNumeric('get'),
						FACosts : $("#txtFACosts").autoNumeric('get'),
						TotalCosts : $("#txtTotalCosts").autoNumeric('get'),
						FARate : $("#txtFARate").autoNumeric('get')
					};

					var costShareInfo = {
						InstitutionalCommitted : $(
								"#ddlInstitutionalCommitmentCost").val(),
						ThirdPartyCommitted : $("#ddlThirdPartyCommitmentCost")
								.val()
					};

					var univCommitments = {
						NewRenovatedFacilitiesRequired : $(
								"#ddlNewSpaceRequired").val(),
						RentalSpaceRequired : $("#ddlRentalSpaceRequired")
								.val(),
						InstitutionalCommitmentRequired : $(
								"#ddlInstitutionalCommitmentsRequired").val()
					};

					var conflicOfInterestInfo = {
						FinancialCOI : $("#ddlFinancialCOI").val(),
						ConflictDisclosed : $("#ddlDisclosedFinancialCOI")
								.val(),
						DisclosureFormChange : $("#ddlMaterialChanged").val()
					};

					var complianceInfo = {
						InvolveUseOfHumanSubjects : $("#ddlUseHumanSubjects")
								.val(),
						InvolveUseOfVertebrateAnimals : $(
								"#ddlUseVertebrateAnimals").val(),
						InvolveBiosafetyConcerns : $("#ddlInvovleBioSafety")
								.val(),
						InvolveEnvironmentalHealthAndSafetyConcerns : $(
								"#ddlEnvironmentalConcerns").val()
					};

					if ($("#ddlUseHumanSubjects").val() == "1") {
						complianceInfo.IRBPending = $("#ddlIRBOptions").val();
					}

					if ($("#ddlIRBOptions").val() == "1") {
						complianceInfo.IRB = $("#txtIRB").val();
					}

					if ($("#ddlUseVertebrateAnimals").val() == "1") {
						complianceInfo.IACUCPending = $("#ddlIACUCOptions")
								.val();
					}

					if ($("#ddlIACUCOptions").val() == "1") {
						complianceInfo.IACUC = $("#txtIACUC").val();
					}

					if ($("#ddlInvovleBioSafety").val() == "1") {
						complianceInfo.IBCPending = $("#ddlIBCOptions").val();
					}

					if ($("#ddlIBCOptions").val() == "1") {
						complianceInfo.IBC = $("#txtIBC").val();
					}

					var additionalInfo = {
						AnticipatesForeignNationalsPayment : $(
								"#ddlAnticipateForeignNationals").val(),
						AnticipatesCourseReleaseTime : $(
								"#ddlAnticipateReleaseTime").val(),
						RelatedToCenterForAdvancedEnergyStudies : $(
								"#ddlRelatedToEnergyStudies").val()
					};

					var collaborationInfo = {
						InvolveNonFundedCollab : $(
								"#ddlInvolveNonFundedCollabs").val()
					};

					if ($("#ddlInvolveNonFundedCollabs").val() == "1") {
						collaborationInfo.Collaborators = $("#txtCollaborators")
								.val();
					}

					var confidentialInfo = {
						ContainConfidentialInformation : $(
								"#ddlProprietaryInformation").val(),
						InvolveIntellectualProperty : $(
								"#ddlOwnIntellectualProperty").val()
					};

					if ($("#ddlProprietaryInformation").val() == "1") {
						confidentialInfo.OnPages = $.trim($(
								"#txtPagesWithProprietaryInfo").val());
						confidentialInfo.Patentable = $("#chkPatentable").prop(
								"checked");
						confidentialInfo.Copyrightable = $("#chkCopyrightable")
								.prop("checked");
					}

					var proposalInfo = {
						ProposalID : _proposalId,
						InvestigatorInfo : investigatorInfo,
						ProjectInfo : projectInfo,
						SponsorAndBudgetInfo : sponsorAndBudgetInfo,
						CostShareInfo : costShareInfo,
						UnivCommitments : univCommitments,
						ConflicOfInterestInfo : conflicOfInterestInfo,
						ComplianceInfo : complianceInfo,
						AdditionalInfo : additionalInfo,
						CollaborationInfo : collaborationInfo,
						ConfidentialInfo : confidentialInfo
					};

					if (signatureInfo != "") {
						proposalInfo.SignatureInfo = signatureInfo;
					}

					// TODO check if the OSP section is allowed to edit ?
					if (!_flag) {
						// proposalInfo.ProposalStatus = $("#ddlProposalStatus")
						// .val();

						var OSPSection = {
							ListAgency : $.trim($("#txtAgencyList").val()),

							Federal : $("#chkFederal").prop("checked"),
							FederalFlowThrough : $("#chkFederalFlowThrough")
									.prop("checked"),
							StateOfIdahoEntity : $("#chkStateOfIdahoEntity")
									.prop("checked"),
							PrivateForProfit : $("#chkPrivateForProfit").prop(
									"checked"),
							NonProfitOrganization : $(
									"#chkNonProfitOrganization")
									.prop("checked"),
							NonIdahoStateEntity : $("#chkNonIdahoStateEntity")
									.prop("checked"),
							CollegeOrUniversity : $("#chkCollegeUniversity")
									.prop("checked"),
							LocalEntity : $("#chkLocalEntity").prop("checked"),
							NonIdahoLocalEntity : $("#chkNonIdahoLocalEntity")
									.prop("checked"),
							TirbalGovernment : $("#chkTribalGovernment").prop(
									"checked"),
							Foreign : $("#chkForeign").prop("checked"),

							CFDANo : $.trim($("#txtCFDANo").val()),
							ProgramNo : $.trim($("#txtProgramNo").val()),
							ProgramTitle : $.trim($("#txtProgramTitle").val()),

							// --------------------------
							FullRecovery : $("#chkFullRecovery")
									.prop("checked"),
							NoRecoveryNormalSponsorPolicy : $(
									"#chkNoRecoveryNormal").prop("checked"),
							NoRecoveryInstitutionalWaiver : $(
									"#chkNoRecoveryInstitutional").prop(
									"checked"),
							LimitedRecoveryNormalSponsorPolicy : $(
									"#chkLimitedRecoveryNormal")
									.prop("checked"),
							LimitedRecoveryInstitutionalWaiver : $(
									"#chkLimitedRecoveryInstitutional").prop(
									"checked"),

							MTDC : $("#chkMTDC").prop("checked"),
							TDC : $("#chkTDC").prop("checked"),
							TC : $("#chkTC").prop("checked"),
							Other : $("#chkOther").prop("checked"),
							NotApplicable : $("#chkNA").prop("checked"),

							// --------------------------
							IsPISalaryIncluded : $("#ddlPISalaryIncluded")
									.val(),
							PISalary : $("#txtPISalary").autoNumeric('get'),
							PIFringe : $("#txtPIFringe").autoNumeric('get'),
							DepartmentId : $.trim($("#txtDepartmentID").val()),
							InstitutionalCostDocumented : $(
									"#ddlInstitutionalCostDocumented").val(),
							ThirdPartyCostDocumented : $(
									"#ddlThirdPartyCostDocumented").val(),

							// --------------------------
							IsAnticipatedSubRecipients : $("#ddlSubrecipients")
									.val(),

							// --------------------------
							PIEligibilityWaiver : $("#ddlPIEligibilityWaiver")
									.val(),
							ConflictOfInterestForms : $("#ddlCOIForms").val(),
							ExcludedPartyListChecked : $(
									"#ddlCheckedExcludedPartyList").val()
						};

						if ($("#ddlSubrecipients").val() == "1") {
							OSPSection.AnticipatedSubRecipientsNames = $
									.trim($("#txtNamesSubrecipients").val());
						}

						proposalInfo.OSPSectionInfo = OSPSection;
					}

					$
							.each(
									this.config.uploadObj.getResponses()
											.reverse(),
									function(i, val) {
										val['title'] = $(
												myProposal.config.uploadObj.container
														.children(
																".ajax-file-upload-statusbar")
														.find('.extrahtml')
														.find("input").get(i))
												.val();
									});

					proposalInfo.AppendixInfo = this.config.uploadObj
							.getResponses().reverse();

					myProposal.AddProposalInfo(_buttonType, _proposalRoles,
							proposalInfo);

				}
			} else {
				myProposal.focusTabWithErrors("#accordion");
			}
		},

		AddProposalInfo : function(buttonClicked, _proposalRoles, info) {
			alert(buttonClicked);
			this.config.url = this.config.baseURL + "SaveUpdateProposal";
			this.config.data = JSON2.stringify({
				buttonType : buttonClicked,
				proposalRoles : _proposalRoles,
				proposalUserTitle : GPMS.utils.GetUserPositionTitle(),
				proposalInfo : info,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 9;
			this.ajaxCall(this.config);
			return false;
		},

		UpdateProposalStatus : function(buttonClicked, _proposalId) {
			alert(buttonClicked);
			this.config.url = this.config.baseURL + "UpdateProposalStatus";
			this.config.data = JSON2.stringify({
				buttonType : buttonClicked,
				proposalUserTitle : GPMS.utils.GetUserPositionTitle(),
				proposalId : _proposalId,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 9;
			this.ajaxCall(this.config);
			return false;
		},

		BindProposalStatus : function() {
			this.config.url = this.config.baseURL + "GetProposalStatusList";
			this.config.data = "{}";
			this.config.ajaxCallMode = 1;
			this.ajaxCall(this.config);
			return false;
		},

		// TODO only bind user based on current user details not show all users
		BindUserDropDown : function() {
			// Used User REST API instead Proposal
			this.config.url = this.config.rootURL + "users/"
					+ "GetAllUserDropdown";
			this.config.data = "{}";
			this.config.ajaxCallMode = 5;
			this.ajaxCall(this.config);
			return false;
		},

		BindAllUsersAndPositions : function() {
			// Used User REST API instead Proposal
			this.config.url = this.config.rootURL + "users/" + "GetAllUserList";
			this.config.data = "{}";
			this.config.ajaxCallMode = 5;
			this.ajaxCall(this.config);
			return false;
		},

		BindAllPositionDetailsForAUser : function(userId) {
			if (userId != null) {
				var doExists = false;
				$.each(positionsDetails, function(item, value) {
					if (value.id == userId) {
						doExists = true;
						return false;
					}
				});

				if (!doExists) {
					this.config.url = this.config.rootURL + "users/"
							+ "GetAllPositionDetailsForAUser";
					this.config.data = JSON2.stringify({
						userId : userId
					});
					this.config.ajaxCallMode = 6;
					this.ajaxCall(this.config);
				}
			}
			return false;
		},

		BindUserMobileNo : function(userId) {
			if (userId != null) {
				$.each(positionsDetails, function(item, value) {
					if (value.id == userId) {
						// $('input[name="txtPhoneNo"]').eq(rowIndex).val('');
						$('input[name="txtPhoneNo"]').eq(rowIndex).val(
								value.mobileNumber).mask("(999) 999-9999");
						return false;
					}
				});
			}
			return false;
		},

		BindCollegeDropDown : function(userId) {
			if (userId != null) {
				$('select[name="ddlCollege"]').get(rowIndex).options.length = 0;
				$('select[name="ddlDepartment"]').get(rowIndex).options.length = 0;
				$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
				$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
				var arrCollege = [];

				$
						.map(
								positionsDetails,
								function(item, value) {
									if (item.id == userId) {
										$
												.map(
														item.positions,
														function(collegelist,
																keyCollege) {
															if ($.inArray(
																	keyCollege,
																	arrCollege) !== -1) {
																return false;
															} else {
																arrCollege
																		.push(keyCollege);
																$(
																		'select[name="ddlCollege"]')
																		.get(
																				rowIndex).options[$(
																		'select[name="ddlCollege"]')
																		.get(
																				rowIndex).options.length] = new Option(
																		keyCollege,
																		keyCollege);
															}
														});
										return false;
									}
								});
			}
			return false;
		},

		BindDepartmentDropDown : function(userId, collegeName) {
			if (userId != null && collegeName != null) {
				$('select[name="ddlDepartment"]').get(rowIndex).options.length = 0;
				$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
				$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
				var arrDepartment = [];

				$
						.map(
								positionsDetails,
								function(item, value) {
									if (item.id == userId) {
										$
												.map(
														item.positions,
														function(collegelist,
																keyCollege) {
															if (keyCollege == collegeName) {
																$
																		.map(
																				collegelist,
																				function(
																						college,
																						collegeCount) {
																					$
																							.map(
																									college,
																									function(
																											departmentlist,
																											keyDepartment) {
																										if ($
																												.inArray(
																														keyDepartment,
																														arrDepartment) !== -1) {
																											return false;
																										} else {
																											arrDepartment
																													.push(keyDepartment);
																											$(
																													'select[name="ddlDepartment"]')
																													.get(
																															rowIndex).options[$(
																													'select[name="ddlDepartment"]')
																													.get(
																															rowIndex).options.length] = new Option(
																													keyDepartment,
																													keyDepartment);
																										}
																									});
																				});
															}
														});
										return false;
									}
								});
			}
			return false;
		},

		BindPositionTypeDropDown : function(userId, collegeName, departmentName) {
			if (userId != null && collegeName != null && departmentName != null) {
				$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
				$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
				var arrPositionType = [];

				$
						.map(
								positionsDetails,
								function(item, value) {
									if (item.id == userId) {
										$
												.map(
														item.positions,
														function(collegelist,
																keyCollege) {
															if (keyCollege == collegeName) {
																$
																		.map(
																				collegelist,
																				function(
																						college,
																						collegeCount) {
																					$
																							.map(
																									college,
																									function(
																											departmentlist,
																											keyDepartment) {
																										if (keyDepartment == departmentName) {
																											$
																													.map(
																															departmentlist,
																															function(
																																	positionTypelist,
																																	positionTypeCount) {
																																$
																																		.map(
																																				positionTypelist,
																																				function(
																																						valuePositionTitle,
																																						keyPositionType) {
																																					if ($
																																							.inArray(
																																									keyPositionType,
																																									arrPositionType) !== -1) {
																																						return false;
																																					} else {
																																						arrPositionType
																																								.push(keyPositionType);
																																						$(
																																								'select[name="ddlPositionType"]')
																																								.get(
																																										rowIndex).options[$(
																																								'select[name="ddlPositionType"]')
																																								.get(
																																										rowIndex).options.length] = new Option(
																																								keyPositionType,
																																								keyPositionType);
																																					}
																																				});
																															});
																										}
																									});
																				});
															}
														});
										return false;
									}
								});
			}
			return false;
		},

		BindPositionTitleDropDown : function(userId, collegeName,
				departmentName, positionTypeName) {
			if (userId != null && collegeName != null && departmentName != null
					&& positionTypeName != null) {
				$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
				$
						.map(
								positionsDetails,
								function(item, value) {
									if (item.id == userId) {
										$
												.map(
														item.positions,
														function(collegelist,
																keyCollege) {
															// (keyCollege.hasOwnProperty(collegeName))
															if (keyCollege == collegeName) {
																$
																		.map(
																				collegelist,
																				function(
																						college,
																						collegeCount) {
																					$
																							.map(
																									college,
																									function(
																											departmentlist,
																											keyDepartment) {
																										if (keyDepartment == departmentName) {
																											$
																													.map(
																															departmentlist,
																															function(
																																	positionTypelist,
																																	positionTypeCount) {
																																$
																																		.map(
																																				positionTypelist,
																																				function(
																																						valuePositionTitle,
																																						keyPositionType) {
																																					if (keyPositionType == positionTypeName) {
																																						$(
																																								'select[name="ddlPositionTitle"]')
																																								.get(
																																										rowIndex).options[$(
																																								'select[name="ddlPositionTitle"]')
																																								.get(
																																										rowIndex).options.length] = new Option(
																																								valuePositionTitle,
																																								valuePositionTitle);
																																					}
																																				});

																															});
																										}
																									});
																				});
															}
														});
										return false;
									}
								});
			}
			return false;
		},

		ajaxSuccess : function(msg) {
			switch (myProposal.config.ajaxCallMode) {
			case 0:
				break;

			case 1: // For Proposal Status Dropdown Binding for both form and
				// search
				$('#ddlSearchProposalStatus option').length = 1;
				$('#ddlProposalStatus option').length = 0;

				$.each(msg, function(index, item) {
					$('#ddlSearchProposalStatus').append(
							new Option(item.statusValue, item.statusKey));
					// $('#ddlProposalStatus').append(
					// new Option(item.statusValue, item.statusKey));
				});
				break;

			case 2: // Single Proposal Delete
				myProposal.BindProposalGrid(null, null, null, null, null, null,
						null, null);
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'Proposal has been deleted successfully.' + "</p>");

				$('#divProposalForm').hide();
				$('#divProposalGrid').show();
				$('#divProposalAuditGrid').hide();
				myProposal.config.proposalId = '0';
				myProposal.config.proposalRoles = "";
				myProposal.config.buttonType = "";
				myProposal.config.arguments = [];
				myProposal.config.events = "";
				myProposal.config.content = [];
				break;
			break;

		case 3: // Multiple Proposal Delete
			SageData.Get("gdvProposals").Arr.length = 0;
			myProposal.BindProposalGrid(null, null, null, null, null, null,
					null, null);
			csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
					+ 'Selected proposal(s) has been deleted successfully.'
					+ "</p>");
			break;

		case 4:// For Proposal Edit Action
			myProposal.FillForm(msg);

			// Initialize Appendices content and Uploader
			myProposal.InitializeUploader(msg.appendices);

			$('#divProposalGrid').hide();
			$('#divProposalForm').show();
			$('#divProposalAuditGrid').hide();
			// $("#accordion").accordion("option", "active", 0);
			break;

		case 5: // Bind User List for Investigator Info
			$('select[name="ddlName"]').get(rowIndex).options.length = 0;
			$('select[name="ddlCollege"]').get(rowIndex).options.length = 0;
			$('select[name="ddlDepartment"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
			$('input[name="txtPhoneNo"]').eq(rowIndex).val('');

			$
					.each(
							msg,
							function(item, value) {
								$('select[name="ddlName"]').get(rowIndex).options[$(
										'select[name="ddlName"]').get(rowIndex).options.length] = new Option(
								// value.fullName, value.id);
								value, item);
							});
			break;

		case 6: // Bind User Position Details on dropdown selection change
			positionsDetails = [];
			$.merge(positionsDetails, msg);
			$('select[name="ddlCollege"]').get(rowIndex).options.length = 0;
			$('select[name="ddlDepartment"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
			$('input[name="txtPhoneNo"]').eq(rowIndex).val('');
			break;

		case 7:// Unique Project Title Check
			projectTitleIsUnique = stringToBoolean(msg);
			break;

		case 8:
			$
					.each(
							msg,
							function(index, item) {
								var signedDate = '';
								var readOnly = '';
								var focusMethod = '';
								var allowedChange = false;
								var allowedSign = false;

								if (item.signedDate != null) {
									signedDate = item.signedDate;
								}

								if (GPMS.utils.GetUserProfileID() != item.userProfileId) {
									readOnly = 'readonly="true"';
								} else if (GPMS.utils.GetUserProfileID() == item.userProfileId) {
									// if (item.signature != ""
									// && item.signedDate != null) {
									// readOnly = 'readonly="true"';
									// } else
									if (item.signedDate == null) {
										focusMethod = 'onfocus="myProposal.BindCurrentDateTime(this);" required="true"';
										readOnly = 'required="true"';
										allowedSign = true;
										allowedChange = true;
									}
								}

								// if (readOnly == '') {
								// allowedChange = true;
								// }
								var cloneRow = '<tr allowchange="'
										+ allowedChange
										+ '" allowsign="'
										+ allowedSign
										+ '"><td><span class="cssClassLabel" name="fullname" role="'
										+ item.positionTitle
										+ '" delegated="'
										+ item.delegated
										+ '">'
										+ item.fullName
										+ '</span></td><td><input data-for="signature" data-value="'
										+ item.userProfileId
										+ '" title="'
										+ item.positionTitle
										+ '\'s Signature" class="sfInputbox" placeholder="'
										+ item.positionTitle
										+ '\'s Signature" type="text" value="'
										+ item.signature
										+ '"'
										+ ' name="'
										+ item.userProfileId
										+ item.positionTitle
										+ '" '
										+ readOnly
										+ '>'
										+ '</td><td><input data-for="signaturedate" name="signaturedate'
										+ item.userProfileId
										+ item.positionTitle
										+ '" title="Signed Date" class="sfInputbox" placeholder="Signed Date" type="text" readonly="true" '
										+ focusMethod
										+ ' value="'
										+ $.format.date(signedDate,
												'yyyy/MM/dd hh:mm:ss a')
										+ '"></td><td><textarea rows="2" cols="26" name="proposalNotes'
										+ item.userProfileId
										+ item.positionTitle
										+ '" '
										+ readOnly
										+ ' title="Proposal Notes" class="cssClassTextArea" >'
										+ item.note + '</textarea></td></tr>';

								switch (item.positionTitle) {
								case "PI":
								case "Co-PI":
								case "Senior":
									$(cloneRow).appendTo("#trSignPICOPI tbody");
									break;
								case "Department Chair":
									$(cloneRow).appendTo("#trSignChair tbody");
									break;
								case "Dean":
									$(cloneRow).appendTo("#trSignDean tbody");
									break;
								case "Business Manager":
									$(cloneRow).appendTo(
											"#trSignBusinessManager tbody");
									break;

								case "Research Administrator":
									$(cloneRow).appendTo(
											"#trSignAdministrator tbody");
									break;

								case "University Research Director":
									$(cloneRow).appendTo(
											"#trSignDirector tbody");
									break;
								default:
									break;
								}
							});

			break;

		case 9:
			myProposal.BindProposalGrid(null, null, null, null, null, null,
					null, null);
			$('#divProposalGrid').show();
			myProposal.config.proposalId = '0';
			myProposal.config.proposalRoles = "";
			myProposal.config.buttonType = "";
			myProposal.config.arguments = [];
			myProposal.config.events = "";
			myProposal.config.content = [];

			// $("#accordion").accordion("option", "active", 0);

			if (myProposal.config.proposalId != "0") {
				var changeMade = "Updated";
				switch (myProposal.config.buttonType) {
				case "Update":
					changeMade = "Updated";
					break;

				case "Submit":
					changeMade = "Submitted";
					break;

				case "Approve":
					changeMade = "Approved";
					break;
				case "Disapprove":
					changeMade = "Disapproved";
					break;

				case "Withdraw":
					changeMade = "Withdrawn";
					break;

				case "Archive":
					changeMade = "Archived";
					break;

				default:
					break;
				}
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'Proposal has been ' + changeMade + ' successfully.'
						+ "</p>");
			} else {
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'Proposal has been saved successfully.' + "</p>");
			}
			$('#divProposalForm').hide();
			$('#divProposalAuditGrid').hide();
			// myProposal.CollapseAccordion();
			// myProposal.SelectFirstAccordion();
			break;

		case 10:
			if (myProposal.config.proposalId != '0') {
				myProposal.DeleteProposalById(myProposal.config.proposalId,
						myProposal.config.proposalRoles);
			} else {
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load Proposal.' + '</p>');
			}
			break;

		case 11:
			if (myProposal.config.proposalId != '0') {
				myProposal.SaveProposal(myProposal.config.buttonType,
						myProposal.config.proposalRoles,
						myProposal.config.proposalId, false);
			}

			break;

		case 12:
			if (myProposal.config.proposalId == '0') {
				$('#lblFormHeading').html('New Proposal Details');

				// Initialize Appendices content and Uploader
				myProposal.InitializeUploader("");

				$("#btnReset").show();
				$("#btnSaveProposal").show();
				$("#btnUpdateProposal").hide();
				$("#btnDeleteProposal").hide();

				$("#btnSubmitProposal").show();

				// For Admin user only
				$("#btnApproveProposal").hide();
				$("#btnDisapproveProposal").hide();
				$("#btnWithdrawProposal").hide();
				$("#btnArchiveProposal").hide();

				$('#ui-id-23').hide();
				$('#ui-id-24').find('input, select, textarea').each(function() {
					// $(this).addClass("ignore");
					$(this).prop('disabled', true);
				});

				$('select[name=ddlName]').eq(0).val(
						GPMS.utils.GetUserProfileID()).prop('selected',
						'selected').prop('disabled', 'disabled');

				myProposal.ClearForm();
				myProposal.BindDefaultUserPosition(0);
				myProposal.BindPICoPISignatures();

				$('#divProposalGrid').hide();
				$('#divProposalForm').show();
				$('#divProposalAuditGrid').hide();
				$("#accordion").accordion("option", "active", 0);
			}
			break;

		// Withdraw/ Archive
		case 13:
			if (myProposal.config.proposalId != '0') {
				myProposal.UpdateProposalStatus(myProposal.config.buttonType,
						myProposal.config.proposalId);
			}

			break;

		case 14:
			if (myProposal.config.proposalId != '0') {
				alert("You are allowed to View this Section!");
			}
			break;

		case 15:
			if (myProposal.config.proposalId != '0') {
				alert("You are allowed to Edit this Section!");
				$(myProposal.config.content).find('input, select, textarea')
						.each(function() {
							// $(this).addClass("ignore");
							$(this).prop('disabled', false);
						});
			}
			break;

		case 16:
			if (myProposal.config.proposalId != '0') {
				var argus = myProposal.config.arguments;
				$('#lblLogsHeading').html('View Audit Logs for: ' + argus[1]);

				if (argus[2] != null && argus[2] != "") {
					$('#tblLastAuditedInfo').show();
					$('#lblLastUpdatedOn').html(argus[2]);
					$('#lblLastUpdatedBy').html(argus[3]);
					$('#lblActivity').html(argus[4]);
				} else {
					$('#tblLastAuditedInfo').hide();
				}
				// Get Audit Logs
				// $("#gdvProposalsAuditLog").empty();
				// $("#gdvProposalsAuditLog_Pagination").remove();

				myProposal.BindProposalAuditLogGrid(argus[0], null, null, null,
						null);

				$('#divProposalGrid').hide();
				$('#divProposalForm').hide();
				$('#divProposalAuditGrid').show();
			}
			break;

		case 17:
			if (msg != "No Record") {
				window.location.href = GPMS.utils.GetGPMSServicePath()
						+ 'files/download?fileName=' + msg;
			} else {
				csscody.alert("<h2>" + 'Information Message' + "</h2><p>"
						+ 'No Record found!' + "</p>");
			}
			break;

		}
	},

		ajaxFailure : function(msg) {
			switch (myProposal.config.ajaxCallMode) {
			case 0:
				break;
			case 1:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load Proposal Status.' + '</p>');
				break;
			case 2:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to delete the proposal.' + '</p>');
				break;
			case 3:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to delete multiple proposals.' + '</p>');
				break;
			case 4:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load proposal details.' + '</p>');
				break;
			case 5:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load user list.' + '</p>');
				break;

			case 6:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Failed to load user\'s position details.' + "</p>");
				break;

			case 7:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot check for unique project title' + "</p>");
				break;

			case 8:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot get certification/ signatures information'
						+ "</p>");
				break;

			case 9:
				if (myProposal.config.proposalId != "0") {
					csscody.error("<h2>" + 'Error Message' + "</h2><p>"
							+ 'Failed to update proposal! ' + msg.responseText
							+ "</p>");
				} else {
					csscody.error("<h2>" + 'Error Message' + "</h2><p>"
							+ 'Failed to save proposal! ' + msg.responseText
							+ "</p>");
				}
				break;

			case 10:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to DELETE this proposal! '
						+ msg.responseText + '</p>');
				break;

			case 11:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to perform this OPERATION! '
						+ msg.responseText + '</p>');
				break;

			case 12:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to CREATE a Proposal! '
						+ msg.responseText + '</p>');
				break;

			case 13:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to perform this OPERATION! '
						+ msg.responseText + '</p>');
				break;

			case 14:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not Allowed to View this Section! '
						+ msg.responseText + '</p>');
				myProposal.config.event.preventDefault();
				break;

			case 15:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not Allowed to EDIT this Section! '
						+ msg.responseText + '</p>');
				// myProposal.config.event.preventDefault();
				alert(myProposal.config.content);
				$(myProposal.config.content).find('input, select, textarea')
						.each(function() {
							// $(this).addClass("ignore");
							$(this).prop('disabled', true);
						});
				break;

			case 16:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not Allowed to VIEW Audit Logs! '
						+ msg.responseText + '</p>');
				break;

			case 17:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot create and download Excel report!' + "</p>");
				break;

			}
		},

		ExportToExcel : function(projectTitle, usernameBy, submittedOnFrom,
				submittedOnTo, totalCostsFrom, totalCostsTo, proposalStatus,
				userRole) {
			var proposalBindObj = {
				ProjectTitle : projectTitle,
				UsernameBy : usernameBy,
				SubmittedOnFrom : submittedOnFrom,
				SubmittedOnTo : submittedOnTo,
				TotalCostsFrom : totalCostsFrom,
				TotalCostsTo : totalCostsTo,
				ProposalStatus : proposalStatus,
				UserRole : userRole
			};

			this.config.data = JSON2.stringify({
				proposalBindObj : proposalBindObj,
				gpmsCommonObj : gpmsCommonObj()
			});

			this.config.url = this.config.baseURL + "ProposalsExportToExcel";
			this.config.ajaxCallMode = 17;
			this.ajaxCall(this.config);
			return false;
		},

		LogsExportToExcel : function(proposalId, action, auditedBy,
				activityOnFrom, activityOnTo) {
			var auditLogBindObj = {
				Action : action,
				AuditedBy : auditedBy,
				ActivityOnFrom : activityOnFrom,
				ActivityOnTo : activityOnTo,
			};
			this.config.data = JSON2.stringify({
				proposalId : proposalId,
				auditLogBindObj : auditLogBindObj
			});

			this.config.url = this.config.baseURL + "ProposalLogsExportToExcel";
			this.config.ajaxCallMode = 17;
			this.ajaxCall(this.config);
			return false;
		},

		init : function(config) {
			myProposal.InitializeAccordion();

			// var appendices = [ {
			// "filename" : "one.pdf",
			// "extension" : "pdf",
			// "filepath" : "uploads\one.pdf",
			// "filesize" : "82393"
			// }, {
			// "filename" : "two.jpg",
			// "extension" : "jpg",
			// "filepath" : "uploads\two.jpg",
			// "filesize" : "82393"
			// } ];

			// myProposal.InitializeUploader(appendices);

			$('#btnLogsBack').on("click", function() {
				$('#divProposalGrid').show();
				$('#divProposalForm').hide();
				$('#divProposalAuditGrid').hide();
				myProposal.config.proposalId = '0';
				myProposal.config.proposalRoles = "";
				myProposal.config.buttonType = "";
				myProposal.config.arguments = [];
				myProposal.config.events = "";
				myProposal.config.content = [];
			});

			$("#txtSearchSubmittedOnFrom").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						onSelect : function(selectedDate) {
							$("#txtSearchSubmittedOnTo").datepicker("option",
									"minDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});
			$("#txtSearchSubmittedOnTo").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						onSelect : function(selectedDate) {
							$("#txtSearchSubmittedOnFrom").datepicker("option",
									"maxDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});
			myProposal.BindProposalGrid(null, null, null, null, null, null,
					null, null);
			$('#divProposalForm').hide();
			$('#divProposalGrid').show();
			$('#divProposalAuditGrid').hide();

			// For Filling Form
			$("#txtDueDate").datepicker({
				dateFormat : 'yy-mm-dd',
				changeMonth : true,
				changeYear : true
			}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});

			$("#txtProjectPeriodFrom").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						onSelect : function(selectedDate) {
							$("#txtProjectPeriodTo").datepicker("option",
									"minDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});
			$("#txtProjectPeriodTo").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						onSelect : function(selectedDate) {
							$("#txtProjectPeriodFrom").datepicker("option",
									"maxDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});

			myProposal.BindProposalStatus();

			// myProposal.BindAllUsersAndPositions();

			myProposal.BindUserDropDown();

			// Form Position details Drop downs
			$('select[name="ddlName"]').on("change", function() {
				rowIndex = $(this).closest('tr').prevAll("tr").length;
				if ($(this).val() != "0") {
					myProposal.BindDefaultUserPosition(rowIndex);
				} else {
					$(this).find('option:gt(0)').remove();
				}
			});

			$('select[name="ddlCollege"]').on(
					"change",
					function() {
						rowIndex = $(this).closest('tr').prevAll("tr").length;
						if ($(this).val() != "0") {
							myProposal.BindDepartmentDropDown($(
									'select[name="ddlName"]').eq(rowIndex)
									.val(), $(this).val());
							myProposal.BindPositionTypeDropDown($(
									'select[name="ddlName"]').eq(rowIndex)
									.val(), $(this).val(), $(
									'select[name="ddlDepartment"]')
									.eq(rowIndex).val());
							myProposal.BindPositionTitleDropDown($(
									'select[name="ddlName"]').eq(rowIndex)
									.val(), $(this).val(), $(
									'select[name="ddlDepartment"]')
									.eq(rowIndex).val(), $(
									'select[name="ddlPositionType"]').eq(
									rowIndex).val());
						} else {
							$(this).find('option:gt(0)').remove();
						}
					});

			$('select[name="ddlDepartment"]')
					.on(
							"change",
							function() {
								rowIndex = $(this).closest('tr').prevAll("tr").length;
								if ($('select[name="ddlCollege"]').eq(rowIndex)
										.val() != "0"
										&& $(this).val() != "0") {
									myProposal.BindPositionTypeDropDown($(
											'select[name="ddlName"]').eq(
											rowIndex).val(), $(
											'select[name="ddlCollege"]').eq(
											rowIndex).val(), $(this).val());
									myProposal.BindPositionTitleDropDown($(
											'select[name="ddlName"]').eq(
											rowIndex).val(), $(
											'select[name="ddlCollege"]').eq(
											rowIndex).val(), $(this).val(), $(
											'select[name="ddlPositionType"]')
											.eq(rowIndex).val());
								} else {
									$('select[name="ddlPositionType"]').find(
											'option:gt(0)').remove();
								}
							});

			$('select[name="ddlPositionType"]')
					.on(
							"change",
							function() {
								rowIndex = $(this).closest('tr').prevAll("tr").length;
								if ($('select[name="ddlCollege"]').eq(rowIndex)
										.val() != "0"
										&& $('select[name="ddlDepartment"]')
												.eq(rowIndex).val() != "0"
										&& $(this).val() != "0") {
									myProposal.BindPositionTitleDropDown($(
											'select[name="ddlName"]').eq(
											rowIndex).val(), $(
											'select[name="ddlCollege"]').eq(
											rowIndex).val(), $(
											'select[name="ddlDepartment"]').eq(
											rowIndex).val(), $(this).val());
								} else {
									$('select[name="ddlPositionTitle"]').find(
											'option:gt(0)').remove();
								}

							});

			// unused
			$('#btnDeleteSelected')
					.click(
							function() {
								var proposal_ids = '';
								proposal_ids = SageData.Get("gdvProposals").Arr
										.join(',');

								if (proposal_ids.length > 10) {
									var properties = {
										onComplete : function(e) {
											myProposal.ConfirmDeleteMultiple(
													proposal_ids, e);
										}
									};
									csscody
											.confirm(
													"<h2>"
															+ 'Delete Confirmation'
															+ "</h2><p>"
															+ 'Are you certain you want to delete selected proposal(s)?'
															+ "</p>",
													properties);
								} else {
									csscody
											.alert('<h2>'
													+ 'Information Alert'
													+ '</h2><p>'
													+ 'Please select at least one proposal before deleting.'
													+ '</p>');
								}
							});

			$('#btnAddNew').on(
					"click",
					function() {
						myProposal.config.ajaxCallMode = 12;

						myProposal.CheckUserPermissionWithPositionType(
								"Create", "Whole Proposal", myProposal.config);

					});

			$("#btnLogsExportToExcel").on(
					"click",
					function() {
						var action = $.trim($("#txtSearchAction").val());
						if (action.length < 1) {
							action = null;
						}

						var auditedBy = $.trim($("#txtSearchAuditedBy").val());
						if (auditedBy.length < 1) {
							auditedBy = null;
						}

						var activityOnFrom = $.trim($(
								"#txtSearchActivityOnFrom").val());
						if (activityOnFrom.length < 1) {
							activityOnFrom = null;
						}

						var activityOnTo = $.trim($("#txtSearchActivityOnTo")
								.val());
						if (activityOnTo.length < 1) {
							activityOnTo = null;
						}

						myProposal.LogsExportToExcel(
								myProposal.config.proposalId, action,
								auditedBy, activityOnFrom, activityOnTo);
					});

			$("#btnExportToExcel")
					.on(
							"click",
							function() {

								var projectTitle = $.trim($(
										"#txtSearchProjectTitle").val());
								var usernameBy = $.trim($("#txtSearchUserName")
										.val());
								var submittedOnFrom = $.trim($(
										"#txtSearchSubmittedOnFrom").val());
								var submittedOnTo = $.trim($(
										"#txtSearchSubmittedOnTo").val());
								var totalCostsFrom = $.trim($(
										"#txtSearchTotalCostsFrom")
										.autoNumeric('get'));
								var totalCostsTo = $.trim($(
										"#txtSearchTotalCostsTo").autoNumeric(
										'get'));

								var proposalStatus = $.trim($(
										'#ddlSearchProposalStatus').val()) == "" ? null
										: $.trim($('#ddlSearchProposalStatus')
												.val()) == "0" ? null
												: $
														.trim($(
																'#ddlSearchProposalStatus')
																.val());

								var userRole = $.trim($('#ddlSearchUserRole')
										.val()) == "" ? null
										: $.trim($('#ddlSearchUserRole').val()) == "0" ? null
												: $
														.trim($(
																'#ddlSearchUserRole')
																.val());

								if (projectTitle.length < 1) {
									projectTitle = null;
								}
								if (usernameBy.length < 1) {
									usernameBy = null;
								}
								if (totalCostsFrom.length < 1) {
									totalCostsFrom = null;
								}
								if (totalCostsTo.length < 1) {
									totalCostsTo = null;
								}
								if (submittedOnFrom.length < 1) {
									submittedOnFrom = null;
								}
								if (submittedOnTo.length < 1) {
									submittedOnTo = null;
								}

								myProposal.ExportToExcel(projectTitle,
										usernameBy, submittedOnFrom,
										submittedOnTo, totalCostsFrom,
										totalCostsTo, proposalStatus, userRole);
							});

			$('#btnBack').on("click", function() {
				$('#divProposalGrid').show();
				$('#divProposalForm').hide();
				$('#divProposalAuditGrid').hide();
				myProposal.config.proposalId = '0';
				myProposal.config.proposalRoles = "";
				myProposal.config.buttonType = "";
				myProposal.config.arguments = [];
				myProposal.config.events = "";
				myProposal.config.content = [];

				// $("#accordion").accordion("option", "active", 0);
			});

			$('#btnReset').on("click", function() {
				myProposal.ClearForm();
				myProposal.BindDefaultUserPosition(0);
				myProposal.BindPICoPISignatures();
				// $("#accordion").accordion("option", "active", 0);
			});

			// Save As Draft
			$('#btnSaveProposal').click(function(e) {
				if (validator.form()) {
					var $buttonType = $.trim($(this).text());
					$(this).disableWith('Saving As Draft...');

					myProposal.SaveProposal($buttonType, "", "0", true);

					$(this).enableAgain();
					e.preventDefault();
					return false;
				} else {
					myProposal.focusTabWithErrors("#accordion");
				}
			});

			// Update
			$('#btnUpdateProposal').click(
					function(e) {
						if (validator.form()) {
							var $buttonType = $.trim($(this).text());
							$(this).disableWith('Updating...');

							myProposal.config.ajaxCallMode = 11;

							if (myProposal.config.proposalRoles != "") {
								myProposal.CheckUserPermissionWithProposalRole(
										$buttonType,
										myProposal.config.proposalRoles,
										myProposal.config.proposalId,
										"Whole Proposal", myProposal.config);
							}

							$(this).enableAgain();
							e.preventDefault();
							return false;
						} else {
							myProposal.focusTabWithErrors("#accordion");
						}
					});

			// Delete
			$('#btnDeleteProposal').click(
					function(e) {
						if (validator.form()) {
							var $buttonType = $.trim($(this).text());
							$(this).disableWith('Deleting...');

							myProposal.config.ajaxCallMode = 10;

							if (myProposal.config.proposalRoles != "") {
								myProposal.CheckUserPermissionWithProposalRole(
										$buttonType,
										myProposal.config.proposalRoles,
										myProposal.config.proposalId,
										"Whole Proposal", myProposal.config);
							} else {
								myProposal
										.CheckUserPermissionWithPositionTitle(
												$buttonType,
												myProposal.config.proposalId,
												"Whole Proposal",
												myProposal.config);
							}

							$(this).enableAgain();
							e.preventDefault();
							return false;
						} else {
							myProposal.focusTabWithErrors("#accordion");
						}
					});

			// Submit
			$('#btnSubmitProposal').click(
					function(e) {
						if (validator.form()) {
							var $buttonType = $.trim($(this).text());
							$(this).disableWith('Submitting...');

							myProposal.config.ajaxCallMode = 11;

							if (myProposal.config.proposalRoles != "") {
								myProposal.CheckUserPermissionWithProposalRole(
										$buttonType,
										myProposal.config.proposalRoles,
										myProposal.config.proposalId,
										"Whole Proposal", myProposal.config);
							} else {
								myProposal
										.CheckUserPermissionWithPositionTitle(
												$buttonType,
												myProposal.config.proposalId,
												"Whole Proposal",
												myProposal.config);
							}

							$(this).enableAgain();
							e.preventDefault();
							return false;
						} else {
							myProposal.focusTabWithErrors("#accordion");
						}
					});

			// Approve
			$('#btnApproveProposal').click(
					function(e) {
						if (validator.form()) {
							var $buttonType = $.trim($(this).text());
							$(this).disableWith('Approving...');

							myProposal.config.ajaxCallMode = 11;

							myProposal.CheckUserPermissionWithPositionTitle(
									$buttonType, myProposal.config.proposalId,
									"Whole Proposal", myProposal.config);

							$(this).enableAgain();
							e.preventDefault();
							return false;
						} else {
							myProposal.focusTabWithErrors("#accordion");
						}
					});

			// Disapprove
			$('#btnDisapproveProposal').click(
					function(e) {
						if (validator.form()) {
							var $buttonType = $.trim($(this).text());
							$(this).disableWith('Disapproving...');

							myProposal.config.ajaxCallMode = 11;

							myProposal.CheckUserPermissionWithPositionTitle(
									$buttonType, myProposal.config.proposalId,
									"Whole Proposal", myProposal.config);

							$(this).enableAgain();
							e.preventDefault();
							return false;
						} else {
							myProposal.focusTabWithErrors("#accordion");
						}
					});

			// Withdraw
			$('#btnWithdrawProposal').click(
					function(e) {
						if (validator.form()) {
							var $buttonType = $.trim($(this).text());
							$(this).disableWith('Withdrawing...');

							myProposal.config.ajaxCallMode = 13;

							myProposal.CheckUserPermissionWithPositionTitle(
									$buttonType, myProposal.config.proposalId,
									"Whole Proposal", myProposal.config);

							$(this).enableAgain();
							e.preventDefault();
							return false;
						} else {
							myProposal.focusTabWithErrors("#accordion");
						}
					});

			// Archive
			$('#btnArchiveProposal').click(
					function(e) {
						if (validator.form()) {
							var $buttonType = $.trim($(this).text());
							$(this).disableWith('Archiving...');

							myProposal.config.ajaxCallMode = 13;

							myProposal.CheckUserPermissionWithPositionTitle(
									$buttonType, myProposal.config.proposalId,
									"Whole Proposal", myProposal.config);

							$(this).enableAgain();
							e.preventDefault();
							return false;
						} else {
							myProposal.focusTabWithErrors("#accordion");
						}
					});

			$('#txtProjectTitle').on("focus", function() {
				$(this).siblings('.cssClassRight').hide();
			}), $('#txtProjectTitle').on(
					"blur",
					function() {
						var projectTitle = $.trim($(this).val());

						myProposal.checkUniqueProjectTitle(
								myProposal.config.proposalId, projectTitle,
								$(this));
						return false;
					});

			$("input[type=button].AddOption")
					.on(
							"click",
							function() {
								if ($(this).prop("name") == "DeleteOption") {
									var t = $(this).closest('tr');

									t.find("td").wrapInner(
											"<div style='display: block'/>")
											.parent().find("td div").slideUp(
													300, function() {
														t.remove();
													});

								} else if ($(this).prop("name") == "AddMore") {
									var cloneRow = $(this).closest('tr').clone(
											true);
									$(cloneRow).find("input").each(
											function(i) {
												if ($(this).is(".AddOption")) {
													$(this).prop("name",
															"DeleteOption");
													$(this).prop("value",
															"Delete ");
													$(this).prop("title",
															"Delete");
												}
											});
									$(cloneRow)
											.find("select")
											.each(
													function(j) {
														$(this).removeAttr(
																"disabled");
														$(this).removeAttr(
																"required");
														// Remove PI option
														// after first row
														if (j == 0) {
															$(this)
																	.find(
																			'option:first')
																	.remove();
														} else if (j == 1) {
															$(this).prop(
																	"disabled",
																	false);
															$('#ui-id-2')
																	.find(
																			"select[name='ddlName']")
																	.each(
																			function(
																					k) {
																				$(
																						cloneRow)
																						.find(
																								'option[value='
																										+ $(
																												this)
																												.val()
																										+ ']')
																						.remove();
																			});
														}
														$(this)
																.find("option")
																.removeAttr(
																		"selected");
													});

									if ($(cloneRow).find(
											"select[name='ddlName'] option").length > 0) {
										$('#dataTable tr:last').find(
												"select[name='ddlName']").prop(
												"disabled", true);

										$(cloneRow).appendTo("#dataTable")
												.hide().fadeIn(1200);

										rowIndex = $('#dataTable > tbody tr')
												.size() - 1;
										myProposal
												.BindDefaultUserPosition(rowIndex);
									}
								}
							});

			$("#btnSearchProposal").on("click", function() {
				// if ($("#form1").valid()) {
				myProposal.SearchProposals();
				// }
				return false;
			});

			$("#btnSearchProposalAuditLog").on("click", function() {
				myProposal.SearchProposalAuditLogs();
				return false;
			});

			$("#ddlInstitutionalCommitmentCost").on("change", function() {
				if ($("#ddlInstitutionalCommitmentCost").val() == "1") {
					$("#lblConfirmCommitment").show();
				} else {
					$("#lblConfirmCommitment").hide();
				}
			});

			$("#ddlInstitutionalCommitmentsRequired").on("change", function() {
				if ($("#ddlInstitutionalCommitmentsRequired").val() == "1") {
					$("#lblCommitmentsRequired").show();
				} else {
					$("#lblCommitmentsRequired").hide();
				}
			});

			$("#ddlDisclosedFinancialCOI").on("change", function() {
				if ($("#ddlDisclosedFinancialCOI").val() == "1") {
					$("#lblDisclosureRequired").show();
				} else {
					$("#lblDisclosureRequired").hide();
				}
			});

			$("#ddlMaterialChanged").on("change", function() {
				if ($("#ddlMaterialChanged").val() == "1") {
					$("#lblMaterialChanged").show();
				} else {
					$("#lblMaterialChanged").hide();
				}
			});

			$("#ddlUseHumanSubjects").on("change", function() {
				if ($("#ddlUseHumanSubjects").val() == "1") {
					$("#ddlIRBOptions").removeClass("ignore");
					$("#lblUseHumanSubjects").show();
					// $("#ddlIRBOptions").prop("selectedIndex", 0);
					$("#tdHumanSubjectsOption").show();
					$("#tdIRBOption").show();
					if ($("#ddlIRBOptions").val() == "1") {
						// $("#txtIRB").val('');
						$("#txtIRB").removeClass("ignore");
						$("#tdIRBtxt").show();
					} else {
						// $("#txtIRB").val('');
						$("#txtIRB").addClass("ignore");
						$("#tdIRBtxt").hide();
					}
				} else {
					$("#ddlIRBOptions").addClass("ignore");
					$("#txtIRB").addClass("ignore");
					$("#lblUseHumanSubjects").hide();
					// $("#ddlIRBOptions").prop("selectedIndex", 0);
					$("#tdHumanSubjectsOption").hide();
					$("#tdIRBOption").hide();
					$("#tdIRBtxt").hide();
				}
			});

			$("#ddlIRBOptions").on("change", function() {
				if ($("#ddlIRBOptions").val() == "1") {
					// $("#txtIRB").val('');
					$("#txtIRB").removeClass("ignore");
					$("#tdIRBtxt").show();
				} else {
					// $("#txtIRB").val('');
					$("#txtIRB").addClass("ignore");
					$("#tdIRBtxt").hide();
				}
			});

			$("#ddlUseVertebrateAnimals").on("change", function() {
				if ($("#ddlUseVertebrateAnimals").val() == "1") {
					$("#ddlIACUCOptions").removeClass("ignore");
					$("#lblUseVertebrateAnimals").show();
					// $("#ddlIACUCOptions").prop("selectedIndex", 0);
					$("#tdVertebrateAnimalsOption").show();
					$("#tdIACUCOption").show();
					if ($("#ddlIACUCOptions").val() == "1") {
						// $("#txtIACUC").val('');
						$("#txtIACUC").removeClass("ignore");
						$("#tdIACUCtxt").show();
					} else {
						// $("#txtIACUC").val('');
						$("#txtIACUC").addClass("ignore");
						$("#tdIACUCtxt").hide();
					}
				} else {
					$("#ddlIACUCOptions").addClass("ignore");
					$("#txtIACUC").addClass("ignore");
					$("#lblUseVertebrateAnimals").hide();
					// $("#ddlIACUCOptions").prop("selectedIndex", 0);
					$("#tdVertebrateAnimalsOption").hide();
					$("#tdIACUCOption").hide();
					$("#tdIACUCtxt").hide();
				}
			});

			$("#ddlIACUCOptions").on("change", function() {
				if ($("#ddlIACUCOptions").val() == "1") {
					// $("#txtIACUC").val('');
					$("#txtIACUC").removeClass("ignore");
					$("#tdIACUCtxt").show();
				} else {
					// $("#txtIACUC").val('');
					$("#txtIACUC").addClass("ignore");
					$("#tdIACUCtxt").hide();
				}
			});

			$("#ddlInvovleBioSafety").on("change", function() {
				if ($("#ddlInvovleBioSafety").val() == "1") {
					$("#ddlIBCOptions").removeClass("ignore");
					$("#lblHasBiosafetyConcerns").show();
					// $("#ddlIBCOptions").prop("selectedIndex", 0);
					$("#tdBiosafetyOption").show();
					$("#tdIBCOption").show();
					if ($("#ddlIBCOptions").val() == "1") {
						// $("#txtIBC").val('');
						$("#txtIBC").removeClass("ignore");
						$("#tdIBCtxt").show();
					} else {
						// $("#txtIBC").val('');
						$("#txtIBC").addClass("ignore");
						$("#tdIBCtxt").hide();
					}
				} else {
					$("#ddlIBCOptions").addClass("ignore");
					$("#txtIBC").addClass("ignore");
					$("#lblHasBiosafetyConcerns").hide();
					// $("#ddlIBCOptions").prop("selectedIndex", 0);
					$("#tdBiosafetyOption").hide();
					$("#tdIBCOption").hide();
					$("#tdIBCtxt").hide();
				}
			});

			$("#ddlIBCOptions").on("change", function() {
				if ($("#ddlIBCOptions").val() == "1") {
					// $("#txtIBC").val('');
					$("#txtIBC").removeClass("ignore");
					$("#tdIBCtxt").show();
				} else {
					// $("#txtIBC").val('');
					$("#txtIBC").addClass("ignore");
					$("#tdIBCtxt").hide();
				}
			});

			$("#ddlInvolveNonFundedCollabs").on("change", function() {
				if ($("#ddlInvolveNonFundedCollabs").val() == "1") {
					$("#txtCollaborators").removeClass("ignore");
					$("#lblInvolveNonFundedCollabs").show();
					$("#trInvolveNonFundedCollabs").show();
				} else {
					$("#txtCollaborators").addClass("ignore");
					$("#lblInvolveNonFundedCollabs").hide();
					$("#trInvolveNonFundedCollabs").hide();
				}
			});

			$("#ddlProprietaryInformation").on("change", function() {
				if ($("#ddlProprietaryInformation").val() == "1") {
					$("#txtPagesWithProprietaryInfo").removeClass("ignore");
					$("#tdPagesWithProprietaryInfo").show();
					$("#trTypeOfProprietaryInfo").show();
				} else {
					$("#txtPagesWithProprietaryInfo").addClass("ignore");
					$("#tdPagesWithProprietaryInfo").hide();
					$("#trTypeOfProprietaryInfo").hide();
				}
			});

			$("#ddlPISalaryIncluded").on("change", function() {
				if ($("#ddlPISalaryIncluded").val() == "2") {
					$("#lblPISalaryIncluded").show();
				} else {
					$("#lblPISalaryIncluded").hide();
				}
			});

			$("#ddlSubrecipients").on("change", function() {
				if ($("#ddlSubrecipients").val() == "1") {
					$("#txtNamesSubrecipients").removeClass("ignore");
					$("#trSubrecipientsNames").show();
				} else {
					$("#txtNamesSubrecipients").addClass("ignore");
					$("#trSubrecipientsNames").hide();
				}
			});

			$("#txtDOB").datepicker({
				dateFormat : 'yy-mm-dd',
				changeMonth : true,
				changeYear : true
			}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});

			$("#txtSearchActivityOnFrom").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						maxDate : 0,
						onSelect : function(selectedDate) {
							$("#txtSearchActivityOnTo").datepicker("option",
									"minDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});
			$("#txtSearchActivityOnTo").datepicker({
				dateFormat : 'yy-mm-dd',
				changeMonth : true,
				changeYear : true,
				maxDate : 0
			}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});

			$("#txtBusinesManagerDate").datepicker({
				dateFormat : 'yy-mm-dd',
				changeMonth : true,
				changeYear : true
			}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});

			$("#txtSearchTotalCostsFrom").autoNumeric('init', {
				aSep : ',',
				dGroup : '3',
				aDec : '.',
				aSign : '$',
				pSign : 'p',
				aPad : true
			// vMin : "1.00"
			});

			$("#txtSearchTotalCostsTo").autoNumeric('init', {
				aSep : ',',
				dGroup : '3',
				aDec : '.',
				aSign : '$',
				pSign : 'p',
				aPad : true
			// vMin : "1.00"
			});

			$("#txtDirectCosts").autoNumeric('init', {
				aSep : ',',
				dGroup : '3',
				aDec : '.',
				aSign : '$',
				pSign : 'p',
				aPad : true
			});
			$("#txtFACosts").autoNumeric('init', {
				aSep : ',',
				dGroup : '3',
				aDec : '.',
				aSign : '$',
				pSign : 'p',
				aPad : true
			});
			$("#txtTotalCosts").autoNumeric('init', {
				aSep : ',',
				dGroup : '3',
				aDec : '.',
				aSign : '$',
				pSign : 'p',
				aPad : true
			});

			$("#txtFARate").autoNumeric('init', {
				aDec : '.',
				aSign : ' %',
				pSign : 's',
				aPad : true,
				vMin : "0.00",
				vMax : "99.99"
			});

			$("#txtPISalary").autoNumeric('init', {
				aSep : ',',
				dGroup : '3',
				aDec : '.',
				aSign : '$',
				pSign : 'p',
				aPad : true
			});

			$("#txtPIFringe").autoNumeric('init', {
				aSep : ',',
				dGroup : '3',
				aDec : '.',
				aSign : '$',
				pSign : 'p',
				aPad : true
			});

			$(
					'#txtSearchProjectTitle,#txtSearchUserName,#txtSearchSubmittedOnFrom,#txtSearchSubmittedOnTo,#txtSearchTotalCostsFrom,#txtSearchTotalCostsTo,#ddlSearchProposalStatus',
					'#ddlSearchUserRole').keyup(function(event) {
				if (event.keyCode == 13) {
					$("#btnSearchProposal").click();
				}
			});

			$(
					'#txtSearchAction, #txtSearchAuditedBy, #txtSearchActivityOnFrom, #txtSearchActivityOnTo')
					.keyup(function(event) {
						if (event.keyCode == 13) {
							$("#btnSearchUserAuditLog").click();
						}
					});
		}
	};
	myProposal.init();
});