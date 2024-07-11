package com.example.HTML.Paskaita_2024_07_04;

import com.example.HTML.Paskaita_2024_07_04.Models.Client;
import com.example.HTML.Paskaita_2024_07_04.Models.User;
import com.example.HTML.Paskaita_2024_07_04.Security.JwtDecoder;
import com.example.HTML.Paskaita_2024_07_04.Security.JwtGenerator;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ClientService {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public String createClient(Client client, String authorizationHeader){

        if(!autorize(authorizationHeader)) return "No authorization";

        if(client.getName() == null || client.getSurname() ==null || client.getEmail() == null || client.getPhone() == null) return "Invalid data";

        String sql = "INSERT INTO client (name,surname,email,phone)\n" +
                "VALUES (?,?,?,?);";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,client.getName());
            preparedStatement.setString(2,client.getSurname());
            preparedStatement.setString(3,client.getEmail());
            preparedStatement.setString(4,client.getPhone());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {
            throw new RuntimeException("Database connection failed");
        }

        return "Client was successfully added ";

    }
    public List<Client> getAllClients(String authorizationHeader ) {

        if(!autorize(authorizationHeader)) return Collections.emptyList();

        String sql = "SELECT * FROM client;";
        List<Client> clientList = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet =  preparedStatement.executeQuery();


            while (resultSet.next()){
                Client client = new Client();
                client.setId(resultSet.getInt("id"));
                client.setName(resultSet.getString("name"));
                client.setSurname(resultSet.getString("surname"));
                client.setEmail(resultSet.getString("email"));
                client.setPhone(resultSet.getString("phone"));

                clientList.add(client);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {
            throw new RuntimeException("Database connection failed");
        }

        return clientList;
    }

    public Client getClientsById(Integer id, String authorizationHeader){

        if(!autorize(authorizationHeader)) return new Client();

        String sql = "SELECT * FROM client WHERE id = ? ";
        Client client;

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,id);
            ResultSet resultSet =  preparedStatement.executeQuery();

            client = new Client();
            resultSet.next();

            client.setId(resultSet.getInt("id"));
            client.setName(resultSet.getString("name"));
            client.setSurname(resultSet.getString("surname"));
            client.setEmail(resultSet.getString("email"));
            client.setPhone(resultSet.getString("phone"));


        }catch (SQLException e) {
            throw new RuntimeException("Database connection failed");
        }

        return client;
    }


    public String alterClient(Client client,Integer id,String authorizationHeader){

        if(!autorize(authorizationHeader)) return "No authorization";

        String sql = "UPDATE client SET name = ?, surname = ?, email = ?, phone = ? WHERE id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1,client.getName());
            statement.setString(2,client.getSurname());
            statement.setString(3,client.getEmail());
            statement.setString(4,client.getPhone());
            statement.setInt(5,id);
            statement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException("Database connection failed");
        }

        return "Cleint was updated successfully";
    }

    public String deleteClient(Integer id, String authorizationHeader){

        if(!autorize(authorizationHeader)) return "No authorization";

        String sql = "DELETE FROM client WHERE id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            statement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException("Database connection failed");
        }

        return "Successfully deleted";
    }

    public String getToken(User user){

        String sql = "SELECT * FROM users WHERE user_name = ? AND user_password = ?";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getUser());
            preparedStatement.setString(2,user.getPassword());
            ResultSet resultSet =  preparedStatement.executeQuery();

            if(!resultSet.next()) return "Invalid username or password";
            user.setId(resultSet.getInt("user_id"));

        }catch (SQLException e) {
            throw new RuntimeException("Database connection failed");
        }

       // UUID userUUID = UUID.nameUUIDFromBytes(String.valueOf(user.getId()).getBytes(StandardCharsets.UTF_8));
        return JwtGenerator.generateJwt(user.getId());

    }

    public boolean autorize(String authorizationHeader){

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return false;
        }
        try {
            JwtDecoder.decodeJwt(authorizationHeader);
        } catch (JwtException e){
            return false;
        }
        return true;
    }

    public int getIdFromToken(String authorizationHeader){

        Claims claims;

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return -1;
        }
        try {
            claims = JwtDecoder.decodeJwt(authorizationHeader);
        } catch (JwtException e){
            return -1;
        }
        return Integer.parseInt(claims.get("UserId").toString());
    }

    public void logAPICommands(String endPoint, String jsonString, Integer userID){
        if(userID == null) userID = -1;

        String sql = "INSERT INTO command_logs (endpoint,data,user_id)\n" +
                "VALUES (?,?,?);";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,endPoint);
            preparedStatement.setString(2,jsonString);
            preparedStatement.setInt(3,userID);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        }catch (SQLException e) {
            throw new RuntimeException("Database connection failed");
        }

    }


}
