package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class Interaction2Test extends FunSuite with Checkers {

  test("Interaction2.yearSelection") {

    val slider = Var(1200)
    val layer = Signal(Interaction2.availableLayers(1))

    val res = Interaction2.yearSelection(layer, slider)
    assert(res() == 1990)

    slider() = 1999
    assert(res() == 1999)
  }
}