package com.zeafen.LocNetMonitoring;

import com.zeafen.LocNetMonitoring.data.repositories.BufferRepository;
import com.zeafen.LocNetMonitoring.domain.models.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class BufferRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BufferRepository bufferRepository;


    private Machine testMachine1;
    private Machine testMachine2;
    private Maintenance testMaintenance1;
    private Maintenance testMaintenance2;

    private OffsetDateTime baseDate;
    private OffsetDateTime dateBefore;
    private OffsetDateTime dateAfter;

    @BeforeEach
    public void setUp() {
        baseDate = OffsetDateTime.of(2024, 1, 15, 10, 0, 0, 0, ZoneOffset.UTC);
        dateBefore = baseDate.minusDays(5);
        dateAfter = baseDate.plusDays(5);

        // Создание тестовых устройств
        MachineType machineType = new MachineType();
        machineType.setName("Lorem ipsum type");
        entityManager.persist(machineType);

        MachineModel machineModel = new MachineModel();
        machineModel.setName("Lorem ipsum model");
        machineModel.setYearsExpire((short) 50);
        machineModel.setType(machineType);
        entityManager.persist(machineModel);

        testMachine1 = new Machine();
        testMachine1.setId(1);
        testMachine1.setName("Machine 1");
        testMachine1.setAddress("255.255.255.255");
        testMachine1.setModel(machineModel);
        testMachine1.setDateProduced(baseDate);
        testMachine1.setDateCommissioning(dateAfter);
        testMachine1.setSerialNumber(123451);
        entityManager.persist(testMachine1);

        testMachine2 = new Machine();
        testMachine2.setId(2);
        testMachine2.setName("Machine 2");
        testMachine2.setAddress("255.255.255.255");
        testMachine2.setModel(machineModel);
        testMachine2.setDateProduced(baseDate);
        testMachine2.setDateCommissioning(dateAfter);
        testMachine2.setSerialNumber(123452);
        entityManager.persist(testMachine2);

        // Создание тестовых ТО
        MaintenanceType maintenanceType1 = new MaintenanceType();
        maintenanceType1.setName("Maintenance 1");
        maintenanceType1.setPeriod("1 month");
        entityManager.persist(maintenanceType1);

        MaintenanceType maintenanceType2 = new MaintenanceType();
        maintenanceType2.setName("Maintenance 2");
        maintenanceType2.setPeriod("2 month");
        entityManager.persist(maintenanceType2);


        testMaintenance1 = new Maintenance();
        testMaintenance1.setId(1);
        testMaintenance1.setWorkDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        testMaintenance1.setMaintenanceType(maintenanceType1);
        entityManager.persist(testMaintenance1);

        testMaintenance2 = new Maintenance();
        testMaintenance2.setId(2);
        testMaintenance2.setWorkDescription("Lorem ipsum dolor sit amet");
        testMaintenance2.setMaintenanceType(maintenanceType2);
        entityManager.persist(testMaintenance2);

        // Создание тестовых данных Buffer
        createTestBuffers();
    }

    private void createTestBuffers() {
        // Buffer 1: machine1, maintenance1, date=baseDate, isRead=false, type=1
        Buffer buffer1 = new Buffer();
        buffer1.setMachine(testMachine1);
        buffer1.setMaintenance(testMaintenance1);
        buffer1.setDateGenerated(baseDate);
        buffer1.setRead(false);
        buffer1.setBufferType((short) 1);
        entityManager.persist(buffer1);

        // Buffer 2: machine1, maintenance2, date=dateAfter, isRead=true, type=1
        Buffer buffer2 = new Buffer();
        buffer2.setMachine(testMachine1);
        buffer2.setMaintenance(testMaintenance2);
        buffer2.setDateGenerated(dateAfter);
        buffer2.setRead(true);
        buffer2.setBufferType((short) 1);
        entityManager.persist(buffer2);

        // Buffer 3: machine2, null maintenance, date=dateBefore, isRead=false, type=2
        Buffer buffer3 = new Buffer();
        buffer3.setMachine(testMachine2);
        buffer3.setMaintenance(null);
        buffer3.setDateGenerated(dateBefore);
        buffer3.setRead(false);
        buffer3.setBufferType((short) 2);
        entityManager.persist(buffer3);

        // Buffer 4: machine2, maintenance1, date=baseDate, isRead=false, type=2
        Buffer buffer4 = new Buffer();
        buffer4.setMachine(testMachine2);
        buffer4.setMaintenance(testMaintenance1);
        buffer4.setDateGenerated(baseDate);
        buffer4.setRead(false);
        buffer4.setBufferType((short) 2);
        entityManager.persist(buffer4);

        // Buffer 5: machine1, maintenance1, date=dateBefore, isRead=true, type=1
        Buffer buffer5 = new Buffer();
        buffer5.setMachine(testMachine1);
        buffer5.setMaintenance(testMaintenance1);
        buffer5.setDateGenerated(dateBefore);
        buffer5.setRead(true);
        buffer5.setBufferType((short) 1);
        entityManager.persist(buffer5);
    }

    @Test
    void testFindAllFiltered_WithAllNullParameters_ShouldReturnAllRecords() {
        // When
        Page<Buffer> result = bufferRepository.findAllFiltered(
                null, null, null, null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getTotalElements()).isEqualTo(5);
    }

    @Test
    public void testFindAllFiltered_WithMachineIdFilter_ShouldReturnFilteredResults() {
        // When
        Page<Buffer> result = bufferRepository.findAllFiltered(
                testMachine1.getId(), null, null, null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .allMatch(buffer -> buffer.getMachine().getId().equals(testMachine1.getId()));
    }

    @Test
    public void testFindAllFiltered_WithMaintenanceIdFilter_ShouldReturnFilteredResults() {
        // When
        Page<Buffer> result = bufferRepository.findAllFiltered(
                null, testMaintenance1.getId(), null, null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .allMatch(buffer ->
                        buffer.getMaintenance() != null &&
                                buffer.getMaintenance().getId().equals(testMaintenance1.getId())
                );
    }

    @Test
    public void testFindAllFiltered_WithDateFromFilter_ShouldReturnRecordsAfterDate() {

        // When
        Page<Buffer> result = bufferRepository.findAllFiltered(
                null, null, baseDate, null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .allMatch(buffer ->
                        buffer.getDateGenerated().isEqual(baseDate) ||
                                buffer.getDateGenerated().isAfter(baseDate)
                );
    }

    @Test
    public void testFindAllFiltered_WithDateUntilFilter_ShouldReturnRecordsBeforeDate() {
        // When
        Page<Buffer> result = bufferRepository.findAllFiltered(
                null, null, null, baseDate, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(4);
        assertThat(result.getContent())
                .allMatch(buffer ->
                        buffer.getDateGenerated().isEqual(baseDate) ||
                                buffer.getDateGenerated().isBefore(baseDate)
                );
    }

    @Test
    public void testFindAllFiltered_WithDateRangeFilter_ShouldReturnRecordsInRange() {
        //Given
        var dateFrom = baseDate.minusDays(1);
        var dateUntil = baseDate.plusDays(1);

        // When
        Page<Buffer> result = bufferRepository.findAllFiltered(
                null, null, dateFrom, dateUntil, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .allMatch(buffer ->
                        !buffer.getDateGenerated().isBefore(dateFrom) &&
                                !buffer.getDateGenerated().isAfter(dateUntil)
                );
    }

    @Test
    public void testFindAllFiltered_WithIsReadFilter_ShouldReturnFilteredResults() {
        // When
        Page<Buffer> result = bufferRepository.findAllFiltered(
                null, null, null, null, false, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .allMatch(buffer -> buffer.getRead().equals(false));
    }

    @Test
    public void testFindAllFiltered_WithBufferTypeFilter_ShouldReturnFilteredResults() {
        // When
        Page<Buffer> result = bufferRepository.findAllFiltered(
                null, null, null, null, null, (short) 1,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .allMatch(buffer -> buffer.getBufferType().equals((short) 1));
    }

    @Test
    public void testFindAllFiltered_WithMultipleFilters_ShouldReturnCorrectResults() {
        // When
        Page<Buffer> result = bufferRepository.findAllFiltered(
                null, null, null, null, false, (short) 2,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .allMatch(buffer ->
                        buffer.getRead().equals(false) &&
                                buffer.getBufferType().equals((short) 2)
                );
    }

    @Test
    public void testFindAllFiltered_WithPagination_ShouldReturnCorrectPage() {
        // When
        Page<Buffer> firstPageResult = bufferRepository.findAllFiltered(
                null, null, null, null, null, null, PageRequest.of(0, 2)
        );
        Page<Buffer> secondPageResult = bufferRepository.findAllFiltered(
                null, null, null, null, null, null, PageRequest.of(1, 2)
        );

        // Assert
        assertThat(firstPageResult.getContent()).hasSize(2);
        assertThat(secondPageResult.getContent()).hasSize(2);
        assertThat(firstPageResult.getTotalElements()).isEqualTo(5);
        assertThat(firstPageResult.getTotalPages()).isEqualTo(3);

        List<UUID> firstPageIds = firstPageResult.getContent().stream()
                .map(Buffer::getId)
                .toList();
        List<UUID> secondPageIds = secondPageResult.getContent().stream()
                .map(Buffer::getId)
                .toList();
        assertThat(firstPageIds).doesNotContainAnyElementsOf(secondPageIds);
    }

    @Test
    public void testCountTypeUnread_WithNullBufferType_ShouldCountAllUnread() {
        // When
        Long count = bufferRepository.countTypeUnread(null);

        // Assert
        assertThat(count).isEqualTo(3L);
    }

    @Test
    public void testCountTypeUnread_WithSpecificBufferType_ShouldCountTypeUnread() {
        // Given
        Short bufferType1 = (short) 1;
        Short bufferType2 = (short) 2;

        // When
        Long countType1 = bufferRepository.countTypeUnread(bufferType1);
        Long countType2 = bufferRepository.countTypeUnread(bufferType2);

        // Assert
        assertThat(countType1).isEqualTo(1L);
        assertThat(countType2).isEqualTo(2L);
    }

    @Test
    public void testCountTypeUnread_WithNonExistentBufferType_ShouldReturnZero() {
        // When
        Long count = bufferRepository.countTypeUnread((short) 999);

        // Assert
        assertThat(count).isEqualTo(0L);
    }

}
