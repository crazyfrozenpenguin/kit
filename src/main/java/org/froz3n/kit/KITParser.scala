package org.froz3n.kit

import scala.util.parsing.combinator.JavaTokenParsers

import org.froz3n.kit.action.loader.Action
import org.froz3n.kit.action.loader.ActionLoaderObj

case class Command(action: String, what: Option[String] = None, properties: Option[List[Property]] = None) {
  def mapProperties():Map[String, String] = {
    properties.get map { p => (p.name, p.value) } toMap
  }
}
case class Property(name: String, value: String)

/**
 * The KITParser is used to parse commands in the following format:
 *
 *   action [what] [-prop1] ...
 *
 */
object KITParser extends JavaTokenParsers {
  
  def model: Parser[List[Command]] = commandList

  def commandList: Parser[List[Command]] = rep(command)

  def command: Parser[Command] = commandName ~ opt(commandValue) ~ opt(propertyList) ^^ {
    case name ~ value ~ props => new Command(name, value, props)
  }

  def commandName: Parser[String] = ident

  def commandValue: Parser[String] = """([^?!-]([\w\d\.\\-]*))""".r ^^ {
    case value => value.trim
  }
  
  def propertyList: Parser[List[Property]] = rep(property)

  def property: Parser[Property] = "-" ~ propertyName ~ opt(propertyValue) ^^ {
    case "-" ~ name ~ value => new Property(name, value match {
      case value if value.isDefined => new String(new sun.misc.BASE64Decoder().decodeBuffer(value.get))
      case value => null
    });
  }

  def propertyName: Parser[String] = ident

  def propertyValue: Parser[String] = """("(.*?)?")|([=\w\d\s\.\\]*)""".r ^^ {
    case value => value.trim
  }
  
  def parse(cmdString: String): Option[List[Command]] = {
    Option(parseAll(model, cmdString).get)
  }

  def processCommands(commands: Option[List[Command]]):List[Action] = {
    var executedActions:List[Action] = List[Action]()
    if (commands.isDefined) {
      commands.get.foreach(command => {
        var action = ActionLoaderObj.loadCommandAction(command)
        if (action.isDefined) {
          action.get.execute(command);
          executedActions = executedActions.+:(action.get)
        }
      })
    }
    executedActions;
  }

  def execute(cmdString: String) = {
    processCommands(parse(cmdString))
  }
  
  def prepareCommandArguments(args:Array[String]):String = {
    if (args.isEmpty) return "version"
    for (i <- 0 until args.length) {
      if (args(i).startsWith("-") && i+1 < args.length && !args(i+1).startsWith("-")) {
        args(i+1) = new sun.misc.BASE64Encoder().encode(args(i+1).getBytes)
      } 
    }
    return args.mkString(" ")
  }
}