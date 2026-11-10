package client;
import com.google.gson.Gson;
import model.AuthData;
import service.RegisterRequest;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;
    private final Gson serializer = new Gson();

    public ServerFacade(int port)  {
        serverUrl =  "http://localhost:" + port;
    }

    // register
    public AuthData register(String username, String password, String email) throws Exception {
        URL url = new URL(serverUrl + "/user");
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

    public void clear() throws Exception {
        URL url = new URL(serverUrl + "/db");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("DELETE");

        if(conn.getResponseCode() !=  200) {
            throw new Exception("not able to clear");
        }
    }
}
