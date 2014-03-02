package org.froz3n.kit.settings.xml

import scala.xml.Elem
import scala.xml.Node
import scala.xml.XML

/**
 * XML element <[action]/>
 *
 */
case class ActionElem(node: Node) {
  def getTagName(): String = node.label

  def getAttribute(key: String): Option[String] = Option((node \ ("@" + key)).text)

  def getAttributes():Map[String,String] = node.attributes.asAttrMap
  
  def getText(): Option[String] = Option(node.text)

  def toXML(): Elem = {
    var attrs: String = ""
    node.attributes.foreach(a => attrs += " " + a.key + "=\"" + a.value(0) + "\"")
    var xml = "<" + node.label + attrs + (node.text match {
      case null | "" => "/>"
      case _ => ">" + node.text + "</" + node.label + ">"
    })
    return XML.loadString(xml)
  }
}

/**
 * XML element <repo/>
 *
 */
case class RepoElem(node: Node = null) {
  var name: Option[String] = None
  var url: Option[String] = None

  if (node != null) (node \ "@name").foreach(n => name = Option(n.text))
  if (node != null) (node \ "@url").foreach(u => url = Option(u.text))

  def toXML(): Elem = url.isDefined && name.isDefined match {
    case true => <repo name={ name.get } url={ url.get }/>
    case _ => <repo/>
  }
}

/**
 * XML element <repositories/>
 *
 */
case class RepositoriesElem(node: Node) {
  var repoList: Seq[RepoElem] = Seq[RepoElem]()
  (node \ "repo").foreach(r => repoList = repoList.:+(RepoElem(r)))

  def addRepo(repo: RepoElem) = repoList.contains(repo) match {
    case false => {
      repoList = repoList.:+(repo)
      true
    }
    case true => false
  }

  def toXML(): Elem = repoList.isEmpty match {
    case true => <repositories></repositories>
    case _ => {
      var doc = <repositories></repositories>
      repoList.foreach(r => doc = XmlHelper.addChild(doc, r.toXML))
      return doc
    }
  }
}

/**
 * XML element <actions/>
 *
 */
case class ActionsElem(node: Node = null) {
  var actionList: Seq[ActionElem] = Seq[ActionElem]()
  if (node != null) node.child.foreach(r => actionList = actionList.:+(ActionElem(r)))

  def addAction(action: ActionElem) = actionList.contains(action) match {
    case false => {
      actionList = actionList.:+(action)
      true
    }
    case true => false
  }

  def toXML(): Elem = actionList.isEmpty match {
    case true => <actions></actions>
    case _ => {
      var doc = <actions></actions>
      actionList.foreach(a => doc = XmlHelper.addChild(doc, a.toXML))
      return doc
    }
  }
}

/**
 * XML root element <kit/>
 *
 */
case class KitElem(elem: Elem = null) {
  var repositories: Option[RepositoriesElem] = None
  (elem \ "repositories").foreach(r => repositories = Option(RepositoriesElem(r)));

  var actions: Option[ActionsElem] = None
  (elem \ "actions").foreach(a => actions = Option(ActionsElem(a)));

  def toXML(): Elem = <kit>{
    repositories.isDefined match {
      case true => repositories.get.toXML
      case _ => ""
    }
  }{
    actions.isDefined match {
      case true => actions.get.toXML
      case _ => ""
    }
  }</kit>
}

/**
 * XmlHelper
 *
 */
object XmlHelper {
  def addChild(n: Node, newChild: Node) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) =>
      Elem(prefix, label, attribs, scope, child ++ newChild: _*)
    case _ => error("Can only add children to elements!")
  }
}