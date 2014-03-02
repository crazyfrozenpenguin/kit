package org.froz3n.kit.action

import org.froz3n.kit.Command
import org.froz3n.kit.action.loader.Action

class InstallAction extends Action {

  def version():String = "1.0"
  
  def execute(command:Command):Unit = {
    println("Executing Install Action")
  }
  
  def help():String = {
    return "install [what] [options]\n" +
    "  Installs files or applications.\n" +
    "  what - The name of what is to be installed \n" +
    "  options:\n" +
    "  -v | -version [version to install]\n" +
    "  -c | -conf [config file]\n" +
    "  -d | -dir  [path do install directory]\n"
  }

}