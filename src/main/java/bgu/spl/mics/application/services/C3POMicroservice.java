package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
	
    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize()
    {

        AtomicInteger atomicIntegerC3 = new AtomicInteger();

        this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast terminateBroadcast) -> {
            System.out.println("C3P0: It's times like this that I really feel like shutting down.");
            Diary.getInstance().setC3POTerminate(System.currentTimeMillis());
            this.terminate();
        });

        this.subscribeEvent(AttackEvent.class,(AttackEvent attackEvent) ->
        {
            long startC3 = System.currentTimeMillis();
            List<Integer> ewoksSerialsList = attackEvent.getSerials();
            long fightDuration = attackEvent.getDuration();
            Ewoks ewoksList = Ewoks.getInstance();
            Ewok currEwok = null;
            for (Integer ewokSerialNum : ewoksSerialsList) {
                currEwok = ewoksList.getEwokObj(ewokSerialNum);
                currEwok.acquire();
            }
            // after han succeed get all the resources we needs
            try {
                Thread.sleep(fightDuration); //sleep = execute the attack event for the fight duration
            } catch (InterruptedException e) {}
            long test =  System.currentTimeMillis() - startC3;
            Diary.getInstance().setC3POFinish( Diary.getInstance().getC3POFinish()  +  System.currentTimeMillis() - startC3);
            System.out.println("C3P0: Im done with the attackevent: " + attackEvent);
          //  System.out.println("The time for that attack event is " + test);
            this.complete(attackEvent,true);

            for(Integer ewokSerialNum: ewoksSerialsList) { //Release all the resources for that attack event.
                currEwok = ewoksList.getEwokObj(ewokSerialNum);
                currEwok.release();
            }

            Diary.getInstance().increment();

         });



    }

}