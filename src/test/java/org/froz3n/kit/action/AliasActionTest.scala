package org.froz3n.kit.action

import java.io.ByteArrayOutputStream

import org.froz3n.kit.Command
import org.froz3n.kit.Property
import org.froz3n.scala.test.base.UnitSpec

class AliasActionTest extends UnitSpec {

  val uut = new AliasAction()
  val actionName = "alias"

  var stream: ByteArrayOutputStream = null
  before {
    stream = new ByteArrayOutputStream()
  }

  "The AliasAction.execute" should "show alias help" in {
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

  it should "add new alias" in {
    // GIVEN
    val command = Command(actionName, Option("add"))

    // WHEN
    uut.execute(command)

    // THEN
  }

  it should "add run alias" in {
    // GIVEN
    val command = Command(actionName, Option("run"), Option(List(Property("name","install-mysql"))))

    // WHEN
    uut.execute(command)

    // THEN
  }

}