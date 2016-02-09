<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><html>
    <body>
        <h1>File Upload Example - howtodoinjava.com</h1>
     
        <form action="REST/upload/pdf" method="post" enctype="multipart/form-data">
     
            <p>Select a file : <input type="file" name="file" /></p>
            <input type="submit" value="Upload PDF" />
             
        </form>
     
    </body>
</html>