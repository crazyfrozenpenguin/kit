package org.froz3n.kit.action

import java.io.ByteArrayOutputStream
import org.froz3n.scala.test.base.UnitSpec
import org.froz3n.kit.Command
import org.froz3n.kit.action.loader.ActionLoaderObj
import org.froz3n.kit.Property

class BatchActionsTest extends UnitSpec {

  val uut = new BatchAction()
  val actionName = "batch"

  var stream: ByteArrayOutputStream = null
  before {
    ActionLoaderObj.sysLoader = Thread.currentThread().getContextClassLoader();
    stream = new ByteArrayOutputStream()
  }

  "The BatchActions.execute" should "show batch help" in {
    // GIVEN
    val command = Command("batch")

    // WHEN
    Console.withOut(stream) {
      uut.execute(command)
    }

    // THEN
    val result = stream.toString
    result should be(uut.help + "\n")
  }

  it should "execute all commands in batch file using -c property" in {
    // GIVEN
    val command = Command("batch", None, Option(List(Property("c", "target/test-classes/batch-test.txt"))))

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

  it should "execute all commands in batch file using -conf property" in {
    // GIVEN
    val command = Command("batch", None, Option(List(Property("conf", "target/test-classes/batch-test.txt"))))

    // WHEN
    Console.withOut(stream) {
      uut.execute(command)
    }

    // THEN    
    val result = stream.toString
    result should include("help")
    result should include("list")
    result should include("version")
    result should include("batch")
  }
}