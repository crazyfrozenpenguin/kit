package org.froz3n.kit

object Main {
  def main(args: Array[String]) {
    var cmd = KITParser.prepareCommandArguments(args)
    try {
      KITParser.execute(cmd)
    } catch {
      case _: Throwable => {
        println("ERROR: Failed to parse the provided command.")
      }
    }
  }
}