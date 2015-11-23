var logIn = '';
$(function() {

	var userSession = "<%=session.getAttribute('userid')%>";
	// if (userSession != null) {
	// window.location = 'Home.jsp';
	// }

	var validator = $('#form1')
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
							}
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
							}
						},
						submitHandler : function(form, event) {
							form.submit();
							event.preventDefault();
						}
					});
});