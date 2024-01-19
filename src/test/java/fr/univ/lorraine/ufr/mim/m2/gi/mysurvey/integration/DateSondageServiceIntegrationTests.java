package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.integration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.DateSondageService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.ParticipantService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.SondageService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.DateSondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;

@SpringBootTest
@Transactional
class DateSondageServiceIntegrationTests {

    @Autowired
    DateSondageService dateSondageService;

    @Autowired
    ParticipantService participantService;

    @Autowired
    SondageService sondageService;

    Participant participant;
    Sondage sondage;
    DateSondage dateSondage;
    DateSondage dateSondage2;

    @BeforeEach
    void setup() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date dateInOneHour = calendar.getTime();

        var participant = new Participant(null, "name", "fname");
        this.participant = participantService.create(participant);

        var sondage = new Sondage(null, "name", "description", dateInOneHour, false, null, null, participant);
        this.sondage = sondageService.create(this.participant.getParticipantId(), sondage);

        DateSondage dateSondage = new DateSondage(null, new Date(), this.sondage, new ArrayList<>());
        this.dateSondage = this.dateSondageService.create(this.sondage.getSondageId(), dateSondage);

        DateSondage dateSondage2 = new DateSondage(null, new Date(), this.sondage, new ArrayList<>());
        this.dateSondage2 = this.dateSondageService.create(this.sondage.getSondageId(), dateSondage2);
    }

    @Test
    void createDateSondageCreatedObjectIsReturnedOne() {
        var res = this.dateSondageService.create(this.sondage.getSondageId(), dateSondage);

        Assertions.assertEquals(res, this.dateSondage);
    }

    @Test
    void create2DateSondagesShouldNotBeSame() {
        var res = this.dateSondageService.create(this.sondage.getSondageId(), this.dateSondage);
        var res2 = this.dateSondageService.create(this.sondage.getSondageId(), this.dateSondage2);

        Assertions.assertNotEquals(res, res2);
    }

    @Test
    void createDateSondageTwiceShouldBeSame() {
        var res = this.dateSondageService.create(this.sondage.getSondageId(), this.dateSondage);
        var res2 = this.dateSondageService.create(this.sondage.getSondageId(), this.dateSondage);

        Assertions.assertEquals(res, res2);
    }

    @Test
    void deleteExistingCommentaireShouldWork() {
        var res = this.dateSondageService.create(this.sondage.getSondageId(), this.dateSondage);

        int deleted = this.dateSondageService.delete(res.getDateSondageId());

        Assertions.assertEquals(1, deleted);

        Assertions.assertEquals(0, this.dateSondageService.delete(res.getDateSondageId()));
    }

    @Test
    void deleteNotExistingCommentaireShouldNotWork() {
        Assertions.assertEquals(0, this.dateSondageService.delete(Long.MIN_VALUE));
    }

    @Test
    void deleteByNullShouldException() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.dateSondageService.delete(null);
        });
    }

    @Test
    void getByExistingIdShouldBeSame() {
        var res = this.dateSondageService.create(this.sondage.getSondageId(), this.dateSondage);

        Assertions.assertEquals(res, this.dateSondageService.getById(res.getDateSondageId()));
    }

    @Test
    void getByNotExistingIdShouldBeNull() {
        Assertions.assertEquals(null, this.dateSondageService.getById(Long.MIN_VALUE));
    }

    @Test
    void getByNullShouldException() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.dateSondageService.getById(null);
        });
    }
}
