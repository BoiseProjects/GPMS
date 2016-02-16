var myAccount = '';

$(function() {

	if (userProfileId == "null") {
		window.location = 'Login.jsp';
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
			UserCollege : GPMS.utils.GetUserCollege(),
			UserDepartment : GPMS.utils.GetUserDepartment(),
			UserPositionType : GPMS.utils.GetUserPositionType(),
			UserPositionTitle : GPMS.utils.GetUserPositionTitle()
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
	var userNameIsUnique = false;
	var emailIsUnique = false;

	var positions = [];

	myAccount = {
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
						type : myAccount.config.type,
						beforeSend : function(request) {
							request.setRequestHeader('GPMS-TOKEN', _aspx_token);
							request.setRequestHeader("UName", GPMS.utils
									.GetUserName());
							request.setRequestHeader("PID", GPMS.utils
									.GetUserProfileID());
							request.setRequestHeader("PType", "v");
							request.setRequestHeader('Escape', '0');
						},
						contentType : myAccount.config.contentType,
						cache : myAccount.config.cache,
						async : myAccount.config.async,
						url : myAccount.config.url,
						data : myAccount.config.data,
						dataType : myAccount.config.dataType,
						success : myAccount.ajaxSuccess,
						error : myAccount.ajaxFailure
					});
		},

		EditUser : function(userProfileId) {
			$('#txtPassword').rules("remove");
			$('#txtConfirmPassword').rules("remove");

			// $("#btnSaveUser").prop("name", userProfileId);
			$("#btnSaveUser").data("name", userProfileId);

			myAccount.config.url = myAccount.config.baseURL
					+ "GetUserDetailsByProfileId";
			myAccount.config.data = JSON2.stringify({
				userId : userProfileId
			});
			myAccount.config.ajaxCallMode = 2;
			myAccount.ajaxCall(myAccount.config);

			myAccount.BindUserAuditLogGrid(userProfileId, null, null, null,
					null);
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

			myAccount.BindUserPostionDetails(response['details']);

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

			$.each(response['userAccount'], function(index, value) {
				$('#txtUserName').val(response['userAccount']['userName']);
				$('#txtUserName').prop('disabled', 'disabled');

				$('#txtPassword').val(response['userAccount']['password']);
			});
		},

		BindUserPostionDetails : function(postitionDetails) {
			if (postitionDetails.length != 0) {
				$
						.each(
								postitionDetails,
								function(i, value) {
									// alert(i + " :: " +
									// value['positionTitle']);
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
																					myAccount
																							.BindDepartmentOnly($(
																									'select[name="ddlCollege"] option:selected')
																									.eq(
																											rowIndex)
																									.val());
																				} else {
																					$this
																							.remove();
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

																					myAccount
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
																				} else {
																					$this
																							.remove();
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

																					myAccount
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
																				} else {
																					$this
																							.remove();
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
																				} else {
																					$this
																							.remove();
																				}
																			});
														}
													});

									$(
											'#dataTable tbody>tr:eq('
													+ rowIndex + ')')
											.find("input")
											.each(
													function(l) {
														var $button = $(this);
														if ($button
																.hasClass("class-isdefault")) {
															$button
																	.prop(
																			'checked',
																			value['isDefault']);
														}
													});
								});
				$('#dataTable>tbody tr:first').remove();
			} else {
				myAccount.BindDepartmentDropDown($('select[name="ddlCollege"]')
						.eq(0).val());
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

			var userId = $('#btnSaveUser').data("name");
			if (userId == '') {
				userId = "0";
			}

			myAccount.BindUserAuditLogGrid(userId, action, auditedBy,
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

		ClearForm : function() {
			validator.resetForm();
			myAccount.SetFirstTabActive();

			$('#divUserAccountForm').show();

			$('.cssClassRight').hide();
			$('.warning').hide();

			$("#gdvUsersAuditLog").empty();
			$("#gdvUsersAuditLog_Pagination").remove();

			// $("#btnSaveUser").removeAttr("name");
			$("#btnSaveUser").removeData("name");

			rowIndex = 0;
			$("#dataTable tbody>tr:gt(0)").remove();
			return false;
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
			if (validator.form()) {
				var $username = $('#txtUserName');
				var userName = $.trim($username.val());
				var validateErrorMessage = myAccount.checkUniqueUserName(
						_userId, userName, $username);
				if (validateErrorMessage == "") {
					var $workEmail = $("#txtWorkEmail");
					var workEmail = $.trim($workEmail.val());
					validateErrorMessage += myAccount.checkUniqueEmailAddress(
							_userId, workEmail, "txtWorkEmail");
				}

				if (validateErrorMessage == "") {
					var $personalEmail = $("#txtPersonalEmail");
					var personalEmail = $.trim($personalEmail.val());
					validateErrorMessage += myAccount.checkUniqueEmailAddress(
							_userId, personalEmail, "txtPersonalEmail");
				}

				if (validateErrorMessage == "") {
					var _saveOptions = '';
					$("#dataTable")
							.find("tr input, select")
							.each(
									function(i) {
										var optionsText = $(this).val();
										if ($(this).hasClass("sfListmenu")) {
											if (!optionsText
													&& $(this).prop("name") != "ddlPositionTitle") {
												validateErrorMessage = 'Please select all position details for this user.'
														+ "<br/>";
												myAccount.SetFirstTabActive();
												$(this).focus();
											} else {
												_saveOptions += optionsText
														+ "!#!";
											}
										} else if ($(this).hasClass(
												"class-isdefault")) {
											var _IsChecked = $(this).prop(
													'checked');
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
						UserName : $.trim($('#txtUserName').val()),
						SaveOptions : _saveOptions
					};

					var password = $.trim($('#txtPassword').val());
					if (password != "") {
						userInfo.Password = password;
					}
					myAccount.AddUserInfo(userInfo);
				}
			} else {
				myAccount.focusTabWithErrors("#container-7");
			}
		},

		checkUniqueUserName : function(user_id, userName, textBoxUserName) {
			var errors = '';
			if (!textBoxUserName.hasClass('warning') && userName.length > 0) {
				if (!myAccount.isUniqueUserName(user_id, userName)) {
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
			var gpmsCommonInfo = gpmsCommonObj();
			this.config.url = this.config.baseURL + "CheckUniqueUserName";
			this.config.data = JSON2.stringify({
				userUniqueObj : userUniqueObj,
				gpmsCommonObj : gpmsCommonInfo
			});
			this.config.ajaxCallMode = 3;
			this.ajaxCall(this.config);
			return userNameIsUnique;
		},

		checkUniqueEmailAddress : function(user_id, email, textBoxEmail) {
			var errors = '';
			var txtEmail = $("#" + textBoxEmail);
			if (!txtEmail.hasClass('warning') && email.length > 0) {
				if (!myAccount.isUniqueEmail(user_id, email)) {
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
			var gpmsCommonInfo = gpmsCommonObj();
			this.config.url = this.config.baseURL + "CheckUniqueEmail";
			this.config.data = JSON2.stringify({
				userUniqueObj : userUniqueObj,
				gpmsCommonObj : gpmsCommonInfo
			});
			this.config.ajaxCallMode = 4;
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
			this.config.ajaxCallMode = 5;
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
			$('select[name="ddlCollege"]').get(rowIndex).options.length = 0;
			$('select[name="ddlDepartment"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionType"]').get(rowIndex).options.length = 0;
			$('select[name="ddlPositionTitle"]').get(rowIndex).options.length = 0;

			$
					.each(
							positions,
							function(keyCollege, valueCollege) {
								// For form Dropdown Binding
								$('select[name="ddlCollege"]').get(rowIndex).options[$(
										'select[name="ddlCollege"]').get(
										rowIndex).options.length] = new Option(
										keyCollege, keyCollege);
							});
			myAccount.BindDepartmentDropDown($(
					'select[name="ddlCollege"] option:selected').eq(rowIndex)
					.val());
			return false;
		},

		BindDepartmentDropDown : function(collegeName) {
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
			myAccount.BindPositionTypeDropDown($(
					'select[name="ddlCollege"] option:selected').eq(rowIndex)
					.val(), $('select[name="ddlDepartment"] option:selected')
					.eq(rowIndex).val());
			return false;
		},

		BindPositionTypeDropDown : function(collegeName, departmentName) {
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

			myAccount.BindPositionTitleDropDown($(
					'select[name="ddlCollege"] option:selected').eq(rowIndex)
					.val(), $('select[name="ddlDepartment"] option:selected')
					.eq(rowIndex).val(), $(
					'select[name="ddlPositionType"] option:selected').eq(
					rowIndex).val());
			return false;
		},

		BindPositionTitleDropDown : function(collegeName, departmentName,
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
			switch (myAccount.config.ajaxCallMode) {
			case 0:
				break;
			case 1: // For Position Details Global Binding
				positions = msg;
				break;

			case 2:// For User Edit Action
				myAccount.FillForm(msg);
				break;

			case 3:
				userNameIsUnique = stringToBoolean(msg);
				break;

			case 4:
				emailIsUnique = stringToBoolean(msg);
				break;

			case 5:
				myAccount.SetFirstTabActive();
				$('.cssClassRight').hide();
				$('.warning').hide();
				$('#txtConfirmPassword').val('');
				$('#txtPassword').rules("remove");
				$('#txtConfirmPassword').rules("remove");
				myAccount.BindUserAuditLogGrid(userProfileId, null, null, null,
						null);
				csscody.info("<h2>" + 'Successful Message' + "</h2><p>"
						+ 'User has been updated successfully.' + "</p>");
				break;
			}
		},

		ajaxFailure : function(msg) {
			switch (myAccount.config.ajaxCallMode) {
			case 0:
				break;
			case 1:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load colleges list.' + '</p>');
				break;
			case 2:
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load user details.' + '</p>');
				break;
			case 3:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot check for unique Username' + "</p>");
				break;

			case 4:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Cannot check for unique Email' + "</p>");
				break;

			case 5:
				csscody.error("<h2>" + 'Error Message' + "</h2><p>"
						+ 'Failed to update user!' + "</p>");
				break;
			}
		},

		init : function() {
			myAccount.ClearForm();
			myAccount.BindPositionDetailsHash();
			myAccount.BindCollegeDropDown();
			myAccount.EditUser(userProfileId);

			$('#txtPassword').rules("remove");
			$('#txtConfirmPassword').rules("remove");

			$('#btnSaveUser').click(function(e) {
				$(this).disableWith('Saving...');
				var user_id = $(this).data("name");
				if (user_id != '') {
					myAccount.saveUser(user_id);
				}
				$(this).enableAgain();
				e.preventDefault();
				return false;
			});

			$('#txtPassword').dblclick(function() {
				$(this).val('');
				myAccount.addPwdValidateRules();
			});

			$('#txtUserName').on("focus", function() {
				$(this).siblings('.cssClassRight').hide();
			}), $('#txtUserName').on("blur", function() {
				var userName = $.trim($(this).val());
				var user_id = $('#btnSaveUser').data("name");
				if (user_id == '') {
					user_id = "0";
				}
				myAccount.checkUniqueUserName(user_id, userName, $(this));
				return false;
			});

			$('#txtWorkEmail, #txtPersonalEmail').on("focus", function() {
				$(this).siblings('.cssClassRight').hide();
			}), $('#txtWorkEmail, #txtPersonalEmail').on("blur", function() {
				var email = $.trim($(this).val());
				var user_id = $('#btnSaveUser').data("name");
				if (user_id == '') {
					user_id = "0";
				}
				myAccount.checkUniqueEmailAddress(user_id, email, this.id);
				return false;
			});

			$("#btnSearchUserAuditLog").bind("click", function() {
				myAccount.SearchUserAuditLogs();
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
					'#txtSearchAction, #txtSearchAuditedBy, #txtSearchActivityOnFrom, #txtSearchActivityOnTo')
					.keyup(function(event) {
						if (event.keyCode == 13) {
							$("#btnSearchUserAuditLog").click();
						}
					});

		}
	};
	myAccount.init();
});