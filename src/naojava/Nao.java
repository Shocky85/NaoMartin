package naojava;

import com.aldebaran.qimessaging.*;
import com.aldebaran.qimessaging.helpers.al.*;

import java.lang.Object;

public class Nao {

    private static String PORT = "9559";
    private static String IP   = "172.16.6.117";

    static Session session;

    static ALTextToSpeech tts;
    static ALMotion motion;
    static ALRobotPosture posture;
    static ALLandMarkDetection markProxy;
    static NaoSpeech speech;
    static NodeService sender;
    static ALMemory memProxy;


    public static void main(String[] args) throws Exception {

        // Init session & connexion
        session        = new Session();
        Future<Void> future = session.connect("tcp://" + IP + ":" + PORT);
        future.get();

        // Init nao features
        tts       = new ALTextToSpeech(session);
        motion    = new ALMotion(session);
        posture   = new ALRobotPosture(session);
        markProxy = new ALLandMarkDetection(session);
        speech    = new NaoSpeech();
        sender    = new NodeService();

        // Init variables
        int detectedLandmark = 0;

        tts.setLanguage("French");

        tts.say("Bonjour je m'appelle Martin. Bienvenue à l'office de tourisme !");
        motion.setBreathEnabled("Body", true);


        while(detectedLandmark == 0) {

            detectedLandmark = detectLandmark();

            if(detectedLandmark != 0) {
                tts.say("J'ai détecté le lieu numéro " + detectedLandmark);
                sender.get("/page/" + detectedLandmark);
                presentDestination(detectedLandmark);
                detectedLandmark = 0;
            }
        }
    }


    public static int detectLandmark() throws Exception {

        markProxy.subscribe("LandmarkDetected", 500, (float) 0.0);
        memProxy = new ALMemory(session);

        Object dataLandmark = memProxy.getData("LandmarkDetected");

        int timeToSpeak = 1000;
        while(dataLandmark.toString()=="[]") {

            if(timeToSpeak == 1000) {
                tts.say("Présentez moi un marqueur");
                timeToSpeak = 0;
            }

            markProxy.subscribe("LandmarkDetected", 500, (float) 0.0);
            dataLandmark = memProxy.getData("LandmarkDetected");

            if(dataLandmark.toString() != "[]") {
                String values = dataLandmark.toString();
                String[] results = values.split(",");
                int id = Integer.parseInt(results[8].replace("[", "").replace("]", "").replace(" ",""));
                return id;
            }

            ++timeToSpeak;
        }

        return 0;
    }

    public static void presentDestination(int id) throws Exception {

        String text;

        switch(id) {
            case 170:
                text = speech.getValleedessinges();
                break;
            case 130:
                text = speech.getPuydufou();
                break;
            case 68:
                text = speech.getZoodelafleche();
                break;
            case 187:
                text = speech.getPlanetesauvage();
                break;
            default:
                text = "Lieu inconnu !";
                break;
        }

        motion.wakeUp();
        motion.setBreathEnabled("Body", true);
        tts.say(text);
        motion.setBreathEnabled("Body", false);
        motion.rest();
    }
}