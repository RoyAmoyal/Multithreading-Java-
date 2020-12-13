package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private final long bombDestroyerDuration;
    public LandoMicroservice(long duration) {
        super("Lando");
        this.bombDestroyerDuration = duration;
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast terminateBroadcast) -> {
            System.out.println("Lando: Yeah, I’m responsible these days. It’s the price you pay for being successful.");
            this.terminate();
        });

        this.subscribeEvent(BombDestroyerEvent.class, (BombDestroyerEvent bombDestroyerEvent) -> {
                    try {
                        Thread.sleep(this.bombDestroyerDuration); // Deactivate the sheild generator.
                    }
                    catch (InterruptedException e) {}

                    System.out.println("Lando: You know, that ship’s saved my life quite a few times. She’s the fastest hunk o’ junk in the galaxy!");
                    this.complete(bombDestroyerEvent, true);
                }
        );




    }
}
