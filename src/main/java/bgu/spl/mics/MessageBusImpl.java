package bgu.spl.mics;


import java.util.Map;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBusImpl implements MessageBus {


	private final ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServicesMessageQueuesMap;
	private final ConcurrentHashMap<Class<? extends Event<?>>, ConcurrentLinkedQueue<MicroService>> eventsHashmap;
	private final ConcurrentHashMap<Class<? extends Broadcast> , ConcurrentLinkedQueue<MicroService>> broadcastsHashmap;
	private final ConcurrentHashMap<Event , Future> futuresHashmap;

	private static class MsgBusSingletonHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl()
	{
		microServicesMessageQueuesMap = new ConcurrentHashMap<>();
		eventsHashmap = new ConcurrentHashMap<>();
		broadcastsHashmap = new ConcurrentHashMap<>();
		futuresHashmap = new ConcurrentHashMap<>();
	}


	public static MessageBusImpl GetMessageBusInstance()
	{
		return MsgBusSingletonHolder.instance;  //only here the instance will create
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized(type) {
			if (!this.eventsHashmap.containsKey(type)) {
				ConcurrentLinkedQueue<MicroService> newLinkedQueue = new ConcurrentLinkedQueue<>();
				this.eventsHashmap.put(type, newLinkedQueue);
			}
			ConcurrentLinkedQueue<MicroService> currLinkedQueue = eventsHashmap.get(type);
			currLinkedQueue.add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized(type) {
			if (!broadcastsHashmap.containsKey(type)) {
				ConcurrentLinkedQueue<MicroService> newLinkedQueue = new ConcurrentLinkedQueue<>();
				broadcastsHashmap.put(type, newLinkedQueue);
			}
			ConcurrentLinkedQueue<MicroService> currLinkedQueue = broadcastsHashmap.get(type);
			currLinkedQueue.add(m);
		}
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
			Future futureToResolve = futuresHashmap.get(e);
			futureToResolve.resolve(result);
	}


	@Override
	public void sendBroadcast(Broadcast b)
	{
		/*In case threadOne is the ***only subscribed microService*** to broadcast of type b and threadTwo is sending Broadcast b
		but in parallel threadOne is unregistering so his MessageQueue doesn't exist anymore and b.getClass() isnt a key anymore
		in the broadcastsHashmap so Exception may appear in the line:
		"LinkedBlockingQueue<Message> currMessageQueue = microServicesMessageQueuesMap.get(item);"
		or in the line:
		"for (MicroService item : subscribedToTheBroadcast)"
		*/
		synchronized (b.getClass())
		{
			ConcurrentLinkedQueue<MicroService> subscribedToTheBroadcast = this.broadcastsHashmap.get(b.getClass());
			if(subscribedToTheBroadcast!=null){
				for (MicroService item : subscribedToTheBroadcast) { //Run over all the elements(microservices) of the list and adds the message to their queues.
					LinkedBlockingQueue<Message> currMessageQueue = microServicesMessageQueuesMap.get(item);
					try {
						currMessageQueue.put(b);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public  <T> Future<T> sendEvent(Event<T> e) {
		synchronized (e.getClass()) { // In case 2 threads are trying to send the *same type* of event and then the poll() might throw a nullPointerException
			if (!this.eventsHashmap.containsKey(e.getClass()))
				return null;

			if (this.eventsHashmap.get(e.getClass()).isEmpty())
				return null;
			/* --------------- The Future Part ----------------- */
			Future<T> newFuture = new Future<T>();
			this.futuresHashmap.put(e, newFuture);
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
			return newFuture;
		}
		}



	@Override
	public void register(MicroService m) {
		LinkedBlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>(); // Creates a safety consumer-producer queue with no maximum capacity
		this.microServicesMessageQueuesMap.put(m,messageQueue); //Puts the message's queue of microservice m on the hashmap, using microservice m as a key in the map.
	}

	@Override
	public void unregister(MicroService m) {
		for(Map.Entry<Class<? extends Event<?>>, ConcurrentLinkedQueue<MicroService>> item: this.eventsHashmap.entrySet()) {
			ConcurrentLinkedQueue<MicroService> currLinkedQueue = item.getValue();
				if (currLinkedQueue.contains(m))
					currLinkedQueue.remove(m);
		}

		for(Map.Entry<Class<? extends Broadcast> , ConcurrentLinkedQueue<MicroService>> item: this.broadcastsHashmap.entrySet()) {
			ConcurrentLinkedQueue<MicroService> currLinkedQueue = item.getValue();
				if (currLinkedQueue.contains(m))
					currLinkedQueue.remove(m);
		}

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
