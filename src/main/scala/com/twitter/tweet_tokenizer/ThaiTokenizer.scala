package com.twitter.tweet_tokenizer

import scala.collection.mutable

class ThaiTokenizer(dict: ThaiDict) {

  private[this] val MinusInfinity = -1000
  private[this] val FobiddenEnds = "ไ แ เ ัโใ".toCharArray.filter { _ != ' ' }.toSet
  private[this] val FobiddenStarts = "า ำ ์ ื ิ ้ ็  ัํ ี ๊ ึ ุ ู ่ ๋".toCharArray.filter { _ != ' ' }.toSet

  def break(s: String): Seq[String] = {
    val cs = s.toCharArray
    val n = cs.length

    val table = Array.ofDim[Int](n)
    val froms = Array.ofDim[Int](n)

    0.until(n).foreach { i =>
      table.update(i, score(cs, 0, i))
      froms.update(i, -1)

      1.until(i).foreach { j =>
        val curScore = score(cs, j, i) + table(j-1)
        if (curScore > table(i)) {
          table.update(i, curScore)
          froms.update(i, j-1)
        }
      }
    }

    var index = n - 1
    val buffer = mutable.ListBuffer[String]()
    while (index > -1) {
      val next = froms(index)
      buffer.prepend(cs.subSequence(next + 1, index + 1).toString)
      index = next
    }

    buffer.toSeq
  }

  def score(cs: Array[Char], start: Int, end: Int): Int = {
    if ((end - start + 1) <= 1
        || cs(start + 1) == '์'
        || FobiddenEnds.contains(cs(end))
        || FobiddenStarts.contains(cs(start))) {
      MinusInfinity
    } else {
      if (dict.hasWord(new String(cs, start , end - start + 1))) {
        end - start + 1
      } else {
        0
      }
    }
  }
}
