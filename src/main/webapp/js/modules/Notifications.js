var notifications = '';

$(function() {
	
	if (userProfileId == "null") {
		window.location = 'Login.jsp';
	}
	
	jQuery.fn.exists = function() {
		return this.length > 0;
	}
});