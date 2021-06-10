package org.example.quarkus;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response.Status;

@QuarkusTest
@QuarkusTestResource(PostgreSQLResource.class)
class MessageResourceTest {

	static final String USER = "user";
	static final String MESSAGELESS_USER = "user0";
	
    @Test
    void messagesOfUserShouldBeEmpty() {
    	given()
    		.when()
    			.contentType(ContentType.JSON)
    			.get("/message/{user}", MESSAGELESS_USER)
    		.then()
    			.contentType(ContentType.JSON)
    			.statusCode(Status.OK.getStatusCode())
    			.body("$", is(empty()));
    }

    @Test
    void postMessageShouldReturnPojoWithId() {
    	Message message = new Message(null, USER, "Meld fra hvor du går, da!");
		given()
    		.when()
				.contentType(ContentType.JSON)
    			.body(message)
    			.post("/message/{user}", USER)
    		.then()
    			.log().all()
    			.statusCode(Status.CREATED.getStatusCode())    		
				.contentType(ContentType.JSON)
				.body("sender", is(message.sender),
					"recipient", is(USER),	
					"content", is(message.content),				
					"id", is(notNullValue()))								
    			;    		
    }
    
    @Test
    void getSpecificMessageReturnsMessage() {
    	Message message = new Message(USER, USER, "Meld deg sjøl"); 
    	var sentMessage = 
		  given()
			.when()
				.contentType(ContentType.JSON)
				.body(message)
				.post("/message/{user}", USER)
			.then()
				.statusCode(Status.CREATED.getStatusCode())
				.extract().as(Message.class);
    	
		var retrievedMessage = given()
    		.when()
    			.contentType(ContentType.JSON)
    			.get("/message/{user}/{id}", USER, sentMessage.id)
    		.then()
    			.contentType(ContentType.JSON)
    			.statusCode(Status.OK.getStatusCode())
    			.extract().as(Message.class);
    	assertEquals(retrievedMessage, sentMessage);    	
    }
    
}