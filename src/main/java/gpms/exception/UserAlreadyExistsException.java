package gpms.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UserAlreadyExistsException extends WebApplicationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsException(String msg) {
		super(Response.status(Response.Status.FORBIDDEN).entity(msg)
				.type("text/plain").build());
	}
}
