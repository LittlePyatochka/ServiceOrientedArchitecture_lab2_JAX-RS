package kamysh.service;

import kamysh.dto.SpaceMarineDto;
import kamysh.dto.StarshipDTO;
import kamysh.dto.StarshipListDTO;
import kamysh.entity.LoadStarship;
import kamysh.entity.Starship;
import kamysh.exceptions.EntryNotFound;
import kamysh.exceptions.ErrorMessage;
import kamysh.exceptions.StorageServiceRequestException;
import kamysh.repository.StarshipRepository;
import kamysh.util.ClientFactoryBuilder;
import lombok.SneakyThrows;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class StarshipService {
    private final Client client;
    private final String storageServiceUrl;
    private final StarshipRepository starshipRepository;

    public StarshipService() {
        this.client = ClientFactoryBuilder.getClient();
        this.storageServiceUrl = ClientFactoryBuilder.getStorageServiceUrl();
        this.starshipRepository = new StarshipRepository();

    }

    @SneakyThrows
    public StarshipDTO setParatroopers(Long starshipId, Long spaceMarineId){

        Starship starship = starshipRepository.findById(starshipId);
        if(starship == null){
            throw new EntryNotFound(ErrorMessage.STARSHIP_NOT_FOUND);
        }

        LoadStarship loadStarship = new LoadStarship();
        loadStarship.setStarship(starship);
        loadStarship.setSpaceMarineId(spaceMarineId);

        if (checkById(spaceMarineId)){
            starshipRepository.save(loadStarship);

            StarshipDTO starshipDTO = StarshipDTO.builder()
                    .name(starship.getName())
                    .totalParatroopers(starshipRepository.getCountSpaceMarineInStarship(starshipId))
                    .build();

            return starshipDTO;
        }
        return new StarshipDTO();
    }

    @SneakyThrows
    public StarshipDTO landAllParatroopers(Long starshipId){
        starshipRepository.delete(starshipId);
        StarshipDTO starshipDTO = StarshipDTO.builder()
                .name(starshipRepository.findById(starshipId).getName())
                .totalParatroopers(starshipRepository.getCountSpaceMarineInStarship(starshipId))
                .build();
        return starshipDTO;
    }

    @SneakyThrows
    public boolean checkById(Long id) {
        String url = storageServiceUrl + "api/space-marine/" + id;
        System.out.println(url);
        checkServerState();
        Response response = client.target(url)
                .request(MediaType.APPLICATION_XML)
                .get();
        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()){
            throw new EntryNotFound(id, ErrorMessage.SPACEMARINE_NOT_FOUND);
        }
        if (response.getStatus() > 300) throw new StorageServiceRequestException();
        return response.getStatus() == 200;

    }

    @SneakyThrows
    public void checkServerState() {
        String url = storageServiceUrl + "api/state";
        Response response = client.target(url).request().get();
        if (response.getStatus() != 200){
            throw new StorageServiceRequestException(ErrorMessage.SERVER_NOT_AVAILABLE);
        }
    }
}
