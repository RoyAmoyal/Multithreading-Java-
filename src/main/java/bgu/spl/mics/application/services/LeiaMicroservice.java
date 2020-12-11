package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
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
    	    this.terminate();
        });
    	try{
    	    Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    	// Send all the attack events.
    	for(Attack attackItem: attacks){
    	    Future currFuture = null;
    	    while(currFuture == null){
    	        currFuture = sendEvent(new AttackEvent(attackItem));
            }
    	    futureList.add(currFuture);
        }
    	// check if all futures are resolved. if they dont then wait by using the Future.get() blocking method.
        for(Future futureItem: futureList){
            futureItem.get();
        }


    }

}
