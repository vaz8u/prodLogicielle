package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.util;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestUtil {
    public static <T> void assertDto(T expectedDto, T actualDto) {
        assertNotNull(actualDto, "Result should not be null");
        assertEquals(expectedDto, actualDto, "DTOs should be equal");
    }

    public static <T, U> void assertDtoListSize(List<T> expectedDtos, List<U> actualDtos) {
        assertNotNull(actualDtos, "Result should not be null");
        assertEquals(expectedDtos.size(), actualDtos.size(), "Number of Dtos should match");
    }
}
