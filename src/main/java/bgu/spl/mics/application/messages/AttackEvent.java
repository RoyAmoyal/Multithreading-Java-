package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttackEvent implements Event<Boolean> {
    private final List<Integer> serials;
    private final long duration;

    public AttackEvent(List<Integer> s, long d){
        this.serials = s;
        Collections.sort(this.serials); // Sort the resources to prevent a deadlock.
        this.duration = d;
    }



    public long getDuration() {
        return duration;
    }

    public List<Integer> getSerials() {
        return serials;
    }
}
