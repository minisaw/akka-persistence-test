import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestKitBase, ImplicitSender}

import com.typesafe.config.ConfigFactory
import org.scalatest.{WordSpecLike, BeforeAndAfterAll}

import scala.concurrent.duration._

class TestApp extends WordSpecLike with TestKitBase with ImplicitSender with BeforeAndAfterAll {

  lazy val appCfg = ConfigFactory.load("akka-test.conf")
  override lazy val system = ActorSystem(getClass.getSimpleName, appCfg)

  "before reload" in {
    println("Creating actor before reload")
    val actor = system.actorOf(Props[TheActor])

    actor ! 2
    expectMsg(1 second, 2)

    actor ! 1
    expectMsg(1 second, 1)

    actor ! 2
    expectMsg(1 second, 2)

    system.stop(actor)
  }

  "after reload" in {
    println("Creating actor after reload")
    val actor = system.actorOf(Props[TheActor])

    // commenting this 'sleep' makes test passed
    Thread.sleep(2000)

    // println("sending ....")
    actor ! 2
    expectMsg(1 second, 0)

    system.stop(actor)
  }
}
