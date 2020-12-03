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

    private ArrayList<Ewok> ewokObj = new ArrayList<Ewok>();  // collection of ewok objects  (not sure about the syntax)

    private static Ewoks instance = null;

    private Ewoks()
    {
        for (int i = 1; i<= numberOfEwoks;i++)  //we get as input the numberOfEwoks
           ewokObj.add(new Ewok(i));
    }

    public static Ewoks getInstance()    // make it singelton
    {
        if (instance == null)
            instance = new Ewoks();
        return instance;
    }




}
