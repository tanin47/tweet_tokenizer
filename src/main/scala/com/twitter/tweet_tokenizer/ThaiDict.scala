package com.twitter.tweet_tokenizer

import scala.io.Source

class ThaiDict(dictFile: String) {
  val hash = Source.fromURL(getClass.getResource(dictFile)).getLines().flatMap { line =>
    val sanitized = line.trim

    if (!sanitized.startsWith("#")) {
      Some(sanitized)
    } else {
      None
    }
  }.toSet

  def hasWord(s: String) = hash.contains(s)
}
