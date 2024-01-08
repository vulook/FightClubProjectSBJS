package edu.cbsystematics.com.fightclubprojectsbjs.internal;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class InternalHttpServer {

    @Value("${server.anotherPort}")
    private int anotherPort;

    // Configures Jetty server customization
    @Bean
    public JettyServletWebServerFactory jettyCustomizer() {
        JettyServletWebServerFactory jetty = new JettyServletWebServerFactory();
        jetty.addServerCustomizers(new JettyCustomizer(anotherPort)); // Adds custom Jetty server customizer
        return jetty;
    }

    // Custom Jetty server customizer
    static class JettyCustomizer implements JettyServerCustomizer {

        private final int anotherPort;

        // Constructor initializing the internalPort
        public JettyCustomizer(int anotherPort) {
            this.anotherPort = anotherPort;
        }

        // Overrides the customize method to modify the Jetty server
        @Override
        public void customize(Server server) {

            // Creates a new connector for the server with the provided internalPort
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(anotherPort);
            server.addConnector(connector); // Adds the connector to the server

            try {
                connector.start(); // Starts the connector

            } catch (Exception ex) {
                // Throws an exception if failed to start the Jetty connector
                throw new IllegalStateException("Failed to start Jetty connector", ex);
            }
        }
    }

}