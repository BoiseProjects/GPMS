var home = '';
$(function() {

	var gpmsCommonObj = function() {
		var gpmsCommonInfo = {
			UserName : GPMS.utils.GetUserName(),
			UserProfileID : GPMS.utils.GetUserProfileID(),
			CultureName : GPMS.utils.GetCultureName()
		};
		return gpmsCommonInfo;
	};

	$("#welcome").text("Welcome " + userProfileId);
	alert(userProfileId == null + " From Home.jsp");
	if (userProfileId == null) {
		window.location = 'Login.jsp';
	}
});
