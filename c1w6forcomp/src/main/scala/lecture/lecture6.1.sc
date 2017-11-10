val xs = Array(1, 2, 3, 44)
xs map (_ * 2)

val s = "Hello World"
s filter (_.isUpper)

s exists (_.isUpper)
s forall (_.isUpper)

val pairs = List(1, 2, 3) zip s
pairs.unzip

s flatMap (c => List('.', c))

xs.sum
xs.product
xs.max
xs.min

val M = 5
val N = 3

(1 to M) flatMap (x => (1 to N) map ((x, _)))

def scalarProduct(xs: Vector[Double], ys: Vector[Double]): Double =
  (xs zip ys).map(xy => xy._1 * xy._2).sum

def isPrime(n: Int): Boolean = (2 until n) forall(d => n % d != 0)
isPrime(5)
isPrime(11)
isPrime(12)