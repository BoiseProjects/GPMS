<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta
	content="Your account is on its way. Filling a form takes a minute. Enjoying lasts for weeks."
	name="DESCRIPTION">
<meta content="Sign up, try for free - GPMS" name="KEYWORDS">
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
<link type="icon shortcut" media="icon" href="favicon.ico">
<!--[if IE 9]>
        <link rel="stylesheet" href="css/ie9.css" type="text/css" media="screen"/><![endif]-->
<!--[if lt IE 9]>
        <link rel="stylesheet" href="css/IE.css" type="text/css" media="screen"/><![endif]-->
<!--[if lt IE 7]>
        <script type="text/javascript" src="js/core/IE8.js"></script>
    <![endif]-->
<title>Sign up, try for free - GPMS</title>

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
	
	var userProfileId = '<%=session.getAttribute("userProfileId")%>';
	var gpmsUserName = '<%=session.getAttribute("gpmsUserName")%>';
	var isAdmin = '<%=session.getAttribute("isAdmin")%>';
	var userPositionType = '<%=session.getAttribute("userPositionType")%>';
	var	userPositionTitle = '<%=session.getAttribute("userPositionTitle")%>';
	var userDepartment = '<%=session.getAttribute("userDepartment")%>';
	var userCollege = '<%=session.getAttribute("userCollege")%>';

	var gpmsServicePath = "REST/";
	var gpmsRootPath = "http://localhost:8181/GPMS/";

	//]]>
</script>

<script type="text/javascript" src="js/jQuery/jquery-ui.js"></script>

<script type="text/javascript" src="js/core/gpmscore.js"></script>

<script type="text/javascript" src="js/core/jquery.disable_with.js"></script>

<script type="text/javascript"
	src="js/FormValidation/jquery.validate.js"></script>
<script type="text/javascript"
	src="js/FormValidation/jquery.ui.datepicker.validation.js"></script>
<script type="text/javascript"
	src="js/FormValidation/jquery.maskedinput.js"></script>
<script type="text/javascript" src="js/FormValidation/autoNumeric.js"></script>

<script type="text/javascript" src="js/core/json2.js"></script>

<script type="text/javascript" src="js/jquery-browser.js"></script>
<script type="text/javascript" src="js/jquery.uniform.js"></script>

<script type="text/javascript" src="js/MessageBox/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="js/MessageBox/alertbox.js"></script>

<script type="text/javascript" src="js/modules/SignUp.js"></script>

<link type="text/css" rel="stylesheet"
	href="css/Templates/jquery-ui.css" />

<link type="text/css" rel="stylesheet" href="css/MessageBox/style.css" />

<link media="screen" rel="stylesheet"
	href="css/Templates/application.css" type="text/css" />

</head>
<body class="account">
	<div style="display: none;" id="UpdateProgress1">
		<div class="sfLoadingbg">&nbsp;</div>
		<div class="sfLoadingdiv">
			<img id="imgProgress" src="./images/ajax-loader.gif"
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
		<nav class="account__nav"> Already have an account? <a class="a"
			href="Login.jsp">Log in</a> </nav>
	</div>
	<div class="account__title">
		<h1 class="account__headline h h--1">Your account is on its way.
		</h1>
		<div class="sh account-header__subheadline sh--1">Filling a form
			takes a minute. Enjoying GPMS lasts for weeks.</div>
	</div>
	<div class="row">
		<div
			class="row__col row__col--xl-6 row__col--l-7 row__col--m-8 row__col--xl-centered row__col--l-centered row__col--m-centered">
			<div class="account__box box">
				<form enctype="multipart/form-data" accept-charset="UTF-8"
					action="SignUp.jsp" class="account__form form" method="post"
					name="form1" id="form1">
					<div class="form__item">
						<label class="form__label label" for="txtUserName"> User
							name <span class="cssClassRequired">*</span>
							<div class="label__description">It must be unique</div>
						</label> <input class="form__input input" id="txtUserName"
							placeholder="User Name" name="username" title="User name"><span
							style="display: none;" class="cssClassRight"> <img
							src="./images/right.jpg" class="cssClassSuccessImg" alt="Right"
							title="Right" height="13" width="18">
						</span>
					</div>
					<div class="form__item">
						<label class="form__label label" for="txtPassword">
							Password <span class="cssClassRequired">*</span>
							<div class="label__description">At least 6 and at most 15
								characters, please</div>
						</label> <input class="form__input input" id="txtPassword"
							placeholder="Password" name="password" title="Password"
							type="password">
					</div>

					<div class="form__item">
						<label class="form__label label" for="txtConfirmPassword">
							Confirm Password <span class="cssClassRequired">*</span>
							<div class="label__description">At least 6 and at most 15
								characters, please</div>
						</label> <input class="form__input input" id="txtConfirmPassword"
							placeholder="Password (Again)" name="confirm_password"
							title="Confirm Password" type="password">
					</div>
					<div class="form__item">
						<label class="form__label label" for="txtWorkEmail"> Work
							Email <span class="cssClassRequired">*</span>
							<div class="label__description">No spam, only relevant
								updates</div>
						</label> <input class="form__input input" id="txtWorkEmail"
							placeholder="Work Email" name="workEmail" title="Work Email"><span
							class="cssClassRight"> <img src="./images/right.jpg"
							class="cssClassSuccessImg" height="13" width="18" alt="Right"
							title="Right" />
						</span>
					</div>
					<div class="form__item">
						<label class="form__label label" for="txtFirstName"> First
							Name <span class="cssClassRequired">*</span>
							<div class="label__description">So that we know how to say
								hello</div>
						</label> <input class="form__input input" id="txtFirstName"
							placeholder="First Name" name="firstName" title="First name">
					</div>
					<div class="form__item">
						<label class="form__label label" for="txtMiddleName">
							Middle Name
							<div class="label__description">So that we know how to say
								hello</div>
						</label> <input class="form__input input" id="txtMiddleName"
							placeholder="Middle Name" name="middleName" title="middle name">
					</div>
					<div class="form__item">
						<label class="form__label label" for="txtLastName"> Last
							Name <span class="cssClassRequired">*</span>
							<div class="label__description">So that we know how to say
								hello</div>
						</label> <input class="form__input input" id="txtLastName"
							placeholder="Last Name" name="lastName" title="Last name">
					</div>
					<div class="form__item">
						<label class="form__label label" for="txtDOB"> Date of
							Birth <span class="cssClassRequired">*</span>
							<div class="label__description">So that we know when to say
								happy birthday</div>
						</label> <input class="form__input input" id="txtDOB"
							placeholder="Date of Birth" name="dob" title="Date of Birth">
					</div>
					<div class="form__item">
						<label class="form__label label"> Gender <span
							class="cssClassRequired">*</span>
						</label> <select class="form__input select" id="ddlGender" name="gender"
							title="Choose Gender">
							<option value="">Choose Gender</option>
							<option value="Male">Male</option>
							<option value="Female">Female</option>
						</select>
					</div>

					<div class="form__item">
						<label class="form__label label" for="txtStreet"> Street <span
							class="cssClassRequired">*</span>
							<div class="label__description">So that is where you
								located</div>
						</label> <input class="form__input input" id="txtStreet"
							placeholder="Street" name="street" title="Street">
					</div>
					<div class="form__item">
						<label class="form__label label" for="txtApt"> Apt., Suit,
							Floor, etc
							<div class="label__description">So that is where you stay</div>
						</label> <input class="form__input input" id="txtApt"
							placeholder="Apt., Suite, Floor, etc. (optional)" name="apt"
							title="Apt., Suite, Floor, etc. (optional)">
					</div>
					<div class="form__item">
						<label class="form__label label" for="txtCity"> City <span
							class="cssClassRequired">*</span>
							<div class="label__description">Every user belongs to a
								city</div>
						</label> <input class="form__input input" id="txtCity" placeholder="City"
							name="city" title="City">
					</div>
					<div class="form__item">
						<label class="form__label label"> State <span
							class="cssClassRequired">*</span>
						</label> <select id="ddlState" class="form__input select" name="state"
							title="Choose State"><option value="">Choose
								State</option>
							<option value="1">Alabama</option>
							<option value="2">Alaska</option>
							<option value="4">Arizona</option>
							<option value="5">Arkansas</option>
							<option value="6">California</option>
							<option value="7">Colorado</option>
							<option value="8">Connecticut</option>
							<option value="10">Delaware</option>
							<option value="57">District of Columbia</option>
							<option value="11">Florida</option>
							<option value="12">Georgia</option>
							<option value="14">Hawaii</option>
							<option value="15">Idaho</option>
							<option value="16">Illinois</option>
							<option value="17">Indiana</option>
							<option value="18">Iowa</option>
							<option value="19">Kansas</option>
							<option value="20">Kentucky</option>
							<option value="21">Louisiana</option>
							<option value="22">Maine</option>
							<option value="23">Maryland</option>
							<option value="24">Massachusetts</option>
							<option value="25">Michigan</option>
							<option value="26">Minnesota</option>
							<option value="27">Mississippi</option>
							<option value="28">Missouri</option>
							<option value="29">Montana</option>
							<option value="30">Nebraska</option>
							<option value="31">Nevada</option>
							<option value="32">New Hampshire</option>
							<option value="33">New Jersey</option>
							<option value="34">New Mexico</option>
							<option value="35">New York</option>
							<option value="36">North Carolina</option>
							<option value="37">North Dakota</option>
							<option value="39">Ohio</option>
							<option value="40">Oklahoma</option>
							<option value="41">Oregon</option>
							<option value="42">Pennsylvania</option>
							<option value="58">Puerto Rico</option>
							<option value="44">Rhode Island</option>
							<option value="45">South Carolina</option>
							<option value="46">South Dakota</option>
							<option value="47">Tennessee</option>
							<option value="48">Texas</option>
							<option value="59">U.S. Virgin Islands</option>
							<option value="49">Utah</option>
							<option value="50">Vermont</option>
							<option value="51">Virginia</option>
							<option value="53">Washington</option>
							<option value="54">West Virginia</option>
							<option value="55">Wisconsin</option>
							<option value="56">Wyoming</option>
						</select>
					</div>
					<div class="form__item">
						<label class="form__label label" for="txtZip"> Zip Code <span
							class="cssClassRequired">*</span>
							<div class="label__description">Every state has a zip code</div>
						</label> <input class="form__input input" id="txtZip"
							placeholder="Zip Code" name="zip" title="Zip Code">
					</div>
					<div class="form__item">
						<label class="form__label label"> Country <span
							class="cssClassRequired">*</span>
						</label> <select id="ddlCountry" class="form__input select" name="country"
							title="Choose Country"><option value="">Choose
								Country</option>
							<option value="1">United States</option>
						</select>
					</div>
					<div class="form__item">
						<label class="form__label label" for="txtMobileNumber">
							Mobile Number <span class="cssClassRequired">*</span>
							<div class="label__description">So that is where we can
								call you</div>
						</label> <input class="form__input input" id="txtMobileNumber"
							placeholder="Mobile Number" name="mobileNumber"
							title="Mobile Number">
					</div>

					<button type="button" id="btnSignUp"
						class="btn btn--large btn--expanded btn--blue">
						<span>Sign up</span>
					</button>
				</form>
				<!-- 				<div class="account__buttons"> -->
				<!-- 					<div class="account__divider uppercase"> -->
				<!-- 						<div class="account__divider-text">Or sign up with</div> -->
				<!-- 					</div> -->
				<!-- 					<div class="row row--lessgutter"> -->
				<!-- 						<div class="row__col row__col--m-6"> -->
				<!-- 							<a -->
				<!-- 								class="account__social btn btn--small btn--expanded btn--icon btn--github" -->
				<!-- 								data-track="Sent Valid Signup" data-track-category="Sign Up" -->
				<!-- 								data-track-label="Github" href="account/profile/auth/github">Github -->
				<!-- 							</a> -->
				<!-- 						</div> -->
				<!-- 						<div class="row__col row__col--m-6"> -->
				<!-- 							<a class="btn btn--small btn--expanded btn--icon btn--google" -->
				<!-- 								data-track="Sent Valid Signup" data-track-category="Sign Up" -->
				<!-- 								data-track-label="Google" -->
				<!-- 								href="account/profile/auth/google_oauth2">Google </a> -->
				<!-- 						</div> -->
				<!-- 					</div> -->
				<!-- 				</div> -->
			</div>
			<div class="account__meta">
				By signing up you agree to our <br> <a class="a"
					href="TermsAndService.jsp">Terms of Service</a> and <a class="a"
					href="PrivacyPolicy.jsp">Privacy Policy</a>
			</div>
		</div>
	</div>
</body>
</html>