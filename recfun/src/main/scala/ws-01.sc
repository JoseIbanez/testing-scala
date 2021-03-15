

def f1(acc: Double, x: Double): Double = acc + x*x

def f2 = f1(_,_)


val l1 = List[Double]( 1, 2, 1)


l1.fold[Double](0){(a,b) => f1(a,b)}
l1.fold[Double](1){f2(_,_)}








