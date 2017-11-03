trait List[T] {
  def empty: Boolean
  def head: T
  def tail: List[T]
  override def toString: String = if (empty) "Nil" else "List(" + head + ", " + tail + ")"
}

class Nil[T] extends List[T] {
  def empty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def empty: Boolean = false
}

object List {
  def apply[T](): Nil[T] = new Nil[T]
  def apply[T](x: T): List[T] = new Cons[T](x, new Nil[T])
  def apply[T](x1: T, x2: T): List[T] = new Cons[T](x1, new Cons[T](x2, new Nil[T]))
}

List()
List(1)
List(1, 2)