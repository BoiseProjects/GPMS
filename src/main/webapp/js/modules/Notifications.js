(function($) {
	$.NotificationViewList = function(p) {
		p = $.extend({
			modulePath : GPMS.utils.GetGPMSServicePath(),
			notificationsNumber : 0,
			originaltitle : $('title').text()
		}, p);
		var gpmsCommonInfo = {
			UserProfileID : GPMS.utils.GetUserProfileID(),
			UserName : GPMS.utils.GetUserName(),
			UserIsAdmin : GPMS.utils.IsAdmin(),
			UserPositionType : GPMS.utils.GetUserPositionType(),
			UserPositionTitle : GPMS.utils.GetUserPositionTitle(),
			UserDepartment : GPMS.utils.GetUserDepartment(),
			UserCollege : GPMS.utils.GetUserCollege()
		};
		// this.config.data = JSON2.stringify({ UserProfileID:
		// gpmsCommonInfo.UserProfileID, UserName: gpmsCommonInfo.UserName });
		var gpmsCommonObj = function() {
			var gpmsCommonInfo = {
				UserProfileID : GPMS.utils.GetUserProfileID(),
				UserName : GPMS.utils.GetUserName(),
				UserIsAdmin : GPMS.utils.IsAdmin(),
				UserPositionType : GPMS.utils.GetUserPositionType(),
				UserPositionTitle : GPMS.utils.GetUserPositionTitle(),
				UserDepartment : GPMS.utils.GetUserDepartment(),
				UserCollege : GPMS.utils.GetUserCollege()
			};
			return gpmsCommonInfo;
		};

		var popupStatus = 0;
		NotificationView = {
			config : {
				isPostBack : false,
				async : false,
				cache : false,
				type : 'POST',
				contentType : "application/json; charset=utf-8",
				data : '{}',
				dataType : 'json',
				baseURL : p.modulePath + "notifications/",
				method : "",
				url : "",
				ajaxCallMode : 0
			},
			ajaxCall : function(config) {
				$.ajax({
					type : this.config.type,
					contentType : this.config.contentType,
					cache : this.config.cache,
					async : this.config.async,
					url : this.config.url,
					data : this.config.data,
					dataType : this.config.dataType,
					success : this.ajaxSuccess,
					error : this.ajaxFailure
				});
			},
			ajaxSuccess : function(msg) {
				switch (NotificationView.config.ajaxCallMode) {
				case 0:
					break;
				// your methods
				case 1:
					NotificationView.NotificationGetAllCountSuccess(msg);
					break;

				case 2:
					NotificationView.NotificationGetAllSuccess(msg);
					break;
				}
			},
			ajaxFailure : function(msg) {
				switch (NotificationView.config.ajaxCallMode) {
				case 0:
					break;
				case 1:
					// Show csscody alert with apt message
					csscody.error('<h2>' + 'Error Message' + '</h2><p>'
							+ 'Failed to load Notifications.' + '</p>');
					break;
				case 2:
					csscody.error('<h2>' + 'Error Message' + '</h2><p>'
							+ 'Failed to load Notifications details.' + '</p>');
					break;
				}
			},
			init : function() {
				NotificationView.NotificationGetAllCount();
				$('#linkNotifyInfo').click(function() {
					if ($('.cssClassNotify').is(":hidden")) {
						$(this).addClass("sfNotificationSelect");
						$('.beeperNub').show();
						NotificationView.NotificationGetAll();
						$('.cssClassNotify').slideDown('slow');
					} else {
						$(this).removeClass("sfNotificationSelect");												
						$('.cssClassNotify').slideUp('slow');
						$('.beeperNub').hide();
					}
				});

				$(".notificationsSticker").outside('click', function() {
					if ($('.cssClassNotify').is(":visible")) {
						$('.cssClassNotify').slideUp('slow');
						$('#linkNotifyInfo').removeClass("sfNotificationSelect");
						$('.beeperNub').hide();
					}
					return false;
				});

				$(document).on(
						'click',
						'a.cssClassLowItemInfo',
						function() {
							var itemsku = $(this).attr('id');
							location.href = aspxRedirectPath + 'item/'
									+ itemsku + pageExtension;
							return false;
						});
			},
			UpdateTitle : function() {
				if (p.notificationsNumber > 0) {
					$('title').text(
							$('title').text() + " (" + p.notificationsNumber
									+ ")");
				} else {
					$('title').text(originaltitle);
				}
			},
			NotificationGetAllCount : function() {
				this.config.method = "NotificationGetAllCount";
				this.config.url = this.config.baseURL + this.config.method;
				this.config.data = JSON2.stringify({
					gpmsCommonObj : gpmsCommonInfo
				});
				this.config.ajaxCallMode = 1;
				this.ajaxCall(this.config);
				return false;
			},
			NotificationGetAllCountSuccess : function(msg) {
				if (msg !== 0) {
					$("#spanNotifyInfo").html(msg);
					$("#spanNotifyInfo").show();
					p.notificationsNumber += parseInt(msg);
				} else {
					$("#spanNotifyInfo").hide();
				}

				// NotificationView.UpdateTitle();
			},
			NotificationGetAll : function() {
				this.config.method = "NotificationGetAll";
				this.config.url = this.config.baseURL + this.config.method;
				this.config.data = JSON2.stringify({
					gpmsCommonObj : gpmsCommonInfo
				});
				this.config.ajaxCallMode = 2;
				this.ajaxCall(this.config);
				$("#spanNotifyInfo").hide();
				return false;
			},
			NotificationGetAllSuccess : function(msg) {
				var contentUser = "";
				var allContent = "";

				contentUser = '<div class="beeperNubWrapper">';

				if (msg.length > 0) {
					contentUser += '<h5 class="cssClassNotifyHead">'
							+ 'Notifications' + '</h5><ul>';
					var i = 1;

					var userID = "";
					var userName = "";
					var proposalID = "";

					var intNewUsers = parseInt($('#spanNotifyInfo').text());

					$
							.each(
									msg,
									function(index, value) {
										var classForAction = "status registered";
										if (value.critical) {
											classForAction = "status outOfStock";
										}
										switch (value.type) {
										case 'Investigator':
											userID = strEncrypt(value.userProfileId);
											userName = strEncrypt(value.username);

											contentUser += '<li '
													+ (intNewUsers > 0 ? 'class="sfLastestNotification"'
															: '')
													+ '>'
													+ '<a id="'
													+ value.proposalId
													+ '" title="Click to View" href = "'
													+ './ManageProposals.jsp?proposalID='
													+ proposalID
													+ '">'
													+ value.proposalTitle
													+ '</a><span class="activityon">'
													+ $.format
															.date(
																	value.activityDate,
																	'yyyy/MM/dd hh:mm:ss a')
													+ '</span><span class="'
													+ classForAction
													+ '"><strong>'
													+ value.action
													+ '</strong></span>'
													+ ' </li>';
											break;
										case 'User':
											userID = strEncrypt(value.userProfileId);
											userName = strEncrypt(value.username);

											contentUser += '<li '
													+ (intNewUsers > 0 ? 'class="sfLastestNotification"'
															: '')
													+ '>'
													+ '<a id="'
													+ value.username
													+ '" title="Click to View" href = "'
													+ './ManageUsers.jsp?userID='
													+ userID
													+ "&userName="
													+ userName
													+ '"> '
													+ value.username
													+ '</a><span class="activityon">'
													+ $.format
															.date(
																	value.activityDate,
																	'yyyy/MM/dd hh:mm:ss a')
													+ '</span><span class="'
													+ classForAction
													+ '"><strong>'
													+ value.action
													+ '</strong></span>'
													+ ' </li>';
											break;
										case 'Proposal':
											proposalID = strEncrypt(value.proposalId);
											// userID =
											// strEncrypt(value.userProfileId);
											// userName =
											// strEncrypt(value.username);

											contentUser += '<li '
													+ (intNewUsers > 0 ? 'class="sfLastestNotification"'
															: '')
													+ '>'
													+ '<a id="'
													+ value.proposalId
													+ '" title="Click to View" href = "'
													+ './ManageProposals.jsp?proposalID='
													+ proposalID
													+ '">'
													+ value.proposalTitle
													+ '</a><span class="activityon">'
													+ $.format
															.date(
																	value.activityDate,
																	'yyyy/MM/dd hh:mm:ss a')
													+ '</span><span class="'
													+ classForAction
													+ '"><strong>'
													+ value.action
													+ '</strong></span>'
													+ ' </li>';
											break;
										case 'Signature':
											userID = strEncrypt(value.userProfileId);
											userName = strEncrypt(value.username);
											proposalID = strEncrypt(value.proposalId);

											contentUser += '<li '
													+ (intNewUsers > 0 ? 'class="sfLastestNotification"'
															: '')
													+ '>'
													+ '<a id="'
													+ value.proposalId
													+ '" title="Click to View" href = "'
													+ './ManageProposals.jsp?proposalID='
													+ proposalID
													+ '">'
													+ value.proposalTitle
													+ '</a><span class="activityon">'
													+ $.format
															.date(
																	value.activityDate,
																	'yyyy/MM/dd hh:mm:ss a')
													+ '</span><span class="'
													+ classForAction
													+ '"><strong>'
													+ value.action
													+ '</strong></span>'
													+ ' </li>';
											break;
										default:
											break;
										}

										if (intNewUsers > 0) {
											intNewUsers--;
										}
									});
					contentUser += '</ul></div>';

					p.notificationsNumber -= parseInt(msg.length);
					// NotificationView.UpdateTitle();

				} else {
					contentUser += '<h5 class="cssClassNotifyHead">'
							+ 'There are no Notifications!' + '</h5>';
					contentUser += '</div>';
				}

				allContent += contentUser;

				$('.cssClassNotify').html(allContent);
			}
		};
		NotificationView.init();
	};
	// initialization of class
	$.fn.NotificationViewDetails = function(p) {
		$.NotificationViewList(p);
	};
	// outside plugin inject
	$.fn.outside = function(ename, cb) {
		return this.each(function() {
			var $this = $(this), self = this;

			$(document).bind(ename, function tempo(e) {
				if (e.target !== self && !$.contains(self, e.target)) {
					cb.apply(self, [ e ]);
					if (!self.parentNode)
						$(document.body).unbind(ename, tempo);
				}
			});
		});
	};
})(jQuery);

// Source: http://stackoverflow.com/questions/497790
var dates = {
	convert : function(d) {

		if (d != null) {
			return (d.constructor === Date ? d
					: d.constructor === Array ? new Date(d[0], d[1], d[2])
							: d.constructor === Number ? new Date(d)
									: d.constructor === String ? new Date(d)
											: typeof d === "object" ? new Date(
													d.year, d.month, d.date)
													: NaN);
		} else {
			return NaN;
		}
	},
	compare : function(a, b) {
		return (isFinite(a = this.convert(a).valueOf())
				&& isFinite(b = this.convert(b).valueOf()) ? (a > b) - (a < b)
				: NaN);
	},
	inRange : function(d, start, end) {
		return (isFinite(d = this.convert(d).valueOf())
				&& isFinite(start = this.convert(start).valueOf())
				&& isFinite(end = this.convert(end).valueOf()) ? start <= d
				&& d <= end : NaN);
	}
};