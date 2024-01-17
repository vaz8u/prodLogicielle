package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.endtoend;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.ParticipantDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.RestAssured;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ParticipantTestEndToEnd{

    private static final String API_BASE_PATH = "/api/participant/";



    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void participantTestEndToEnd(){
        Response res;
        res =
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(API_BASE_PATH)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

        List<Participant> participants = res.jsonPath().getList(".", Participant.class);
        if (participants.isEmpty()) {
            assertEquals(participants.size(), 0);
            assertEquals(res.asString(), "[]");

        } else {
            Participant firstParticipant = participants.get(0);
            assertNotNull(firstParticipant.getParticipantId());
            assertNotNull(firstParticipant.getNom());
            assertNotNull(firstParticipant.getPrenom());
        }


        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setParticipantId(1L);
        participantDto.setNom("Man");
        participantDto.setPrenom("Sam");

        res = given()
                    .contentType(ContentType.JSON)
                    .body(participantDto)
                    .when()
                    .post(API_BASE_PATH)
                    .then()
                    .statusCode(201)
                    .extract()
                    .response();

        Participant createdParticipant = res.as(Participant.class);
        Long participantId = createdParticipant.getParticipantId();
        assertEquals(createdParticipant.getNom(), participantDto.getNom());
        assertEquals(createdParticipant.getPrenom(), participantDto.getPrenom());


        res = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(API_BASE_PATH + participantId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

        createdParticipant = res.as(Participant.class);
        assertEquals(createdParticipant.getParticipantId(), participantId);
        assertEquals(createdParticipant.getNom(), participantDto.getNom());
        assertEquals(createdParticipant.getPrenom(), participantDto.getPrenom());


        participantDto.setNom("Pierre");
        participantDto.setPrenom("Caillou");
        res = given()
                    .contentType(ContentType.JSON)
                    .body(participantDto)
                    .when()
                    .put(API_BASE_PATH + participantId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

        createdParticipant = res.as(Participant.class);
        assertEquals(createdParticipant.getParticipantId(), participantId);
        assertEquals(createdParticipant.getNom(), participantDto.getNom());
        assertEquals(createdParticipant.getPrenom(), participantDto.getPrenom());


        res = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(API_BASE_PATH + participantId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

        createdParticipant = res.as(Participant.class);
        assertEquals(createdParticipant.getParticipantId(), participantId);
        assertEquals(createdParticipant.getNom(), participantDto.getNom());
        assertEquals(createdParticipant.getPrenom(), participantDto.getPrenom());


        participantDto.setNom(null);
        res = given()
                .contentType(ContentType.JSON)
                .body(participantDto)
                .when()
                .put(API_BASE_PATH + participantId)
                .then()
                .statusCode(500)
                .extract()
                .response();


        res = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(API_BASE_PATH + participantId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();


        res = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(API_BASE_PATH + participantId)
                    .then()
                    .statusCode(500)
                    .extract()
                    .response();


        res = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(API_BASE_PATH + participantId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();


        res = given()
                .contentType(ContentType.JSON)
                .body(participantDto)
                .when()
                .put(API_BASE_PATH + participantId)
                .then()
                .statusCode(500)
                .extract()
                .response();


        participantDto.setPrenom(null);
        res = given()
                .contentType(ContentType.JSON)
                .body(participantDto)
                .when()
                .post(API_BASE_PATH)
                .then()
                .statusCode(500)
                .extract()
                .response();
    }

}