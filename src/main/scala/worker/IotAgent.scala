package worker

import scala.concurrent.duration._
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.Props
import akka.pattern._
import akka.util.Timeout
import akka.cluster.client.ClusterClient.SendToAll

import com.sandinh.paho.akka._
import com.sandinh.paho.akka.MqttPubSub._

object IotAgent {
  def props(clusterClient: ActorRef, mqttPubSub: ActorRef, deviceRequest: ActorRef): Props = Props(
    classOf[IotAgent], clusterClient, mqttPubSub, deviceRequest)

  case object Ok
  case object NotOk
}

class IotAgent(clusterClient: ActorRef, mqttPubSub: ActorRef, deviceRequest: ActorRef) extends Actor with ActorLogging {
  import IotAgent._
  import context.dispatcher

  mqttPubSub ! Subscribe(MqttConfig.topic, self)

  def receive = {
    case SubscribeAck(Subscribe(MqttConfig.topic, `self`, _)) => {
      log.info("IoT Agent -> MQTT subscription to {} acknowledged", MqttConfig.topic)
      context.become(ready)
    }
    case _ =>
      log.info("IoT Agent -> ALERT: Problem receiving message!")
  }

  def ready: Receive = {
    case msg: Message => {
      val work = MqttConfig.readFromByteArray[Work](msg.payload)
      log.info("IoT Agent -> Received MQTT message: Device Id {} | Device State {} | Work Id {}", work.device.getId, work.device.getState, work.workId)

      log.info("IoT Agent -> Sending work to cluster master")
      implicit val timeout = Timeout(5.seconds)
      (clusterClient ? SendToAll("/user/master/singleton", work)) map {
        case Master.Ack(_) => Ok
      } recover { case _ => NotOk } pipeTo deviceRequest
    }
    case _ =>
      log.info("IoT Agent -> ALERT: Problem with received message!")
  }
}