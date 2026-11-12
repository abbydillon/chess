package client;
import com.google.gson.Gson;
import model.AuthData;
import service.LoginRequest;
import service.RegisterRequest;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import service.*;

public class ServerFacade {

    private final String serverUrl;
    private final Gson serializer = new Gson();

    public ServerFacade(int port)  {
        serverUrl =  "http://localhost:" + port;
    }

    // register
    public AuthData register(String username, String password, String email) throws Exception {
        URL url = URI.create(serverUrl + "/user").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("Content-Type", "application/json");

        RegisterRequest request = new RegisterRequest(username, password, email);

        try(OutputStreamWriter writer =  new OutputStreamWriter(conn.getOutputStream()))  {
            writer.write(serializer.toJson(request));
        }
        if(conn.getResponseCode() !=  200) {
            throw  new Exception("Not able to register");
        }
        try(InputStreamReader iReader = new InputStreamReader(conn.getInputStream())) {
            return serializer.fromJson(iReader, AuthData.class);
        }
    }

    //login
    public AuthData login(String username, String password) throws Exception {
        URL url = URI.create(serverUrl + "/session").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("Content-Type", "application/json");

        LoginRequest request = new LoginRequest(username, password);

        try(OutputStreamWriter writer =  new OutputStreamWriter(conn.getOutputStream()))  {
            writer.write(serializer.toJson(request));
        }
        if(conn.getResponseCode() !=  200) {
            throw  new Exception("Not able to login");
        }
        try(InputStreamReader iReader = new InputStreamReader(conn.getInputStream())) {
            return serializer.fromJson(iReader, AuthData.class);
        }
    }

    // create game
    public CreateGameResult createGame(String authToken,  String gameName) throws Exception {
        URL url = URI.create(serverUrl + "/game").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("authorization", authToken);

        CreateGameRequest request = new CreateGameRequest(gameName);

        try(OutputStreamWriter writer =  new OutputStreamWriter(conn.getOutputStream()))  {
            writer.write(serializer.toJson(request));
        }

        if(conn.getResponseCode() !=  200) {
            throw  new Exception("Not able to create game");
        }

        try(InputStreamReader iReader = new InputStreamReader(conn.getInputStream())) {
            return serializer.fromJson(iReader, CreateGameResult.class);
        }
    }

    // list games
    public ListGameResult listGames (String authToken) throws Exception {
        URL url = URI.create(serverUrl + "/game").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("authorization", authToken);

        if(conn.getResponseCode() !=  200) {
            throw  new Exception("Not able to create game");
        }

        try(InputStreamReader iReader = new InputStreamReader(conn.getInputStream())) {
            return serializer.fromJson(iReader, ListGameResult.class);
        }
    }


    //clear
    public void clear() throws Exception {
        URL url = URI.create(serverUrl + "/db").toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("DELETE");

        if(conn.getResponseCode() !=  200) {
            throw new Exception("not able to clear");
        }
    }
}
