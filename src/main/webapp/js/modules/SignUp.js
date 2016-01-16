var signUp = '';
$(function() {
	jQuery.fn.exists = function() {
		return this.length > 0;
	}

	var validator = $("#form1")
			.validate(
					{
						rules : {
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
							},
							workEmail : {
								required : true,
								email : true
							},
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
						},
						errorElement : "label",
						messages : {
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
								equalTo : "Please enter the same password as above",
								maxlength : "Your password must be between 6 and 15 characters"
							},
							workEmail : {
								required : "Please enter your work email",
								email : "Please enter valid email id"
							},
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
							}
						}
					});

	var userNameIsUnique = false;
	var emailIsUnique = false;

	signUp = {
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
						type : signUp.config.type,
						beforeSend : function(request) {
							request.setRequestHeader('GPMS-TOKEN', _aspx_token);
							request.setRequestHeader("UName", GPMS.utils
									.GetUserName());
							request.setRequestHeader("PID", GPMS.utils
									.GetUserProfileID());
							request.setRequestHeader("PType", "v");
							request.setRequestHeader('Escape', '0');
						},
						contentType : signUp.config.contentType,
						cache : signUp.config.cache,
						async : signUp.config.async,
						url : signUp.config.url,
						data : signUp.config.data,
						dataType : signUp.config.dataType,
						success : signUp.ajaxSuccess,
						error : signUp.ajaxFailure
					});
		},

		LoadStaticImage : function() {
			$('.cssClassSuccessImg').prop('src', 'images/right.jpg');
		},

		checkUniqueUserName : function(user_id, userName, textBoxUserName) {
			var errors = '';
			if (!textBoxUserName.hasClass('error') && userName.length > 0) {
				if (!signUp.isUniqueUserName(user_id, userName)) {
					errors += getLocale(gpmsSignUp,
							"Please enter unique username.")
							+ " '"
							+ userName.trim()
							+ "' "
							+ getLocale(gpmsSignUp, "has already been taken.");
					textBoxUserName.addClass("error");
					textBoxUserName.siblings('.cssClassRight').hide();
					if (textBoxUserName.siblings('label.error').exists()) {
						textBoxUserName.siblings('label.error').html(errors);
					} else {
						$(
								'<label id="txtUserName-error" class="error" for="txtUserName">'
										+ errors + '</label>').insertAfter(
								textBoxUserName);
					}

					textBoxUserName.siblings('.error').show();
					// textBoxUserName.focus();
				} else {
					textBoxUserName.removeClass("error");
					textBoxUserName.siblings('.cssClassRight').show();
					textBoxUserName.siblings('.error').hide();
					textBoxUserName.siblings('.error').html('');
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
				userUniqueObj : userUniqueObj
			});
			this.config.ajaxCallMode = 1;
			this.ajaxCall(this.config);
			return userNameIsUnique;
		},

		checkUniqueEmailAddress : function(user_id, email, textBoxEmail) {
			var errors = '';
			var txtEmail = $("#" + textBoxEmail);
			if (!txtEmail.hasClass('error') && email.length > 0) {
				if (!signUp.isUniqueEmail(user_id, email)) {
					errors += getLocale(gpmsSignUp,
							"Please enter unique email id.")
							+ " '"
							+ email.trim()
							+ "' "
							+ getLocale(gpmsSignUp, "has already been taken.");
					txtEmail.addClass("error");
					txtEmail.siblings('.cssClassRight').hide();
					if (txtEmail.siblings('label.error').exists()) {
						txtEmail.siblings('.error').html(errors);
					} else {
						$(
								'<label id="' + textBoxEmail
										+ '-error" class="error" for="'
										+ textBoxEmail + '">' + errors
										+ '</label>').insertAfter(txtEmail);
					}

					txtEmail.siblings('.error').show();
					// txtEmail.focus();
				} else {
					txtEmail.removeClass("error");
					txtEmail.siblings('.cssClassRight').show();
					txtEmail.siblings('.error').hide();
					txtEmail.siblings('.error').html('');
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
				userUniqueObj : userUniqueObj
			});
			this.config.ajaxCallMode = 2;
			this.ajaxCall(this.config);
			return emailIsUnique;
		},

		signUpUser : function() {
			if (validator.form()) {
				var $username = $('#txtUserName');
				var userName = $.trim($username.val());
				var user_id = "0";
				var validateErrorMessage = signUp.checkUniqueUserName(user_id,
						userName, $username);

				if (validateErrorMessage == "") {
					var $workEmail = $("#txtWorkEmail");
					var workEmail = $.trim($workEmail.val());
					validateErrorMessage += signUp.checkUniqueEmailAddress(
							user_id, workEmail, "txtWorkEmail");
				}

				if (validateErrorMessage == "") {
					var userInfo = {
						UserID : user_id,
						UserName : $.trim($('#txtUserName').val()),
						Password : $.trim($('#txtPassword').val()),
						WorkEmail : $('#txtWorkEmail').val(),
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
						MobileNumber : $('#txtMobileNumber').mask()
					};
					signUp.AddUserInfo(userInfo);
				} else {
					return false;
				}
			}
		},

		AddUserInfo : function(info) {
			this.config.url = this.config.baseURL + "signup";
			this.config.data = JSON2.stringify({
				userInfo : info
			});
			this.config.ajaxCallMode = 3;
			this.ajaxCall(this.config);
			return false;
		},

		ClearForm : function() {
			validator.resetForm();
			$('.cssClassRight').hide();
			$('.warning').hide();

			var inputs = $("#form1").find('INPUT, SELECT');
			$.each(inputs, function(i, item) {
				// rmErrorClass(item);
				$(this).val('');
				$(this).val($(this).find('option').first().val());
			});
			return false;
		},

		ajaxSuccess : function(msg) {
			switch (signUp.config.ajaxCallMode) {
			case 0:
				break;
			case 1:
				userNameIsUnique = stringToBoolean(msg);
				break;
			case 2:
				emailIsUnique = stringToBoolean(msg);
				break;
			case 3:
				csscody
						.info("<h2>"
								+ getLocale(gpmsSignUp,
										'Great! You are signed up.')
								+ "</h2><p>"
								+ "<b>"
								+ $("#txtWorkEmail").val()
								+ "</b>"
								+ "</br>"
								+ getLocale(
										gpmsSignUp,
										'Now, go check your email.<br/>The email contains a confirmation link for account activation.')
								+ "</p>");

				signUp.ClearForm();
				break;
			}
		},

		ajaxFailure : function(msg) {
			switch (signUp.config.ajaxCallMode) {
			case 0:
				break;
			case 1:
				csscody.error("<h2>"
						+ getLocale(gpmsSignUp, 'Error Message')
						+ "</h2><p>"
						+ getLocale(gpmsSignUp,
								'Cannot check for unique Username') + "</p>");
				break;
			case 2:
				csscody
						.error("<h2>"
								+ getLocale(gpmsSignUp, 'Error Message')
								+ "</h2><p>"
								+ getLocale(gpmsSignUp,
										'Cannot check for unique Email')
								+ "</p>");
				break;
			case 3:
				csscody.error("<h2>" + getLocale(gpmsSignUp, 'Error Message')
						+ "</h2><p>"
						+ getLocale(gpmsSignUp, 'Failed to save user!')
						+ "</p>");
				break;
			}
		},

		init : function() {
			signUp.LoadStaticImage();
			signUp.ClearForm();

			$("#txtMobileNumber").mask("(999) 999-9999");
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

			$("#btnSignUp").on("click", function(e) {
				$(this).disableWith('Registering...');
				signUp.signUpUser();
				$(this).enable();
				e.preventDefault();
				return false;
			});

			$('#txtUserName').on("focus", function() {
				$(this).siblings('.cssClassRight').hide();
			}), $('#txtUserName').on("blur", function() {
				var userName = $.trim($(this).val());
				var user_id = "0";
				signUp.checkUniqueUserName(user_id, userName, $(this));
				return false;
			});

			$('#txtWorkEmail').on("focus", function() {
				$(this).siblings('.cssClassRight').hide();
			}), $('#txtWorkEmail').on("blur", function() {
				var email = $.trim($(this).val());
				var user_id = "0";
				signUp.checkUniqueEmailAddress(user_id, email, this.id);
				return false;
			});

			var $form = $("#form1");
			$form.find("[data-form-input]").on(
					"focus",
					function() {
						$this = $(this), fieldName = $this.attr("id"), $(
								'[for="' + fieldName + '"]').find(
								"[data-form-label-description]").addClass(
								"is-visible")
					}), $form.find("[data-form-input]").on(
					"blur",
					function() {
						$("[data-form-label-description].is-visible")
								.removeClass("is-visible")
					});
		}
	};
	signUp.init();
});