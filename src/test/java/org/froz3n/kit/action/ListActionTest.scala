package org.froz3n.kit.action

import java.io.ByteArrayOutputStream

import org.froz3n.kit.Command
import org.froz3n.kit.action.loader.ActionLoaderObj
import org.froz3n.scala.test.base.UnitSpec

class ListActionTest extends UnitSpec {
  
  val uut = new ListAction()
  val actionName = "list"

  var stream:ByteArrayOutputStream = null
  before {
    ActionLoaderObj.sysLoader = Thread.currentThread().getContextClassLoader();
    stream = new ByteArrayOutputStream()
  }
  
  "The ListAction.execute" should "show list help" in {
    // GIVEN
    val command = Command(actionName)
    
    // WHEN
    Console.withOut(stream) {
		uut.execute(command)
    }
    
    // THEN
	val result = stream.toString.split("\n")
	println(stream.toString)
	result.size should be(2)
	result(0) should equal("For more details on each action use: list full")
	result(1) should equal("The available actions are: help install list version")
  }

  it should "run list full command " in {
    // GIVEN
    val command = Command(actionName, Option("full"))
    
    // WHEN
    Console.withOut(stream) {
		uut.execute(command)
    }
    
    // THEN
	val result = stream.toString
	result should include("help")
	result should include("list")
	result should include("version")
  }
  
}