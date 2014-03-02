package org.froz3n.kit.action

import java.io.ByteArrayOutputStream
import org.froz3n.scala.test.base.UnitSpec
import org.froz3n.kit.action.loader.ActionLoaderObj
import org.froz3n.kit.Command
import org.froz3n.kit.Property


class ExecActionTest extends UnitSpec {

  val uut = new ExecAction()
  val actionName = "exec"

  var stream: ByteArrayOutputStream = null
  before {
    ActionLoaderObj.sysLoader = Thread.currentThread().getContextClassLoader();
    stream = new ByteArrayOutputStream()
  }

  "The ExecAction.execute" should "show exec help" in {
    // GIVEN
    val command = Command(actionName)

    // WHEN
    Console.withOut(stream) {
      uut.execute(command)
    }

    // THEN
    val result = stream.toString
    result should equal(uut.help + "\n")
  }

  it should "execute command" in {
    // GIVEN
    val command = Command(actionName, None, Option(List(Property("command","gnome-terminal"))))
    
    // WHEN
    uut.execute(command)
    
    // THEN
    
  }
  
}