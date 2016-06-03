package worker

import akka.actor.Actor
import java.util.concurrent.ThreadLocalRandom

class WorkProcessor extends Actor {
  def random = ThreadLocalRandom.current

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
      val change = changes(random.nextInt(0, changes.size))
      val byDegF = 1 + random.nextInt(0, 3)
      if (change == "NO CHANGE") "NO CHANGE" else
        change + byDegF + "F"
    }
    case l: Lamp => {
      val states = List(0, 1)
      val state = states(random.nextInt(0, states.size))
      val opposite = if (device.getState == "ON") "OFF" else "ON"
      if (state == 0) "NO CHANGE" else
        "Switch light to " + opposite
    }
    case s: SecurityAlarm => {
      val states = List(0, 1)
      val state = states(random.nextInt(0, states.size))
      val opposite = if (device.getState == "ON") "OFF" else "ON"
      if (state == 0) "NO CHANGE" else
        "Switch light to " + opposite
    }
    case _ => "NO CHANGE"
  }
}
