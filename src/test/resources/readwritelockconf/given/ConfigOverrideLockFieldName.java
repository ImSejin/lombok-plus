package test;

import lombok.WriteLock;

public class ConfigOverrideLockFieldName {

  @WriteLock
  public void configOverrideLockFieldName() {
    System.out.println("lock");
  }
}


