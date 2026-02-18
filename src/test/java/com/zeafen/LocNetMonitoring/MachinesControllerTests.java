package com.zeafen.LocNetMonitoring;

import com.zeafen.LocNetMonitoring.config.LocNetMonitoringAuthenticationSuccessHandler;
import com.zeafen.LocNetMonitoring.config.PasswordEncoderConfiguration;
import com.zeafen.LocNetMonitoring.config.WebSecurityConfig;
import com.zeafen.LocNetMonitoring.data.controller.MachinesController;
import com.zeafen.LocNetMonitoring.domain.models.entity.Machine;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineModel;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineType;
import com.zeafen.LocNetMonitoring.domain.services.MachineModelsService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MachinesController.class)
@AutoConfigureMockMvc
@Import({PasswordEncoderConfiguration.class, WebSecurityConfig.class})
@EnableSpringDataWebSupport
public class MachinesControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MachinesService machines;
    @MockitoBean
    private MachineModelsService models;
    @MockitoBean
    private MachineTypesService types;
    @MockitoBean
    private MaintenanceService maintenance;
    @MockitoBean
    private UsersService users;
    @MockitoBean
    private LocNetMonitoringAuthenticationSuccessHandler successHandler;
    @MockitoBean
    private PasswordEncoder passwordEncode;


    @Test
    void createMachine_invalidRole_shouldReturnNotPermitted() throws Exception {
        //Assert
        mockMvc.perform(
                        post("/machines/save")
                                .with(csrf())
                                .with(user("USER_OPERATOR").authorities(UserRole.values()))
                                .param("id", "")
                                .param("name", "")
                                .param("address", "")
                                .param("serialNumber", "")
                                .param("dateCommissioning", "")
                                .param("dateProduced", "")
                                .param("model", "")
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createMachine_InvalidMachine_ShouldRedirectToErrorPage() throws Exception {
        //When
        Mockito.when(machines.saveMachine(any(Machine.class))).thenReturn(new Machine());
        var actionResult = mockMvc.perform(
                post("/machines/save")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Lorem ipsum dolor sit amet")
                        .param("address", "000")
                        .param("serialNumber", "-1")
                        .param("dateCommissioning", LocalDateTime.now().minusYears(2).toString())
                        .param("dateProduced", LocalDateTime.now().minusYears(1).toString())
                        .param("model.id", "1")
                        .with(user("USER_STUFF").authorities(UserRole.values()))
        );

        //Assert
        actionResult
                .andExpect(redirectedUrl("/error"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void createMachine_ValidMachine_ShouldReturnRedirect() throws Exception {
        //When
        var actionResult = mockMvc.perform(
                post("/machines/save")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Lorem ipsum")
                        .param("address", "00.000.000.254")
                        .param("serialNumber", "1")
                        .param("dateCommissioning", LocalDateTime.now().minusYears(1).toString())
                        .param("dateProduced", LocalDateTime.now().minusYears(2).toString())
                        .param("model.id", "1")
                        .with(user("USER_STUFF").authorities(UserRole.values()))
        );

        //Assert
        actionResult
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/machines"));
    }

    @Test
    void getMachines_AllParameters_PageShouldContain() throws Exception {
        //Given
        Page<Machine> expectedMachines = Page.empty(PageRequest.of(0, 10));

        MachineType machineType = new MachineType();
        MachineModel expectedModel = new MachineModel();
        machineType.setName("Lorem ipsum");
        machineType.setId((short) 1);
        expectedModel.setId((short) 1);
        expectedModel.setName("Lorem");
        expectedModel.setType(machineType);
        expectedModel.setYearsExpire((short) 11);

        String expectedName = "Lorem ipsum";
        String expectedAddress = "dolor sit";
        Integer expectedSerialNumber = 12345;
        LocalDateTime commissionedFrom = LocalDateTime.of(2025, 1, 1, 12, 0);
        LocalDateTime commissionedUntil = LocalDateTime.of(2025, 2, 1, 12, 0);

        //When
        Mockito.when(machines.getMachines(
                        anyInt(),
                        anyInt(),
                        any(String.class),
                        any(String.class),
                        any(OffsetDateTime.class),
                        any(OffsetDateTime.class),
                        any(Integer.class)))
                .thenReturn(expectedMachines);

        var actions = mockMvc.perform(
                get("/machines")
                        .param("page", "0")
                        .param("perPage", "10")
                        .param("name", expectedName)
                        .param("address", expectedAddress)
                        .param("serialNumber", expectedSerialNumber.toString())
                        .param("commissionedFrom", commissionedFrom.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("commissionedUntil", commissionedUntil.format(DateTimeFormatter.ISO_DATE_TIME))
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actions
                .andExpect(status().isOk())
                .andExpectAll(
                        model().attribute("machines", expectedMachines),
                        model().attribute("name", expectedName),
                        model().attribute("address", expectedAddress),
                        model().attribute("serialNumber", expectedSerialNumber),
                        model().attribute("commissionedFrom", commissionedFrom),
                        model().attribute("commissionedUntil", commissionedUntil)
                );
    }

    @Test
    void getMachines_ByModelId_PageShouldContain() throws Exception {
        //Given
        Page<Machine> expectedMachines = new PageImpl<Machine>(List.of(new Machine[]{}), PageRequest.of(0, 10), 0);

        MachineType machineType = new MachineType();
        MachineModel expectedModel = new MachineModel();
        machineType.setName("Lorem ipsum");
        machineType.setId((short) 1);
        expectedModel.setId((short) 1);
        expectedModel.setName("Lorem");
        expectedModel.setType(machineType);
        expectedModel.setYearsExpire((short) 11);

        //When
        Mockito.when(machines.getMachinesByModel(
                        anyInt(),
                        anyInt(),
                        eq(expectedModel.getId()),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()))
                .thenReturn(expectedMachines);
        Mockito.when(models.getModelById(eq(expectedModel.getId())))
                .thenReturn(expectedModel);

        var actions = mockMvc.perform(
                get("/machines")
                        .param("page", "0")
                        .param("perPage", "10")
                        .param("model", expectedModel.getId().toString())
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actions
                .andExpect(status().isOk())
                .andExpectAll(
                        model().attribute("machines", expectedMachines),
                        model().attribute("selectedModel", expectedModel)
                );
    }

    @Test
    void getMachines_ByModelName_PageShouldContain() throws Exception {
        //Given
        Page<Machine> expectedMachines = Page.empty(PageRequest.of(0, 10));

        MachineType machineType = new MachineType();
        MachineModel expectedModel = new MachineModel();
        machineType.setName("Lorem ipsum");
        machineType.setId((short) 1);
        expectedModel.setId((short) 1);
        expectedModel.setName("Lorem");
        expectedModel.setType(machineType);
        expectedModel.setYearsExpire((short) 11);

        //When
        Mockito.when(machines.getMachinesByModel(
                        anyInt(),
                        anyInt(),
                        eq(expectedModel.getName()),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()))
                .thenReturn(expectedMachines);
        Mockito.when(models.getModelById(eq(expectedModel.getId())))
                .thenReturn(expectedModel);

        var actions = mockMvc.perform(
                get("/machines")
                        .param("page", "0")
                        .param("perPage", "10")
                        .param("modelName", expectedModel.getName())
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actions
                .andExpect(status().isOk())
                .andExpectAll(
                        model().attribute("machines", expectedMachines),
                        model().attribute("modelName", expectedModel.getName())
                );
    }

    @Test
    void getMachines_ByTypeId_PageShouldContain() throws Exception {
        //Given
        Page<Machine> expectedMachines = Page.empty(PageRequest.of(0, 10));

        MachineType expectedType = new MachineType();
        expectedType.setName("Lorem ipsum");
        expectedType.setId((short) 1);

        //When
        Mockito.when(machines.getMachinesByType(
                        anyInt(),
                        anyInt(),
                        eq(expectedType.getId()),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()))
                .thenReturn(expectedMachines);
        Mockito.when(types.getMachineTypeByID(eq(expectedType.getId())))
                .thenReturn(expectedType);

        var actions = mockMvc.perform(
                get("/machines")
                        .param("page", "0")
                        .param("perPage", "10")
                        .param("type", expectedType.getId().toString())
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actions
                .andExpect(status().isOk())
                .andExpectAll(
                        model().attribute("machines", expectedMachines),
                        model().attribute("selectedType", expectedType)
                );
    }

    @Test
    void getMachines_ByTypeName_PageShouldContain() throws Exception {
        //Given
        Page<Machine> expectedMachines = Page.empty(PageRequest.of(0, 10));

        MachineType expectedType = new MachineType();
        expectedType.setName("Lorem ipsum");
        expectedType.setId((short) 1);

        //When
        Mockito.when(machines.getMachinesByType(
                        anyInt(),
                        anyInt(),
                        eq(expectedType.getName()),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()))
                .thenReturn(expectedMachines);

        var actions = mockMvc.perform(
                get("/machines")
                        .param("page", "0")
                        .param("perPage", "10")
                        .param("typeName", expectedType.getName())
                        .with(user("USER_OPERATOR").authorities(Collections.singleton(UserRole.OPERATOR)))
        );

        //Assert
        actions
                .andExpect(status().isOk())
                .andExpectAll(
                        model().attribute("machines", expectedMachines),
                        model().attribute("typeName", expectedType.getName())
                );
    }
}
