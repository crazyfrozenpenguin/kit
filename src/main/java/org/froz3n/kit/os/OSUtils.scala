package org.froz3n.kit.os

object OSUtils {

  private val OS = System.getProperty("os.name").toLowerCase();
  private val Win = "(.*win.*)".r
  private val Mac = "(.*mac.*)".r
  private val Nix = "(.*nix.*)".r
  private val Nux = "(.*nux.*)".r
  
  def isWindows():Boolean = OS.matches(Win.toString)
  def isMac():Boolean = OS.matches(Mac.toString)
  def isUnix():Boolean = OS.matches(Nix.toString) || OS.matches(Nux.toString)
  
  def getShell():Array[String] = {
    OS match {
      case Win(w) => Array("cmd /K start")
      case _ => Array("/bin/bash","-c")
    }
  }
  
  def getBatchShell():Array[String] = {
    OS match {
      case Win(w) => Array("cmd /C start")
      case _ => Array("/bin/bash","-c")
    }
  }
  
  def getExecCmd():Array[String] = {
    OS match {
      case Win(w) => Array("cmd /C")
      case _ => Array("/bin/bash","-c")
    }
  }
  
}
