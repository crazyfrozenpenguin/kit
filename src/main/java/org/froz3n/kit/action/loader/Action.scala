package org.froz3n.kit.action.loader

import org.froz3n.kit.Command

trait Action {

  def version():String;
  
  def execute(command:Command):Unit;

  def help():String;
  
}