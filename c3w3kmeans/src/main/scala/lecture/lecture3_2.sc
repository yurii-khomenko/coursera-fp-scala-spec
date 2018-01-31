def sum(xs: Array[Int]): Int =
  xs.par.fold(0)(_ + _)

def max(xs: Array[Int]): Int =
  xs.par.foldLeft(Int.MinValue)(math.max)

def isVowel(c: Char): Boolean =
  Array('a', 'e', 'i', 'o', 'u').contains(c.toLower)

def vowelCount(xs: Array[Char]): Int =
  xs.par.aggregate(0)(
    (count, c) => if(isVowel(c)) count + 1 else count,
    _ + _
  )

val array = Array(1, 2, 5, 7, 10)

sum(array)
max(array)

val epfl = Array('E', 'P', 'F', 'L')
vowelCount(epfl)