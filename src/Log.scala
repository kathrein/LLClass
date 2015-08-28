/**
 * Log.scala
 *
 * @author Wade Shen <BR>
 * <tt><a href=mailto:swade@ll.mit.edu>swade@ll.mit.edu</a></tt> <BR>
 * Copyright &copy; 2005 Massachusetts Institute of Technology, Lincoln Laboratory
 * @version 0.1a
 */

package mitll
import utilities._
import java.io._;
import java.util.Date;
import java.text.{DateFormat, SimpleDateFormat};

class Log(name : String, stderr : Boolean = false) extends PrintStream(if (name == null) System.err else new FileOutputStream(name, true), true, "UTF8") {
  /** Write message to log/err and flush line add newline as needed */
  override def print(msg : String) {
    if (stderr && name != null) System.err.print(msg);
    super.print(msg);
  }

  def prefix(time : Date, mType : String) = df.format(time) + " " + "%-10s".format("[" + mType.substring(0, math.min(mType.length, 8)) + "]");
  def add(time : Date, mType : String, msg : String, args : Any*) { print(prefix(time, mType) + " " + msg.format(args : _*) + (if (msg.endsWith("\n")) "" else "\n")); }
  def rawAdd(time : Date, mType : String, msg : String) { print(prefix(time, mType) + " " + msg + (if (msg.endsWith("\n")) "" else "\n")); }

  def add(mType : String, mesg : String, args: Any*) {
    val d = new Date();
    if (args.length > 0) add(d, mType, mesg, args : _*);
    else rawAdd(d, mType, mesg)
  }

  def add(mesg : String, args: Any*) {
    val d = new Date();
    if (args.length > 0) add(d, "INFO", mesg, args : _*);
    else rawAdd(d, "INFO", mesg)
  }

  // Add multiline message
  def madd(mType : String = "INFO", mesg : String) {
    val d = new Date();
    for (m <- mesg.split("\n")) rawAdd(d, mType, m)
  }

  def startProgress(limit : Int = 100, scale : Int = 1000, pType : String = "PROG") { 
    n = 0; this.limit = limit; this.scale = scale; this.pType = pType;
    print(prefix(new Date(), pType) + " ");
    time = System.nanoTime;
  }
  def progress { 
    n += 1; 
    if (n % scale == 0) print("."); 
    if (n % (scale * limit) == 0) { 
      val nTime = System.nanoTime;
      printf(" %15d completed. [took: %15.3f seconds]\n", int2Integer(n), float2Float((nTime - time).asInstanceOf[Float] / 1e9f)); print(prefix(new Date(), pType) + " "); 
      time = nTime;
    }
  }
  def separator(limit : Int = 70) = println(prefix(new Date(), "SEP") + " " + ("-" * limit));
  def endProgress(units : String = "") { 
    println("\n" + prefix(new Date(), pType) + " " + ("-" * limit)); 
    print(prefix(new Date(), pType)); printf(" TOTAL: %15d %s\n", int2Integer(n), units); 
    n = 0; limit = 0; 
  }
  def apply(time : Date, mType : String, mesg : String, args: Any*) { add(time, mType, mesg, args : _*); }
  def apply(mType : String, mesg : String, args: Any*) { add(mType, mesg, args : _*); }
  def apply(mesg : String, args: Any*) { add(mesg, args : _*); }
  val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  var n = 0;
  var limit = 0;
  var scale = 0;
  var pType = "";
  var time  = 0L;
}



object Log {

  var logInfo = "[info]"
  var logError = "[error]"
  var logDebug = "[debug]"

  def info (message : String) {
    var calendar = java.util.Calendar.getInstance();
    var currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
    println(logInfo + " " + currentTimestamp.toString + " " + message)
  }

  def error (message : String) {
    var calendar = java.util.Calendar.getInstance();
    var currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
    println(logError + " " + message)
    println(logError + " " + currentTimestamp.toString + " " + message)
  }

  def debug (message : String) {
    var calendar = java.util.Calendar.getInstance();
    var currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
    println(logDebug + " " + currentTimestamp.toString + " " + message)
  }
}
