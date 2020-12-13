package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	int serialNumber;
	boolean available;
	/* maybe it will fix the deadlock, cant be sure yet
	public static boolean state1 = false;
	public static boolean state2 = false;
	*/

	public Ewok (int s)    // When we create Ewok its should be available
    {
        serialNumber = s;
        available = true;
    }
	
  
    /**
     * Acquires an Ewok
     */
    public synchronized void acquire() {
        while(!this.available)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e) { }
        }
        available = false;
    }

    /**
     * release an Ewok
     */
    public void release() {
        available = true;
    }
}
