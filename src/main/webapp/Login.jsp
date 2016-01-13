<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta content="Log in - GPMS" name="DESCRIPTION">
<meta content="Log in - GPMS" name="KEYWORDS">
<meta content="@GPMS" name="COPYRIGHT">
<meta content="GENERATOR" name="GENERATOR">
<meta content="Author" name="AUTHOR">
<meta content="DOCUMENT" name="RESOURCE-TYPE">
<meta content="GLOBAL" name="DISTRIBUTION">
<meta content="INDEX, FOLLOW" name="ROBOTS">
<meta content="1 DAYS" name="REVISIT-AFTER">
<meta content="GENERAL" name="RATING">
<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
<!-- Mimic Internet Explorer 7 -->
<meta content="IE=EmulateIE7" http-equiv="X-UA-Compatible">
<meta content="RevealTrans(Duration=0,Transition=1)"
	http-equiv="PAGE-ENTER">

<meta content="authenticity_token" name="csrf-param">
<meta content="XhX9LjcVdDyY9jnVo+fFJBteb+x2anfwMUyirFUck3U="
	name="csrf-token">
<link type="icon shortcut" media="icon" href="favicon.ico">
<!--[if IE 9]>
        <link rel="stylesheet" href="css/ie9.css" type="text/css" media="screen"/><![endif]-->
<!--[if lt IE 9]>
        <link rel="stylesheet" href="css/IE.css" type="text/css" media="screen"/><![endif]-->
<!--[if lt IE 7]>
        <script type="text/javascript" src="js/core/IE8.js"></script>
    <![endif]-->
<title>Log in - GPMS</title>

<script src="js/jQuery/jquery-1.11.3.min.js" type="text/javascript"></script>

<script type="text/javascript">
	var _aspx_token = "NWExODgyNDctMzA2OS00MWNhLWJjOWEtNGEyODI5N2FiZWJjOlNhZ2VGcmFtZS5BVVRIanhyMzB3eWNqenZwcWQwanYzdmt5Yng0WkFESlg5U0xPQzE6MjAxNTA2MzAxNTA2NTg5NDM5";
	$.ajaxSetup({
		'beforeSend' : function(xhr) {
			xhr.setRequestHeader("ASPX-TOKEN", _aspx_token);
		}
	});
</script>

<script type="text/javascript">
	//<![CDATA[
	var gpmsAppPath = "";
	var gpmsUserName = "superuser";
	var gpmsCurrentCulture = "en-US";
	var gpmsHostURL = "http://localhost:8181/GPMS/";
	var gpmsSecureToken = "GPMS.AUTHjxr30wycjzvpqd0jv3vkybx4ZADJX9SLOC1";

	var gpmsServicePath = "REST/";
	var gpmsRootPath = "http://localhost:8181/GPMS/";
	var userProfileId = '<%=session.getAttribute("userid")%>';
	var sessionCode = "jxr30wycjzvpqd0jv3vkybx4";
	var clientIPAddress = "::1";
	var gpmsCountryName = "RESERVED";
	var gpmsRedirectPath = "/";

	var logInURL = "login";
	var pageExtension = ".jsp";

	$(function() {
		$(".sfLocale").localize({
			moduleKey : gpmsLogIn
		});
	});

	//]]>
</script>

<script type="text/javascript" src="js/core/gpmscore.js"></script>
<script type="text/javascript" src="js/core/rememberme.js"></script>

<script type="text/javascript"
	src="js/FormValidation/jquery.validate.js"></script>

<script type="text/javascript"
	src="js/modules/Language/CoreJsLanguage.js"></script>

<script type="text/javascript" src="js/core/json2.js"></script>

<script type="text/javascript" src="js/jquery-browser.js"></script>
<script type="text/javascript" src="js/jquery.uniform.js"></script>

<script type="text/javascript" src="js/jquery.qtip-1.0.0-rc3.js"></script>

<script type="text/javascript" src="js/MessageBox/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="js/MessageBox/alertbox.js"></script>

<!-- <script type="text/javascript" src="js/core/Session.js"></script> -->
<script type="text/javascript" src="js/core/encoder.js"></script>

<script type="text/javascript" src="js/modules/LogIn.js"></script>
<script type="text/javascript" src="js/modules/Language/GPMSLogIn.js"></script>

<link media="screen" rel="stylesheet"
	href="css/Templates/application.css" type="text/css" />

</head>
<body class="account">
	<div style="display: none;" id="UpdateProgress1">
		<div class="sfLoadingbg">&nbsp;</div>
		<div class="sfLoadingdiv">
			<img id="imgProgress" src="images/ajax-loader.gif"
				style="border-width: 0px;" alt="Loading..." title="Loading..." /> <br>
			<span id="lblPrgress">Please wait...</span>
		</div>
	</div>
	<noscript>
		<span>This page requires java-script to be enabled. Please
			adjust your browser-settings.</span>
	</noscript>
	<div class="account__header">
		<a href="#"><img alt="GPMS" title="GPMS" class="account__logo"
			src="images/logo.png"> </a>
		<nav class="account__nav"> New to GPMS? <a href="SignUp.jsp"
			class="a">Sign Up</a> </nav>
	</div>
	<div class="account__title">
		<h1 class="account__headline h h--1">Welcome back</h1>
		<div class="sh account-header__subheadline sh--1">Log in to your
			GPMS account.</div>
	</div>
	<div class="row">
		<div
			class="row__col row__col--xl-6 row__col--l-7 row__col--m-8 row__col--xl-centered row__col--l-centered row__col--m-centered">
			<div class="account__box box">
				<form accept-charset="UTF-8" action="REST/users/login"
					class="form account__form" id="form1" method="post">
					<div style="margin: 0; padding: 0; display: inline">
						<input name="utf8" type="hidden" value="✓"><input
							name="authenticity_token" type="hidden"
							value="XhX9LjcVdDyY9jnVo+fFJBteb+x2anfwMUyirFUck3U=">
					</div>
					<div id="flash">
						<%
							String message = request.getParameter("msg");
							if (message != null && message.equalsIgnoreCase("error")) {
						%>
						<span class="alert">Invalid email or password.</span>
						<%
							}
						%>
					</div>
					<div class="form__item">
						<label class="label form__label" for="user_email">Email/
							Username</label> <input class="form__input input" id="user_email"
							name="username" size="30" type="text">
					</div>
					<div class="form__item">
						<label class="label form__label" for="user_password">Password</label>
						<input class="form__input input" id="user_password"
							name="password" size="30" type="password">
					</div>
					<div class="form__item">
						<input id="remember_me" name="remember_me" type="checkbox"> <label
							class="form__label label label--inline" for="remember_me">
							Remember me </label>
					</div>
					<input class="btn btn--large btn--expanded btn--blue" name="commit"
						type="submit" value="Log In">
				</form>

				<!-- 				<div class="account__buttons"> -->
				<!-- 					<div class="account__divider uppercase"> -->
				<!-- 						<div class="account__divider-text">Or log in with</div> -->
				<!-- 					</div> -->
				<!-- 					<div class="row row--lessgutter"> -->
				<!-- 						<div class="row__col row__col--m-4"> -->
				<!-- 							<a href="account/profile/auth/github" -->
				<!-- 								class="account__social btn btn--small btn--expanded btn--icon btn--github"><span -->
				<!-- 								class="auth_provider_name">Github</span></a> -->
				<!-- 						</div> -->
				<!-- 						<div class="row__col row__col--m-4"> -->
				<!-- 							<a -->
				<!-- 								href="account/profile/auth/google_oauth2" -->
				<!-- 								class="account__social btn btn--small btn--expanded btn--icon btn--google"><span -->
				<!-- 								class="auth_provider_name">Google</span></a> -->
				<!-- 						</div> -->
				<!-- 						<div class="row__col row__col--m-4"> -->
				<!-- 							<a href="account/profile/auth/twitter" -->
				<!-- 								class="account__social btn btn--small btn--expanded btn--icon btn--twitter"><span -->
				<!-- 								class="auth_provider_name">Twitter</span></a> -->
				<!-- 						</div> -->
				<!-- 					</div> -->
				<!-- 				</div> -->
			</div>
			<div class="account__meta">
				<a href="ForgotPassword.jsp" class="a">Reset password</a> <span>|</span>
				<a href="ResendConfirmation.jsp" class="a">Resend confirmation</a> <span>
			</div>
		</div>
	</div>
</body>
</html>