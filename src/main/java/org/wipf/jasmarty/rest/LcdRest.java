package org.wipf.jasmarty.rest;

import javax.inject.Inject;
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
@Path("/lcd")
public class LcdRest {

	@Inject
	LcdConnect lcdConnect;

	@GET
	@Path("/open")
	@Produces(MediaType.APPLICATION_JSON)
	public String open() {
		return "{\"open\":\"" + lcdConnect.startPort() + "\"}";
	}

	@GET
	@Path("/close")
	@Produces(MediaType.APPLICATION_JSON)
	public String close() {
		return "{\"close\":\"" + lcdConnect.close() + "\"}";
	}

	@GET
	@Path("/refresh")
	@Produces(MediaType.TEXT_PLAIN)
	public Response refreshDisplay() {
		lcdConnect.refreshDisplay();
		return Response.ok("{\"TODO\":\"TODO\"}").build(); // TODO
	}

	@GET
	@Path("/write/{x}/{y}/{str}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response cWriteLine(@PathParam("x") Integer x, @PathParam("y") Integer y, @PathParam("str") String s) {
		lcdConnect.writeLineToCache(x, y, s.toCharArray());
		return Response.ok("{\"save\":\"TODO\"}").build(); // TODO
	}

	@GET
	@Path("/ist")
	@Produces(MediaType.TEXT_PLAIN)
	public String chIst() {
		return lcdConnect.getCachIstAsString();

	}

	@GET
	@Path("/soll")
	@Produces(MediaType.TEXT_PLAIN)
	public String chSoll() {
		return lcdConnect.getCachSollAsString();

	}

//	@GET
//	@Path("/writeAscii/{int}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String writeAscii(@PathParam("int") Integer n) {
//		return "{\"test\":\"" + jaSmartyConnect.writeAscii(n) + "\"}";
//	}

//	@GET
//	@Path("/pos/{x}/{y}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String pos(@PathParam("x") Integer x, @PathParam("y") Integer y) {
//		jaSmartyConnect.setCursor(x, y);
//		return "{}";
//	}

//	@GET
//	@Path("/cls")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String cls() {
//		jaSmartyConnect.clearScreen();
//		return "{}";
//	}

//	@GET
//	@Path("/write/{s}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String close(@PathParam("s") String s) {
//		jaSmartySend.sendString(s);
//		return "{}";
//	}

}