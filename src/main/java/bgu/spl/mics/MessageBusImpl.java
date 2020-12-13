package bgu.spl.mics;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBusImpl implements MessageBus {

	private static MessageBusImpl instance = null;   //Singelton class
	private final ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServicesMessageQueuesMap;
	private final ConcurrentHashMap<Class<? extends Event<?>>, ConcurrentLinkedQueue<MicroService>> eventsHashmap;
	private final ConcurrentHashMap<Class<? extends Broadcast> , ConcurrentLinkedQueue<MicroService>> broadcastsHashmap;
	private final ConcurrentHashMap<Event , Future<?>> futuresHashmap;



	private MessageBusImpl()
	{
		microServicesMessageQueuesMap = new ConcurrentHashMap<>();
		eventsHashmap = new ConcurrentHashMap<>();
		broadcastsHashmap = new ConcurrentHashMap<>();
		futuresHashmap = new ConcurrentHashMap<>();
	}


	public static MessageBusImpl GetMessageBus()
	{
		if(instance==null)
			instance = new MessageBusImpl();
		return instance;
	}
	/*
	lei.asubsricbeto danceEvent
	HASHMAPEVENT:
	AttackEvent, LISTMICROSERVICE: Han - > c3p0
	terminateevent, listmicroservice: (han -> c3p0 -> r2d2 -> lando -> leia)
	dance Event, Listmicroservice: Leia, R2D2
	*/
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if(!this.eventsHashmap.containsKey(type)) {
			ConcurrentLinkedQueue<MicroService> newLinkedQueue = new ConcurrentLinkedQueue<>();
			this.eventsHashmap.put(type,newLinkedQueue);
		}
		ConcurrentLinkedQueue<MicroService> currLinkedQueue = eventsHashmap.get(type);
		currLinkedQueue.add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(!broadcastsHashmap.containsKey(type)) {
			ConcurrentLinkedQueue<MicroService> newLinkedQueue = new ConcurrentLinkedQueue<>();
			broadcastsHashmap.put(type,newLinkedQueue);
		}
		ConcurrentLinkedQueue<MicroService> currLinkedQueue = broadcastsHashmap.get(type);
		currLinkedQueue.add(m);
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future futureToResolve = futuresHashmap.get(e);
		futureToResolve.resolve(result);
	}


	@Override
	public void sendBroadcast(Broadcast b)
	{
		ConcurrentLinkedQueue<MicroService> subscribedToTheBroadcast = this.broadcastsHashmap.get(b.getClass());
		if(subscribedToTheBroadcast!=null){
			for(MicroService item: subscribedToTheBroadcast){ //Run over all the elements(microservices) of the list and adds the message to their queues.
				LinkedBlockingQueue<Message> currMessageQueue = microServicesMessageQueuesMap.get(item);
				try {
					currMessageQueue.put(b);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* UN FINISHED METHOD WE NEED TO FINISH IT */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if(!this.eventsHashmap.containsKey(e.getClass())) {
			return null;
		}
		/* --------------- The Event Part ----------------- */
		ConcurrentLinkedQueue<MicroService> currLinkedQueue = this.eventsHashmap.get(e.getClass()); // Finds the microservices list that subscribed to the type of this event.
        MicroService currMicroService = currLinkedQueue.poll(); //removes and returns the head of the list (the microservice that should handle the event in the round-robin)
		LinkedBlockingQueue<Message> currMessageQueue = this.microServicesMessageQueuesMap.get(currMicroService); //Finds the MessageQueue for the microservice that need to handle the event.
		try {
			currMessageQueue.put(e); // Adds the event to the MessageQueue of that microservice to handle it when possible.
		} catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}
		currLinkedQueue.add(currMicroService); // adds the currMicroservice to end of the link the subscribedQueue.
		/* --------------- The Future Part ----------------- */
		Future newFuture = new Future();
		this.futuresHashmap.put(e, newFuture); // adds a new future that belong to the event e
		return newFuture;
	}


	@Override
	public void register(MicroService m) {
		LinkedBlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>(); // Creates a safety consumer-producer queue with no maximum capacity
		this.microServicesMessageQueuesMap.put(m,messageQueue); //Puts the message's queue of microservice m on the hashmap, using microservice m as a key in the map.
	}

	@Override
	public void unregister(MicroService m) {
		this.microServicesMessageQueuesMap.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		LinkedBlockingQueue<Message> messageQueue = this.microServicesMessageQueuesMap.get(m);

		if(messageQueue==null)
			throw new IllegalStateException();
		try {
			Message message = messageQueue.take();
			return message;
		} catch (InterruptedException e) { throw new InterruptedException(m + ".awaitMessage got interrupted while waiting for a message");}

	}
}
