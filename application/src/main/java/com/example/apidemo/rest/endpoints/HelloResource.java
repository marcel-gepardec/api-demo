package com.example.apidemo.rest.endpoints;

import com.example.apidemo.domain.filter.annotation.Secured;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;

@Path("/hello-world")
@Tag(name = "Hello World", description = "Hello World")
public class HelloResource {

    @Inject
    private Logger logger;

    @GET
    @Secured
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @HeaderParam(HttpHeaders.AUTHORIZATION)
    @Produces("text/plain")
    @Operation(
            summary = "Hello World",
            description = "Call Hello World with Barrier token"
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Successful, returning a Barrier token"),
            @APIResponse(responseCode = "401", description = "Unauthorised, token is not valid")
    })
    public Response hello() {
        logger.info("test");
        return Response.status(Response.Status.OK).entity("Hello, World!").build();
    }
}
