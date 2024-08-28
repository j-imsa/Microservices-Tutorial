package be.jimsa.iotproject.utility;

import be.jimsa.iotproject.utility.id.PublicIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PublicIdGeneratorTests {

    private PublicIdGenerator publicIdGenerator;
    private static final String CHARACTERS_REGEX = "[A-Za-z0-9_\\-]+";
    private static final int DEFAULT_LENGTH = 64;
    private static final int MIN = 32;
    private static final int MAX = 512;

    @BeforeEach
    void setUp() {
        publicIdGenerator = new PublicIdGenerator();
    }

    @Test
    @DisplayName("testGeneratePublicIdPublicIdGenerator_whitAValidData_shouldReturnAValidString")
    void givenAValidLength_whenGeneratePublicId_thenShouldReturnAValidString() {
        int length = 128;
        String result = publicIdGenerator.generatePublicId(length);
        assertThat(result)
                .hasSize(length)
                .matches(CHARACTERS_REGEX);
    }

    @Test
    @DisplayName("testGeneratePublicIdPublicIdGenerator_whitANegativeData_shouldReturnAValidStringWithDefaultLength")
    void givenANegativeLength_whenGeneratePublicId_thenShouldReturnAValidStringWithDefaultLength() {
        int length = -1024;
        String result = publicIdGenerator.generatePublicId(length);
        assertThat(result)
                .hasSize(DEFAULT_LENGTH)
                .matches(CHARACTERS_REGEX);
    }

    @Test
    @DisplayName("testGeneratePublicIdPublicIdGenerator_whitABigLength_shouldReturnAValidStringWithDefaultLength")
    void givenABigLength_whenGeneratePublicId_thenShouldReturnAValidStringWithDefaultLength() {
        int length = 1024;
        String result = publicIdGenerator.generatePublicId(length);
        assertThat(result)
                .hasSize(DEFAULT_LENGTH)
                .matches(CHARACTERS_REGEX);
    }

    @Test
    @DisplayName("testGeneratePublicIdPublicIdGenerator_whitAMinLength_shouldReturnAValidStringWithDefaultLength")
    void givenAMinLength_whenGeneratePublicId_thenShouldReturnAValidStringWithDefaultLength() {
        int length = MIN;
        String result = publicIdGenerator.generatePublicId(length);
        assertThat(result)
                .hasSize(MIN)
                .matches(CHARACTERS_REGEX);
    }

    @Test
    @DisplayName("testGeneratePublicIdPublicIdGenerator_whitAMaxLength_shouldReturnAValidStringWithDefaultLength")
    void givenAMaxLength_whenGeneratePublicId_thenShouldReturnAValidStringWithDefaultLength() {
        int length = MAX;
        String result = publicIdGenerator.generatePublicId(length);
        assertThat(result)
                .hasSize(MAX)
                .matches(CHARACTERS_REGEX);
    }

    @Test
    @DisplayName("testGeneratePublicIdPublicIdGenerator_whitAValidLengthFor100Times_shouldReturnAValidStringFor100Times")
    void givenMultipleIdsGenerated_whenGeneratePublicId_thenShouldBeUnique() {
        Set<String> generatedIds = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            generatedIds.add(publicIdGenerator.generatePublicId(DEFAULT_LENGTH));
        }
        assertThat(generatedIds).hasSize(1000);
    }

    @Test
    @DisplayName("testGeneratePublicId_whitARangeOfLength_shouldReturnAValidStringWithoutNull")
    void givenAnyLength_whenGeneratePublicId_thenShouldNeverReturnNull() {
        int[] lengths = {-100, 0, 1, MIN, 128, MAX, 1024};

        for (int length : lengths) {
            String result = publicIdGenerator.generatePublicId(length);
            assertThat(result).isNotNull();
        }
    }


}
