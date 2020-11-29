package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTeste{

    MessageBus m1;
    @BeforeEach
    void setUp() {
    m1 = new MessageBusImpl();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSubscribeEvent() {

    }

    @Test
    void testSubscribeBroadcast() {
    }

    @Test
    void testComplete() {
    AttackEvent e1 = new AttackEvent();
    HanSoloMicroservice h1 = new HanSoloMicroservice();
    LeiaMicroservice l1 = new LeiaMicroservice(new Attack[0]);
    Future<AttackEvent> futureOfHanSolo = new Future<>();
    m1.register(h1);
    h1.subscribeEvent(AttackEvent.class , (AttackEvent call) -> { new Callback<AttackEvent>() {
                @Override
                public void call(AttackEvent c){};
    };
        });
    l1.sendEvent(e1);
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

    @Test
    void testSendEvent() {
        AttackEvent e = new AttackEvent();
        LeiaMicroservice l1 = new LeiaMicroservice(new Attack[0]);
        HanSoloMicroservice h1 = new HanSoloMicroservice();
        h1.subscribeEvent(AttackEvent.class , (AttackEvent call) -> { new Callback<AttackEvent>() { // checks the subscribe event.
                @Override
             public void call(AttackEvent c){};
            };
        });

        m1.register(h1);
        l1.sendEvent(e); // microservice.sendEvent is using MessageBus sendEvent method so we test the method anyways.
        try{
        Message msg = m1.awaitMessage(h1);
        assertEquals(msg,e);
                }
        catch (InterruptedException exception){
        };
    }


    @Test
    void testRegister()  {
        HanSoloMicroservice h1 = new HanSoloMicroservice();
        try {
            m1.awaitMessage(h1);
            fail("\n*Test failed:* " +
                    "The register method for microservice hasn't being called yet"+ "\n"
                    + "but the awaitMessage method didn't throw IllegalStateException," +
                    "\n"+ "which means the microservice manage to register illegally somehow");
        } catch(IllegalStateException e)
        {
            // Success
        }
        catch(InterruptedException i){
            // Success
        };


        m1.register(h1);
        try {
            m1.awaitMessage(h1);

        }
        catch(IllegalStateException e)
        {
            fail("\n*Test failed:* " +
                    "awaitMessage method throws IllegalStateException but the microservice has been registered. "+
                    "\n"+ "It suggests that the register method doesn't work properly ");
        }
        catch(InterruptedException i){};
            // Success
    }

    @Test
    void unregister() {
    }

    @Test
    void awaitMessage() {
        HanSoloMicroservice h1 = new HanSoloMicroservice();
        m1.register(h1);
        h1.sendBroadcast(new Broadcast() {});
        try {
            m1.awaitMessage(h1);
            //Success
        }
        catch(InterruptedException e){
            fail("\n*Test failed:* " +
                    "awaitMessage method throws InterruptedException but there is available message in the microservice's queue"+
                    "\n" + "and no one interrupted the microservice. It suggests that the awaitMessage method doesn't work properly");
        }
    }
}