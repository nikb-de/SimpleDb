package org.example;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ParameterizedTestsExample {

    // Test with CSV Source
    @ParameterizedTest
    @CsvSource({
            "1, 2, 3",
            "2, 3, 5",
            "5, 5, 10"
    })
    void testSumWithCsvSource(int a, int b, int expectedSum) {
        assertEquals(expectedSum, a + b);
    }

    // Test with Value Source
    @ParameterizedTest
    @ValueSource(strings = {"hello", "world"})
    void testStringLengthWithValueSource(String input) {
        assertFalse(input.isEmpty());
    }

    // Test with Method Source
    @ParameterizedTest
    @MethodSource("provideStringsForTesting")
    void testUpperCaseWithMethodSource(String input, String expected) {
        assertEquals(expected, input.toUpperCase());
    }

    // Method Source for testUpperCaseWithMethodSource
    private static Stream<Arguments> provideStringsForTesting() {
        return Stream.of(
                Arguments.of("test", "TEST"),
                Arguments.of("junit", "JUNIT"),
                Arguments.of("java", "JAVA")
        );
    }
}