package com.example.HTML.Paskaita_2024_07_04;

import com.example.HTML.Paskaita_2024_07_04.Models.Client;
import com.example.HTML.Paskaita_2024_07_04.Models.User;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500"})
public class Controller {

    private final ClientService clientService;

    @Autowired
    public Controller(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients(@RequestHeader("Authorization") String authorizationHeader) {
        clientService.logAPICommands("/clients/", null, clientService.getIdFromToken(authorizationHeader));

        List<Client> clientResponseList= new ArrayList<>();
        String response;
        try {
            clientResponseList = clientService.getAllClients(authorizationHeader);;
            if(clientResponseList.isEmpty()) response = "No authorization";
            else response = "ok";
        }catch (RuntimeException e){
            response = e.getMessage();
        }

        HttpStatus status = checkHttpStatus(response);

        return new ResponseEntity<>(clientResponseList,status);
    }
    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Integer id, @RequestHeader("Authorization") String authorizationHeader){
        clientService.logAPICommands("/clients/"+id, null, clientService.getIdFromToken(authorizationHeader));
       // if (!clientService.autorize(authorizationHeader)) new  ResponseEntity<>(new Client(), HttpStatus.UNAUTHORIZED);

        Client clientResponse = null;
        String response;
        try {
            clientResponse = clientService.getClientsById(id,authorizationHeader);
            if(clientResponse.getId()==0) response = "No authorization";
            else response = "ok";
        }catch (RuntimeException e){
            response = e.getMessage();
        }

        HttpStatus status = checkHttpStatus(response);

        return new ResponseEntity<>(clientResponse,status);
    }
    @PostMapping("/clients")
    public ResponseEntity<String> createClients(@RequestBody Client client, @RequestHeader("Authorization") String authorizationHeader) {
        Gson gson = new Gson();
        clientService.logAPICommands("/clients", gson.toJson(client), clientService.getIdFromToken(authorizationHeader));

        String response;
        try {
            response = clientService.createClient(client,authorizationHeader);
        }catch (RuntimeException e){
            response = e.getMessage();
        }

        HttpStatus status = checkHttpStatus(response);

        return new ResponseEntity<>(response,status);
    }
    @DeleteMapping ("/clients/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer id, @RequestHeader("Authorization") String authorizationHeader){
        clientService.logAPICommands("/clients/"+id, null, clientService.getIdFromToken(authorizationHeader));

        String response;
        try {
            response = clientService.deleteClient(id, authorizationHeader);
        }catch (RuntimeException e){
            response = e.getMessage();
        }

        HttpStatus status = checkHttpStatus(response);

        return new ResponseEntity<>(response,status);
    }
    @PutMapping ("/clients/{id}")
    public ResponseEntity<String> alterClient(@RequestBody Client client, @PathVariable Integer id, @RequestHeader("Authorization") String authorizationHeader){
        Gson gson = new Gson();
        clientService.logAPICommands("/clients/"+id, gson.toJson(client), clientService.getIdFromToken(authorizationHeader));

        String response;
        try {
            response = clientService.alterClient(client,id,authorizationHeader);;
        }catch (RuntimeException e){
            response = e.getMessage();
        }

        HttpStatus status = checkHttpStatus(response);

        return new ResponseEntity<>(response,status);
    }
    @PostMapping("/clients/user")
    public ResponseEntity<String> getToken(@RequestBody User user) {
        Gson gson = new Gson();
        clientService.logAPICommands("/clients/user", gson.toJson(user), null);

        String response;
        try {
            response = clientService.getToken(user);
        }catch (RuntimeException e){
            response = e.getMessage();
        }

        HttpStatus status = checkHttpStatus(response);

        return new ResponseEntity<>(response,status);
    }

    @GetMapping("/clients/autorize")
    public ResponseEntity<Boolean> autorize(@RequestHeader("Authorization") String authorizationHeader){
        clientService.logAPICommands("/clients/autorize", null, clientService.getIdFromToken(authorizationHeader));
        HttpStatus status;
        boolean response = clientService.autorize(authorizationHeader);
        if (response) status = HttpStatus.OK;
        else status = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(response,status);
    }

    private HttpStatus checkHttpStatus(String response){

        switch (response){
            case "Invalid username or password", "No authorization":
                return HttpStatus.UNAUTHORIZED;
            case "Database connection failed":
                return HttpStatus.INTERNAL_SERVER_ERROR;
            default:
                return HttpStatus.OK;
        }

    }


}