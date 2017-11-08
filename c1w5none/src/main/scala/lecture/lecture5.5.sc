val nums = 2 :: -4 :: 5 :: 7 :: 1 :: Nil

def sumReduceLeft(xs: List[Int]) = (0 :: xs) reduceLeft (_ + _)
sumReduceLeft(nums)

def sumFoldLeft(xs: List[Int]) = (xs foldLeft 0) (_ + _)
sumFoldLeft(nums)

def sumReduceRight(xs: List[Int]) = (0 :: xs) reduceRight (_ + _)
sumReduceRight(nums)

def sumFoldRight(xs: List[Int]) = (xs foldRight 0) (_ + _)
sumFoldRight(nums)

def mapFun[T, U](xs: List[T], f: T => U): List[U] =
  (xs foldRight List[U]()) ((x, list) => f(x) :: list)

mapFun[Int, Int](nums, x => x * x)

def lengthFun[T](xs: List[T]): Int =
  (xs foldRight 0) ((_, n) => n + 1)

lengthFun(nums)