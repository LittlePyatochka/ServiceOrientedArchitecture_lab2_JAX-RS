package kamysh.controllers;

import kamysh.dto.CoordinatesDTO;
import kamysh.dto.LengthDTO;
import kamysh.exceptions.ArgumentFormatException;
import kamysh.exceptions.ErrorMessage;
import kamysh.service.CityService;
import kamysh.service.CoordinatesService;
import kamysh.util.ExceptionUtils;
import lombok.SneakyThrows;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/calculate")
public class CalculationResource {
    private final CoordinatesService coordinatesService;
    private final CityService cityService;

    @SneakyThrows
    public CalculationResource() {
        Context context = new ContextProvider().getContext();
        String moduleName = "service-oriented-architecture-actions-jaxrs-api-ejb";
        Object refCityService = context.lookup("java:global/" + moduleName + "/CityServiceImpl!" + CityService.class.getName());
        Object refCoordinatesService = context.lookup("java:global/" + moduleName + "/CoordinatesServiceImpl!" + CoordinatesService.class.getName());
        this.cityService = (CityService) PortableRemoteObject.narrow(refCityService, CityService.class);
        this.coordinatesService = (CoordinatesService) PortableRemoteObject.narrow(refCoordinatesService, CoordinatesService.class);
    }

    @GET
    @Path("/length/{id1}/{id2}")
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public LengthDTO calculateDistanceBetweenCities(
            @PathParam("id1") String id1,
            @PathParam("id2") String id2
    ) {
        long lid1, lid2;
        try {
            lid1 = Long.parseLong(id1);
        } catch (NumberFormatException e) {
            throw new ArgumentFormatException("id1", ErrorMessage.IS_INTEGER);
        }
        try {
            lid2 = Long.parseLong(id2);
        } catch (NumberFormatException e) {
            throw new ArgumentFormatException("id2", ErrorMessage.IS_INTEGER);
        }
        try {
            CoordinatesDTO c1 = this.coordinatesService.getById(this.cityService.getById(lid1).getCoordinates());
            CoordinatesDTO c2 = this.coordinatesService.getById(this.cityService.getById(lid2).getCoordinates());
            return new LengthDTO(this.coordinatesService.getDistanceBetween(c1, c2));
        } catch (Exception e) {
            System.out.println("CAAAAATCH AN EXCEEEPTION: " + e.getClass().getName());
            System.out.println("CAAAAAUSE: " + e.getCause().getClass().getName() + " MESSAGE: " + e.getCause().getMessage());
            throw ExceptionUtils.deserializeRemoteException(e);
        }
    }

    @GET
    @Path("/to-max-populated")
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public LengthDTO calculateDistanceToMaxPopulated() {
        try {
            CoordinatesDTO c = this.coordinatesService.getById(this.cityService.getCityWithMaxPopulation().getCoordinates());
            return new LengthDTO(this.coordinatesService.getDistanceBetween(c,
                    CoordinatesDTO.builder().x(0L).y(0).build()));
        } catch (Exception e) {
            throw ExceptionUtils.deserializeRemoteException(e);
        }
    }
}
