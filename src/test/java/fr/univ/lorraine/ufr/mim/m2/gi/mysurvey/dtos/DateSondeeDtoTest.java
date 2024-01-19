package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class DateSondeeDtoTest {
    @Test
    @DisplayName("Test get/set-DateSondeeId method")
    void testGetSetDateSondeeId() {
        DateSondeeDto dateSondeeDto = new DateSondeeDto();
        dateSondeeDto.setDateSondeeId(1L);

        Long result = dateSondeeDto.getDateSondeeId();

        assertEquals(1L, result);
    }

    @Test
    @DisplayName("Test get/set-choix method")
    void testGetSetChoix() {
        String choix = "choix";
        DateSondeeDto dateSondeeDto = new DateSondeeDto();
        dateSondeeDto.setChoix(choix);

        String result = dateSondeeDto.getChoix();

        assertEquals(choix, result);
    }

    @Test
    @DisplayName("Test equals method")
    void testEquals() {
        DateSondeeDto dateSondeeDto = new DateSondeeDto();
        DateSondeeDto dateSondeeDto1 = new DateSondeeDto();
        assertEquals(dateSondeeDto, dateSondeeDto1);

        dateSondeeDto.setDateSondeeId(1L);
        dateSondeeDto.setChoix("choix");

        DateSondeeDto dateSondeeDto2 = new DateSondeeDto();
        dateSondeeDto2.setDateSondeeId(1L);
        dateSondeeDto2.setChoix("choix");
        dateSondeeDto2.setParticipant(3L);

        assertNotEquals(dateSondeeDto, dateSondeeDto2);

        dateSondeeDto2.setDateSondeeId(2L);
        dateSondeeDto2.setParticipant(null);

        assertNotEquals(dateSondeeDto, dateSondeeDto2);

        dateSondeeDto2.setDateSondeeId(1L);
        dateSondeeDto2.setChoix("choix2");

        assertNotEquals(dateSondeeDto, dateSondeeDto2);
    }

    @Test
    @DisplayName("Test hashCode method")
    void testHashCode() {
        DateSondeeDto dateSondeeDto = new DateSondeeDto();
        int result = dateSondeeDto.hashCode();

        assertEquals(961, result, result);
    }
}
