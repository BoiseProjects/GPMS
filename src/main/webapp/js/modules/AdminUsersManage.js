var adminUsersManage = '';

$(function() {

	if (isAdmin == "false") {
		if (userProfileId == "null") {
			window.location = 'Login.jsp';
		} else {
			window.location = 'Home.jsp';
		}
	} else {
		if (userProfileId == "null") {
			window.location = 'Login.jsp';
		}
	}

	jQuery.fn.exists = function() {
		return this.length > 0;
	}

	// $.validator.unobtrusive.parse(#form1);
	$.validator.setDefaults({
		ignore : []
	});

	$.validator.addMethod('notequalto', function(value, element, param) {
		if (value != "" && $(param).val() != "") {
			return value != $(param).val();
		} else {
			return true;
		}
	}, 'Both emails looks same!');

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
							firstName : {
								required : true,
								maxlength : 40
							},
							lastName : {
								required : true,
								maxlength : 40
							},
							dob : {
								required : true,
								dpDate : true
							},
							gender : {
								required : true
							},
							street : {
								required : true,
								minlength : 3
							},
							city : {
								required : true
							},
							state : {
								required : true
							},
							zip : {
								required : true
							},
							country : {
								required : true
							},
							mobileNumber : {
								required : true
							},
							workEmail : {
								required : true,
								email : true,
								notequalto : '#txtPersonalEmail'
							},
							personalEmail : {
								email : true,
								notequalto : '#txtWorkEmail'
							},
							username : {
								required : true,
								minlength : 3
							},
							password : {
								required : true,
								minlength : 6,
								maxlength : 15
							},
							confirm_password : {
								required : true,
								minlength : 6,
								maxlength : 15,
								equalTo : "#txtPassword"
							}
						},
						errorElement : "span",
						errorClass : "warning",
						messages : {
							firstName : {
								required : "Please enter your firstname",
								maxlength : "Your firstname must be at most 40 characters long"
							},
							lastName : {
								required : "Please enter your lastname",
								maxlength : "Your lastname must be at most 40 characters long"
							},
							dob : {
								required : "Please enter your date of birth",
								dpDate : "Please enter valid date"
							},
							gender : {
								required : "Please select your gender"
							},
							street : {
								required : "Please enter your street address",
								minlength : "Please enter valid your street address"
							},
							city : {
								required : "Please enter your city"
							},
							state : {
								required : "Please select your city"
							},
							zip : {
								required : "Please enter your zip code"
							},
							country : {
								required : "Please select your country"
							},
							mobileNumber : {
								required : "Please enter your mobile phone number"
							},
							workEmail : {
								required : "Please enter your work email",
								email : "Please enter valid email id"
							},
							personalEmail : {
								email : "Please enter valid email id"
							},
							username : {
								required : "Please enter a username",
								minlength : "Your username must be at least 3 characters long"
							},
							password : {
								required : "Please provide a password",
								minlength : "Your password must be between 6 and 15 characters",
								maxlength : "Your password must be between 6 and 15 characters"
							},
							confirm_password : {
								required : "Please confirm your password",
								minlength : "Your password must be between 6 and 15 characters",
								maxlength : "Your password must be between 6 and 15 characters",
								equalTo : "Please enter the same password as above"
							}
						}
					});

	var rowIndex = 0;
	var editFlag = "0";
	var userNameIsUnique = false;
	var emailIsUnique = false;

	var positions = [];

	adminUsersManage = {
		config : {
			isPostBack : false,
			async : false,
			cache : false,
			type : 'POST',
			contentType : "application/json; charset=utf-8",
			data : '{}',
			dataType : 'json',
			baseURL : GPMS.utils.GetGPMSServicePath() + "users/",
			method : "",
			url : "",
			ajaxCallMode : 0
		},

		ajaxCall : function(config) {
			$
					.ajax({
						type : adminUsersManage.config.type,
						beforeSend : function(request) {
							request.setRequestHeader('GPMS-TOKEN', _aspx_token);
							request.setRequestHeader("UName", GPMS.utils
									.GetUserName());
							request.setRequestHeader("PID", GPMS.utils
									.GetUserProfileID());
							request.setRequestHeader("PType", "v");
							request.setRequestHeader('Escape', '0');
						},
						contentType : adminUsersManage.config.contentType,
						cache : adminUsersManage.config.cache,
						async : adminUsersManage.config.async,
						url : adminUsersManage.config.url,
						data : adminUsersManage.config.data,
						dataType : adminUsersManage.config.dataType,
						success : adminUsersManage.ajaxSuccess,
						error : adminUsersManage.ajaxFailure
					});
		},

		SearchUsers : function() {
			var userName = $.trim($("#txtSearchUserName").val());
			var positionTitle = $.trim($('#ddlSearchPositionTitle').val()) == "" ? null
					: $.trim($('#ddlSearchPositionTitle').val()) == "0" ? null
							: $.trim($('#ddlSearchPositionTitle').val());
			var isActive = $.trim($("#ddlSearchIsActive").val()) == "" ? null
					: $.trim($("#ddlSearchIsActive").val()) == "True" ? true
							: false;
			if (userName.length < 1) {
				userName = null;
			}
			adminUsersManage.BindAdminUserGrid(userName, positionTitle,
					isActive);
		},

		BindAdminUserGrid : function(userName, positionTitle, isActive) {
			this.config.url = this.config.baseURL;
			this.config.method = "GetAdminUsersList";
			var offset_ = 1;
			var current_ = 1;
			var perpage = ($("#gdvAdminUsers_pagesize").length > 0) ? $(
					"#gdvAdminUsers_pagesize :selected").text() : 10;

			var userBindObj = {
				UserName : userName,
				PositionTitle : positionTitle,
				IsActive : isActive
			};
			this.config.data = {
				userBindObj : userBindObj
			};
			var data = this.config.data;

			$("#gdvAdminUsers").sagegrid({
				url : this.config.url,
				functionMethod : this.config.method,
				colModel : [ {
					display : 'User Profile ID',
					name : 'userProfile_id',
					cssclass : 'cssClassHeadCheckBox',
					coltype : 'checkbox',
					align : 'center',
					checkFor : '10,12', // this is count from 0 column index
					elemClass : 'attrChkbox',
					elemDefault : false,
					controlclass : 'attribHeaderChkbox'
				}, {
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
					display : 'PI Count',
					name : 'PI_proposal_count',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					hide : true
				}, {
					display : 'Co-PI Count',
					name : 'CoPI_proposal_count',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					hide : true
				}, {
					display : 'Senior Count',
					name : 'senior_proposal_count',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					hide : true
				}, {
					display : 'Added On',
					name : 'added_on',
					cssclass : 'cssClassHeadDate',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					type : 'date',
					format : 'yyyy/MM/dd hh:mm:ss a'
				// Want more format then
				// :https://github.com/phstc/jquery-dateFormat/blob/master/test/expected_inputs_spec.js
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
					display : 'Is Deleted?',
					name : 'is_deleted',
					cssclass : 'cssClassHeadBoolean',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					// To override it we need
					type : 'boolean',
					format : 'Yes/No'
				// default format (No Need to specify) is True/False
				// you can define 'Yes/No'
				// hide : true
				}, {
					display : 'Is Active?',
					name : 'is_active',
					cssclass : 'cssClassHeadBoolean',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					type : 'boolean',
					format : 'Yes/No'
				}, {
					display : 'Is Admin?',
					name : 'is_admin',
					cssclass : 'cssClassHeadBoolean',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					type : 'boolean',
					format : 'Yes/No',
					hide : true
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
					callMethod : 'adminUsersManage.EditUser',
					arguments : '1,2,3,4,5,6,7,8,9,10,11,12'
				}, {
					display : 'Delete',
					name : 'delete',
					enable : true,
					_event : 'click',
					trigger : '2',
					callMethod : 'adminUsersManage.DeleteUser',
					arguments : '10,12'
				}, {
					display : 'Activate',
					name : 'activate',
					enable : true,
					_event : 'click',
					trigger : '3',
					callMethod : 'adminUsersManage.ActiveUser',
					arguments : '11,12'
				}, {
					display : 'Deactivate',
					name : 'deactivate',
					enable : true,
					_event : 'click',
					trigger : '4',
					callMethod : 'adminUsersManage.DeactiveUser',
					arguments : '11,12'
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

		EditUser : function(tblID, argus) {
			switch (tblID) {
			case "gdvAdminUsers":
				if (argus[12].toLowerCase() != "no") {
					csscody.alert('<h2>' + 'Information Alert' + '</h2><p>'
							+ 'Sorry! this user can not be edited.' + '</p>');

				} else {
					adminUsersManage.ClearForm();
					$('#txtPassword').rules("remove");
					$('#txtConfirmPassword').rules("remove");

					$('#lblFormHeading').html(
							'Edit User Details for: ' + argus[2]);

					if (argus[7] != null && argus[7] != "") {
						$('#tblLastAuditedInfo').show();
						$('#lblLastUpdatedOn').html(argus[7]);
						$('#lblLastUpdatedBy').html(argus[8]);
						$('#lblActivity').html(argus[9]);
					} else {
						$('#tblLastAuditedInfo').hide();
					}
					// $('#txtUserName').val(argus[1]);
					// $('#txtUserName').prop('disabled', 'disabled');
					if (argus[10].toLowerCase() != "yes") {
						$(".delbutton").data("id", argus[0]);
						$(".delbutton").show();
					} else {
						$(".delbutton").removeData("id");
						$(".delbutton").hide();
					}
					$("#btnSaveUser").data("name", argus[0]);

					$("#btnReset").hide();

					adminUsersManage.config.url = adminUsersManage.config.baseURL
							+ "GetUserDetailsByProfileId";
					adminUsersManage.config.data = JSON2.stringify({
						userId : argus[0]
					});
					adminUsersManage.config.ajaxCallMode = 2;
					adminUsersManage.ajaxCall(adminUsersManage.config);

					adminUsersManage.BindUserAuditLogGrid(argus[0], null, null,
							null, null);
					$('#auditLogTab').show();
				}
				break;
			default:
				break;
			}
		},

		FillForm : function(response) {
			// See this how we can get response object based on fields
			$('#txtFirstName').val(response['firstName']);
			$('#txtMiddleName').val(response['middleName']);
			$('#txtLastName').val(response['lastName']);
			$('#txtDOB').val(response['dateOfBirth']);

			// $('#ddlGender').val(response['gender']);

			$('#ddlGender option').map(function() {
				if ($(this).text() == response['gender'])
					return this;
			}).prop('selected', 'selected');

			adminUsersManage.BindUserPostionDetails(response['details']);

			$.each(response['officeNumbers'], function(index, value) {
				// alert(index + " :: " + value);
				$('#txtOfficeNumber').val(response['officeNumbers']).mask(
						"(999) 999-9999");
			});

			$.each(response['mobileNumbers'], function(index, value) {
				// alert(index + " :: " + value);
				$('#txtMobileNumber').val(response['mobileNumbers']).mask(
						"(999) 999-9999");
			});

			$.each(response['homeNumbers'], function(index, value) {
				// alert(index + " :: " + value);
				$('#txtHomeNumber').val(response['homeNumbers']).mask(
						"(999) 999-9999");
			});

			$.each(response['otherNumbers'], function(index, value) {
				// alert(index + " :: " + value);
				$('#txtOtherNumber').val(response['otherNumbers']).mask(
						"(999) 999-9999");
			});

			$.each(response['addresses'], function(index, value) {
				$('#txtStreet').val(value['street']);
				$('#txtApt').val(value['apt']);
				$('#txtCity').val(value['city']);

				$('#ddlState option').map(function() {
					if ($(this).text() == value['state'])
						return this;
				}).prop('selected', 'selected');

				$('#txtZip').val(value['zipcode']);

				$('#ddlCountry option').map(function() {
					if ($(this).text() == value['country'])
						return this;
				}).prop('selected', 'selected');
			});

			$.each(response['workEmails'], function(index, value) {
				// alert(index + " :: " + value);
				$('#txtWorkEmail').val(response['workEmails']);
			});

			$.each(response['personalEmails'], function(index, value) {
				// alert(index + " :: " + value);
				$('#txtPersonalEmail').val(response['personalEmails']);
			});

			$('input[name=chkActive]').prop('checked',
					response['userAccount']['active']);

			$.each(response['userAccount'], function(index, value) {
				$('#txtUserName').val(response['userAccount']['userName']);
				$('#txtUserName').prop('disabled', 'disabled');

				$('#txtPassword').val(response['userAccount']['password']);
			});
		},

		BindUserPostionDetails : function(postitionDetails) {
			$.each(postitionDetails, function(i, value) {
				// alert(i + " :: " + value['positionTitle']);

				$('select[name="ddlPositionTitle"]').find('option').each(
						function() {
							var $this = $(this);
							if ($this.text() == value['positionTitle']) {
								$this.prop('selected', 'selected');
								return false;
							}
						});

			});
		},

		SearchUserAuditLogs : function() {
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

			var userId = $('#btnSaveUser').data("name");
			if (userId == '') {
				userId = "0";
			}

			adminUsersManage.BindUserAuditLogGrid(userId, action, auditedBy,
					activityOnFrom, activityOnTo);
		},

		BindUserAuditLogGrid : function(userId, action, auditedBy,
				activityOnFrom, activityOnTo) {
			this.config.url = this.config.baseURL;
			this.config.method = "GetUserAuditLogList";
			var offset_ = 1;
			var current_ = 1;
			var perpage = ($("#gdvAdminUsersAuditLog_pagesize").length > 0) ? $(
					"#gdvAdminUsersAuditLog_pagesize :selected").text()
					: 10;

			var auditLogBindObj = {
				Action : action,
				AuditedBy : auditedBy,
				ActivityOnFrom : activityOnFrom,
				ActivityOnTo : activityOnTo,
			};
			this.config.data = {
				userId : userId,
				auditLogBindObj : auditLogBindObj
			};
			var data = this.config.data;

			$("#gdvAdminUsersAuditLog").sagegrid({
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

		DeleteUser : function(tblID, argus) {
			switch (tblID) {
			case "gdvAdminUsers":
				if (argus[2].toLowerCase() != "yes") {
					if (argus[1].toLowerCase() != "yes") {
						adminUsersManage.DeleteUserById(argus[0]);
					} else {
						csscody.alert('<h2>' + 'Information Alert' + '</h2><p>'
								+ 'Sorry! this user is already deleted.'
								+ '</p>');
					}
				} else {
					csscody.alert('<h2>' + 'Information Alert' + '</h2><p>'
							+ 'Sorry! this user can not be deleted.' + '</p>');
				}
				break;
			default:
				break;
			}
		},

		DeleteUserById : function(_userId) {
			var properties = {
				onComplete : function(e) {
					adminUsersManage.ConfirmSingleDelete(_userId, e);
				}
			};
			csscody.confirm("<h2>" + 'Delete Confirmation' + "</h2><p>"
					+ 'Are you certain you want to delete this user?' + "</p>",
					properties);
		},

		ConfirmSingleDelete : function(user_id, event) {
			if (event) {
				adminUsersManage.DeleteSingleUser(user_id);
			}
		},

		DeleteSingleUser : function(_userId) {
			this.config.url = this.config.baseURL + "DeleteUserByUserID";
			this.config.data = JSON2.stringify({
				userId : _userId,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 3;
			this.ajaxCall(this.config);
			return false;
		},

		ConfirmDeleteMultiple : function(user_ids, event) {
			if (event) {
				adminUsersManage.DeleteMultipleUsers(user_ids);
			}
		},

		DeleteMultipleUsers : function(_userIds) {
			// this.config.dataType = "html";
			this.config.url = this.config.baseURL
					+ "DeleteMultipleUsersByUserID";
			this.config.data = JSON2.stringify({
				userIds : _userIds,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 4;
			this.ajaxCall(this.config);
			return false;
		},

		ActivateUser : function(_userId, _isActive) {
			this.config.url = this.config.baseURL
					+ "UpdateUserIsActiveByUserID";
			this.config.data = JSON2.stringify({
				userId : _userId,
				gpmsCommonObj : gpmsCommonObj(),
				isActive : _isActive
			});
			if (_isActive) {
				this.config.ajaxCallMode = 5;
			} else {
				this.config.ajaxCallMode = 6;
			}
			this.ajaxCall(this.config);
			return false;
		},

		ActiveUser : function(tblID, argus) {
			switch (tblID) {
			case "gdvAdminUsers":
				if (argus[2].toLowerCase() != "yes") {
					if (argus[1].toLowerCase() != "yes") {
						adminUsersManage.ActivateUser(argus[0], true);
					} else {
						csscody.alert('<h2>' + 'Information Alert' + '</h2><p>'
								+ 'Sorry! this user is already actived.'
								+ '</p>');
					}
				} else {
					csscody
							.alert('<h2>' + 'Information Alert' + '</h2><p>'
									+ 'Sorry! this user can not be activated.'
									+ '</p>');
				}
				break;
			default:
				break;
			}
		},

		DeactiveUser : function(tblID, argus) {
			switch (tblID) {
			case "gdvAdminUsers":
				if (argus[2].toLowerCase() != "yes") {
					if (argus[1].toLowerCase() != "no") {
						adminUsersManage.ActivateUser(argus[0], false);
					} else {
						csscody.alert('<h2>' + 'Information Alert' + '</h2><p>'
								+ 'Sorry! this user is already deactived.'
								+ '</p>');
					}
				} else {
					csscody.alert('<h2>' + 'Information Alert' + '</h2><p>'
							+ 'Sorry! this user can not be deactivated.'
							+ '</p>');
				}
				break;
			default:
				break;
			}
		},

		ClearForm : function() {
			validator.resetForm();
			// $('#form1').removeData('validator');
			// $('.class-text').removeClass('error').next('span').removeClass(
			// 'error');
			var inputs = $("#container-7").find(
					'INPUT:not(".class-isdefault"), SELECT, TEXTAREA');
			$.each(inputs, function(i, item) {
				// rmErrorClass(item);
				$(this).prop('checked', false);
				$(this).val($(this).find('option').first().val());
			});

			adminUsersManage.onInit();
			$('#lblFormHeading').html('New User Details');
			$(".delbutton").removeData("id");
			$("#btnSaveUser").removeData("name");
			$(".delbutton").hide();
			$("#btnReset").show();

			$('#txtUserName').removeAttr('disabled');

			$('select[name="ddlPositionTitle"]').val("");

			if (!$('input[name=chkActive]').is(":checked")) {
				$('input[name=chkActive]').prop('checked', 'checked');
			}

			return false;
		},

		ExportToExcel : function(userName, positionTitle, isActive) {
			var userBindObj = {
				UserName : userName,
				PositionTitle : positionTitle,
				IsActive : isActive
			};

			this.config.data = JSON2.stringify({
				userBindObj : userBindObj
			});

			this.config.url = this.config.baseURL + "AdminUsersExportToExcel";
			this.config.ajaxCallMode = 10;
			this.ajaxCall(this.config);
			return false;
		},

		onInit : function() {
			adminUsersManage.SetFirstTabActive();
			$('#btnReset').hide();
			$('.cssClassRight').hide();
			$('.warning').hide();

			$("#gdvAdminUsersAuditLog").empty();
			$("#gdvAdminUsersAuditLog_Pagination").remove();
		},

		SetFirstTabActive : function() {
			var $tabs = $('#container-7').tabs({
				fx : [ null, {
					height : 'show',
					opacity : 'show'
				} ]
			});
			$tabs.tabs('option', 'active', 0);
		},

		saveUser : function(_userId) {
			// $('#iferror').hide();
			// var $form = $("#form1");
			// $form.valid();
			if (validator.form()) {
				var $username = $('#txtUserName');
				var userName = $.trim($username.val());
				var validateErrorMessage = adminUsersManage
						.checkUniqueUserName(_userId, userName, $username);
				if (validateErrorMessage == "") {
					var $workEmail = $("#txtWorkEmail");
					var workEmail = $.trim($workEmail.val());
					validateErrorMessage += adminUsersManage
							.checkUniqueEmailAddress(_userId, workEmail,
									"txtWorkEmail");
				}

				if (validateErrorMessage == "") {
					var $personalEmail = $("#txtPersonalEmail");
					var personalEmail = $.trim($personalEmail.val());
					validateErrorMessage += adminUsersManage
							.checkUniqueEmailAddress(_userId, personalEmail,
									"txtPersonalEmail");
				}

				if (validateErrorMessage == "") {
					var optionsText = $('select[name="ddlPositionTitle"]')
							.val();
					if (optionsText != "") {
						var userInfo = {
							UserID : _userId,
							FirstName : $.trim($('#txtFirstName').val()),
							MiddleName : $.trim($('#txtMiddleName').val()),
							LastName : $.trim($('#txtLastName').val()),
							DOB : $('#txtDOB').val(),
							Gender : $('#ddlGender :selected').val(),
							Street : $.trim($('#txtStreet').val()),
							Apt : $.trim($('#txtApt').val()),
							City : $.trim($('#txtCity').val()),
							State : $('#ddlState :selected').text(),
							Zip : $.trim($('#txtZip').val()),
							Country : $('#ddlCountry :selected').text(),
							OfficeNumber : $('#txtOfficeNumber').mask(),
							MobileNumber : $('#txtMobileNumber').mask(),
							HomeNumber : $('#txtHomeNumber').mask(),
							OtherNumber : $('#txtOtherNumber').mask(),
							WorkEmail : $('#txtWorkEmail').val(),
							PersonalEmail : $('#txtPersonalEmail').val(),
							IsActive : $('input[name=chkActive]').prop(
									'checked'),
							UserName : $.trim($('#txtUserName').val()),
							positionTitle : optionsText
						};

						var password = $.trim($('#txtPassword').val());
						if (password != "") {
							userInfo.Password = password;
						}
						adminUsersManage.AddUserInfo(userInfo);
					}
				}
			} else {
				adminUsersManage.focusTabWithErrors("#container-7");
			}
		},

		checkUniqueUserName : function(user_id, userName, textBoxUserName) {
			var errors = '';
			if (!textBoxUserName.hasClass('warning') && userName.length > 0) {
				if (!adminUsersManage.isUniqueUserName(user_id, userName)) {
					errors += 'Please enter unique username.' + " '"
							+ userName.trim() + "' "
							+ 'has already been taken.';
					textBoxUserName.addClass("error");
					textBoxUserName.siblings('.cssClassRight').hide();
					if (textBoxUserName.siblings('span.warning').exists()) {
						textBoxUserName.siblings('span.warning').html(errors);
					} else {
						$(
								'<span id="txtUserName-error" class="warning" for="txtUserName">'
										+ errors + '</span>').insertAfter(
								textBoxUserName);
					}

					textBoxUserName.siblings('.warning').show();
					textBoxUserName.focus();
				} else {
					textBoxUserName.removeClass("error");
					textBoxUserName.siblings('.cssClassRight').show();
					textBoxUserName.siblings('.warning').hide();
					textBoxUserName.siblings('.warning').html('');
				}
			}
			return errors;
		},

		isUniqueUserName : function(userId, newUserName) {
			var userUniqueObj = {
				UserID : userId,
				NewUserName : newUserName
			};

			this.config.url = this.config.baseURL + "CheckUniqueUserName";
			this.config.data = JSON2.stringify({
				userUniqueObj : userUniqueObj,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 7;
			this.ajaxCall(this.config);
			return userNameIsUnique;
		},

		checkUniqueEmailAddress : function(user_id, email, textBoxEmail) {
			var errors = '';
			var txtEmail = $("#" + textBoxEmail);
			if (!txtEmail.hasClass('warning') && email.length > 0) {
				if (!adminUsersManage.isUniqueEmail(user_id, email)) {
					errors += 'Please enter unique email id.' + " '"
							+ email.trim() + "' " + 'has already been taken.';
					txtEmail.addClass("error");
					txtEmail.siblings('.cssClassRight').hide();
					if (txtEmail.siblings('span.warning').exists()) {
						txtEmail.siblings('span.warning').html(errors);
					} else {
						$(
								'<span id="' + textBoxEmail
										+ '-error" class="warning" for="'
										+ textBoxEmail + '">' + errors
										+ '</span>').insertAfter(txtEmail);
					}

					txtEmail.siblings('.warning').show();
					txtEmail.focus();
				} else {
					txtEmail.removeClass("error");
					txtEmail.siblings('.cssClassRight').show();
					txtEmail.siblings('.warning').hide();
					txtEmail.siblings('.warning').html('');
				}
			}
			return errors;
		},

		isUniqueEmail : function(userId, newEmail) {
			var userUniqueObj = {
				UserID : userId,
				NewEmail : newEmail
			};

			this.config.url = this.config.baseURL + "CheckUniqueEmail";
			this.config.data = JSON2.stringify({
				userUniqueObj : userUniqueObj,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 8;
			this.ajaxCall(this.config);
			return emailIsUnique;
		},

		addPwdValidateRules : function() {
			$("#txtPassword")
					.rules(
							"add",
							{
								required : true,
								minlength : 6,
								maxlength : 15,
								messages : {
									required : "Please provide a password",
									minlength : "Your password must be between 6 and 15 characters",
									maxlength : "Your password must be between 6 and 15 characters"
								}
							});
			$("#txtConfirmPassword")
					.rules(
							"add",
							{
								required : true,
								minlength : 6,
								maxlength : 15,
								equalTo : "#txtPassword",
								messages : {
									required : "Please confirm your password",
									minlength : "Your password must be between 6 and 15 characters",
									maxlength : "Your password must be between 6 and 15 characters",
									equalTo : "Please enter the same password as above"
								}
							});
		},

		focusTabWithErrors : function(tabPanelName) {
			$(tabPanelName).children('div.ui-tabs-panel:not("#fragment-4")')
					.each(function(index) {
						if ($(this).find("span.warning").text() != "") {
							$(tabPanelName).tabs("option", "active", index);
							return false;
						}
					});
		},

		AddUserInfo : function(info) {
			this.config.url = this.config.baseURL + "SaveUpdateUser";
			this.config.data = JSON2.stringify({
				userInfo : info,
				gpmsCommonObj : gpmsCommonObj()
			});
			this.config.ajaxCallMode = 9;
			this.ajaxCall(this.config);
			return false;
		},

		ajaxSuccess : function(msg) {
			switch (adminUsersManage.config.ajaxCallMode) {
			case 0:
				break;

			case 2:// For User Edit Action
				adminUsersManage.FillForm(msg);
				$('#divUserGrid').hide();
				$('#divUserForm').show();
				break;

			case 3:// For User Delete
				adminUsersManage.BindAdminUserGrid(null, null, null);
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'User has been deleted successfully.' + "</p>");

				$('#divUserForm').hide();
				$('#divUserGrid').show();
				break;

			case 4:
				SageData.Get("gdvAdminUsers").Arr.length = 0;
				adminUsersManage.BindAdminUserGrid(null, null, null);
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'Selected user(s) has been deleted successfully.'
						+ "</p>");
				break;

			case 5:
				adminUsersManage.BindAdminUserGrid(null, null, null);
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'User has been activated successfully.' + "</p>");
				break;

			case 6:
				adminUsersManage.BindAdminUserGrid(null, null, null);
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'User has been deactivated successfully.' + "</p>");
				break;

			case 7:
				userNameIsUnique = stringToBoolean(msg);
				break;

			case 8:
				emailIsUnique = stringToBoolean(msg);
				break;

			case 9:
				adminUsersManage.BindAdminUserGrid(null, null, null);
				$('#divUserGrid').show();
				if (editFlag != "0") {
					csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
							+ 'User has been updated successfully.' + "</p>");
				} else {
					csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
							+ 'User has been saved successfully.' + "</p>");
				}
				adminUsersManage.ClearForm();
				$('#divUserForm').hide();
				break;

			case 10:
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
			switch (adminUsersManage.config.ajaxCallMode) {
			case 0:
				break;
			case 2:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load user details.' + '</p>');
				break;
			case 3:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'User cannot be deleted.' + "</p>");
				break;
			case 4:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Selected user(s) cannot be deleted.' + "</p>");
				break;
			case 5:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'User cannot be activated.' + "</p>");
				break;
			case 6:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'User cannot be deactivated.' + "</p>");
				break;
			case 7:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot check for unique Username' + "</p>");
				break;

			case 8:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot check for unique Email' + "</p>");
				break;

			case 9:
				if (editFlag != "0") {
					csscody.error("<h2>" + 'Error Message' + "</h2><p>"
							+ 'Failed to update user!' + "</p>");
				} else {
					csscody.error("<h2>" + 'Error Message' + "</h2><p>"
							+ 'Failed to save user!' + "</p>");
				}
				break;

			case 10:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot create and download Excel report!' + "</p>");
				break;
			}
		},

		init : function() {
			adminUsersManage.BindAdminUserGrid(null, null, null);
			$('#divUserForm').hide();
			$('#divUserGrid').show();

			$('#btnDeleteSelected')
					.click(
							function() {
								var user_ids = '';
								user_ids = SageData.Get("gdvAdminUsers").Arr
										.join(',');

								if (user_ids.length > 0) {
									var properties = {
										onComplete : function(e) {
											adminUsersManage
													.ConfirmDeleteMultiple(
															user_ids, e);
										}
									};
									csscody
											.confirm(
													"<h2>"
															+ 'Delete Confirmation'
															+ "</h2><p>"
															+ 'Are you certain you want to delete selected user(s)?'
															+ "</p>",
													properties);
								} else {
									csscody
											.alert('<h2>'
													+ 'Information Alert'
													+ '</h2><p>'
													+ 'Please select at least one user before deleting.'
													+ '</p>');
								}
							});

			$('#btnAddNew').bind("click", function() {
				adminUsersManage.ClearForm();
				adminUsersManage.addPwdValidateRules();

				$('#auditLogTab').hide();
				$('#divUserGrid').hide();
				$('#divUserForm').show();
			});

			$("#btnExportToExcel")
					.on(
							"click",
							function() {
								var userName = $.trim($("#txtSearchUserName")
										.val());
								var positionTitle = $.trim($(
										'#ddlSearchPositionTitle').val()) == "" ? null
										: $.trim($('#ddlSearchPositionTitle')
												.val()) == "0" ? null
												: $
														.trim($(
																'#ddlSearchPositionTitle')
																.val());
								var isActive = $.trim($("#ddlSearchIsActive")
										.val()) == "" ? null
										: $.trim($("#ddlSearchIsActive").val()) == "True" ? true
												: false;
								if (userName.length < 1) {
									userName = null;
								}

								adminUsersManage.ExportToExcel(userName,
										positionTitle, isActive);
							});

			$('#btnBack').bind("click", function() {
				$('#divUserForm').hide();
				$('#divUserGrid').show();
				adminUsersManage.ClearForm();
			});

			$('#btnReset').bind("click", function() {
				adminUsersManage.ClearForm();
			});

			$('#btnSaveUser').click(function(e) {
				$(this).disableWith('Saving...');
				var user_id = $(this).data("name");
				if (user_id != undefined) {
					editFlag = user_id;
					adminUsersManage.saveUser(user_id);
				} else {
					editFlag = "0";
					adminUsersManage.saveUser("0");
				}
				$(this).enableAgain();
				e.preventDefault();
				return false;
			});

			$('#txtPassword').dblclick(function() {
				$(this).val('');
				adminUsersManage.addPwdValidateRules();
			});

			$('#txtUserName').on("focus", function() {
				$(this).siblings('.cssClassRight').hide();
			}), $('#txtUserName').on(
					"blur",
					function() {
						var userName = $.trim($(this).val());
						var user_id = $('#btnSaveUser').data("name");
						if (user_id == undefined) {
							user_id = "0";
						}
						adminUsersManage.checkUniqueUserName(user_id, userName,
								$(this));
						return false;
					});

			$('#txtWorkEmail, #txtPersonalEmail').on("focus", function() {
				$(this).siblings('.cssClassRight').hide();
			}), $('#txtWorkEmail, #txtPersonalEmail').on(
					"blur",
					function() {
						var email = $.trim($(this).val());
						var user_id = $('#btnSaveUser').data("name");
						if (user_id == undefined) {
							user_id = "0";
						}
						adminUsersManage.checkUniqueEmailAddress(user_id,
								email, this.id);
						return false;
					});

			$(".delbutton").click(function() {
				var user_id = $(this).data("id");
				adminUsersManage.DeleteUserById(user_id);
			});

			$("#btnSearchUser").bind("click", function() {
				adminUsersManage.SearchUsers();
				return false;
			});

			$("#btnSearchUserAuditLog").bind("click", function() {
				adminUsersManage.SearchUserAuditLogs();
				return false;
			});

			// propose username by combining first- and lastname
			$("#txtUserName").focus(function() {
				var firstname = $("#txtFirstName").val();
				var lastname = $("#txtLastName").val();
				if (firstname && lastname && !this.value) {
					this.value = firstname + "." + lastname;
				}
			});

			$("#txtOfficeNumber").mask("(999) 999-9999");
			$("#txtMobileNumber").mask("(999) 999-9999");
			$("#txtHomeNumber").mask("(999) 999-9999");
			$("#txtOtherNumber").mask("(999) 999-9999");
			$("#txtZip").mask("99999");

			$("#txtDOB").datepicker({
				dateFormat : 'yy-mm-dd',
				changeMonth : true,
				changeYear : true,
				yearRange : "-100:+0",
				maxDate : 0
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
			$("#txtSearchActivityOnTo").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						changeMonth : true,
						changeYear : true,
						maxDate : 0,
						onSelect : function(selectedDate) {
							$("#txtSearchActivityOnFrom").datepicker("option",
									"maxDate", selectedDate);
						}
					}).mask("9999-99-99", {
				placeholder : "yyyy-mm-dd"
			});

			$('#txtSearchUserName, #ddlSearchPositionTitle, #ddlSearchIsActive')
					.keyup(function(event) {
						if (event.keyCode == 13) {
							$("#btnSearchUser").click();
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
	adminUsersManage.init();
});