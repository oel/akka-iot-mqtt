package worker

import java.io.Serializable
import java.util.concurrent.ThreadLocalRandom

sealed trait Device extends Serializable {
  def random = ThreadLocalRandom.current
  def getId: String
  def getState: String
}

// Thermostat with random id & state
class Thermostat extends Device {
  val states = List("HEAT", "COOL")
  val id = "thermostat-" + (1000 + super.random.nextInt(0, 100)).toString
  val state = states(super.random.nextInt(0, states.size))

  def getId: String = id
  def getState: String = state
}

// Lamp with random id & state
class Lamp extends Device {
  val states = List("ON", "OFF")
  val id = "lamp-" + (5000 + super.random.nextInt(0, 500)).toString
  val state = states(super.random.nextInt(0, states.size))

  def getId: String = id
  def getState: String = state
}

// Security alarm with random id & state
class SecurityAlarm extends Device {
  val states = List("ON", "OFF")
  val id = "security-alarm-" + (9000 + super.random.nextInt(0, 100)).toString
  val state = states(super.random.nextInt(0, states.size))

  def getId: String = id
  def getState: String = state
}
