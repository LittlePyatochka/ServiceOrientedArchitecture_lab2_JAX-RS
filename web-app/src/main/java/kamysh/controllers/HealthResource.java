package kamysh.controllers;

import kamysh.dto.StatusDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/health")
public class HealthResource {
    @GET
    public StatusDTO getStatus() {
        return StatusDTO.builder().status("UP").build();
    }

    @GET
    @Path("/more")
    public StatusDTO getStatusMore() {
        System.out.println("Got more health request to check balancing");
        return StatusDTO.builder().status("UP").build();
    }
}
