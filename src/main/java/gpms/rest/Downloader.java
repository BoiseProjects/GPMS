package gpms.rest;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Downloader
 */
@WebServlet(name = "Downloader", urlPatterns = { "/Downloader" })
public class Downloader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Downloader() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// try {
		//
		// URL templateFileUrl = ExcellaTestResource.class
		// .getResource("myTemplate.xls");
		// //
		// /C:/Users/m-hugohugo/Documents/NetBeansProjects/KogaAlpha/build/web/WEB-INF/classes/local/test/jaxrs/myTemplate.xls
		// System.out.println(templateFileUrl.getPath());
		// String templateFilePath = URLDecoder.decode(
		// templateFileUrl.getPath(), "UTF-8");
		// String outputFileDir = "MasatoExcelHorizontalOutput";
		//
		// ReportProcessor reportProcessor = new ReportProcessor();
		// ReportBook outputBook = new ReportBook(templateFilePath,
		// outputFileDir, ExcelExporter.FORMAT_TYPE);
		//
		// ReportSheet outputSheet = new ReportSheet("MySheet");
		// outputBook.addReportSheet(outputSheet);
		//
		// reportProcessor.addReportBookExporter(new OutputStreamExporter(
		// response));
		// System.out.println("wtf???");
		// reportProcessor.process(outputBook);
		//
		// System.out.println("done!!");
		// } catch (Exception e) {
		// System.out.println(e);
		// }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
