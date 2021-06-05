package lombok.sample;

import lombok.ReadLock;
import lombok.WriteLock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class LockSample {

    private final ReentrantReadWriteLock existingLock = new ReentrantReadWriteLock();
    private int value;

    @WriteLock
    public void setValue(int value) {
        this.value = value;
    }

    @ReadLock
    public int getValue() {
        return this.value;
    }

    @WriteLock("namedLock")
    public void createNamedLock() {
        System.out.println("New lock with custom name");
    }

    @ReadLock("existingLock")
    public void reuseExistingLock() {
        System.out.println("Reuse existing lock");
    }

}
