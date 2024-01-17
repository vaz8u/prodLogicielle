package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.endtoend;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.CommentaireDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.DateSondageDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.ParticipantDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.SondageDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
public class SondageTestEndToEnd {

    private static final String API_BASE_PATH = "/api/sondage/";

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void participantTestEndToEnd() {
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


        res = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(API_BASE_PATH)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
        List<SondageDto> sondages = res.jsonPath().getList(".", SondageDto.class);
        if (sondages.isEmpty()) {
            assertEquals(sondages.size(), 0);
            assertEquals(res.asString(), "[]");
        } else {
            SondageDto firstSondage = sondages.get(0);
            assertNotNull(firstSondage.getSondageId());
            assertNotNull(firstSondage.getNom());
            assertNotNull(firstSondage.getCloture());
            assertNotNull(firstSondage.getCreateBy());
            assertNotNull(firstSondage.getDescription());
            assertNotNull(firstSondage.getFin());
        }


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
                .post(API_BASE_PATH)
                .then()
                .statusCode(201)
                .extract()
                .response();
        SondageDto createdSondage = res.as(SondageDto.class);
        Long sondageId = createdSondage.getSondageId();
        assertEquals(createdSondage.getCreateBy(), sondageDto.getCreateBy());
        assertEquals(createdSondage.getNom(), sondageDto.getNom());
        assertEquals(createdSondage.getCloture(), sondageDto.getCloture());
        assertEquals(createdSondage.getFin(), sondageDto.getFin());
        assertEquals(createdSondage.getDescription(), sondageDto.getDescription());


        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_BASE_PATH + sondageId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        createdSondage = res.as(SondageDto.class);
        assertEquals(createdSondage.getCreateBy(), sondageDto.getCreateBy());
        assertEquals(createdSondage.getNom(), sondageDto.getNom());
        assertEquals(createdSondage.getCloture(), sondageDto.getCloture());
        assertEquals(createdSondage.getFin(), sondageDto.getFin());
        assertEquals(createdSondage.getDescription(), sondageDto.getDescription());

        //TODO modification
//        sondageDto.setDescription("Déscription modifié");
//        sondageDto.setNom("Nom modifié");
//        res = given()
//                .contentType(ContentType.JSON)
//                .body(sondageDto)
//                .when()
//                .put(API_BASE_PATH + sondageId)
//                .then()
//                .statusCode(500)
//                .extract()
//                .response();
//
//        System.out.println(res.getBody().asString());
//        createdSondage = res.as(SondageDto.class);
//        assertEquals(createdSondage.getCreateBy(), sondageDto.getCreateBy());
//        assertEquals(createdSondage.getNom(), sondageDto.getNom());
//        assertEquals(createdSondage.getCloture(), sondageDto.getCloture());
//        assertEquals(createdSondage.getFin(), sondageDto.getFin());
//        assertEquals(createdSondage.getDescription(), sondageDto.getDescription());


        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_BASE_PATH + sondageId + "/best/")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<CommentaireDto> bestCommentaires = res.jsonPath().getList(".", CommentaireDto.class);
        assertEquals(bestCommentaires.size(), 0);
        assertEquals(res.asString(), "[]");


        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_BASE_PATH + sondageId + "/commentaires/")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<CommentaireDto> commentaires = res.jsonPath().getList(".", CommentaireDto.class);
        assertEquals(commentaires.size(), 0);
        assertEquals(res.asString(), "[]");


        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_BASE_PATH + sondageId + "/dates/")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<DateSondageDto> dates = res.jsonPath().getList(".", DateSondageDto.class);
        assertEquals(dates.size(), 0);
        assertEquals(res.asString(), "[]");


        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_BASE_PATH + sondageId + "/maybe/")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<DateSondageDto> maybeDates = res.jsonPath().getList(".", DateSondageDto.class);
        assertEquals(maybeDates.size(), 0);
        assertEquals(res.asString(), "[]");



        CommentaireDto commentaireDto = new CommentaireDto();
        commentaireDto.setParticipant(participantId);
        commentaireDto.setCommentaire("Commentaire Test");
        res = given()
                .contentType(ContentType.JSON)
                .body(commentaireDto)
                .when()
                .post(API_BASE_PATH + sondageId + "/commentaires/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        CommentaireDto createdCommentaire = res.as(CommentaireDto.class);
        Long commentaireId = createdCommentaire.getCommentaireId();
        assertEquals(createdCommentaire.getCommentaireId(), commentaireId);
        assertEquals(createdCommentaire.getParticipant(), commentaireDto.getParticipant());
        assertEquals(createdCommentaire.getCommentaire(), commentaireDto.getCommentaire());


        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_BASE_PATH + sondageId + "/commentaires/")
                .then()
                .statusCode(200)
                .extract()
                .response();
        commentaires = res.jsonPath().getList(".", CommentaireDto.class);
        if (!commentaires.isEmpty()) {
            CommentaireDto firstCommentaire = commentaires.get(0);
            assertEquals(firstCommentaire.getCommentaireId(), commentaireId);
            assertEquals(firstCommentaire.getParticipant(), commentaireDto.getParticipant());
            assertEquals(firstCommentaire.getCommentaire(), commentaireDto.getCommentaire());
        }


        DateSondageDto dateSondage1Dto = new DateSondageDto();
        dateSondage1Dto.setDate(new Date());
        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondage1Dto)
                .when()
                .post(API_BASE_PATH + sondageId + "/dates/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        DateSondageDto createdDateSondage1 = res.as(DateSondageDto.class);
        Long dateSondage1Id = createdDateSondage1.getDateSondageId();
        assertEquals(createdDateSondage1.getDateSondageId(), dateSondage1Id);
        assertEquals(createdDateSondage1.getDate(), dateSondage1Dto.getDate());

        DateSondageDto dateSondage2Dto = new DateSondageDto();
        dateSondage2Dto.setDate(new Date());
        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondage2Dto)
                .when()
                .post(API_BASE_PATH + sondageId + "/dates/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        DateSondageDto createdDateSondage2 = res.as(DateSondageDto.class);
        Long dateSondage2Id = createdDateSondage2.getDateSondageId();
        assertEquals(createdDateSondage2.getDateSondageId(), dateSondage2Id);
        assertEquals(createdDateSondage2.getDate(), dateSondage2Dto.getDate());


        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_BASE_PATH + sondageId + "/dates/")
                .then()
                .statusCode(200)
                .extract()
                .response();
        dates = res.jsonPath().getList(".", DateSondageDto.class);
        if (!commentaires.isEmpty()) {
            DateSondageDto firstDate = dates.get(0);
            assertEquals(firstDate.getDateSondageId(), dateSondage1Id);
            assertEquals(firstDate.getDate(), dateSondage1Dto.getDate());
        }


        //TODO dates maybe
        //TODO dates best

        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/commentaire/" + commentaireId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/date/" + dateSondage1Id)
                .then()
                .statusCode(200)
                .extract()
                .response();

        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/date/" + dateSondage2Id)
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
                .delete("/api/sondage/" + sondageId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get(API_BASE_PATH + sondageId)
                .then()
                .statusCode(500)
                .extract()
                .response();
    }

}
