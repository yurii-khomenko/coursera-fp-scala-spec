def squareListRec(xs: List[Int]): List[Int] =
  xs match {
    case Nil => xs
    case y :: ys => y * y :: squareListRec(ys)
  }

def squareListFun(xs: List[Int]): List[Int] =
  xs map (x => x * x)

val nums = 2 :: -4 :: 5 :: 7 :: 1 :: Nil

squareListRec(nums)
squareListFun(nums)

nums filter (_ > 0)
nums filterNot (_ > 0)
nums partition (_ > 0)

nums takeWhile (_ > 0)
nums dropWhile (_ > 0)
nums span (_ > 0)


def pack[T](xs: List[T]): List[List[T]] = xs match {
  case Nil => Nil
  case x :: xs1 =>
    val (first, rest) = xs span (_ == x)
    first :: pack(rest)
}

pack(List("a", "a", "a", "b", "c", "c", "a"))

def encode[T](xs: List[T]): List[(T, Int)] =
  pack(xs) map (ys => (ys.head, ys.size))

encode(List("a", "a", "a", "b", "c", "c", "a"))