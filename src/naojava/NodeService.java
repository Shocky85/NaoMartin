package naojava;

import java.io.IOException;
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
}
