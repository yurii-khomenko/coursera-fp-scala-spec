def initializeArray(xs: Array[Int])(v: Int): Unit =
  for (i <- xs.indices.par) xs(i) = v

val array = Array.ofDim[Int](1000)


initializeArray(array)(10)