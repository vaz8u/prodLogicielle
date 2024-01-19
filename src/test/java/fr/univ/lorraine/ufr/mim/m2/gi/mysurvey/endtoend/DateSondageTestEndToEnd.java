package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.endtoend;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.DateSondageDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.DateSondeeDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.ParticipantDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.SondageDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Choix;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DateSondageTestEndToEnd {

    private static final String API_BASE_PATH = "/api/date/";

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void dateSondageTestEndToEnd() {
        Response res;

        //Création d'un participant
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setParticipantId(1L);
        participantDto.setNom("Man");
        participantDto.setPrenom("Sam");
        res = given()
                .contentType(ContentType.JSON)
                .body(participantDto)
                .when()
                .post("/api/participant/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        Participant createdParticipant = res.as(Participant.class);
        Long participantId = createdParticipant.getParticipantId();

        //Création d'un sondage
        SondageDto sondageDto = new SondageDto();
        sondageDto.setCreateBy(participantId);
        sondageDto.setNom("Sondage Test");
        sondageDto.setCloture(false);
        sondageDto.setFin(new Date());
        sondageDto.setDescription("Description du Sondage");
        res = given()
                .contentType(ContentType.JSON)
                .body(sondageDto)
                .when()
                .post("/api/sondage/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        SondageDto createdSondage = res.as(SondageDto.class);
        Long sondageId = createdSondage.getSondageId();

        //Création d'une date de sondage
        DateSondageDto dateSondageDto = new DateSondageDto();
        dateSondageDto.setDate(new Date());
        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondageDto)
                .when()
                .post("/api/sondage/" + sondageId + "/dates/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        DateSondageDto createdDateSondage = res.as(DateSondageDto.class);
        Long dateSondageId = createdDateSondage.getDateSondageId();

        //Création d'une date sondée
        //Test de la date sondée
        DateSondeeDto dateSondeeDto = new DateSondeeDto();
        dateSondeeDto.setParticipant(participantId);
        dateSondeeDto.setChoix(String.valueOf(Choix.DISPONIBLE));
        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondeeDto)
                .when()
                .post(API_BASE_PATH + dateSondageId + "/participer/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        DateSondeeDto createdDateSondee = res.as(DateSondeeDto.class);
        Long dateSondeeId = createdDateSondee.getDateSondeeId();
        assertEquals(createdDateSondee.getDateSondeeId(), dateSondeeId);
        assertEquals(createdDateSondee.getParticipant(), dateSondeeDto.getParticipant());
        assertEquals(createdDateSondee.getChoix(), dateSondeeDto.getChoix());

        //Suppression de la date sondée
        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(API_BASE_PATH + dateSondeeId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        //Suppression du sondage
        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/sondage/" + sondageId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        //Suppression du participant
        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/participant/" + participantId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        //Suppression de la date sondée
        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(API_BASE_PATH + dateSondeeId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
