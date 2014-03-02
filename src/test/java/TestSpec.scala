import scala.io.Source
import org.froz3n.scala.test.base.UnitSpec
import org.froz3n.kit.KITParser

class KITParserSpec extends UnitSpec {

  "Simple Test" should "simply run" in {
//    var args:Array[String] = Array("alias", "add-foo", "-name", "install-mysql", "-command", "install mysql -version 5.0 -dir \"file:///c:/my/path\"")
//    var args:Array[String] = Array("alias", "install-mysql")
//    var args:Array[String] = Array("alias")
//    var args:Array[String] = Array("alias", "remove", "-name", "install-mysql")
//    var args:Array[String] = Array("exec", "shell", "-command", "gnome-terminal")
//    var args:Array[String] = Array("exec", "-command", "gnome-terminal")
    var args:Array[String] = Array("alias", "gterm")

    //    println("cmd = " + args.mkString(" "))
//    for (i <- 0 until args.length) {
//      if (args(i).startsWith("-") && i+1 <= args.length) {
//        args(i+1) = new sun.misc.BASE64Encoder().encode(args(i+1).getBytes)
//      } 
//    }
//    println("cmd = " + args.mkString(" "))
//    for (i <- 0 until args.length) {
//      if (args(i).startsWith("-") && i+1 <= args.length) {
//        args(i+1) = new String(new sun.misc.BASE64Decoder().decodeBuffer(args(i+1)))
//      } 
//    }
//    println("cmd = " + args.mkString(" "))

    val cmd = KITParser.prepareCommandArguments(args)
    println(cmd)
    val c = KITParser.execute(cmd)
    println(c)
  }

}