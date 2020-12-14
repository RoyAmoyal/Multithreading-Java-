package bgu.spl.mics.application.services;


import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    public HanSoloMicroservice() {
        super("Han");
    }




    @Override
    protected void initialize() {



        this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast terminateBroadcast) -> {
            System.out.println("HanSolo: Never tell me the odds!");
            Diary.getInstance().setHanSoloTerminate(System.currentTimeMillis());
            this.terminate();
        });

      this.subscribeEvent(AttackEvent.class,(AttackEvent attackEvent) -> {
          long startHan = System.currentTimeMillis();
          List<Integer> ewoksSerialsList = attackEvent.getSerials();
          long fightDuration = attackEvent.getDuration();
          Ewoks ewoksList = Ewoks.getInstance();
          Ewok currEwok = null;
          for(Integer ewokSerialNum: ewoksSerialsList) { //Acquire all the resources for that attack event.
              currEwok = ewoksList.getEwokObj(ewokSerialNum);
              currEwok.acquire();
          }
          // after han succeed get all the resources we needs
          try {
              Thread.sleep(fightDuration); // sleep = execute the attack event for the fight duration
          }catch (InterruptedException e){}

          Diary.getInstance().setHanSoloFinish( Diary.getInstance().getHanSoloFinish()  +  System.currentTimeMillis() - startHan);
          System.out.println("Han: Im done with the attackevent: " + attackEvent);
          this.complete(attackEvent,true);

          for(Integer ewokSerialNum: ewoksSerialsList) { //Release all the resources for that attack event.
              currEwok = ewoksList.getEwokObj(ewokSerialNum);
              currEwok.release();
          }

          Diary.getInstance().increment();

      });


    }
}
