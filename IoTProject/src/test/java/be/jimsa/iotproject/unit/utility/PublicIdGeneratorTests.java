package be.jimsa.iotproject.unit.utility;

import be.jimsa.iotproject.utility.id.PublicIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PublicIdGeneratorTests {

    private PublicIdGenerator publicIdGenerator;

    @BeforeEach
    void setUp() {
        publicIdGenerator = new PublicIdGenerator();
    }


}
