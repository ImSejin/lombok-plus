package test;

import lombok.WriteLock;

public class NamedLock {

  @WriteLock("namedLock")
  public void createNamedLock() {
    System.out.println("New lock with custom name");
  }
}


