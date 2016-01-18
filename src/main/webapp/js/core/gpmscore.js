//<![CDATA[

var GPMS = {};
$(function() {
	GPMS = {
		config : {
			isPostBack : false,
			async : false,
			cache : false,
			type : 'POST',
			contentType : "application/json; charset=utf-8",
			data : '{}',
			dataType : 'json',
			baseURL : gpmsServicePath,
			method : "",
			url : "",
			ajaxCallMode : 0

		},

		vars : {
			IsAlive : true
		},

		ajaxCall : function(config) {
			$.ajax({
				type : GPMS.config.type,
				contentType : GPMS.config.contentType,
				cache : GPMS.config.cache,
				async : GPMS.config.async,
				url : GPMS.config.url,
				data : GPMS.config.data,
				dataType : GPMS.config.dataType,
				success : GPMS.ajaxSuccess,
				error : GPMS.ajaxFailure
			});
		},

		utils : {
			GetUserName : function() {
				return gpmsUserName;
			},
			GetUserProfileID : function() {
				return userProfileId;
			},
			IsAdmin : function() {
				return isAdmin;
			},
			GetUserPositionType : function() {
				return userPositionType;
			},
			GetUserPositionTitle : function() {
				return userPositionTitle;
			},
			GetUserDepartment : function() {
				return userDepartment;
			},
			GetUserCollege : function() {
				return userCollege;
			},

			GetCultureName : function() {
				return gpmsCurrentCulture;
			},

			GetSessionCode : function() {
				return sessionCode;
			},
			GetClientIP : function() {
				return clientIPAddress;
			},
			GetGPMSClientCountry : function() {
				return gpmsCountryName;
			},
			GetGPMSServicePath : function() {
				return gpmsServicePath;
			},
			GetGPMSRootPath : function() {
				return gpmsRootPath;
			}
		},
		GPMSCommonObj : function() {
			var gpmsCommonInfo = {
				UserName : GPMS.utils.GetUserName(),
				UserProfileID : GPMS.utils.GetUserProfileID(),
				UserIsAdmin : GPMS.utils.IsAdmin(),
				UserPositionType : GPMS.utils.GetUserPositionType(),
				UserPositionTitle : GPMS.utils.GetUserPositionTitle(),
				UserDepartment : GPMS.utils.GetUserDepartment(),
				UserCollege : GPMS.utils.GetUserCollege(),
				CultureName : GPMS.utils.GetCultureName(),
				SessionCode : GPMS.utils.GetSessionCode()
			};
			return gpmsCommonInfo;
		},
		CheckSessionActive : function(gpmsCommonObj) {
			GPMS.config.url = GPMS.config.baseURL
					+ "AspxCommonHandler.ashx/CheckSessionActive";
			GPMS.config.data = JSON2.stringify({
				gpmsCommonObj : gpmsCommonObj
			});
			GPMS.config.ajaxCallMode = 1;
			GPMS.ajaxCall(GPMS.config);
		},
		GetUrlVars : function() {
			var vars = {};
			var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
					function(m, key, value) {
						vars[key] = value;
					});
			return vars;
		},
		RootFunction : {
			Login : function(returnUrl) {
				returnUrl = window.location.href;
				window.location.href = GPMS.utils.GetGPMSRootPath() + logInURL
						+ pageExtension + '?' + "ReturnUrl=" + returnUrl;
				return false;
			},
			// You can call ajax call to db here too using REST service methods
			RedirectToOtherPage : function(pagename) {
				window.location.href = GPMS.utils.GetGPMSRootPath() + 'item/'
						+ pagename + pageExtension;
				return false;
			},
			AddToWishListFromJS : function(itemID, ip, countryName,
					costVariantValueIDs) {
				var gpmsCommonInfo = GPMS.gpmsCommonObj();
				delete gpmsCommonInfo.CultureName;
				delete gpmsCommonInfo.SessionCode;
				delete gpmsCommonInfo.CutomerID;
				var saveWishList = {
					ItemID : itemID,
					CostVariantValueIDs : costVariantValueIDs,
					IP : ip,
					CountryName : countryName
				};
				var addparam = {
					saveWishListInfo : saveWishList,
					gpmsCommonObj : gpmsCommonInfo
				};
				var adddata = JSON2.stringify(addparam);
				$
						.ajax({
							type : "POST",
							url : GPMS.utils.GetGPMSServicePath()
									+ 'SaveWishItems',
							data : adddata,
							contentType : "application/json; charset=utf-8",
							dataType : "json",
							success : function(msg) {
								// MyWishList();
								if ($('#lnkMyWishlist').length > 0) {
									GPMS.RootFunction.IncreaseWishListCount(); // for
									// header
									// counter
									// increase
									// HeaderControl.GetWishListCount(); // for
									// header wish counter increase for database
								}
								if ($('#divRecentlyAddedWishList').length > 0) {
									WishItems.BindMyWishList(); // for wishlist
									// item in
									// rightside
								}
								csscody
										.info('<h2>'
												+ 'Successful Message'
												+ '</h2><p>'
												+ 'Item has been successfully added to wishlist.'
												+ '</p>');
							},
							error : function(msg) {
								csscody.error('<h2>' + 'Error Message'
										+ '</h2><p>'
										+ 'Failed to add item in wishlist!'
										+ '</p>');
							}
						});
			}
		},
		IncreaseWishListCount : function() {
			var wishListCount = $('#lnkMyWishlist span').html().replace(
					/[^0-9]/gi, '');
			wishListCount = parseInt(wishListCount) + 1;
			$('.cssClassLoginStatusInfo ul li a#lnkMyWishlist span').html(
					" [" + wishListCount + "]");
		},
		ajaxSuccess : function(data) {
			switch (GPMS.config.ajaxCallMode) {
			case 0:
				break;
			case 1:
				GPMS.vars.IsAlive = data.d;
				break;
			default:
				break;
			}
		},
		init : function() {

			// $('body').append('<div id="ajaxBusy"><div id="dialog"
			// style="background-color:#AAAAAA;
			// position:absolute;left:50%;top:50%;display:none;z-index:9999;"
			// >Please Wait...<br /><img src="' + GPMS.utils.GetGPMSRootPath() +
			// 'Templates/Default/images/progress_bar.gif" alt=""
			// title="Loading"/></div><div id="mask" style="
			// position:absolute;left:0;top:0;z-index:9000;background-color:#000;display:none;"></div></div>');

		}
	};
	GPMS.init();

});

// ]]>

function fixedEncodeURIComponent(str) {
	// return encodeURIComponent(str).replace( /!/g , '%21').replace( /'/g ,
	// '%27').replace( /\(/g , '%28').replace( /\)/g , '%29').replace( /-/g ,
	// '_').replace( /\*/g , '%2A').replace( /%26/g , 'ampersand').replace(
	// /%20/g , '-');

	var Results = encodeURIComponent(str);
	// Results = Results.Replace("%", "%25");
	Results = Results.replace("!", "%21");
	Results = Results.replace("'", "%27");
	Results = Results.replace("(", "%28");
	Results = Results.replace(")", "%29");
	Results = Results.replace("*", "%2A");
	Results = Results.replace("<", "%3C");
	Results = Results.replace(">", "%3E");
	Results = Results.replace("#", "%23");
	Results = Results.replace("{", "%7B");
	Results = Results.replace("}", "%7D");
	Results = Results.replace("|", "%7C");
	Results = Results.replace("\"", "%5C");
	Results = Results.replace("^", "%5E");
	Results = Results.replace("~", "%7E");
	Results = Results.replace("[", "%5B");
	Results = Results.replace("]", "%5D");
	Results = Results.replace("`", "%60");
	Results = Results.replace(";", "%3B");
	Results = Results.replace("/", "%2F");
	Results = Results.replace("?", "%3F");
	Results = Results.replace(":", "%3A");
	Results = Results.replace("@", "%40");
	Results = Results.replace("=", "%3D");
	Results = Results.replace("&", "%26");
	Results = Results.replace("%26", "ampersand");
	Results = Results.replace("$", "%24");
	Results = Results.replace(" ", "%20");
	return Results;
}

function fixedDecodeURIComponent(str) {
	var Results = str;
	// Results = Results.Replace("%25","%");
	Results = Results.replace("%21", "!");
	Results = Results.replace("%27", "'");
	Results = Results.replace("%28", "(");
	Results = Results.replace("%29", ")");
	Results = Results.replace("%2A", "*");
	Results = Results.replace("%3C", "<");
	Results = Results.replace("%3E", ">");
	Results = Results.replace("%23", "#");
	Results = Results.replace("%7B", "{");
	Results = Results.replace("%7D", "}");
	Results = Results.replace("%7C", "|");
	Results = Results.replace("%5C", "\"");
	Results = Results.replace("%5E", "^");
	Results = Results.replace("%7E", "~");
	Results = Results.replace("%5B", "[");
	Results = Results.replace("%5D", "]");
	Results = Results.replace("%60", "`");
	Results = Results.replace("%3B", ";");
	Results = Results.replace("%2F", "/");
	Results = Results.replace("%3F", "?");
	Results = Results.replace("%3A", ":");
	Results = Results.replace("%40", "@");
	Results = Results.replace("%3D", "=");
	Results = Results.replace("ampersand", "%26");
	Results = Results.replace("%26", "&");
	Results = Results.replace("%24", "$");
	Results = Results.replace("%20", " ");
	return Results;
}

function GetSystemLocale(text) {
	return SystemLocale[$.trim(text)] == undefined ? text : SystemLocale[$
			.trim(text)];
}

function getLocale(moduleKey, text) {
	return moduleKey[$.trim(text)] == undefined ? text
			: moduleKey[$.trim(text)];
}
$.fn.SystemLocalize = function() {

	return this.each(function() {

		var t = $(this);
		if (t.is("input:button")) {
			var text = t.attr("value");
			var localeValue = SystemLocale[$.trim(text)];
			t.attr("value", localeValue);
		} else {
			t.html(SystemLocale[$.trim(t.text())] == undefined ? $.trim(t
					.text()) : SystemLocale[$.trim(t.text())]);
		}
	});
}

$.fn.localize = function(p) {
	return this.each(function() {
		var t = $(this);
		t.html(p.moduleKey[t.html().replace(/^\s+|\s+$/g, "")] == undefined ? t
				.html().replace(/^\s+|\s+$/g, "") : p.moduleKey[t.html()
				.replace(/^\s+|\s+$/g, "")]);
		if (t.is("input[type='button']")) {
			t.val(p.moduleKey[t.attr("value")] == undefined ? t.attr("value")
					: p.moduleKey[t.attr("value")]);
		}
	});
}

function getParameterByName(name) {
	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex
			.exec(location.search);
	return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g,
			" "));
}

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
/* Base64 class: Base 64 encoding / decoding (c) Chris Veness 2002-2011 */
/* note: depends on Utf8 class */
/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

var Base64 = {}; // Base64 namespace

Base64.code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

/**
 * Encode string into Base64, as defined by RFC 4648
 * [http://tools.ietf.org/html/rfc4648] (instance method extending String
 * object). As per RFC 4648, no newlines are added.
 * 
 * @param {String}
 *            str The string to be encoded as base-64
 * @param {Boolean}
 *            [utf8encode=false] Flag to indicate whether str is Unicode string
 *            to be encoded to UTF8 before conversion to base64; otherwise
 *            string is assumed to be 8-bit characters
 * @returns {String} Base64-encoded string
 */
Base64.encode = function(str, utf8encode) { // http://tools.ietf.org/html/rfc4648
	utf8encode = (typeof utf8encode == 'undefined') ? false : utf8encode;
	var o1, o2, o3, bits, h1, h2, h3, h4, e = [], pad = '', c, plain, coded;
	var b64 = Base64.code;

	plain = utf8encode ? Utf8.encode(str) : str;

	c = plain.length % 3; // pad string to length of multiple of 3
	if (c > 0) {
		while (c++ < 3) {
			pad += '=';
			plain += '\0';
		}
	}
	// note: doing padding here saves us doing special-case packing for trailing
	// 1 or 2 chars

	for (c = 0; c < plain.length; c += 3) { // pack three octets into four
		// hexets
		o1 = plain.charCodeAt(c);
		o2 = plain.charCodeAt(c + 1);
		o3 = plain.charCodeAt(c + 2);

		bits = o1 << 16 | o2 << 8 | o3;

		h1 = bits >> 18 & 0x3f;
		h2 = bits >> 12 & 0x3f;
		h3 = bits >> 6 & 0x3f;
		h4 = bits & 0x3f;

		// use hextets to index into code string
		e[c / 3] = b64.charAt(h1) + b64.charAt(h2) + b64.charAt(h3)
				+ b64.charAt(h4);
	}
	coded = e.join(''); // join() is far faster than repeated string
	// concatenation in IE

	// replace 'A's from padded nulls with '='s
	coded = coded.slice(0, coded.length - pad.length) + pad;

	return coded;
}

/**
 * Decode string from Base64, as defined by RFC 4648
 * [http://tools.ietf.org/html/rfc4648] (instance method extending String
 * object). As per RFC 4648, newlines are not catered for.
 * 
 * @param {String}
 *            str The string to be decoded from base-64
 * @param {Boolean}
 *            [utf8decode=false] Flag to indicate whether str is Unicode string
 *            to be decoded from UTF8 after conversion from base64
 * @returns {String} decoded string
 */
Base64.decode = function(str, utf8decode) {
	utf8decode = (typeof utf8decode == 'undefined') ? false : utf8decode;
	var o1, o2, o3, h1, h2, h3, h4, bits, d = [], plain, coded;
	var b64 = Base64.code;

	coded = utf8decode ? Utf8.decode(str) : str;

	for (var c = 0; c < coded.length; c += 4) { // unpack four hexets into three
		// octets
		h1 = b64.indexOf(coded.charAt(c));
		h2 = b64.indexOf(coded.charAt(c + 1));
		h3 = b64.indexOf(coded.charAt(c + 2));
		h4 = b64.indexOf(coded.charAt(c + 3));

		bits = h1 << 18 | h2 << 12 | h3 << 6 | h4;

		o1 = bits >>> 16 & 0xff;
		o2 = bits >>> 8 & 0xff;
		o3 = bits & 0xff;

		d[c / 4] = String.fromCharCode(o1, o2, o3);
		// check for padding
		if (h4 == 0x40)
			d[c / 4] = String.fromCharCode(o1, o2);
		if (h3 == 0x40)
			d[c / 4] = String.fromCharCode(o1);
	}
	plain = d.join(''); // join() is far faster than repeated string
	// concatenation in IE

	return utf8decode ? Utf8.decode(plain) : plain;
}

function startLoader() {
	if ($("#fade").length < 1) {
		var $divappend;

		if ($("#sfOuterwrapper").length > 0) {
			$divappend = $("#sfOuterwrapper");

			$divappend
					.prepend("<div id='gpmsloading'>"
							+ "<img id='loading' src='"
							+ GPMS.utils.GetGPMSRootPath()
							+ "images/gpms-loader.gif' title='GPMS loading' alt='GPMS loading'>"
							+ "</div><div id='fade'></div>");
			$("#fade").show();
		}
	}
}

function stopLoader() {
	$("#fade").remove();
	$("#gpmsloading").remove();
}

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

function strEncrypt(value) {
	if (value !== null) {
		return Base64.encode(value.toString().trim());
	} else {
		return '';
	}

}

function strDecrypt(value) {
	var result = Base64.decode(value);
	if (result.toString().split(',').length > 0) {
		var tempResult = result.toString().split(',');
		var strI = '';
		for (var i = 0; i < tempResult.length; i++) {
			strI += tempResult[i];
		}
		result = parseInt(strI);
	}
	return result;
}
Boolean.parse = function(b) {
	var a = b.trim().toLowerCase();
	if (a === "false")
		return false;
	if (a === "true")
		return true
}
stringToBoolean = function(b) {
	switch (b.toLowerCase()) {
	case "true":
	case "yes":
	case "1":
		return true;
	case "false":
	case "no":
	case "0":
	case null:
		return false;
	default:
		return Boolean(b);
	}
}

function removeDuplicates(inputArray) {
	var i;
	var len = inputArray.length;
	var outputArray = [];
	var temp = {};

	for (i = 0; i < len; i++) {
		temp[inputArray[i]] = 0;
	}
	for (i in temp) {
		outputArray.push(i);
	}
	return outputArray;
}