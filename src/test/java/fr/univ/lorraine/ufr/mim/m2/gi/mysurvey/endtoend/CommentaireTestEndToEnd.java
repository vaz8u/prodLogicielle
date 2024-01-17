package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.endtoend;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.*;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.repositories.CommentaireRepository;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.CommentaireService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CommentaireTestEndToEnd {

    private static final String API_BASE_PATH = "/api/commentaire/";

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void commentaireTestEndToEnd(){
        Response res;

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


        CommentaireDto commentaireDto = new CommentaireDto();
        commentaireDto.setParticipant(participantId);
        commentaireDto.setCommentaire("Commentaire Test");
        res = given()
                .contentType(ContentType.JSON)
                .body(commentaireDto)
                .when()
                .post("/api/sondage/" + sondageId + "/commentaires/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        CommentaireDto createdCommentaire = res.as(CommentaireDto.class);
        Long commentaireId = createdCommentaire.getCommentaireId();

//TODO Modification
//        commentaireDto.setCommentaire("Commentaire Test Modifié");
//        res = given()
//                .contentType(ContentType.JSON)
//                .body(commentaireDto)
//                .when()
//                .put(API_BASE_PATH + commentaireId)
//                .then()
//                .statusCode(200)
//                .extract()
//                .response();
//        createdCommentaire = res.as(CommentaireDto.class);
//        assertEquals(createdCommentaire.getCommentaireId(), commentaireId);
//        assertEquals(createdCommentaire.getCommentaireId(), createdCommentaire.getCommentaire());
//        assertEquals(createdCommentaire.getParticipant(), createdCommentaire.getParticipant());


        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(API_BASE_PATH + commentaireId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/sondage/" + sondageId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/participant/" + participantId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(API_BASE_PATH + commentaireId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
