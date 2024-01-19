package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.integration;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.ParticipantService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.SondageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
class SondageServiceIntegrationTests {

    @Autowired
    SondageService sondageService;

    @Autowired
    ParticipantService participantService;

    Participant participant;
    Sondage sondage;
    Sondage sondage2;

    @BeforeEach
    void createDataSample() {
        var participant = new Participant(null, "name", "fname");
        this.participant = participantService.create(participant);

        var sondage = new Sondage(null, "name", "description", new Date(), true, null, null, participant);
        this.sondage = sondageService.create(this.participant.getParticipantId(), sondage);

        var sondage2 = new Sondage(null, "name", "description", new Date(), true, null, null, participant);
        this.sondage2 = sondageService.create(this.participant.getParticipantId(), sondage2);
    }

    @Test
    void createParticpantCreatedObjectIsReturnedOne() {
        var res = this.sondageService.create(this.participant.getParticipantId(), this.sondage);

        Assertions.assertEquals(res, this.sondage);
    }

    @Test
    void create2ParticpantsShouldNotBeSame() {
        var res = this.sondageService.create(this.participant.getParticipantId(), this.sondage);
        var res2 = this.sondageService.create(this.participant.getParticipantId(), this.sondage2);

        Assertions.assertNotEquals(res, res2);
    }

    @Test
    void createParticpantTwiceShouldBeSame() {
        var res = this.sondageService.create(this.participant.getParticipantId(), this.sondage);
        var res2 = this.sondageService.create(this.participant.getParticipantId(), this.sondage);

        Assertions.assertEquals(res, res2);
    }

    @Test
    void editSondageByRefShouldBeSame() {
        var sondage2 = new Sondage();

        var res = this.sondageService.create(this.participant.getParticipantId(), this.sondage);

        var res2 = this.sondageService.update(res.getSondageId(), sondage2);

        Assertions.assertEquals(res, res2);
    }

    @Test
    void editSondageByValueShouldNotBeSame() {
        var res = this.sondageService.create(this.participant.getParticipantId(), this.sondage);

        var res2 = this.sondageService.create(this.participant.getParticipantId(), this.sondage2);

        res.setDescription(res2.getDescription());
        res = this.sondageService.update(res.getSondageId(), res);

        Assertions.assertNotEquals(res, res2);
    }

    @Test
    void editSondageNotFoundShouldBeNull() {
        var res = this.sondageService.update(Long.MIN_VALUE, new Sondage());
        Assertions.assertNull(res);
    }

    @Test
    void editCommentaireCannotAssertNull() {
        var res = this.sondageService.create(this.participant.getParticipantId(), this.sondage);

        Assertions.assertThrows(NullPointerException.class, () -> {
            this.sondageService.update(res.getSondageId(), null);
        });
    }

    @Test
    void deleteExistingCommentaireShouldWork() {
        var res = this.sondageService.create(this.participant.getParticipantId(), new Sondage());

        int deleted = this.sondageService.delete(res.getSondageId());

        Assertions.assertEquals(1, deleted);

        Assertions.assertEquals(0, this.sondageService.delete(res.getSondageId()));
    }

    @Test
    void deleteNotExistingCommentaireShouldNotWork() {
        Assertions.assertEquals(0, this.sondageService.delete(Long.MIN_VALUE));
    }

    @Test
    void deleteByNullShouldException() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.sondageService.delete(null);
        });
    }

    @Test
    void getByExistingIdShouldBeSame() {
        var res = this.sondageService.create(this.participant.getParticipantId(), this.sondage);

        Assertions.assertEquals(res, this.sondageService.getById(res.getSondageId()));
    }

    @Test
    void getByNotExistingIdShouldBeNull() {
        Assertions.assertNull(this.sondageService.getById(Long.MIN_VALUE));
    }

    @Test
    void getByNullShouldException() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.sondageService.getById(null);
        });
    }

    @Test
    void getAllContentShouldBeSame() {
        int beforeSize = this.sondageService.getAll().size();

        var sondages = List.of(new Sondage(), new Sondage());
        List<Sondage> res = new ArrayList<>();

        for (int i = 0; i < sondages.size(); i++) {
            res.add(i, this.sondageService.create(this.participant.getParticipantId(), sondages.get(i)));

            System.out.println(res.get(i));
        }

        Assertions.assertEquals(2, this.sondageService.getAll().size() - beforeSize);

        Assertions.assertTrue(this.sondageService.getAll().containsAll(res));
    }
}
