<script type="text/javascript">
    //<![CDATA[
    $(function () {     
        var userProfileId = '<%=session.getAttribute("userProfileId")%>';
		var gpmsUserName = '<%=session.getAttribute("gpmsUserName")%>';
		var isAdmin = '<%=session.getAttribute("isAdmin")%>';
		var userPositionType = '<%=session.getAttribute("userPositionType")%>';
		var	userPositionTitle = '<%=session.getAttribute("userPositionTitle")%>';
		var userDepartment = '<%=session.getAttribute("userDepartment")%>';
		var userCollege = '<%=session.getAttribute("userCollege")%>';

		//if (isAdmin.toLowerCase() == 'true') {
		//	$(".sfDashBoard").show();
		//}

		$('.myProfile').on('click', function() {
			if ($('.myProfileDrop').hasClass('Off')) {
				$('.myProfileDrop').removeClass('Off');
				$('.myProfileDrop').show();
			} else {
				$('.myProfileDrop').addClass('Off');
				$('.myProfileDrop').hide();
			}
		});
	});
	//]]>
</script>

<div class="sfTopbar clearfix" style="position: relative;">
	<ul class="left">
		<li>
			<div class="sfLogo">
				<a href="./Dashboard.jsp" id="topStickybar_hypLogo"><img
					alt="Dashboard" title="Dashboard" src="images/logo_small.png"></a>
			</div>
		</li>
	</ul>

	<ul class="right">
		<li class="home"><a href="./Dashboard.jsp" class="icon-home"
			id="hypHome"></a></li>

		<li class="loggedin"><span class="icon-user"> Logged As</span>
			&nbsp;<strong><%=session.getAttribute("gpmsUserName")%></strong></li>

		<li class="logout"><span class="myProfile icon-arrow-s"></span>
			<div class="myProfileDrop Off" style="display: none;">
				<ul>
					<li>Hello, <%=session.getAttribute("gpmsUserName")%></li>
					<li><a href="./Logout.jsp" class="sfBtnlogin"><i
							class="i-logout"></i>Log Out</a></li>

				</ul>
			</div></li>
	</ul>
	<div class="clear"></div>
</div>