<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<script type="text/javascript">
		//<![CDATA[    
		$(function() {
			SidebarMgr.init();
		});
		var SidebarMgr = {
				
			config : {
				isPostBack : false,
				async : true,
				cache : true,
				type : 'POST',
				contentType : "application/json; charset=utf-8",
				data : '{}',
				dataType : 'json',
				method : "",
				url : "",
				categoryList : "",
				ajaxCallMode : 0,
				arr : [],
				arrModules : [],
				baseURL : '/aspx'
						+ '/Modules/Dashboard/Services/DashboardWebService.asmx/',
				PortalID : 1,
				Path : '/aspx' + '/Modules/Dashboard/',
				SaveMode : "Add",
				SidebarItemID : 0,
				SidebarMode : '1',
				ShowSideBar : '1',
				UserName : 'superuser',
				PortalID : '1',
				ForceTrigger : ''
			},
			init : function() {
				if (SidebarMgr.config.SidebarMode == 0) {
					InitSuperfish();
					$('#divFooterWrapper').addClass('sfFooterCollapse');
				} else {
					$('#divFooterWrapper').removeClass('sfFooterCollapse');
					$('ul.menu').initMenu();
					SidebarMgr.HighlightSelected();
					if ($('.menu .Grandparent').hasClass('active')) {
						if ($('.menu .Grandparent .parent').hasClass('active')) {
							$('.Grandparent.active').find('a').eq(0).trigger(
									'click');
							$('.parent.active').find('a').eq(0)
									.trigger('click');
						} else {
							$('.Grandparent.active').find('a').eq(0).trigger(
									'click');
						}
					} else {
						$('.Grandparent').find('a').eq(0).trigger('click');
					}
				}
				if (SidebarMgr.config.ShowSideBar == "1")
					$('div.sfHidepanel').on(
							"click",
							function() {
								if (!$('div.sfSidebar').hasClass(
										"sfSidebarhide")) {
									//$('div.sfSidebar').find("ul li ul").hide(function() { $(this).animate({ display: "none" }, 100) });
									//$('div.sfSidebar').find("ul li a span").hide(function() { $(this).animate({ display: "none" }, 100) });
									//$('div.sfHidepanel').find("a span").hide(function() { $(this).animate({ display: "none" }, 100) });
									$('div.sfSidebar').animate({
										width : "45px"
									}, 400, function() {
										//                            $('div.sfHidepanel').find("img").animate({ opacity: 0 }, 100, function() {
										//                                $('div.sfHidepanel img').attr("src", SageFrame.utils.GetAdminImage("show-arrow.png"))
										//                                $('div.sfHidepanel img').animate({ opacity: 1 }, 100);
										//                            });
									});
									InitSuperfish();
									$('div.sfSidebar')
											.addClass("sfSidebarhide");
									InitModuleFloat(65);
									$('.Grandparent').find('a:eq(0)').find(
											'span:eq(0)').hide();
									SidebarMgr.UpdateSidebarMode();

								} else {
									$('div.sfSidebar').find("ul li ul").show(
											function() {
												$(this).animate({
													display : "block"
												}, 100)
											});
									$('div.sfSidebar').find("ul li a span")
											.show(function() {
												$(this).animate({
													display : "block"
												}, 100)
											});
									$('div.sfSidebar').animate({
										width : "210px"
									}, 400, function() {
										//                            $('div.sfHidepanel').find("a span").show(function() { $(this).animate({ display: "block" }, 100) });
										//                            $('div.sfHidepanel').find("img").attr("src", gpmsAppPath + "/Administrator/Templates/Default/images/hide-arrow.png");
									});
									InitAccordianMode();
									$('#sidebar ul').attr("class", "menu").css(
											"visibility", "visible");
									$('#sidebar ul li.Grandparent ul').attr(
											"class", "acitem fullwidth");
									$('#sidebar ul li a').removeAttr("class");
									$('div.sfSidebar').removeClass(
											"sfSidebarhide");
									InitModuleFloat(200);
									$('.Grandparent').find('a').eq(0).find(
											'span').eq(0).show();
									SidebarMgr.UpdateSidebarMode();

								}

								if ($('.sfHidepanel').find('i').hasClass(
										'sidebarExpand')) {
									$('.sfHidepanel').find('i').removeClass(
											'sidebarExpand').addClass(
											'sidebarCollapse');
									$('#divFooterWrapper').addClass(
											'sfFooterCollapse');
								} else {
									$('.sfHidepanel').find('i').removeClass(
											'sidebarCollapse').addClass(
											'sidebarExpand');
									$('#divFooterWrapper').removeClass(
											'sfFooterCollapse');
								}
							});
			},
			HighlightSelected : function() {
				var sidebar = $('#sidebar ul li');
				$.each(sidebar, function(index, item) {
					if ($(this).hasClass("parent")) {
						var submenu = $(this).find("ul li");
						$.each(submenu, function() {
							if ($(this).hasClass("parentchild")) {
								var subsubmenu = $(this).find("ul li");
								$.each(subsubmenu, function() {
									var hreflink = $(this).find("a").attr(
											"href");
									if (location.href.toLowerCase().indexOf(
											hreflink.toLowerCase()) > -1) {
										$(this).parent("ul.acitem").css(
												"display", "block").addClass(
												"active");
										$(this).parent("ul.acitem").prev("a")
												.addClass("active");
										$(this).parent("ul.acitem").parents(
												'li.parent').css("display",
												"block").addClass("active");
										$(this).parent("ul.acitem").parent(
												'ul.acitem').css("display",
												"block").addClass("active");
										$(this).parent("ul.acitem").parents(
												'li.Grandparent').css(
												"display", "block").addClass(
												"active");
									}
								});
							} else {
								var hreflink = $(this).find("a").attr("href");
								if (location.href.toLowerCase().indexOf(
										hreflink.toLowerCase()) > -1) {
									$(this).parent("ul.acitem").css("display",
											"block").addClass("active");
									$(this).parent("ul.acitem").prev("a")
											.addClass("active");
									$(this).parent("ul.acitem").parents(
											'li.Grandparent').css("display",
											"block").addClass("active");
									//$(this).parent("ul.acitem").parents('li.Grandparent').find("a").eq(0).addClass("active");
								}
							}
						});
					} else if (!$(this).hasClass("parent")) {
						var hreflink = $(this).find("a").attr("href");
						if (location.href.toLowerCase().indexOf(
								hreflink.toLowerCase()) > -1) {
							$(this).find("a").addClass('active');
							$(this).parent("ul.acitem").parents(
									'li.Grandparent').css("display", "block")
									.addClass("active");
						}
					}
				});
			},
			BuildURL : function(item) {
				var portalchange = SidebarMgr.config.PortalID > 1 ? "/portal/"
						+ 'default' : "";
				var url = '/aspx' + portalchange + item.URL
						+ SageFrameSettingKeys.PageExtension
				return url;
			},
			UpdateSidebarMode : function() {
				var _status = $('div.sfSidebar').hasClass("sfSidebarhide") ? "closed"
						: "open";
				var param = JSON2.stringify({
					status : _status,
					PortalID : SageFramePortalID,
					UserName : SageFrameUserName,
					secureToken : SageFrameSecureToken
				});
				$.ajax({
					type : SidebarMgr.config.type,
					contentType : SidebarMgr.config.contentType,
					cache : SidebarMgr.config.cache,
					url : SidebarMgr.config.baseURL + "UpdateSidebar",
					data : param,
					dataType : SidebarMgr.config.dataType,
					success : function(msg) {
					}
				});
			}
		};

		function InitCollapsedMode() {
			$('div.sfSidebar').find("ul li ul").hide(function() {
				$(this).animate({
					display : "none"
				}, 100)
			});
			$('div.sfHidepanel').find("a span").hide(function() {
				$(this).animate({
					display : "none"
				}, 100)
			});
			//$('div.sfSidebar').animate({ width: "45px" }, 400, function() {
			//            $('div.sfHidepanel').find("img").animate({ opacity: 0 }, 100, function() {
			//                $('div.sfHidepanel img').attr("src", GetAdminImage("show-arrow.png"))
			//                $('div.sfHidepanel img').animate({ opacity: 1 }, 100);
			//            });
			//});
			$('div.sfSidebar').addClass("sfSidebarhide");
			InitSuperfish();
		}

		function GetAdminImage(imagename) {
			return (gpmsAppPath + "/Administrator/Templates/Default/images/" + imagename);
		}

		function InitSuperfish() {
			$('ul.menu').addClass("sf-vertical");
			var ul = $('ul.menu ul.acitem');
			$.each(ul, function(index, item) {
				$(this).addClass("sfCollapsed").removeClass("fullwidth");
			});
			$('ul.menu').superfish({
				animation : {
					height : 'show'
				}, // slide-down effect without fade-in 
				delay : 100
			// 1.2 second delay on mouseout 
			});
		}

		function InitAccordianMode() {
			var ul = $('ul.menu ul.acitem');
			$.each(ul, function(index, item) {
				$(this).removeClass("sfCollapsed").addClass("fullwidth");
			});
			$('ul.menu').removeClass("sf-vertical");
			$('ul.menu').initMenu();
		}
		//]]>
	</script>

	<div style="float: left; height: 877px;" id="sidebar" class="sfSidebar">
		<ul class="menu">
			<li class="Grandparent sfLevel0 active" style="display: block;"><a
				href="#"><i class="icon-portal-management"></i><span>User
						Control Panel</span></a>
				<ul style="display: none" class="acitem">
					<li class="parent sfLevel1 active"><a href="#"><i
							class="icon-create-template"></i><span>Proposals</span></a></li>
					<li class="parent sfLevel1"><a href="#"><i
							class="icon-text"></i><span>Notifications</span></a>
						</li>
					<li class="parent sfLevel1"><a href="./UserAccountSettings.jsp"><i
							class="icon-user-setting"></i><span>Account Settings</span></a>
						</li>
				</ul></li>
		</ul>
		<div class="sfHidepanel clearfix">
			<a href="#"><i class="sidebarExpand"></i><span></span></a>
		</div>
	</div>

	<div class="sfFooterwrapper clearfix" id="divFooterWrapper">
		<div id="ctl_CPanleFooter1_divFooterContent">Copyright 2015
			GPMS. All Rights Reserved&reg;</div>

	</div>
</body>
</html>