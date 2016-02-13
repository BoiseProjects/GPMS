<html>
<head>
<title>Image Upload</title>
<meta charset="UTF-8">
<script script="text/javascript">
	var BASE_URL = "http://localhost:8181/GPMS/REST/img";

	onload = function() {
		document.getElementById("submit").onclick = uploadFile;
	};

	function uploadFile() {

		document.getElementById("status").innerHTML = "making http call ... ";

		var file = document.getElementById("filechooser").files[0];
		var extension = file.name.split(".").pop();

		var type;
		if (extension === "jpg" || extension === "jpeg" || extension === "JPG"
				|| extension === "JPEG") {
			type = "image/jpeg";
		} else if (extension === "png" || extension === "PNG") {
			type = "image/png";
		} else {
			document.getElementById("status").innerHTML = "Invalid file type";
			return;
		}

		try {
			var request = new XMLHttpRequest();
			request.open("POST", BASE_URL, true);
			request.onload = function() {
				if (request.status === 201) {
					var fileName = request.getResponseHeader("Location").split(
							"/").pop();
					document.getElementById("status").innerHTML = "File created with name "
							+ fileName;
					alert('Loaded');
				} else {
					document.getElementById("status").innerHTML = "Error creating file: ("
							+ request.status + ") " + request.responseText;
					alert('Error');
				}
			};

			request.setRequestHeader("Content-Type", type);
			request.send(file);
		} catch (err) {
			alert(err);
			document.getElementById("status").innerHTML += "\nXMLHttprequest error: "
					+ err.description;
		}
	}
</script>
</head>
<body>
	<form>
		<p>Upload image through a web service</p>
		<input type="file" id="filechooser">
		<button type="button" id="submit">Upload</button>
	</form>
	<p>Status:</p>
	<p id="status"></p>
</body>
</html>