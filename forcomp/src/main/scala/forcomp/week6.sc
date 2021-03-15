import forcomp.Anagrams.Occurrences
import forcomp.Anagrams.Word
import forcomp.Anagrams._
import forcomp._
import forcomp.Dictionary.loadDictionary

val oc = wordOccurrences("heello")

def combinations(occurrences: Occurrences): List[Occurrences] = occurrences match {
  case Nil => List(Nil)
  case ys :: xs => if (ys._2 == 1)   combinations(xs).map(zs =>  (ys :: zs))   :: combinations(xs)
  else occurrences :: combinations( (ys._1, ys._2 -1) :: xs )
}


combinations(oc)

val c:Char = 'x'


oc.map(xs => ('x'.toChar,1) :: oc)

/*
oc

res0: List[forcomp.Anagrams.Occurrences] = List(
List((e,2), (h,1), (l,2), (o,1)),
List((e,1), (h,1), (l,2), (o,1)),
List((h,1), (l,2), (o,1)),
List((l,2), (o,1)),
List((l,1), (o,1)),
List((o,1)),
List()
)




val s1 = oc match {
  case ys :: xs => {
    //println(ys);println(xs)
    if (ys._2 == 1) oc :: List(xs) else oc :: List ((ys._1, ys._2-1)::xs)
  }
}
*/
