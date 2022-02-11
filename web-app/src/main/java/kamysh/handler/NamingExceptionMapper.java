package kamysh.handler;

import kamysh.dto.ErrorDTO;
import kamysh.exceptions.ErrorMessage;
import lombok.SneakyThrows;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class NamingExceptionMapper implements ExceptionMapper<NamingException> {
    @Override
    @SneakyThrows
    public Response toResponse(NamingException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorDTO
                        .builder()
                        .error(ErrorCode.REMOTE_SERVICE_UNAVAILABLE.name())
                        .message(ErrorMessage.REMOTE_SERVICE_UNAVAILABLE)
                        .build())
                .build();
    }
}
