package observatory

import observatory.Extraction._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers with Config {

  test("Interaction.tileLocation") {

    val tile = Tile(0, 0, 0)
    val actual = tile.toLocation

    val expected = Location(85.05112877980659, -180.0)

    assert(actual == expected)
  }

  test("Interaction.tileLocation2") {

    val tile = Tile(1, 0, 1)
    val actual = tile.toLocation

    val expected = Location(85.05112877980659, 0.0)

    assert(actual == expected)
  }

  test("Interaction.tileLocation3") {

    val tile = Tile(65544,43582,17)
    val actual = tile.toLocation

    val expected = Location(51.512161249555156, 0.02197265625)

    assert(actual == expected)
  }
}