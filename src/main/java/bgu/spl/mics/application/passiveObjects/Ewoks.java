package bgu.spl.mics.application.passiveObjects;


import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

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
    int numberOfEwoks; // we get it from the json

    private static class EwoksSingletonHolder {  // the class that make the singleton thread safe
        private static Ewoks instance = new Ewoks();
    }

    private Ewoks()
    {
        for (int i = 1; i<= numberOfEwoks; i++)  //we get as input the numberOfEwoks
           ewokObj.add(new Ewok(i));
    }

    public static Ewoks getInstance()    // make it singelton
    {
        return EwoksSingletonHolder.instance;
    }




}
