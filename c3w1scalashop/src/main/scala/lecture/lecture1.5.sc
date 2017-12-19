import scala.math._

def sumSegment(a: Array[Int], p: Double, s: Int, t: Int): Int =
  (s until t)
    .map(i => (pow(abs(a(i)), p)).toInt)
    .sum

def pNorm(a: Array[Int], p: Double): Int =
  floor(pow(sumSegment(a, p, 0, a.length), 1 / p)).toInt

val vector = Array(2, 10)

//sumSegment(vector, vector.length, 0, 2)

pNorm(vector, vector.length)