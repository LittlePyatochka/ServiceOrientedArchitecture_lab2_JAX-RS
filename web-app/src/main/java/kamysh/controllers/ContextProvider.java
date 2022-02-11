package kamysh.controllers;

import lombok.SneakyThrows;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

public class ContextProvider {
    @SneakyThrows
    public Context getContext() {
//        Properties props = new Properties();
//        props.put(Context.INITIAL_CONTEXT_FACTORY, InitialContextFactory.class.getName());
//        props.put(Context.PROVIDER_URL, System.getenv("SOA_PROVIDER_URL"));
//        props.put(Context.PROVIDER_URL, "ldap://localhost:8080/soa-ejb");

        Hashtable<String, String> environment = new Hashtable<String, String>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "fish.payara.ejb.rest.client.RemoteEJBContextFactory");
        environment.put(Context.PROVIDER_URL, "http://localhost:8080/ejb-invoker/");
//        environment.put(Context.SECURITY_PRINCIPAL, "u1");
//        environment.put(Context.SECURITY_CREDENTIALS, "p1");
        return new InitialContext(environment);
    }
}
