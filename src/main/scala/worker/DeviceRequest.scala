package worker

import java.util.UUID
import java.util.concurrent.ThreadLocalRandom
import scala.concurrent.duration._
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef

import com.sandinh.paho.akka._
import com.sandinh.paho.akka.MqttPubSub._

object DeviceRequest {
  case object Tick
}

class DeviceRequest(mqttPubSub: ActorRef) extends Actor with ActorLogging {
  import DeviceRequest._
  import context.dispatcher

  def scheduler = context.system.scheduler
  def random = ThreadLocalRandom.current
  def nextWorkId(): String = UUID.randomUUID().toString

  override def preStart(): Unit = scheduler.scheduleOnce(5.seconds, self, Tick)

  // override postRestart so we don't call preStart and schedule a new Tick
  override def postRestart(reason: Throwable): Unit = ()

  def receive = {
    case Tick => {
      // Random pick from a list of serializable devices
      val deviceList: List[Device] = List(new Thermostat, new Lamp, new SecurityAlarm)
      val device = deviceList(random.nextInt(0, deviceList.size))
      val work = Work(nextWorkId(), device, "Adjust device")
      log.info("Device Request -> Device Id {} | Device State {}", device.getId, device.getState)

      val payload = MqttConfig.writeToByteArray(work)

      log.info("Device Request -> Publishing MQTT Topic {}: Device Id {} | Device State {}", MqttConfig.topic, work.device.getId, work.device.getState)

      mqttPubSub ! new Publish(MqttConfig.topic, payload)

      context.become(waitAccepted(work, payload), discardOld = false)
    }
  }

  def waitAccepted(work: Work, payload: Array[Byte]): Actor.Receive = {
    case IotAgent.Ok =>
      context.unbecome()
      scheduler.scheduleOnce(random.nextInt(3, 10).seconds, self, Tick)
    case IotAgent.NotOk =>
      log.info("Device Request -> ALERT: Work Id {} from Device Id {} NOT ACCEPTED, retrying ... ", work.workId, work.device.getId)
      scheduler.scheduleOnce(3.seconds, mqttPubSub, new Publish(MqttConfig.topic, payload))
  }
}
