package org.froz3n.kit

import org.froz3n.scala.test.base.UnitSpec

class KITParserSpec extends UnitSpec {

  var actionName = "install"
  var withWhat = "mysql"
  var withProperty = "-version 5.0"
  var withProperties = """-dest "c:\dev-programs\mysql\5.0""""

  "The KITParser.parse" should "have one result" in {
    // WHEN
    var commands = KITParser.parse(actionName)

    // THEN
    assert(!commands.isEmpty)
  }

  "The KITParser.parse result" should "have a command 'action'" in {
    // WHEN
    var command = KITParser.parse(actionName).get(0)

    // THEN
    expect(command.action) { actionName }
  }

  it should "have a command 'what'" in {
    // WHEN
    var command = KITParser.parse(actionName + " " + withWhat).get(0)

    // THEN
    expect(command.what.get) { withWhat }
  }

  it should "have a command 'property'" in {
    // WHEN
    var command = KITParser.parse(actionName + " " + withWhat + " " + withProperty).get(0)

    // THEN
    assert(!command.properties.get.isEmpty)
    expect(command.properties.get(0).name + " " + command.properties.get(0).value) { withProperty.tail }
  }

  it should "have a several command 'properties'" in {
    // WHEN
    var command = KITParser.parse(actionName + " " + withWhat + " " + withProperty + " " + withProperties).get(0)

    // THEN
    assert(!command.properties.get.isEmpty)
    expect(command.properties.get(0).name + " " + command.properties.get(0).value) { withProperty.tail }
    expect(command.properties.get(1).name + " " + command.properties.get(1).value) { withProperties.tail }
  }

  "The KITParser.processCommands" should "return executed actions" in {
    // GIVEN
    val command = Command("version")
    
    // WHEN
    val result = KITParser.processCommands(Option(List(command)))

    // THEN
    result.size should equal (1)
    result(0).getClass().getSimpleName() should be ("VersionAction")
  }

}