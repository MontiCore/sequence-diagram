package com.example;

public final class Foo {

  private int cDoStuff = 0;
  public int getDoStuffCallCount() { return this.cDoStuff; }

  public void doStuff() {
    cDoStuff++;
  }

}
