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
        sondageDto.setFin(null);

        assert sondageDto.getFin() == null;
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

        SondageDto sondageDto3 = new SondageDto();
        sondageDto3.setSondageId(1L);
        sondageDto3.setNom("Nom");
        sondageDto3.setDescription("Description");
        sondageDto3.setFin(null);
        sondageDto3.setCloture(false);//
        sondageDto3.setCreateBy(1L);

        assertNotEquals(sondageDto, sondageDto3);

        SondageDto sondageDto4 = new SondageDto();
        sondageDto4.setSondageId(1L);
        sondageDto4.setNom("no");//
        sondageDto4.setDescription("Description");
        sondageDto4.setFin(null);
        sondageDto4.setCloture(true);
        sondageDto4.setCreateBy(1L);

        assertNotEquals(sondageDto, sondageDto4);

        SondageDto sondageDto5 = new SondageDto();
        sondageDto5.setSondageId(2L);//
        sondageDto5.setNom("Nom");
        sondageDto5.setDescription("Description");
        sondageDto5.setFin(null);
        sondageDto5.setCloture(true);
        sondageDto5.setCreateBy(1L);

        assertNotEquals(sondageDto, sondageDto5);

        SondageDto sondageDto6 = new SondageDto();
        sondageDto6.setSondageId(1L);
        sondageDto6.setNom("Nom");
        sondageDto6.setDescription("Descriptio");//
        sondageDto6.setFin(null);
        sondageDto6.setCloture(true);
        sondageDto6.setCreateBy(1L);

        assertNotEquals(sondageDto, sondageDto6);

        SondageDto sondageDto7 = new SondageDto();
        sondageDto7.setSondageId(1L);
        sondageDto7.setNom("Nom");
        sondageDto7.setDescription("Description");
        sondageDto7.setFin(new Date());//
        sondageDto7.setCloture(true);
        sondageDto7.setCreateBy(1L);

        assertNotEquals(sondageDto, sondageDto7);

        SondageDto sondageDto8 = new SondageDto();
        sondageDto8.setSondageId(1L);
        sondageDto8.setNom("Nom");
        sondageDto8.setDescription("Description");
        sondageDto8.setFin(null);
        sondageDto8.setCloture(false);
        sondageDto8.setCreateBy(2L);//

        assertNotEquals(sondageDto, sondageDto8);
    }

    @Test
    @DisplayName("Test hashCode method")
    void testHashCode() {
        SondageDto sondageDto = new SondageDto();
        int result = sondageDto.hashCode();
        assertEquals(887503681, result, "hashCode should be 887503681 "+result);
    }
}
