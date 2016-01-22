package gpms.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ProposalAlreadyExistsException extends WebApplicationException {
	public ProposalAlreadyExistsException(String msg) {
		super(Response.status(Response.Status.FORBIDDEN).entity(msg)
				.type("text/plain").build());
	}
}
