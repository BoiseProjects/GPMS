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

	$("#welcome").text("Welcome " + userProfileId.toString());
	if (userProfileId == "null") {
		window.location = 'Login.jsp';
	}
});
