package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;

import java.util.HashMap;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private final long deActivationDuration;
    public R2D2Microservice(long duration) {
        super("R2D2");
        this.deActivationDuration = duration;
    }
//998371158
    @Override
    protected void initialize() {
        this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast terminateBroadcast) -> {
                this.terminate();
        });

        this.subscribeEvent(DeactivationEvent.class,
                (DeactivationEvent d) -> {
                    try{
                        Thread.sleep(this.deActivationDuration); // Deactivate the sheild generator.
                    }
                    catch (InterruptedException ignored){
                    }
                    this.complete(d,true);
                    System.out.println("R2D2: Bep , Bop, BEEP BEPP!" + "\n" + "C3P0-Translation-Of-R2D2: The shield generator is Off!");

                }
        );


    }
}
