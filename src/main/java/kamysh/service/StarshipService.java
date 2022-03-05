package kamysh.service;

import kamysh.dto.StarshipDTO;
import kamysh.entity.LoadStarship;
import kamysh.entity.Starship;
import kamysh.exceptions.EntryNotFound;
import kamysh.exceptions.ErrorMessage;
import kamysh.exceptions.SpaceMarineOnBoardException;
import kamysh.exceptions.StorageServiceRequestException;
import kamysh.handler.SpaceMarineOnBoardMapper;
import kamysh.repository.StarshipRepository;
import kamysh.util.ClientFactoryBuilder;
import lombok.SneakyThrows;

import javax.persistence.NoResultException;
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
    public StarshipDTO setParatroopers(Long starshipId, Long spaceMarineId) {

        Starship starship = starshipRepository.findById(starshipId);
        if (starship == null) {
            throw new EntryNotFound(starshipId, ErrorMessage.STARSHIP_NOT_FOUND);
        }
        checkSpaceMarineOnBoard(spaceMarineId, starshipId);
        LoadStarship loadStarship = new LoadStarship();
        loadStarship.setStarship(starship);
        loadStarship.setSpaceMarineId(spaceMarineId);

        if (checkById(spaceMarineId)) {
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
    public StarshipDTO landAllParatroopers(Long starshipId) {
        Starship starship = starshipRepository.findById(starshipId);
        if (starship == null) {
            throw new EntryNotFound(starshipId, ErrorMessage.STARSHIP_NOT_FOUND);
        }

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
        Response response;
        try {
            response = client.target(url)
                    .request(MediaType.APPLICATION_XML + "; charset=UTF-8")
                    .get();
        } catch (Exception e) {
            throw new StorageServiceRequestException(ErrorMessage.STORAGE_SERVICE_REQUEST_FAILED);
        }
        if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new EntryNotFound(id, ErrorMessage.SPACEMARINE_NOT_FOUND);
        }
        if (response.getStatus() > 300) throw new StorageServiceRequestException();
        return response.getStatus() == 200;


    }

    @SneakyThrows
    public void checkServerState() {
        String url = storageServiceUrl + "api/state";
        try {

            Response response = client.target(url).request().get();
            if (response.getStatus() != 200) {
                throw new StorageServiceRequestException(ErrorMessage.SERVER_NOT_AVAILABLE);
            }
        } catch (Exception e) {
            throw new StorageServiceRequestException(ErrorMessage.SERVER_NOT_AVAILABLE);
        }
    }

    @SneakyThrows
    public void checkSpaceMarineOnBoard(final long spaceMarineId, final long currentStarshipId) {
        try {
            Long starshipId = starshipRepository.getStarshipIdBySpaceMarine(spaceMarineId);
            if (currentStarshipId == starshipId) {
                throw new SpaceMarineOnBoardException();
            } else {
                throw new SpaceMarineOnBoardException(starshipId);
            }
        } catch (Exception e) {
            return;
        }
    }

    @SneakyThrows
    public void landParatrooper(Long spaceMatine) {
        starshipRepository.deleteParatrooper(spaceMatine);
    }
}
