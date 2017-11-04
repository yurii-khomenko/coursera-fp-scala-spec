sealed trait Expr {
  def eval: Int = this match {
    case Number(n) => n
    case Var(name) => throw new Error("Var.eval")
    case Sum(e1, e2) => e1.eval + e2.eval
    case Prod(e1, e2) => e1.eval * e2.eval
  }
}

case class Number(n: Int) extends Expr
case class Var(name: String) extends Expr
case class Sum(e1: Expr, e2: Expr) extends Expr

object Sum {
  def apply(x: Int, e: Expr): Sum = Sum(Number(x), e)
  def apply(e: Expr, x: Int): Sum = Sum(e, Number(x))
  def apply(x1: Int, x2: Int): Sum = Sum(Number(x1), Number(x2))
}

case class Prod(e1: Expr, e2: Expr) extends Expr

object Prod {
  def apply(x: Int, e: Expr): Prod = Prod(Number(x), e)
  def apply(e: Expr, x: Int): Prod = Prod(e, Number(x))
  def apply(x1: Int, x2: Int): Prod = Prod(Number(x1), Number(x2))
}

val exp1 = Number(1)
val exp2 = Sum(Sum(1, 2), 3)
val exp3 = Sum(Prod(2, Var("x")), Var("y"))
val exp4 = Prod(Sum(2, Var("x")), Var("y"))
val exp5 = Prod(Var("y"), Sum(2, Var("x")))
val exp6 = Prod(Sum(2, Var("x")), Sum(2, Var("y")))

def show(e: Expr): String = e match {

  case Number(x) => x.toString
  case Var(name) => name
  case Sum(l, r) => show(l) + " + " + show(r)

  case Prod(l@Sum(_, _), r@Sum(_, _)) => "(" + show(l) + ") * (" + show(r) + ")"
  case Prod(l@Sum(_, _), r) => "(" + show(l) + ") * " + show(r)
  case Prod(l, r@Sum(_, _)) => show(l) + " * (" + show(r) + ")"
  case Prod(l, r) => show(l) + " * " + show(r)
}

show(exp1)
show(exp2)
show(exp3)
show(exp4)
show(exp5)
show(exp6)