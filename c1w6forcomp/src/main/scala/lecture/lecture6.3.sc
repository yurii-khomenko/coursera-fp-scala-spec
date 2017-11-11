val fruit = Set("apple", "banana", "pear")
val s = (1 to 6).toSet

s map (_ + 2)
fruit filter (_ startsWith "app")
s.nonEmpty

s map (_ / 2)

s contains 5

def queens(n: Int): Set[List[Int]] = {

  def placeQueens(k: Int): Set[List[Int]] =
    if (k == 0) Set(Nil)
    else
      for {
        queens <- placeQueens(k - 1)
        col <- 0 until n
        if isSafe(col, queens)
      } yield col :: queens

  def isSafe(col: Int, queens: List[Int]): Boolean = {

    val row = queens.length
    val queensWithRow = (row - 1 to 0 by -1) zip queens

    queensWithRow forall {
      case (r, c) => col != c && math.abs(col - c) != row - r
    }
  }

  placeQueens(n)
}

def show(queens: List[Int]) = {
  val lines =
    for (col <- queens.reverse)
    yield Vector.fill(queens.length)("* ").updated(col, "X ").mkString
  "\n" + (lines mkString "\n")
}

(queens(8) take 3 map show) mkString "\n"







