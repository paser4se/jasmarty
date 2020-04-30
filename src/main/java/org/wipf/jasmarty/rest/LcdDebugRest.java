package org.wipf.jasmarty.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.jasmarty.LcdConnect;

/**
 * @author wipf
 *
 */
@Path("/lcddebug")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LcdDebugRest {

	@Inject
	LcdConnect lcdConnect;

	@GET
	@Path("/write/{x}/{y}/{str}")
	public Response cWriteLine(@PathParam("x") Integer x, @PathParam("y") Integer y, @PathParam("str") String s) {
		lcdConnect.writeLineToCache(x, y, s.toCharArray());
		return Response.ok("{}").build();
	}

	@GET
	@Path("/refresh")
	public Response refreshDisplay() {
		lcdConnect.refreshDisplay();
		return Response.ok("{\"TODO\":\"TODO\"}").build();
	}

	@GET
	@Path("/writeAscii/{int}")
	public Response writeAscii(@PathParam("int") Integer n) {
		lcdConnect.writeAscii(n);
		return Response.ok("{}").build();
	}

	@GET
	@Path("/pos/{x}/{y}")
	public Response pos(@PathParam("x") Integer x, @PathParam("y") Integer y) {
		lcdConnect.setCursor(x, y);
		return Response.ok("{}").build();
	}

	@GET
	@Path("/cls")
	public Response cls() {
		lcdConnect.clearScreen();
		return Response.ok("{}").build();
	}

}
