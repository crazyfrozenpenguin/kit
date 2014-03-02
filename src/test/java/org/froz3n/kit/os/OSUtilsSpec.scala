package org.froz3n.kit.os

import org.froz3n.scala.test.base.UnitSpec

class OSUtilsSpec extends UnitSpec {

  "OSUtils Test" should "isUnix" in {
    // When
    val result = OSUtils.isUnix
    
    // Then
    result should be(true)
  }

  it should "return unix shell command" in {
    // When
    val result = OSUtils.getShell
    
    // Then
    result should equal(Array("/bin/bash","-c"))
  }
}