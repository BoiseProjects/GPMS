<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link
	href="https://rawgithub.com/hayageek/jquery-upload-file/master/css/uploadfile.css"
	rel="stylesheet">
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script
	src="https://rawgithub.com/hayageek/jquery-upload-file/master/js/jquery.uploadfile.min.js"></script>

<title>Insert title here</title>
</head>
<body>
	<div id="fileuploader">Upload</div>
	<script>
		$(document)
				.ready(
						function() {
							$("#fileuploader")
									.uploadFile(
											{
												url : "REST/files/multiupload",
												multiple : true,
												dragDrop : true,
												fileName : "myfile",
												// 	formData : {
												// 	"name" : "Milson",
												// 	"age" : 29
												// 	},
												//acceptFiles : "image/*",
												maxFileCount : 5,
												//maxFileSize : 100 * 1024,
												returnType : "json",
												showDelete : true,
												showDownload : true,
												statusBarWidth : 600,
												dragdropWidth : 600,
												onLoad : function(obj) {
													$
															.ajax({
																cache : false,
																url : "REST/proposals/GetAttachmentsOfAProposal",
																dataType : "json",
																success : function(
																		data) {
																	for (var i = 0; i < data.length; i++) {
																		obj
																				.createProgress(
																						data[i]["name"],
																						data[i]["path"],
																						data[i]["size"]);
																	}
																}
															});
												},
												deleteCallback : function(data,
														pd) {
													for (var i = 0; i < data.length; i++) {
														$
																.post(
																		"delete.php",
																		{
																			op : "delete",
																			name : data[i]
																		},
																		function(
																				resp,
																				textStatus,
																				jqXHR) {
																			//Show Message	
																			alert("File Deleted");
																		});
													}
													pd.statusbar.hide(); //You choice.

												},
												downloadCallback : function(
														filename, pd) {
													//location.href = "download.php?filename=" + filename;
													window.location.href = 'REST/files/download?fileName='
															+ filename;
												}
											});
						});
	</script>
</body>
</html>