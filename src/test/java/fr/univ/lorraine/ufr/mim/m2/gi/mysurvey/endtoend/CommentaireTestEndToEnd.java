package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.endtoend;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.*;
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

        //Création d'un commentaire
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

        //Modification d'un commentaire
        //Test si les données modifié du commentaire son bon
        commentaireDto.setCommentaire("Commentaire Test Modifié");
        res = given()
                .contentType(ContentType.JSON)
                .body(commentaireDto)
                .when()
                .put(API_BASE_PATH + commentaireId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        createdCommentaire = res.as(CommentaireDto.class);
        assertEquals(createdCommentaire.getCommentaireId(), commentaireId);
        assertEquals(createdCommentaire.getCommentaire(), createdCommentaire.getCommentaire());
        assertEquals(createdCommentaire.getParticipant(), createdCommentaire.getParticipant());


        //Suppression du commentaire
        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(API_BASE_PATH + commentaireId)
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

        //Suppression du commentaire
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
