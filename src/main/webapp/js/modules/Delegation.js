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
			delegationId : "0",
			delegateeId : "",
			delegateeEmail : "",
			delegateeCollege : "",
			delegateeDepartment : "",
			delegateePositionType : "",
			delegateePositionTitle : ""
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
			var createdFrom = $.trim($("#txtSearchCreatedFrom").val());
			var createdTo = $.trim($("#txtSearchCreatedTo").val());

			var delegatedAction = $.trim($('#ddlSearchDelegatedAction').val()) == "" ? null
					: $.trim($('#ddlSearchDelegatedAction').val()) == "0" ? null
							: $.trim($('#ddlSearchDelegatedAction').val());

			var isRevoked = $.trim($("#ddlSearchIsRevoked").val()) == "" ? null
					: $.trim($("#ddlSearchIsRevoked").val()) == "True" ? true
							: false;

			if (delegatee.length < 1) {
				delegatee = null;
			}
			if (createdFrom.length < 1) {
				createdFrom = null;
			}
			if (createdTo.length < 1) {
				createdTo = null;
			}

			delegation.BindDelegationGrid(delegatee, createdFrom, createdTo,
					delegatedAction, isRevoked);
		},

		BindDelegationGrid : function(delegatee, createdFrom, createdTo,
				delegatedAction, isRevoked) {
			this.config.url = this.config.baseURL;
			this.config.method = "GetDelegationsList";
			var offset_ = 1;
			var current_ = 1;
			var perpage = ($("#gdvDelegations_pagesize").length > 0) ? $(
					"#gdvDelegations_pagesize :selected").text() : 10;

			var delegationBindObj = {
				delegatee : delegatee,
				createdFrom : createdFrom,
				createdTo : createdTo,
				delegatedAction : delegatedAction,
				isRevoked : isRevoked
			};

			this.config.data = {
				delegationBindObj : delegationBindObj,
				gpmsCommonObj : gpmsCommonObj()
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
					controlclass : 'attribHeaderChkbox',
					hide : true
				}, {
					display : 'Delegatee',
					name : 'delegatee',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Delegatee Email',
					name : 'delegatee_email',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					hide : true
				}, {
					display : 'Delegatee Position Title',
					name : 'delegatee_position_title',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					hide : true
				}, {
					display : 'Delegated Position Title',
					name : 'delegated_position_title',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					hide : true
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
					format : 'yyyy/MM/dd'
				}, {
					display : 'Delegated To',
					name : 'delegated_to',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					type : 'date',
					format : 'yyyy/MM/dd'
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
					arguments : '1, 3, 5, 6, 7, 8, 9, 13'
				}, {
					display : 'Revoke',
					name : 'revoke',
					enable : true,
					_event : 'click',
					trigger : '2',
					callMethod : 'delegation.RevokeDelegation',
					arguments : '13'
				}, {
					display : 'View Change Logs',
					name : 'changelog',
					enable : true,
					_event : 'click',
					trigger : '3',
					callMethod : 'delegation.ViewChangeLogs',
					arguments : '1, 11, 12, 13'
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
					14 : {
						sorter : false
					}
				}
			});
		},

		GetDelegableActions : function() {
			this.config.url = this.config.baseURL
					+ "GetDelegableActionsForAUser";
			this.config.data = JSON2.stringify({
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 5;
			this.ajaxCall(this.config);
			return false;
		},

		GetDelegableUsers : function(delegateAction) {
			var attributeArray = [];

			var currentPositionTitle = GPMS.utils.GetUserPositionTitle();
			var currentDepartment = GPMS.utils.GetUserDepartment();

			attributeArray.push({
				attributeType : "Subject",
				attributeName : "position.title",
				attributeValue : currentPositionTitle
			});

			attributeArray.push({
				attributeType : "Subject",
				attributeName : "department",
				attributeValue : currentDepartment
			});

			attributeArray.push({
				attributeType : "Action",
				attributeName : "proposal.action",
				attributeValue : delegateAction
			});

			this.config.url = this.config.baseURL
					+ "GetDelegableUsersForAction";
			this.config.data = JSON2.stringify({
				policyInfo : attributeArray,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 1;
			this.ajaxCall(this.config);
			return false;
		},

		EditDelegation : function(tblID, argus) {
			switch (tblID) {
			case "gdvDelegations":
				if (argus[8].toLowerCase() != "yes") {
					$('#lblFormHeading').html(
							'Edit Delegation Details for: ' + argus[1]);

					delegation.ClearForm();

					delegation.config.delegationId = argus[0];
					delegation.config.delegateePositionTitle = argus[2];

					$("#ddlDelegateAction").val(argus[3])
							.prop("disabled", true);

					$('#ddlDelegateTo').empty().append(
							new Option(argus[1], argus[1])).prop("disabled",
							true);

					$("#txtDelegationReason").val(argus[4]);
					$("#lblDelegationDateCreated").text(argus[5]);

					var regex = new RegExp('/', 'g');
					$("#txtDelegationFrom").val(argus[6].replace(regex, '\-'));
					$("#txtDelegationTo").val(argus[7].replace(regex, '\-'));

					$("#btnRevokeDelegation").show();
					$("#btnReset").hide();
					$("#trAddedOn").show();

					$('#divDelegationGrid').hide();
					$('#divDelegationForm').show();
					$('#divDelegationAuditGrid').hide();
				} else {
					csscody.alert('<h2>' + 'Information Alert' + '</h2><p>'
							+ 'Sorry! this delegation is already revoked.'
							+ '</p>');
				}
				break;
			default:
				break;
			}
		},

		SaveDelegation : function(config) {
			var delegationInfo = {
				DelegationId : config.delegationId,
				DelegationFrom : $("#txtDelegationFrom").val(),
				DelegationTo : $("#txtDelegationTo").val(),
				DelegationReason : $("#txtDelegationReason").val()
			};

			if (config.delegationId == "0") {
				delegationInfo.DelegatedAction = $("#ddlDelegateAction").val();
				delegationInfo.Delegatee = $("#ddlDelegateTo").val();
				delegationInfo.DelegateeId = config.delegateeId;
				// delegation.config.delegateeEmail
				delegationInfo.DelegateeCollege = config.delegateeCollege;
				delegationInfo.DelegateeDepartment = config.delegateeDepartment;
				delegationInfo.DelegateePositionType = config.delegateePositionType;
				delegationInfo.DelegateePositionTitle = config.delegateePositionTitle;
			}

			delegation.AddDelegationInfo(config, delegationInfo);
		},

		AddDelegationInfo : function(config, info) {
			this.config.url = this.config.baseURL + "SaveUpdateDelegation";
			this.config.data = JSON2.stringify({
				delegationInfo : info,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 2;
			this.ajaxCall(this.config);
			return false;
		},

		ExportToExcel : function(delegatee, createdFrom, createdTo,
				delegatedAction, isRevoked) {
			var delegationBindObj = {
				delegatee : delegatee,
				createdFrom : createdFrom,
				createdTo : createdTo,
				delegatedAction : delegatedAction,
				isRevoked : isRevoked
			};

			this.config.data = JSON2.stringify({
				delegationBindObj : delegationBindObj,
				gpmsCommonObj : gpmsCommonObj()
			});

			this.config.url = this.config.baseURL + "DelegationsExportToExcel";
			this.config.ajaxCallMode = 3;
			this.ajaxCall(this.config);
			return false;
		},

		RevokeDelegation : function(tblID, argus) {
			switch (tblID) {
			case "gdvDelegations":
				if (argus[1].toLowerCase() != "yes") {
					delegation.config.delegationId = argus[0];
					var properties = {
						onComplete : function(e) {
							if (e) {
								delegation
										.RevokeSingleDelegation(delegation.config);
							}
						}
					};
					csscody
							.confirm(
									"<h2>"
											+ 'Revoke Confirmation'
											+ "</h2><p>"
											+ 'Are you certain you want to revoke this delegation?'
											+ "</p>", properties);
					return false;
				} else {
					csscody.alert('<h2>' + 'Information Alert' + '</h2><p>'
							+ 'Sorry! this delegation is already revoked.'
							+ '</p>');
				}
				break;
			default:
				break;
			}
		},

		RevokeSingleDelegation : function(config) {
			this.config.url = this.config.baseURL
					+ "RevokeDelegationByDelegationID";
			this.config.data = JSON2.stringify({
				delegationId : config.delegationId,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 4;
			this.ajaxCall(this.config);
			return false;
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
					$('#divDelegationAuditGrid').show();
				}
				break;
			default:
				break;
			}
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

		BindDelegationAuditLogGrid : function(delegationId, action, auditedBy,
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
				delegationId : delegationId,
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

		LogsExportToExcel : function(delegationId, action, auditedBy,
				activityOnFrom, activityOnTo) {
			var auditLogBindObj = {
				Action : action,
				AuditedBy : auditedBy,
				ActivityOnFrom : activityOnFrom,
				ActivityOnTo : activityOnTo,
			};
			this.config.data = JSON2.stringify({
				delegationId : delegationId,
				auditLogBindObj : auditLogBindObj
			});

			this.config.url = this.config.baseURL
					+ "DelegationLogsExportToExcel";
			this.config.ajaxCallMode = 3;
			this.ajaxCall(this.config);
			return false;
		},

		ClearForm : function() {
			validator.resetForm();

			delegation.config.delegationId = '0';
			delegation.config.delegateeId = "";
			delegation.config.delegateeEmail = "";
			delegation.config.delegateeCollege = "";
			delegation.config.delegateeDepartment = "";
			delegation.config.delegateePositionType = "";
			delegation.config.delegateePositionTitle = "";

			$("#btnRevokeDelegation").hide();

			$("#ddlDelegateTo").empty()

			var container = $("#tblDeletationDetails");
			var inputs = container.find('INPUT, SELECT, TEXTAREA');
			$.each(inputs, function(i, item) {
				$(this).val('');
				$(this).val($(this).find('option').first().val());
			});

			return false;
		},

		ajaxSuccess : function(msg) {
			switch (delegation.config.ajaxCallMode) {
			case 0:
				break;

			case 1:
				// Get Delegable Users for a User with an Action
				$('#ddlDelegateTo option').remove();

				$.each(msg, function(index, item) {
					$('#ddlDelegateTo').append(
							$('<option></option>').val(item.fullName).html(
									item.fullName).data("delegateeId",
									item.userProfileId).data(
									"delegateeUserName", item.userName).data(
									"delegateeEmail", item.email).data(
									"delegateeCollege", item.college).data(
									"delegateeDepartment", item.department)
									.data("delegateePositionType",
											item.positionType).data(
											"delegateePositionTitle",
											item.positionTitle));
				});

				var element = $('#ddlDelegateTo').find('option:selected');
				delegation.config.delegateeId = element.data("delegateeId");
				delegation.config.delegateeEmail = element
						.data("delegateeEmail");
				delegation.config.delegateeCollege = element
						.data("delegateeCollege");
				delegation.config.delegateeDepartment = element
						.data("delegateeDepartment");
				delegation.config.delegateePositionType = element
						.data("delegateePositionType");
				delegation.config.delegateePositionTitle = element
						.data("delegateePositionTitle");
				break;

			case 2: // Save / Update Delegation
				delegation.BindDelegationGrid(null, null, null, null, null,
						null, null);
				$('#divDelegationGrid').show();

				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'Delegation has been Saved successfully.' + "</p>");

				$('#divDelegationForm').hide();
				$('#divDelegationAuditGrid').hide();

				delegation.config.delegationId = '0';
				delegation.config.delegateeId = "";
				delegation.config.delegateeEmail = "";
				delegation.config.delegateeCollege = "";
				delegation.config.delegateeDepartment = "";
				delegation.config.delegateePositionType = "";
				delegation.config.delegateePositionTitle = "";
				break;

			case 3: // Export to Excel Delgations
				if (msg != "No Record") {
					window.location.href = GPMS.utils.GetGPMSServicePath()
							+ 'files/download?fileName=' + msg;
				} else {
					csscody.alert("<h2>" + 'Information Message' + "</h2><p>"
							+ 'No Record found!' + "</p>");
				}
				break;

			case 4:
				// Single Delegation Revoke
				delegation.BindDelegationGrid(null, null, null, null, null,
						null, null);
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'Delegation has been revoked successfully.' + "</p>");

				$('#divDelegationForm').hide();
				$('#divDelegationGrid').show();
				$('#divDelegationAuditGrid').hide();

				delegation.config.delegationId = '0';
				delegation.config.delegateeId = "";
				delegation.config.delegateeEmail = "";
				delegation.config.delegateeCollege = "";
				delegation.config.delegateeDepartment = "";
				delegation.config.delegateePositionType = "";
				delegation.config.delegateePositionTitle = "";
				break;

			case 5: // Get all Delegable actions for a User
				// ddlSearchDelegatedAction

				// ddlDelegateAction

				console.log(msg);
				$('#ddlSearchDelegatedAction option').length = 0;
				$('#ddlDelegateAction').empty();

				$.each(msg, function(index, item) {
					$('#ddlSearchDelegatedAction').append(
							new Option(item, item));

					$('#ddlDelegateAction').append(new Option(item, item));
				});

				break;

			}
		},

		ajaxFailure : function(msg) {
			switch (delegation.config.ajaxCallMode) {
			case 0:
				break;
			case 1:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load Delegable Users for you. '
						+ msg.responseText + '</p>');
				break;
			case 2:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to DELEGATE!' + '</p>');
				break;
			case 3:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot create and download Excel report!' + "</p>");
				break;

			case 4:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to revoke this delegation! '
						+ msg.responseText + '</p>');
				break;

			case 5:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to delegate any actions yet! '
						+ msg.responseText + '</p>');

				break;

			}
		},

		init : function(config) {
			delegation.GetDelegableActions();

			$("#txtSearchCreatedFrom").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						onSelect : function(selectedDate) {
							$("#txtSearchCreatedTo").datepicker("option",
									"minDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});
			$("#txtSearchCreatedTo").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						onSelect : function(selectedDate) {
							$("#txtSearchCreatedFrom").datepicker("option",
									"maxDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});

			delegation.BindDelegationGrid(null, null, null, null, null, null,
					null);
			$('#divDelegationForm').hide();
			$('#divDelegationGrid').show();
			$('#divDelegationAuditGrid').hide();

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
								var delegatee = $.trim($("#txtSearchDelegatee")
										.val());
								var createdFrom = $.trim($(
										"#txtSearchCreatedFrom").val());
								var createdTo = $.trim($("#txtSearchCreatedTo")
										.val());

								var delegatedAction = $.trim($(
										'#ddlSearchDelegatedAction').val()) == "" ? null
										: $.trim($('#ddlSearchDelegatedAction')
												.val()) == "0" ? null
												: $
														.trim($(
																'#ddlSearchDelegatedAction')
																.val());

								var isRevoked = $.trim($("#ddlSearchIsRevoked")
										.val()) == "" ? null
										: $
												.trim($("#ddlSearchIsRevoked")
														.val()) == "True" ? true
												: false;

								if (delegatee.length < 1) {
									delegatee = null;
								}
								if (createdFrom.length < 1) {
									createdFrom = null;
								}
								if (createdTo.length < 1) {
									createdTo = null;
								}

								delegation.ExportToExcel(delegatee,
										createdFrom, createdTo,
										delegatedAction, isRevoked);
							});

			$('#btnBack').on("click", function() {
				$('#divDelegationGrid').show();
				$('#divDelegationForm').hide();
				delegation.config.delegationId = '0';
				delegation.config.delegateeId = "";
				delegation.config.delegateeEmail = "";
				delegation.config.delegateeCollege = "";
				delegation.config.delegateeDepartment = "";
				delegation.config.delegateePositionType = "";
				delegation.config.delegateePositionTitle = "";
			});

			$('#btnLogsBack').on("click", function() {
				$('#divDelegationGrid').show();
				$('#divDelegationForm').hide();
				$('#divDelegationAuditGrid').hide();
				delegation.config.delegationId = '0';
				delegation.config.delegateeId = "";
				delegation.config.delegateeEmail = "";
				delegation.config.delegateeCollege = "";
				delegation.config.delegateeDepartment = "";
				delegation.config.delegateePositionType = "";
				delegation.config.delegateePositionTitle = "";
			});

			$('#btnAddNew')
					.on(
							"click",
							function() {
								if (delegation.config.delegationId == '0') {
									$('#lblFormHeading').html(
											'New Delegation Details');

									$("#trAddedOn").hide();
									$("#btnReset").show();
									$("#btnSaveDelegation").show();
									$("#btnRevokeDelegation").hide();

									$("#ddlDelegateAction").prop("disabled",
											false);
									$("#ddlDelegateTo").prop("disabled", false);

									delegation.ClearForm();

									delegation.GetDelegableUsers($(
											"#ddlDelegateAction").val());

									$('#divDelegationGrid').hide();
									$('#divDelegationForm').show();
									$('#divDelegationAuditGrid').hide();
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
												delegation.GetDelegableUsers($(
														"#ddlDelegateAction")
														.val());
											}
										}
									}
								};
								csscody
										.confirm(
												"<h2>"
														+ 'Reset Confirmation'
														+ "</h2><p>"
														+ 'Are you certain you want to reset this delegation?'
														+ "</p>", properties);
							});

			// Revoke
			$('#btnRevokeDelegation')
					.click(
							function(event) {
								var properties = {
									onComplete : function(e) {
										if (e) {
											$('#btnRevokeDelegation')
													.disableWith('Revoking...');

											if (delegation.config.delegationId != "0") {
												delegation
														.RevokeSingleDelegation(delegation.config);
											}

											$('#btnRevokeDelegation')
													.enableAgain();
											event.preventDefault();
											return false;
										}
									}
								};
								csscody
										.confirm(
												"<h2>"
														+ 'Revoke Confirmation'
														+ "</h2><p>"
														+ 'Are you certain you want to revoke this delegation?'
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
												$('#btnSaveDelegation')
														.disableWith(
																'Saving...');

												delegation
														.SaveDelegation(delegation.config);

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
															+ 'Are you certain you want to save this delegation?'
															+ "</p>",
													properties);
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

			$("#ddlDelegateAction").on("change", function() {
				delegation.GetDelegableUsers($(this).val());
				return false;
			});

			$("#ddlDelegateTo").on(
					"change",
					function() {
						var element = $(this).find('option:selected');
						delegation.config.delegateeId = element
								.data("delegateeId");
						delegation.config.delegateeEmail = element
								.data("delegateeEmail");
						delegation.config.delegateeCollege = element
								.data("delegateeCollege");
						delegation.config.delegateeDepartment = element
								.data("delegateeDepartment");
						delegation.config.delegateePositionType = element
								.data("delegateePositionType");
						delegation.config.delegateePositionTitle = element
								.data("delegateePositionTitle");
						return false;
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
					'#txtSearchDelegatee, #txtSearchCreatedFrom, #txtSearchCreatedTo, #ddlSearchDelegatedAction, #ddlSearchIsRevoked')
					.keyup(function(event) {
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