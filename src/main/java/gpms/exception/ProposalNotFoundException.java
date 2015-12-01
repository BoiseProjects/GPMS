package gpms.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ProposalNotFoundException extends WebApplicationException {
	public ProposalNotFoundException(String msg) {
		super(Response.status(Response.Status.NOT_FOUND).entity(msg)
				.type("text/plain").build());
	}

}
