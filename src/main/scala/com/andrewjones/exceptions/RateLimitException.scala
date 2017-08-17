package com.andrewjones.exceptions

import java.util.Date
import java.text.SimpleDateFormat

/** Custom exception when being rate limited by GitHub.
  *
  * @param reset The epoch time until the limit is reset.
  */
case class RateLimitException(reset: Long) extends Exception {
  val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
  override def getMessage: String = {
    s"GitHub rate limit exceeded. Will be reset at (${df.format(new Date(reset * 1000))})."
  }
}
