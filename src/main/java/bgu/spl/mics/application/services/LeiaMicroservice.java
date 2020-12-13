package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.AttackEvent;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private final ArrayList<Future> futureList;




    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		this.futureList = new ArrayList<>();
    }

    @Override
    protected void initialize() {
    	this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast terminateBroadcast) -> {
    	    System.out.println("Leia: Hope is not lost today. It is found");
    	    this.terminate();
        });

    	try{
    	    Thread.sleep(100); //let han and c3p0 to successfully register.
        } catch (InterruptedException e) {
        }
    	// Send all the attack events.
    	for(Attack attackItem: attacks){
    	    Future currFuture = null;
    	    while(currFuture == null){
    	        currFuture = sendEvent(new AttackEvent(attackItem.getSerials(),attackItem.getDuration()));
            }
    	    futureList.add(currFuture);
        }
    	// check if all futures are resolved. if they dont then wait by using the Future.get() blocking method.
        for(Future futureItem: futureList){
            futureItem.get();
        }

        Future r2d2Future = null;
        while(r2d2Future==null){
            r2d2Future = sendEvent(new DeactivationEvent());
        }
        r2d2Future.get(); //waiting for R2D2 to finish the deactivation

        Future landoFuture = null;
        while(landoFuture==null){
            landoFuture = sendEvent(new BombDestroyerEvent());
        }
        landoFuture.get();// waiting for Lando to strike the last finish attack and win the Battle Of Endor

        this.sendBroadcast(new TerminateBroadcast());

    }

}
