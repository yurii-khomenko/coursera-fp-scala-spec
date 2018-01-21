import java.util

List(1, 3, 8).scan(100)((s, x) => s + x)
List(1, 3, 8).scanRight(100)((s, x) => s + x)

def scanLeft[A](in: Array[A], a0: A, f: (A, A) => A, out: Array[A]): Unit = {

  out(0) = a0

  for (i <- in.indices)
    out(i + 1) = f(out(i), in(i))
}

val in = Array(1, 3, 8)
val out = Array(0, 0, 0, 0)
scanLeft(in, 100, (x: Int, y: Int) => x + y, out)

util.Arrays.toString(out)


def mapSeg[A, B](in: Array[A], left: Int, right: Int,
                 fi: (Int, A) => B, out: Array[A]): Unit = ???

def reduceSeg1[A](in: Array[A], left: Int, right: Int,
                  a0: A, f: (A, A) => A): A = ???

def scanLeftPar[A](in: Array[A], a0: A, f: (A, A) => A, out: Array[A]): Unit = {

  val fi = (i: Int, v: A) => reduceSeg1(in, 0, i, a0, f)
  mapSeg(in, 0, in.length, fi, out)

  val last = in.length - 1
  out(last + 1) = f(out(last), in(last))
}