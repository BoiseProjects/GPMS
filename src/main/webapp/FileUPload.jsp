<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta content="text/html;charset=utf-8" http-equiv="Content-Type" />
<meta content="utf-8" http-equiv="encoding" />

<title>Log in - GPMS</title>
<script src="js/jQuery/jquery-1.11.3.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$('#btnDownload').on("click", function(event) {
							var name = $('#txtFileName').val();
							$.get('REST/file/download', {
								fileName : name
							}, function(responseText) {
								//$('#ajaxGetUserServletResponse').text(responseText);
								alert(responseText);
							});
						});

						$('#btnUpload')
								.on(
										"click",
										function(event) {
											$('input[name="file"]')
													.each(
															function(index,
																	value) {
																var file = value.files[0];
																if (file) {
																	var formData = new FormData();
																	formData
																			.append(
																					'file',
																					file);
																	$
																			.ajax({
																				url : 'REST/files/upload',
																				type : 'POST',
																				data : formData,
																				cache : false,
																				contentType : false,
																				processData : false,
																				success : function(
																						data,
																						textStatus,
																						jqXHR) {
																					var message = jqXHR.responseText;
																					$(
																							"#messages")
																							.append(
																									"<li>"
																											+ message
																											+ "</li>");
																				},
																				error : function(
																						jqXHR,
																						textStatus,
																						errorThrown) {
																					$(
																							"#messages")
																							.append(
																									"<li style='color: red;'>"
																											+ textStatus
																											+ "</li>");
																				}
																			});
																}
															});
										});
					});
</script>
</head>
<body>
	<h1>File Upload Example - howtodoinjava.com</h1>

	<form action="REST/files/upload" method="post"
		enctype="multipart/form-data">
		<p>
			Select file 1: <input type="file" name="file" size="45" accept=".pdf" />
		</p>
		<p>
			Select file 2: <input type="file" name="file" size="45" accept=".pdf" />
		</p>
		<p>
			Select file 3: <input type="file" name="file" size="45" accept=".pdf" />
		</p>
		<p>
			<input id="btnUpload" type="button" value="Upload Files" />
		<ul id="messages">
		</ul>
		</p>
		
	</form>

</body>
</html>