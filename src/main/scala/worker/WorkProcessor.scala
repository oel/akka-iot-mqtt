package worker

import akka.actor.Actor

class WorkProcessor extends Actor {
  import scala.util.Random

  def receive = {
    case device: Device => {
      val result = adjust(device)
      sender() ! Worker.WorkComplete(result)
    }
    case _ =>
  }

  def adjust(device: Device): String = device match {
    case t: Thermostat => {
      val changes = List("RAISE temperature by ", "LOWER temperature by ", "NO CHANGE")
      val change = changes(Random.nextInt(changes.size))
      val byDegF = 1 + Random.nextInt(3)
      if (change == "NO CHANGE") "NO CHANGE" else
        change + byDegF + "F"
    }
    case l: Lamp => {
      val states = List(0, 1)
      val state = states(Random.nextInt(states.size))
      val opposite = if (device.getState == "ON") "OFF" else "ON"
      if (state == 0) "NO CHANGE" else
        "Switch light to " + opposite
    }
    case s: SecurityAlarm => {
      val states = List(0, 1)
      val state = states(Random.nextInt(states.size))
      val opposite = if (device.getState == "ON") "OFF" else "ON"
      if (state == 0) "NO CHANGE" else
        "Switch light to " + opposite
    }
    case _ => "NO CHANGE"
  }
}
