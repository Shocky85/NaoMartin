package naojava;

import com.aldebaran.qimessaging.Future;
import com.aldebaran.qimessaging.Session;
import com.aldebaran.qimessaging.helpers.al.ALMotion;
import com.aldebaran.qimessaging.helpers.al.ALRobotPosture;
import com.aldebaran.qimessaging.helpers.al.*;

import java.io.OutputStream;

public class Nao {

    private static String PORT = "9559";
    private static String IP   = "172.16.6.117";

    static Session session;

    static ALLandMarkDetection markProxy;
    static ALTextToSpeech tts;
    static ALMotion motion;
    static ALRobotPosture posture;
    static ALMemory memProxy;

    public static void main(String[] args) throws Exception {

        // Init session & connexion
        session        = new Session();
        Future<Void> future = session.connect("tcp://" + IP + ":" + PORT);
        future.get();

        // Init nao features
        tts     = new ALTextToSpeech(session);
        motion  = new ALMotion(session);
        posture = new ALRobotPosture(session);
        int detectedLandmark = 0;

        tts.setLanguage("French");
        tts.say("Bonjour la France !");
        // motion.wakeUp();

         markProxy = new ALLandMarkDetection(session);
        markProxy.subscribe("LandmarkDetected", 500, (float) 0.0);
        memProxy = new ALMemory(session);

        tts.say("En attente de détection");
        Object dataLandmark = memProxy.getData("LandmarkDetected");

        System.out.println(dataLandmark.toString());
        while(dataLandmark.toString()=="[]") {


            markProxy.subscribe("LandmarkDetected", 500, (float) 0.0);
            dataLandmark = memProxy.getData("LandmarkDetected");

            if(dataLandmark.toString() != "[]") {
                String values = dataLandmark.toString();
                String[] results = values.split(",");
                int id = Integer.parseInt(results[8].replace("[", "").replace("]", "").replace(" ",""));
                detectedLandmark=id;
            }
        }
        //int detectedLandmark = detectLandmark();
         tts.say("Landmark " + detectedLandmark + " détecté !");


        NodeService sender = new NodeService();
        int exit = sender.get("/page/1");
        System.out.println(exit);

    }


    public static int detectLandmark() throws Exception {

        markProxy.subscribe("LandmarkDetected", 500, (float) 0.0);
        memProxy = new ALMemory(session);

        tts.say("En attente de détection");
        Object dataLandmark = memProxy.getData("LandmarkDetected");

        System.out.println(dataLandmark.toString());
        while(dataLandmark.toString()=="[]") {


            markProxy.subscribe("LandmarkDetected", 500, (float) 0.0);
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