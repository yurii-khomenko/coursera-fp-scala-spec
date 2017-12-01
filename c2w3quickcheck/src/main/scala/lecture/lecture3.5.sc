trait Simulation {
  def currentTime: Int = ???
  def aferDelay(delay: Int)(block: => Unit): Unit = ???
  def run(): Unit = ???
}

type Action = () => Unit

class Wire {

  private var sigVal = false
  private var actions: List[Action] = Nil

  def getSignal: Boolean = sigVal

  def setSignal(s: Boolean): Unit =
    if (s != sigVal) {
      sigVal = s
      actions foreach(_())
    }

  def addAction(a: Action): Unit = {
    actions = a :: actions
    a()
  }
}

def inverter(input: Wire, output: Wire): Unit = {
  def invertAction(): Unit = {
    val inputSig = input.getSignal
    afterDelay(InverterDelay) { output setSignal !inputSig }
  }
  input addAction invertAction
}

def andGate(in1: Wire, in2: Wire, output: Wire): Unit = {
  def andAction(): Unit = {
    val in1Sig = in1.getSignal
    val in2Sig = in2.getSignal
    afterDelay(AndGateDelay) { output setSignal (in1Sig & in2Sig) }
  }

  in1 addAction andAction
  in2 addAction andAction
}

def orGate(in1: Wire, in2: Wire, output: Wire): Unit = {
  def andAction(): Unit = {
    val in1Sig = in1.getSignal
    val in2Sig = in2.getSignal
    afterDelay(AndGateDelay) { output setSignal (in1Sig | in2Sig) }
  }

  in1 addAction andAction
  in2 addAction andAction
}

def halfAdder(a: Wire, b: Wire, s: Wire, c: Wire): Unit = {
  val d, e = new Wire
  orGate(a, b, d)
  andGate(a, b, c)
  inverter(c, e)
  andGate(d, e, s)
}

def fullAdder(a: Wire, b: Wire, cin: Wire, sum: Wire, cout: Wire): Unit = {
  val s, c1, c2 = new Wire
  halfAdder(b, cin, s, c1)
  halfAdder(a, s, sum, c2)
  orGate(c1, c2, cout)
}