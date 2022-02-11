package kamysh.service;

import kamysh.dto.CityDTO;

import javax.ejb.Remote;
import java.io.Serializable;

@Remote
public interface CityService extends Serializable {
    CityDTO getById(Long id) throws Exception;

    CityDTO getCityWithMaxPopulation() throws Exception;
}