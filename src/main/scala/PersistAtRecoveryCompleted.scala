import akka.actor.Props
import akka.persistence.{PersistentActor, RecoveryCompleted}

case object InitialStateEvent

object PersistAtRecoveryCompleted {
  def props() = Props(new PersistAtRecoveryCompleted())
}

sealed trait State
case object NotTouchedByEventHandler extends State
case object TouchedByEventHandler extends State

class PersistAtRecoveryCompleted extends PersistentActor {

  var state: State = NotTouchedByEventHandler

  override def receiveRecover: Receive = {

    case RecoveryCompleted =>
      println(s"got RecoveryCompleted, persisting an event...")

      persist(InitialStateEvent) { event =>
        state = TouchedByEventHandler
        println(s"event handler has been invoked...")
      }
  }

  override def receiveCommand: Receive = {
    case a =>
      println(s"$a command has been received, state is $state")
      sender ! state
  }

  override def persistenceId: String = "test"
}
