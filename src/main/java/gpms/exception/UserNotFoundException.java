package gpms.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UserNotFoundException extends WebApplicationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String msg) {
		super(Response.status(Response.Status.NOT_FOUND).entity(msg)
				.type("text/plain").build());
	}
}
