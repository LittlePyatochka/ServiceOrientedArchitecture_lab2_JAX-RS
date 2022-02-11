package kamysh.exceptions;

public class StorageServiceRequestException extends Exception {
    public StorageServiceRequestException(String message) {
        super(message);
    }

    public StorageServiceRequestException() {
        super(ErrorMessage.STORAGE_SERVICE_REQUEST_FAILED);
    }

    @Override
    public String toString() {
        return "StorageServiceRequestException;" + getMessage();
    }
}
