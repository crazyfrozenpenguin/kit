package org.froz3n.kit.action

import java.io.ByteArrayOutputStream
import org.froz3n.scala.test.base.UnitSpec
import org.froz3n.kit.Command
import org.froz3n.kit.action.loader.ActionLoaderObj

class HelpActionTest extends UnitSpec {
  
  val uut = new HelpAction()
  val actionName = "help"
    
  var stream:ByteArrayOutputStream = null
  before {
    ActionLoaderObj.sysLoader = Thread.currentThread().getContextClassLoader();
    stream = new ByteArrayOutputStream()
  }
  
  "The HelpAction.execute" should "show help help" in {
    // GIVEN
    val stream = new java.io.ByteArrayOutputStream()
    val command = Command(actionName)
    
    // WHEN
    Console.withOut(stream) {
		uut.execute(command)
    }
    
    // THEN
	val result = stream.toString.split("\n")
	result.size should be(2)
	result(0) should equal("help [command name]")
	result(1) should equal("  Shows this help or the help for the given command name.")
  }

  it should "run help for help command" in {
    // GIVEN
    val stream = new java.io.ByteArrayOutputStream()
    val command = Command(actionName, Option(actionName))
    
    // WHEN
    Console.withOut(stream) {
		uut.execute(command)
    }
    
    // THEN
	val result = stream.toString.split("\n")
	result.size should be(2)
	result(0) should equal("help [command name]")
	result(1) should equal("  Shows this help or the help for the given command name.")
  }
  
}