package org.froz3n.kit.action

import org.froz3n.kit.Command
import org.froz3n.kit.action.loader.Action
import org.froz3n.kit.action.loader.ActionLoaderObj

class VersionAction extends Action {

  def version():String = "1.0"
  
  def execute(command:Command):Unit = {
    if (command.what.isDefined) {
      var what:String = command.what.get
      var action = ActionLoaderObj.loadCommandAction(what);
      if (action.isDefined) {
        println(what.head.toUpper + what.tail.toString + " v" + action.get.version);
      }
    } else {
      println("KIT - Ad hoc toolKIT system v" + version)
      println("Usage: kit [action] (what) (-prop1 one -prop2 two ...)")
      println("Basic actions: list, help, version\n")
    }
  }
    
  def help():String = {
    return "version [command name]\n" +
    "  Returns the application version or the version of a given command action.\n" +
    "  If a command name is provided then it will show the version for the selected command actions.\n"
  }
  
}