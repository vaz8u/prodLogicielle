package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class CommentaireDtoTest {
    @Test
    @DisplayName("Test getCommentaireId method")
    void testGetCommentaireId() {
        CommentaireDto commentaireDto = new CommentaireDto();
        commentaireDto.setCommentaireId(1L);

        Long result = commentaireDto.getCommentaireId();

        assertEquals(1L, result);
    }

    @Test
    @DisplayName("Test equals method")
    void testEquals() {
        CommentaireDto commentaireDto = new CommentaireDto();
        CommentaireDto commentaireDto1 = new CommentaireDto();
        assertEquals(commentaireDto, commentaireDto1);

        commentaireDto.setCommentaireId(1L);
        commentaireDto.setCommentaire("Commentaire");
        CommentaireDto commentaireDto2 = new CommentaireDto();
        commentaireDto2.setCommentaireId(1L);
        commentaireDto2.setCommentaire("Commentaire");

        assertEquals(commentaireDto, commentaireDto2);

        commentaireDto2.setCommentaireId(2L);

        assertNotEquals(commentaireDto, commentaireDto2);

        commentaireDto2.setCommentaireId(1L);
        commentaireDto2.setCommentaire("Commentaire2");

        assertNotEquals(commentaireDto, commentaireDto2);

        commentaireDto2.setCommentaire("Commentaire");
        commentaireDto2.setParticipant(3L);

        assertNotEquals(commentaireDto, commentaireDto2);
    }

    @Test
    @DisplayName("Test hashCode method")
    void testHashCode() {
        CommentaireDto commentaireDto = new CommentaireDto();
        CommentaireDto commentaireDto1 = new CommentaireDto();

        assertEquals(commentaireDto.hashCode(), commentaireDto1.hashCode());
    }
}
