package org.froz3n.kit.settings.xml

import java.lang.System.getProperty
import java.lang.System.setProperty
import scala.xml.Unparsed
import org.froz3n.scala.test.base.UnitSpec

class KitElemTest extends UnitSpec {

  before {
    setProperty("user.home", getProperty("user.dir") + "/target/test-classes")
  }

  "KitElem(xmlElement)" should "parse simple document" in {
    // GIVEN
    val settings = <kit></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.repositories should be(None)
  }

  it should "parse empty repositories" in {
    // GIVEN
    val settings = <kit><repositories/></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.repositories.get.repoList.size should be(0)
  }

  it should "parse empty actions" in {
    // GIVEN
    val settings = <kit><actions/></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.actions.get.actionList.size should be(0)
  }

  it should "parse all repo entries" in {
    // GIVEN
    val settings = <kit><repositories><repo name="repoA" url="http://localhost:8080"/><repo name="repoB" url="file:///path/to/dir"/></repositories></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.repositories.get.repoList.size should be(2)
  }

  it should "parse all action entries" in {
    // GIVEN
    val settings = <kit><actions><myAction1 key1="value1"/><myAction2 key2="value2"/></actions></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.actions.get.actionList.size should be(2)
  }

  it should "parse repo url attribute in provided order" in {
    // GIVEN
    val settings = <kit><repositories><repo name="repoA" url="http://localhost:8080"/><repo name="repoB" url="file:///path/to/dir"/></repositories></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.repositories.get.repoList(0).url.get should equal("http://localhost:8080")
    uut.repositories.get.repoList(1).url.get should equal("file:///path/to/dir")
  }

  it should "parse action attribute in provided order" in {
    // GIVEN
    val settings = <kit><actions><myAction1 key1="value1"/><myAction2 key2="value2"/></actions></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    val result1 = uut.actions.get.actionList(0)
    result1.getTagName should equal("myAction1")
    result1.getAttribute("key1").get should equal("value1")

    val result2 = uut.actions.get.actionList(1)
    result2.getTagName should equal("myAction2")
    result2.getAttribute("key2").get should equal("value2")
  }
  
  "KitElem.toXML" should "generate document" in {
    // GIVEN
    val settings = <kit></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.toXML should equal(settings)
  }

  it should "include empty repositories element" in {
    // GIVEN
    val settings = <kit><repositories/></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.toXML should equal(settings)
  }

  it should "include empty actions element" in {
    // GIVEN
    val settings = <kit><actions/></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.toXML should equal(settings)
  }
  
  it should "include repo element" in {
    // GIVEN
    val settings = <kit><repositories><repo/></repositories></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.toXML should equal(settings)
  }

  it should "include action element" in {
    // GIVEN
    val settings = <kit><actions><myAction1/></actions></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.toXML should equal(settings)
  }

  it should "include repo url element" in {
    // GIVEN
    val settings = <kit><repositories><repo name="repoA" url="http://localhost:8000"/></repositories></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.toXML should equal(settings)
  }

  it should "include action key/value" in {
    // GIVEN
    val settings = <kit><actions><myAction1 key1="value1"/></actions></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.toXML should equal(settings)
  }

  it should "include multiple repo url element" in {
    // GIVEN
    val settings = <kit><repositories><repo name="repoA" url="http://localhost:8000"/><repo name="repoB" url="file:///path/to/dir"/></repositories></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.toXML should equal(settings)
  }

  it should "include multiple action elements" in {
    // GIVEN
    val settings = <kit><actions><myAction1 key1="value1" key2="value2"/><myAction2 key1="value1"/></actions></kit>

    // WHEN
    val uut = KitElem(settings)

    // THEN
    uut.toXML should equal(settings)
  }
  
  "KitElem" should "allow for dynamic addition of new repo" in {
    // GIVEN
    val settings = <kit><repositories><repo name="repoA" url="http://localhost:8000"/></repositories></kit>
    val expected = <kit><repositories><repo name="repoA" url="http://localhost:8000"/><repo name="repoB" url="file:///path/to/dir"/></repositories></kit>

    // WHEN
    val uut = KitElem(settings)
    var repo = RepoElem()
    repo.name = Option("repoB")
    repo.url = Option("file:///path/to/dir")
    uut.repositories.get.addRepo(repo)

    // THEN
    uut.toXML should equal(expected)
  }

  "KitElem" should "allow for dynamic addition of new action" in {
    // GIVEN
    val settings = <kit><actions><myAction1 key1="value1"/></actions></kit>
    val expected = <kit><actions><myAction1 key1="value1"/><myAction2 key1="value1"/></actions></kit>
    
    // WHEN
    val uut = KitElem(settings)
    var action = ActionElem(<myAction2 key1="value1" />)
    uut.actions.get.addAction(action)
    
    // THEN
    uut.toXML should equal(expected)
  }
  
  "ActionElem" should "allow for text in element" in {
    // GIVEN
    val elementText = "Element text goes here"
    val settings = <kit><actions><myAction1 key1="value1">{elementText}</myAction1></actions></kit>
    
    // WHEN
    val uut = KitElem(settings)
    
    // THEN
    uut.actions.get.actionList(0).getText.get should equal(elementText)
  }
  
  "ActionElem" should "allow for CDATA text in element" in {
    // GIVEN
    val elementText = Unparsed("<![CDATA[%s]]>".format("This </myAction1> has <html/> tags"))
    val settings = <kit><actions><myAction1 key1="value1">{elementText}</myAction1></actions></kit>
    
    // WHEN
    val uut = KitElem(settings)
    
    // THEN
    uut.actions.get.actionList(0).getText.get should equal(elementText.toString)
    //uut.toXML should equal(settings)
    println("text = " +  uut.actions.get.actionList(0).getText.get)
  }

}