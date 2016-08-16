<html>
<head>
<title>Index Page</title>
<script src="js/jQuery/jquery-1.11.3.min.js" type="text/javascript"></script>

<script type="text/javascript">
	//Define the checkboxes dependencies
	var dependencies = {
		"#item1" : [ "#item1" ],
		"#item2" : [ "#item1", "#item3" ],
		"#item3" : [ "#item1", "#item3" ],
		"#item4" : [ "#item1", "#item3" ],
		"#item5" : [ "#item3" ]
	};

	var dependenciesName = {
		"item1" : [ "item2", "item3" ],
		"item2" : [ "item1" ],
		"item3" : [ "item4" ]
	};

	$(function() {
		$("input:checkbox").change(function() {
			var prop = ($(this).is(':checked')) ? true : false;

			var value = $(this).val();

			$.grep(Object.keys(dependenciesName), function(k) {
				if (k === value) {
					$.each(dependenciesName[k], function(i, e) {
						$('#' + e).prop('checked', prop);

					});
				}

			});

			// 			jQuery.grep(dependenciesName, function(obj) {
			// 				console.log(obj.item1);
			// 			});

			// 			$.each(dependenciesName, function(k, v) {
			// 				$.each(v, function(i, e) {
			// 					// If any dependency is checked, then the current item needs to be red
			// 					$('#' + e).prop('checked', prop);

			// 				});
			// 			});
		});

		// 		jQuery.grep(dependenciesName, function(obj) {
		// 			console.log(obj);
		// 			return obj === "item1";
		// 		});

		// 		$('#item1, #item3').on('click', function(e) {
		// 			var prop = ($(this).is(':checked')) ? true : false;

		// 			$('#item1, #item3').prop('checked', prop);
		// 		});
		// 		// When any checkbox is clicked
		// 		$("input:checkbox").change(function() {
		// 			// Go through all dependencies
		// 			$.each(dependencies, function(k, v) {
		// 				var colour = "green";
		// 				$.each(v, function(i, e) {
		// 					// If any dependency is checked, then the current item needs to be red
		// 					if ($(e + ":checked").length) {
		// 						colour = "red";
		// 					}
		// 				});

		// 				// Get label corresponding to current item
		// 				var label = $("label[for='" + $(k).attr("id") + "']");
		// 				// And give it the correct class
		// 				label.removeClass("green").removeClass("red").addClass(colour);
		// 			});
		// 		});
	});
</script>
</head>
<body>
	<h2>Jersey RESTful Web Application!</h2>
	Visit
	<a href="http://jersey.java.net">Project Jersey website</a> for more
	information on Jersey!
	<input type="checkbox" id="item1" value="item1" />
	<label for="item1" class="green">item1</label>
	<br />
	<input type="checkbox" id="item2" value="item2" />
	<label for="item2" class="green">item2</label>
	<br />
	<input type="checkbox" id="item3" value="item3" />
	<label for="item3" class="green">item3</label>
	<br />
	<input type="checkbox" id="item4" value="item4" />
	<label for="item4" class="green">item4</label>
	<br />
	<input type="checkbox" id="item5" value="item5" />
	<label for="item5" class="green">item5</label>
	<br />
</body>
</html>