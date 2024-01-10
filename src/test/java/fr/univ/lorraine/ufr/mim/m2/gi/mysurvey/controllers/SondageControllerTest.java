package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.controllers;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.CommentaireDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.DateSondageDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.SondageDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Commentaire;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.DateSondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.CommentaireService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.DateSondageService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.SondageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SondageControllerTest {

    @Mock
    private SondageService sondageService;

    @Mock
    private CommentaireService scommentaire;

    @Mock
    private DateSondageService sdate;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private SondageController sondageController;

    @BeforeEach
    void setupMapper() {
        // Configuration du comportement du mapper mock
        lenient().when(mapper.map(Mockito.any(Sondage.class), Mockito.eq(SondageDto.class)))
            .thenAnswer(invocation -> {
                Sondage source = invocation.getArgument(0);
                SondageDto target = new SondageDto();
                target.setSondageId(source.getSondageId());
                target.setNom(source.getNom());
                // Map other properties if needed
                return target;
        });
    }

    private <T> void assertDto(T expectedDto, T actualDto) {
        assertNotNull(actualDto, "Result should not be null");
        assertEquals(expectedDto, actualDto, "DTOs should be equal");
    }

    private <T,U> void assertDtoListSize(List<T> expectedDtos, List<U> actualDtos) {
        assertNotNull(actualDtos, "Result should not be null");
        assertEquals(expectedDtos.size(), actualDtos.size(), "Number of Dtos should match");
    }

    @Test
    @DisplayName("Test get Sondage by Id")
    void testGetById() {
        Long sondageId = 1L;
        Sondage expectedModel = new Sondage();
        SondageDto expectedDto = new SondageDto();

        when(sondageService.getById(sondageId)).thenReturn(expectedModel);
        when(mapper.map(expectedModel, SondageDto.class)).thenReturn(expectedDto);

        SondageDto result = sondageController.get(sondageId);

        assertDto(expectedDto, result);

        verify(sondageService, times(1)).getById(sondageId);
        verify(mapper, times(1)).map(expectedModel, SondageDto.class);
    }

    @Test
    @DisplayName("Test get All Sondages")
    void testGetAll() {
        List<Sondage> expectedModels = Arrays.asList(new Sondage(), new Sondage(), new Sondage());

        when(sondageService.getAll()).thenReturn(expectedModels);

        List<SondageDto> result = sondageController.get();

        assertDtoListSize(expectedModels, result);

        verify(sondageService, times(1)).getAll();
        verify(mapper, times(expectedModels.size())).map(any(Sondage.class), eq(SondageDto.class));
    }

    @Test
    @DisplayName("Test get Commentaires for Sondage")
    void testGetCommentaires() {
        Long sondageId = 1L;
        String expectedComment = "Commentaire attendu";
        List<Commentaire> expectedModels = Arrays.asList(new Commentaire(), new Commentaire());
        expectedModels.get(0).setCommentaire(expectedComment);
        expectedModels.get(1).setCommentaire(expectedComment);
        List<CommentaireDto> expectedDtos = Arrays.asList(new CommentaireDto(), new CommentaireDto());

        when(scommentaire.getBySondageId(sondageId)).thenReturn(expectedModels);
        when(mapper.map(any(Commentaire.class), eq(CommentaireDto.class)))
                .thenAnswer(invocation -> {
                    Commentaire source = invocation.getArgument(0);
                    CommentaireDto target = new CommentaireDto();
                    target.setCommentaireId(source.getCommentaireId());
                    target.setCommentaire(source.getCommentaire());
                    return target;
                });

        List<CommentaireDto> result = sondageController.getCommentaires(sondageId);

        assertDtoListSize(expectedDtos, result);
        for (int i = 0; i < expectedDtos.size(); i++) {
            assertEquals(expectedModels.get(i).getCommentaire(), result.get(i).getCommentaire(), "Commentaire content should match");
        }

        verify(scommentaire, times(1)).getBySondageId(sondageId);
        verify(mapper, times(expectedDtos.size())).map(any(), eq(CommentaireDto.class));
    }

    @Test
    @DisplayName("Test get Dates for Sondage")
    void testGetDates() {
        Long sondageId = 1L;
        List<DateSondage> expectedModels = Arrays.asList(new DateSondage(), new DateSondage());
        List<DateSondageDto> expectedDtos = Arrays.asList(new DateSondageDto(), new DateSondageDto());

        when(sdate.getBySondageId(sondageId)).thenReturn(expectedModels);
        when(mapper.map(any(DateSondage.class), eq(DateSondageDto.class)))
                .thenAnswer(invocation -> {
                    DateSondage source = invocation.getArgument(0);
                    DateSondageDto target = new DateSondageDto();
                    target.setDateSondageId(source.getDateSondageId());
                    return target;
                });

        List<DateSondageDto> result = sondageController.getDates(sondageId);

        assertDtoListSize(expectedDtos, result);

        verify(sdate, times(1)).getBySondageId(sondageId);
        verify(mapper, times(expectedDtos.size())).map(any(), eq(DateSondageDto.class));
    }

    @Test
    @DisplayName("Test create Sondage")
    void testCreate() {
        Long expectedParticipantId = 1L;
        Sondage expectedModel = new Sondage();
        SondageDto sondageDto = new SondageDto();
        SondageDto expectedDto = new SondageDto();

        expectedDto.setCreateBy(expectedParticipantId);
        sondageDto.setCreateBy(expectedParticipantId);

        when(mapper.map(sondageDto, Sondage.class)).thenReturn(expectedModel);
        when(mapper.map(expectedModel, SondageDto.class)).thenReturn(expectedDto);
        when(sondageService.create(eq(expectedParticipantId), eq(expectedModel))).thenReturn(expectedModel);

        SondageDto result = sondageController.create(sondageDto);

        assertDto(expectedDto, result);
        assertEquals(expectedParticipantId, sondageDto.getCreateBy(), "CreateBy should match");

        verify(mapper, times(1)).map(sondageDto, Sondage.class);
        verify(mapper, times(1)).map(expectedModel, SondageDto.class);
        verify(sondageService, times(1)).create(eq(expectedParticipantId), eq(expectedModel));
    }

    @Test
    @DisplayName("Test create Commentaire for Sondage")
    void testCreateCommentaire() {
        Long sondageId = 1L;
        Long participantId = 2L;

        Participant participant = new Participant();
        participant.setParticipantId(participantId);
        CommentaireDto commentaireDto = new CommentaireDto();
        Commentaire expectedModel = new Commentaire();
        CommentaireDto expectedDto = new CommentaireDto();
        commentaireDto.setParticipant(participant.getParticipantId());
        expectedModel.setParticipant(participant);

        when(mapper.map(commentaireDto, Commentaire.class)).thenReturn(expectedModel);
        when(scommentaire.addCommantaire(sondageId, commentaireDto.getParticipant(), expectedModel))
                .thenReturn(expectedModel);
        when(mapper.map(expectedModel, CommentaireDto.class)).thenReturn(expectedDto);

        CommentaireDto result = sondageController.createCommantaire(sondageId, commentaireDto);

        assertDto(expectedDto, result);

        verify(mapper, times(1)).map(commentaireDto, Commentaire.class);
        verify(scommentaire, times(1)).addCommantaire(sondageId, commentaireDto.getParticipant(), expectedModel);
        verify(mapper, times(1)).map(expectedModel, CommentaireDto.class);
    }

    @Test
    @DisplayName("Test create DateSondage for Sondage")
    void testCreateDate() {
        Long sondageId = 1L;
        DateSondageDto dateSondageDto = new DateSondageDto();
        DateSondage expectedModel = new DateSondage();
        DateSondageDto expectedDto = new DateSondageDto();

        when(mapper.map(dateSondageDto, DateSondage.class)).thenReturn(expectedModel);
        when(sdate.create(sondageId, expectedModel)).thenReturn(expectedModel);
        when(mapper.map(expectedModel, DateSondageDto.class)).thenReturn(expectedDto);

        DateSondageDto result = sondageController.createDate(sondageId, dateSondageDto);

        assertDto(expectedDto, result);

        verify(mapper, times(1)).map(dateSondageDto, DateSondage.class);
        verify(sdate, times(1)).create(sondageId, expectedModel);
        verify(mapper, times(1)).map(expectedModel, DateSondageDto.class);
    }

    @Test
    @DisplayName("Test update Sondage")
    void testUpdate() {
        Long sondageId = 1L;
        SondageDto sondageDto = new SondageDto();
        Sondage expectedModel = new Sondage();
        SondageDto expectedDto = new SondageDto();

        sondageDto.setSondageId(sondageId);
        expectedModel.setSondageId(sondageId);

        when(mapper.map(sondageDto, Sondage.class)).thenReturn(expectedModel);
        when(mapper.map(expectedModel, SondageDto.class)).thenReturn(expectedDto);
        when(sondageService.update(sondageId, expectedModel)).thenReturn(expectedModel);

        SondageDto result = sondageController.update(sondageId, sondageDto);

        assertDto(expectedDto, result);
        assertEquals(sondageId, expectedModel.getSondageId(), "SondageId should match");

        verify(mapper, times(1)).map(sondageDto, Sondage.class);
        verify(mapper, times(1)).map(expectedModel, SondageDto.class);
        verify(sondageService, times(1)).update(sondageId, expectedModel);
    }
}
