package bgu.spl.mics;

import bgu.spl.mics.application.messages.MockMicroService;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageBusImplTest {


    private MessageBusImpl m1;

    @BeforeEach
    void setUp() {
        m1 = MessageBusImpl.GetMessageBus();
        HanSoloMicroservice h1 = new HanSoloMicroservice();
        C3POMicroservice m2 = new C3POMicroservice();
    }

    @AfterEach
    void tearDown() {
        m1 = null;
    }


    @Test
    void testSubscribeEvent() {  //maybe SendEvent is enoght
    }

    @Test
    void testSubscribeBroadcast() {
    }

    @Test
    void testComplete() {

    }

    @Test
    void testSendBroadcast() {
        /*
        subscribeBroadcast(Class<? extends Broadcast> someBroadCastType, m1);
        subscribeBroadcast(Class<? extends Broadcast> someBroadCastType, m2);
        sendBroadcast(Broadcast b);
        Make sure m1 got the broadcast
        make sure m2 got the broadcast
        */
    }

    @Test
    void testSendEvent() {

    }

    @Test
    void testRegister() throws InterruptedException {
   //     HanSoloMicroservice h1 = new HanSoloMicroservice();
    //    mb.register(h1);
        HanSoloMicroservice h1 = new HanSoloMicroservice();

        try {
            m1.awaitMessage(h1);
            fail("\n*Test failed:* " +
                    "The register method for microservice hasn't being called yet"+ "\n"
                    + "but the awaitMessage method didn't throw IllegalStateException," +
                    "\n"+ "which means the microservice manage to register illegally somehow");
        }
        catch(IllegalStateException e)
        {
            System.out.println("Success");
            // Success
        }
        catch(InterruptedException i){
            // Success..
        };


        m1.register(h1);
        try {
            m1.awaitMessage(h1);
            //Success
        }
        catch(IllegalStateException e)
        {
            fail("\n*Test failed:* " +
                    "awaitMessage method throws IllegalStateException but the microservice has been registered. "+
                    "\n"+ "It suggests that the register method doesn't work properly ");
        }
        catch(InterruptedException i){
            // Success
        };



    }

    @Test
    void testUnregister() {  //no needed
    }

    @Test
    void testAwaitMessage() {  // test only the case where there's a message waiting to be fetched, and make sure it is indeed fetched.

    }
}