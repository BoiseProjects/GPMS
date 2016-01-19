﻿(function($) {
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
							+ 'Failed to load Proposal Status.' + '</p>');
					break;
				}
			},
			init : function() {
				NotificationView.NotificationGetAllCount();
				$("#linkUsersInfo").click(function() {
					if (!$(".cssClassNotify").is(":visible")) {
						$(this).addClass("sfNotificationSelect");
						NotificationView.NotificationGetAll();
					} else {
						$(this).removeClass("sfNotificationSelect");
					}
				});

				$(".notificationsSticker").outside(
						'click',
						function() {
							$('.cssClassNotify').stop(true, true).slideUp(
									'slow');
							$('.sfNotificationSelect').removeClass(
									"sfNotificationSelect");

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

				// Default events

				$(".topopup")
						.click(
								function() {

									$(".cssClassNotify")
											.each(
													function() {
														if ($(this).is(
																':visible')) {
															$parentList = $(
																	this)
																	.parent(
																			"li")
																	.siblings()
																	.find(
																			'a :first');
															$parentList
																	.removeClass("sfNotificationSelect");
															$(this).slideUp();
														}
													});

									var $t = $(this).parent().find(
											".cssClassNotify");

									if ($t.is(':visible')) {
										$parentList = $(this).parent("li")
												.siblings().find('a :first');
										$parentList
												.removeClass("sfNotificationSelect");
										$t.slideUp();
									} else {
										$parentList = $(this).parent("li")
												.siblings().find('a :first');
										$parentList
												.removeClass("sfNotificationSelect");
										$(this)
												.addClass(
														"sfNotificationSelect");
										$t.slideDown();
									}
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
				if (msg.UsersInfoCount !== 0) {
					$("#spanUsersInfo").html(msg.UsersInfoCount);
					$("#spanUsersInfo").show();
					p.notificationsNumber += parseInt(msg.UsersInfoCount);
				} else {
					$("#spanUsersInfo").hide();
				}
				if (msg.ItemsInfoCount !== 0) {
					$("#spanItemsInfo").html(msg.ItemsInfoCount);
					$("#spanItemsInfo").show();
					p.notificationsNumber += parseInt(msg.ItemsInfoCount);
				} else {
					$("#spanItemsInfo").hide();
				}
				if (msg.NewOrdersCount !== 0) {
					$("#spanOrdersInfo").html(msg.NewOrdersCount);
					$("#spanOrdersInfo").show();
					p.notificationsNumber += parseInt(msg.NewOrdersCount);
				} else {
					$("#spanOrdersInfo").hide();
				}
				NotificationView.UpdateTitle();
			},
			NotificationGetAll : function() {
				this.config.method = "NotificationGetAll";
				this.config.url = this.config.baseURL + this.config.method;
				this.config.data = JSON2.stringify({
					gpmsCommonObj : gpmsCommonInfo
				});
				this.config.ajaxCallMode = 2;
				this.ajaxCall(this.config);
				$("#spanUsersInfo").hide();
				return false;
			},
			NotificationGetAllSuccess : function(msg) {
				var contentUser = "";
				var contentSubscription = "";
				var userData = "";

				contentUser = '<div>';
				if (msg.length > 0) {
					contentUser += '<h5 class="cssClassNotifyHead">'
							+ 'Recently Registered and Subscribed Users:'
							+ '</h5><ul>';
					var i = 1;

					var userName = "";
					var customerID = "";

					var intNewUsers = parseInt($('#spanUsersInfo').text());

					$
							.each(
									msg,
									function(index, value) {
										userName = strEncrypt(value.UserName);
										customerID = strEncrypt(value.CustomerID);
										if (value.SubscriptionEmail != '') {
											contentUser += '<li '
													+ (intNewUsers > 0 ? 'class="sfLastestNotification"'
															: '')
													+ '>'
													+ (value.UserName != '' ? "<a class='subsribedName'>"
															+ value.UserName
															+ "</a>"
															: '')
													+ (value.SubscriptionEmail != "INSUFFICIENT_PARAMS" ? "<span class='subsribedEmail'>"
															+ value.SubscriptionEmail
															+ "</span>"
															: "")
													+ "<span class='status subscribed'><strong>"
													+ 'subscribed on:'
													+ "</strong>"
													+ value.AddedOn
													+ "</span></li>";
										} else {
											contentUser += '<li '
													+ (intNewUsers > 0 ? 'class="sfLastestNotification"'
															: '')
													+ '>'
													+ '<a id="'
													+ value.UserName
													+ '" class="registeredName" title="Click to View User Profile" href = "'
													+ aspxRedirectPath
													+ 'Admin/AspxCommerce/Customers/Manage-Customers'
													+ pageExtension
													+ "?customerID="
													+ customerID
													+ "&userName="
													+ userName
													+ '"> '
													+ value.UserName
													+ ' </a><span class="status registered"><strong>'
													+ 'registered on:'
													+ '</strong>'
													+ value.AddedOn + '</span>'
													+ ' </li>';
										}

										if (intNewUsers > 0) {
											intNewUsers--;
										}
									});
					contentUser += '</ul></div>';

					p.notificationsNumber -= parseInt(msg.length);
					NotificationView.UpdateTitle();

				} else {
					contentUser += '<h5 class="cssClassNotifyHead">'
							+ 'There are no Recently Registered or Subscribed Users:'
							+ '</h5>';
					contentUser += '</div>';
				}

				userData += '<div class="cssClassNotify" style="display:none">'
						+ contentUser + '</div>';

				$('.sfqckUserInfo').append(userData);
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