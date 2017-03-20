import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKitBase}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import scala.concurrent.duration._

class TestApp extends WordSpecLike with TestKitBase with ImplicitSender with BeforeAndAfterAll {

  lazy val appCfg = ConfigFactory.load("akka-test.conf")
  override lazy val system = ActorSystem(getClass.getSimpleName, appCfg)

  "persist an event at recovery completion" must {
    "not be interrupted by a command" in {

      println("creating a PersistAtRecoveryCompleted actor")
      val actor = system.actorOf(PersistAtRecoveryCompleted.props())

      actor ! 1
      expectMsg(1 second, TouchedByEventHandler)

      Thread.sleep(1000)

      system.stop(actor)
    }
  }

}
