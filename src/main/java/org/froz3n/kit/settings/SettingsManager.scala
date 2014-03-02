package org.froz3n.kit.settings

import scala.xml.Elem
import scala.xml.XML
import java.lang.System.getProperty
import java.io.File
import java.io.FileNotFoundException
import org.froz3n.kit.settings.xml.KitElem
import org.froz3n.kit.settings.xml.RepoElem
import org.froz3n.kit.settings.xml.RepositoriesElem
import org.froz3n.kit.settings.xml.ActionsElem
import org.froz3n.kit.settings.xml.ActionElem

object SettingsManager {

  var settings: KitElem = KitElem(<kit/>);

  {
    loadSettings()
  }

  def getRemoteRepositories(): Seq[RepoElem] = settings.repositories.isDefined match {
    case true => settings.repositories.get.repoList
    case _ => Seq[RepoElem]()
  }

  def addRemoteRepository(name: String, url: String): Boolean = {
    if (!settings.repositories.isDefined) {
      settings.repositories = Option(RepositoriesElem(<repositories/>))
    }
    val result = settings.repositories.get.addRepo(RepoElem(<repo name={ name } url={ url }/>))
    if (result) XML.save(getLocation, settings.toXML)
    result
  }

  def removeRemoteRepository(name: String): Boolean = {
    val repos = settings.repositories.get.repoList
    settings.repositories.get.repoList = repos.filter(r => r.name.isDefined match {
      case true => r.name.get != name
      case false => false
    })
    val result = (settings.repositories.get.repoList.size < repos.size)
    if (result) XML.save(getLocation, settings.toXML)
    result
  }

  def addAction(elem: Elem): Boolean = {
    if (!settings.actions.isDefined) {
      settings.actions = Option(ActionsElem(<actions/>))
    }
    val result = settings.actions.get.addAction(ActionElem(elem))
    if (result) XML.save(getLocation, settings.toXML)
    else println("Action already exists")
    result
  }

  def removeAction(name: String): Boolean = {
    val actions = settings.actions.get.actionList
    settings.actions.get.actionList = actions.filter(a => a.getTagName != name)
    val result = (settings.actions.get.actionList.size < actions.size)
    if (result) XML.save(getLocation, settings.toXML)
    result
  }

  def getActions(actionName: String): Seq[ActionElem] = {
    var result = Seq[ActionElem]()
    if (settings.actions.isDefined) {
      for (
        action <- settings.actions.get.actionList if action.getTagName == actionName
      ) {
        result = result :+ action
      }
    }
    result
  }

  private def loadSettings() = {
    try {
      settings = KitElem(XML.loadFile(getLocation))
    } catch {
      case fnf: FileNotFoundException => initSettings
      case t: Throwable => {
        println(t.getClass())
        println("An error as occurred while reading HOME/.kit/settings.xml : " + t.getMessage())
      }
    }
  }

  private def initSettings() = {
    settings = KitElem(<kit>
                         |<repositories/>
                         |<actions/>
                       </kit>)
    val dir = new File(getKitDir)
    if (!dir.exists) dir.mkdir()
    XML.save(getLocation, settings.toXML, xmlDecl = true)
  }

  private def getLocation() = {
    getKitDir + "/settings.xml"
  }

  private def getKitDir() = getProperty("user.home") + "/.kit"
}