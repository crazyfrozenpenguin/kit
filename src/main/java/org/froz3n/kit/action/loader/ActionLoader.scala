package org.froz3n.kit.action.loader

import org.froz3n.kit.Command

trait ActionLoader {

  def loadCommandAction(command: Command): Option[Action];
  
  def loadCommandAction(actionName: String): Option[Action];

  def findAllCommandActions(): List[Action];
  
}