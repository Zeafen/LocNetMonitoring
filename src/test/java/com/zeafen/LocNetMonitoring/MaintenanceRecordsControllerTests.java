package com.zeafen.LocNetMonitoring;

import com.zeafen.LocNetMonitoring.config.LocNetMonitoringAuthenticationSuccessHandler;
import com.zeafen.LocNetMonitoring.config.PasswordEncoderConfiguration;
import com.zeafen.LocNetMonitoring.config.WebSecurityConfig;
import com.zeafen.LocNetMonitoring.controller.MaintenanceController;
import com.zeafen.LocNetMonitoring.domain.models.entity.*;
import com.zeafen.LocNetMonitoring.domain.services.MachineTypesService;
import com.zeafen.LocNetMonitoring.domain.services.MachinesService;
import com.zeafen.LocNetMonitoring.domain.services.MaintenanceService;
import com.zeafen.LocNetMonitoring.domain.stub.UserRole;
import com.zeafen.LocNetMonitoring.domain.stub.UsersService;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Matcher;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MaintenanceController.class)
@AutoConfigureMockMvc
@Import({PasswordEncoderConfiguration.class, WebSecurityConfig.class})
@EnableSpringDataWebSupport
public class MaintenanceRecordsControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MachinesService _machines;
    @MockitoBean
    private MachineTypesService _types;
    @MockitoBean
    private MaintenanceService _maintenance;
    @MockitoBean
    private UsersService _users;
    @MockitoBean
    private LocNetMonitoringAuthenticationSuccessHandler successHandler;
    @MockitoBean
    private PasswordEncoder passwordEncode;

    private static Machine _expectedMachine;
    private static Maintenance _expectedMaintenance;

    @BeforeAll
    static void setUp() {
        //setting up machine
        MachineType machineType1 = new MachineType();
        machineType1.setName("Lorem ipsum");

        MachineModel model1 = new MachineModel();
        model1.setName("Сonsectetur adipiscing");
        model1.setYearsExpire((short) 13);
        model1.setType(machineType1);

        Machine expectedMachine = new Machine();
        expectedMachine.setId(101);
        expectedMachine.setName("ut labore et dolore");
        expectedMachine.setAddress("255.255.255.0");
        expectedMachine.setSerialNumber(123456);
        expectedMachine.setDateProduced(OffsetDateTime.of(2022, 1, 15, 10, 0, 0, 0, ZoneOffset.UTC));
        expectedMachine.setDateCommissioning(OffsetDateTime.of(2022, 2, 1, 9, 0, 0, 0, ZoneOffset.UTC));
        expectedMachine.setModel(model1);

        _expectedMachine = expectedMachine;

        //setting up maintenance
        MaintenanceType maintenanceType = new MaintenanceType();
        Maintenance expectedMaintenance = new Maintenance();
        maintenanceType.setId((short) 1);
        maintenanceType.setPeriod("1 day");
        maintenanceType.setName("Lorem ipsum");
        expectedMaintenance.setId(1);
        expectedMaintenance.setWorkDescription("Lorem ipsum dolor sit amet");
        expectedMaintenance.setMaintenanceType(maintenanceType);

        _expectedMaintenance = expectedMaintenance;
    }

    @BeforeEach
    void setupMock() {
        Mockito.when(
                _machines.getMachineByID(eq(_expectedMachine.getId()))
        ).thenReturn(_expectedMachine);

        Mockito.when(
                _maintenance.getMaintenanceById(eq(_expectedMaintenance.getId()))
        ).thenReturn(_expectedMaintenance);
    }

    @Test
    void getMaintenanceRecords_AllParameters_ShouldShow() throws Exception {
        //Given
        String typeName = "Lorem ipsum";
        String period = "1 day";
        String workDescription = "Dolor sit amet";

        //When
        Mockito.when(
                _maintenance.getMaintenance(
                        anyInt(),
                        anyInt(),
                        eq(typeName),
                        eq(period),
                        eq(workDescription)
                )
        ).thenReturn(Page.empty(PageRequest.of(0, 10)));

        var actionsResult = mockMvc.perform(
                get("/maintenance")
                        .param("page", "0")
                        .param("perPage", "10")
                        .param("typeName", typeName)
                        .param("period", period)
                        .param("workDescription", workDescription)
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().isOk(),
                        model().attribute("typeName", typeName),
                        model().attribute("period", period),
                        model().attribute("workDescription", workDescription)
                );
    }

    @Test
    void getMaintenanceRecords_SelectedMachine_ShouldShowCard() throws Exception {
        //When
        Mockito.when(
                _maintenance.getMaintenanceRecords(
                        anyInt(),
                        anyInt(),
                        eq(_expectedMachine.getId()),
                        any(),
                        any(),
                        any(),
                        any()
                )
        ).thenReturn(Page.empty(PageRequest.of(0, 10)));

        var actionsResult = mockMvc.perform(
                get("/maintenance/records")
                        .param("machine", _expectedMachine.getId().toString())
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("selectedMachine")
                );
    }

    @Test
    void getMaintenanceRecords_SelectedMaintenance_ShouldShowCard() throws Exception {
        //Given
        var currentMaintenance = _expectedMaintenance;

        //When
        Mockito.when(
                _maintenance.getMaintenanceRecords(
                        anyInt(),
                        anyInt(),
                        any(),
                        eq(currentMaintenance.getId()),
                        any(),
                        any(),
                        any()
                )
        ).thenReturn(Page.empty(PageRequest.of(0, 10)));

        var actionsResult = mockMvc.perform(
                get("/maintenance/records")
                        .param("maintenance", currentMaintenance.getId().toString())
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().isOk(),
                        model().attribute("selectedMaintenance", currentMaintenance)
                );
    }

    @Test
    void getMaintenance_MaintenanceMachineNotSelected_CardWontShow() throws Exception {
        //When
        Mockito.when(
                _maintenance.getMaintenanceRecords(
                        anyInt(),
                        anyInt(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
        ).thenReturn(Page.empty(PageRequest.of(0, 10)));

        var actionsResult = mockMvc.perform(
                get("/maintenance/records")
                        .param("record", "-1")
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().isOk(),
                        model().attributeDoesNotExist("selectedRecord")
                );
    }

    @Test
    void getMaintenance_MaintenanceMachineSelected_CardWillShow() throws Exception {
        //Given
        var currentMaintenance = _expectedMaintenance;
        var currentMachine = _expectedMachine;

        //When
        Mockito.when(
                _maintenance.getMaintenanceRecords(
                        anyInt(),
                        anyInt(),
                        eq(currentMachine.getId()),
                        eq(currentMaintenance.getId()),
                        any(),
                        any(),
                        any()
                )
        ).thenReturn(Page.empty(PageRequest.of(0, 10)));

        var actionsResult = mockMvc.perform(
                get("/maintenance/records")
                        .param("record", "-1")
                        .param("machine", currentMachine.getId().toString())
                        .param("maintenance", currentMaintenance.getId().toString())
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("selectedRecord")
                );
    }

    @Test
    void postMaintenance_InValidForm_RedirectsToErrorPage() throws Exception {
        //When
        Mockito.when(
                _maintenance.saveMaintenanceRecord(any(MaintenanceRecords.class))
        ).thenReturn(new MaintenanceRecords());
        var actionsResult = mockMvc.perform(
                post("/maintenance/records/save")
                        .with(csrf())
                        .param("id", UUID.randomUUID().toString())
                        .param("dateStarted", LocalDateTime.of(2022, 2, 15, 10, 0, 0, 0).format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("dateFinished", LocalDateTime.of(2022, 1, 15, 10, 0, 0, 0).format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("reason", "Lorem")
                        .param("status", "0")
                        .param("maintenance.id", "1")
                        .param("machine.id", "1")
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/error")
                );
    }

    @Test
    void postMaintenance_ValidForm_RedirectsToErrorPage() throws Exception {
        //When
        Mockito.when(
                _maintenance.saveMaintenanceRecord(any(MaintenanceRecords.class))
        ).thenReturn(new MaintenanceRecords());
        var actionsResult = mockMvc.perform(
                post("/maintenance/records/save")
                        .with(csrf())
                        .param("id", UUID.randomUUID().toString())
                        .param("dateStarted", LocalDateTime.of(2022, 1, 15, 10, 0, 0, 0).format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("dateFinished", LocalDateTime.of(2022, 2, 15, 10, 0, 0, 0).format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("reason", "Lorem ipsum")
                        .param("status", "0")
                        .param("maintenance.id", "1")
                        .param("machine.id", "1")
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/maintenance/records")
                );
    }
}
