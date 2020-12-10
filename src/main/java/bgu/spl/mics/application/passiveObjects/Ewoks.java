package bgu.spl.mics.application.passiveObjects;


import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {

    private ArrayList<Ewok> ewok = new ArrayList<Ewok>();  // collection of ewok objects  (not sure about the syntax)
    int numberOfEwoks;
    private static Ewoks instance = null;

    private Ewoks(int numEwoks)
    {
        this.numberOfEwoks = numEwoks;
        for (int i = 1; i<= this.numberOfEwoks; i++)  //we get as input the numberOfEwoks
           this.ewok.add(new Ewok(i));
    }

    public static Ewoks getInstance(int numEwoks)    // make it singelton
    {
        if (instance == null)
            instance = new Ewoks(numEwoks);
        return instance;
    }




}
