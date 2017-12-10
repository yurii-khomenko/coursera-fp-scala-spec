package lecture.lecture4_1

trait Subscriber {
  def handler(pub: Publisher)
}