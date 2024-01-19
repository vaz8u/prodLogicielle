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

        //Récuperation des participants
        //Test si la liste de participants est vide
        //Test si la liste de participants n'est pas vide
        res = given()
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

        //Création d'un participant
        //Test si les données crée du participant son bon
        ParticipantDto participantDto = new ParticipantDto();
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

        //Récuperation du participant crée
        //Test si les données récuperé son bon
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

        //Modification du participant
        //Test si les données modifié son bon
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

        //Récuperation du participant modifié
        //Test si les données récuperé son bon
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

        //Modification du participant avec un nom null
        //Test si une erreur 500 a été lancé
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

        //Suppression du participant
        res = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(API_BASE_PATH + participantId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

        //Récuperation d'un participant qui n'existe pas
        res = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(API_BASE_PATH + participantId)
                    .then()
                    .statusCode(500)
                    .extract()
                    .response();

        //Suppression d'un participant qui n'existe pas
        res = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(API_BASE_PATH + participantId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

        //Modification d'un participant qui n'existe pas
        res = given()
                .contentType(ContentType.JSON)
                .body(participantDto)
                .when()
                .put(API_BASE_PATH + participantId)
                .then()
                .statusCode(500)
                .extract()
                .response();

        //Création d'un participant avec un prénom null
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