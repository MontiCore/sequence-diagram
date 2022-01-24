/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development.sd2java;

import com.example.Bid;
import com.example.Foo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SD2JavaTest {

  @Test
  public void test() {
    Foo foo = new Foo();
    Bid bid = new Bid(foo);

    bid.testMethod();

    assertEquals(1, foo.getDoStuffCallCount());
  }

}
