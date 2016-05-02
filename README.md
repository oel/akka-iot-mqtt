## IoT & Akka Actor Systems

---

This is a Scala-based application to primarily illustrate how to build a scalable distributed worker system using Akka actors to service requests from a MQTT-based IoT (Internet of Things) system.  A good portion of the Akka clustering setup is re-purposed from Lightbend's Akka distributed workers template (http://www.lightbend.com/activator/template/akka-distributed-workers).

##### To run the application within a single JVM on a web browser, simply proceed as follows:

1. Git-clone the repo to a local disk
2. Run './activator ui' from within the project root (Activator's UI will be fired off in a web browser)
3. Click 'compile', then 'run' from within the Activator's UI
4. Observe console output displayed on the web browser

##### To run the application on separate JVMs, please proceed as follows:

First, git-clone the repo to a local disk.  Next, open up separate command line terminals and launch the different components on separate JVMs:

> Launch the master cluster seed node with persistence journal:
>> $ {project-root}/bin/activator "runMain worker.Main 2551"
>
> Launch additional master cluster seed node:
>> $ {project-root}/bin/activator "runMain worker.Main 2552"
>
> Launch the IoTAgent-DeviceRequest node:
>> $ {project-root}/bin/activator "runMain worker.Main 3001"
>
> Launch the Worker node:
>> $ {project-root}/bin/activator "runMain worker.Main 0"

Additional IotAgent-DeviceRequest nodes (port# 3xxx) and/or Worker nodes (port# 0) can be launched/shut down, if wanted.

---
