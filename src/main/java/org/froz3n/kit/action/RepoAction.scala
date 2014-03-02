package org.froz3n.kit.action

import org.froz3n.kit.Command
import org.froz3n.kit.action.loader.Action
import org.froz3n.kit.settings.SettingsManager

class RepoAction extends Action {

  def version(): String = "1.0"

  def execute(command: Command): Unit = {
    command.what.isDefined match {
      case true => processCommand(command)
      case false => println(help)
    }
  }

  def help(): String = {
    return "repo [command name] [options]\n" +
      "  add -name \"repo name\" -url \"http://host:port/context\"\n" +
      "  remove -name \"repo name\"\n" +
      "  list\n" +
      "  test\n"
  }

  private def processCommand(command: Command) {
    command.what.get match {
      case "add" => processAdd(command)
      case "remove" => processRemove(command)
      case "list" => processList(command)
      case "test" => processTest(command)
      case _ => println(help())
    }
  }

  private def processAdd(command: Command) {
    try {
      val props = command.mapProperties
      val name: String = props.getOrElse("name", null)
      val url: String = props.getOrElse("url", null)
      (name != null && url != null) match {
        case true => {
          if (SettingsManager.addRemoteRepository(name, url)) println("Added Repo")
          else println("Failed to add repo")
        }
        case false => println(help)
      }
    } catch {
      case _: Throwable => println(help)
    }
  }

  private def processRemove(command: Command) {
    try {
      val props = command.mapProperties
      val name: String = props.getOrElse("name", null)
      name != null match {
        case true => {
          if (SettingsManager.removeRemoteRepository(name)) println("Repo removed")
          else println("Failed to remove repo")
        }
        case false => println(help)
      }
    } catch {
      case _: Throwable => println(help)
    }
  }

  private def processList(command: Command) {
    for (repo <- SettingsManager.getRemoteRepositories) {
      println("Repo: " + repo.name.getOrElse("") + " -- Url: " + repo.url.getOrElse(""))
    }
  }
  
  private def processTest(command:Command) {
    println("TBD - This command will verify if all repos are up and available for usage")
  }
  
}