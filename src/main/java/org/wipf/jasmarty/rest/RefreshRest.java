package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.jasmarty.RefreshLoop;

/**
 * @author wipf
 *
 */
@Path("/refresh")
@ApplicationScoped
public class RefreshRest {

	@Inject
	RefreshLoop refreshLoop;

	@GET
	@Path("/on")
	@Produces(MediaType.APPLICATION_JSON)
	public Response on() {
		refreshLoop.start();
		return Response.ok("{}").build();
	}

	@GET
	@Path("/off")
	@Produces(MediaType.APPLICATION_JSON)
	public Response off() {
		refreshLoop.stop();
		return Response.ok("{}").build();
	}

	@GET
	@Path("/refreshCache")
	@Produces(MediaType.APPLICATION_JSON)
	public Response refreshCache() {
		refreshLoop.doRefreshCacheManuell();
		return Response.ok("{}").build();
	}

}
