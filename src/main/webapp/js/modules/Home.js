var home = '';
$(function() {

	var gpmsCommonObj = function() {
		var gpmsCommonInfo = {
			UserName : GPMS.utils.GetUserName(),
			UserProfileID : GPMS.utils.GetUserProfileID(),
			UserIsAdmin : GPMS.utils.IsAdmin(),
			UserPositionType : GPMS.utils.GetUserPositionType(),
			UserPositionTitle : GPMS.utils.GetUserPositionTitle(),
			UserDepartment : GPMS.utils.GetUserDepartment(),
			UserCollege : GPMS.utils.GetUserCollege()
		};
		console.log(gpmsCommonInfo);
		return gpmsCommonInfo;
	};

	$("#welcome").text("Welcome " + gpmsCommonObj().UserName);
	// if (userProfileId == "null") {
	// window.location = 'Login.jsp';
	// }
});
