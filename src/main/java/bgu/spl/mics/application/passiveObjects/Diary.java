package bgu.spl.mics.application.passiveObjects;


/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

   private int TotalAttacks; // we can use AtomicInteger for this

   private long HanSoloFinish;    // We can use  System.currentTimeMillis(). for all this 8 methods
   private long C3POFinish;
   private long R2D2Deactivated;

   private long LeiaTerminate;
   private long HanSoloTerminate;
   private long C3POTerminate;
   private long R2D2Terminate;
   private long LandoTerminate;



   //            --------     Lest start with the setters  ------------


    public void setTotalAttacks(int totalAttacks) {    //lets decide that Han will count.
        TotalAttacks = totalAttacks;
    }

    public void setHanSoloFinish(long hanSoloFinish)
    {
        HanSoloFinish = hanSoloFinish;
    }

    public void setC3POFinish(long c3POFinish) {
        C3POFinish = c3POFinish;
    }

    public void setR2D2Deactivated(long r2D2Deactivated) {
        R2D2Deactivated = r2D2Deactivated;
    }


    //      -  Terminate sets  -

    public void setLeiaTerminate(long leiaTerminate) {
        LeiaTerminate = leiaTerminate;
    }

    public void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }

    public void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    public void setR2D2Terminate(long r2D2Terminate) {
        R2D2Terminate = r2D2Terminate;
    }

    public void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }



    //            -------     getters ze hadibur ------


    public int getTotalAttacks() {
        return TotalAttacks;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getR2D2Deactivated() {
        return R2D2Deactivated;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

}
