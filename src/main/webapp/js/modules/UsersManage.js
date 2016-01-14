var usersManage = '';

$(function() {
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
			UserName : GPMS.utils.GetUserName(),
			UserProfileID : GPMS.utils.GetUserProfileID(),
			CultureName : GPMS.utils.GetCultureName()
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
	var editFlag = 0;
	var userNameIsUnique = false;
	var emailIsUnique = false;

	var positions = [];

	usersManage = {
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
						type : usersManage.config.type,
						beforeSend : function(request) {
							request.setRequestHeader('GPMS-TOKEN', _aspx_token);
							request.setRequestHeader("UName", GPMS.utils
									.GetUserName());
							request.setRequestHeader("PID", GPMS.utils
									.GetUserProfileID());
							request.setRequestHeader("PType", "v");
							request.setRequestHeader('Escape', '0');
						},
						contentType : usersManage.config.contentType,
						cache : usersManage.config.cache,
						async : usersManage.config.async,
						url : usersManage.config.url,
						data : usersManage.config.data,
						dataType : usersManage.config.dataType,
						success : usersManage.ajaxSuccess,
						error : usersManage.ajaxFailure
					});
		},

		LoadStaticImage : function() {
			$('.cssClassSuccessImg').prop('src',
					'' + GPMS.utils.GetGPMSRootPath() + 'images/right.jpg');
		},

		SearchUsers : function() {
			var userName = $.trim($("#txtSearchUserName").val());
			var college = $.trim($('#ddlSearchCollege').val()) == "" ? null : $
					.trim($('#ddlSearchCollege').val()) == "0" ? null : $
					.trim($('#ddlSearchCollege').val());
			var department = $.trim($('#ddlSearchDepartment').val()) == "" ? null
					: $.trim($('#ddlSearchDepartment').val()) == "0" ? null : $
							.trim($('#ddlSearchDepartment').val());
			var positionType = $.trim($('#ddlSearchPositionType').val()) == "" ? null
					: $.trim($('#ddlSearchPositionType').val()) == "0" ? null
							: $.trim($('#ddlSearchPositionType').val());
			var positionTitle = $.trim($('#ddlSearchPositionTitle').val()) == "" ? null
					: $.trim($('#ddlSearchPositionTitle').val()) == "0" ? null
							: $.trim($('#ddlSearchPositionTitle').val());
			var isActive = $.trim($("#ddlSearchIsActive").val()) == "" ? null
					: $.trim($("#ddlSearchIsActive").val()) == "True" ? true
							: false;
			if (userName.length < 1) {
				userName = null;
			}
			usersManage.BindUserGrid(userName, college, department,
					positionType, positionTitle, isActive);
		},

		BindUserGrid : function(userName, college, department, positionType,
				positionTitle, isActive) {
			this.config.url = this.config.baseURL;
			this.config.method = "GetUsersList";
			var offset_ = 1;
			var current_ = 1;
			var perpage = ($("#gdvUsers_pagesize").length > 0) ? $(
					"#gdvUsers_pagesize :selected").text() : 10;

			var userBindObj = {
				UserName : userName,
				College : college,
				Department : department,
				PositionType : positionType,
				PositionTitle : positionTitle,
				IsActive : isActive
			};
			this.config.data = {
				userBindObj : userBindObj
			};
			var data = this.config.data;

			$("#gdvUsers").sagegrid({
				url : this.config.url,
				functionMethod : this.config.method,
				colModel : [ {
					display : 'User Profile ID',
					name : 'userProfile_id',
					cssclass : 'cssClassHeadCheckBox',
					coltype : 'checkbox',
					align : 'center',
					checkFor : '11,12', // this is count from 0 column index
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
					align : 'left'
				}, {
					display : 'Co-PI Count',
					name : 'CoPI_proposal_count',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : 'Senior Count',
					name : 'senior_proposal_count',
					cssclass : '',
					controlclass : '',
					coltype : 'label',
					align : 'left'
				}, {
					display : getLocale(gpmsUsersManagement, 'Added On'),
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
					display : getLocale(gpmsUsersManagement, 'Last Audited'),
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
					display : getLocale(gpmsUsersManagement, 'Is Active?'),
					name : 'is_active',
					cssclass : 'cssClassHeadBoolean',
					controlclass : '',
					coltype : 'label',
					align : 'left',
					type : 'boolean',
					format : 'Yes/No'
				}, {
					display : getLocale(gpmsUsersManagement, 'Is Deleted?'),
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
					display : getLocale(gpmsUsersManagement, 'Actions'),
					name : 'action',
					cssclass : 'cssClassAction',
					coltype : 'label',
					align : 'center'
				} ],

				buttons : [ {
					display : getLocale(gpmsUsersManagement, "Edit"),
					name : 'edit',
					enable : true,
					_event : 'click',
					trigger : '1',
					callMethod : 'usersManage.EditUser',
					arguments : '1,2,3,4,5,6,7,8,9,10,11,12'
				}, {
					display : getLocale(gpmsUsersManagement, "Delete"),
					name : 'delete',
					enable : true,
					_event : 'click',
					trigger : '2',
					callMethod : 'usersManage.DeleteUser',
					arguments : '11,12'
				}, {
					display : getLocale(gpmsUsersManagement, "Activate"),
					name : 'activate',
					enable : true,
					_event : 'click',
					trigger : '3',
					callMethod : 'usersManage.ActiveUser',
					arguments : '10,12'
				}, {
					display : getLocale(gpmsUsersManagement, "Deactivate"),
					name : 'deactivate',
					enable : true,
					_event : 'click',
					trigger : '4',
					callMethod : 'usersManage.DeactiveUser',
					arguments : '10,12'
				} ],
				rp : perpage,
				nomsg : getLocale(gpmsUsersManagement, 'No Records Found!'),
				param : data,
				current : current_,
				pnew : offset_,
				sortcol : {
					0 : {
						sorter : false
					},
					12 : {
						sorter : false
					}
				}
			});
		},

		EditUser : function(tblID, argus) {
			switch (tblID) {
			case "gdvUsers":				
				if(argus[12].toLowerCase() != "no"){
					csscody.alert('<h2>'
							+ getLocale(gpmsUsersManagement,
									"Information Alert")
							+ '</h2><p>'
							+ getLocale(gpmsUsersManagement,
									"Sorry! this user can not be edited.")
							+ '</p>');
					
				} else {
					usersManage.ClearForm();
					$('#txtPassword').rules("remove");
					$('#txtConfirmPassword').rules("remove");
	
					$('#lblFormHeading').html(
							getLocale(gpmsUsersManagement,
									'Edit User Details for: ')
									+ argus[2]);
	
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
					if (argus[11].toLowerCase() != "yes") {
						$(".delbutton").prop("id", argus[0]);
						$(".delbutton").show();
					} else {
						$(".delbutton").removeAttr("id");
						$(".delbutton").hide();
					}
					$("input[name=AddMore]").removeAttr('disabled');
					$("input[name=DeleteOption]").removeAttr('disabled');
					$("#btnSaveUser").prop("name", argus[0]);
	
					$("#btnReset").hide();
	
					usersManage.config.url = usersManage.config.baseURL
							+ "GetUserDetailsByProfileId";
					usersManage.config.data = JSON2.stringify({
						userId : argus[0]
					});
					usersManage.config.ajaxCallMode = 2;
					usersManage.ajaxCall(usersManage.config);
	
					usersManage.BindUserAuditLogGrid(argus[0], null, null, null,
							null);
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

			usersManage.BindUserPostionDetails(response['details']);

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
					response['userAccount']['isActive']);

			$.each(response['userAccount'], function(index, value) {
				$('#txtUserName').val(response['userAccount']['userName']);
				$('#txtUserName').prop('disabled', 'disabled');

				$('#txtPassword').val(response['userAccount']['password']);
			});

			// if (item.ItemTypes.length > 0) {
			// $('#ddlApplyTo').val('1');
			// $('.itemTypes').show();
			// var itemsType = item.ItemTypes;
			// var arr = itemsType.split(",");
			// $.each(arr, function(i) {
			// $("#lstItemType option[value=" + arr[i] + "]").prop(
			// "selected", "selected");
			// });
			// } else {
			// $('#ddlApplyTo').val('0');
			// }
		},

		BindUserPostionDetails : function(postitionDetails) {
			if (postitionDetails.length != 0) {
				$
						.each(
								postitionDetails,
								function(i, value) {
									// alert(i + " :: " +
									// value['positionTitle']);
									var btnOption = "[+] Add";
									var btnTitle = "Add More"
									var btnName = "AddMore";
									if (i > 0) {
										btnOption = "Delete ";
										btnTitle = "Delete";
										btnName = "DeleteOption";
									}
									var cloneRow = $(
											'#dataTable tbody>tr:first').clone(
											true);
									$(cloneRow).appendTo("#dataTable");

									rowIndex = i + 1;
									$(
											'#dataTable tbody>tr:eq('
													+ rowIndex + ')')
											.find("select")
											.each(
													function(j) {
														if (this.name == "ddlCollege") {
															// $(this).val(value['college']);

															$(this)
																	.find(
																			'option')
																	.each(
																			function() {
																				var $this = $(this);
																				if ($this
																						.text() == value['college']) {
																					$this
																							.prop(
																									'selected',
																									'selected');
																					usersManage
																							.BindDepartmentOnly($(
																									'select[name="ddlCollege"] option:selected')
																									.eq(
																											rowIndex)
																									.val());
																					return false;
																				}
																			});
														} else if (this.name == "ddlDepartment") {
															// $(this).val(value['department']);

															$(this)
																	.find(
																			'option')
																	.each(
																			function() {
																				var $this = $(this);
																				if ($this
																						.text() == value['department']) {
																					$this
																							.prop(
																									'selected',
																									'selected');

																					usersManage
																							.BindPositionTypeOnly(
																									$(
																											'select[name="ddlCollege"] option:selected')
																											.eq(
																													rowIndex)
																											.val(),
																									$(
																											'select[name="ddlDepartment"] option:selected')
																											.eq(
																													rowIndex)
																											.val());
																					return false;
																				}
																			});
														} else if (this.name == "ddlPositionType") {
															// $(this).val(value['positionType']);

															$(this)
																	.find(
																			'option')
																	.each(
																			function() {
																				var $this = $(this);
																				if ($this
																						.text() == value['positionType']) {
																					$this
																							.prop(
																									'selected',
																									'selected');

																					usersManage
																							.BindPositionTitleOnly(
																									$(
																											'select[name="ddlCollege"] option:selected')
																											.eq(
																													rowIndex)
																											.val(),
																									$(
																											'select[name="ddlDepartment"] option:selected')
																											.eq(
																													rowIndex)
																											.val(),
																									$(
																											'select[name="ddlPositionType"] option:selected')
																											.eq(
																													rowIndex)
																											.val());
																					return false;
																				}
																			});
														} else if (this.name == "ddlPositionTitle") {
															// $(this).val(value['positionTitle']);

															$(this)
																	.find(
																			'option')
																	.each(
																			function() {
																				var $this = $(this);
																				if ($this
																						.text() == value['positionTitle']) {
																					$this
																							.prop(
																									'selected',
																									'selected');
																					return false;
																				}
																			});
														}
													});									

									$(
											'#dataTable tbody>tr:eq('
													+ rowIndex + ')').find(
											"input").each(function(l) {												
										var $button = $(this);
										if ($button.is(".AddOption")) {
											$button.prop("name", btnName);
											$button.prop("value", btnOption);
											$button.prop("title", btnTitle);	
										}else if ($button.hasClass("class-isdefault")) {
											$button.prop('checked', value['isDefault']);												
										}
									});
								});
				$('#dataTable>tbody tr:first').remove();
			} else {
				usersManage.BindDepartmentDropDown($(
						'select[name="ddlCollege"]').eq(0).val(), false);
			}
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

			var userId = $('#btnSaveUser').prop("name");
			if (userId == '') {
				userId = "0";
			}

			usersManage.BindUserAuditLogGrid(userId, action, auditedBy,
					activityOnFrom, activityOnTo);
		},

		BindUserAuditLogGrid : function(userId, action, auditedBy,
				activityOnFrom, activityOnTo) {
			this.config.url = this.config.baseURL;
			this.config.method = "GetUserAuditLogList";
			var offset_ = 1;
			var current_ = 1;
			var perpage = ($("#gdvUsersAuditLog_pagesize").length > 0) ? $(
					"#gdvUsersAuditLog_pagesize :selected").text() : 10;

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

			$("#gdvUsersAuditLog").sagegrid({
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
				nomsg : getLocale(gpmsUsersManagement, 'No Records Found!'),
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
			case "gdvUsers":
				if(argus[2].toLowerCase() != "yes"){
					if (argus[1].toLowerCase() != "yes") {
						usersManage.DeleteUserById(argus[0]);
					} else {
						csscody.alert('<h2>'
								+ getLocale(gpmsUsersManagement,
										"Information Alert")
								+ '</h2><p>'
								+ getLocale(gpmsUsersManagement,
										"Sorry! this user is already deleted.")
								+ '</p>');
					}
				} else {
					csscody.alert('<h2>'
								+ getLocale(gpmsUsersManagement,
										"Information Alert")
								+ '</h2><p>'
								+ getLocale(gpmsUsersManagement,
										"Sorry! this user can not be deleted.")
								+ '</p>');
					}
				break;
			default:
				break;
			}
		},

		DeleteUserById : function(_userId) {
			var properties = {
				onComplete : function(e) {
					usersManage.ConfirmSingleDelete(_userId, e);
				}
			};
			csscody.confirm("<h2>"
					+ getLocale(gpmsUsersManagement, "Delete Confirmation")
					+ "</h2><p>"
					+ getLocale(gpmsUsersManagement,
							"Are you sure you want to delete this user?")
					+ "</p>", properties);
		},

		ConfirmSingleDelete : function(user_id, event) {
			if (event) {
				usersManage.DeleteSingleUser(user_id);
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
				usersManage.DeleteMultipleUsers(user_ids);
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
			case "gdvUsers":
				if(argus[2].toLowerCase() != "yes"){
					if (argus[1].toLowerCase() != "yes") {
						usersManage.ActivateUser(argus[0], true);
					} else {
						csscody.alert('<h2>'
								+ getLocale(gpmsUsersManagement,
										"Information Alert")
								+ '</h2><p>'
								+ getLocale(gpmsUsersManagement,
										"Sorry! this user is already actived.")
								+ '</p>');
					}
				} else {
					csscody.alert('<h2>'
							+ getLocale(gpmsUsersManagement,
									"Information Alert")
							+ '</h2><p>'
							+ getLocale(gpmsUsersManagement,
									"Sorry! this user can not be activated.")
							+ '</p>');
				}
				break;
			default:
				break;
			}
		},

		DeactiveUser : function(tblID, argus) {
			switch (tblID) {
			case "gdvUsers":
				if(argus[2].toLowerCase() != "yes"){
					if (argus[1].toLowerCase() != "no") {
						usersManage.ActivateUser(argus[0], false);
					} else {
						csscody.alert('<h2>'
								+ getLocale(gpmsUsersManagement,
										"Information Alert")
								+ '</h2><p>'
								+ getLocale(gpmsUsersManagement,
										"Sorry! this user is already deactived.")
								+ '</p>');
					}
				} else {
					csscody.alert('<h2>'
							+ getLocale(gpmsUsersManagement,
									"Information Alert")
							+ '</h2><p>'
							+ getLocale(gpmsUsersManagement,
									"Sorry! this user can not be deactivated.")
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
			var inputs = $("#container-7").find('INPUT:not(".class-isdefault"), SELECT, TEXTAREA');
			$.each(inputs, function(i, item) {
				// rmErrorClass(item);
				$(this).prop('checked', false);
				$(this).val($(this).find('option').first().val());
			});	
			
			usersManage.onInit();
			$('#lblFormHeading').html(
					getLocale(gpmsUsersManagement, "New User Details"));
			$(".delbutton").removeAttr("id");
			$("#btnSaveUser").removeAttr("name");
			$(".delbutton").hide();
			$("#btnReset").show();
			// $(".required:enabled").each(function() {
			// if ($(this).parent("td").find("span.error").length == 1) {
			// $(this).removeClass("error").addClass("required");
			// $(this).parent("td").find("span.error").remove();
			// }
			// });
			$('#txtUserName').removeAttr('disabled');			

			rowIndex = 0;
			$("#dataTable tbody>tr:gt(0)").remove();
			$("#dataTable tr:eq(1)").find("input:not(:last)").prop('checked', true);
			$(".AddOption").val("[+] Add");

			if (!$('input[name=chkActive]').is(":checked")) {
				$('input[name=chkActive]').prop('checked', 'checked');
			}

			return false;
		},

		onInit : function() {
			usersManage.SetFirstTabActive();
			$('#btnReset').hide();
			$('.cssClassRight').hide();
			$('.warning').hide();

			$("#gdvUsersAuditLog").empty();
			$("#gdvUsersAuditLog_Pagination").remove();
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

		saveUser : function(_userId, _flag) {
			// $('#iferror').hide();
			// var $form = $("#form1");
			// $form.valid();
			if (validator.form()) {
				var $username = $('#txtUserName');
				var userName = $.trim($username.val());
				var validateErrorMessage = usersManage.checkUniqueUserName(
						_userId, userName, $username);
				if (validateErrorMessage == "") {
					var $workEmail = $("#txtWorkEmail");
					var workEmail = $.trim($workEmail.val());
					validateErrorMessage += usersManage
							.checkUniqueEmailAddress(_userId, workEmail,
									"txtWorkEmail");
				}

				if (validateErrorMessage == "") {
					var $personalEmail = $("#txtPersonalEmail");
					var personalEmail = $.trim($personalEmail.val());
					validateErrorMessage += usersManage
							.checkUniqueEmailAddress(_userId, personalEmail,
									"txtPersonalEmail");
				}

				if (validateErrorMessage == "") {
					var _saveOptions = '';
					$("#dataTable")
							.find("tr input:not(:last), select")
							.each(
									function(i) {
										var optionsText = $(this).val();
										if ($(this).hasClass("sfListmenu")) {
											if (!optionsText
													&& $(this).prop("name") != "ddlPositionTitle") {
												validateErrorMessage = getLocale(
														gpmsUsersManagement,
														"Please select all position details for this user.")
														+ "<br/>";
												usersManage.SetFirstTabActive();
												$(this).focus();
											}else{										
												_saveOptions += optionsText + "!#!";
											}										
										} else if ($(this).hasClass("class-isdefault")) {											
				                            var _IsChecked = $(this).prop('checked');
				                            _saveOptions += _IsChecked + "#!#";
				                        }
									});

					_saveOptions = _saveOptions.substring(0,
							_saveOptions.length - 3);

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
						IsActive : $('input[name=chkActive]').prop('checked'),
						UserName : $.trim($('#txtUserName').val()),
						Flag : _flag, // false for Update true for New Add
						SaveOptions : _saveOptions
					};

					var password = $.trim($('#txtPassword').val());
					if (password != "") {
						userInfo.Password = password;
					}
					usersManage.AddUserInfo(userInfo);
				}
			} else {
				usersManage.focusTabWithErrors("#container-7");
			}
		},

		checkUniqueUserName : function(user_id, userName, textBoxUserName) {
			var errors = '';
			if (!textBoxUserName.hasClass('warning') && userName.length > 0) {
				if (!usersManage.isUniqueUserName(user_id, userName)) {
					errors += getLocale(gpmsUsersManagement,
							"Please enter unique username.")
							+ " '"
							+ userName.trim()
							+ "' "
							+ getLocale(gpmsUsersManagement,
									"has already been taken.");
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
			var gpmsCommonInfo = gpmsCommonObj();
			this.config.url = this.config.baseURL + "CheckUniqueUserName";
			this.config.data = JSON2.stringify({
				userUniqueObj : userUniqueObj,
				gpmsCommonObj : gpmsCommonInfo
			});
			this.config.ajaxCallMode = 7;
			this.ajaxCall(this.config);
			return userNameIsUnique;
		},

		checkUniqueEmailAddress : function(user_id, email, textBoxEmail) {
			var errors = '';
			var txtEmail = $("#" + textBoxEmail);
			if (!txtEmail.hasClass('warning') && email.length > 0) {
				if (!usersManage.isUniqueEmail(user_id, email)) {
					errors += getLocale(gpmsUsersManagement,
							"Please enter unique email id.")
							+ " '"
							+ email.trim()
							+ "' "
							+ getLocale(gpmsUsersManagement,
									"has already been taken.");
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
			var gpmsCommonInfo = gpmsCommonObj();
			this.config.url = this.config.baseURL + "CheckUniqueEmail";
			this.config.data = JSON2.stringify({
				userUniqueObj : userUniqueObj,
				gpmsCommonObj : gpmsCommonInfo
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

		BindPositionDetailsHash : function() {
			this.config.url = this.config.baseURL + "GetPositionDetailsHash";
			this.config.data = "{}";
			this.config.ajaxCallMode = 1;
			this.ajaxCall(this.config);
			return false;
		},

		BindCollegeDropDown : function() {
			$('#ddlSearchCollege').get(rowIndex).options.length = 1;
			$('#ddlSearchDepartment').get(rowIndex).options.length = 1;
			$('#ddlSearchPositionType').get(rowIndex).options.length = 1;
			$('#ddlSearchPositionTitle').get(rowIndex).options.length = 1;

			$('select[name="ddlCollege"]').get(rowIndex).options.length = 0;
			$('select[name="ddlDepartment"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;

			$
					.each(
							positions,
							function(keyCollege, valueCollege) {
								$("#ddlSearchCollege").get(rowIndex).options[$(
										"#ddlSearchCollege").get(rowIndex).options.length] = new Option(
										keyCollege, keyCollege);

								// For form Dropdown Binding
								$('select[name="ddlCollege"]').get(rowIndex).options[$(
										'select[name="ddlCollege"]').get(
										rowIndex).options.length] = new Option(
										keyCollege, keyCollege);
							});
			usersManage.BindDepartmentDropDown($(
					'select[name="ddlCollege"] option:selected').eq(rowIndex)
					.val(), false);
			return false;
		},

		BindDepartmentDropDown : function(collegeName, flagSearch) {
			if (flagSearch) {
				$('#ddlSearchDepartment').get(rowIndex).options.length = 1;
				$('#ddlSearchPositionType').get(rowIndex).options.length = 1;
				$('#ddlSearchPositionTitle').get(rowIndex).options.length = 1;
				$
						.each(
								positions,
								function(keyCollege, valueCollege) {
									if (keyCollege == collegeName) {
										$
												.each(
														valueCollege,
														function(keyDepartment,
																valueDepartment) {
															$(
																	"#ddlSearchDepartment")
																	.get(0).options[$(
																	"#ddlSearchDepartment")
																	.get(0).options.length] = new Option(
																	keyDepartment,
																	keyDepartment);
														});
									}
								});

			} else {
				$('select[name="ddlDepartment"]').get(rowIndex).options.length = 0;
				$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
				$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
				$
						.each(
								positions,
								function(keyCollege, valueCollege) {
									if (keyCollege == collegeName) {
										$
												.each(
														valueCollege,
														function(keyDepartment,
																valueDepartment) {
															$(
																	'select[name="ddlDepartment"]')
																	.get(
																			rowIndex).options[$(
																	'select[name="ddlDepartment"]')
																	.get(
																			rowIndex).options.length] = new Option(
																	keyDepartment,
																	keyDepartment);
														});
									}
								});
				usersManage.BindPositionTypeDropDown($(
						'select[name="ddlCollege"] option:selected').eq(
						rowIndex).val(), $(
						'select[name="ddlDepartment"] option:selected').eq(
						rowIndex).val(), false);
			}
			return false;
		},

		BindPositionTypeDropDown : function(collegeName, departmentName,
				flagSearch) {
			if (flagSearch) {
				$('#ddlSearchPositionType').get(rowIndex).options.length = 1;
				$('#ddlSearchPositionTitle').get(rowIndex).options.length = 1;
				$
						.each(
								positions,
								function(keyCollege, valueCollege) {
									if (keyCollege == collegeName) {
										$
												.each(
														valueCollege,
														function(keyDepartment,
																valueDepartment) {
															if (keyDepartment == departmentName) {
																$
																		.each(
																				valueDepartment,
																				function(
																						keyPositionType,
																						valuePositionType) {
																					$(
																							"#ddlSearchPositionType")
																							.get(
																									rowIndex).options[$(
																							"#ddlSearchPositionType")
																							.get(
																									0).options.length] = new Option(
																							keyPositionType,
																							keyPositionType);
																				});
															}
														});
									}
								});
			} else {
				$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
				$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
				$
						.each(
								positions,
								function(keyCollege, valueCollege) {
									if (keyCollege == collegeName) {
										$
												.each(
														valueCollege,
														function(keyDepartment,
																valueDepartment) {
															if (keyDepartment == departmentName) {
																$
																		.each(
																				valueDepartment,
																				function(
																						keyPositionType,
																						valuePositionType) {
																					$(
																							'select[name="ddlPositionType"]')
																							.get(
																									rowIndex).options[$(
																							'select[name="ddlPositionType"]')
																							.get(
																									rowIndex).options.length] = new Option(
																							keyPositionType,
																							keyPositionType);
																				});
															}
														});
									}
								});

				usersManage.BindPositionTitleDropDown($(
						'select[name="ddlCollege"] option:selected').eq(
						rowIndex).val(), $(
						'select[name="ddlDepartment"] option:selected').eq(
						rowIndex).val(), $(
						'select[name="ddlPositionType"] option:selected').eq(
						rowIndex).val(), false);
			}
			return false;
		},

		BindPositionTitleDropDown : function(collegeName, departmentName,
				positionTypeName, flagSearch) {
			if (flagSearch) {
				$('#ddlSearchPositionTitle').get(rowIndex).options.length = 1;
				$
						.each(
								positions,
								function(keyCollege, valueCollege) {
									if (keyCollege == collegeName) {
										$
												.each(
														valueCollege,
														function(keyDepartment,
																valueDepartment) {
															if (keyDepartment == departmentName) {
																$
																		.each(
																				valueDepartment,
																				function(
																						keyPositionType,
																						valuePositionType) {
																					if (keyPositionType == positionTypeName) {
																						for ( var item in valuePositionType) {
																							$(
																									"#ddlSearchPositionTitle")
																									.get(
																											rowIndex).options[$(
																									"#ddlSearchPositionTitle")
																									.get(
																											rowIndex).options.length] = new Option(
																									valuePositionType[item],
																									valuePositionType[item]);
																						}
																					}
																				});

															}
														});
									}
								});
			} else {
				$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
				$
						.each(
								positions,
								function(keyCollege, valueCollege) {
									if (keyCollege == collegeName) {
										$
												.each(
														valueCollege,
														function(keyDepartment,
																valueDepartment) {
															if (keyDepartment == departmentName) {
																$
																		.each(
																				valueDepartment,
																				function(
																						keyPositionType,
																						valuePositionType) {
																					if (keyPositionType == positionTypeName) {
																						for ( var item in valuePositionType) {
																							$(
																									'select[name="ddlPositionTitle"]')
																									.get(
																											rowIndex).options[$(
																									'select[name="ddlPositionTitle"]')
																									.get(
																											rowIndex).options.length] = new Option(
																									valuePositionType[item],
																									valuePositionType[item]);
																						}
																					}
																				});
															}
														});
									}
								});
			}
			return false;
		},

		BindDepartmentOnly : function(collegeName) {
			$('select[name="ddlDepartment"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
			$
					.each(
							positions,
							function(keyCollege, valueCollege) {
								if (keyCollege == collegeName) {
									$
											.each(
													valueCollege,
													function(keyDepartment,
															valueDepartment) {
														$(
																'select[name="ddlDepartment"]')
																.get(rowIndex).options[$(
																'select[name="ddlDepartment"]')
																.get(rowIndex).options.length] = new Option(
																keyDepartment,
																keyDepartment);
													});
								}
							});
			return false;
		},

		BindPositionTypeOnly : function(collegeName, departmentName) {
			$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
			$.each(positions, function(keyCollege, valueCollege) {
				if (keyCollege == collegeName) {
					$.each(valueCollege, function(keyDepartment,
							valueDepartment) {
						if (keyDepartment == departmentName) {
							$.each(valueDepartment, function(keyPositionType,
									valuePositionType) {
								$('select[name="ddlPositionType"]').get(
										rowIndex).options[$(
										'select[name="ddlPositionType"]').get(
										rowIndex).options.length] = new Option(
										keyPositionType, keyPositionType);
							});
						}
					});
				}
			});
			return false;
		},

		BindPositionTitleOnly : function(collegeName, departmentName,
				positionTypeName) {
			$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;
			$
					.each(
							positions,
							function(keyCollege, valueCollege) {
								if (keyCollege == collegeName) {
									$
											.each(
													valueCollege,
													function(keyDepartment,
															valueDepartment) {
														if (keyDepartment == departmentName) {
															$
																	.each(
																			valueDepartment,
																			function(
																					keyPositionType,
																					valuePositionType) {
																				if (keyPositionType == positionTypeName) {
																					for ( var item in valuePositionType) {
																						$(
																								'select[name="ddlPositionTitle"]')
																								.get(
																										rowIndex).options[$(
																								'select[name="ddlPositionTitle"]')
																								.get(
																										rowIndex).options.length] = new Option(
																								valuePositionType[item],
																								valuePositionType[item]);
																					}
																				}
																			});
														}
													});
								}
							});
			return false;
		},

		ajaxSuccess : function(msg) {
			switch (usersManage.config.ajaxCallMode) {
			case 0:
				break;
			case 1: // For Position Details Global Binding
				positions = msg;
				break;

			case 2:// For User Edit Action
				usersManage.FillForm(msg);
				$('#divUserGrid').hide();
				$('#divUserForm').show();
				break;

			case 3:// For Form Department Dropdown Binding
				usersManage.BindUserGrid(null, null, null, null, null, null);
				csscody
						.info("<h2>"
								+ getLocale(gpmsUsersManagement,
										'Successful Message')
								+ "</h2><p>"
								+ getLocale(gpmsUsersManagement,
										'User has been deleted successfully.')
								+ "</p>");

				$('#divUserForm').hide();
				$('#divUserGrid').show();
				break;

			case 4:
				SageData.Get("gdvUsers").Arr.length = 0;
				usersManage.BindUserGrid(null, null, null, null, null, null);
				csscody
						.info("<h2>"
								+ getLocale(gpmsUsersManagement,
										'Successful Message')
								+ "</h2><p>"
								+ getLocale(gpmsUsersManagement,
										'Selected user(s) has been deleted successfully.')
								+ "</p>");
				break;

			case 5:
				usersManage.BindUserGrid(null, null, null, null, null, null);
				csscody.info("<h2>"
						+ getLocale(gpmsUsersManagement, 'Successful Message')
						+ "</h2><p>"
						+ getLocale(gpmsUsersManagement,
								'User has been activated successfully.')
						+ "</p>");
				break;

			case 6:
				usersManage.BindUserGrid(null, null, null, null, null, null);
				csscody.info("<h2>"
						+ getLocale(gpmsUsersManagement, 'Successful Message')
						+ "</h2><p>"
						+ getLocale(gpmsUsersManagement,
								'User has been deactivated successfully.')
						+ "</p>");
				break;

			case 7:
				userNameIsUnique = stringToBoolean(msg);
				break;

			case 8:
				emailIsUnique = stringToBoolean(msg);
				break;

			case 9:
				usersManage.BindUserGrid(null, null, null, null, null, null);
				$('#divUserGrid').show();
				if (editFlag > 0) {
					csscody.info("<h2>"
							+ getLocale(gpmsUsersManagement,
									'Successful Message')
							+ "</h2><p>"
							+ getLocale(gpmsUsersManagement,
									'User has been updated successfully.')
							+ "</p>");
				} else {
					csscody.info("<h2>"
							+ getLocale(gpmsUsersManagement,
									'Successful Message')
							+ "</h2><p>"
							+ getLocale(gpmsUsersManagement,
									'User has been saved successfully.')
							+ "</p>");
				}
				usersManage.ClearForm();
				$('#divUserForm').hide();
				break;
			}
		},

		ajaxFailure : function(msg) {
			switch (usersManage.config.ajaxCallMode) {
			case 0:
				break;
			case 1:
				csscody.error('<h2>'
						+ getLocale(gpmsUsersManagement, "Error Message")
						+ '</h2><p>'
						+ getLocale(gpmsUsersManagement,
								"Failed to load colleges list.") + '</p>');
				break;
			case 2:
				csscody.error('<h2>'
						+ getLocale(gpmsUsersManagement, "Error Message")
						+ '</h2><p>' + "Failed to load user details." + '</p>');
				break;
			case 3:
				csscody.error("<h2>"
						+ getLocale(gpmsUsersManagement, 'Error Message')
						+ "</h2><p>"
						+ getLocale(gpmsUsersManagement,
								'User cannot be deleted.') + "</p>");
				break;
			case 4:
				csscody
						.error("<h2>"
								+ getLocale(gpmsUsersManagement,
										'Error Message')
								+ "</h2><p>"
								+ getLocale(gpmsUsersManagement,
										'Selected user(s) cannot be deleted.')
								+ "</p>");
				break;
			case 5:
				csscody.error("<h2>"
						+ getLocale(gpmsUsersManagement, 'Error Message')
						+ "</h2><p>"
						+ getLocale(gpmsUsersManagement,
								'User cannot be activated.') + "</p>");
				break;
			case 6:
				csscody.error("<h2>"
						+ getLocale(gpmsUsersManagement, 'Error Message')
						+ "</h2><p>"
						+ getLocale(gpmsUsersManagement,
								'User cannot be deactivated.') + "</p>");
				break;
			case 7:
				csscody.error("<h2>"
						+ getLocale(gpmsUsersManagement, 'Error Message')
						+ "</h2><p>"
						+ getLocale(gpmsUsersManagement,
								'Cannot check for unique Username') + "</p>");
				break;

			case 8:
				csscody.error("<h2>"
						+ getLocale(gpmsUsersManagement, 'Error Message')
						+ "</h2><p>"
						+ getLocale(gpmsUsersManagement,
								'Cannot check for unique Email') + "</p>");
				break;

			case 9:
				if (editFlag > 0) {
					csscody.error("<h2>"
							+ getLocale(gpmsUsersManagement, 'Error Message')
							+ "</h2><p>"
							+ getLocale(gpmsUsersManagement,
									'Failed to update user!') + "</p>");
				} else {
					csscody.error("<h2>"
							+ getLocale(gpmsUsersManagement, 'Error Message')
							+ "</h2><p>"
							+ getLocale(gpmsUsersManagement,
									'Failed to save user!') + "</p>");
				}
				break;
			}
		},

		init : function() {
			usersManage.LoadStaticImage();
			usersManage.BindUserGrid(null, null, null, null, null, null);
			$('#divUserForm').hide();
			$('#divUserGrid').show();
			usersManage.BindPositionDetailsHash();
			usersManage.BindCollegeDropDown();

			$("#ddlSearchCollege").bind("change", function() {
				if ($(this).val() != "0") {
					rowIndex = 0;
					usersManage.BindDepartmentDropDown($(this).val(), true);
				} else {
					$('#ddlSearchDepartment').find('option:gt(0)').remove();
				}

			});

			$("#ddlSearchDepartment").bind(
					"change",
					function() {
						if ($("#ddlSearchCollege").val() != "0"
								&& $(this).val() != "0") {
							rowIndex = 0;
							usersManage.BindPositionTypeDropDown($(
									"#ddlSearchCollege").val(), $(this).val(),
									true);
						} else {
							$('#ddlSearchPositionType').find('option:gt(0)')
									.remove();
						}

					});

			$("#ddlSearchPositionType").bind(
					"change",
					function() {
						if ($("#ddlSearchCollege").val() != "0"
								&& $("#ddlSearchDepartment").val() != "0"
								&& $(this).val() != "0") {
							rowIndex = 0;
							usersManage.BindPositionTitleDropDown($(
									"#ddlSearchCollege").val(), $(
									"#ddlSearchDepartment").val(), $(this)
									.val(), true);
						} else {
							$('#ddlSearchPositionTitle').find('option:gt(0)')
									.remove();
						}

					});

			// Form Position details Drop downs
			$('select[name="ddlCollege"]').on("change", function() {
				rowIndex = $(this).closest('tr').prevAll("tr").length;
				if ($(this).val() != "0") {
					usersManage.BindDepartmentDropDown($(this).val(), false);
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
									usersManage.BindPositionTypeDropDown($(
											'select[name="ddlCollege"]').eq(
											rowIndex).val(), $(this).val(),
											false);
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
									usersManage.BindPositionTitleDropDown($(
											'select[name="ddlCollege"]').eq(
											rowIndex).val(), $(
											'select[name="ddlDepartment"]').eq(
											rowIndex).val(), $(this).val(),
											false);
								} else {
									$('select[name="ddlPositionTitle"]').find(
											'option:gt(0)').remove();
								}

							});

			$('#btnDeleteSelected')
					.click(
							function() {
								var user_ids = '';
								user_ids = SageData.Get("gdvUsers").Arr
										.join(',');

								if (user_ids.length > 0) {
									var properties = {
										onComplete : function(e) {
											usersManage.ConfirmDeleteMultiple(
													user_ids, e);
										}
									};
									csscody
											.confirm(
													"<h2>"
															+ getLocale(
																	gpmsUsersManagement,
																	'Delete Confirmation')
															+ "</h2><p>"
															+ getLocale(
																	gpmsUsersManagement,
																	'Are you sure you want to delete selected user(s)?')
															+ "</p>",
													properties);
								} else {
									csscody
											.alert('<h2>'
													+ getLocale(
															gpmsUsersManagement,
															"Information Alert")
													+ '</h2><p>'
													+ getLocale(
															gpmsUsersManagement,
															"Please select at least one user before deleting.")
													+ '</p>');
								}
							});

			$('#btnAddNew').bind(
					"click",
					function() {
						usersManage.ClearForm();
						usersManage.addPwdValidateRules();

						usersManage
								.BindDepartmentDropDown($(
										'select[name="ddlCollege"]').eq(0)
										.val(), false);
						$('#auditLogTab').hide();
						$('#divUserGrid').hide();
						$('#divUserForm').show();
					});

			$('#btnBack').bind("click", function() {
				$('#divUserForm').hide();
				$('#divUserGrid').show();
				usersManage.ClearForm();
			});

			$('#btnReset').bind(
					"click",
					function() {
						usersManage.ClearForm();
						usersManage
								.BindDepartmentDropDown($(
										'select[name="ddlCollege"]').eq(0)
										.val(), false);
					});

			$('#btnSaveUser').click(function(e) {
				$(this).disableWith('Saving...');
				var user_id = $(this).prop("name");
				if (user_id != '') {
					editFlag = user_id;
					usersManage.saveUser(user_id, false);
				} else {
					editFlag = 0;
					usersManage.saveUser("0", true);
				}
				$(this).enable();
				e.preventDefault();
				return false;
			});

			$('#txtPassword').dblclick(function() {
				$(this).val('');
				usersManage.addPwdValidateRules();
			});

			$('#txtUserName').on("focus", function() {
				$(this).siblings('.cssClassRight').hide();
			}), $('#txtUserName').on("blur", function() {
				var userName = $.trim($(this).val());
				var user_id = $('#btnSaveUser').prop("name");
				if (user_id == '') {
					user_id = "0";
				}
				usersManage.checkUniqueUserName(user_id, userName, $(this));
				return false;
			});

			$('#txtWorkEmail, #txtPersonalEmail').on("focus", function() {
				$(this).siblings('.cssClassRight').hide();
			}), $('#txtWorkEmail, #txtPersonalEmail').on("blur", function() {
				var email = $.trim($(this).val());
				var user_id = $('#btnSaveUser').prop("name");
				if (user_id == '') {
					user_id = "0";
				}
				usersManage.checkUniqueEmailAddress(user_id, email, this.id);
				return false;
			});

			$(".delbutton").click(function() {
				// var user_id = $(this).prop("id").replace(/[^0-9]/gi, '');
				var user_id = $(this).prop("id");
				usersManage.DeleteUserById(user_id);
			});

			$("input[type=button].AddOption")
					.on(
							"click",
							function() {
								// var checkedState = false;
								if ($(this).prop("name") == "DeleteOption") {
									var t = $(this).closest('tr');	
									
									if(t.find("input:not(:last)").prop('checked')){
										$("#dataTable tr:eq(1)").find("input:not(:last)").prop('checked', true);
									}
									
									t.find("td").wrapInner(
											"<div style='display: block'/>")
											.parent().find("td div").slideUp(
													300, function() {
														t.remove();
													});

								} else if ($(this).prop("name") == "AddMore") {
									// checkedState = $('#dataTable>tbody
									// tr:first').find('input[type="radio"]').prop("checked");
									var cloneRow = $(this).closest('tr').clone(
											true);
									$(cloneRow).find("input").each(
											function(i) {
												if ($(this).hasClass(
														"AddOption")) {
													$(this).prop("name",
															"DeleteOption");
													$(this).prop("value",
															"Delete ");
													$(this).prop("title",
															"Delete");
												}else if ($(this).hasClass("class-isdefault")) {
						                            this.checked = false;
												}
												$(this).parent('td').find(
														'span').removeClass(
														'error');
												$(this).removeClass('error');
											});
									$(cloneRow).find("select").find("option")
											.each(function(j) {
												$(this).removeAttr("selected");
											});
									$(cloneRow).appendTo("#dataTable").hide()
											.fadeIn(1200);
									// $('#dataTable>tbody
									// tr:first').find('input[type="radio"]').prop("checked",
									// checkedState);

									rowIndex = $('#dataTable > tbody tr')
											.size() - 1;
									usersManage
											.BindDepartmentDropDown(
													$(
															'select[name="ddlCollege"] option:selected')
															.eq(rowIndex).val(),
													false);

									// $('#dataTable tr:last
									// td').fadeIn('slow');
								}
							});
			$("#btnSearchUser").bind("click", function() {
				usersManage.SearchUsers();
				return false;
			});

			$("#btnSearchUserAuditLog").bind("click", function() {
				usersManage.SearchUserAuditLogs();
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

			$(
					'#txtSearchUserName, #ddlSearchCollege, #ddlSearchDepartment, #ddlSearchPositionType, #ddlSearchPositionTitle, #ddlSearchIsActive')
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
	usersManage.init();
});