package org.froz3n.kit.action

import org.froz3n.kit.Command
import org.froz3n.kit.action.loader.Action
import org.froz3n.kit.action.loader.ActionLoaderObj

class HelpAction extends Action {

  def version():String = "1.0"

  def execute(command:Command):Unit = {
    if (command.what.isDefined) {
      var what:String = command.what.get
      var action = ActionLoaderObj.loadCommandAction(what);
      if (action.isDefined) {
        println(action.get.help);
      }
    } else {
      println(help)
    }
  }
    
  def help():String = {
    return "help [command name]\n" +
    "  Shows this help or the help for the given command name.\n"
  }
  
}