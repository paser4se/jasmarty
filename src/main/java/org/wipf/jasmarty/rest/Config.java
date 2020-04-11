package org.wipf.jasmarty.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.wipf.jasmarty.logic.jasmarty.SerialConfig;

/**
 * @author wipf
 *
 */
@Path("/config")
public class Config {

	@Inject
	SerialConfig serialConfig;

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPage() {
		return serialConfig.getConfig().toJson();
	}

	@PUT
	@Path("/set")
	@Produces(MediaType.APPLICATION_JSON)
	public String getconfig(String jnRoot) {
		return serialConfig.setConfig(jnRoot);

	}

}
