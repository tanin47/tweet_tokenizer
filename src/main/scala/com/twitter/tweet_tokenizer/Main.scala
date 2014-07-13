package com.twitter.tweet_tokenizer

import java.text.BreakIterator
import java.util.Locale

object Main {
  def main(args: Array[String]) {
    println("Hello")

    val boundary = BreakIterator.getWordInstance(Locale.forLanguageTag("th"))

//    val source = "สงสัย บิ๊กตู่ ดูผลดุสิตโพลล์ ก็คงไม่พอใจอีก เพราะตั้งเป้าให้ได้คะแนนเต็ม100....แต่โพลล์ นี้ สำรวจน้อยแค่ราว1,400คนเท่านั้น น่าถามสักหมื่นคน"
    val source = "เมื่อ 00.08 น.เกิดเหตุเพลิงไหม้โกดังเก็บดอกไม้พลาสติก ถ.แสงชูโต ต.ท่าผา อ.บ้านโป่ง จ.ราชบุรี เสียหาย 1 โกดัง ...ล่าสุดเพลิงสงบ ไม่มีคนเจ็บ"
    boundary.setText(source)
    var start = boundary.first
    var end = boundary.next

    while (end != BreakIterator.DONE) {
      print(source.substring(start,end) + "|")
      start = end
      end = boundary.next
    }

    println()
  }
}