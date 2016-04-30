package worker

import java.io.Serializable
import scala.util.Random

sealed trait Device extends Serializable {
  def getId: String
  def getState: String
}

// Thermostat with random id & state
class Thermostat extends Device {
  val states = List("HEAT", "COOL")
  val id = "thermostat-" + (1000 + Random.nextInt(100)).toString
  val state = states(Random.nextInt(states.size))

  def getId: String = id
  def getState: String = state
}

// Lamp with random id & state
class Lamp extends Device {
  val states = List("ON", "OFF")
  val id = "lamp-" + (5000 + Random.nextInt(500)).toString
  val state = states(Random.nextInt(states.size))

  def getId: String = id
  def getState: String = state
}

// Security alarm with random id & state
class SecurityAlarm extends Device {
  val states = List("ON", "OFF")
  val id = "security-alarm-" + (9000 + Random.nextInt(100)).toString
  val state = states(Random.nextInt(states.size))

  def getId: String = id
  def getState: String = state
}
