package org.wipf.jasmarty.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.telegram.extensions.TAppTodoList;

import com.google.inject.Inject;

/**
 * @author wipf
 *
 */
@Path("/todolist")
public class TodoListRest {

	@Inject
	TAppTodoList todoList;

	@GET
	@Path("/getAll")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getall() {
		return Response.ok(todoList.getAll()).build();
	}

	@GET
	@Path("/getByUserID/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getByUserID(@PathParam("id") Integer nId) {
		return Response.ok(todoList.getAllByUser(nId)).build();
	}

	@GET
	@Path("/getAllFull")
	@Produces(MediaType.TEXT_PLAIN)
	public Response todolist() {
		return Response.ok(todoList.getAllFull()).build();
	}

	@GET
	@Path("/getAllJson")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllJson() {
		return Response.ok(todoList.getAllAsJson()).build();
	}

//	
//	// TODO
//	@POST
//	@Path("/addTodo/{msg}")
//	@Produces(MediaType.TEXT_PLAIN)
//	public Response addTodo(@PathParam("msg") String sMsg) {
//		return MWipf.genResponse(MTeleMsg.sendMsgToGroup(sMsg));
//	}

}
