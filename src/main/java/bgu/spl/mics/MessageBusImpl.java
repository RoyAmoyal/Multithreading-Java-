package bgu.spl.mics;
import bgu.spl.mics.application.messages.AttackEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {


	private final ConcurrentHashMap<MicroService,BlockingQueue<Message>> microServicesMessageQueuesMap;
	/*Hashmaps for the types of the event/broadcasts.
	 contains a linkedQueue (its a linklist style FIFO) of the microservices
	*/
	// DONT FORGET TO CHECK ABOUT THE LINKEDQUEUE MAYBE WE NEED TO CHANGE IT TO DIFFRENT DATA STRUCTURE AND WITHOUT CONCURRUENT
	private final ConcurrentHashMap<Class<? extends Event<?>>, ConcurrentLinkedQueue<MicroService>> eventsHashmap;
	private final ConcurrentHashMap<Class<? extends Broadcast> , ConcurrentLinkedQueue<MicroService>> broadcastsHashmap;
	private final ConcurrentHashMap<Future<?> , ConcurrentLinkedQueue<MicroService>> futuresHashmap;



	private static class MsgSingletonHolder {    //private class that make the singleton thread safe
		private static MessageBusImpl instance = new MessageBusImpl();   //Singelton class
	}


	private MessageBusImpl()
	{
		microServicesMessageQueuesMap = new ConcurrentHashMap<>();
		eventsHashmap = new ConcurrentHashMap<>();
		broadcastsHashmap = new ConcurrentHashMap<>();
		futuresHashmap = new ConcurrentHashMap<>();
	}


	public static MessageBusImpl GetMessageBus()
	{
		return MsgSingletonHolder.instance;
	}


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
		
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	/* UN FINISHED METHOD WE NEED TO FINISH IT */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if(!this.eventsHashmap.contains(e))
			return null;
		ConcurrentLinkedQueue<MicroService> currLinkedQueue = this.eventsHashmap.get(e); // Finds the microservices list that subscribed to the type of this event.
        MicroService currMicroService = currLinkedQueue.poll(); //removes and returns the head of the list (the microservice that should handle the event in the round-robin)
        BlockingQueue<Message> currMessageQueue = this.microServicesMessageQueuesMap.get(currMicroService); //Finds the MessageQueue for the microservice that need to handle the event.
        currMessageQueue.add(e); // Adds the event to the MessageQueue of that microservice to handle it when possible.
		return null; // ********* need to handle how to return the future.
	}

	@Override
	public void register(MicroService m) {
		BlockingQueue<Message> messageQueue = new LinkedBlockingDeque<>(); // Creates a safety consumer-producer queue with no maximum capacity
		this.microServicesMessageQueuesMap.put(m,messageQueue); //Puts the message's queue of microservice m on the hashmap, using microservice m as a key in the map.
	}

	@Override
	public void unregister(MicroService m) {
		this.microServicesMessageQueuesMap.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		BlockingQueue<Message> messageQueue = this.microServicesMessageQueuesMap.get(m);
		if(messageQueue==null)
			throw new IllegalStateException();
		try {
			Message message = messageQueue.take(); // take() method of BlockingQueue is a blocking method that wait for new element in the queue if the queue is empty.
			return message;
		} catch (InterruptedException e) { throw new InterruptedException(m + ".awaitMessage got interrupted while waiting for a message");}

	}
}
