trait List[+T] {

  def empty: Boolean
  def head: T
  def tail: List[T]
  def prepend [U >: T] (x: U): List[U] = new Cons(x, this)

  override def toString: String = if (empty) "Nil" else "List(" + head + ", " + tail + ")"
}

object Nil extends List[Nothing] {
  def empty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def empty: Boolean = false
}

object List {
  def apply[T](): Nil.type = Nil
  def apply[T](x: T): List[T] = new Cons(x, Nil)
  def apply[T](x1: T, x2: T): List[T] = new Cons(x1, new Cons(x2, Nil))
}

List()
List(1)
List(1, 2)