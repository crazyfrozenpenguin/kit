package org.froz3n.kit.settings

import java.lang.System.getProperty
import java.lang.System.setProperty

import org.froz3n.kit.settings.xml.KitElem
import org.froz3n.scala.test.base.UnitSpec

class SettingsManagerTest extends UnitSpec {

  before {
    setProperty("user.home", getProperty("user.dir") + "/target/test-classes")
  }

  "SettingsManager.settings" should "exist" in {
    // WHEN
    val settings = SettingsManager.settings

    // THEN
    settings shouldNot be(None)
  }

  "SettingsManager.addRemoteRepository" should "not add existing repo" in {
    // GIVEN
    SettingsManager.settings = KitElem(<kit><repositories><repo name="test" url="http://localhost:8080"/><repo name="test1" url="http://localhost:8080/kit"/></repositories></kit>)

    // WHEN
    val result = SettingsManager.addRemoteRepository("test", "http://localhost:8080")

    // THEN
    result should be(false)
  }

  it should "add new repo" in {
    // GIVEN
    SettingsManager.settings = KitElem(<kit><repositories><repo name="test" url="http://localhost:8080/kit"/></repositories></kit>)

    // WHEN
    val result = SettingsManager.addRemoteRepository("test", "http://localhost:8080")

    // THEN
    result should be(true)
    SettingsManager.settings.toString should include("http://localhost:8080")
  }

  "SettingsManager.removeRemoteRepository" should "remove repo" in {
    // GIVEN
    SettingsManager.settings = KitElem(<kit><repositories><repo name="test" url="http://localhost:8080"/><repo name="test1" url="http://localhost:8080/kit"/></repositories></kit>)

    // WHEN
    val result = SettingsManager.removeRemoteRepository("test")

    // THEN
    result should be(true)
  }
  
  "SettingsManager.addAction" should "add new action" in {
    // GIVEN
    SettingsManager.settings = KitElem(<kit><actions/></kit>)

    // WHEN
    val action = <myAction foo="baa">action text</myAction>
    val result = SettingsManager.addAction(action)

    // THEN
    result should be(true)
    SettingsManager.getActions(action.label)(0).toXML should equal(action)
  }
}