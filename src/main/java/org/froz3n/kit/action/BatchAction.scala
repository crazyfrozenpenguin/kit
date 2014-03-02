package org.froz3n.kit.action

import scala.io.Source

import org.froz3n.kit.Command
import org.froz3n.kit.Main
import org.froz3n.kit.action.loader.Action

class BatchAction extends Action {

  def version(): String = "1.0"

  def execute(command: Command): Unit = {
    var line: String = null
    try {
      val properties = command.mapProperties
      val filename = properties.getOrElse("conf", properties.getOrElse("c", null))
      if (filename == null) {
        println(help)
      } else {
        for (line <- Source.fromFile(filename).getLines()) {
          if (!line.startsWith("#")) {
            try {
              val pattern = "\".*\"|\\S+".r
              var cmdArray = Array[String]()
              for (item <- pattern.findAllIn(line)) {
                cmdArray = cmdArray.:+(item)
              }
              Main.main(cmdArray)
            } catch {
              case err: Throwable => {
                println(err.getMessage())
                println("Failed to execute action: " + line)
                println("Please make sure all remote kit repositories are available by executing: kit repo test")
              }
            }
          }
        }
      }
    } catch {
      case _: Throwable => {
        if (line != null) {
          println("Failed to execute command line:")
          println(line)
        } else {
          println(help)
        }
      }
    }
  }

  def help(): String = {
    return "batch [options]\n" +
      "  Execute loaded actions.\n" +
      "  options:\n" +
      "  -c | -conf [config file with 1 kit command per line]\n"
  }

}