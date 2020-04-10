package org.wipf.jasmarty.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wipf.jasmarty.logic.base.App;

/**
 * @author wipf
 *
 */
@Path("/wipf")
public class Wipf {

	@GET
	@Path("/ver")
	@Produces(MediaType.APPLICATION_JSON)
	public String getver() {
		return "{\"ver\":\"" + App.VERSION + "\"}";
	}

}