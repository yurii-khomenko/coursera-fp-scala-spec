package lecture

import lecture.sim._

object lecture3_6 extends App {

  object sim extends Circuits with Parameters

  val in1, in2, sum, carry = new Wire
  sim.halfAdder(in1, in2, sum, carry)

  sim.probe("sum", sum)
  sim.probe("carry", carry)

  in1 setSignal true
  sim.run()

  in2 setSignal true
  sim.run()

  in1 setSignal false
  sim.run()
}
