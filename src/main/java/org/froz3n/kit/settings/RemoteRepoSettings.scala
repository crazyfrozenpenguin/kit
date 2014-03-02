package org.froz3n.kit.settings

import scala.xml.Elem
import scala.xml.XML
import java.net.URL
import java.net.URLClassLoader
import scala.util.control.Exception
import org.froz3n.kit.settings.xml.RepoElem

object RemoteRepoSettings {

  private var sysLoader: Option[ClassLoader] = None;

  def getSysLoader(): ClassLoader = sysLoader.isDefined match {
    case true => sysLoader.get
    case _ => getClass().getClassLoader()
  }

  def loadRepoActions(repo: RepoElem) {
    val repoSettings: Option[Elem] = repo.url.isDefined match {
      case true => {
        try {
          Option(XML.load(new URL(repo.url.get + "/kit-repo.xml")))
        } catch {
          case _: Throwable => None
        }
      }
      case _ => None
    }
    if (repoSettings.isDefined) {
      for (action <- (repoSettings.get \\ "action")) {
        Exception.ignoring(classOf[Throwable]) {
          sysLoader.isDefined match {
            case true => sysLoader = Option(new URLClassLoader(Array(new URL(repo.url.get + "/" + (action \ "@url"))), sysLoader.get))
            case _ => sysLoader = Option(new URLClassLoader(Array(new URL(repo.url.get + "/" + (action \ "@url"))), getClass().getClassLoader()))
          }
        }
      }
    }
  }

}