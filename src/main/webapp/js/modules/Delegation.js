var delegation = '';

$(function() {

	if (isAdmin == "false") {
		if (userProfileId == "null") {
			window.location = 'Login.jsp';
		}
	} else {
		if (userProfileId == "null") {
			window.location = 'Login.jsp';
		} else {
			window.location = 'Dashboard.jsp';
		}
	}

	jQuery.fn.exists = function() {
		return this.length > 0;
	}

	// $.validator.unobtrusive.parse(#form1);
	$.validator.setDefaults({
		ignore : []
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

	var validator = $("#form1")
			.validate(
					{
						rules : {
							delegationFrom : {
								required : true,
								dpDate : true,
								maxlength : 10
							},
							delegationTo : {
								required : true,
								dpDate : true,
								maxlength : 10
							},
							delegationReason : {
								required : true,
								minlength : 5,
								maxlength : 250
							}
						},
						errorElement : "span",
						messages : {
							delegationFrom : {
								required : "Please enter delegation period from date",
								dpDate : "Please enter valid date",
								maxlength : "This is not a valid Date"
							},
							delegationTo : {
								required : "Please enter delegation period to date",
								dpDate : "Please enter valid date",
								maxlength : "This is not a valid Date"
							},
							delegationReason : {
								required : "Please enter your delegation reason.",
								minlength : "Your delegation reason must be at least 5 characters long",
								maxlength : "Your delegation reason must be at most 250 characters long"
							}
						}
					});

	delegation = {
		config : {
			isPostBack : false,
			async : false,
			cache : false,
			type : 'POST',
			contentType : "application/json; charset=utf-8",
			data : '{}',
			dataType : 'json',
			rootURL : GPMS.utils.GetGPMSServicePath(),
			baseURL : GPMS.utils.GetGPMSServicePath() + "delegations/",
			method : "",
			url : "",
			ajaxCallMode : 0,
			delegationId : "0"
		},

		ajaxCall : function(config) {
			$
					.ajax({
						type : delegation.config.type,
						beforeSend : function(request) {
							request.setRequestHeader('GPMS-TOKEN', _aspx_token);
							request.setRequestHeader("UName", GPMS.utils
									.GetUserName());
							request.setRequestHeader("PID", GPMS.utils
									.GetUserProfileID());
							request.setRequestHeader("PType", "v");
							request.setRequestHeader('Escape', '0');
						},
						contentType : delegation.config.contentType,
						cache : delegation.config.cache,
						async : delegation.config.async,
						url : delegation.config.url,
						data : delegation.config.data,
						dataType : delegation.config.dataType,
						success : delegation.ajaxSuccess,
						error : delegation.ajaxFailure
					});
		},

		SearchDelegations : function() {
			var delegatee = $.trim($("#txtSearchDelegatee").val());
			var delegatedFrom = $.trim($("#txtSearchDelegatedFrom").val());
			var delegatedTo = $.trim($("#txtSearchDelegatedTo").val());

			var delegatedAction = $.trim($('#ddlSearchDelegatedAction').val()) == "" ? null
					: $.trim($('#ddlSearchDelegatedAction').val()) == "0" ? null
							: $.trim($('#ddlSearchDelegatedAction').val());

			var isRevoked = $.trim($('#ddlSearchIsRevoked').val()) == "" ? null
					: $.trim($('#ddlSearchIsRevoked').val()) == "0" ? null : $
							.trim($('#ddlSearchIsRevoked').val());

			if (delegatee.length < 1) {
				delegatee = null;
			}
			if (delegatedFrom.length < 1) {
				delegatedFrom = null;
			}
			if (delegatedTo.length < 1) {
				delegatedTo = null;
			}
			if (delegatedAction.length < 1) {
				delegatedAction = null;
			}
			if (isRevoked.length < 1) {
				isRevoked = null;
			}

			delegation.BindDelegationGrid(delegatee, delegatedFrom,
					delegatedTo, delegatedAction, isRevoked);
		},

		BindDelegationGrid : function(delegatee, delegatedFrom, delegatedTo,
				delegatedAction, isRevoked) {
			this.config.url = this.config.baseURL;
			this.config.method = "GetDelegationsList";
			var offset_ = 1;
			var current_ = 1;
			var perpage = ($("#gdvDelegations_pagesize").length > 0) ? $(
					"#gdvDelegations_pagesize :selected").text() : 10;

			var delegationBindObj = {
				delegatee : delegatee,
				delegatedFrom : delegatedFrom,
				delegatedTo : delegatedTo,
				delegatedAction : delegatedAction,
				isRevoked : isRevoked
			};

			this.config.data = {
				delegationBindObj : delegationBindObj
			};
			var data = this.config.data;

			$("#gdvDelegations").sagegrid({
				url : this.config.url,
				functionMethod : this.config.method,
				colModel : [ {
					display : 'Delegation ID',
					cssclass : 'cssClassHeadCheckBox',
					coltype : 'checkbox',
					align : 'center',
					checkFor : '12',
					elemClass : 'attrChkbox',
					elemDefault : false,
					controlclass : 'attribHeaderChkbox'
				}, {
					display : 'Delegatee',
					name : 'delegatee',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Delegatee Department',
					name : 'delegatee_department',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Delegatee PositionType',
					name : 'delegatee_position_type',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Delegated Action',
					name : 'delegated_action',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Delegation Reason',
					name : 'delegation_reason',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Date Created',
					name : 'date_created',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					type : 'date',
					format : 'yyyy/MM/dd hh:mm:ss a'
				}, {
					display : 'Delegated From',
					name : 'delegated_from',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					type : 'date',
					format : 'yyyy/MM/dd hh:mm:ss a'
				}, {
					display : 'Delegated To',
					name : 'delegated_to',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					type : 'date',
					format : 'yyyy/MM/dd hh:mm:ss a'
				}, {
					display : 'Last Audited',
					name : 'last_audited',
					cssclass : 'cssClassHeadDate',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					type : 'date',
					format : 'yyyy/MM/dd hh:mm:ss a'
				}, {
					display : 'Last Audited By',
					name : 'last_audited_by',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Last Audited Action',
					name : 'last_audited_action',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Is Revoked?',
					name : 'is_revoked',
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

				buttons : [ {
					display : 'Edit',
					name : 'edit',
					enable : true,
					_event : 'click',
					trigger : '1',
					callMethod : 'delegation.EditDelegation',
					arguments : '1'
				}, {
					display : 'Revoke',
					name : 'revoke',
					enable : true,
					_event : 'click',
					trigger : '2',
					callMethod : 'delegation.RevokeDelegation',
					arguments : '1'
				}, {
					display : 'View Change Logs',
					name : 'changelog',
					enable : true,
					_event : 'click',
					trigger : '3',
					callMethod : 'delegation.ViewChangeLogs',
					arguments : '1'
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
					13 : {
						sorter : false
					}
				}
			});
		},

		EditDelegation : function(tblID, argus) {
			switch (tblID) {
			case "gdvDelegations":
				// $('#accordion-expand-holder').show();
				$("#accordion").accordion("option", "active", false);

				$('#lblFormHeading').html(
						'Edit Delegation Details for: ' + argus[1]);

				$("#lblDelegationDateReceived").text(argus[3]);

				delegation.ClearForm();

				delegation.config.proposalRoles = $.trim(argus[5]);
				delegation.config.delegationId = argus[0];

				delegation.config.submittedByPI = argus[7];
				delegation.config.readyForSubmitionByPI = argus[8];
				delegation.config.deletedByPI = argus[9];
				delegation.config.chairApproval = argus[10];
				delegation.config.businessManagerApproval = argus[11];
				delegation.config.irbapproval = argus[12];
				delegation.config.deanApproval = argus[13];
				delegation.config.researchAdministratorApproval = argus[14];
				delegation.config.researchAdministratorWithdraw = argus[15];
				delegation.config.researchDirectorApproval = argus[16];
				delegation.config.researchDirectorDeletion = argus[17];
				delegation.config.researchAdministratorSubmission = argus[18];
				delegation.config.researchDirectorArchived = argus[19];

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

				$('#ddlDelegationStatus option').length = 0;
				$('#ddlDelegationStatus')
						.append(new Option(argus[6], argus[6])).prop(
								'disabled', true);

				delegation.config.proposalStatus = argus[6];

				delegation.BindUserPositionDetailsForADelegation(argus[4]);

				delegation.BindDelegationDetailsByDelegationId(argus[0]);

				$("#btnReset").hide();

				// Certification/ Signatures Info
				delegation.BindAllSignatureForADelegation(argus[0], argus[20]);

				// Delegation Info

				$("#dataTable tbody tr:gt(0)").find('input.AddSenior').remove();
				$("#fileuploader").show();
				$('input.AddCoPI').show();
				$('input.AddSenior').show();
				break;
			default:
				break;
			}
		},

		BindUserPositionDetailsForADelegation : function(users) {
			if (users != null) {
				this.config.url = this.config.rootURL + "users/"
						+ "GetUserPositionDetailsForADelegation";
				this.config.data = JSON2.stringify({
					userIds : users
				});
				this.config.ajaxCallMode = 6;
				this.ajaxCall(this.config);
			}
			return false;
		},

		BindDelegationDetailsByDelegationId : function(proposalId) {
			this.config.url = this.config.baseURL
					+ "GetDelegationDetailsByDelegationId";
			this.config.data = JSON2.stringify({
				proposalId : proposalId
			});
			this.config.ajaxCallMode = 4;
			this.ajaxCall(this.config);
			return false;
		},

		FillForm : function(response) {

		},

		SearchDelegationAuditLogs : function() {
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

			delegation.BindDelegationAuditLogGrid(
					delegation.config.delegationId, action, auditedBy,
					activityOnFrom, activityOnTo);
		},

		BindDelegationAuditLogGrid : function(proposalId, action, auditedBy,
				activityOnFrom, activityOnTo) {
			this.config.url = this.config.baseURL;
			this.config.method = "GetDelegationAuditLogList";
			var offset_ = 1;
			var current_ = 1;
			var perpage = ($("#gdvDelegationsAuditLog_pagesize").length > 0) ? $(
					"#gdvDelegationsAuditLog_pagesize :selected").text()
					: 10;

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

			$("#gdvDelegationsAuditLog").sagegrid({
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
			case "gdvDelegations":
				delegation.config.delegationId = argus[0];
				if (argus[0] != '0') {
					$('#lblLogsHeading').html(
							'View Audit Logs for: ' + argus[1]);

					if (argus[2] != null && argus[2] != "") {
						$('#tblLastAuditedInfo').show();
						$('#lblLastUpdatedOn').html(argus[2]);
						$('#lblLastUpdatedBy').html(argus[3]);
						$('#lblActivity').html(argus[4]);
					} else {
						$('#tblLastAuditedInfo').hide();
					}
					// Get Audit Logs
					// $("#gdvDelegationsAuditLog").empty();
					// $("#gdvDelegationsAuditLog_Pagination").remove();

					delegation.BindDelegationAuditLogGrid(argus[0], null, null,
							null, null);

					$('#divDelegationGrid').hide();
					$('#divDelegationForm').hide();
				}
				break;
			default:
				break;
			}
		},

		RevokeDelegation : function(tblID, argus) {
			switch (tblID) {
			case "gdvDelegations":
				var proposal_roles = $.trim(argus[1]);
				if (argus[2].toLowerCase() != "yes") {
					var properties = {
						onComplete : function(e) {
							if (e) {
								delegation.config.proposalRoles = proposal_roles;
								delegation.config.delegationId = argus[0];
								delegation.config.proposalStatus = argus[3];
								delegation.config.submittedByPI = argus[4];
								delegation.config.readyForSubmissionByPI = argus[5];
								delegation.config.deletedByPI = argus[6];
								delegation.config.chairApproval = argus[7];
								delegation.config.businessManagerApproval = argus[8];
								delegation.config.irbapproval = argus[9];
								delegation.config.deanApproval = argus[10];
								delegation.config.researchAdministratorApproval = argus[11];
								delegation.config.researchAdministratorWithdraw = argus[12];
								delegation.config.researchDirectorApproval = argus[13];
								delegation.config.researchDirectorDeletion = argus[14];
								delegation.config.researchAdministratorSubmission = argus[15];
								delegation.config.researchDirectorArchived = argus[16];

								delegation.DeleteSingleDelegation("Revoke",
										delegation.config);
							}
						}
					};
					csscody
							.confirm(
									"<h2>"
											+ 'Revoke Confirmation'
											+ "</h2><p>"
											+ 'Are you certain you want to revoke this proposal?'
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

		ConfirmDeleteMultiple : function(delegation_ids, event) {
			if (event) {
				delegation.DeleteMultipleDelegations(delegation_ids);
			}
		},

		DeleteMultipleDelegations : function(_proposalIds) {
			// this.config.dataType = "html";
			this.config.url = this.config.baseURL
					+ "DeleteMultipleDelegationsByAdmin";
			this.config.data = JSON2.stringify({
				proposalIds : _proposalIds,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 3;
			this.ajaxCall(this.config);
			return false;
		},

		DeleteSingleDelegation : function(buttonType, config) {
			this.config.url = this.config.baseURL + "RevokeDelegationByAdmin";
			this.config.data = JSON2.stringify({
				proposalId : config.proposalId,
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

			delegation.config.delegationId = '0';

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
			delegation.BindAllPositionDetailsForAUser($('select[name=ddlName]')
					.eq(0).val());

			delegation.BindUserMobileNo($('select[name="ddlName"]').eq(
					rowIndexVal).val());

			delegation.BindCollegeDropDown($('select[name="ddlName"]').eq(
					rowIndexVal).val());
			delegation.BindDepartmentDropDown($('select[name="ddlName"]').eq(
					rowIndexVal).val(), $('select[name="ddlCollege"]').eq(
					rowIndexVal).val());
			delegation.BindPositionTypeDropDown($('select[name="ddlName"]').eq(
					rowIndexVal).val(), $('select[name="ddlCollege"]').eq(
					rowIndexVal).val(), $('select[name="ddlDepartment"]').eq(
					rowIndexVal).val());
			delegation.BindPositionTitleDropDown($('select[name="ddlName"]')
					.eq(rowIndexVal).val(), $('select[name="ddlCollege"]').eq(
					rowIndexVal).val(), $('select[name="ddlDepartment"]').eq(
					rowIndexVal).val(), $('select[name="ddlPositionType"]').eq(
					rowIndexVal).val());
			return false;
		},

		BindDefaultUserPosition : function(rowIndexVal) {
			// For form Dropdown Binding
			delegation.BindAllPositionDetailsForAUser($(
					'select[name="ddlName"]').eq(rowIndexVal).val());

			delegation.BindUserMobileNo($('select[name="ddlName"]').eq(
					rowIndexVal).val());

			delegation.BindCollegeDropDown($('select[name="ddlName"]').eq(
					rowIndexVal).val());
			delegation.BindDepartmentDropDown($('select[name="ddlName"]').eq(
					rowIndexVal).val(), $('select[name="ddlCollege"]').eq(
					rowIndexVal).val());
			delegation.BindPositionTypeDropDown($('select[name="ddlName"]').eq(
					rowIndexVal).val(), $('select[name="ddlCollege"]').eq(
					rowIndexVal).val(), $('select[name="ddlDepartment"]').eq(
					rowIndexVal).val());
			delegation.BindPositionTitleDropDown($('select[name="ddlName"]')
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
					+ 'PI" title="Signed Date" class="sfInputbox" placeholder="Signed Date" type="text" required="true" readonly="true" onfocus="delegation.BindCurrentDateTime(this);"></td><td><textarea rows="2" cols="26" name="proposalNotes'
					+ $('select[name="ddlName"]').eq(0).val()
					+ 'PI" required="true" title="Delegation Notes" class="cssClassTextArea"></textarea></td></tr>';
			$(cloneRow).appendTo("#trSignPICOPI tbody");

			$('#trSignPICOPI tbody tr:last').data("allowchange", "true").data(
					"allowsign", "true");
		},

		InitializeAccordion : function() {
			var icons = {
				header : "ui-icon-circle-arrow-e",
				activeHeader : "ui-icon-circle-arrow-s"
			};

			var $accordion = $("#accordion").accordion({
				heightStyle : "content",
				icons : icons,
				active : false,
				collapsible : true
			});
			// delegation.SelectFirstAccordion();
			// $("#accordion").accordion("option", "active", 0);
			return false;
		},

		BindAllSignatureForADelegation : function(proposalId, irbSignRequired) {
			delegation.config.url = delegation.config.baseURL
					+ "GetAllSignatureForADelegation";
			delegation.config.data = JSON2.stringify({
				proposalId : proposalId,
				irbApprovalRequired : irbSignRequired
			});
			delegation.config.ajaxCallMode = 8;
			delegation.ajaxCall(delegation.config);
		},

		BindCurrentDateTime : function(obj) {
			$(obj).val($.format.date(new Date(), 'yyyy/MM/dd hh:mm:ss a'));
			return false;
		},

		SaveDelegation : function(buttonType, config) {
			// if (validator.form()) {

			var $projectTitle = $('#txtProjectTitle');
			var projectTitle = $.trim($projectTitle.val());

			var projectInfo = {
				ProjectTitle : $.trim($("#txtProjectTitle").val()),
				ProjectType : $("#ddlProjectType").val(),
				TypeOfRequest : $("#ddlTypeOfRequest").val(),
				ProjectLocation : $("#ddlLocationOfProject").val(),
				DueDate : $("#txtDueDate").val(),
				ProjectPeriodFrom : $("#txtProjectPeriodFrom").val(),
				ProjectPeriodTo : $("#txtProjectPeriodTo").val()
			};

			delegation.AddDelegationInfo(buttonType, config, proposalInfo);
		},

		AddDelegationInfo : function(buttonType, config, info) {
			alert(buttonType);

			this.config.url = this.config.baseURL
					+ "SaveUpdateDelegationByAdmin";
			this.config.data = JSON2.stringify({
				buttonType : buttonType,
				proposalInfo : info,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 9;
			this.config.buttonType = buttonType;
			this.ajaxCall(this.config);
			return false;
		},

		BindUserDropDown : function() {
			// Used User REST API instead Delegation
			this.config.url = this.config.rootURL + "users/"
					+ "GetAllUserDropdown";
			this.config.data = "{}";
			this.config.ajaxCallMode = 5;
			this.ajaxCall(this.config);
			return false;
		},

		BindAllUsersAndPositions : function() {
			// Used User REST API instead Delegation
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

		ajaxSuccess : function(msg) {
			switch (delegation.config.ajaxCallMode) {
			case 0:
				break;

			case 1: // For Delegation Status Dropdown Binding for both form and
				// search
				$('#ddlSearchDelegationStatus option').length = 1;
				$('#ddlDelegationStatus option').length = 0;

				$.each(msg, function(index, item) {
					$('#ddlSearchDelegationStatus').append(
							new Option(item.statusValue, item.statusKey));
					// $('#ddlDelegationStatus').append(
					// new Option(item.statusValue, item.statusKey));
				});
				break;

			case 2: // Single Delegation Revoke
				delegation.BindDelegationGrid(null, null, null, null, null,
						null, null);
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'Delegation has been deleted successfully.' + "</p>");

				$('#divDelegationForm').hide();
				$('#divDelegationGrid').show();
				delegation.config.delegationId = '0';
				delegation.config.proposalRoles = "";
				delegation.config.buttonType = "";
				delegation.config.arguments = [];
				delegation.config.events = "";
				delegation.config.content = "";
				delegation.config.investigatorButton = "";
				break;
			break;

		case 3: // Multiple Delegation Revoke
			SageData.Get("gdvDelegations").Arr.length = 0;
			delegation.BindDelegationGrid(null, null, null, null, null, null,
					null);
			csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
					+ 'Selected proposal(s) has been deleted successfully.'
					+ "</p>");
			break;

		case 4:// For Delegation Edit Action
			delegation.FillForm(msg);

			$('#divDelegationGrid').hide();
			$('#divDelegationForm').show();
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

		case 9:
			delegation.BindDelegationGrid(null, null, null, null, null, null,
					null);
			$('#divDelegationGrid').show();

			// $("#accordion").accordion("option", "active", 0);

			// if (delegation.config.delegationId != "0") {
			var changeMade = "Saved";
			switch (delegation.config.buttonType) {
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
					+ 'Delegation has been ' + changeMade + ' successfully.'
					+ "</p>");
			// } else {
			// csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
			// + 'Delegation has been Saved successfully.' + "</p>");
			// }
			$('#divDelegationForm').hide();

			delegation.config.delegationId = '0';
			delegation.config.proposalRoles = "";
			delegation.config.buttonType = "";
			delegation.config.arguments = [];
			delegation.config.events = "";
			delegation.config.content = "";
			delegation.config.investigatorButton = "";
			break;

		}
	},

		ajaxFailure : function(msg) {
			switch (delegation.config.ajaxCallMode) {
			case 0:
				break;
			case 1:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load Delegation Status.' + '</p>');
				break;
			case 2:
				// csscody.error('<h2>' + 'Error Message' + '</h2><p>'
				// + 'Failed to revoke the proposal.' + '</p>');
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to DELETE this proposal! '
						+ msg.responseText + '</p>');
				break;
			case 3:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to revoke multiple proposals.' + '</p>');
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
						+ delegation.config.buttonType + ' this proposal! '
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
						+ 'You are not allowed to CREATE a Delegation! '
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
				// delegation.config.event.preventDefault();
				break;

			case 15:
				// csscody.error('<h2>' + 'Error Message' + '</h2><p>'
				// + 'You are not Allowed to EDIT this Section! '
				// + msg.responseText + '</p>');
				// delegation.config.event.preventDefault();

				if (delegation.config.content.attr("id") == "ui-id-2") {
					$("input.AddCoPI").hide();
					$("input.AddSenior").hide();
				} else if (delegation.config.content.attr("id") == "ui-id-26") {
					$("#fileuploader").hide();
					$('.ajax-file-upload-red').hide();
				}

				$(delegation.config.content).find('input, select, textarea')
						.each(function() {
							// $(this).addClass("ignore");
							$(this).prop('disabled', true);

						});
				// delegation.config.event.preventDefault();
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
						+ 'Cannot revoke Investigator! ' + msg.responseText
						+ "</p>");
				break;

			}
		},

		ExportToExcel : function(projectTitle, usernameBy, submittedOnFrom,
				submittedOnTo, totalCostsFrom, totalCostsTo, proposalStatus) {
			var proposalBindObj = {
				ProjectTitle : projectTitle,
				UsernameBy : usernameBy,
				SubmittedOnFrom : submittedOnFrom,
				SubmittedOnTo : submittedOnTo,
				TotalCostsFrom : totalCostsFrom,
				TotalCostsTo : totalCostsTo,
				DelegationStatus : proposalStatus
			};

			this.config.data = JSON2.stringify({
				proposalBindObj : proposalBindObj,
				gpmsCommonObj : gpmsCommonObj()
			});

			this.config.url = this.config.baseURL
					+ "AllDelegationsExportToExcel";
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

			this.config.url = this.config.baseURL
					+ "DelegationLogsExportToExcel";
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
			$("#txtSearchDelegatedFrom").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						onSelect : function(selectedDate) {
							$("#txtSearchDelegatedTo").datepicker("option",
									"minDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});
			$("#txtSearchDelegatedTo").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						onSelect : function(selectedDate) {
							$("#txtSearchDelegatedFrom").datepicker("option",
									"maxDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});
			// delegation.BindDelegationGrid(null, null, null, null, null, null,
			// null);
			$('#divDelegationForm').show();
			$('#divDelegationGrid').show();

			// For Filling Form
			$("#txtDelegationFrom").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						onSelect : function(selectedDate) {
							$("#txtDelegationTo").datepicker("option",
									"minDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});
			$("#txtDelegationTo").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						onSelect : function(selectedDate) {
							$("#txtDelegationFrom").datepicker("option",
									"maxDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});

			// delegation.BindAllUsersAndPositions();

			delegation.BindUserDropDown();		

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

						delegation.LogsExportToExcel(
								delegation.config.delegationId, action,
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
										'#ddlSearchDelegationStatus').val()) == "" ? null
										: $
												.trim($(
														'#ddlSearchDelegationStatus')
														.val()) == "0" ? null
												: $
														.trim($(
																'#ddlSearchDelegationStatus')
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

								delegation.ExportToExcel(projectTitle,
										usernameBy, submittedOnFrom,
										submittedOnTo, totalCostsFrom,
										totalCostsTo, proposalStatus);
							});

			$('#btnBack').on("click", function() {
				$('#divDelegationGrid').show();
				$('#divDelegationForm').hide();
				delegation.config.delegationId = '0';
				delegation.config.proposalRoles = "";
				delegation.config.proposalStatus = "";
				delegation.config.submittedByPI = "";
				delegation.config.readyForSubmitionByPI = "";
				delegation.config.deletedByPI = "";
				delegation.config.chairApproval = "";
				delegation.config.businessManagerApproval = "";
				delegation.config.irbapproval = "";
				delegation.config.deanApproval = "";
				delegation.config.researchAdministratorApproval = "";
				delegation.config.researchAdministratorWithdraw = "";
				delegation.config.researchDirectorApproval = "";
				delegation.config.researchDirectorDeletion = "";
				delegation.config.researchAdministratorSubmission = "";
				delegation.config.researchDirectorArchived = "";
				delegation.config.buttonType = "";
				delegation.config.arguments = [];
				delegation.config.events = "";
				delegation.config.content = "";
				delegation.config.investigatorButton = "";
				// $("#accordion").accordion("option", "active",
				// 0);
			});

			$('#btnAddNew').on("click", function() {
				if (delegation.config.delegationId == '0') {
					$('#lblFormHeading').html('New Delegation Details');

					$("#btnReset").show();
					$("#btnSaveDelegation").show();
					$("#btnRevokeDelegation").hide();

					delegation.ClearForm();

					delegation.BindCurrentUserPosition(0);

					$('#divDelegationGrid').hide();
					$('#divDelegationForm').show();
				}
			});

			$('#btnReset')
					.on(
							"click",
							function() {
								var properties = {
									onComplete : function(e) {
										if (e) {
											if (delegation.config.delegationId == "0") {
												delegation.ClearForm();

												delegation
														.BindCurrentUserPosition(0);

												// delegation
												// .BindPICoPISignatures();
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

			// Revoke
			$('#btnRevokeDelegation')
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
													'#btnRevokeDelegation')
													.text());
											$('#btnRevokeDelegation')
													.disableWith('Revoking...');

											if (delegation.config.delegationId != "0"
													&& delegation.config.proposalStatus != "") {
												delegation
														.DeleteSingleDelegation(
																$buttonType,
																delegation.config);
											}

											$('#btnRevokeDelegation')
													.enableAgain();
											event.preventDefault();
											return false;
											// } else {
											// delegation.focusTabWithErrors("#accordion");
											// }
										}
									}
								};
								csscody
										.confirm(
												"<h2>"
														+ 'Revoke Confirmation'
														+ "</h2><p>"
														+ 'Are you certain you want to revoke this proposal?'
														+ "</p>", properties);
							});

			// Save
			$('#btnSaveDelegation')
					.click(
							function(event) {
								if (validator.form()) {
									var properties = {
										onComplete : function(e) {
											if (e) {
												var $buttonType = $.trim($(
														'#btnSaveDelegation')
														.text());
												$('#btnSaveDelegation')
														.disableWith(
																'Saving...');

												delegation.SaveDelegation(
														$buttonType,
														delegation.config);

												$('#btnSaveDelegation')
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
									delegation.focusTabWithErrors("#accordion");
								}
							});

			$("#btnSearchDelegation").on("click", function() {
				// if ($("#form1").valid()) {
				delegation.SearchDelegations();
				// }
				return false;
			});

			$("#btnSearchDelegationAuditLog").on("click", function() {
				delegation.SearchDelegationAuditLogs();
				return false;
			});

			$("#ddlInstitutionalCommitmentCost").on("change", function() {
				if ($("#ddlInstitutionalCommitmentCost").val() == "1") {
					$("#lblConfirmCommitment").show();
				} else {
					$("#lblConfirmCommitment").hide();
				}
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

			$(
					'#txtSearchProjectTitle,#txtSearchUserName,#txtSearchSubmittedOnFrom,#txtSearchSubmittedOnTo,#txtSearchTotalCostsFrom,#txtSearchTotalCostsTo,#ddlSearchDelegationStatus',
					'#ddlSearchUserRole').keyup(function(event) {
				if (event.keyCode == 13) {
					$("#btnSearchDelegation").click();
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
	delegation.init();
});