
val size = 300
val src = Array.fill(size)(3)
val dst = Array.fill(size)(0)

val parallelism = 8

val cellsPerTask = math.max(src.length / parallelism, 1)
val startPoints = src.indices by cellsPerTask

val tasks = startPoints map { start =>
  println(s">>>$start, ${start + cellsPerTask - 1}")
}