import scala.annotation.tailrec

def abs(x: Double) = if (x < 0) -x else x

def sqrtIter(guess: Double, x: Double): Double =
  if (isGoodEnough(guess, x)) guess
  else sqrtIter(improve(guess, x), x)

def isGoodEnough(guess: Double, x: Double) = abs(guess * guess - x) / x < 1e-6

def improve(guess: Double, x: Double) = (guess + x / guess) / 2

def sqrt(x: Double) = sqrtIter(1.0, x)

sqrt(0.001)
sqrt(0.1e-20)
sqrt(1.0e20)
sqrt(1.0e50)



def sqrtImprove(x: Double) = {

  def abs(x: Double) = if (x < 0) -x else x

  def sqrtIter(guess: Double): Double =
    if (isGoodEnough(guess)) guess
    else sqrtIter(improve(guess))

  def isGoodEnough(guess: Double) = abs(guess * guess - x) / x < 1e-6

  def improve(guess: Double) = (guess + x / guess) / 2

  sqrtIter(1.0)
}

sqrt(0.001)
sqrt(0.1e-20)
sqrt(1.0e20)
sqrt(1.0e50)


def fact(x: Long) = {

  @tailrec
  def factStep(x: Long, acc: Long): Long =
    if (x <= 1) acc else factStep(x - 1, x * acc)

  factStep(x, 1)
}

fact(1)