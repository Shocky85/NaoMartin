package naojava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NodeService {

    private String apiUrl = "http://172.16.113.106";

    protected int get(String path) throws IOException {
        HttpURLConnection con = sendData(path, "GET");
        con.setRequestProperty("Content-Type", "text");
        return con.getResponseCode();
    }

    private HttpURLConnection sendData(String path, String method) throws IOException {
        URL url = new URL(apiUrl + path);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod(method);
        httpCon.setRequestProperty("Content-Type","text");
        return httpCon;
    }

    private void sendGet() throws Exception {
        URL obj = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection)obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }
}
