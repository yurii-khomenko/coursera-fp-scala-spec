//
//val size = 300
//val src = Array.fill(size)(3)
//val dst = Array.fill(size)(0)
//
//val parallelism = 8
//
//val cellsPerTask = math.max(src.length / parallelism, 1)
//val startPoints = src.indices by cellsPerTask
//
//val tasks = startPoints map { start =>
//  println(s">>>$start, ${start + cellsPerTask - 1}")
//}

case class Rational(n: Int, d: Int) extends Ordered[Rational] {
  override def compare(that: Rational) = (this.n * that.d) - (that.n * this.d)
}

val r1 = Rational(1, 2)
val r2 = Rational(1, 4)

r1 < r2

val list = List(5, 4, 3, 2, 1)
val result = (list :\ 0) { (`next element`, `running total`) ⇒
  `next element` - `running total`
}

2.5 * (2 / 3)