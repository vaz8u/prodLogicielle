package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class DateSondageDtoTest {
    @Test
    @DisplayName("Test getDateSondageId method")
    void testGetDateSondageId() {
        DateSondageDto dateSondageDto = new DateSondageDto();
        dateSondageDto.setDateSondageId(1L);
        Long result = dateSondageDto.getDateSondageId();
        assertEquals(1L, result);
    }

    @Test
    @DisplayName("Test get/set-Date method")
    void testGetSetDate() {
        DateSondageDto dateSondageDto = new DateSondageDto();
        Date date = new Date();
        dateSondageDto.setDate(date);
        assertEquals(date, dateSondageDto.getDate());
    }

    @Test
    @DisplayName("Test equals method")
    void testEquals() {
        DateSondageDto dateSondageDto = new DateSondageDto();
        DateSondageDto dateSondageDto1 = new DateSondageDto();
        assertEquals(dateSondageDto, dateSondageDto1);

        dateSondageDto.setDateSondageId(1L);
        dateSondageDto.setDate(null);
        DateSondageDto dateSondageDto2 = new DateSondageDto();
        dateSondageDto2.setDateSondageId(1L);
        dateSondageDto2.setDate(new Date());

        assertNotEquals(dateSondageDto, dateSondageDto2);

        dateSondageDto2.setDateSondageId(2L);

        assertNotEquals(dateSondageDto, dateSondageDto2);
    }

    @Test
    @DisplayName("Test hashCode method")
    void testHashCode() {
        DateSondageDto dateSondageDto = new DateSondageDto();
        int result = dateSondageDto.hashCode();

        assertEquals(961, result, result);
    }
}
