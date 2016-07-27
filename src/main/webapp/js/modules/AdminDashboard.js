var adminDashboard = '';
$(function() {

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
		// console.log(gpmsCommonInfo);
		return gpmsCommonInfo;
	};

	// $("#welcome").text("Welcome " + gpmsCommonObj().UserName);
	gpmsCommonObj();
	if (isAdmin == false) {
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
});