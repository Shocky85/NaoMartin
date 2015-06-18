package naojava;

import com.aldebaran.qimessaging.*;
import com.aldebaran.qimessaging.helpers.al.*;

import java.lang.Object;

public class Nao {

    private static String PORT = "9559";
    private static String IP   = "172.16.6.117";

    static Session session;

    static ALLandMarkDetection markProxy;
    static ALTextToSpeech tts;
    static ALMotion motion;
    static ALRobotPosture posture;
    static ALMemory memProxy;
    static NaoSpeech speech;

    public static void main(String[] args) throws Exception {

        // Init session & connexion
        session        = new Session();
        Future<Void> future = session.connect("tcp://" + IP + ":" + PORT);
        future.get();

        // Init nao features
        tts     = new ALTextToSpeech(session);
        motion  = new ALMotion(session);
        posture = new ALRobotPosture(session);
        speech  = new NaoSpeech();
        int detectedLandmark = 0;
        tts.setLanguage("French");
        tts.say("Bonjour la France !");
        // motion.wakeUp();
        markProxy = new ALLandMarkDetection(session);
        while(detectedLandmark == 0) {
            detectedLandmark = detectLandmark();
            tts.say("Landmark " + detectedLandmark + " détecté !");
            NodeService sender = new NodeService();
            int exit = sender.get("/page/" + detectedLandmark);
            presentDestination(detectedLandmark);
            System.out.println(exit);
            detectedLandmark = 0;
        }
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

    public static void presentDestination(int id) throws Exception{
        switch(id) {
            case 170:
                tts.say(speech.getValleedessinges());
                break;
            case 130:
                tts.say(speech.getPuydufou());
                break;
            case 68:
                tts.say(speech.getZoodelafleche());
                break;
            case 187:
                tts.say(speech.getPlanetesauvage());
                break;
            default:
                tts.say("Destination inconnue !");
                break;
        }
    }
}