package com.example.apidemo.endpoints;

import com.example.apidemo.domain.filter.annotation.Secured;
import com.example.apidemo.domain.logic.UserController;
import com.example.apidemo.endpoints.translator.LoginDataTranslator;
import com.example.apidemo.endpoints.translator.LoginResponseTranslator;
import com.example.apidemo.endpoints.translator.RegistrationResponseTranslator;
import com.example.apidemo.rest.model.LoginDataV1;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;

@Path("/user")
@Tag(name = "Config User service", description = "Login / Registration / Delete user")
public class UserResource {

    @Inject
    private Logger logger;

    @Inject
    private UserController userController;

    @Inject
    private LoginDataTranslator loginDataTranslator;

    @Inject
    private LoginResponseTranslator loginResponseTranslator;

    @Inject
    private RegistrationResponseTranslator registrationResponseTranslator;

    @POST
    @Path("/login")
    @Operation(
            summary = "User Login",
            description = "Login and get Barrier Token back"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                            implementation = LoginDataV1.class,
                            example = """
                                        {
                                            "email": "jemand.example@gmail.com",
                                            "password": "strongPassword"
                                        }
                                    """
                    )
            )
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Successful, returning the value"),
            @APIResponse(responseCode = "401", description = "Unauthorised, E-Mail or Password is incorrect")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDataV1 loginDataV1) {
        try {
            return Response.status(Response.Status.OK)
                    .entity(loginResponseTranslator.fromDomain(
                            userController.login(
                                    loginDataTranslator.toDomain(
                                            loginDataV1))))
                    .build();
        } catch (NoResultException | NotAuthorizedException ignored) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


    @POST
    @Path("/registration")
    @Operation(
            summary = "User Registration",
            description = "Register new user and get Barrier Token back"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                            implementation = LoginDataV1.class,
                            example = """
                                        {
                                            "email": "jemand.example@gmail.com",
                                            "password": "strongPassword"
                                        }
                                    """
                    )
            )
    )
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Created, returning a Barrier token"),
            @APIResponse(responseCode = "400", description = "Bad Request, E-Mail not valid"),
            @APIResponse(responseCode = "409", description = "Conflict, E-Mail already exists")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registration(LoginDataV1 loginDataV1) {
        try {
            if (!loginDataV1.getEmail().contains("@")) {
                return Response.status(Response.Status.BAD_REQUEST).entity("It's not a valid email address.").build();
            }
            return Response.status(Response.Status.CREATED)
                    .entity(registrationResponseTranslator.fromDomain(
                            userController.registration(
                                    loginDataTranslator.toDomain(
                                            loginDataV1))))
                    .build();
        } catch (NoResultException ignored) {
            return Response.status(Response.Status.CONFLICT).entity("E-Mail already exists.").build();
        }
    }

    @POST
    @Secured
    @Path("/delete")
    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @HeaderParam(HttpHeaders.AUTHORIZATION)
    @Operation(
            summary = "User Deletion",
            description = "Delete a user"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                            implementation = LoginDataV1.class,
                            example = """
                                        {
                                            "email": "jemand.example@gmail.com",
                                            "password": "strongPassword"
                                        }
                                    """
                    )
            )
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Successful, returning the value"),
            @APIResponse(responseCode = "401", description = "Unauthorised, E-Mail or Password is incorrect")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(LoginDataV1 loginDataV1) {
        try {
            return Response.status(Response.Status.OK)
                    .entity(userController.delete(
                            loginDataTranslator.toDomain(
                                    loginDataV1)))
                    .build();
        } catch (NoResultException | NotAuthorizedException ignored) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}