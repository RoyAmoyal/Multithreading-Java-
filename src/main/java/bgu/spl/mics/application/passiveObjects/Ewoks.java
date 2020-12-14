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

    private List<Ewok> ewokObj;  // collection of ewok objects  (not sure about the syntax)
    private static int numberOfEwoks; // we get it from the json

    private static class EwoksSingletonHolder {  // the class that make the singleton thread safe
        private static Ewoks instance = new Ewoks();
    }

    private Ewoks()
    {
        ewokObj = new ArrayList<>();
        for (int i = 0; i < numberOfEwoks; i++)  //we get as input the numberOfEwoks
           ewokObj.add(new Ewok(i));
    }

    public Ewok getEwokObj(int i) {
        if(this.ewokObj.size() < i)
            throw new IllegalStateException();
        return this.ewokObj.get(i-1);
    }

    public static Ewoks getInstance(int numEwoks)    // make it singelton
    {
        numberOfEwoks = numEwoks;
        return EwoksSingletonHolder.instance;
    }

    public static Ewoks getInstance()
    {
      return EwoksSingletonHolder.instance;
    }


}
