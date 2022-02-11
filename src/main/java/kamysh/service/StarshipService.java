package kamysh.service;

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
import java.util.List;

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
    public StarshipDTO setParatroopers(int starshipId, int spaceMarineId){

        LoadStarship loadStarship = new LoadStarship();
        Starship starship = starshipRepository.findById(starshipId);
        loadStarship.setStarship(starship);
        loadStarship.setSpaceMarineId(spaceMarineId);

        StarshipDTO starshipDTO = StarshipDTO.builder()
                .name(starship.getName())
                .totalParatroopers(starshipRepository.getCountSpaceMarineInStarship(starshipId))
                .build();

        return starshipDTO;
    }

    @SneakyThrows
    public StarshipDTO landAllParatroopers(int starshipId){
        starshipRepository.delete(starshipId);
        StarshipDTO starshipDTO = StarshipDTO.builder()
                .name(starshipRepository.findById(starshipId).getName())
                .totalParatroopers(starshipRepository.getCountSpaceMarineInStarship(starshipId))
                .build();
        return starshipDTO;
    }

    @SneakyThrows
    public StarshipDTO getById(Long id) {
        Response response = client.target(storageServiceUrl + "/api/city/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()){
            throw new EntryNotFound(id, ErrorMessage.CITY_NOT_FOUND);
        }
        if (response.getStatus() > 300) throw new StorageServiceRequestException();
        return response.readEntity(StarshipDTO.class);
    }

    @SneakyThrows
    public StarshipDTO getCityWithMaxPopulation() {
        List<StarshipDTO> cityDTOList = client.target(storageServiceUrl + "/api/city?sorting=-population&limit=1")
                .request(MediaType.APPLICATION_JSON)
                .get()
                .readEntity(StarshipListDTO.class)
                .getResults();
        if (cityDTOList.size() == 0) throw new EntryNotFound(ErrorMessage.CITY_NOT_FOUND);
        return cityDTOList.get(0);
    }
}
