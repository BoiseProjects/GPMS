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
			proposalStatus : "",
			submittedByPI : "",
			readyForSubmitionByPI : "",
			deletedByPI : "",
			chairApproval : "",
			businessManagerApproval : "",
			irbapproval : "",
			deanApproval : "",
			researchAdministratorApproval : "",
			researchAdministratorWithdraw : "",
			researchDirectorApproval : "",
			researchDirectorDeletion : "",
			researchAdministratorSubmission : "",
			researchDirectorArchived : "",
			buttonType : "",
			arguments : [],
			events : "",
			content : "",
			uploadObj : "",
			investigatorButton : ""
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
				attributeName : "position.type",
				attributeValue : GPMS.utils.GetUserPositionType()
			});

			attributeArray.push({
				attributeType : "Resource",
				attributeName : "proposal.section",
				attributeValue : proposalSection
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal.action",
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
				attributeName : "position.title",
				attributeValue : GPMS.utils.GetUserPositionTitle()
			});

			attributeArray.push({
				attributeType : "Resource",
				attributeName : "proposal.section",
				attributeValue : proposalSection
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal.action",
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
				attributeName : "proposal.role",
				attributeValue : proposal_roles
			});

			attributeArray.push({
				attributeType : "Resource",
				attributeName : "proposal.section",
				attributeValue : proposalSection
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal.action",
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

		CheckUserPermissionWithProposalRoleEditSection : function(buttonType,
				proposal_roles, proposal_id, proposalSection, config) {
			var attributeArray = [];

			var currentProposalRoles = config.proposalRoles.split(', ');
			if (currentProposalRoles != ""
					&& ($.inArray("PI", currentProposalRoles) !== -1 || (($
							.inArray("Co-PI", currentProposalRoles) !== -1 || $
							.inArray("Senior Personnel", currentProposalRoles) !== -1) && config.readyForSubmitionByPI == "False"))) {
				attributeArray.push({
					attributeType : "Subject",
					attributeName : "proposal.role",
					attributeValue : config.proposalRoles
				});

				attributeArray.push({
					attributeType : "Resource",
					attributeName : "SubmittedByPI",
					attributeValue : config.submittedByPI
				});

				attributeArray.push({
					attributeType : "Resource",
					attributeName : "DeletedByPI",
					attributeValue : config.deletedByPI
				});

				if ($.inArray("Co-PI", currentProposalRoles) !== -1
						&& config.readyForSubmitionByPI == "False") {
					attributeArray.push({
						attributeType : "Resource",
						attributeName : "ReadyForSubmissionByPI",
						attributeValue : config.readyForSubmitionByPI
					});
				}
			} else {
				var currentPositionTitle = GPMS.utils.GetUserPositionTitle();

				attributeArray.push({
					attributeType : "Subject",
					attributeName : "position.title",
					attributeValue : currentPositionTitle
				});

				switch (currentPositionTitle) {
				case "Department Chair":
					attributeArray.push({
						attributeType : "Resource",
						attributeName : "ApprovedByDepartmentChair",
						attributeValue : config.chairApproval
					});
					break;
				case "Business Manager":
					attributeArray.push({
						attributeType : "Resource",
						attributeName : "ApprovedByBusinessManager",
						attributeValue : config.businessManagerApproval
					});
					break;
				case "IRB":
					attributeArray.push({
						attributeType : "Resource",
						attributeName : "ApprovedByIRB",
						attributeValue : config.irbapproval
					});
					break;
				case "Dean":
					attributeArray.push({
						attributeType : "Resource",
						attributeName : "ApprovedByDean",
						attributeValue : config.deanApproval
					});
					break;
				case "University Research Administrator":
					attributeArray
							.push({
								attributeType : "Resource",
								attributeName : "ApprovedByUniversityResearchAdministrator",
								attributeValue : config.researchAdministratorApproval
							});
					break;
				case "University Research Director":
					attributeArray.push({
						attributeType : "Resource",
						attributeName : "ApprovedByUniversityResearchDirector",
						attributeValue : config.researchDirectorApproval
					});
					break;
				default:
					break;
				}
			}

			attributeArray.push({
				attributeType : "Resource",
				attributeName : "proposal.section",
				attributeValue : proposalSection
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal.action",
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

		CheckUserPermissionForInvestigator : function(buttonType,
				proposalSection, config) {
			var attributeArray = [];
			var currentProposalRoles = config.proposalRoles.split(', ');
			if (($.inArray("PI", currentProposalRoles) !== -1 || ($.inArray(
					"Co-PI", currentProposalRoles) !== -1 && config.readyForSubmitionByPI == "False"))
					&& config.submittedByPI == "NOTSUBMITTED") {
				attributeArray.push({
					attributeType : "Subject",
					attributeName : "proposal.role",
					attributeValue : config.proposalRoles
				});

				attributeArray.push({
					attributeType : "Resource",
					attributeName : "SubmittedByPI",
					attributeValue : config.submittedByPI
				});

				attributeArray.push({
					attributeType : "Resource",
					attributeName : "DeletedByPI",
					attributeValue : config.deletedByPI
				});

				if ($.inArray("Co-PI", currentProposalRoles) !== -1
						&& config.readyForSubmitionByPI == "False") {
					attributeArray.push({
						attributeType : "Resource",
						attributeName : "ReadyForSubmissionByPI",
						attributeValue : config.readyForSubmitionByPI
					});
				}
			}

			attributeArray.push({
				attributeType : "Resource",
				attributeName : "proposal.section",
				attributeValue : proposalSection
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal.action",
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
								colModel : [
										{
											display : 'Proposal ID',
											cssclass : 'cssClassHeadCheckBox',
											coltype : 'checkbox',
											align : 'center',
											checkFor : '23',
											elemClass : 'attrChkbox',
											elemDefault : false,
											controlclass : 'attribHeaderChkbox'
										},
										{
											display : 'Project Title',
											name : 'project_title',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left'
										},
										{
											display : 'Project Type',
											name : 'project_type',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Type of Request',
											name : 'type_of_request',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'array',
											hide : true
										},
										{
											display : 'Project Location',
											name : 'project_location',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Granting Agencies',
											name : 'granting_agencies',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'array'
										},
										{
											display : 'Direct Costs',
											name : 'directCosts',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'currency',
											hide : true
										},
										{
											display : 'FA Costs',
											name : 'FA_costs',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'currency',
											hide : true
										},
										{
											display : 'Total Costs',
											name : 'total_costs',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'currency'
										},
										{
											display : 'FA Rate',
											name : 'FA_rate',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'percent',
											hide : true
										},
										{
											display : 'Date Created',
											name : 'date_created',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'date',
											format : 'yyyy/MM/dd hh:mm:ss a',
											hide : true
										},
										{
											display : 'Date Submitted',
											name : 'date_submitted',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'date',
											format : 'yyyy/MM/dd hh:mm:ss a'
										},
										{
											display : 'Due Date',
											name : 'due_date',
											cssclass : 'cssClassHeadDate',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'date',
											format : 'yyyy/MM/dd hh:mm:ss a'
										},
										{
											display : 'Project Period From',
											name : 'project_period_from',
											cssclass : 'cssClassHeadDate',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'date',
											format : 'yyyy/MM/dd hh:mm:ss a',
											hide : true
										},
										{
											display : 'Project Period To',
											name : 'project_period_to',
											cssclass : 'cssClassHeadDate',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'date',
											format : 'yyyy/MM/dd hh:mm:ss a',
											hide : true
										},
										{
											display : 'Last Audited',
											name : 'last_audited',
											cssclass : 'cssClassHeadDate',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'date',
											format : 'yyyy/MM/dd hh:mm:ss a',
											hide : true
										},
										{
											display : 'Last Audited By',
											name : 'last_audited_by',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Last Audited Action',
											name : 'last_audited_action',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'PI User',
											name : 'pi_user',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Co-PI Users',
											name : 'co_pi_users',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'array',
											hide : true
										},
										{
											display : 'Senior Personnel Users',
											name : 'senior_personnel_users',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'array',
											hide : true
										},
										{
											display : 'All Involved Users',
											name : 'all_users',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'array',
											hide : true
										},
										{
											display : 'Your Role',
											name : 'proposal_roles',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'array'
										},
										{
											display : 'Is Deleted?',
											name : 'is_deleted',
											cssclass : 'cssClassHeadBoolean',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'boolean',
											format : 'Yes/No'
										},
										{
											display : 'Status',
											name : 'proposal_status',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'array'
										},
										{
											display : 'Submitted by PI',
											name : 'submitted_by_PI',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Ready for Submission by PI',
											name : 'ready_for_submission_by_PI',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'boolean',
											format : 'True/False',
											hide : true
										},
										{
											display : 'Deleted by PI',
											name : 'deleted_by_PI',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Chair Approval',
											name : 'chair_approval',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Business Manager Approval',
											name : 'business_manager_approval',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'IRB Approval',
											name : 'IRB_approval',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Dean Approval',
											name : 'dean_approval',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Research Administrator Approval',
											name : 'research_administrator_approval',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Withdrawn by Research Administrator',
											name : 'withdrawn_by_research_administrator',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Research Director Approval',
											name : 'research_director_approval',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Deleted by Research Director',
											name : 'deleted_by_research_director',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Submitted by Research Administrator',
											name : 'submitted_by_research_administrator',
											cssclass : 'cssClassHeadBoolean',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										},
										{
											display : 'Archived by Research Director',
											name : 'archived_by_research_director',
											cssclass : '',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											hide : true
										}, {
											display : 'IRB Approval Required?',
											name : 'irb_approval_required',
											cssclass : 'cssClassHeadBoolean',
											controlclass : '',
											coltype : 'label',
											align : 'left',
											type : 'boolean',
											format : 'True/False',
											hide : true
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
											arguments : '1, 5, 10, 21, 22, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38'
										},
										{
											display : 'Delete',
											name : 'delete',
											enable : true,
											_event : 'click',
											trigger : '2',
											callMethod : 'myProposal.DeleteProposal',
											arguments : '22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37'
										},
										{
											display : 'View Change Logs',
											name : 'changelog',
											enable : true,
											_event : 'click',
											trigger : '3',
											callMethod : 'myProposal.ViewChangeLogs',
											arguments : '1, 15, 16, 17, 22'
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
									39 : {
										sorter : false
									}
								}
							});
		},

		ButtonHideShow : function(config) {
			$("#btnReset").hide();
			$("#btnSaveProposal").hide();
			$("#btnDeleteProposal").hide();
			$("#btnSubmitProposal").hide();
			$("#btnApproveProposal").hide();
			$("#btnDisapproveProposal").hide();
			$("#btnWithdrawProposal").hide();
			$("#btnArchiveProposal").hide();

			var currentPositionTitle = GPMS.utils.GetUserPositionTitle();

			var proposalStatus = config.proposalStatus;

			// if ($.inArray("NOTSUBMITTEDBYPI", proposalStatus) !== -1) {
			// $("#btnSubmitProposal").show();
			// } else {
			// $("#btnSubmitProposal").hide();
			// }

			var currentProposalRoles = config.proposalRoles.split(', ');

			if (proposalStatus != ""
					&& (($.inArray("PI", currentProposalRoles) !== -1
							&& config.submittedByPI == "NOTSUBMITTED"
							&& config.readyForSubmitionByPI == "True" && config.deletedByPI == "NOTDELETED") || (currentPositionTitle == "University Research Administrator"
							&& config.researchAdministratorSubmission == "NOTSUBMITTED" && config.researchDirectorApproval == "APPROVED"))) {
				$("#btnSubmitProposal").show();
			} else {
				$("#btnSubmitProposal").hide();
			}

			if (proposalStatus != ""
					&& ($.inArray("PI", currentProposalRoles) !== -1 || ($
							.inArray("Co-PI", currentProposalRoles) !== -1 && config.readyForSubmitionByPI == "False"))
					&& config.submittedByPI == "NOTSUBMITTED"
					&& config.deletedByPI == "NOTDELETED") {
				$("#btnSaveProposal").show();
			} else {
				$("#btnSaveProposal").hide();
			}

			$
					.each(
							currentProposalRoles,
							function(index, value) {
								if (proposalStatus != ""
										&& (($.inArray("PI",
												currentProposalRoles) !== -1
												&& config.submittedByPI == "NOTSUBMITTED" && config.deletedByPI == "NOTDELETED") || (currentPositionTitle == "University Research Director"
												&& config.researchDirectorDeletion == "NOTDELETED" && config.researchDirectorApproval == "READYFORAPPROVAL"))) {
									$("#btnDeleteProposal").show();
								} else {
									$("#btnDeleteProposal").hide();
								}
							});

			if (proposalStatus != ""
					&& ((currentPositionTitle == "Department Chair" && config.chairApproval == "READYFORAPPROVAL")
							|| (currentPositionTitle == "Business Manager" && config.businessManagerApproval == "READYFORAPPROVAL")
							|| (currentPositionTitle == "IRB" && config.irbapproval == "READYFORAPPROVAL")
							|| (currentPositionTitle == "Dean" && config.deanApproval == "READYFORAPPROVAL")
							|| (currentPositionTitle == "University Research Administrator" && config.researchAdministratorApproval == "READYFORAPPROVAL") || (currentPositionTitle == "University Research Director" && config.researchDirectorApproval == "READYFORAPPROVAL"))) {
				$("#btnApproveProposal").show();
				$("#btnDisapproveProposal").show();
			} else {
				$("#btnApproveProposal").hide();
				$("#btnDisapproveProposal").hide();
			}

			if (proposalStatus != ""
					&& config.researchAdministratorWithdraw == "NOTWITHDRAWN"
					&& config.researchAdministratorApproval == "READYFORAPPROVAL"
					&& currentPositionTitle == "University Research Administrator") {
				$("#btnWithdrawProposal").show();
			} else {
				$("#btnWithdrawProposal").hide();
			}

			if (proposalStatus != ""
					&& config.researchDirectorArchived == "NOTARCHIVED"
					&& config.researchAdministratorSubmission == "SUBMITTED"
					&& currentPositionTitle == "University Research Director") {
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
						'Edit Proposal Details for: ' + argus[1]);

				$("#lblProposalDateReceived").text(argus[3]);

				myProposal.ClearForm();

				myProposal.config.proposalRoles = $.trim(argus[5]);
				myProposal.config.proposalId = argus[0];

				myProposal.config.submittedByPI = argus[7];
				myProposal.config.readyForSubmitionByPI = argus[8];
				myProposal.config.deletedByPI = argus[9];
				myProposal.config.chairApproval = argus[10];
				myProposal.config.businessManagerApproval = argus[11];
				myProposal.config.irbapproval = argus[12];
				myProposal.config.deanApproval = argus[13];
				myProposal.config.researchAdministratorApproval = argus[14];
				myProposal.config.researchAdministratorWithdraw = argus[15];
				myProposal.config.researchDirectorApproval = argus[16];
				myProposal.config.researchDirectorDeletion = argus[17];
				myProposal.config.researchAdministratorSubmission = argus[18];
				myProposal.config.researchDirectorArchived = argus[19];

				$("#txtNameOfGrantingAgency").val(argus[2]);

				$("#trSignChair").show();
				$("#trSignBusinessManager").show();
				if (argus[20].toLowerCase() != "true") {
					$("#trSignIRB").hide();
				} else {
					$("#trSignIRB").show();
				}
				$("#trSignDean").show();
				$("#trSignAdministrator").show();
				$("#trSignDirector").show();

				// OSP Section
				$('#ui-id-23').show();

				var currentPositionTitle = GPMS.utils.GetUserPositionTitle();

				if (currentPositionTitle == "University Research Administrator"
						|| currentPositionTitle == "University Research Director") {
					$('#ui-id-24').find('input, select, textarea').each(
							function() {
								// $(this).addClass("ignore");
								$(this).prop('disabled', false);
							});
				} else {
					$('#ui-id-24').find('input, select, textarea').each(
							function() {
								// $(this).addClass("ignore");
								$(this).prop('disabled', true);
							});
				}

				$('#ddlProposalStatus option').length = 0;
				$('#ddlProposalStatus').append(new Option(argus[6], argus[6]))
						.prop('disabled', true);

				myProposal.config.proposalStatus = argus[6];

				myProposal.BindUserPositionDetailsForAProposal(argus[4]);

				myProposal.BindProposalDetailsByProposalId(argus[0]);

				$('.sfButtonwrapper>p>button').show();

				// myProposal.ButtonHideShow(myProposal.config);

				// Certification/ Signatures Info
				myProposal.BindAllSignatureForAProposal(argus[0], argus[20]);

				// Delegation Info

				$("#fileuploader").hide();
				$("#dataTable tbody tr").find('input.AddCoPI').hide();
				$("#dataTable tbody tr").find('input.AddSenior').hide();
				$("#dataTable tbody tr:gt(0)").find('input.AddSenior').remove();

				var currentProposalRoles = myProposal.config.proposalRoles
						.split(', ');
				if ($.inArray("PI", currentProposalRoles) !== -1) {
					$("#fileuploader").show();
					$("#dataTable tbody tr").find('input.AddCoPI').show();
					$("#dataTable tbody tr").find('input.AddSenior').show();
				} else if ($.inArray("Co-PI", currentProposalRoles) !== -1) {
					$("#fileuploader").show();
					$("#dataTable tbody tr").find('input.AddSenior').show();
					$("#dataTable tbody tr:gt(0)").each(function(index) {
						if ($(this).find("select:first").val() == 2)
							$(this).find('input.AddCoPI').show();
					});
				}
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
			// Investigator Information
			myProposal.BindInvestigatorInfo(response.investigatorInfo);

			// Project Extra Information
			$("#lblHiddenDateReceived").text(response.dateReceived);

			// Project Information
			$("#txtProjectTitle").val(response.projectInfo.projectTitle).prop(
					"disabled", true);

			if (response.projectInfo.projectType.researchBasic) {
				$("#ddlProjectType").val(1);
			} else if (response.projectInfo.projectType.researchApplied) {
				$("#ddlProjectType").val(2);
			} else if (response.projectInfo.projectType.researchDevelopment) {
				$("#ddlProjectType").val(3);
			} else if (response.projectInfo.projectType.instruction) {
				$("#ddlProjectType").val(4);
			} else if (response.projectInfo.projectType.otherSponsoredActivity) {
				$("#ddlProjectType").val(5);
			} else {
				$("#ddlProjectType").prop("selectedIndex", 0);
			}

			if (response.projectInfo.typeOfRequest.preProposal) {
				$("#ddlTypeOfRequest").val(1);
			} else if (response.projectInfo.typeOfRequest.newProposal) {
				$("#ddlTypeOfRequest").val(2);
			} else if (response.projectInfo.typeOfRequest.continuation) {
				$("#ddlTypeOfRequest").val(3);
			} else if (response.projectInfo.typeOfRequest.supplement) {
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
					response.sponsorAndBudgetInfo.faCosts);
			$("#txtTotalCosts").autoNumeric('set',
					response.sponsorAndBudgetInfo.totalCosts);
			$("#txtFARate").autoNumeric('set',
					response.sponsorAndBudgetInfo.faRate);

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
				if (response.complianceInfo.irbPending) {
					$("#ddlIRBOptions").val(2);
					$("#tdIRBtxt").hide();
				} else if (!response.complianceInfo.irbPending
						&& response.complianceInfo.irb != "") {
					$("#ddlIRBOptions").val(1);
					$("#txtIRB").val(response.complianceInfo.irb);
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
				if (response.complianceInfo.iacucPending) {
					$("#ddlIACUCOptions").val(2);
					$("#tdIACUCtxt").hide();
				} else if (!response.complianceInfo.iacucPending
						&& response.complianceInfo.iacuc != "") {
					$("#ddlIACUCOptions").val(1);
					$("#txtIACUC").val(response.complianceInfo.iacuc);
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
				if (response.complianceInfo.ibcPending) {
					$("#ddlIBCOptions").val(2);
					$("#tdIBCtxt").hide();
				} else if (!response.complianceInfo.ibcPending
						&& response.complianceInfo.ibc != "") {
					$("#ddlIBCOptions").val(1);
					$("#txtIBC").val(response.complianceInfo.ibc);
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
			$("#txtAgencyList").val(response.ospSectionInfo.listAgency);

			$("#chkFederal").prop("checked",
					response.ospSectionInfo.fundingSource.federal);
			$("#chkFederalFlowThrough").prop("checked",
					response.ospSectionInfo.fundingSource.federalFlowThrough);
			$("#chkStateOfIdahoEntity").prop("checked",
					response.ospSectionInfo.fundingSource.stateOfIdahoEntity);
			$("#chkPrivateForProfit").prop("checked",
					response.ospSectionInfo.fundingSource.privateForProfit);
			$("#chkNonProfitOrganization")
					.prop(
							"checked",
							response.ospSectionInfo.fundingSource.nonProfitOrganization);
			$("#chkNonIdahoStateEntity").prop("checked",
					response.ospSectionInfo.fundingSource.nonIdahoStateEntity);
			$("#chkCollegeUniversity").prop("checked",
					response.ospSectionInfo.fundingSource.collegeOrUniversity);
			$("#chkLocalEntity").prop("checked",
					response.ospSectionInfo.fundingSource.localEntity);
			$("#chkNonIdahoLocalEntity").prop("checked",
					response.ospSectionInfo.fundingSource.nonIdahoLocalEntity);
			$("#chkTribalGovernment").prop("checked",
					response.ospSectionInfo.fundingSource.tirbalGovernment);
			$("#chkForeign").prop("checked",
					response.ospSectionInfo.fundingSource.foreign);

			$("#txtCFDANo").val(response.ospSectionInfo.cfdaNo);
			$("#txtProgramNo").val(response.ospSectionInfo.programNo);
			$("#txtProgramTitle").val(response.ospSectionInfo.programTitle);

			$("#chkFullRecovery").prop("checked",
					response.ospSectionInfo.recovery.fullRecovery);
			$("#chkNoRecoveryNormal")
					.prop(
							"checked",
							response.ospSectionInfo.recovery.noRecoveryNormalSponsorPolicy);
			$("#chkNoRecoveryInstitutional")
					.prop(
							"checked",
							response.ospSectionInfo.recovery.noRecoveryInstitutionalWaiver);
			$("#chkLimitedRecoveryNormal")
					.prop(
							"checked",
							response.ospSectionInfo.recovery.limitedRecoveryNormalSponsorPolicy);
			$("#chkLimitedRecoveryInstitutional")
					.prop(
							"checked",
							response.ospSectionInfo.recovery.limitedRecoveryInstitutionalWaiver);

			$("#chkMTDC")
					.prop("checked", response.ospSectionInfo.baseInfo.mtdc);
			$("#chkTDC").prop("checked", response.ospSectionInfo.baseInfo.tdc);
			$("#chkTC").prop("checked", response.ospSectionInfo.baseInfo.tc);
			$("#chkOther").prop("checked",
					response.ospSectionInfo.baseInfo.other);
			$("#chkNA").prop("checked",
					response.ospSectionInfo.baseInfo.notApplicable);

			if (response.ospSectionInfo.piSalaryIncluded) {
				$("#ddlPISalaryIncluded").val(1);
				$("#lblPISalaryIncluded").hide();
			} else if (!response.ospSectionInfo.piSalaryIncluded) {
				$("#ddlPISalaryIncluded").val(2);
				$("#lblPISalaryIncluded").show();
			} else {
				$("#ddlPISalaryIncluded").prop("selectedIndex", 0);
				$("#lblPISalaryIncluded").hide();
			}

			$("#txtPISalary").autoNumeric('set',
					response.ospSectionInfo.piSalary);
			$("#txtPIFringe").autoNumeric('set',
					response.ospSectionInfo.piFringe);

			$("#txtDepartmentID").val(response.ospSectionInfo.departmentId);

			if (response.ospSectionInfo.institutionalCostDocumented.yes) {
				$("#ddlInstitutionalCostDocumented").val(1);
			} else if (response.ospSectionInfo.institutionalCostDocumented.no) {
				$("#ddlInstitutionalCostDocumented").val(2);
			} else if (response.ospSectionInfo.institutionalCostDocumented.notApplicable) {
				$("#ddlInstitutionalCostDocumented").val(3);
			} else {
				$("#ddlInstitutionalCostDocumented").prop("selectedIndex", 0);
			}

			if (response.ospSectionInfo.thirdPartyCostDocumented.yes) {
				$("#ddlThirdPartyCostDocumented").val(1);
			} else if (response.ospSectionInfo.thirdPartyCostDocumented.no) {
				$("#ddlThirdPartyCostDocumented").val(2);
			} else if (response.ospSectionInfo.thirdPartyCostDocumented.notApplicable) {
				$("#ddlThirdPartyCostDocumented").val(3);
			} else {
				$("#ddlThirdPartyCostDocumented").prop("selectedIndex", 0);
			}

			if (response.ospSectionInfo.anticipatedSubRecipients) {
				$("#ddlSubrecipients").val(1);
				$("#txtNamesSubrecipients").removeClass("ignore");
				$("#txtNamesSubrecipients").val(
						response.ospSectionInfo.anticipatedSubRecipientsNames);
				$("#trSubrecipientsNames").show();
			} else if (!response.ospSectionInfo.anticipatedSubRecipients) {
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

			if (response.ospSectionInfo.piEligibilityWaiver.yes) {
				$("#ddlPIEligibilityWaiver").val(1);
			} else if (response.ospSectionInfo.piEligibilityWaiver.no) {
				$("#ddlPIEligibilityWaiver").val(2);
			} else if (response.ospSectionInfo.piEligibilityWaiver.notApplicable) {
				$("#ddlPIEligibilityWaiver").val(3);
			} else if (response.ospSectionInfo.piEligibilityWaiver.thisProposalOnly) {
				$("#ddlPIEligibilityWaiver").val(4);
			} else if (response.ospSectionInfo.piEligibilityWaiver.blanket) {
				$("#ddlPIEligibilityWaiver").val(5);
			} else {
				$("#ddlPIEligibilityWaiver").prop("selectedIndex", 0);
			}

			if (response.ospSectionInfo.conflictOfInterestForms.yes) {
				$("#ddlCOIForms").val(1);
			} else if (response.ospSectionInfo.conflictOfInterestForms.no) {
				$("#ddlCOIForms").val(2);
			} else if (response.ospSectionInfo.conflictOfInterestForms.notApplicable) {
				$("#ddlCOIForms").val(3);
			} else {
				$("#ddlCOIForms").prop("selectedIndex", 0);
			}

			if (response.ospSectionInfo.excludedPartyListChecked.yes) {
				$("#ddlCheckedExcludedPartyList").val(1);
			} else if (response.ospSectionInfo.excludedPartyListChecked.no) {
				$("#ddlCheckedExcludedPartyList").val(2);
			} else if (response.ospSectionInfo.excludedPartyListChecked.notApplicable) {
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
				myProposal
						.BindUserToPositionDetails(senior, "Senior Personnel");
			});

			$("#dataTable>tbody select[name='ddlName']").each(function(index) {
				if ($(this).find('option').length == 0) {
					$(this).parents('tr').remove();
				}
			});

			$('#dataTable>tbody tr:first').remove();
		},

		AddCoPIInvestigator : function(button) {
			var cloneRow = button.closest('tr').clone(true)
					.addClass("trStatic");
			$(cloneRow).find("input").each(function(i) {
				if ($(this).is(".AddCoPI")) {
					$(this).prop("name", "DeleteOption");
					$(this).prop("value", "Delete");
					$(this).prop("title", "Delete");
					$(this).show();
				} else if ($(this).is(".AddSenior")) {
					$(this).remove();
				}
			});

			$(cloneRow).find("select").each(
					function(j) {
						$(this).removeProp("disabled");
						$(this).removeProp("required");
						// Remove PI
						// option
						// after first
						// row
						if (j == 0) {
							$(this).find('option:first').remove();
							$(this).find('option:last').remove();
							$(this).prop("disabled", true);
						} else if (j == 1) {
							$(this).prop("disabled", false);
							$('#ui-id-2').find("select[name='ddlName']").each(
									function(k) {
										$(cloneRow).find(
												'option[value=' + $(this).val()
														+ ']').remove();
									});
						}
						$(this).find("option").removeProp("selected");
					});

			if ($(cloneRow).find("select[name='ddlName'] option").length > 0) {
				$('#dataTable tr:last').find("select[name='ddlName']").prop(
						"disabled", true);

				$(cloneRow).appendTo("#dataTable").hide().fadeIn(1200);

				rowIndex = $('#dataTable > tbody tr').size() - 1;
				myProposal.BindDefaultUserPosition(rowIndex);
			} else {
				csscody.alert('<h2>' + 'Error Message' + '</h2><p>'
						+ 'There are no user available to be added!</p>');
			}
		},

		AddSeniorPersonnelInvestigator : function(button) {
			var cloneRow = button.closest('tr').clone(true)
					.addClass("trStatic");
			$(cloneRow).find("input").each(function(i) {
				if ($(this).is(".AddCoPI")) {
					$(this).prop("name", "DeleteOption");
					$(this).prop("value", "Delete");
					$(this).prop("title", "Delete");
					$(this).show();
				} else if ($(this).is(".AddSenior")) {
					$(this).remove();
				}
			});

			$(cloneRow).find("select").each(
					function(j) {
						$(this).removeProp("disabled");
						$(this).removeProp("required");
						// Remove PI
						// option
						// after first
						// row
						if (j == 0) {
							$(this).find('option:lt(2)').remove();
							$(this).prop("disabled", true);
							;
						} else if (j == 1) {
							$(this).prop("disabled", false);
							$('#ui-id-2').find("select[name='ddlName']").each(
									function(k) {
										$(cloneRow).find(
												'option[value=' + $(this).val()
														+ ']').remove();
									});
						}
						$(this).find("option").removeProp("selected");
					});

			if ($(cloneRow).find("select[name='ddlName'] option").length > 0) {
				$('#dataTable tr:last').find("select[name='ddlName']").prop(
						"disabled", true);

				$(cloneRow).appendTo("#dataTable").hide().fadeIn(1200);

				rowIndex = $('#dataTable > tbody tr').size() - 1;
				myProposal.BindDefaultUserPosition(rowIndex);
			} else {
				csscody.alert('<h2>' + 'Error Message' + '</h2><p>'
						+ 'There are no user available to be added!</p>');
			}
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
				var btnOption = "Add Co-PI";
				var btnTitle = "Add Co-PI"
				var btnName = "AddCoPI";
				if (rowIndex > 1) {
					btnOption = "Delete";
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
													'selected').prop(
													'disabled', true);
										} else if (userType == "Co-PI") {
											$(this).val(1).prop('selected',
													'selected').prop(
													'disabled', true);
											$(this).find('option').not(
													':selected').remove();
										} else if (userType == "Senior Personnel") {
											$(this).val(2).prop('selected',
													'selected').prop(
													'disabled', true);
											$(this).find('option').not(
													':selected').remove();
										}
									} else if (this.name == "ddlName") {
										$(this).val(userDetails.userProfileId)
												.prop('selected', 'selected')
												.prop('disabled', true);

										if (userType == "Co-PI") {
											$(this).find('option').not(
													':selected').remove();
										} else if (userType == "Senior Personnel") {
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
												'selected', 'selected').prop(
												'disabled', true);
										myProposal.BindDepartmentDropDown($(
												'select[name="ddlName"]').eq(
												rowIndex).val(), $(
												'select[name="ddlCollege"]')
												.eq(rowIndex).val());
									} else if (this.name == "ddlDepartment") {
										$(this).val(userDetails.department)
												.prop('selected', 'selected')
												.prop('disabled', true);
										myProposal.BindPositionTypeDropDown($(
												'select[name="ddlName"]').eq(
												rowIndex).val(), $(
												'select[name="ddlCollege"]')
												.eq(rowIndex).val(), $(
												'select[name="ddlDepartment"]')
												.eq(rowIndex).val());
									} else if (this.name == "ddlPositionType") {
										$(this).val(userDetails.positionType)
												.prop('selected', 'selected')
												.prop('disabled', true);
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
												.prop('selected', 'selected')
												.prop('disabled', true);
									}
								});

				$('#dataTable tbody>tr:eq(' + rowIndex + ')').find("input")
						.each(function(l) {
							if ($(this).is(".AddCoPI")) {
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
					var properties = {
						onComplete : function(e) {
							if (e) {
								myProposal.config.proposalRoles = proposal_roles;
								myProposal.config.proposalId = argus[0];
								myProposal.config.proposalStatus = argus[3];
								myProposal.config.submittedByPI = argus[4];
								myProposal.config.readyForSubmissionByPI = argus[5];
								myProposal.config.deletedByPI = argus[6];
								myProposal.config.chairApproval = argus[7];
								myProposal.config.businessManagerApproval = argus[8];
								myProposal.config.irbapproval = argus[9];
								myProposal.config.deanApproval = argus[10];
								myProposal.config.researchAdministratorApproval = argus[11];
								myProposal.config.researchAdministratorWithdraw = argus[12];
								myProposal.config.researchDirectorApproval = argus[13];
								myProposal.config.researchDirectorDeletion = argus[14];
								myProposal.config.researchAdministratorSubmission = argus[15];
								myProposal.config.researchDirectorArchived = argus[16];

								myProposal.DeleteSingleProposal("Delete",
										"Whole Proposal", myProposal.config);
							}
						}
					};
					csscody
							.confirm(
									"<h2>"
											+ 'Delete Confirmation'
											+ "</h2><p>"
											+ 'Are you certain you want to delete this proposal?'
											+ "</p>", properties);
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

		DeleteSingleProposal : function(buttonType, proposalSection, config) {

			var attributeArray = [];

			var currentPositionTitle = GPMS.utils.GetUserPositionTitle();
			var currentProposalRoles = config.proposalRoles.split(', ');

			if ($.inArray("PI", currentProposalRoles) !== -1
					&& config.submittedByPI == "NOTSUBMITTED"
					&& config.deletedByPI == "NOTDELETED") {
				attributeArray.push({
					attributeType : "Subject",
					attributeName : "proposal.role",
					attributeValue : config.proposalRoles
				});

				attributeArray.push({
					attributeType : "Resource",
					attributeName : "SubmittedByPI",
					attributeValue : config.submittedByPI
				});

				attributeArray.push({
					attributeType : "Resource",
					attributeName : "DeletedByPI",
					attributeValue : config.deletedByPI
				});
			} else if (currentPositionTitle == "University Research Director"
					&& config.researchDirectorDeletion == "NOTDELETED"
					&& config.researchDirectorApproval == "READYFORAPPROVAL") {
				attributeArray.push({
					attributeType : "Subject",
					attributeName : "position.title",
					attributeValue : currentPositionTitle
				});

				attributeArray.push({
					attributeType : "Resource",
					attributeName : "DeletedByUniversityResearchDirector",
					attributeValue : config.researchDirectorDeletion
				});

				attributeArray.push({
					attributeType : "Resource",
					attributeName : "ApprovedByUniversityResearchDirector",
					attributeValue : config.researchDirectorApproval
				});
			}

			attributeArray.push({
				attributeType : "Resource",
				attributeName : "proposal.section",
				attributeValue : proposalSection
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal.action",
				attributeValue : buttonType
			});

			this.config.url = this.config.baseURL
					+ "DeleteProposalByProposalID";
			this.config.data = JSON2.stringify({
				proposalId : config.proposalId,
				proposalRoles : config.proposalRoles,
				proposalUserTitle : currentPositionTitle,
				policyInfo : attributeArray,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 2;
			this.config.buttonType = buttonType;
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
			myProposal.config.content = "";
			myProposal.config.investigatorButton = "";

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
			$("#trSignIRB").hide();
			$("#trSignDean").hide();
			$("#trSignAdministrator").hide();
			$("#trSignDirector").hide();
			signatureInfo = '';
			$("#trSignPICOPI tbody").empty();
			$("#trSignChair tbody").empty();
			$("#trSignBusinessManager tbody").empty();
			$("#trSignIRB tbody").empty();
			$("#trSignDean tbody").empty();
			$("#trSignAdministrator tbody").empty();
			$("#trSignDirector tbody").empty();

			$('#txtProjectTitle').removeProp('disabled');

			rowIndex = 0;
			$("#dataTable tbody>tr:gt(0)").remove();

			$('select[name=ddlRole]').eq(0).val(0).prop('selected', 'selected')
					.prop('disabled', true);
			$('select[name=ddlName]').eq(0).prop('disabled', false);
			$('select[name = ddlCollege]').eq(0).prop('disabled', false);
			$('select[name = ddlDepartment]').eq(0).prop('disabled', false);
			$('select[name = ddlPositionType]').eq(0).prop('disabled', false);
			$('select[name=ddlPositionTitle]').eq(0).prop('disabled', false);

			var container = $("#accordion > div").slice(1, 12);
			var inputs = container.find('INPUT, SELECT, TEXTAREA');
			$.each(inputs, function(i, item) {
				$(this).prop('checked', false);
				$(this).val('');
				$(this).val($(this).find('option').first().val());
			});
			$(".AddCoPI").val("Add Co-PI");
			$(".AddSenior").val("Add Senior Personnel");
			return false;
		},

		BindCurrentUserPosition : function(rowIndexVal) {
			// For form Dropdown Binding
			myProposal.BindCurrentDetailsForPI(GPMS.utils.GetUserProfileID());

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
			var cloneRow = '<tr><td><span class="cssClassLabel" name ="fullname" role="PI" delegated="false">'
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

			$('#trSignPICOPI tbody tr:last').data("allowchange", "true").data(
					"allowsign", "true");
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
										// alert($.trim(ui.newHeader.text()));
										myProposal.config.ajaxCallMode = 15;
										// myProposal.config.event = event;
										myProposal.config.content = ui.newPanel;

										myProposal
												.CheckUserPermissionWithProposalRoleEditSection(
														"Edit",
														myProposal.config.proposalRoles,
														myProposal.config.proposalId,
														$.trim(ui.newHeader
																.text()),
														myProposal.config);
									}
								}
							// ,
							// beforeActivate : function(event, ui) {
							// // Size = 0 --> collapsing
							// // Size = 1 --> Expanding
							// if (myProposal.config.proposalId != "0"
							// && ui.newHeader.length != 0) {
							// // alert($.trim(ui.newHeader.text()));
							// myProposal.config.ajaxCallMode = 14;
							// myProposal.config.event = event;
							// myProposal.config.content = ui.newPanel;
							// if (myProposal.config.proposalRoles != "") {
							// myProposal
							// .CheckUserPermissionWithProposalRole(
							// "View",
							// myProposal.config.proposalRoles,
							// myProposal.config.proposalId,
							// $.trim(ui.newHeader
							// .text()),
							// myProposal.config);
							// } else {
							// myProposal
							// .CheckUserPermissionWithPositionTitle(
							// "View",
							// myProposal.config.proposalId,
							// $.trim(ui.newHeader
							// .text()),
							// myProposal.config);
							// }
							//
							// }
							// }
							});
			// myProposal.SelectFirstAccordion();
			// $("#accordion").accordion("option", "active", 0);
			return false;
		},

		BindAllSignatureForAProposal : function(proposalId, irbSignRequired) {
			myProposal.config.url = myProposal.config.baseURL
					+ "GetAllSignatureForAProposal";
			myProposal.config.data = JSON2.stringify({
				proposalId : proposalId,
				irbApprovalRequired : irbSignRequired
			});
			myProposal.config.ajaxCallMode = 8;
			myProposal.ajaxCall(myProposal.config);
		},

		BindCurrentDateTime : function(obj) {
			$(obj).val($.format.date(new Date(), 'yyyy/MM/dd hh:mm:ss a'));
			return false;
		},

		GetUserSignature : function(obj) {
			var allowedChangeAttr = obj.data('allowchange');
			var allowedSignAttr = obj.data('allowsign');

			if (typeof allowedChangeAttr !== typeof undefined
					&& allowedChangeAttr !== false
					&& allowedChangeAttr == "true"
					&& typeof allowedSignAttr !== typeof undefined
					&& allowedSignAttr !== false && allowedSignAttr == "true") {
				obj.find("input").each(function() {
					var optionsText = $(this).val();
					if ($(this).attr("data-for") != "signaturedate") {

						signatureInfo += $(this).attr("data-value") + "!#!"; // UserProfileID

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

		SaveProposal : function(buttonType, proposalSection, config, _flag) {
			// if (validator.form()) {

			var $projectTitle = $('#txtProjectTitle');
			var projectTitle = $.trim($projectTitle.val());
			var validateErrorMessage = myProposal.checkUniqueProjectTitle(
					config.proposalId, projectTitle, $projectTitle);

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
											'input[name="txtPhoneNo"]').mask()
											+ "#!#";
								});

				investigatorInfo = investigatorInfo.substring(0,
						investigatorInfo.length - 3);

				signatureInfo = '';

				$(
						'#trSignPICOPI > tbody  > tr, #trSignChair > tbody  > tr, #trSignDean > tbody  > tr, #trSignBusinessManager > tbody  > tr, #trSignIRB > tbody  > tr, #trSignAdministrator > tbody  > tr, #trSignDirector > tbody  > tr')
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
					GrantingAgency : $
							.trim($("#txtNameOfGrantingAgency").val()),
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
					NewRenovatedFacilitiesRequired : $("#ddlNewSpaceRequired")
							.val(),
					RentalSpaceRequired : $("#ddlRentalSpaceRequired").val(),
					InstitutionalCommitmentRequired : $(
							"#ddlInstitutionalCommitmentsRequired").val()
				};

				var conflicOfInterestInfo = {
					FinancialCOI : $("#ddlFinancialCOI").val(),
					ConflictDisclosed : $("#ddlDisclosedFinancialCOI").val(),
					DisclosureFormChange : $("#ddlMaterialChanged").val()
				};

				var complianceInfo = {
					InvolveUseOfHumanSubjects : $("#ddlUseHumanSubjects").val(),
					InvolveUseOfVertebrateAnimals : $(
							"#ddlUseVertebrateAnimals").val(),
					InvolveBiosafetyConcerns : $("#ddlInvovleBioSafety").val(),
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
					complianceInfo.IACUCPending = $("#ddlIACUCOptions").val();
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
					InvolveNonFundedCollab : $("#ddlInvolveNonFundedCollabs")
							.val()
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
					ProposalID : config.proposalId,
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

				if (!_flag) {
					// proposalInfo.ProposalStatus = $("#ddlProposalStatus")
					// .val();

					var OSPSection = {
						ListAgency : $.trim($("#txtAgencyList").val()),

						Federal : $("#chkFederal").prop("checked"),
						FederalFlowThrough : $("#chkFederalFlowThrough").prop(
								"checked"),
						StateOfIdahoEntity : $("#chkStateOfIdahoEntity").prop(
								"checked"),
						PrivateForProfit : $("#chkPrivateForProfit").prop(
								"checked"),
						NonProfitOrganization : $("#chkNonProfitOrganization")
								.prop("checked"),
						NonIdahoStateEntity : $("#chkNonIdahoStateEntity")
								.prop("checked"),
						CollegeOrUniversity : $("#chkCollegeUniversity").prop(
								"checked"),
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
						FullRecovery : $("#chkFullRecovery").prop("checked"),
						NoRecoveryNormalSponsorPolicy : $(
								"#chkNoRecoveryNormal").prop("checked"),
						NoRecoveryInstitutionalWaiver : $(
								"#chkNoRecoveryInstitutional").prop("checked"),
						LimitedRecoveryNormalSponsorPolicy : $(
								"#chkLimitedRecoveryNormal").prop("checked"),
						LimitedRecoveryInstitutionalWaiver : $(
								"#chkLimitedRecoveryInstitutional").prop(
								"checked"),

						MTDC : $("#chkMTDC").prop("checked"),
						TDC : $("#chkTDC").prop("checked"),
						TC : $("#chkTC").prop("checked"),
						Other : $("#chkOther").prop("checked"),
						NotApplicable : $("#chkNA").prop("checked"),

						// --------------------------
						IsPISalaryIncluded : $("#ddlPISalaryIncluded").val(),
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
						OSPSection.AnticipatedSubRecipientsNames = $.trim($(
								"#txtNamesSubrecipients").val());
					}

					proposalInfo.OSPSectionInfo = OSPSection;
				}

				$
						.each(
								this.config.uploadObj.getResponses().reverse(),
								function(i, val) {
									val['title'] = $(
											myProposal.config.uploadObj.container
													.children(
															".ajax-file-upload-statusbar")
													.find('.extrahtml').find(
															"input").get(i))
											.val();
								});

				proposalInfo.AppendixInfo = this.config.uploadObj
						.getResponses().reverse();

				myProposal.AddProposalInfo(buttonType, proposalSection, config,
						proposalInfo, _flag);

			} else {
				myProposal.focusTabWithErrors("#accordion");
			}
			// } else {
			// myProposal.focusTabWithErrors("#accordion");
			// }
		},

		AddProposalInfo : function(buttonType, proposalSection, config, info,
				flag) {
			alert(buttonType);

			var attributeArray = [];
			var currentPositionTitle = GPMS.utils.GetUserPositionTitle();

			if (!flag) {
				var currentProposalRoles = config.proposalRoles.split(', ');

				switch (buttonType) {
				case "Save":
					if (currentProposalRoles != ""
							&& ($.inArray("PI", currentProposalRoles) !== -1 || ($
									.inArray("Co-PI", currentProposalRoles) !== -1 && config.readyForSubmitionByPI == "False"))
							&& config.submittedByPI == "NOTSUBMITTED"
							&& config.deletedByPI == "NOTDELETED") {
						attributeArray.push({
							attributeType : "Subject",
							attributeName : "proposal.role",
							attributeValue : config.proposalRoles
						});

						attributeArray.push({
							attributeType : "Resource",
							attributeName : "SubmittedByPI",
							attributeValue : config.submittedByPI
						});

						attributeArray.push({
							attributeType : "Resource",
							attributeName : "DeletedByPI",
							attributeValue : config.deletedByPI
						});

						if ($.inArray("Co-PI", currentProposalRoles) !== -1
								&& config.readyForSubmitionByPI == "False") {
							attributeArray.push({
								attributeType : "Resource",
								attributeName : "ReadyForSubmissionByPI",
								attributeValue : config.readyForSubmitionByPI
							});
						}
					}

					break;

				case "Submit":
					if ($.inArray("PI", currentProposalRoles) !== -1
							&& config.submittedByPI == "NOTSUBMITTED"
							&& config.readyForSubmitionByPI == "True"
							&& config.deletedByPI == "NOTDELETED") {
						attributeArray.push({
							attributeType : "Subject",
							attributeName : "proposal.role",
							attributeValue : config.proposalRoles
						});

						attributeArray.push({
							attributeType : "Resource",
							attributeName : "SubmittedByPI",
							attributeValue : config.submittedByPI
						});

						attributeArray.push({
							attributeType : "Resource",
							attributeName : "ReadyForSubmissionByPI",
							attributeValue : config.readyForSubmitionByPI
						});

						attributeArray.push({
							attributeType : "Resource",
							attributeName : "DeletedByPI",
							attributeValue : config.deletedByPI
						});

					} else if (currentPositionTitle == "University Research Administrator"
							&& config.researchAdministratorSubmission == "NOTSUBMITTED"
							&& config.researchDirectorApproval == "APPROVED") {
						attributeArray.push({
							attributeType : "Subject",
							attributeName : "position.title",
							attributeValue : currentPositionTitle
						});

						attributeArray
								.push({
									attributeType : "Resource",
									attributeName : "SubmittedByUniversityResearchAdministrator",
									attributeValue : config.researchAdministratorSubmission
								});

						attributeArray
								.push({
									attributeType : "Resource",
									attributeName : "ApprovedByUniversityResearchDirector",
									attributeValue : config.researchDirectorApproval
								});
					}

					break;
				case "Approve":
				case "Disapprove":
					attributeArray.push({
						attributeType : "Subject",
						attributeName : "position.title",
						attributeValue : currentPositionTitle
					});

					switch (currentPositionTitle) {
					case "Department Chair":
						attributeArray.push({
							attributeType : "Resource",
							attributeName : "ApprovedByDepartmentChair",
							attributeValue : config.chairApproval
						});
						break;
					case "Business Manager":
						attributeArray.push({
							attributeType : "Resource",
							attributeName : "ApprovedByBusinessManager",
							attributeValue : config.businessManagerApproval
						});
						break;
					case "IRB":
						attributeArray.push({
							attributeType : "Resource",
							attributeName : "ApprovedByIRB",
							attributeValue : config.irbapproval
						});
						break;
					case "Dean":
						attributeArray.push({
							attributeType : "Resource",
							attributeName : "ApprovedByDean",
							attributeValue : config.deanApproval
						});
						break;
					case "University Research Administrator":
						attributeArray
								.push({
									attributeType : "Resource",
									attributeName : "ApprovedByUniversityResearchAdministrator",
									attributeValue : config.researchAdministratorApproval
								});
						break;
					case "University Research Director":
						attributeArray
								.push({
									attributeType : "Resource",
									attributeName : "ApprovedByUniversityResearchDirector",
									attributeValue : config.researchDirectorApproval
								});
						break;
					default:
						break;
					}

					break;
				default:
					break;
				}

				attributeArray.push({
					attributeType : "Resource",
					attributeName : "proposal.section",
					attributeValue : proposalSection
				});

				attributeArray.push({
					attributeType : "Action",
					attributeName : "proposal.action",
					attributeValue : buttonType
				});
			} else {
				switch (buttonType) {
				case "Save":
					attributeArray.push({
						attributeType : "Subject",
						attributeName : "position.type",
						attributeValue : GPMS.utils.GetUserPositionType()
					});

					attributeArray.push({
						attributeType : "Resource",
						attributeName : "proposal.section",
						attributeValue : proposalSection
					});

					attributeArray.push({
						attributeType : "Action",
						attributeName : "proposal.action",
						attributeValue : buttonType
					});

					break;
				default:
					break;
				}
			}

			this.config.url = this.config.baseURL + "SaveUpdateProposal";
			this.config.data = JSON2.stringify({
				buttonType : buttonType,
				proposalRoles : config.proposalRoles,
				proposalUserTitle : currentPositionTitle,
				proposalInfo : info,
				policyInfo : attributeArray,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 9;
			this.config.buttonType = buttonType;
			this.ajaxCall(this.config);
			return false;
		},

		UpdateProposalStatus : function(buttonType, proposalSection, config) {
			alert(buttonType);
			var attributeArray = [];

			var currentPositionTitle = GPMS.utils.GetUserPositionTitle();

			switch (buttonType) {
			case "Withdraw":
				if (config.researchAdministratorWithdraw == "NOTWITHDRAWN"
						&& config.researchAdministratorApproval == "READYFORAPPROVAL"
						&& currentPositionTitle == "University Research Administrator") {
					attributeArray.push({
						attributeType : "Subject",
						attributeName : "position.title",
						attributeValue : currentPositionTitle
					});

					attributeArray
							.push({
								attributeType : "Resource",
								attributeName : "WithdrawnByUniversityResearchAdministrator",
								attributeValue : config.researchAdministratorWithdraw
							});

					attributeArray
							.push({
								attributeType : "Resource",
								attributeName : "ApprovedByUniversityResearchAdministrator",
								attributeValue : config.researchAdministratorApproval
							});
				}

				break;
			case "Archive":
				if (config.researchDirectorArchived == "NOTARCHIVED"
						&& config.researchAdministratorSubmission == "SUBMITTED"
						&& currentPositionTitle == "University Research Director") {
					attributeArray.push({
						attributeType : "Subject",
						attributeName : "position.title",
						attributeValue : currentPositionTitle
					});

					attributeArray.push({
						attributeType : "Resource",
						attributeName : "ArchivedByUniversityResearchDirector",
						attributeValue : config.researchDirectorArchived
					});

					attributeArray
							.push({
								attributeType : "Resource",
								attributeName : "SubmittedByUniversityResearchAdministrator",
								attributeValue : config.researchAdministratorSubmission
							});
				}

				break;
			default:
				break;
			}

			attributeArray.push({
				attributeType : "Resource",
				attributeName : "proposal.section",
				attributeValue : proposalSection
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal.action",
				attributeValue : buttonType
			});

			this.config.url = this.config.baseURL + "UpdateProposalStatus";
			this.config.data = JSON2.stringify({
				buttonType : buttonType,
				proposalUserTitle : currentPositionTitle,
				proposalId : config.proposalId,
				policyInfo : attributeArray,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 9;
			this.config.buttonType = buttonType;
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

		BindCurrentDetailsForPI : function(userId) {
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
							+ "GetCurrentPositionDetailsForPI";
					this.config.data = JSON2.stringify({
						gpmsCommonObj : gpmsCommonObj()
					});
					this.config.ajaxCallMode = 6;
					this.ajaxCall(this.config);
				}
			}
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
				myProposal.config.content = "";
				myProposal.config.investigatorButton = "";
				break;
			break;

		// Unused
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
								var userPositionTitle = item.positionTitle
										.split(' ').join('_');

								if (item.signedDate != null) {
									signedDate = item.signedDate;
								}

								var cloneRow = '<tr><td><span class="cssClassLabel" name="fullname" role="'
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
										+ '" name="'
										+ item.userProfileId
										+ userPositionTitle
										+ '" readonly="true">'
										+ '</td><td><input data-for="signaturedate" name="signaturedate'
										+ item.userProfileId
										+ userPositionTitle
										+ '" title="Signed Date" class="sfInputbox" placeholder="Signed Date" type="text" readonly="true" value="'
										+ $.format.date(signedDate,
												'yyyy/MM/dd hh:mm:ss a')
										+ '"></td><td><textarea rows="2" cols="26" name="proposalNotes'
										+ item.userProfileId
										+ userPositionTitle
										+ '" readonly="true" title="Proposal Notes" class="cssClassTextArea" >'
										+ item.note + '</textarea></td></tr>';

								// $('#trSignChair').hide();
								// $('#trSignDean').hide();
								// $('#trSignBusinessManager').hide();
								// $('#trSignIRB').hide();
								// $('#trSignAdministrator').hide();
								// $('#trSignDirector').hide();

								switch (item.positionTitle) {
								case "PI":
								case "Co-PI":
									$(cloneRow).appendTo("#trSignPICOPI tbody");
									break;
								case "Department Chair":
									$(cloneRow).appendTo("#trSignChair tbody");
									$('#trSignChair').show();
									break;
								case "Dean":
									$(cloneRow).appendTo("#trSignDean tbody");
									$('#trSignDean').show();
									break;
								case "Business Manager":
									$(cloneRow).appendTo(
											"#trSignBusinessManager tbody");
									$('#trSignBusinessManager').show();
									break;
								case "IRB":
									$(cloneRow).appendTo("#trSignIRB tbody");
									$('#trSignIRB').show();
									break;
								case "University Research Administrator":
									$(cloneRow).appendTo(
											"#trSignAdministrator tbody");
									$('#trSignAdministrator').show();
									break;
								case "University Research Director":
									$(cloneRow).appendTo(
											"#trSignDirector tbody");
									$('#trSignDirector').show();
									break;
								default:
									break;
								}
							});

			var signTable = '';
			var currentProposalRoles = myProposal.config.proposalRoles
					.split(', ');
			if ($.inArray("PI", currentProposalRoles) !== -1
					|| $.inArray("Co-PI", currentProposalRoles) !== -1) {
				signTable = "#trSignPICOPI";
			} else {
				switch (GPMS.utils.GetUserPositionTitle()) {
				case "Department Chair":
					signTable = "#trSignChair";
					break;
				case "Dean":
					signTable = "#trSignDean";
					break;
				case "Business Manager":
					signTable = "#trSignBusinessManager";
					break;
				case "IRB":
					signTable = "#trSignIRB";
					break;
				case "University Research Administrator":
					signTable = "#trSignAdministrator";
					break;
				case "University Research Director":
					signTable = "#trSignDirector";
					break;
				default:
					break;
				}
			}

			if (signTable !== "") {
				$(signTable + " tbody tr")
						.find('input:eq(0)')
						.each(
								function(index) {

									if (GPMS.utils.GetUserProfileID() == $(this)
											.attr("data-value")) {
										// if (item.signedDate == null) {
										$(this).parents('tr').data(
												"allowchange", "true").data(
												"allowsign", "true");
										$(this)
												.parents('tr')
												.find('input, textarea')
												.each(
														function(index) {
															$(this).removeProp(
																	"readonly");

															if (index == 1) {// Date
																$(this)
																		.click(
																				function() {
																					myProposal
																							.BindCurrentDateTime(this);
																				});
																$(this)
																		.attr(
																				"required",
																				"true");
															} else { // Signature,
																// Note
																$(this)
																		.attr(
																				"required",
																				"true");
															}
														});
										// }
									}
								});
			}

			break;

		case 9:
			myProposal.BindProposalGrid(null, null, null, null, null, null,
					null, null);
			$('#divProposalGrid').show();

			// $("#accordion").accordion("option", "active", 0);

			// if (myProposal.config.proposalId != "0") {
			var changeMade = "Saved";
			switch (myProposal.config.buttonType) {
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
			// } else {
			// csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
			// + 'Proposal has been Saved successfully.' + "</p>");
			// }
			$('#divProposalForm').hide();
			$('#divProposalAuditGrid').hide();
			// myProposal.CollapseAccordion();
			// myProposal.SelectFirstAccordion();

			myProposal.config.proposalId = '0';
			myProposal.config.proposalRoles = "";
			myProposal.config.buttonType = "";
			myProposal.config.arguments = [];
			myProposal.config.events = "";
			myProposal.config.content = "";
			myProposal.config.investigatorButton = "";
			break;

		case 10:
			// myProposal.DeleteSingleProposal(myProposal.config.proposalId,
			// myProposal.config.proposalRoles);
			break;

		case 11:
			// if (myProposal.config.proposalId != '0') {
			// myProposal.SaveProposal(myProposal.config.buttonType,
			// myProposal.config.proposalRoles,
			// myProposal.config.proposalId, false);
			// }
			break;

		case 12:
			if (myProposal.config.proposalId == '0') {
				$('#lblFormHeading').html('New Proposal Details');

				// Initialize Appendices content and Uploader
				myProposal.InitializeUploader("");

				$("#btnReset").show();
				$("#btnSaveProposal").show();
				$("#btnDeleteProposal").hide();

				// For old Proposal only visible
				$("#btnSubmitProposal").hide();

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

				myProposal.ClearForm();

				$('select[name=ddlName]').eq(0).val(
						GPMS.utils.GetUserProfileID()).prop('selected',
						'selected').prop('disabled', true);
				$('select[name=ddlName]').eq(0).prop('disabled', false);
				$('select[name = ddlCollege]').eq(0).prop('disabled', false);
				$('select[name = ddlDepartment]').eq(0).prop('disabled', false);
				$('select[name = ddlPositionType]').eq(0).prop('disabled',
						false);
				$('select[name=ddlPositionTitle]').eq(0)
						.prop('disabled', false);

				myProposal.BindCurrentUserPosition(0);

				$("#dataTable tbody tr").find('select').prop('disabled', true);

				myProposal.BindPICoPISignatures();

				$('#divProposalGrid').hide();
				$('#divProposalForm').show();
				$('#divProposalAuditGrid').hide();
				$("#accordion").accordion("option", "active", 0);
				
				$('.sfButtonwrapper>p>button').show();
			}
			break;

		// Withdraw/ Archive
		case 13:
			// if (myProposal.config.proposalId != '0') {
			// myProposal.UpdateProposalStatus(myProposal.config.buttonType,
			// myProposal.config.proposalId);
			// }

			break;

		case 14:
			// if (myProposal.config.proposalId != '0') {
			// return false;
			// }
			break;

		case 15:
			if (myProposal.config.proposalId != '0') {
				if (myProposal.config.content.attr("id") != "ui-id-2") {
					$(myProposal.config.content)
							.find('input, select, textarea').each(function() {
								// $(this).addClass("ignore");
								$(this).prop('disabled', false);
							});
				} else {
					$(myProposal.config.content).find('input.AddCoPI').prop(
							'disabled', false);
					$(myProposal.config.content).find('input.AddSenior').prop(
							'disabled', false);
				}

				// if (myProposal.config.content.attr("id") == "ui-id-4") {
				// $("#txtProjectTitle").prop("disabled", true);
				// }
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

		case 18:
			positionsDetails = [];
			$.merge(positionsDetails, msg);
			$('select[name="ddlCollege"]').get(0).options.length = 0;
			$('select[name="ddlDepartment"]').get(0).options.length = 0;
			$('select[name="ddlPositionType"]').get(0).options.length = 0;
			$('select[name="ddlPositionTitle"]').get(0).options.length = 0;
			$('input[name="txtPhoneNo"]').eq(0).val('');
			break;

		case 19:
			myProposal
					.AddCoPIInvestigator(myProposal.config.investigatorButton);
			break;

		case 20:
			myProposal
					.AddSeniorPersonnelInvestigator(myProposal.config.investigatorButton);
			break;

		case 21:
			var t = myProposal.config.investigatorButton.closest('tr');
			t.find("td").wrapInner("<div style='display: block'/>").parent()
					.find("td div").slideUp(300);
			t.remove();
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
				// csscody.error('<h2>' + 'Error Message' + '</h2><p>'
				// + 'Failed to delete the proposal.' + '</p>');
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to DELETE this proposal! '
						+ msg.responseText + '</p>');
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
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to '
						+ myProposal.config.buttonType + ' this proposal! '
						+ msg.responseText + '</p>');
				break;

			case 10:
				// csscody.error('<h2>' + 'Error Message' + '</h2><p>'
				// + 'You are not allowed to DELETE this proposal! '
				// + msg.responseText + '</p>');
				break;

			case 11:
				// csscody.error('<h2>' + 'Error Message' + '</h2><p>'
				// + 'You are not allowed to perform this OPERATION! '
				// + msg.responseText + '</p>');
				break;

			case 12:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to CREATE a Proposal! '
						+ msg.responseText + '</p>');
				break;

			case 13:
				// csscody.error('<h2>' + 'Error Message' + '</h2><p>'
				// + 'You are not allowed to perform this OPERATION! '
				// + msg.responseText + '</p>');
				break;

			case 14:
				// csscody.error('<h2>' + 'Error Message' + '</h2><p>'
				// + 'You are not Allowed to View this Section! '
				// + msg.responseText + '</p>');
				// myProposal.config.event.preventDefault();
				break;

			case 15:
				// csscody.error('<h2>' + 'Error Message' + '</h2><p>'
				// + 'You are not Allowed to EDIT this Section! '
				// + msg.responseText + '</p>');
				// myProposal.config.event.preventDefault();

				if (myProposal.config.content.attr("id") == "ui-id-2") {
					$("input.AddCoPI").hide();
					$("input.AddSenior").hide();
				} else if (myProposal.config.content.attr("id") == "ui-id-26") {
					$("#fileuploader").hide();
					$('.ajax-file-upload-red').hide();
				}

				$(myProposal.config.content).find('input, select, textarea')
						.each(function() {
							// $(this).addClass("ignore");
							$(this).prop('disabled', true);

						});
				// myProposal.config.event.preventDefault();
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

			case 18:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot load user position details!' + "</p>");
				break;

			case 19:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot add Co-PI! ' + msg.responseText + "</p>");
				break;

			case 20:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot add Senior Personnel! ' + msg.responseText
						+ "</p>");
				break;

			case 21:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot delete Investigator! ' + msg.responseText
						+ "</p>");
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

		countCoPIs : function() {
			var countCoPIs = 0;
			$("#dataTable tbody tr:gt(0)").find('select:first').each(
					function(index) {
						if ($(this).val() == 1) {
							countCoPIs++;
						}
					});
			return countCoPIs;
		},

		countSeniorPersonnels : function() {
			var countSeniors = 0;
			$("#dataTable tbody tr:gt(0)").find('select:first').each(
					function(index) {
						if ($(this).val() == 2) {
							countSeniors++;
						}
					});
			return countSeniors;
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
				myProposal.config.content = "";
				myProposal.config.investigatorButton = "";
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
				myProposal.config.proposalStatus = "";
				myProposal.config.submittedByPI = "";
				myProposal.config.readyForSubmitionByPI = "";
				myProposal.config.deletedByPI = "";
				myProposal.config.chairApproval = "";
				myProposal.config.businessManagerApproval = "";
				myProposal.config.irbapproval = "";
				myProposal.config.deanApproval = "";
				myProposal.config.researchAdministratorApproval = "";
				myProposal.config.researchAdministratorWithdraw = "";
				myProposal.config.researchDirectorApproval = "";
				myProposal.config.researchDirectorDeletion = "";
				myProposal.config.researchAdministratorSubmission = "";
				myProposal.config.researchDirectorArchived = "";
				myProposal.config.buttonType = "";
				myProposal.config.arguments = [];
				myProposal.config.events = "";
				myProposal.config.content = "";
				myProposal.config.investigatorButton = "";
				// $("#accordion").accordion("option", "active",
				// 0);
			});

			$('#btnAddNew').on(
					"click",
					function() {
						myProposal.config.ajaxCallMode = 12;
						myProposal.CheckUserPermissionWithPositionType("Add",
								"Whole Proposal", myProposal.config);
					});

			$('#btnReset')
					.on(
							"click",
							function() {
								var properties = {
									onComplete : function(e) {
										if (e) {
											if (myProposal.config.proposalId == "0") {
												myProposal.ClearForm();

												$('select[name=ddlName]')
														.eq(0)
														.val(
																GPMS.utils
																		.GetUserProfileID())
														.prop('selected',
																'selected')
														.prop('disabled', true);

												myProposal
														.BindCurrentUserPosition(0);

												myProposal
														.BindPICoPISignatures();
												// $("#accordion").accordion("option",
												// "active", 0);
											}
										}
									}
								};
								csscody
										.confirm(
												"<h2>"
														+ 'Reset Confirmation'
														+ "</h2><p>"
														+ 'Are you certain you want to reset this proposal?'
														+ "</p>", properties);
							});

			// Delete
			$('#btnDeleteProposal')
					.click(
							function(event) {
								$('#ui-id-24').find('input, select, textarea')
										.each(function() {
											// $(this).addClass("ignore");
											$(this).prop('disabled', true);
										});

								var properties = {
									onComplete : function(e) {
										if (e) {
											// if (validator.form()) {
											var $buttonType = $.trim($(
													'#btnDeleteProposal')
													.text());
											$('#btnDeleteProposal')
													.disableWith('Deleting...');

											if (myProposal.config.proposalId != "0"
													&& myProposal.config.proposalStatus != "") {
												myProposal
														.DeleteSingleProposal(
																$buttonType,
																"Whole Proposal",
																myProposal.config);
											}

											$('#btnDeleteProposal')
													.enableAgain();
											event.preventDefault();
											return false;
											// } else {
											// myProposal.focusTabWithErrors("#accordion");
											// }
										}
									}
								};
								csscody
										.confirm(
												"<h2>"
														+ 'Delete Confirmation'
														+ "</h2><p>"
														+ 'Are you certain you want to delete this proposal?'
														+ "</p>", properties);
							});

			// Save
			$('#btnSaveProposal')
					.click(
							function(event) {
								if (validator.form()) {
									var properties = {
										onComplete : function(e) {
											if (e) {
												var $buttonType = $.trim($(
														'#btnSaveProposal')
														.text());
												$('#btnSaveProposal')
														.disableWith(
																'Saving...');

												if (myProposal.config.proposalRoles != ""
														&& myProposal.config.proposalId != "0"
														&& myProposal.config.proposalStatus != "") {

													myProposal.SaveProposal(
															$buttonType,
															"Whole Proposal",
															myProposal.config,
															false);
												} else if (myProposal.config.proposalId == "0") {
													myProposal.SaveProposal(
															$buttonType,
															"Whole Proposal",
															myProposal.config,
															true);
												} else {
													csscody
															.error('<h2>'
																	+ 'Error Message'
																	+ '</h2><p>'
																	+ 'You are not allowed to Save this proposal!'
																	+ '</p>');
												}

												$('#btnSaveProposal')
														.enableAgain();
												event.preventDefault();
												return false;
											}
										}
									};
									csscody
											.confirm(
													"<h2>"
															+ 'Save Confirmation'
															+ "</h2><p>"
															+ 'Are you certain you want to save this proposal?'
															+ "</p>",
													properties);
								} else {
									myProposal.focusTabWithErrors("#accordion");
								}
							});

			// Submit
			$('#btnSubmitProposal')
					.click(
							function(event) {
								var currentPositionTitle = GPMS.utils
										.GetUserPositionTitle();

								if (currentPositionTitle == "University Research Administrator"
										|| currentPositionTitle == "University Research Director") {
									$('#ui-id-24').find(
											'input, select, textarea')
											.each(
													function() {
														// $(this).addClass("ignore");
														$(this).prop(
																'disabled',
																false);
													});
								} else {
									$('#ui-id-24').find(
											'input, select, textarea').each(
											function() {
												// $(this).addClass("ignore");
												$(this).prop('disabled', true);
												$(this).removeClass("error");
											});
								}
								if (validator.form()) {
									var properties = {
										onComplete : function(e) {
											if (e) {
												var $buttonType = $.trim($(
														'#btnSubmitProposal')
														.text());
												$('#btnSubmitProposal')
														.disableWith(
																'Submitting...');

												if (myProposal.config.proposalId != "0") {
													myProposal.SaveProposal(
															$buttonType,
															"Whole Proposal",
															myProposal.config,
															false);
												}

												$('#btnSubmitProposal')
														.enableAgain();
												event.preventDefault();
												return false;
											}
										}
									};
									csscody
											.confirm(
													"<h2>"
															+ 'Submit Confirmation'
															+ "</h2><p>"
															+ 'Are you certain you want to submit this proposal?'
															+ "</p>",
													properties);
								} else {
									myProposal.focusTabWithErrors("#accordion");
								}
							});

			// Approve
			$('#btnApproveProposal')
					.click(
							function(event) {
								var currentPositionTitle = GPMS.utils
										.GetUserPositionTitle();

								if (currentPositionTitle == "University Research Administrator"
										|| currentPositionTitle == "University Research Director") {
									$('#ui-id-24').find(
											'input, select, textarea')
											.each(
													function() {
														// $(this).addClass("ignore");
														$(this).prop(
																'disabled',
																false);
													});
								} else {
									$('#ui-id-24').find(
											'input, select, textarea').each(
											function() {
												// $(this).addClass("ignore");
												$(this).prop('disabled', true);
												$(this).removeClass("error");
											});
								}

								if (validator.form()) {
									var properties = {
										onComplete : function(e) {
											if (e) {
												var $buttonType = $.trim($(
														'#btnApproveProposal')
														.text());
												$('#btnApproveProposal')
														.disableWith(
																'Approving...');

												if (myProposal.config.proposalRoles == ""
														&& myProposal.config.proposalId != "0"
														&& myProposal.config.proposalStatus != "") {
													myProposal.SaveProposal(
															$buttonType,
															"Whole Proposal",
															myProposal.config,
															false);
												}

												$('#btnApproveProposal')
														.enableAgain();
												event.preventDefault();
												return false;
											}
										}
									};
									csscody
											.confirm(
													"<h2>"
															+ 'Approve Confirmation'
															+ "</h2><p>"
															+ 'Are you certain you want to approve this proposal?'
															+ "</p>",
													properties);
								} else {
									myProposal.focusTabWithErrors("#accordion");
								}
							});

			// Disapprove
			$('#btnDisapproveProposal')
					.click(
							function(event) {
								$('#ui-id-24').find('input, select, textarea')
										.each(function() {
											// $(this).addClass("ignore");
											$(this).prop('disabled', true);
											$(this).removeClass("error");
										});

								if (validator.form()) {
									var properties = {
										onComplete : function(e) {
											if (e) {
												var $buttonType = $
														.trim($(
																'#btnDisapproveProposal')
																.text());
												$('#btnDisapproveProposal')
														.disableWith(
																'Disapproving...');

												if (myProposal.config.proposalRoles == ""
														&& myProposal.config.proposalId != "0"
														&& myProposal.config.proposalStatus != "") {
													myProposal.SaveProposal(
															$buttonType,
															"Whole Proposal",
															myProposal.config,
															false);
												}

												$('#btnDisapproveProposal')
														.enableAgain();
												event.preventDefault();
												return false;
											}
										}
									};
									csscody
											.confirm(
													"<h2>"
															+ 'Disapprove Confirmation'
															+ "</h2><p>"
															+ 'Are you certain you want to disapprove this proposal?'
															+ "</p>",
													properties);
								} else {
									myProposal.focusTabWithErrors("#accordion");
								}
							});

			// Withdraw
			$('#btnWithdrawProposal')
					.click(
							function(event) {
								$('#ui-id-24').find('input, select, textarea')
										.each(function() {
											// $(this).addClass("ignore");
											$(this).prop('disabled', true);
											$(this).removeClass("error");
										});

								// if (validator.form()) {
								var properties = {
									onComplete : function(e) {
										if (e) {
											var $buttonType = $.trim($(
													'#btnWithdrawProposal')
													.text());
											$('#btnWithdrawProposal')
													.disableWith(
															'Withdrawing...');

											if (myProposal.config.proposalRoles == ""
													&& myProposal.config.proposalId != "0"
													&& myProposal.config.proposalStatus != "") {
												myProposal
														.UpdateProposalStatus(
																$buttonType,
																"Whole Proposal",
																myProposal.config);
											}

											$('#btnWithdrawProposal')
													.enableAgain();
											event.preventDefault();
											return false;
										}
									}
								};
								csscody
										.confirm(
												"<h2>"
														+ 'Withdraw Confirmation'
														+ "</h2><p>"
														+ 'Are you certain you want to withdraw this proposal?'
														+ "</p>", properties);
								// } else {
								// myProposal.focusTabWithErrors("#accordion");
								// }
							});

			// Archive
			$('#btnArchiveProposal')
					.click(
							function(event) {
								var currentPositionTitle = GPMS.utils
										.GetUserPositionTitle();

								$('#ui-id-24').find('input, select, textarea')
										.each(function() {
											// $(this).addClass("ignore");
											$(this).prop('disabled', true);
											$(this).removeClass("error");
										});

								// if (validator.form()) {
								var properties = {
									onComplete : function(e) {
										if (e) {
											var $buttonType = $.trim($(
													'#btnArchiveProposal')
													.text());
											$('#btnArchiveProposal')
													.disableWith('Archiving...');

											if (myProposal.config.proposalRoles == ""
													&& myProposal.config.proposalId != "0"
													&& myProposal.config.proposalStatus != "") {
												myProposal
														.UpdateProposalStatus(
																$buttonType,
																"Whole Proposal",
																myProposal.config);
											}

											$('#btnArchiveProposal')
													.enableAgain();
											event.preventDefault();
											return false;
										}
									}
								};
								csscody
										.confirm(
												"<h2>"
														+ 'Archive Confirmation'
														+ "</h2><p>"
														+ 'Are you certain you want to archive this proposal?'
														+ "</p>", properties);
								// } else {
								// myProposal.focusTabWithErrors("#accordion");
								// }
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

			$("input[type=button].AddCoPI")
					.on(
							"click",
							function() {
								var currentProposalRoles = myProposal.config.proposalRoles
										.split(', ');
								if ($.inArray("PI", currentProposalRoles) !== -1
										|| $.inArray("Co-PI",
												currentProposalRoles) !== -1
										|| myProposal.config.proposalId == "0") {
									if ($(this).prop("name") == "DeleteOption") {
										var t = $(this).closest('tr');
										myProposal.config.investigatorButton = $(this);
										var properties = {
											onComplete : function(e) {
												if (e) {
													if (t.hasClass("trStatic")) {
														t
																.find("td")
																.wrapInner(
																		"<div style='display: block'/>")
																.parent()
																.find("td div")
																.slideUp(300);
														t.remove();
													} else {
														var subSection = "";
														if (t.find(
																"select:first")
																.val() == 1) {
															subSection = "InvestigatorInformation.Co-PI";
														} else if (t.find(
																"select:first")
																.val() == 2) {
															subSection = "InvestigatorInformation.Senior-Personnel";
														}
														myProposal.config.ajaxCallMode = 21;

														myProposal
																.CheckUserPermissionForInvestigator(
																		"Delete",
																		subSection,
																		myProposal.config);
													}
												}
											}
										};
										csscody
												.confirm(
														"<h2>"
																+ 'Delete Confirmation'
																+ "</h2><p>"
																+ 'Are you certain you want to delete this investigator?'
																+ "</p>",
														properties);

									} else if ($(this).prop("name") == "AddCoPI") {
										if (myProposal.countCoPIs() < 4) {
											if (myProposal.config.proposalId == "0") {
												myProposal
														.AddCoPIInvestigator($(this));
											} else {
												myProposal.config.ajaxCallMode = 19;
												myProposal.config.investigatorButton = $(this);

												var $buttonType = $
														.trim($(this).val());

												myProposal
														.CheckUserPermissionForInvestigator(
																$buttonType,
																"InvestigatorInformation.Co-PI",
																myProposal.config);
											}
										} else {
											csscody
													.error('<h2>'
															+ 'Error Message'
															+ '</h2><p>'
															+ 'Maximum of Co-PIs is 4!</p>');
										}
									}
								}

							});

			$("input[type=button].AddSenior")
					.on(
							"click",
							function() {
								var currentProposalRoles = myProposal.config.proposalRoles
										.split(', ');
								if ($.inArray("PI", currentProposalRoles) !== -1
										|| $.inArray("Co-PI",
												currentProposalRoles) !== -1
										|| myProposal.config.proposalId == "0") {
									if (myProposal.countSeniorPersonnels() < 10) {
										if (myProposal.config.proposalId == "0") {
											myProposal
													.AddSeniorPersonnelInvestigator($(this));
										} else {
											myProposal.config.ajaxCallMode = 20;
											myProposal.config.investigatorButton = $(this);

											var $buttonType = $.trim($(this)
													.val());
											myProposal
													.CheckUserPermissionForInvestigator(
															$buttonType,
															"InvestigatorInformation.Senior-Personnel",
															myProposal.config);
										}
									} else {
										csscody
												.error('<h2>'
														+ 'Error Message'
														+ '</h2><p>'
														+ 'Maximum of Senior Personnel is 10!</p>');
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