var settings = '';

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
});