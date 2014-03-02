package org.froz3n.kit.action

import org.froz3n.kit.Command
import org.froz3n.kit.action.loader.Action
import org.froz3n.kit.action.loader.ActionLoaderObj

class ListAction extends Action {

  def version(): String = "1.0"

  def execute(command: Command): Unit = {
    var actions = ActionLoaderObj.findAllCommandActions
    val isFull= command.what.isDefined && command.what.get == "full"
    if (!isFull) println("For more details on each action use: list full")
    print("The available actions are:")
    actions.foreach(action => {
      if (isFull) {
        print("\n" + action.getClass.newInstance.help)
      } else {
        print(" " + action.getClass.getSimpleName.replaceFirst("Action$", "").toLowerCase)
      }
    })
    println
  }

  def help(): String = {
    return "list (full)\n" +
      "  Lists all available command actions.\n" +
      "  full - Lists full details for available actions\n"
  }

}