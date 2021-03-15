
  def abs(x: Double) = if (x < 0) -x else x

  def sqrt(x: Double) = {

    def isGoodEnough(guess: Double, x: Double): Boolean = {
      val diff = abs(x - guess * guess) / x
      println(s"diff: $diff")
      if (diff < 0.00001) true else false
    }

    def improve(guess: Double, x: Double) = (guess + x / guess) / 2


    def sqrtIter(guess: Double, x: Double): Double =
      if (isGoodEnough(guess, x)) guess
      else sqrtIter(improve(guess, x), x)


    sqrtIter(1.0, x)
  }

  sqrt(9)
