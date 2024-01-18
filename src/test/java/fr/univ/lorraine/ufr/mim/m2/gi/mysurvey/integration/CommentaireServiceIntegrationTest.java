package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.integration;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Commentaire;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.CommentaireService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.ParticipantService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.SondageService;

@SpringBootTest
@Transactional
class CommentaireServiceIntegrationTest {

    @Autowired
    CommentaireService commentaireService;

    @Autowired
    ParticipantService participantService;

    @Autowired
    SondageService sondageService;

    Participant participant;
    Sondage sondage;

    @BeforeEach
    void createDataSample() {
        var participant = new Participant(null, "name", "fname");
        this.participant = participantService.create(participant);

        var sondage = new Sondage(null, "name", "description", new Date(), true, null, null, participant);
        this.sondage = sondageService.create(this.participant.getParticipantId(), sondage);
    }

    @Test
    void addCommentaireCreatedObjectIsReturnedOne() {
        var commentaire = new Commentaire();

        var res = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), commentaire);

        Assertions.assertEquals(res, commentaire);

        commentaire.setCommentaire("commentaire");
        res = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), commentaire);

        Assertions.assertEquals(res, commentaire);
    }

    @Test
    void add2CommentsShouldNotBeSame() {
        var commentaire = new Commentaire();
        commentaire.setCommentaire("commentaire");

        var commentaire2 = new Commentaire();
        commentaire2.setCommentaire("commentaire");

        var res = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), commentaire);

        var res2 = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), commentaire2);

        Assertions.assertNotEquals(res, res2);
    }

    @Test
    void addCommentTwiceShouldBeSame() {
        var commentaire = new Commentaire();

        var res = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), commentaire);

        var res2 = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), commentaire);

        Assertions.assertEquals(res, res2);
    }

    @Test
    void editCommentaireByRefShouldBeSame() {
        var commentaire = new Commentaire();
        var commentaire2 = new Commentaire();

        var res = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), commentaire);

        var res2 = this.commentaireService.update(res.getCommentaireId(), commentaire2);

        Assertions.assertEquals(res, res2);
    }

    @Test
    void editCommentaireByValueShouldNotBeSame() {
        var commentaire = new Commentaire();

        var commentaire2 = new Commentaire();
        commentaire2.setCommentaire("azerty");

        var res = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), commentaire);

        var res2 = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), commentaire2);

        res.setCommentaire(res2.getCommentaire());
        res = this.commentaireService.update(res.getCommentaireId(), res);

        Assertions.assertNotEquals(res, commentaire2);
    }

    @Test
    void editCommentaireNotFoundShouldBeNull() {
        var res = this.commentaireService.update(Long.MAX_VALUE, new Commentaire());
        Assertions.assertEquals(null, res);
    }

    @Test
    void editCommentaireCannotAssertNull() {
        var commentaire = new Commentaire();

        var res = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), commentaire);
        Assertions.assertThrows(NullPointerException.class, () -> {
            this.commentaireService.update(res.getCommentaireId(), null);
        });
    }

    void deleteExistingCommentaireShouldWork() {
        var res = this.commentaireService.addCommantaire(this.sondage.getSondageId(),
                this.participant.getParticipantId(), new Commentaire());

        int deleted = this.commentaireService.delete(res.getCommentaireId());

        Assertions.assertEquals(1, deleted);

        Assertions.assertEquals(0, this.commentaireService.delete(res.getCommentaireId()));
    }

    void deleteNotExistingCommentaireShouldNotWork() {
        Assertions.assertEquals(0, this.commentaireService.delete(Long.MAX_VALUE));
    }

    void deleteByNullShouldException() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            this.commentaireService.delete(null);
        });
    }

}
