package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {



   private long HanSoloFinish = 0;
   private long C3POFinish = 0;
   private long R2D2Deactivated = 0;

   private long LeiaTerminate;
   private long HanSoloTerminate;
   private long C3POTerminate;
   private long R2D2Terminate;
   private long LandoTerminate;


    private AtomicInteger TotalAttacks = new AtomicInteger(); //init to 0

    public void increment()
    {
        TotalAttacks.getAndIncrement();  // its like i++
    }




    private static class DiarySingletonHolder {
        private static Diary instance = new Diary();
    }

    private Diary(){
    };

    public static Diary getInstance()
    {
        return Diary.DiarySingletonHolder.instance;
    } //instance will create here




   //            --------     Lest start with the setters  ------------


    public void setTotalAttacks(AtomicInteger totalAttacks) {    //lets decide that Han will count.
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



    //            -------     getters  ------


    public AtomicInteger getTotalAttacks() {
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
