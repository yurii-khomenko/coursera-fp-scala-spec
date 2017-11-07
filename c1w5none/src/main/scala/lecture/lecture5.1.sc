def last[T](xs: List[T]): T = xs match {
  case Nil => throw new Error("last of empty list")
  case List(x) => x
  case y :: ys => last(ys)
}

def init[T](xs: List[T]): List[T] = xs match {
  case Nil => throw new Error("init of empty list")
  case List(x) => Nil
  case y :: ys => y :: init(ys)
}

def concat[T](xs: List[T], ys: List[T]): List[T] = xs match {
  case Nil => ys
  case z :: zs => z :: concat(zs, ys)
}

def reverse[T](xs: List[T]): List[T] = xs match {
  case Nil => xs
  case y :: ys => reverse(ys) ::: y :: Nil
}

def removeAt[T](xs: List[T], n: Int) = (xs take n) ::: (xs drop n + 1)

def flatten(xs: List[Any]): List[Any] = xs match {
  case Nil => xs
  case (head: List[_]) :: tail => flatten(head) ::: flatten(tail)
  case head :: tail => head :: flatten(tail)
}

val list = 1 :: 2 :: 3 :: Nil

last(list)
init(list)
concat(list, list)
reverse(list)
removeAt(list, 1)

flatten(List(List(1, 1), 2, List(3, List(5, 8))))