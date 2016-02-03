import akka.persistence.fsm.PersistentFSM
import scala.reflect.classTag
import scala.reflect.ClassTag
import scala.concurrent.duration._

sealed trait FsmState extends PersistentFSM.FSMState
case object StateOne extends FsmState {
  override def identifier: String = "one"
}
case object StateTwo extends FsmState {
  override def identifier: String = "two"
}

sealed trait FsmData
case object DataOne extends FsmData
case object DataTwo extends FsmData

sealed trait FsmEvent
case object EvOne extends FsmEvent
case object EvTwo extends FsmEvent

class TheActor extends PersistentFSM[FsmState,FsmData,FsmEvent] {
  override def domainEventClassTag: ClassTag[FsmEvent] = classTag[FsmEvent]
  override def persistenceId: String = "test.actor"
  override def applyEvent(domainEvent: FsmEvent, currentData: FsmData): FsmData = {
    println(s"applying $domainEvent in $currentData")
    (domainEvent, currentData) match {
      case (EvOne, _) => DataOne
      case (EvTwo, _) => DataTwo
    }
  }

  startWith(StateOne, DataOne)

  when(StateOne, 1 second) {
    case Event(1, DataOne) => stay() replying 0
    case Event(2, DataOne) => goto(StateTwo) applying EvTwo replying 2
    case Event(StateTimeout, _) =>
      println("got timeout in 1")
      stop
  }

  when(StateTwo) {
    case Event(1, DataTwo) => goto(StateOne) applying EvOne replying 1
    case Event(2, DataTwo) => stay forMax(5 seconds) applying EvTwo replying 0
    case Event(StateTimeout, _) =>
      println("got timeout in 2")
      stop
  }

  initialize()
}

