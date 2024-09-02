package be.jimsa.iotproject.config.document.annotation;

import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Updating a project with the provided info was successful",
                        content = @Content(
                                examples = @ExampleObject("""
                                        {
                                          "action": true,
                                          "timestamp": "2024-09-02T02:20:06.748776813",
                                          "result": {
                                            "name": "Alpha w6",
                                            "type": "Super lux",
                                            "public_id": "oGqrBVCEOCzdm5oSpKzU_3aTyf3CJhOjOIfF4Qf6x0MWTU5c1PU3otXdDpmP6R3Y"
                                          }
                                        }
                                        """),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Due to invalid public_id, it responded a not-found",
                        content = @Content(
                                examples = @ExampleObject("""
                                        {
                                          "action": false,
                                          "timestamp": "2024-09-02T12:28:27.40787343",
                                          "result": {
                                            "path": "PUT /v1/projects/oGqrBVCEOCzdm5oSpKzU_3aTyf3CJhOjOIfF4Qf6x0MWTU5c1PU3otXdDpmP6R3Y",
                                            "message": "The resource with provided public_id not founded!"
                                          }
                                        }
                                        """),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal server error has occurred",
                        content = @Content(
                                examples = @ExampleObject("""
                                        {
                                            "action": false,
                                            "timestamp": "2024-09-02T02:21:41.009081702",
                                            "result": {
                                                "path": "PUT /v1/projects/oGqrBVCEOCzdm5oSpKzU_3aTyf3CJhOjOIfF4Qf6x0MWTU5c1PU3otXdDpmP6R3Y",
                                                "message": "Internal service error!"
                                            }
                                        }
                                        """),
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                )
        },
        requestBody = @RequestBody(
                description = "This request comes with a name and type",
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = {
                                @ExampleObject(
                                        name = "A valid request with a valid body",
                                        summary = "Valid example",
                                        value = """
                                                {
                                                      "name": "Alpha w6",
                                                      "type": "Super lux"
                                                }
                                                """),
                                @ExampleObject(
                                        name = "An invalid request without a valid body #1",
                                        summary = "Invalid example 1",
                                        value = """
                                                {
                                                      "name": "Alpha w6"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "An invalid request without a valid body #2",
                                        summary = "Invalid example 2",
                                        value = """
                                                {
                                                      "type": "Super lux"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "An invalid request without a valid body #3",
                                        summary = "Invalid example 3",
                                        value = """
                                                    {
                                                            "name": "Alpha w6",
                                                            "type": "Super lux",
                                                            "public_id": "oGqrBVCEOCzdm5oSpKzU_3aTyf3CJhOjOIfF4Qf6x0MWTU5c1PU3otXdDpmP6R3Y"
                                                    }
                                                """
                                )
                        },
                        schema = @Schema(implementation = ProjectDto.class)
                )
        )
)
public @interface PutRequestDocument {
    String summary() default "Default summary";

    String description() default "Default description";
}
