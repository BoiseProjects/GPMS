var DashBoard = {};
$(function() {
	DashBoard = {
		init : function() {
			$('body')
					.append(
							'<div id="ajaxBusy" style="display:none"><img align="absmiddle"  src="../../images/ajax-loader.gif">&nbsp;Working...</div>');
		}
	};
	DashBoard.init();

	// $(".confirm").easyconfirm();
	ResetSidebarHeight();
});

function ResetSidebarHeight() {
	var scrollpos = $(document).scrollTop();

	var masterheight = $("div.sfMaincontent").height();

	var docheight = $(window).height();
	docheight = docheight - $('div.sfTopwrapper').height()
			+ CalculateAdjustmentHeight();
	var incr = 0;
	if (masterheight > docheight) {
		incr = masterheight - docheight;
	}
	docheight = docheight + incr + 30 + "px";
	$('div.sfSidebar').css("height", docheight);
}

$(document).scroll(function() {
	ResetSidebarHeight();
});
$(document).click(function() {
	ResetSidebarHeight();
});

function CalculateAdjustmentHeight() {
	var calc_height = 10;
	var screen_res = screen.height;
	calc_height = screen_res * 0.28;
	return calc_height;
}

function InitModuleFloat(leftOffset) {

	if (location.href.toLowerCase().indexOf("page-modules") > -1) {

		var topOffset = $('#divDroppable').offset().top;
		var adj = $('#sfOuterwrapper').height()
				+ ($('#sfOuterwrapper').offset().top)
				- ($('#divFloat').height());
		$("#divFloat").makeFloat({
			x : leftOffset,
			y : 0,
			speed : "fast",
			adjustment : adj
		});
	}

}

// Ajax activity indicator bound to ajax start/stop document events
$('#ajaxBusy').show();
setTimeout("startAjaxBusy()", 1000);
$(document).ajaxStart(function() {
	$('#ajaxBusy').show();
}).ajaxStop(function() {
	$('#ajaxBusy').hide();
});
$(document).ajaxComplete(function() {
	$('#ajaxBusy').hide();
});
function startAjaxBusy() {
	$('#ajaxBusy').hide();
}
