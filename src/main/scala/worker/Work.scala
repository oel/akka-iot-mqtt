package worker

import java.io.Serializable

case class Work(workId: String, device: Device, job: Any) extends Serializable

case class WorkResult(workId: String, result: Any)