//private def computePixel(xc: Double, yc: Double, maxIter: Int): Int = {
//
//  var i = 0
//  var x, y = 0.0
//
//  while (x * x + y * y < 4 && i < maxIter) {
//    val xt = x * x - y * y + xc
//    val yt = 2 * x * y + yc
//    x = xt; y = yt
//    i += 1
//    color(i)
//  }
//}

val image = Array.ofDim(100)
val maxIter = 5

//def parRender(): Unit =
//  for (idx <- image.indices.par) {
//    val (xc, yc) = coordinatesFor(idx)
//    image(idx) = computePixel(xc, yc, maxIter)
//  }