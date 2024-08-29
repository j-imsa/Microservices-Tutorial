package be.jimsa.iotproject.unit.controller;

import be.jimsa.iotproject.config.exception.BadFormatRequestException;
import be.jimsa.iotproject.config.exception.NullObjectException;
import be.jimsa.iotproject.config.exception.ResourceAlreadyExistException;
import be.jimsa.iotproject.utility.constant.ProjectConstants;
import be.jimsa.iotproject.ws.controller.ProjectController;
import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.service.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private String projectUrl;
    private String projectName;
    private String projectType;
    private String exceptionPostPath;
    private String projectPublicId;
    private long projectId;

    @BeforeEach
    void setUp() {
        projectUrl = "/v1/projects";
        projectName = "Alpha w1";
        projectType = "Luxury";
        projectPublicId = "fgdfgdcxbr2345fdsfGsddfv_-AA";
        exceptionPostPath = "POST " + projectUrl;
        projectId = 100L;
    }

    @Nested
    @DisplayName("ProjectController : CreateProject")
    class CreateProjectTests {

        @Test
        @DisplayName("testCreateProject_withAValidProjectDto_shouldReturnAValidProjectResponseDto")
        void givenAValidProjectDto_whenCreateProject_thenReturnAValidProjectResponseDto() throws Exception {
            ProjectDto projectDto = ProjectDto.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();
            given(projectService.createNewProject(projectDto))
                    .willAnswer(invocation -> invocation.getArgument(0));

            // when - action or the behaviour that we are going test:
            ResultActions actions = mockMvc.perform(
                    post(projectUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(projectDto))
            );

            // then - verify the output:
            actions
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.action").value(true))
                    .andExpect(jsonPath("$.result.name").value(projectName))
                    .andExpect(jsonPath("$.result.type").value(projectType))
                    .andExpect(jsonPath("$.result.type", is(projectDto.getType())));

        }

        @Test
        @DisplayName("testCreateProject_withAnyInvalidDto_shouldReturnNullObjectException")
        void givenAnyInvalidDto_whenCreateProject_thenReturnNullObjectException() throws Exception {
            ProjectDto projectDto = ProjectDto.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();
            given(projectService.createNewProject(any()))
                    .willThrow(new NullObjectException(ProjectConstants.EXCEPTION_INTERNAL_SERVICE_MESSAGE));

            // when - action or the behaviour that we are going test:
            ResultActions actions = mockMvc.perform(
                    post(projectUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(projectDto))
            );

            // then - verify the output:
            actions
                    .andDo(print())
                    .andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$.action").value(false))
                    .andExpect(jsonPath("$.result.message", containsString(ProjectConstants.EXCEPTION_INTERNAL_SERVICE_MESSAGE)))
                    .andExpect(jsonPath("$.result.path", containsString(exceptionPostPath)));

        }

        @Test
        @DisplayName("testCreateProject_withAnyInvalidDto_shouldReturnBadFormatRequestException")
        void givenAnyInvalidDto_whenCreateProject_thenReturnBadFormatRequestException() throws Exception {
            ProjectDto projectDto = ProjectDto.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();
            given(projectService.createNewProject(any()))
                    .willThrow(new BadFormatRequestException(ProjectConstants.EXCEPTION_BAD_FORMAT_MESSAGE));

            // when - action or the behaviour that we are going test:
            ResultActions actions = mockMvc.perform(
                    post(projectUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(projectDto))
            );

            // then - verify the output:
            actions
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.action").value(false))
                    .andExpect(jsonPath("$.result.message", containsString(ProjectConstants.EXCEPTION_BAD_FORMAT_MESSAGE)))
                    .andExpect(jsonPath("$.result.path", containsString(exceptionPostPath)));

        }

        @Test
        @DisplayName("testCreateProject_withAnyInvalidDto_shouldReturnResourceAlreadyExistException")
        void givenAnyInvalidDto_whenCreateProject_thenReturnResourceAlreadyExistException() throws Exception {
            ProjectDto projectDto = ProjectDto.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();
            given(projectService.createNewProject(any()))
                    .willThrow(new ResourceAlreadyExistException(ProjectConstants.EXCEPTION_RESOURCE_ALREADY_EXIST_MESSAGE));

            // when - action or the behaviour that we are going test:
            ResultActions actions = mockMvc.perform(
                    post(projectUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(projectDto))
            );

            // then - verify the output:
            actions
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.action").value(false))
                    .andExpect(jsonPath("$.result.message", containsString(ProjectConstants.EXCEPTION_RESOURCE_ALREADY_EXIST_MESSAGE)))
                    .andExpect(jsonPath("$.result.path", containsString(exceptionPostPath)));

        }

        @Test
        @DisplayName("testCreateProject_withAnyInvalidDto_shouldReturnRuntimeException")
        void givenAnyInvalidDto_whenCreateProject_thenReturnRuntimeException() throws Exception {
            ProjectDto projectDto = ProjectDto.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();
            given(projectService.createNewProject(any()))
                    .willThrow(new RuntimeException(ProjectConstants.EXCEPTION_INTERNAL_SERVICE_MESSAGE));

            // when - action or the behaviour that we are going test:
            ResultActions actions = mockMvc.perform(
                    post(projectUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(projectDto))
            );

            // then - verify the output:
            actions
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.action").value(false))
                    .andExpect(jsonPath("$.result.message", containsString(ProjectConstants.EXCEPTION_INTERNAL_SERVICE_MESSAGE)))
                    .andExpect(jsonPath("$.result.path", containsString(exceptionPostPath)));

        }

    }


}
