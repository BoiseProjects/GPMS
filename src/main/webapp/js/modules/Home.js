var Home = '';
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

	var gpmsCommonInfo = {
		UserProfileID : GPMS.utils.GetUserProfileID(),
		UserName : GPMS.utils.GetUserName(),
		UserIsAdmin : GPMS.utils.IsAdmin(),
		UserPositionType : GPMS.utils.GetUserPositionType(),
		UserPositionTitle : GPMS.utils.GetUserPositionTitle(),
		UserDepartment : GPMS.utils.GetUserDepartment(),
		UserCollege : GPMS.utils.GetUserCollege()
	};

	HomeView = {
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
			$.ajax({
				type : this.config.type,
				contentType : this.config.contentType,
				cache : this.config.cache,
				async : this.config.async,
				url : this.config.url,
				data : this.config.data,
				dataType : this.config.dataType,
				success : this.ajaxSuccess,
				error : this.ajaxFailure
			});
		},
		ajaxSuccess : function(msg) {
			switch (HomeView.config.ajaxCallMode) {
			case 0:
				break;
			// your methods
			case 1:
				HomeView.UserProposalGetAllCountSuccess(msg);
				break;
			}
		},
		ajaxFailure : function(msg) {
			switch (HomeView.config.ajaxCallMode) {
			case 0:
				break;
			case 1:
				// Show csscody alert with apt message
				csscody.error('<h2>' + 'Error Message' + '</h2><p>'
						+ 'Failed to load Notifications.' + '</p>');
				break;
			}
		},
		init : function() {
			HomeView.UserProposalGetAllCount();
		},
		UserProposalGetAllCount : function() {
			this.config.method = "GetAllProposalCountForAUser";
			this.config.url = this.config.baseURL + this.config.method;
			this.config.data = JSON2.stringify({
				gpmsCommonObj : gpmsCommonInfo
			});
			this.config.ajaxCallMode = 1;
			this.ajaxCall(this.config);
			return false;
		},
		UserProposalGetAllCountSuccess : function(msg) {
			$("#spanTotalProposal").html(msg.totalProposalCount);
			$("#spanPICount").html(msg.piCount);
			$("#spanCoPICount").html(msg.coPICount);
			$("#spanSeniorCount").html(msg.seniorCount);
		}
	// Add your other methods here
	};
	HomeView.init();
});