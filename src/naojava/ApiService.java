//package naojava;
//
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public abstract class ApiService {
//
//    private static String apiUrl = "192.168.145.1:3000";
//
//    protected JsonNode get(String path){
//        return Json.parse(apiUrl+path);
//    }
//    protected JsonNode post(String path,JsonNode body) throws IOException {
//        HttpURLConnection con = sendData(path,"POST");
//        con.setRequestProperty("Content-Type", "text/json");
//        OutputStreamWriter out = new OutputStreamWriter(
//                con.getOutputStream());
//        out.write(body.asText());
//        out.close();
//        return Json.toJson(con.getOutputStream());
//    }
//
//    private HttpURLConnection sendData(String path,String method) throws IOException {
//        URL url = new URL(apiUrl+path);
//        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//        httpCon.setDoOutput(true);
//        httpCon.setRequestMethod(method);
//        httpCon.setRequestProperty("Content-Type","text/json");
//        return  httpCon;
//    }
//
//    protected JsonNode put(String path,JsonNode body) throws IOException {
//        HttpURLConnection con = sendData(path,"PUT");
//        OutputStreamWriter out = new OutputStreamWriter(
//                con.getOutputStream());
//        out.write(body.asText());
//        out.close();
//        return Json.toJson(con.getOutputStream());
//    }
//}