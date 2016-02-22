(function($) {

	$.disableWith = {
		initialize : function() {
			this.getElements().click(this._disable);
		},

		getElements : function() {
			return $('input[data-disable-with]');
		},

		_disable : function() {
			var element = $(this);
			var message = element.data('disable-with');
			$(this).disableWith(message);
		}
	};

	$.fn.disableWith = function(message) {
		var element = $(this);

		element.data('enable-with', element.children('span').text());
		element.prop('disabled', true);
		// element.val(message);
		element.children('span').text(message);

		return element;
	};

	$.fn.enableAgain = function() {
		var element = $(this);
		var enableWith = element.data('enable-with');

		element.prop('disabled', false);

		if (enableWith) {
			element.children('span').text(enableWith);
		}

		return element;
	};

	$(function() {
		$.disableWith.initialize();
	});

}(this.jQuery));
