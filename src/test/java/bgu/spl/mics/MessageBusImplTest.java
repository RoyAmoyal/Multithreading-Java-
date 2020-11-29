package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.MockMicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageBusImplTest{


    private MessageBusImpl m1;

    @BeforeEach
    void setUp() {
        m1 = MessageBusImpl.GetMessageBus();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSubscribeEvent() {  //maybe SendEvent is enoght
    }

    @Test
    void testSubscribeBroadcast() {
    }

    @Test
    void testComplete() {
        AttackEvent e1 = new AttackEvent();
        HanSoloMicroservice h1 = new HanSoloMicroservice();
        Future<AttackEvent> futureOfHanSolo = new Future<>();

        h1.complete(e1,true);
        assertTrue(futureOfHanSolo.isDone()); //checks if the complete resolved the future value.
        assertEquals(true , futureOfHanSolo.get()); //checks if the resolved value is the true value result that expected on the complete method
    }


    @Test
    void testSendBroadcast() {
        TerminateBroadcast b = new TerminateBroadcast();
        LeiaMicroservice l1 = new LeiaMicroservice(new Attack[0]);
        HanSoloMicroservice h1 = new HanSoloMicroservice();
        m1.register(h1);
        h1.subscribeBroadcast(TerminateBroadcast.class , (TerminateBroadcast call) -> { new Callback<TerminateBroadcast>()
        {
            @Override
            public void call(TerminateBroadcast c){}
        };
        });

        l1.sendBroadcast(b); // microservice.sendBroadcast is using MessageBus sendBroadcast method so we test the method anyways.
        try{
            Message msg = m1.awaitMessage(h1);
            assertEquals(b,msg);
        }
        catch (InterruptedException exception){
        };
    }

    /* In this test we check the functionally of the methods subscribeEvent and sendEvent because Implementation microservice.subscribe event and microservice.sendevent
        calls the message bus subscribeEvent and sendEvent methods
     */
    @Test
    void testSendEvent() {
        AttackEvent e = new AttackEvent();
        LeiaMicroservice l1 = new LeiaMicroservice(new Attack[0]);
        HanSoloMicroservice h1 = new HanSoloMicroservice();
        m1.register(h1);

        h1.subscribeEvent(AttackEvent.class , (AttackEvent call) -> { new Callback<AttackEvent>() { // checks the subscribe event.
            @Override
            public void call(AttackEvent c){};
         };
        });

        l1.sendEvent(e); // microservice.sendEvent is using MessageBus sendEvent method so we test the method anyways.
        try{
            Message msg = m1.awaitMessage(h1);
            assertEquals(msg,e);
        }
        catch (InterruptedException exception){
        };

    }


    @Test
    void testRegister() throws InterruptedException {
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