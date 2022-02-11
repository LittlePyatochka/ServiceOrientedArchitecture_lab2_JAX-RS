package kamysh.controllers;

import kamysh.dto.StarshipDTO;
import kamysh.exceptions.ArgumentFormatException;
import kamysh.exceptions.ErrorMessage;
import kamysh.service.StarshipService;
import lombok.SneakyThrows;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/starship")
public class StarshipResource {
    private final StarshipService starshipService;

    public StarshipResource() {
        this.starshipService = new StarshipService();
    }

    @POST
    @Path("/{starship-id}/load/{space-marine-id}")
    @Produces(MediaType.APPLICATION_XML)
    @SneakyThrows
    public StarshipDTO putOnStarship(
            @PathParam("starship-id") String starshipId,
            @PathParam("space-marine-id") String spaceMarineId
    ) {
        int starship, spaceMarine;
        try {
            starship = Integer.parseInt(starshipId);
        } catch (NumberFormatException e) {
            throw new ArgumentFormatException("starshipId", ErrorMessage.IS_NOT_INTEGER);
        }
        try {
            spaceMarine = Integer.parseInt(spaceMarineId);
        } catch (NumberFormatException e) {
            throw new ArgumentFormatException("spaceMarineId", ErrorMessage.IS_NOT_INTEGER);
        }

        return starshipService.setParatroopers(starship, spaceMarine);
    }

    @DELETE
    @Path("/{starship-id}/unload-all")
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public StarshipDTO kickOutOfStarship(
            @PathParam("starship-id") String starshipId
    ) {
        int starship, spaceMarine;
        try {
            starship = Integer.parseInt(starshipId);
        } catch (NumberFormatException e) {
            throw new ArgumentFormatException("starshipId", ErrorMessage.IS_NOT_INTEGER);
        }
        return starshipService.landAllParatroopers(starship);

    }
}
