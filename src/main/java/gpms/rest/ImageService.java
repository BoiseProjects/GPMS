package gpms.rest;

/*                                                                                                                                                                                      
 * Licensed under Apache Software Foundation V2 license agreement.  
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership.  The ASF licenses this 
 * file to you under the Apache License, Version 2.0 (the                                                                                                                                    
 * "License"); you may not use this file except in compliance                                                                                                                          
 * with the License.  You may obtain a copy of the License at                                                                                                                          
 *                                                                                                                                                                                      
 *  http://www.apache.org/licenses/LICENSE-2.0                                                                                                                                         
 *                                                                                                                                                                                      
 * Unless required by applicable law or agreed to in writing,                                                                                                                          
 * software distributed under the License is distributed on an                                                                                                                          
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY                                                                                                                              
 * KIND, either express or implied.  See the License for the                                                                                                                            
 * specific language governing permissions and limitations                                                                                                                              
 * under the License.
 *
 *
 * Copyright(c) 2012-2014, Vijay Dialani. ALL RIGHTS RESERVED.
 *                                                                                                                                                                 
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;

@Path("img")
public class ImageService {
	private static final int MAX_SIZE_IN_MB = 16;

	@GET
	@Produces("image/jpeg")
	public Response getImage() {

		InputStream imageStream = ImageService.class
				.getResourceAsStream("/teapot.jpg");
		if (imageStream == null) {
			return Response.serverError().build();
		}
		final byte[] image;
		try {
			image = IOUtils.toByteArray(imageStream);
		} catch (IOException e) {
			return Response.serverError().build();
		}

		return Response.ok().entity(new StreamingOutput() {
			public void write(OutputStream output) throws IOException,
					WebApplicationException {
				output.write(image);
				output.flush();
			}
		}).build();
	}

	@GET
	@Path("upload")
	@Produces("text/html")
	public Response uploadPage() {
		InputStream pageStream = ImageService.class
				.getResourceAsStream("/upload.html");
		if (pageStream == null) {
			return Response.serverError().build();
		}
		final byte[] page;
		try {
			page = IOUtils.toByteArray(pageStream);
		} catch (IOException e) {
			return Response.serverError().build();
		}

		return Response.ok().entity(new StreamingOutput() {
			public void write(OutputStream output) throws IOException,
					WebApplicationException {

				output.write(page);
				output.flush();
			}
		}).build();
	}

	@GET
	@Path("{name}")
	// http://localhost:8181/GPMS/REST/img?name="teapot"
	@Produces("image/jpeg")
	public InputStream getImagebyName(@PathParam("name") String fileName)
			throws IOException {
		fileName += ".jpg";
		java.nio.file.Path dest = Paths.get("images").resolve(fileName);
		if (!Files.exists(dest)) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return Files.newInputStream(dest);
	}

	@POST
	@Consumes("image/jpeg, image/jpg")
	public Response upload(InputStream in,
			@HeaderParam("Content-Type") String fileType,
			@HeaderParam("Content-Length") long fileSize) throws IOException {

		System.out.println("Received call to load");
		// Make sure the file is not larger than the maximum allowed size.
		if (fileSize > 1024 * 1024 * MAX_SIZE_IN_MB) {
			throw new WebApplicationException(Response
					.status(Status.BAD_REQUEST)
					.entity("Image is larger than " + MAX_SIZE_IN_MB + "MB")
					.build());
		}

		String fileName = "" + System.currentTimeMillis();
		if (fileType.equals("image/jpeg")) {
			fileName += ".jpg";
		} else {
			fileName += ".png";
		}
		System.out.println("Set new filename to " + fileName);

		File directory = new File("images");
		if (!directory.isDirectory()) {
			boolean created = directory.mkdir();
			if (!created) {
				System.out.println("Unable to create a folder for images");
				return Response.serverError().build();
			}
		}
		java.nio.file.Path path = Paths.get("images");
		Files.copy(in, path.resolve(fileName),
				StandardCopyOption.REPLACE_EXISTING);
		return Response.status(Status.CREATED)
				.location(URI.create("/" + fileName)).build();
	}

}
