package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.endtoend;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.*;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Choix;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.DateSondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.repositories.ParticipantRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    void sondageTestEndToEnd() {
        Response res;

        //Création d'un participant
        ParticipantDto participantDto1 = new ParticipantDto();
        participantDto1.setNom("Man");
        participantDto1.setPrenom("Sam");
        res = given()
                .contentType(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(participantDto1)
                .when()
                .post("/api/participant/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        Participant createdParticipant1 = res.as(Participant.class);
        Long participant1Id = createdParticipant1.getParticipantId();

        //Création d'un participant
        ParticipantDto participantDto2 = new ParticipantDto();
        participantDto2.setNom("Man");
        participantDto2.setPrenom("Sam");
        res = given()
                .contentType(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(participantDto2)
                .when()
                .post("/api/participant/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        Participant createdParticipant2 = res.as(Participant.class);
        Long participant2Id = createdParticipant2.getParticipantId();


        //Récuperation de tous les sondages
        //Test si la liste de sondages est vide
        //Test si la liste de sondages n'est pas vide
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

        //Création d'un sondage
        //Test si les données crée du sondage son bon
        SondageDto sondageDto = new SondageDto();
        sondageDto.setCreateBy(participant1Id);
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

        //Récuperation du sondage crée
        //Test si les données récuperé du sondage son bon
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

        //Modification du sondage crée
        //Test si les données modifié du sondage son bon
        sondageDto.setDescription("Déscription modifié");
        sondageDto.setNom("Nom modifié");
        res = given()
                .contentType(ContentType.JSON)
                .body(sondageDto)
                .when()
                .put(API_BASE_PATH + sondageId)
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

        //Récupération des meilleurs dates d'un sondage
        //Test si la liste de date est vide
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

        //Récupération de tous les commentaires d'un sondage
        //Test si la liste des commentaires est vide
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

        //Récupération de toutes les dates d'un sondage
        //Test si la liste de date est vide
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

        //Récupération des éventuelles meilleures dates d'un sondage
        //Test si la liste des éventuelles meilleures dates est vide
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

        //Création d'un commentaire dans le sondage
        //Test si les données crée du commetaire son bon
        CommentaireDto commentaireDto = new CommentaireDto();
        commentaireDto.setParticipant(participant1Id);
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

        //Récupération de tous les commentaires d'un sondage
        //Test si la liste des commentaires n'est pas vide
        //Test si le 1er commentaire de la liste est bien le commentaire crée
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

        //Dates maybe et dates best
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.MINUTE, 1);
        Date date1 = calendar.getTime();
        calendar.add(Calendar.MINUTE, 1);
        Date date2 = calendar.getTime();

        //Création d'une date de sondage (1)
        //Test si les données crée de la date de sondage son bon
        DateSondageDto dateSondage1Dto = new DateSondageDto();
        dateSondage1Dto.setDate(date1);
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

        //Création d'une date de sondage (2)
        //Test si les données crée de la date de sondage son bon
        DateSondageDto dateSondage2Dto = new DateSondageDto();
        dateSondage2Dto.setDate(date2);
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

        //Récupération de toutes les dates d'un sondage
        //Test si la liste de date n'est pas vide
        //Test si la 1ere date de la liste est la date sondage 1 crée
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

        //Participation du participant 1
        //Création d'une date sondée 1 sur le sondage 1
        DateSondeeDto dateSondeeDto1 = new DateSondeeDto();
        dateSondeeDto1.setParticipant(participant1Id);
        dateSondeeDto1.setChoix(String.valueOf(Choix.DISPONIBLE));
        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondeeDto1)
                .when()
                .post("/api/date/" + dateSondage1Id + "/participer/")
                .then()
                .statusCode(201)
                .extract()
                .response();

        //Création d'une date sondée 1 sur le sondage 2
        DateSondeeDto dateSondeeDto2 = new DateSondeeDto();
        dateSondeeDto2.setParticipant(participant1Id);
        dateSondeeDto2.setChoix(String.valueOf(Choix.PEUTETRE));
        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondeeDto2)
                .when()
                .post("/api/date/" + dateSondage2Id + "/participer/")
                .then()
                .statusCode(201)
                .extract()
                .response();

        //Participation du participant 2
        //Création d'une date sondée 1 sur le sondage 1
        DateSondeeDto dateSondeeDto3 = new DateSondeeDto();
        dateSondeeDto3.setParticipant(participant2Id);
        dateSondeeDto3.setChoix(String.valueOf(Choix.DISPONIBLE));
        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondeeDto3)
                .when()
                .post("/api/date/" + dateSondage1Id + "/participer/")
                .then()
                .statusCode(201)
                .extract()
                .response();

        //Création d'une date sondée 1 sur le sondage 2
        DateSondeeDto dateSondeeDto4 = new DateSondeeDto();
        dateSondeeDto4.setParticipant(participant2Id);
        dateSondeeDto4.setChoix(String.valueOf(Choix.PEUTETRE));
        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondeeDto4)
                .when()
                .post("/api/date/" + dateSondage2Id + "/participer/")
                .then()
                .statusCode(201)
                .extract()
                .response();

        System.out.println(sondageId);
        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondeeDto1)
                .when()
                .get(API_BASE_PATH + sondageId + "/dates/")
                .then()
                .statusCode(200)
                .extract()
                .response();
        dates = res.jsonPath().getList(".", DateSondageDto.class);
        assertEquals(dates.size(), 2);

        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondeeDto1)
                .when()
                .get(API_BASE_PATH + sondageId + "/best/")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<String> dateBest = res.jsonPath().getList(".", String.class);
        assertEquals(dateBest.size(), 1);
        try {
            assertEquals(date1, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(dateBest.get(0)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        res = given()
                .contentType(ContentType.JSON)
                .body(dateSondeeDto1)
                .when()
                .get(API_BASE_PATH + sondageId + "/maybe/")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<String> dateMaybe = res.jsonPath().getList(".", String.class);
        assertEquals(dateMaybe.size(), 2);
        try {
            assertEquals(date1, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(dateMaybe.get(0)));
            assertEquals(date2, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(dateMaybe.get(1)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //Suppression du commentaire
        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/commentaire/" + commentaireId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        //Suppression de la date sondage 1
        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/date/" + dateSondage1Id)
                .then()
                .statusCode(200)
                .extract()
                .response();

        //Suppression de la date sondage 2
        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/date/" + dateSondage2Id)
                .then()
                .statusCode(200)
                .extract()
                .response();

        //Suppression du participant 1
        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/participant/" + participant1Id)
                .then()
                .statusCode(200)
                .extract()
                .response();

        //Suppression du participant 2
        res = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/participant/" + participant2Id)
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

        //Récuperation du sondage qui n'existe pas
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