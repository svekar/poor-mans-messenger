package org.example.quarkus;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
	    tags = {
	            @Tag(name="message", description="messaging"),
	    },
	    info = @Info(
	        title="Poor man's messaging API",
	        description = "Sending messages the hard way",
	        version = "1.0.0",
	        contact = @Contact(
	            name = "Poor man's messaging API Support",
	            url = "http://exampleurl.com/contact",
	            email = "techsupport@example.com"),
	        termsOfService = "As you wish.")
)
public class PoorMansMessengerApplication extends Application {
}
