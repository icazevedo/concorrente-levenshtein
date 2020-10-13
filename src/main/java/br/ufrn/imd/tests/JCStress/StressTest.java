package br.ufrn.imd.tests.JCStress;

import br.ufrn.imd.concorrente.WordRunner;
import org.openjdk.jcstress.annotations.*;

import java.util.concurrent.atomic.AtomicInteger;

public class StressTest {
    @State
    public static class MyState{
        AtomicInteger smallerDistance;
        MyState(){
            smallerDistance = new AtomicInteger(Integer.MAX_VALUE);
        }
    }

    @JCStressTest
    @Description("Test smaller distance check and update")
    @Outcome(id= {"7"}, expect = Expect.ACCEPTABLE, desc = "Valid")
    public static class UpdateDistanceTest {
        int distance1 = 15;
        int distance2 = 7;

        @Actor
        public void actor0(MyState myState, StressTestResult r) {
            WordRunner.checkAndUpdateDistance(myState.smallerDistance, distance1);
        }

        @Actor
        public void actor1(MyState myState, StressTestResult r) {
            WordRunner.checkAndUpdateDistance(myState.smallerDistance, distance2);
        }

        @Arbiter
        public void arbiter(MyState myState, StressTestResult r){
            r.smallerDistance = myState.smallerDistance.get();
        }
    }
}
