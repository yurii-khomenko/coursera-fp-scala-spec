def isPrime(n: Int): Boolean = (2 until n) forall (d => n % d != 0)

val n = 7

(1 until n) flatMap (i =>
  (1 until i) map ((i, _))) filter (pair =>
    isPrime(pair._1 + pair._2))

case class Person(name: String, age: Int)
val persons = List(Person("Yurii", 28), Person("Igor", 27), Person("Kostya", 26))

for (p <- persons if p.age > 27) yield p.name

for {
  i <- 1 until n
  j <- 1 until i
  if isPrime(i + j)
} yield (i, j)

def scalarProduct(xs: List[Double], ys: List[Double]): Double =
  (for ((x, y) <- xs zip ys) yield x * y).sum
