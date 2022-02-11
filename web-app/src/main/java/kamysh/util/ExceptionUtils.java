package kamysh.util;

import kamysh.exceptions.ArgumentFormatException;
import kamysh.exceptions.EntryNotFound;
import kamysh.exceptions.StorageServiceRequestException;

/**
 * This class doing staff to fix problems with custom exception classes deserialization with Payara
 */
public class ExceptionUtils {
    public static Exception deserializeRemoteException(Throwable e) throws Throwable {
        try {
            Throwable cause = e.getCause();
            if (cause.getMessage().startsWith("EntryNotFound")) {
                String[] parts = cause.getMessage().split(";", 3);
                return new EntryNotFound(Long.parseLong(parts[1]), parts[2]);
            } else if (cause.getMessage().startsWith("ArgumentFormatException")) {
                String[] parts = cause.getMessage().split(";", 3);
                return new ArgumentFormatException(parts[1], parts[2]);
            }  else if (cause.getMessage().startsWith("StorageServiceRequestException")) {
                String[] parts = cause.getMessage().split(";", 2);
                return new StorageServiceRequestException(parts[1]);
            } else {
                System.out.println("DESERIALIZATION NOT FOUND");
                throw cause;
            }
        } catch (Exception e1) {
            System.out.println("DESERIALIZATION FAILED");
            e1.printStackTrace();
            throw e;
        }
    }
}
