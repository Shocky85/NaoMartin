/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package naojava;

import com.aldebaran.qimessaging.Future;
import com.aldebaran.qimessaging.Session;

public class NaoJava{
    public static void main(String[] args) throws Exception{
        Session session = new Session();
        Future<Void> future=session.connect("tcp//hal.local:9559");
        future.get();
        
        com.aldebaran.qimessaging.Object tts= null;
        tts= session.service("ALTextToSppech");
        
        boolean ping = tts.<Boolean>call("ping").get();
        
        if(ping){
            System.out.println("Ping ok");
            tts.call("say","Bonjour tout le monde");
        }
    }
}

