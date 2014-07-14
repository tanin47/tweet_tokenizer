package com.twitter.tweet_tokenizer

import java.text.BreakIterator
import com.twitter.java.text.{DictionaryBasedBreakIterator, RuleBasedBreakIterator}
import scala.collection.mutable
import java.util.Locale

object Main {
  def main(args: Array[String]) {
    val texts = Seq(
      "ประเทศไทยมีบริการเทเลเท็กซ์โดยไม่คิดมูลค่าทางช่อง 5 มานานกว่า 4 ปีแล้ว",
      "อิพี่ลู่ชอบให้ความสำคัญพี่หมินอ่ะ ทั้งสายตาทั้งการกระทำ มองทีละมุนไปถึงซีลีบรัมซ้าย สุดท้ายก็เกลียดอิพี่ไม่ลง",
      "ตอนเป็นเด็กเธอคงยิ้มได้ทันทีน้ำตาหยุดไหล พอวันเวลามันเลือนผ่านพรากเอารอยยิ้มของเธอนั้นให้หายไปพร้อมกับหัวใจด้านชา",
      "จองจูยอน นางเอกของอูบินในเรื่อง Twenty เรื่องนี้รอดูเป็นพิเศษ บอกเลย อิอิ",
      "ยางมัดผมห่อนี้ 30 บาท คือแพงแล้วนะสำหรับสำเพ็ง ถ้าเป็นชลบุรีหรอ สีละ 20 อ่ะ แพงจุง"
    )

    texts.foreach { text =>
      println("JDK:                    " + jdk(text).mkString("|"))
      println("JDK with our dict:      " + jdk(text).mkString("|"))
      println("Improved:               " + improved(text).mkString("|"))
    }
  }

  val jdkBoundary = BreakIterator.getWordInstance(Locale.forLanguageTag("th"))

  def jdk(text: String): Seq[String] = {
    jdkBoundary.setText(text)

    var start = jdkBoundary.first
    var end = jdkBoundary.next

    val buffer = mutable.ListBuffer[String]()

    while (end != BreakIterator.DONE) {
      buffer.append(text.substring(start, end))

      start = end
      end = jdkBoundary.next
    }

    buffer.toSeq
  }

  val jdkWithOurDict = new DictionaryBasedBreakIterator("WordBreakIteratorData_th", "thai_dict_trie")

  def jdkWithOurDict(text: String): Seq[String] = {
    jdkWithOurDict.setText(text)

    var start = jdkWithOurDict.first
    var end = jdkWithOurDict.next

    val buffer = mutable.ListBuffer[String]()

    while (end != BreakIterator.DONE) {
      buffer.append(text.substring(start, end))

      start = end
      end = jdkWithOurDict.next
    }

    buffer.toSeq
  }

  val improvedTokenizer = new ThaiTokenizer(new ThaiDict("/thai_dict.txt"))
  val improvedBoundary = new RuleBasedBreakIterator("WordBreakIteratorData_th")

  def improved(text: String): Seq[String] = {
    improvedBoundary.setText(text)

    var start = improvedBoundary.first
    var end = improvedBoundary.next

    val buffer = mutable.ListBuffer[String]()

    while (end != BreakIterator.DONE) {
      val s = text.substring(start, end)
      buffer.appendAll(improvedTokenizer.break(s))

      start = end
      end = improvedBoundary.next
    }

    buffer.toSeq
  }
}