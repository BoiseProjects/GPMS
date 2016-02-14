<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" rel="stylesheet"
	href="css/Templates/uploadfile.css">
<link type="text/css" rel="stylesheet"
	href="css/Templates/jquery-ui.css" />
<script type="text/javascript" src="js/jQuery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="js/jQuery/jquery-ui.js"></script>

<script type="text/javascript" src="js/Uploader/jquery.uploadfile.js"></script>

<title>Insert title here</title>
</head>
<body>
	<form>
		<div id="fileuploader">Upload</div>
		<div class="ajax-file-upload-green" id="extrabutton">Start
			Upload</div>
		<div class="ajax-file-upload-red" id="resetbutton">Reset</div>
	</form>
	<script>
		$(document)
				.ready(
						function() {
							var appendices = [ {
								"filename" : "one.pdf",
								"extension" : "pdf",
								"filepath" : "uploads\\one.pdf",
								"filesize" : "82393"
							}, {
								"filename" : "two.jpg",
								"extension" : "jpg",
								"filepath" : "uploads\two.jpg",
								"filesize" : "82393"
							} ];
							$("#fileuploader")
									.uploadFile(
											{
												url : "REST/files/multiupload",
												multiple : true,
												dragDrop : true,
												fileName : "myfile",
												allowDuplicates : false,
												duplicateStrict : true,
												// autoSubmit : true,
												// sequential : true,
												// sequentialCount : 1,
												// autoSubmit : false,
												// formData : {
												// "name" : "Milson",
												// "age" : 29
												// },
												// acceptFiles : "image/*",
												maxFileCount : 5,
												// maxFileSize : 5*100 * 1024, //5MB
												returnType : "json",
												showDelete : true,
												confirmDelete : true,
												showDownload : true,
												statusBarWidth : 600,
												dragdropWidth : 600,
												nestedForms : false,

												onLoad : function(obj) {
													$
															.each(
																	appendices,
																	function(
																			index,
																			value) {
																		alert(value.filename);
																		obj
																				.createProgress(
																						value.filename,
																						value.filepath,
																						value.filesize);

																	});
												},
												deleteCallback : function(data,
														pd) {
													$
															.post(
																	"REST/files/delete",
																	{
																		op : "delete",
																		name : data
																	},
																	function(
																			resp,
																			textStatus,
																			jqXHR) {
																		// Show Message
																		alert("File Deleted");
																	});
													pd.statusbar.hide(); // You choice.

												},
												downloadCallback : function(
														filename, pd) {
													// location.href =
													// GPMS.utils.GetGPMSServicePath()
													// + "download.php?fileName="
													// + filename;
													window.location.href = 'REST/files/download?fileName='
															+ filename;
												}
											});

							$("#resetbutton").click(function() {
								uploadObj.reset();
								//uploadObj.remove();
							});
						});
	</script>
</body>
</html>