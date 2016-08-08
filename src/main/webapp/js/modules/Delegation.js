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
			buttonType : "",
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
			var delegatedFrom = $.trim($("#txtSearchDelegatedFrom").val());
			var delegatedTo = $.trim($("#txtSearchDelegatedTo").val());

			var delegatedAction = $.trim($('#ddlSearchDelegatedAction').val()) == "" ? null
					: $.trim($('#ddlSearchDelegatedAction').val()) == "0" ? null
							: $.trim($('#ddlSearchDelegatedAction').val());

			var isRevoked = $.trim($("#ddlSearchIsRevoked").val()) == "" ? null
					: $.trim($("#ddlSearchIsRevoked").val()) == "True" ? true
							: false;

			if (delegatee.length < 1) {
				delegatee = null;
			}
			if (delegatedFrom.length < 1) {
				delegatedFrom = null;
			}
			if (delegatedTo.length < 1) {
				delegatedTo = null;
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
					display : 'Delegatee Department',
					name : 'delegatee_department',
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
					arguments : '1, 4, 5, 6, 7, 8, 12'
				}, {
					display : 'Revoke',
					name : 'revoke',
					enable : true,
					_event : 'click',
					trigger : '2',
					callMethod : 'delegation.RevokeDelegation',
					arguments : '12'
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
			this.config.ajaxCallMode = 4;
			this.ajaxCall(this.config);
			return false;
		},

		EditDelegation : function(tblID, argus) {
			switch (tblID) {
			case "gdvDelegations":
				$('#lblFormHeading').html(
						'Edit Delegation Details for: ' + argus[1]);

				delegation.ClearForm();

				delegation.config.delegationId = argus[0];

				$("#ddlDelegateAction").val(argus[2]).prop("disabled", true);

				$('#ddlDelegateTo').empty().append(
						new Option(argus[1], argus[1])).prop("disabled", true);

				$("#txtDelegationReason").val(argus[3]);
				$("#lblDelegationDateCreated").text(argus[4]);

				var regex = new RegExp('/', 'g');
				$("#txtDelegationFrom").val(argus[5].replace(regex, '\-'));
				$("#txtDelegationTo").val(argus[6].replace(regex, '\-'));

				if (argus[7].toLowerCase() != "yes") {
					$("#btnRevokeDelegation").show();
				}
				$("#btnReset").hide();
				$("#trAddedOn").show();

				$('#divDelegationGrid').hide();
				$('#divDelegationForm').show();
				$('#divDelegationAuditGrid').hide();
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

		ViewChangeLogs : function(tblID, argus) {
			switch (tblID) {
			case "gdvDelegations":
				alert(argus);

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

		RevokeDelegation : function(tblID, argus) {
			switch (tblID) {
			case "gdvDelegations":
				if (argus[1].toLowerCase() != "yes") {
					delegation.config.delegationId = argus[0];
					var properties = {
						onComplete : function(e) {
							if (e) {
								delegation.RevokeSingleDelegation("Revoke",
										delegation.config);
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
							+ 'Sorry! this delegation is already deleted.'
							+ '</p>');
				}
				break;
			default:
				break;
			}
		},

		RevokeSingleDelegation : function(buttonType, config) {
			this.config.url = this.config.baseURL
					+ "RevokeDelegationByDelegationID";
			this.config.data = JSON2.stringify({
				delegationId : config.delegationId,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 5;
			this.config.buttonType = buttonType;
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

		SaveDelegation : function(buttonType, config) {
			var delegationInfo = {
				DelegationFrom : $("#txtDelegationFrom").val(),
				DelegationTo : $("#txtDelegationTo").val(),
				DelegationReason : $("#txtDelegationReason").val()
			};

			if (config.delegationId == "0") {
				delegationInfo.DelegatedAction = $("#ddlDelegateAction").val();
				delegationInfo.Delegatee = $("#ddlDelegateTo").val();
				// DelegateeId
				// DelegateeCollege
				// DelegateeDepartment
				// DelegateePositionType
				// DelegateePositionTitle
				//
				// delegateeId : "",
				// delegateeCollege : "",
				// delegateeDepartment : "",
				// delegateePositionType : "",
				// delegateePositionTitle : ""
			} else {
				delegationInfo.DelegationId = config.delegationId;
			}

			delegation.AddDelegationInfo(buttonType, config, delegationInfo);
		},

		AddDelegationInfo : function(buttonType, config, info) {
			this.config.url = this.config.baseURL + "SaveUpdateDelegation";
			this.config.data = JSON2.stringify({
				buttonType : buttonType,
				delegationInfo : info,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 3;
			this.config.buttonType = buttonType;
			this.ajaxCall(this.config);
			return false;
		},

		ajaxSuccess : function(msg) {
			switch (delegation.config.ajaxCallMode) {
			case 0:
				break;

			case 1:// For Delegation Edit Action
				delegation.FillForm(msg);

				$('#divDelegationGrid').hide();
				$('#divDelegationForm').show();
				$('#divDelegationAuditGrid').hide();
				break;

			case 2: // Export to Excel Delgations
				if (msg != "No Record") {
					window.location.href = GPMS.utils.GetGPMSServicePath()
							+ 'files/download?fileName=' + msg;
				} else {
					csscody.alert("<h2>" + 'Information Message' + "</h2><p>"
							+ 'No Record found!' + "</p>");
				}
				break;

			case 3: // Save / Update Delegation
				delegation.BindDelegationGrid(null, null, null, null, null,
						null, null);
				$('#divDelegationGrid').show();

				var changeMade = "Saved";
				switch (delegation.config.buttonType) {
				case "Revoke":
					changeMade = "Revoked";
					break;
				default:
					break;
				}
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'Delegation has been ' + changeMade
						+ ' successfully.' + "</p>");

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

			case 4:
				// Delegation Users for a User with an Action
				$('#ddlDelegateTo').empty();

				$
						.each(
								msg,
								function(index, item) {
									$('#ddlDelegateTo').append(
											new Option(item.fullName,
													item.fullName));

									delegation.config.delegateeId = item.userProfileId;
									delegation.config.delegateeEmail = item.email;
									delegation.config.delegateeCollege = item.college;
									delegation.config.delegateeDepartment = item.department;
									delegation.config.delegateePositionType = item.positionType;
									delegation.config.delegateePositionTitle = item.positionTitle;

								});
				break;

			case 5:
				// Single Delegation Revoke
				delegation.BindDelegationGrid(null, null, null, null, null,
						null, null);
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'Delegation has been deleted successfully.' + "</p>");

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

			case 9:

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
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'You are not allowed to DEdelegations delegation! '
						+ msg.responseText + '</p>');
				break;
			case 3:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to revoke multiple delegations.' + '</p>');
				break;
			case 4:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load delegation details.' + '</p>');
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
						+ delegation.config.buttonType + ' this delegation! '
						+ msg.responseText + '</p>');
				break;

			case 10:
				// csscody.error('<h2>' + 'Error Message' + '</h2><p>'
				// + 'You are not allowed to DELETE this delegation! '
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

		ExportToExcel : function(delegatee, delegatedFrom, delegatedTo,
				delegatedAction, isRevoked) {
			var delegationBindObj = {
				delegatee : delegatee,
				delegatedFrom : delegatedFrom,
				delegatedTo : delegatedTo,
				delegatedAction : delegatedAction,
				isRevoked : isRevoked
			};

			this.config.data = JSON2.stringify({
				delegationBindObj : delegationBindObj,
				gpmsCommonObj : gpmsCommonObj()
			});

			this.config.url = this.config.baseURL + "DelegationsExportToExcel";
			this.config.ajaxCallMode = 2;
			this.ajaxCall(this.config);
			return false;
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
			this.config.ajaxCallMode = 17;
			this.ajaxCall(this.config);
			return false;
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
						GetDelegationAuditLogList
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
								var delegatedFrom = $.trim($(
										"#txtSearchDelegatedFrom").val());
								var delegatedTo = $.trim($(
										"#txtSearchDelegatedTo").val());

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
								if (delegatedFrom.length < 1) {
									delegatedFrom = null;
								}
								if (delegatedTo.length < 1) {
									delegatedTo = null;
								}

								delegation.ExportToExcel(delegatee,
										delegatedFrom, delegatedTo,
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
											var $buttonType = $.trim($(
													'#btnRevokeDelegation')
													.text());
											$('#btnRevokeDelegation')
													.disableWith('Revoking...');

											if (delegation.config.delegationId != "0") {
												delegation
														.RevokeSingleDelegation(
																$buttonType,
																delegation.config);
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
					'#txtSearchDelegatee, #txtSearchDelegatedFrom, #txtSearchDelegatedTo, #ddlSearchDelegatedAction, #ddlSearchIsRevoked')
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