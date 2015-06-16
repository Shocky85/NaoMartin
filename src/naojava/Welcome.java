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
    private static ALLandMarkDetection markProxy;

    public static void main(String[] args) throws Exception {

        Session session = new Session();
        Future<Void> future = session.connect("tcp://" + IP + ":" + PORT);
        future.get();

        ALTextToSpeech tts = new ALTextToSpeech(session);
        ALMotion motion = new ALMotion(session);
        ALRobotPosture posture = new ALRobotPosture(session);

        tts.setLanguage("French");

        markProxy = new ALLandMarkDetection(session);
        markProxy.subscribe("LandmarkDetected", 500, (float) 0.0);
        ALMemory memProxy = new ALMemory(session);

        tts.say("En attente de détection ...");
        Object data = memProxy.getData("LandmarkDetected");

        while(data.toString()=="[]") {

            markProxy.subscribe("LandmarkDetected", 500, (float) 0.0);
            data = memProxy.getData("LandmarkDetected");

            if(data.toString() != "[]") {
                String values = data.toString();
                String[] results = values.split(",");
                int id = Integer.parseInt(results[8].replace("[", "").replace("]", "").replace(" ",""));
                System.out.println(id);
                tts.say("J'ai détecté le numéro "+id);
            }
        }
        /*
        System.out.println(data);
        if (data.toString()=="[]") {
            tts.say("Je n'ai rien détecté");
        } else{
            String values = data.toString();
            String[] results = values.split(",");
            int id = Integer.parseInt(results[8].replace("[", "").replace("]", "").replace(" ",""));
            System.out.println(id);
            tts.say("J'ai détecté le numéro "+id);
        }
        */
    }



    //motion.wakeUp();
}
