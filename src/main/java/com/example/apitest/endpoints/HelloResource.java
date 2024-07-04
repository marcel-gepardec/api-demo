package com.example.apitest.endpoints;

import com.example.apitest.domain.filter.annotation.Secured;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.slf4j.Logger;

@Path("/hello-world")
public class HelloResource {

    @Inject
    private Logger logger;

    @GET
    @Secured
    @Produces("text/plain")
    public String hello() {
        logger.info("test");
        return "Hello, World!";
    }
}
