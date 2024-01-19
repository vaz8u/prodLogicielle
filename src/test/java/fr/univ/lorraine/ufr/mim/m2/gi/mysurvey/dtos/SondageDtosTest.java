package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class SondageDtosTest {
    @Test
    @DisplayName("Test getSondageId method")
    void testGetSondageId() {
        SondageDto sondageDto = new SondageDto();
        sondageDto.setSondageId(1L);

        Long result = sondageDto.getSondageId();

        assert result == 1L;
    }

    @Test
    @DisplayName("Test getNom method")
    void testGetNom() {
        SondageDto sondageDto = new SondageDto();
        sondageDto.setNom("Nom");

        String result = sondageDto.getNom();

        assert result.equals("Nom");
    }

    @Test
    @DisplayName("Test get/set-Description method")
    void testGetSetDescription() {
        SondageDto sondageDto = new SondageDto();
        sondageDto.setDescription("Description");

        String result = sondageDto.getDescription();

        assert result.equals("Description");
    }

    @Test
    @DisplayName("Test get/set-Fin method")
    void testGetSetFin() {
        SondageDto sondageDto = new SondageDto();
        Date date = new Date();
        sondageDto.setFin(date);

        assertEquals(date, sondageDto.getFin());
    }

    @Test
    @DisplayName("Test get/set-Cloture method")
    void testGetSetCloture() {
        SondageDto sondageDto = new SondageDto();
        sondageDto.setCloture(true);

        assert sondageDto.getCloture();
    }

    @Test
    @DisplayName("Test equals method")
    void testEquals() {
        SondageDto sondageDto = new SondageDto();
        sondageDto.setSondageId(1L);
        sondageDto.setNom("Nom");
        sondageDto.setDescription("Description");
        sondageDto.setFin(null);
        sondageDto.setCloture(true);
        sondageDto.setCreateBy(1L);

        SondageDto sondageDto2 = new SondageDto();
        sondageDto2.setSondageId(1L);
        sondageDto2.setNom("Nom");
        sondageDto2.setDescription("Description");
        sondageDto2.setFin(null);
        sondageDto2.setCloture(true);
        sondageDto2.setCreateBy(1L);

        assertEquals(sondageDto, sondageDto2);

        sondageDto2.setSondageId(2L);

        assertNotEquals(sondageDto, sondageDto2);

        sondageDto2.setSondageId(1L);
        sondageDto2.setNom("Nom2");

        assertNotEquals(sondageDto, sondageDto2);

        sondageDto2.setNom("Nom");
        sondageDto2.setDescription("Description2");

        assertNotEquals(sondageDto, sondageDto2);

        sondageDto2.setDescription("Description");
        sondageDto2.setFin(new Date());

        assertNotEquals(sondageDto, sondageDto2);

        sondageDto2.setFin(null);
        sondageDto2.setCloture(false);

        assertNotEquals(sondageDto, sondageDto2);

        sondageDto2.setCloture(true);
        sondageDto2.setCreateBy(2L);

        assertNotEquals(sondageDto, sondageDto2);
    }

    @Test
    @DisplayName("Test hashCode method")
    void testHashCode() {
        SondageDto sondageDto = new SondageDto();
        int result = sondageDto.hashCode();
        assertEquals(887503681, result, "hashCode should be 887503681 "+result);
    }
}
