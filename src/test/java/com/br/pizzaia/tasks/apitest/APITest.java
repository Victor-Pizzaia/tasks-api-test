package com.br.pizzaia.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}

	@Test
	public void shouldReturnTasks() {
		RestAssured.given()
		.when()
			.get("/todo")
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void shouldAddTaskWithSuccess() {
		RestAssured.given()
			.body("{ \"task\": \"Teste via API\", \"dueDate\": \"2030-04-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void shouldNotAddInvalidTask() {
		RestAssured.given()
			.body("{ \"task\": \"Teste via API\", \"dueDate\": \"2010-04-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
	}
	
	@Test
	public void shouldRemoveTaskWithSuccess() {
		// Inserir
		int id = RestAssured.given()
			.body("{ \"task\": \"Terefa teste\", \"dueDate\": \"2030-04-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
			.extract().path("id")
		;

		// Remover
		RestAssured.given()
		.when()
			.delete("/todo/"+id)
		.then()
			.statusCode(204)
		;
	}
}
