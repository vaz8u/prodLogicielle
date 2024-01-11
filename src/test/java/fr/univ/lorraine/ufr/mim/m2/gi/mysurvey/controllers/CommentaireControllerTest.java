package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.controllers;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.CommentaireDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Commentaire;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.CommentaireService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.util.TestUtil.assertDto;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentaireControllerTest {
    @Mock
    private CommentaireService service;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentaireController controller;

    @Test
    @DisplayName("Test update Commentaire")
    void testUpdate() {
        Long commentaireId = 1L;
        CommentaireDto commentaireDto = new CommentaireDto();
        Commentaire expectedModel = new Commentaire();
        CommentaireDto expectedDto = new CommentaireDto();

        when(modelMapper.map(commentaireDto, Commentaire.class)).thenReturn(expectedModel);
        when(service.update(commentaireId, expectedModel)).thenReturn(expectedModel);
        when(modelMapper.map(expectedModel, CommentaireDto.class)).thenReturn(expectedDto);

        CommentaireDto result = controller.update(commentaireId, commentaireDto);

        assertDto(expectedDto, result);

        verify(modelMapper, times(1)).map(commentaireDto, Commentaire.class);
        verify(service, times(1)).update(commentaireId, expectedModel);
        verify(modelMapper, times(1)).map(expectedModel, CommentaireDto.class);
    }

    @Test
    @DisplayName("Test delete Commentaire")
    void testDelete() {
        Long commentaireId = 1L;

        controller.delete(commentaireId);

        verify(service, times(1)).delete(commentaireId);
    }
}
