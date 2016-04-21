import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestKitBase, ImplicitSender}

import com.typesafe.config.ConfigFactory
import org.scalatest.{WordSpecLike, BeforeAndAfterAll}

import scala.concurrent.duration._

class TestApp extends WordSpecLike with TestKitBase with ImplicitSender with BeforeAndAfterAll {

  lazy val appCfg = ConfigFactory.load("akka-test.conf")
  override lazy val system = ActorSystem(getClass.getSimpleName, appCfg)

  "case 1:" must {
    "before reload" in {
      println("Creating actor before reload")
      val actor = system.actorOf(TheActor.props(1, doInit = false))

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
      val actor = system.actorOf(TheActor.props(1, doInit = false))

      // commenting this 'sleep' makes test passed
      Thread.sleep(2000)

      actor ! 2
      expectMsg(1 second, 0)

      system.stop(actor)
    }
  }

  "case 2" must {
    "before reload" in {
      println("Creating actor before reload")
      val actor = system.actorOf(TheActor.props(2, doInit = false))

      actor ! 2
      expectMsg(1 second, 2)

      actor ! 1
      expectMsg(1 second, 1)

      actor ! 2
      expectMsg(1 second, 2)

      actor ! 2
      expectMsg(1 second, 0)

      system.stop(actor)
    }

    "after reload" in {
      println("Creating actor after reload")
      // setting doInit to true let pass this test
      val actor = system.actorOf(TheActor.props(2, doInit = false))

      Thread.sleep(7000)

      actor ! 2
      expectNoMsg(1 second)

      system.stop(actor)
    }
  }
}
