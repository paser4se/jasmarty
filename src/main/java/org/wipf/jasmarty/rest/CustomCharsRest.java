package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.jasmarty.CustomChars;

/**
 * @author wipf
 *
 */
@Path("/customchars")
@Produces(MediaType.APPLICATION_JSON)
// @Consumes(MediaType.APPLICATION_JSON) TODO POST geht nicht
@ApplicationScoped
public class CustomCharsRest {

	// Testen mit:
	//
	// curl -d '{"name":"test", "id":4, "position":4, "data":"11114444"}' -X POST
	// localhost:8080/customchars/set
	//
	// curl -X POST localhost:8080/customchars/loadCharToLcdFromDB/4
	//
	// Anzeigen mit $char(4)

	@Inject
	CustomChars customChars;

	@GET
	@Path("/get/{id}")
	public Response getC(@PathParam("id") Integer nId) {
		return Response.ok(customChars.getFromDB(nId).toJson()).build();
	}

	@POST
	@Path("/set")
	public Response setC(String jnRoot) {
		customChars.setCustomChar(jnRoot);
		return Response.ok().build();

	}

	@POST
	@Path("/loadCharToLcdFromDB/{id}")
	public Response loadCharToLcdFromDB(@PathParam("id") Integer nId) {
		customChars.loadCharToLcdFromDB(nId);
		return Response.ok().build();
	}

}
