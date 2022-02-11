package kamysh.handler;

import kamysh.dto.ErrorDTO;
import lombok.SneakyThrows;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class EJBExceptionMapper implements ExceptionMapper<EJBException> {
    @Override
    @SneakyThrows
    public Response toResponse(EJBException e) {
//        if (e.getCause() != null) throw e.getCause();
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorDTO
                        .builder()
                        .error(ErrorCode.INTERNAL_SERVER_ERROR.name())
                        .message(e.getCause().getMessage())
                        .build())
                .build();
    }
}
