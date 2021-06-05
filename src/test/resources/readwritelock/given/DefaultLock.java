package test;

import lombok.ReadLock;
import lombok.WriteLock;

public class DefaultLock {

  private int value;

  @ReadLock
  public int getValue() {
    return this.value;
  }

  @WriteLock
  public void setValue(int value) {
    this.value = value;
  }
}
