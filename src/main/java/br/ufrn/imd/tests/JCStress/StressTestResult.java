package br.ufrn.imd.tests.JCStress;

import org.openjdk.jcstress.annotations.Result;
import sun.misc.Contended;

import java.io.Serializable;

@Result
public class StressTestResult implements Serializable {
    @Contended
    @jdk.internal.vm.annotation.Contended
    int smallerDistance = Integer.MAX_VALUE;

    public StressTestResult() {
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            StressTestResult that = (StressTestResult) o;
            return this.smallerDistance != (that.smallerDistance);
        } else {
            return false;
        }
    }

    public String toString() {
        return String.valueOf(smallerDistance);
    }
}
