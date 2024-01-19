package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.integration;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.ParticipantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class ParticipantServiceIntegrationTests {

    @Autowired
    ParticipantService participantService;

    @Test
    void createParticpantCreatedObjectIsReturnedOne() {
        var participant = new Participant(null, "name", "fname");

        var res = this.participantService.create(participant);

        Assertions.assertEquals(res, participant);
    }

    @Test
    void create2ParticpantsShouldNotBeSame() {
        var participant = new Participant(null, "name", "fname");
        var participant2 = new Participant(null, "name", "fname");

        var res = this.participantService.create(participant);
        var res2 = this.participantService.create(participant2);

        Assertions.assertNotEquals(res, res2);
    }

    @Test
    void createParticpantTwiceShouldBeSame() {
        var participant = new Participant(null, "name", "fname");

        var res = this.participantService.create(participant);
        var res2 = this.participantService.create(participant);

        Assertions.assertEquals(res, res2);
    }

    @Test
    void editParticipantByRefShouldBeSame() {
        var participant = new Participant(null, "name", "fname");
        var participant2 = new Participant(null, "name", "fname");

        var res = this.participantService.create(participant);

        var res2 = this.participantService.update(res.getParticipantId(), participant2);

        Assertions.assertEquals(res, res2);
    }

    @Test
    void editParticipantByValueShouldNotBeSame() {
        var participant = new Participant(null, "name", "fname");
        var participant2 = new Participant(null, "name", "fname");

        var res = this.participantService.create(participant);

        var res2 = this.participantService.create(participant2);

        res.setCommentaire(res2.getCommentaire());
        res = this.participantService.update(res.getParticipantId(), res);

        Assertions.assertNotEquals(res, res2);
    }

    @Test
    void editParticipantNotFoundShouldBeNull() {
        var res = this.participantService.update(Long.MIN_VALUE, new Participant(null, "name", "fname"));
        Assertions.assertNull(res);
    }

    @Test
    void editCommentaireCannotAssertNull() {
        var participant = new Participant(null, "name", "fname");

        var res = this.participantService.create(participant);
        Assertions.assertThrows(NullPointerException.class, () -> {
            this.participantService.update(res.getParticipantId(), null);
        });
    }

    @Test
    void deleteExistingCommentaireShouldWork() {
        var res = this.participantService.create(new Participant(null, "name", "fname"));

        int deleted = this.participantService.delete(res.getParticipantId());

        Assertions.assertEquals(1, deleted);

        Assertions.assertEquals(0, this.participantService.delete(res.getParticipantId()));
    }

    @Test
    void deleteNotExistingCommentaireShouldNotWork() {
        Assertions.assertEquals(0, this.participantService.delete(Long.MIN_VALUE));
    }

    @Test
    void deleteByNullShouldException() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.participantService.delete(null);
        });
    }

    @Test
    void getByExistingIdShouldBeSame() {
        var participant = new Participant(null, "name", "fname");

        var res = this.participantService.create(participant);

        Assertions.assertEquals(res, this.participantService.getById(res.getParticipantId()));
    }

    @Test
    void getByNotExistingIdShouldBeNull() {
        Assertions.assertNull(this.participantService.getById(Long.MIN_VALUE));
    }

    @Test
    void getByNullShouldException() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.participantService.getById(null);
        });
    }

    @Test
    void getAllContentShouldBeSame() {
        int beforeSize = this.participantService.getAll().size();

        var participants = List.of(new Participant(null, "name", "fname"), new Participant(null, "name", "fname"));
        List<Participant> res = new ArrayList<>();

        for (int i = 0; i < participants.size(); i++) {
            res.add(i, this.participantService.create(participants.get(i)));

            System.out.println(res.get(i));
        }

        Assertions.assertEquals(2, this.participantService.getAll().size() - beforeSize);

        Assertions.assertTrue(this.participantService.getAll().containsAll(res));
    }
}
