import scala.math._

def norm(vector: Array[Double], p: Double): Double =
  vector map(x => pow(abs(x), p)) sum

val vector = Array(1.0, 2, 4, -5)

norm(vector, 1)