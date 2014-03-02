package org.froz3n.kit.action.loader

import java.util.ServiceLoader

import scala.collection.JavaConversions.asScalaIterator
import scala.xml.Elem

import org.froz3n.kit.Command
import org.froz3n.kit.settings.RemoteRepoSettings
import org.froz3n.kit.settings.SettingsManager

object ActionLoaderObj extends ActionLoader {

  var sysLoader: ClassLoader = null;
  var settings: Option[Elem] = None;

  {
    buildClasspath()
  }

  def loadCommandAction(command: Command): Option[Action] = {
    return loadCommandAction(command.action)
  }

  def loadCommandAction(actionName: String): Option[Action] = {
    var action: Action = null
    try {
      var clazz = findAction(actionName)
      action = clazz.newInstance.asInstanceOf[Action]
    } catch {
      case e: ClassNotFoundException =>
        println("Unknown command: " + actionName)
      case e: Exception =>
        println(e)
    }
    return Option(action)
  }

  def findAllCommandActions(): List[Action] = {
    var list: List[Action] = List[Action]()
    var actions = ServiceLoader.load(classOf[Action], sysLoader)
    actions.iterator().foreach(action => {
      list = list :+ action
    })
    return list;
  }

  private def findAction(actionName: String): Class[_] = {
    var clazz: Class[_] = null;
    var actions = findAllCommandActions
    val name = actionName.head.toUpper + actionName.tail + "Action"

    actions.foreach(action => {
      val clazz = action.getClass
      if (clazz.getSimpleName.equals(name)) {
        return clazz
      }
    })

    throw new ClassNotFoundException
  }

  private def buildClasspath() = {
      for (repo <- SettingsManager.getRemoteRepositories) {
   		RemoteRepoSettings.loadRepoActions(repo)
        sysLoader = RemoteRepoSettings.getSysLoader
      }
  }
}