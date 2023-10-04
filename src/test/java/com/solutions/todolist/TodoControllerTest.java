package com.solutions.todolist;

import static com.solutions.todolist.model.TodoState.UNCOMPLETED;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.solutions.todolist.dto.TodoDTO;
import com.solutions.todolist.model.Todo;
import com.solutions.todolist.repository.TodoRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Instant;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

@Import(DatabaseTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TodoRepository repository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        repository.deleteAll();
    }

    @Test
    void givenEmptyRepositoryWhenFindAllThenReturnEmptyList() {
        given()
                .contentType(ContentType.JSON)
                .when().get("/todos")
                .then().body("data", hasSize(0))
                .body("totalElements", equalTo(0))
                .statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    void givenNTodosWhenFindAllThenReturnListOfNTodos(int N) {
        for (int i = 1; i <= N; i++) {
            final Todo todo = new Todo();
            todo.setTitle("Title " + i);
            repository.save(todo);
        }

        given().contentType(ContentType.JSON)
                .when().get("/todos")
                .then()
                .body("data", hasSize(N))
                .body("data[0].title", containsString("Title"))
                .body("data[0].description", nullValue())
                .body("totalElements", equalTo(N))
                .statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Title 1", "Fix door", "Wash dishes"})
    void givenTodoIdWhenFindAllThenReturnThatTodoId(String title) {
        Todo todo = new Todo();
        todo.setTitle(title);
        int id = Math.toIntExact(repository.save(todo).getId());

        given().contentType(ContentType.JSON)
                .when().get("/todos/" + id)
                .then()
                .body("id", equalTo(id))
                .body("title", equalTo(title))
                .body("description", nullValue())
                .statusCode(200);
    }

    @Test
    void givenValidTodoWhenSavingThenReturnSavedTodo() throws JSONException {
        final String title = "Title 1";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("description", "");
        Instant now = Instant.now();

        final TodoDTO todoDTO = given().contentType(ContentType.JSON)
                .when().body(jsonObject.toString()).post("/todos")
                .then()
                .statusCode(201)
                .extract()
                .as(TodoDTO.class);

        assertThat(todoDTO.id(), notNullValue());
        assertThat(todoDTO.title(), equalTo(title));
        assertThat(todoDTO.description(), emptyString());
        assertThat(todoDTO.state(), equalTo(UNCOMPLETED.name()));
        assertThat(todoDTO.createdAt(), greaterThan(now));
        assertThat(todoDTO.updatedAt(), nullValue());
    }

    @Test
    void givenInvalidTitleTodoWhenSavingThenReturnError() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "");
        jsonObject.put("description", "");
        Instant now = Instant.now();

        given().contentType(ContentType.JSON)
                .when().body(jsonObject.toString()).post("/todos")
                .then()
                .log().all()
                .statusCode(400);
    }


    @Test
    void givenValidTodoWhenUpdatingThenReturnUpdatedTodo() throws JSONException {
        final String title = "Title 1";
        final Todo todo = new Todo();
        todo.setTitle(title);
        Todo savedTodo = repository.save(todo);
        final String updatedTitle = "Updated Title";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", updatedTitle);
        Instant now = Instant.now();

        final TodoDTO todoDTO = given().contentType(ContentType.JSON)
                .when().body(jsonObject.toString()).patch("/todos/" + savedTodo.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(TodoDTO.class);

        assertThat(todoDTO.id(), equalTo(savedTodo.getId()));
        assertThat(todoDTO.title(), equalTo(updatedTitle));
        assertThat(todoDTO.title(), not(equalTo(title)));
        assertThat(todoDTO.description(), nullValue());
        assertThat(todoDTO.state(), equalTo(UNCOMPLETED.name()));
        assertThat(todoDTO.createdAt().toEpochMilli(), equalTo(savedTodo.getCreatedAt().toEpochMilli()));
        assertThat(todoDTO.updatedAt(), greaterThan(now));
    }

    @Test
    void givenValidTodoIdWhenDeletingThenReturnOk() {
        final String title = "Title 1";
        final Todo todo = new Todo();
        todo.setTitle(title);
        final Todo savedTodo = repository.save(todo);

        given().contentType(ContentType.JSON).get("/todos/" + savedTodo.getId()).then().statusCode(200);
        given().contentType(ContentType.JSON).delete("/todos/" + savedTodo.getId()).then().statusCode(204);
        given().contentType(ContentType.JSON).get("/todos/" + savedTodo.getId()).then().statusCode(404);
    }

    @Test
    void givenInvalidTodoIdWhenDeletingThenReturnNotFound() {
        given().contentType(ContentType.JSON).delete("/todos/" + 99).then().statusCode(404);
    }
}
