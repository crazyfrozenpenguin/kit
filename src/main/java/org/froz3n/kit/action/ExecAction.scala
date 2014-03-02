package org.froz3n.kit.action

import org.froz3n.kit.Command
import org.froz3n.kit.action.loader.Action
import org.froz3n.kit.os.OSUtils

class ExecAction extends Action {

  def version(): String = "1.0"

  def execute(command: Command): Unit = {
    command.what.isDefined match {
      case true => processCommand(command)
      case false => processAppCommand(command)
    }
  }

  private def processCommand(command: Command) {
    command.what.get match {
      case "shell" => processShellCommand(command)
      case "batch" => processBatchCommand(command)
      case "app" => processAppCommand(command)
      case _ => println(help)
    }
  }

  private def processShellCommand(command: Command) {
    try {
      val props = command.mapProperties
      val cmd: String = props.getOrElse("command", null)
      (cmd != null) match {
        case true => {
          val command = OSUtils.getShell.:+(cmd)
          execute(command, false)
        }
        case false => println(help)
      }
    } catch {
      case _: Throwable => println(help)
    }
  }

  private def processBatchCommand(command: Command) {
    try {
      val props = command.mapProperties
      val cmd: String = props.getOrElse("command", null)
      (cmd != null) match {
        case true => {
          val command = OSUtils.getBatchShell.:+(cmd)
          execute(Array(cmd))
        }
        case false => println(help)
      }
    } catch {
      case _: Throwable => println(help)
    }
  }

  def processAppCommand(command: Command) {

  }

  def help(): String = {
    return "exec (type) -command [command]\n" +
      "  Execute provided command\n" +
      "  Available types:\n" +
      "    shell - runs command on new shell and leaves it open. Great to set working environments.\n" +
      "    batch - runs command on new shell and closes it once finished.\n" +
      "    app   - runs application (used for graphical applications).\n" +
      "    Defaults to 'app' if no type is defined.\n"
  }

  def execute(command: Array[String], wait:Boolean = true) = {
    var process: Process = null
    if (OSUtils.isWindows) {
      process = Runtime.getRuntime().exec(new String(command.mkString(" ")))
    } else {
      process = Runtime.getRuntime().exec(command)
    }
    if (wait) process.waitFor()
  }

}