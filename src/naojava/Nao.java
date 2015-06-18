package naojava;

import com.aldebaran.qimessaging.*;
import com.aldebaran.qimessaging.helpers.al.*;

import java.lang.Object;

public class Nao {

    private static String PORT = "9559";
    private static String IP   = "172.16.113.110";

    static Session session;
    static Application app;

    static ALLandMarkDetection markProxy;
    static ALTextToSpeech tts;
    static ALMotion motion;
    static ALRobotPosture posture;
    ALMemory memProxy;

    public static void main(String[] args) throws Exception {

        // Init session & connexion
        app     = new Application(args);
        session = new Session();
        Future<Void> fut = session.connect("tcp://172.16.6.117:9559");
        fut.get();


        // Init nao features
        tts     = new ALTextToSpeech(session);
        motion  = new ALMotion(session);
        posture = new ALRobotPosture(session);


        tts.setLanguage("French");
        tts.say("Bonjour la France !");
        // motion.wakeUp();

        // markProxy = new ALLandMarkDetection(session);

        // int detectedLandmark = this.detectLandmark();
        // tts.say("Landmark " + detectedLandmark + " détecté !");


        NodeService sender = new NodeService();
        int exit = sender.get("/page/1");
        System.out.println(exit);

    }


    public int detectLandmark() throws Exception {

        this.markProxy.subscribe("LandmarkDetected", 500, (float) 0.0);
        this.memProxy = new ALMemory(session);

        this.tts.say("En attente de détection ...");
        Object dataLandmark = memProxy.getData("LandmarkDetected");

        while(dataLandmark.toString()=="[]") {

            this.markProxy.subscribe("LandmarkDetected", 500, (float) 0.0);
            dataLandmark = memProxy.getData("LandmarkDetected");

            if(dataLandmark.toString() != "[]") {
                String values = dataLandmark.toString();
                String[] results = values.split(",");
                int id = Integer.parseInt(results[8].replace("[", "").replace("]", "").replace(" ",""));
                return id;
            }
        }

        return 0;
    }

}