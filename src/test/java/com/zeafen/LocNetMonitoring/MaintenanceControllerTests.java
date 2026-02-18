package com.zeafen.LocNetMonitoring;

import com.zeafen.LocNetMonitoring.config.LocNetMonitoringAuthenticationSuccessHandler;
import com.zeafen.LocNetMonitoring.config.PasswordEncoderConfiguration;
import com.zeafen.LocNetMonitoring.config.WebSecurityConfig;
import com.zeafen.LocNetMonitoring.data.controller.MaintenanceController;
import com.zeafen.LocNetMonitoring.domain.models.entity.Maintenance;
import com.zeafen.LocNetMonitoring.domain.models.entity.MaintenanceType;
import com.zeafen.LocNetMonitoring.domain.services.MachineTypesService;
import com.zeafen.LocNetMonitoring.domain.services.MachinesService;
import com.zeafen.LocNetMonitoring.domain.services.MaintenanceService;
import com.zeafen.LocNetMonitoring.domain.stub.UserRole;
import com.zeafen.LocNetMonitoring.domain.stub.UsersService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MaintenanceController.class)
@AutoConfigureMockMvc
@Import({PasswordEncoderConfiguration.class, WebSecurityConfig.class})
@EnableSpringDataWebSupport
public class MaintenanceControllerTests {
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


    @Test
    void getMaintenance_AllParameters_ShouldShow() throws Exception {
        //Given
        Integer status = 0;
        LocalDateTime dateFrom = LocalDateTime.of(2025, 1, 12, 0, 0);
        LocalDateTime dateUntil = LocalDateTime.of(2025, 2, 12, 0, 0);

        //When
        Mockito.when(
                _maintenance.getMaintenanceRecords(
                        anyInt(),
                        anyInt(),
                        any(),
                        any(),
                        eq(status),
                        eq(dateFrom.atOffset(ZoneOffset.UTC)),
                        eq(dateUntil.atOffset(ZoneOffset.UTC))
                )
        ).thenReturn(Page.empty(PageRequest.of(0, 10)));

        var actionsResult = mockMvc.perform(
                get("/maintenance/records")
                        .param("page", "0")
                        .param("perPage", "10")
                        .param("status", status.toString())
                        .param("dateFrom", dateFrom.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("dateUntil", dateUntil.format(DateTimeFormatter.ISO_DATE_TIME))
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().isOk(),
                        model().attribute("status", status),
                        model().attribute("dateFrom", dateFrom),
                        model().attribute("dateUntil", dateUntil)
                );
    }

    @Test
    void getMaintenance_TypeSelected_ShouldShowCard() throws Exception {
        //Given
        MaintenanceType expectedMaintenanceType = new MaintenanceType();
        expectedMaintenanceType.setId((short) 1);
        expectedMaintenanceType.setPeriod("1 day");
        expectedMaintenanceType.setName("Lorem ipsum");

        //When
        Mockito.when(
                _maintenance.getMaintenanceByType(
                        anyInt(),
                        anyInt(),
                        any(),
                        any(),
                        any()
                )
        ).thenReturn(Page.empty(PageRequest.of(0, 10)));
        Mockito.when(
                _maintenance.getMaintenanceTypeById(eq(expectedMaintenanceType.getId()))
        ).thenReturn(expectedMaintenanceType);

        var actionsResult = mockMvc.perform(
                get("/maintenance")
                        .param("type", expectedMaintenanceType.getId().toString())
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().isOk(),
                        model().attribute("selectedType", expectedMaintenanceType)
                );
    }

    @Test
    void getMaintenance_TypeNotSelected_FormWontShow() throws Exception {
        //When
        Mockito.when(
                _maintenance.getMaintenance(
                        anyInt(),
                        anyInt(),
                        any(),
                        any(),
                        any()
                )
        ).thenReturn(Page.empty(PageRequest.of(0, 10)));

        var actionsResult = mockMvc.perform(
                get("/maintenance")
                        .param("maintenance", "-1")
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().isOk(),
                        model().attributeDoesNotExist("selectedMaintenance")
                );
    }

    @Test
    void getMaintenance_TypeSelected_FormWillShow() throws Exception {
        //Given
        MaintenanceType expectedMaintenanceType = new MaintenanceType();
        expectedMaintenanceType.setId((short) 1);
        expectedMaintenanceType.setPeriod("1 day");
        expectedMaintenanceType.setName("Lorem ipsum");

        //When
        Mockito.when(
                _maintenance.getMaintenanceByType(
                        anyInt(),
                        anyInt(),
                        any(),
                        any(),
                        any()
                )
        ).thenReturn(Page.empty(PageRequest.of(0, 10)));

        Mockito.when(_maintenance.getMaintenanceTypeById(expectedMaintenanceType.getId()))
                .thenReturn(expectedMaintenanceType);

        var actionsResult = mockMvc.perform(
                get("/maintenance")
                        .param("type", expectedMaintenanceType.getId().toString())
                        .param("maintenance", "-1")
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("selectedMaintenance")
                );
    }

    @Test
    void postMaintenance_InvalidForm_RedirectsToErrorPage() throws Exception {
        //Given
        MaintenanceType expectedMaintenanceType = new MaintenanceType();
        Maintenance expectedMaintenance = new Maintenance();

        expectedMaintenanceType.setId((short) 1);
        expectedMaintenanceType.setPeriod("1 day");
        expectedMaintenanceType.setName("Lorem ipsum");

        expectedMaintenance.setId(1);
        expectedMaintenance.setMaintenanceType(expectedMaintenanceType);
        expectedMaintenance.setWorkDescription("Lorem ipsum");

        //When
        Mockito.when(_maintenance.getMaintenanceTypeById(expectedMaintenanceType.getId()))
                .thenReturn(expectedMaintenanceType);
        Mockito.when(_maintenance.saveMaintenance(any(Maintenance.class)))
                .thenReturn(expectedMaintenance);

        var actionsResult = mockMvc.perform(
                post("/maintenance/save")
                        .with(csrf())
                        .param("id", expectedMaintenance.getId().toString())
                        .param("type.id", expectedMaintenance.getMaintenanceType().getId().toString())
                        .param("workDescription", expectedMaintenance.getWorkDescription())
                        .with(user("USER_STUFF").authorities(Collections.singleton(UserRole.STUFF)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/error")
                );
    }

    @Test
    void postMaintenance_ValidForm_RedirectsToListsPage() throws Exception {
        //Given
        MaintenanceType expectedMaintenanceType = new MaintenanceType();
        Maintenance expectedMaintenance = new Maintenance();

        expectedMaintenanceType.setId((short) 1);
        expectedMaintenanceType.setPeriod("1 day");
        expectedMaintenanceType.setName("Lorem ipsum");
        expectedMaintenance.setId(1);
        expectedMaintenance.setMaintenanceType(expectedMaintenanceType);
        expectedMaintenance.setWorkDescription("Lorem ipsum dolor sit amet");

        //When
        Mockito.when(_maintenance.getMaintenanceTypeById(expectedMaintenanceType.getId()))
                .thenReturn(expectedMaintenanceType);
        Mockito.when(_maintenance.saveMaintenance(any(Maintenance.class)))
                .thenReturn(expectedMaintenance);

        var actionsResult = mockMvc.perform(
                post("/maintenance/save")
                        .with(csrf())
                        .param("id", expectedMaintenance.getId().toString())
                        .param("type.id", expectedMaintenance.getMaintenanceType().getId().toString())
                        .param("workDescription", expectedMaintenance.getWorkDescription())
                        .with(user("USER_STUFF").authorities(Collections.singleton(UserRole.STUFF)))
        );

        //Assert
        actionsResult
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/maintenance/records")
                );
    }
}