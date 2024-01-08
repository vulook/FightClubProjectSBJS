package edu.cbsystematics.com.fightclubprojectsbjs.controller;

import edu.cbsystematics.com.fightclubprojectsbjs.internal.PortAggregator;
import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/internal")
public class UsersRestInternalFromServerController {

    private static final Logger logger = LoggerFactory.getLogger(UsersRestInternalFromServerController.class);

    private final RestTemplate restTemplate;

    private final PortAggregator portAggregator;

    @Autowired
    public UsersRestInternalFromServerController(RestTemplate restTemplate, PortAggregator portAggregator) {
        this.restTemplate = restTemplate;
        this.portAggregator = portAggregator;
    }

    // build port REST API
    // http://localhost:8089/internal/port-server
    @GetMapping("/port-server")
    public ResponseEntity<Integer> getServerPort() {
        int serverPort = portAggregator.getServerPort();

        return ResponseEntity.ok().body(serverPort);
    }

    // build port REST API
    // http://localhost:8089/internal/port-internal
    @GetMapping("/port-internal")
    public ResponseEntity<Integer> getInternalPort() {
        int internalPort = portAggregator.getAnotherPort();

        return ResponseEntity.ok().body(internalPort);
    }

    // Build Get All Users REST API
    // http://localhost:8089/internal/fetch-users
    @GetMapping("/fetch-users")
    public ResponseEntity<List<User>> fetchUsersFromServer() {
        int serverPort = portAggregator.getServerPort();
        String serverURL = "http://localhost:" + serverPort + "/server/users";
        logger.info("Server URL: {}", serverURL);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<User>> responseEntity = restTemplate.exchange(
                    serverURL,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<User>>() {
            });

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                logger.info("Successfully fetched users using RestTemplate");
                return responseEntity;

            } else {
                logger.error("Failed to fetch users. Status code: {}", responseEntity.getStatusCode());
                return ResponseEntity.status(responseEntity.getStatusCode()).build();
            }

        } catch (HttpStatusCodeException ex) {
            logger.error("Error fetching users using RestTemplate. Status code: {} - Message: {}", ex.getRawStatusCode(), ex.getStatusText());
            return ResponseEntity.status(ex.getRawStatusCode()).body(Collections.emptyList());

        } catch (Exception ex) {
            logger.error("Error fetching users using RestTemplate: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

}