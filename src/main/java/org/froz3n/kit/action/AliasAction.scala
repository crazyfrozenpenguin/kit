package org.froz3n.kit.action

import org.froz3n.kit.settings.SettingsManager
import scala.xml.XML
import scala.xml.Unparsed
import org.froz3n.kit.Command
import org.froz3n.kit.settings.xml.ActionElem
import org.froz3n.kit.Property
import org.froz3n.kit.action.loader.Action
import org.froz3n.kit.Main

class AliasAction extends Action {

  def version(): String = "1.0"

  def execute(command: Command): Unit = {
    command.what.isDefined match {
      case true => processCommand(command)
      case false => println(help)
    }
  }

  private def processCommand(command: Command) {
    command.what.get match {
      case "add" => processAdd(command)
      case "remove" => processRemove(command)
      case "list" => processList(command)
      case "run" => processRun(command)
      case name => processRun(Command("alias", Option("run"), Option(List(Property("name", name)))))
    }
  }

  def help(): String = {
    return "alias [alias name] | [command name] -name [alias name] (-command command)\n" +
      "  \"alias [alias name]\" is expanded to \"alias run -name [alias name]\"\n" +
      "  Available commands:\n" +
      "    add -name [alias name] -command [kit command to be aliased]\n" +
      "    remove -name [alias name]\n" +
      "    list | list -name [command name]\n" +
      "    run -name [alias name]\n"
  }

  private def processAdd(command: Command) {
    try {
      val props = command.mapProperties
      val name: String = props.getOrElse("name", null)
      val cmd: String = props.getOrElse("command", null)
      (name != null && cmd != null) match {
        case true => {
          val elem = XML.loadString("<" + name + ">" + Unparsed("<![CDATA[%s]]>".format(cmd)) + "</" + name + ">")
          if (SettingsManager.addAction(elem)) println("Action added")
          else println("Failed to add action")
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
      val name: Option[String] = props.get("name")
      if (name.isDefined) SettingsManager.removeAction(name.get) match {
        case true => println("Action removed")
        case false => println("Failed to remove action")
      }
    } catch {
      case _: Throwable => println(help)
    }
  }

  private def processList(command: Command) {
    try {
      val props = command.mapProperties
      val name: String = props.getOrElse("name", ".*")
      for (
        action <- SettingsManager.settings.actions.get.actionList if action.getTagName.matches("^" + name + "$")
      ) {
        printAction(action)
      }
    } catch {
      case _: Throwable => println(help)
    }
  }

  private def processRun(command: Command) {
    try {
      val props = command.mapProperties
      val name: Option[String] = props.get("name")
      if (name.isDefined) {
        val actions = SettingsManager.getActions(name.get)
        var action: ActionElem = null
        for (action <- actions) {
          if (action.getText.isDefined) {
            try {
              val pattern = "\".*\"|\\S+".r
              var cmdArray = Array[String]()
              for (item <- pattern.findAllIn(action.getText.get)) {
                  cmdArray = cmdArray.:+(item)
              }
              Main.main(cmdArray)
            } catch {
              case err: Throwable => {
                println(err.getMessage())
                println("Failed to execute action: " + action.getText.get)
                println("Please make sure all remote kit repositories are available by executing: kit repo test")
              }
            }
          }
        }
      } else println(help)
    } catch {
      case _: Throwable => println(help)
    }
  }

  private def printAction(action: ActionElem) {
    println("Action Name: " + action.getTagName)
    for ((key, value) <- action.getAttributes) {
      println("Attribute: " + key + "    Value: " + value)
    }
    println("Action Detail: " + action.getText.getOrElse("N/A"))
    println()
  }

}