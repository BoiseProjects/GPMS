var settings = '';

$(function() {
	// For Sidebar active menu
	$('.acitem').find('a').eq(2).prop("class", "active");
	
	jQuery.fn.exists = function() {
		return this.length > 0;
	}
});