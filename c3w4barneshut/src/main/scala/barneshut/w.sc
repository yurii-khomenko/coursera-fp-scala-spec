object PigLatinizer {
  def apply(x: ⇒ String) = x.tail + x.head + "ay"
}

val result = PigLatinizer {
  val x = "pret"
  val z = "zel"
  x ++ z //concatenate the strings
}