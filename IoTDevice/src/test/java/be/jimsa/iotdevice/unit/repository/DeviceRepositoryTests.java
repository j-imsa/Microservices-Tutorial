package be.jimsa.iotdevice.unit.repository;

import be.jimsa.iotdevice.ws.model.entity.DeviceEntity;
import be.jimsa.iotdevice.ws.repository.DeviceRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@DataJpaTest
class DeviceRepositoryTests {

    @Autowired
    private DeviceRepository deviceRepository;

    private String deviceName;
    private String devicePublicId;
    private int devicePorts;

    @BeforeEach
    void setUp() {
        deviceName = "rock";
        devicePublicId = "sdgfdgdf";
        devicePorts = 16;

        deviceRepository.deleteAll();
    }

    @Nested
    @DisplayName("Save")
    class SaveTests {

        @Test
        @DisplayName("a valid dto as an input, should return a valid saved dto")
        void testAValidDeviceDto_whenSave_ShouldReturnAValidSavedDeviceDto() {
            // given
            DeviceEntity deviceEntity = DeviceEntity.builder()
                    .name(deviceName)
                    .ports(devicePorts)
                    .publicId(devicePublicId)
                    .build();
            // when
            DeviceEntity savedDeviceEntity = deviceRepository.save(deviceEntity);

            // then
            assertThat(savedDeviceEntity)
                    .isNotNull()
                    .isInstanceOf(DeviceEntity.class)
                    .hasFieldOrPropertyWithValue("name", deviceName)
                    .hasFieldOrPropertyWithValue("ports", devicePorts)
                    .hasFieldOrPropertyWithValue("publicId", devicePublicId);
            assertThat(savedDeviceEntity.getId()).isPositive();

            savedDeviceEntity.setPublicId(savedDeviceEntity.getPublicId() + savedDeviceEntity.getPublicId());
            assertTimeout(Duration.ofMillis(10), () -> deviceRepository.save(savedDeviceEntity));
        }

        @RepeatedTest(100)
        @DisplayName("a valid dto as an input, should save less than 10 ms")
        void testAValidDeviceDto_whenSave_ShouldSaveLessThan10Ms() {
            // given
            DeviceEntity deviceEntity = DeviceEntity.builder()
                    .name(deviceName)
                    .ports(devicePorts)
                    .publicId(devicePublicId)
                    .build();

            // when/then
            assertTimeout(Duration.ofMillis(10), () -> deviceRepository.save(deviceEntity));
            assertThat(deviceRepository.count()).isEqualTo(1);
        }

        @ParameterizedTest
        @DisplayName("an invalid dto with ID, should ignore my id, then return a valid saved dto")
        @ValueSource(longs = {Long.MIN_VALUE, -100, -1, 0, 2, 100, Long.MAX_VALUE})
        void testAnInvalidDeviceDtoUsingId_whenSave_thenShouldReturnAValidSavedDeviceDto(long id) {
            DeviceEntity deviceEntity = DeviceEntity.builder()
                    .name(deviceName)
                    .ports(devicePorts)
                    .publicId(devicePublicId)
                    .id(id)
                    .build();

            DeviceEntity savedDeviceEntity = deviceRepository.save(deviceEntity);
            System.out.println("Saved ID: " + savedDeviceEntity.getId());

            assertThat(savedDeviceEntity.getId()).isNotEqualTo(deviceEntity.getId());

        }

        @Test
        @DisplayName("an invalid dto with null publicId, should throw DataIntegrityViolationException exception")
        void testAnInvalidDeviceDtoWithNullPublicId_whenSave_ShouldThrowDataIntegrityViolationException() {
            DeviceEntity deviceEntity = DeviceEntity.builder()
                    .name(deviceName)
                    .ports(devicePorts)
                    .build();

            assertThatThrownBy(() -> deviceRepository.save(deviceEntity))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("not-null property references");

        }

        @Test
        @DisplayName("invalid dtos with same publicId, should throw XXX exception")
        void testInvalidDeviceDtosWithSamePublicId_whenSave_ShouldThrowXXX() {
            DeviceEntity deviceEntity1 = DeviceEntity.builder()
                    .publicId(devicePublicId)
                    .name(deviceName)
                    .ports(devicePorts)
                    .build();
            DeviceEntity deviceEntity2 = DeviceEntity.builder()
                    .publicId(devicePublicId)
                    .name("Alpha")
                    .ports(500)
                    .build();

            DeviceEntity firstSavedDeviceEntity = deviceRepository.save(deviceEntity1);

            assertThat(firstSavedDeviceEntity)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("publicId", devicePublicId)
                    .hasFieldOrPropertyWithValue("name", deviceName)
                    .hasFieldOrPropertyWithValue("ports", devicePorts);
            assertThat(firstSavedDeviceEntity.getId()).isPositive();
            assertThatThrownBy(() -> deviceRepository.save(deviceEntity2))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("could not execute statement [Unique index");

        }


    }


}
