package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.services.MockMicroService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBusImplTest{


    private MessageBusImpl m1;

    @BeforeEach
    void setUp() {
        m1 = MessageBusImpl.GetMessageBusInstance();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSubscribeEvent() {  //The method is tested on testSendEvent() method.


    }

    @Test
    void testSubscribeBroadcast() {  //The method is tested on testSendBroadcast() method.
    }

    @Test
    void testComplete() {
        AttackEvent e1 = new AttackEvent(new ArrayList<Integer>(Arrays.asList(1,2)),100);
        MockMicroService l1 = new MockMicroService();
        MockMicroService h1 = new MockMicroService();
        m1.register(h1);
        h1.subscribeEvent(AttackEvent.class , (AttackEvent call) -> { new Callback<AttackEvent>() {
            @Override
            public void call(AttackEvent c){}
        };
        });
        Future<Boolean> f1 = l1.sendEvent(e1);
        h1.complete(e1,true);
            assertTrue(f1.isDone());     //checks if the complete resolved the future value.
        assertEquals(true , f1.get()); //checks if the resolved value is the true value result that expected on the complete method
        m1.unregister(h1);
    }

    /* In this test we check the functionally of the methods subscribeBroadcast and sendBroadcast of MessageBus because the
           implementation of microservice.subscribeBroadcast event and microservice.sendBroadcast
           are using the MessageBus subscribeBroadcast and sendBroadcast methods.
           In addition, the test for subscribeBroadcast method is contained in this test because they identical.
       */
    @Test
    void testSendBroadcast() {
        TerminateBroadcast b = new TerminateBroadcast();
        MockMicroService l1 = new MockMicroService();
        MockMicroService h1 = new MockMicroService();
        m1.register(h1);
        m1.register(l1);

        l1.subscribeBroadcast(TerminateBroadcast.class , (TerminateBroadcast call) -> { new Callback<TerminateBroadcast>()
        {
            @Override
            public void call(TerminateBroadcast c){}
        };
        });

        h1.subscribeBroadcast(TerminateBroadcast.class , (TerminateBroadcast call) -> { new Callback<TerminateBroadcast>()
        {
            @Override
            public void call(TerminateBroadcast c){}
        };
        });

        l1.sendBroadcast(b);
        try{
            Message msg = m1.awaitMessage(h1);
            assertEquals(b,msg);
        }
        catch (InterruptedException exception){
        }

        try{
            Message msg2 = m1.awaitMessage(l1);
            assertEquals(b,msg2);
        }
        catch (InterruptedException exception){
        }
        m1.unregister(h1);
        m1.unregister(l1);
    }


    /* In this test we check the functionally of the methods subscribeEvent and sendEvent and register of MessageBus because the
         implementation of microservice.subscribeEvent event and microservice.sendEvent and microservice.register
         are using the MessageBus subscribeEvent and sendEvent methods.
         In addition, the test for subscribeEvent method is contained in this test because they identical.
     */
    @Test
    void testSendEvent() {
        AttackEvent e = new AttackEvent(new ArrayList<Integer>(Arrays.asList(1,2)),100);
        MockMicroService l1 = new MockMicroService();
        MockMicroService h1 = new MockMicroService();
        m1.register(h1);
        h1.subscribeEvent(AttackEvent.class , (AttackEvent call) -> { new Callback<AttackEvent>() {
            @Override
            public void call(AttackEvent c){}
         };
        });

       Future<Boolean> f2 = l1.sendEvent(e);

       if(f2 == null)
           fail("sendEvent has returned null but there is a MicroService that has subscribed to the type of that event");

        try{
            Message msg = m1.awaitMessage(h1);
            assertEquals(e,msg);
        }
        catch (InterruptedException exception){
        }
        m1.unregister(h1);
    }


    @Test
    void testRegister()  {
        MockMicroService h1 = new MockMicroService();

        //Tests the scenario when the microservice manage to register illegally without calling the register method for that microservice/
        try {
            m1.awaitMessage(h1);
            fail("\n*Test failed:* " +
                    "The register method for microservice hasn't being called yet"+ "\n"
                    + "but the awaitMessage method didn't throw IllegalStateException," +
                    "\n"+ "which means the microservice manage to register illegally somehow");
        }
        catch(IllegalStateException e)
        {
            // Success, h1 is unregistered.
        }
        catch(InterruptedException i){
            // Success..
        }

        //Tests the scenario when the microservice has registered.
        m1.register(h1);
        m1.subscribeEvent(BombDestroyerEvent.class,h1);
        try {
            BombDestroyerEvent b1 = new BombDestroyerEvent();
            m1.sendEvent(b1);
            assertEquals(b1,m1.awaitMessage(h1));
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
        }
        m1.unregister(h1);

    }

    @Test
    void testUnregister() {  //no needed
    }


    @Test
    void testAwaitMessage() {  // Tests only the case where there's a message waiting to be fetched, and makes sure it is indeed fetched.
        MockMicroService h3 = new MockMicroService();    //checking the method on AttackEvent
        BombDestroyerEvent e = new BombDestroyerEvent();
        MockMicroService l1 = new MockMicroService();

        m1.register(h3);
       h3.subscribeEvent(BombDestroyerEvent.class , (BombDestroyerEvent call) -> { new Callback<BombDestroyerEvent>() { // checks the subscribe event.
            @Override
            public void call(BombDestroyerEvent c){}
         };
        });

        l1.sendEvent(e);

        try {
           Message m = m1.awaitMessage(h3);
            assertEquals(e,m);
        }
        catch (InterruptedException i){}

        m1.unregister(h3);
    }
}