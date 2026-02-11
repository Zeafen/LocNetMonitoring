package com.zeafen.LocNetMonitoring;

import com.zeafen.LocNetMonitoring.data.repositories.BufferRepository;
import com.zeafen.LocNetMonitoring.data.repositories.MachinesRepository;
import com.zeafen.LocNetMonitoring.domain.models.entity.*;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class MachinesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MachinesRepository machinesRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER SEQUENCE IF EXISTS machine_models_id_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE IF EXISTS machine_types_id_seq RESTART WITH 1");

        MachineType machineType1 = new MachineType();
        machineType1.setName("Lorem ipsum");
        entityManager.persist(machineType1);
        entityManager.flush();

        MachineType machineType2 = new MachineType();
        machineType2.setName("Dolor sit");
        entityManager.persist(machineType2);
        entityManager.flush();

        MachineModel model1 = new MachineModel();
        model1.setName("Сonsectetur adipiscing");
        model1.setYearsExpire((short) 13);
        model1.setType(machineType1);
        entityManager.persist(model1);
        entityManager.flush();

        MachineModel model2 = new MachineModel();
        model2.setName("Sed do eiusmod");
        model2.setYearsExpire((short) 23);
        model2.setType(machineType1);
        entityManager.persist(model2);
        entityManager.flush();

        MachineModel model3 = new MachineModel();
        model3.setName("tempor incididunt");
        model3.setYearsExpire((short) 33);
        model3.setType(machineType2);
        entityManager.persist(model3);
        entityManager.flush();

        Machine machine1 = new Machine();
        machine1.setId(101);
        machine1.setName("ut labore et dolore");
        machine1.setAddress("255.255.255.0");
        machine1.setSerialNumber(123456);
        machine1.setDateProduced(OffsetDateTime.of(2022, 1, 15, 10, 0, 0, 0, ZoneOffset.UTC));
        machine1.setDateCommissioning(OffsetDateTime.of(2022, 2, 1, 9, 0, 0, 0, ZoneOffset.UTC));
        machine1.setModel(model1);
        entityManager.persist(machine1);
        entityManager.flush();

        Machine machine2 = new Machine();
        machine2.setId(102);
        machine2.setName("magna aliqua");
        machine2.setAddress("255.255.255.1");
        machine2.setSerialNumber(123457);
        machine2.setDateProduced(OffsetDateTime.of(2022, 3, 10, 14, 0, 0, 0, ZoneOffset.UTC));
        machine2.setDateCommissioning(OffsetDateTime.of(2022, 4, 1, 8, 0, 0, 0, ZoneOffset.UTC));
        machine2.setModel(model1);
        entityManager.persist(machine2);
        entityManager.flush();

        Machine machine3 = new Machine();
        machine3.setId(103);
        machine3.setName("Ut enim ad labore");
        machine3.setAddress("255.255.254.2");
        machine3.setSerialNumber(987654);
        machine3.setDateProduced(OffsetDateTime.of(2020, 5, 20, 11, 0, 0, 0, ZoneOffset.UTC));
        machine3.setDateCommissioning(OffsetDateTime.of(2020, 6, 1, 10, 0, 0, 0, ZoneOffset.UTC));
        machine3.setModel(model2);
        entityManager.persist(machine3);
        entityManager.flush();

        Machine machine4 = new Machine();
        machine4.setId(104);
        machine4.setName("minim veniam");
        machine4.setAddress("255.255.254.3");
        machine4.setSerialNumber(555555);
        machine4.setDateProduced(OffsetDateTime.of(2023, 1, 10, 9, 0, 0, 0, ZoneOffset.UTC));
        machine4.setDateCommissioning(OffsetDateTime.of(2023, 2, 15, 8, 0, 0, 0, ZoneOffset.UTC));
        machine4.setModel(model3);
        entityManager.persist(machine4);
        entityManager.flush();

        Machine machine5 = new Machine();
        machine5.setId(105);
        machine5.setName("quis labore nostrud");
        machine5.setAddress("255.255.254.4");
        machine5.setSerialNumber(555556);
        machine5.setDateProduced(OffsetDateTime.of(2023, 3, 5, 13, 0, 0, 0, ZoneOffset.UTC));
        machine5.setDateCommissioning(OffsetDateTime.of(2023, 4, 1, 7, 0, 0, 0, ZoneOffset.UTC));
        machine5.setModel(model3);
        entityManager.persist(machine5);
        entityManager.flush();
    }

    @Test
    void testFindAllFiltered_NoFilters_ShouldReturnAllMachines() {
        // When
        Page<Machine> result = machinesRepository.findAllFiltered(
                null, null, null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getTotalElements()).isEqualTo(5);
    }

    @Test
    void testFindAllFiltered_ByName_ShouldReturnFilteredMachines() {
        // When
        Page<Machine> result = machinesRepository.findAllFiltered(
                "labore", null, null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .extracting(Machine::getName)
                .allMatch(name -> name.contains("labore"));
    }

    @Test
    void testFindAllFiltered_ByAddress_ShouldReturnFilteredMachines() {
        // When
        Page<Machine> result = machinesRepository.findAllFiltered(
                null, "255.255.254", null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .extracting(Machine::getAddress)
                .allMatch(address -> address.contains("255.255.254"));
    }

    @Test
    void testFindAllFiltered_ByDateCommissioningRange_ShouldReturnFilteredMachines() {
        // Given
        OffsetDateTime fromDate = OffsetDateTime.of(2022, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime toDate = OffsetDateTime.of(2023, 2, 15, 8, 0, 0, 0, ZoneOffset.UTC);

        // When
        Page<Machine> result = machinesRepository.findAllFiltered(
                null, null, fromDate, toDate, null,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))
        );

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .extracting(Machine::getId)
                .containsExactlyInAnyOrder(101, 102, 104);
    }

    @Test
    void testFindAllFiltered_BySerialNumber_ShouldReturnExactMatch() {
        // When
        Page<Machine> result = machinesRepository.findAllFiltered(
                null, null, null, null, 123456,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getSerialNumber()).isEqualTo(123456);
        assertThat(result.getContent().get(0).getId()).isEqualTo(101);
    }

    @Test
    void testFindAllFiltered_CombinedFilters_ShouldReturnFilteredMachines() {
        // When
        Page<Machine> result = machinesRepository.findAllFiltered(
                "labore", null,
                OffsetDateTime.of(2022, 2, 1, 9, 0, 0, 0, ZoneOffset.UTC),
                null, null,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))
        );

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(Machine::getId)
                .containsExactlyInAnyOrder(101, 105);
    }

    @Test
    void testFindAllByTypeIdFiltered_ShouldReturnMachinesOfSpecificType() {
        // When
        Page<Machine> result = machinesRepository.findAllByTypeIdFiltered(
                (short) 1,
                null, null, null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .extracting(m -> m.getModel().getType().getId())
                .allMatch(id -> id == 1);
    }

    @Test
    void testFindAllByTypeNameFiltered_ShouldReturnMachinesByTypeName() {
        // When
        Page<Machine> result = machinesRepository.findAllByTypeNameFiltered(
                "Lorem ipsum",
                null, null, null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .extracting(m -> m.getModel().getType().getName())
                .allMatch(name -> name.contains("Lorem ipsum"));
    }

    @Test
    void testFindAllByModelNameFiltered_ShouldReturnMachinesByModelName() {
        // When
        Page<Machine> result = machinesRepository.findAllByModelNameFiltered(
                "Сonsectetur adipiscing",
                null, null, null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(m -> m.getModel().getName())
                .allMatch(name -> name.contains("Сonsectetur adipiscing"));
    }

    @Test
    void testFindAllByModelIdFiltered_ShouldReturnMachinesByModelId() {
        // When
        Page<Machine> result = machinesRepository.findAllByModelIdFiltered(
                (short) 1,
                null, null, null, null, null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(m -> m.getModel().getId())
                .allMatch(id -> id == 1);
    }

    @Test
    void testFindAllFiltered_Pagination_ShouldReturnCorrectPage() {
        // When
        Page<Machine> firstPageResult = machinesRepository.findAllFiltered(
                null, null, null, null, null,
                PageRequest.of(0, 2)
        );
        Page<Machine> secondPageResult = machinesRepository.findAllFiltered(
                null, null, null, null, null,
                PageRequest.of(1, 2)
        );

        // Assert
        assertThat(firstPageResult.getContent()).hasSize(2);
        assertThat(firstPageResult.getTotalPages()).isEqualTo(3);

        assertThat(secondPageResult.getContent()).hasSize(2);
        assertThat(secondPageResult.getNumber()).isEqualTo(1);
    }

    @Test
    void testFindAllFiltered_DateToOnly_ShouldReturnMachinesBeforeDate() {
        // When
        Page<Machine> result = machinesRepository.findAllFiltered(
                null, null, null,
                OffsetDateTime.of(2021, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC),
                null,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(103);
    }

    @Test
    void testFindAllFiltered_NonExistingSerialNumber_ShouldReturnEmpty() {
        // When
        Page<Machine> result = machinesRepository.findAllFiltered(
                null, null, null, null, 999999,
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).isEmpty();
    }
}
