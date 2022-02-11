package kamysh.handler;

import kamysh.exceptions.StorageServiceRequestException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StorageServiceRequestErrorMapper implements ExceptionMapper<StorageServiceRequestException> {
    @Override
    public Response toResponse(StorageServiceRequestException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .build();
    }
}
