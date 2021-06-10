package org.example.quarkus;

import java.net.URI;
import java.util.Collection;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional(value = TxType.SUPPORTS)
public class MessageResource {

	private static final Logger logger = LoggerFactory
			.getLogger(MessageResource.class);

	@GET
	@Path("{user}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(description = "Get all messages of a user", summary = "Get all messages of a user", deprecated = false)
	public Collection<Message> getMessages(
			@Parameter(description = "The owner of the messages", example = "Zot") @PathParam String user) {
		return Message.getAllMessagesOfUser(user);
	}

	@GET
	@Path("{user}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get a specific message for a user")
	public Response getMessage(
			@Parameter(description = "The owner of the message", example = "Zotter") @PathParam String user,
			@Parameter(description = "The id of the message", example = "1") @PathParam long id) {

		Message msg = Message.getSpecificMessage(id);
		if (msg == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		if (!msg.recipient.equals(user)) {
			logger.warn(
					"User {} attempted to read message {} of user {}. Urettferdig, ass!",
					user, id, msg.recipient);
			return Response.status(Status.FORBIDDEN).build();
		}
		return Response.ok(msg).build();
	}

	@POST
	@Path("{user}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(value = TxType.REQUIRED)
	@Operation(summary = "Send a message to a specific user")
	public Response sendMessage(
			@Parameter(description = "The recipient of the message", example = "Bongo") @PathParam("user") String recipient,
			@Parameter(description = "The message to send", example = "{\"sender\": \"Zot\", \"content\": \"Goddag\"}") Message message,
			@Context UriInfo uriInfo) {
		Message newMsg = new Message(recipient, message.sender,
				message.content);
		newMsg = Message.save(newMsg);
		URI uri = uriInfo.getBaseUriBuilder()
				.path(newMsg.id.toString())
				.build();
		return Response.created(uri).entity(newMsg).build();
	}

}
