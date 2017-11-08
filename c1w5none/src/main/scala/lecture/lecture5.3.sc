def msort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {

  val n = xs.length / 2

  if (n == 0) xs
  else {

    def merge(xs: List[T], ys: List[T]): List[T] =
      (xs, ys) match {
        case (_, Nil) => xs
        case (Nil, _) => ys
        case (x :: xs1, y :: ys1) =>
          if (ord.lt(x, y)) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }

    val (fst, snd) = xs splitAt n
    merge(msort(fst), msort(snd))
  }
}

val list = 3 :: 2 :: 5 :: Nil

msort(list)