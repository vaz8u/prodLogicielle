package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.integration;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.*;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.DateSondageService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.DateSondeeService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.ParticipantService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.SondageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
class DateSondeeServiceIntegrationTests {

    @Autowired
    DateSondeeService dateSondeeService;

    @Autowired
    ParticipantService participantService;

    @Autowired
    SondageService sondageService;

    @Autowired
    DateSondageService dateSondageService;

    Participant participant;
    Participant participant2;
    Sondage sondage;
    DateSondage dateSondage;
    DateSondage dateSondage2;
    DateSondee dateSondee;
    DateSondee dateSondee2;

    @BeforeEach
    void setup() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date dateInOneHour = calendar.getTime();

        var participant = new Participant(null, "name", "fname");
        this.participant = participantService.create(participant);

        var participant2 = new Participant(null, "name", "fname");
        this.participant2 = participantService.create(participant2);

        var sondage = new Sondage(null, "name", "description", dateInOneHour, false, null, null, participant);
        this.sondage = sondageService.create(this.participant.getParticipantId(), sondage);

        DateSondage dateSondage = new DateSondage(null, new Date(), this.sondage, new ArrayList<>());
        this.dateSondage = this.dateSondageService.create(this.sondage.getSondageId(), dateSondage);

        var dateSondee = new DateSondee(null, dateSondage, this.participant, Choix.DISPONIBLE);
        this.dateSondee = dateSondeeService.create(this.dateSondage.getDateSondageId(),
                this.participant.getParticipantId(), dateSondee);

        DateSondage dateSondage2 = new DateSondage(null, new Date(), this.sondage, new ArrayList<>());
        this.dateSondage2 = this.dateSondageService.create(this.sondage.getSondageId(), dateSondage2);

        var dateSondee2 = new DateSondee(null, this.dateSondage2, this.participant, Choix.DISPONIBLE);
        this.dateSondee2 = dateSondeeService.create(this.dateSondage2.getDateSondageId(),
                this.participant.getParticipantId(), dateSondee2);
    }

    @Test
    void createDateSondeeCreatedObjectIsReturnedOne() {
        var res = this.dateSondeeService.create(this.dateSondage.getDateSondageId(),
                this.participant.getParticipantId(), this.dateSondee);

        Assertions.assertEquals(res, this.dateSondee);
    }

    @Test
    void create2DateSondeesShouldNotBeSame() {
        var res = this.dateSondeeService.create(this.dateSondage.getDateSondageId(),
                this.participant.getParticipantId(), this.dateSondee);
        var res2 = this.dateSondeeService.create(this.dateSondage.getDateSondageId(),
                this.participant.getParticipantId(), this.dateSondee2);

        Assertions.assertNotEquals(res, res2);
    }

    @Test
    void createDateSondeeTwiceShouldBeSame() {
        var res = this.dateSondeeService.create(this.dateSondage.getDateSondageId(),
                this.participant.getParticipantId(), this.dateSondee);
        var res2 = this.dateSondeeService.create(this.dateSondage.getDateSondageId(),
                this.participant.getParticipantId(), this.dateSondee);

        Assertions.assertEquals(res, res2);
    }

    @Test
    void testBestDate() {
        // 2 votes for differents dates should return a list of size 2
        List<Date> dates = this.dateSondeeService.bestDate(this.sondage.getSondageId());
        Assertions.assertEquals(2, dates.size());

        // Create 2 PRESENT votes for the same date
        Date currentDate = new Date();

        DateSondage dateSondage = new DateSondage(null, currentDate, this.sondage, new ArrayList<>());
        dateSondage = this.dateSondageService.create(this.sondage.getSondageId(), dateSondage);

        var dateSondee = new DateSondee(null, dateSondage, this.participant, Choix.DISPONIBLE);
        dateSondee = dateSondeeService.create(dateSondage.getDateSondageId(),
                this.participant.getParticipantId(), dateSondee);

        DateSondage dateSondage2 = new DateSondage(null, currentDate, this.sondage, new ArrayList<>());
        dateSondage2 = this.dateSondageService.create(this.sondage.getSondageId(), dateSondage);

        var dateSondee2 = new DateSondee(null, dateSondage2, this.participant2, Choix.DISPONIBLE);
        dateSondee2 = dateSondeeService.create(dateSondage2.getDateSondageId(),
                this.participant2.getParticipantId(), dateSondee2);

        dates = this.dateSondeeService.bestDate(this.sondage.getSondageId());

        Assertions.assertEquals(1, dates.size());
        Assertions.assertTrue(dates.contains(currentDate));

        Assertions.assertEquals(0, this.dateSondeeService.bestDate(Long.MIN_VALUE).size());
    }

    @Test
    void testMaybeBestDate() {
        // 2 votes for differents dates should return a list of size 2
        List<Date> dates = this.dateSondeeService.maybeBestDate(this.sondage.getSondageId());
        Assertions.assertEquals(2, dates.size());

        // Create 2 MAYBE votes for the same date
        Date currentDate = new Date();

        DateSondage dateSondage = new DateSondage(null, currentDate, this.sondage, new ArrayList<>());
        dateSondage = this.dateSondageService.create(this.sondage.getSondageId(), dateSondage);

        var dateSondee = new DateSondee(null, dateSondage, this.participant, Choix.PEUTETRE);
        dateSondee = dateSondeeService.create(dateSondage.getDateSondageId(),
                this.participant.getParticipantId(), dateSondee);

        DateSondage dateSondage2 = new DateSondage(null, currentDate, this.sondage, new ArrayList<>());
        dateSondage2 = this.dateSondageService.create(this.sondage.getSondageId(), dateSondage);

        var dateSondee2 = new DateSondee(null, dateSondage2, this.participant2, Choix.PEUTETRE);
        dateSondee2 = dateSondeeService.create(dateSondage2.getDateSondageId(),
                this.participant2.getParticipantId(), dateSondee2);

        dates = this.dateSondeeService.maybeBestDate(this.sondage.getSondageId());

        Assertions.assertEquals(1, dates.size());
        Assertions.assertTrue(dates.contains(currentDate));

        Assertions.assertEquals(0, this.dateSondeeService.maybeBestDate(Long.MIN_VALUE).size());
    }
}
