package calculator

object Polynomial {

  def computeDelta(sigA: Signal[Double], sigB: Signal[Double],
                   sigC: Signal[Double]): Signal[Double] = Signal {

    val a = sigA()
    val b = sigB()
    val c = sigC()

    b * b - 4 * a * c
  }

  def computeSolutions(sigA: Signal[Double], sigB: Signal[Double],
                       sigC: Signal[Double], deltaSig: Signal[Double]): Signal[Set[Double]] = Signal {

    val delta = deltaSig()

    if (delta < 0) Set(0.0)
    else {
      val a = sigA()
      val b = sigB()
      val root1 = (-b + Math.sqrt(delta)) / (2 * a)
      val root2 = (-b - Math.sqrt(delta)) / (2 * a)
      Set(root1, root2)
    }
  }
}
