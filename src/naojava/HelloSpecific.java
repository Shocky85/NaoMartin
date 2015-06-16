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
import com.aldebaran.qimessaging.helpers.al.ALTextToSpeech;

public class HelloSpecific {

    public static void main(String[] args) throws Exception {
        Session session = new Session();
        Future<Void> future = session.connect("tcp://172.16.6.117:9559");
        future.get();

        ALTextToSpeech tts = new ALTextToSpeech(session);
        ALMotion motion=new ALMotion(session);
        ALRobotPosture posture = new ALRobotPosture(session);

        tts.setLanguage("French");
        tts.say("Hello");
        
        motion.wakeUp();
        
        posture.goToPosture("StandInit", (float)1.0);
        
        motion.rest();
        
    }
}
