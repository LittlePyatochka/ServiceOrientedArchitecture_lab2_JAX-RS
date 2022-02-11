package kamysh;

import kamysh.controllers.CalculationResource;
import kamysh.controllers.HealthResource;
import kamysh.filters.CorsFilter;
import kamysh.handler.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {
    private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> empty = new HashSet<>();

    public RestApplication() {
        singletons.add(new CalculationResource());
        singletons.add(new HealthResource());
        singletons.add(new NullPointerMapper());
        singletons.add(new ArgumentFormatExceptionMapper());
        singletons.add(new RemoteExceptionMapper());
        singletons.add(new EntryNotFoundMapper());
        singletons.add(new NamingExceptionMapper());
        singletons.add(new CorsFilter());
        singletons.add(new StorageServiceRequestExceptionMapper());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
