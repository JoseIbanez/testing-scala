//package objsets

import objsets._

val google = List("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus")
val apple = List("ios", "iOS", "iphone", "iPhone", "ipad", "iPad")

val text = "Text: #Apple #iPhone5 camera is 8megapixels...same as iPhone 4S. But it's not the same camera http://t.co/DwtKQkSu #LIVEBLOG [30]"

apple.exists(text.contains(_))

TweetReader.allTweets.filter(t => {  apple.exists(t.text.contains(_))  } ) .foreach( f => println(f))