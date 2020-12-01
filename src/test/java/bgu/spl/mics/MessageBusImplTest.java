package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.services.MockMicroService;
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
    void testSubscribeEvent() {  //The method is tested on testSendEvent() method.
    }

    @Test
    void testSubscribeBroadcast() {  //The method is tested on testSendBroadcast() method.
    }

    @Test
    void testComplete() {
        AttackEvent e1 = new AttackEvent();
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
        assertTrue(f1.isDone()); //checks if the complete resolved the future value.
        assertEquals(true , f1.get()); //checks if the resolved value is the true value result that expected on the complete method

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
    }

    /* In this test we check the functionally of the methods subscribeEvent and sendEvent of MessageBus because the
         implementation of microservice.subscribeEvent event and microservice.sendEvent
         are using the MessageBus subscribeEvent and sendEvent methods.
         In addition, the test for subscribeEvent method is contained in this test because they identical.
     */
    @Test
    void testSendEvent() {
        AttackEvent e = new AttackEvent();
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
            assertEquals(msg,e);
        }
        catch (InterruptedException exception){
        }

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
        }

    }

    @Test
    void testUnregister() {  //no needed
    }


    @Test
    void testAwaitMessage() {  // Tests only the case where there's a message waiting to be fetched, and makes sure it is indeed fetched.
        MockMicroService h1 = new MockMicroService();    //checking the method on AttackEvent
        AttackEvent e = new AttackEvent();
        MockMicroService l1 = new MockMicroService();
        m1.register(h1);
       h1.subscribeEvent(AttackEvent.class , (AttackEvent call) -> { new Callback<AttackEvent>() { // checks the subscribe event.
            @Override
            public void call(AttackEvent c){}
         };
        });

        l1.sendEvent(e);

        try {
           Message m = m1.awaitMessage(h1);
            assertEquals(m,e);
        }
        catch (InterruptedException i){}

    }
}