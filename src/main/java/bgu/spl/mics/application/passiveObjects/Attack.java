package bgu.spl.mics.application.passiveObjects;

import java.util.List;


/**
 * Passive data-object representing an attack object.
 * You must not alter any of the given public methods of this class.
 * <p>
 * YDo not add any additional members/method to this class (except for getters).
 */
public class Attack {
    final List<Integer> serials;
    final int duration;   //in MilliSec

    /**
     * Constructor.
     */
    public Attack(List<Integer> serialNumbers, int duration) {
        this.serials = serialNumbers;
        this.duration = duration;
    }


    public int getDuration() {
        return duration;
    }


                            //-- Option one --

    public List<Integer> getSerials()
    {
        return serials;
    }

                       //-- Option two --

    public int getEwokReq()   //we get Serial number to initialize a Ewok
    {
        List<Integer> l = serials;
        int i = serials.get(0);
        serials.remove(0);
        return i;

    }




}
