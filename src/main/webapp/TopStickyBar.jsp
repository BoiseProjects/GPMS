<script type="text/javascript">
    //<![CDATA[
    var topStickyBar ='';
    
    $(function () {     
    	var userProfileId = '<%=session.getAttribute("userProfileId")%>';
		var gpmsUserName = '<%=session.getAttribute("gpmsUserName")%>';
		var isAdmin = '<%=session.getAttribute("isAdmin")%>';
		var userPositionType = '<%=session.getAttribute("userPositionType")%>';
		var	userPositionTitle = '<%=session.getAttribute("userPositionTitle")%>';
		var userDepartment = '<%=session.getAttribute("userDepartment")%>';
		var userCollege = '<%=session.getAttribute("userCollege")%>';

		topStickyBar = {
			config : {
				isPostBack : false,
				async : false,
				cache : false,
				type : 'POST',
				contentType : "application/json; charset=utf-8",
				data : '{}',
				dataType : 'json',
				baseURL : GPMS.utils.GetGPMSServicePath() + "users/",
				method : "",
				url : "",
				ajaxCallMode : 0
			},

			ajaxCall : function(config) {
				$.ajax({
					type : topStickyBar.config.type,
					beforeSend : function(request) {
						request.setRequestHeader('GPMS-TOKEN', _aspx_token);
						request.setRequestHeader("UName", GPMS.utils
								.GetUserName());
						request.setRequestHeader("PID", GPMS.utils
								.GetUserProfileID());
						request.setRequestHeader("PType", "v");
						request.setRequestHeader('Escape', '0');
					},
					contentType : topStickyBar.config.contentType,
					cache : topStickyBar.config.cache,
					async : topStickyBar.config.async,
					url : topStickyBar.config.url,
					data : topStickyBar.config.data,
					dataType : topStickyBar.config.dataType,
					success : topStickyBar.ajaxSuccess,
					error : topStickyBar.ajaxFailure
				});
			},

			ajaxSuccess : function(msg) {
				switch (topStickyBar.config.ajaxCallMode) {
				case 0:
					break;
				case 1:
					topStickyBar.LoadUserPositionViews(msg);
					break;
				case 2:
					var currentPage = location.pathname
							.substring(location.pathname.lastIndexOf("/") + 1);
					window.location = currentPage;
					break;
				}
			},

			ajaxFailure : function(msg) {
				switch (topStickyBar.config.ajaxCallMode) {
				case 0:
					break;
				case 1:
					csscody.error('<h2>' + "Error Message" + '</h2><p>'
							+ "Failed to load your position list." + '</p>');
					break;
				case 2:
					csscody.error('<h2>' + "Error Message" + '</h2><p>'
							+ "Failed to set your position view." + '</p>');
					break;
				}
			},

			SetViewSession : function(userId, college, department,
					positionType, positionTitle) {
				this.config.url = this.config.baseURL + "SetUserViewSession";
				this.config.data = JSON2.stringify({
					userId : userId,
					userName : gpmsUserName,
					isAdminUser : isAdmin,
					college : college,
					department : department,
					positionType : positionType,
					positionTitle : positionTitle
				});
				this.config.ajaxCallMode = 2;
				this.ajaxCall(this.config);
				return false;
			},

			UserPositionViews : function() {
				this.config.url = this.config.baseURL
						+ "GetUserDetailsByProfileId";
				this.config.data = JSON2.stringify({
					userId : userProfileId
				});
				this.config.ajaxCallMode = 1;
				this.ajaxCall(this.config);
				return false;
			},

			LoadUserPositionViews : function(response) {
				topStickyBar.BindUserPostionDetails(response['details']);
			},

			BindUserPostionDetails : function(postitionDetails) {
				if (postitionDetails.length != 0) {
					$
							.each(
									postitionDetails,
									function(i, value) {
										var $element = $('<a>')
												.attr('href', '#')
												.attr('data-college',
														value['college'])
												.attr('data-department',
														value['department'])
												.attr('data-positionType',
														value['positionType'])
												.attr('data-positionTitle',
														value['positionTitle'])
												.append(
														"View As "
																+ value['positionTitle']);

										if (value['college'] == userCollege
												&& value['department'] == userDepartment
												&& value['positionType'] == userPositionType
												&& value['positionTitle'] == userPositionTitle) {
											$element.attr('class',
													'icon-checked');
										}
										$('#ulLoggedRoles li:last').before(
												$('<li>').append($element));
									});
				} else {
					csscody
							.error('<h2>'
									+ "Error Message"
									+ '</h2><p>'
									+ "You are not assigned to any position title yet! Please contact administrator as soon as possible."
									+ '</p>');
				}
			},

			init : function() {
				topStickyBar.UserPositionViews();

				$("#ulLoggedRoles > li:gt(1):not(:last) a")
						.on(
								'click',
								function() {
									$this = $(this);
									if (!$this.hasClass('icon-checked')) {
										$this.attr('class', 'icon-checked');
										$("#ulLoggedRoles li a").not(this)
												.removeAttr('class');
										$('.myProfile').trigger("click");
										var $college = $this
												.attr('data-college');
										var $department = $this
												.attr('data-department');
										var $positionType = $this
												.attr('data-positionType');
										var $positionTitle = $this
												.attr('data-positionTitle');
										topStickyBar.SetViewSession(
												userProfileId, $college,
												$department, $positionType,
												$positionTitle);
									}
								});

				$('.myProfile').on('click', function() {
					if ($('.myProfileDrop').hasClass('Off')) {
						$('.myProfileDrop').removeClass('Off');
						$('.myProfileDrop').show();
					} else {
						$('.myProfileDrop').addClass('Off');
						$('.myProfileDrop').hide();
					}
				});
			}
		};
		topStickyBar.init();

		//for Notification
		$(this).NotificationViewDetails();
	});
	//]]>
</script>

<script type="text/javascript" src="js/modules/Notifications.js"></script>

<script type="text/javascript" src="js/EventSource.js"></script>

<div class="sfTopbar clearfix" style="position: relative;">
	<ul class="left">
		<li>
			<div class="sfLogo">
				<a href="./Home.jsp" id="topStickybar_hypLogo"><img alt="Home"
					title="Home" src="images/logo_small.png"></a>
			</div>
		</li>
	</ul>

	<ul class="right">
		<li class="home"><a href="./Home.jsp" class="icon-home"
			title="Home">Home</a></li>

		<li class="sfquickNotification">
			<div id="divNotification" class="sfHtmlview notificationsSticker">
				<ul>
					<li class="notifyInfoPanel"><span id="spanNotifyInfo"
						class="notired" style="display: none">0</span> <a
						id="linkNotifyInfo"
						class="showfrindreq mesgnotfctn topopup icon-portal-management"
						title="Click to View Recent Activities">&nbsp;</a>
						<div class="beeperNub" style="display: none;"></div>
						<div class="cssClassNotify" style="display: none;"></div></li>
				</ul>
			</div>
		</li>

		<li class="loggedin"><span class="icon-user"> Logged As</span>
			&nbsp;<strong><%=session.getAttribute("gpmsUserName")%></strong></li>

		<li class="logout"><span class="myProfile icon-arrow-s"></span>
			<div class="myProfileDrop Off" style="display: none;">
				<ul id="ulLoggedRoles">
					<li>Hello, <%=session.getAttribute("gpmsUserName")%></li>
					<li class="myaccount"><a href="./MyAccount.jsp">My Account</a></li>
					<li><a href="./Logout.jsp" class="sfBtnlogin"><i
							class="i-logout"></i>Log Out</a></li>

				</ul>
			</div></li>
	</ul>
	<div class="clear"></div>
</div>