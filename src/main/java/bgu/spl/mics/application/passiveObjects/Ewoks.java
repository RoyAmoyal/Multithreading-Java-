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

    private List<Ewok> ewokObj;
    private static int numberOfEwoks;

    private static class EwoksSingletonHolder {
        private static Ewoks instance = new Ewoks();
    }

    private Ewoks()
    {
        ewokObj = new ArrayList<>();
        for (int i = 0; i < numberOfEwoks; i++)
           ewokObj.add(new Ewok(i));
    }

    public Ewok getEwokObj(int i) {
        if(this.ewokObj.size() < i)
            throw new IllegalStateException();
        return this.ewokObj.get(i-1);
    }

    public static Ewoks getInstance(int numEwoks)
    {
        numberOfEwoks = numEwoks;
        return EwoksSingletonHolder.instance;
    }

    public static Ewoks getInstance()
    {
      return EwoksSingletonHolder.instance;
    }


}
