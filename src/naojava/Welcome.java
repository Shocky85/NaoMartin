/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naojava;

import com.aldebaran.qimessaging.Future;
import com.aldebaran.qimessaging.Session;
import com.aldebaran.qimessaging.helpers.al.ALMotion;
import com.aldebaran.qimessaging.helpers.al.ALRobotPosture;
import com.aldebaran.qimessaging.helpers.al.*;

public class Welcome {

    private static String PORT ="9559";
    private static String IP ="192.168.1.25";

    Session session;

    ALLandMarkDetection markProxy;
    ALTextToSpeech tts;
    ALMotion motion;
    ALRobotPosture posture;
    ALMemory memProxy;

    public void main(String[] args) throws Exception {

        // Init session & connexion
        this.session        = new Session();
        Future<Void> future = this.session.connect("tcp://" + IP + ":" + PORT);
        future.get();

        // Init nao features
        this.tts     = new ALTextToSpeech(session);
        this.motion  = new ALMotion(session);
        this.posture = new ALRobotPosture(session);


        this.tts.setLanguage("French");
        this.motion.wakeUp();

        this.markProxy = new ALLandMarkDetection(session);

        int detectedLandmark = this.detectLandmark();

        this.tts.say("Landmark " + detectedLandmark + " détecté !");

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
