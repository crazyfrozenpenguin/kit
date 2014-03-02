package org.froz3n.kit.action

import java.io.ByteArrayOutputStream

import org.froz3n.kit.Command
import org.froz3n.kit.action.loader.ActionLoaderObj
import org.froz3n.scala.test.base.UnitSpec

class VersionActionTest extends UnitSpec {

  val uut = new VersionAction()
  val actionName = "version"

  var stream: ByteArrayOutputStream = null
  before {
    ActionLoaderObj.sysLoader = Thread.currentThread().getContextClassLoader();
    stream = new ByteArrayOutputStream()
  }

  "The VersionAction.execute" should "show version help" in {
    // GIVEN
    val command = Command(actionName)

    // WHEN
    Console.withOut(stream) {
      uut.execute(command)
    }

    // THEN
    val result = stream.toString.split("\n")
    result.size should be(3)
    result(0) should equal("KIT - Ad hoc toolKIT system v" + uut.version)
    result(1) should equal("Usage: kit [action] (what) (-prop1 one -prop2 two ...)")
    result(2) should equal("Basic actions: list, help, version")
  }

  it should "run version for version command" in {
    // GIVEN
    val command = Command(actionName, Option(actionName))

    // WHEN
    Console.withOut(stream) {
      uut.execute(command)
    }

    // THEN
    val result = stream.toString.split("\n")
    result.size should be(1)
    result(0) should equal("Version v" + uut.version)
  }

}