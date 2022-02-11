package kamysh.service;

import kamysh.dto.CoordinatesDTO;

import javax.ejb.Remote;

@Remote
public interface CoordinatesService {
    CoordinatesDTO getById(Long id) throws Exception;

    Double getDistanceBetween(CoordinatesDTO from, CoordinatesDTO to) throws Exception;
}
