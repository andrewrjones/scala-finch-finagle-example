package com.andrewjones.services

import cats.data.EitherT
import com.andrewjones.exceptions.RateLimitException
import com.andrewjones.models.GithubRepositories
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.logging.Logger
import com.twitter.util.Future
import io.catbird.util._
import io.circe.generic.auto._
import io.circe.parser.parse

class GithubService(
                     client: Service[Request, Response] = Http.client.withTls("api.github.com").newService(s"api.github.com:443")
                   ) {
  private val log = Logger.get(getClass)

  def getRepositories(username: String): Future[List[GithubRepositories]] = {
    val requestUrl = s"/users/$username/repos"

    val req = Request(requestUrl)
    req.contentType = "application/json"
    req.userAgent = "andrew-jones.com-example"
    req.accept = "application/vnd.github.v3+json"

    // This for comprehension is just shorthand for calling flatMap
    (for {
      response <- EitherT.right(client(req))
      rawJson <- EitherT
        .fromEither[Future](parse(response.getContentString()))
        .leftMap(failure => {
          // TODO: we could do some error handling here and provide a more useful response to the user
          log.error(failure, "error parsing JSON from content string")
          List.empty[GithubRepositories]
        })
      repos <- EitherT
        .fromEither[Future](rawJson.as[List[GithubRepositories]])
        .leftMap(failure => {
          log.error(failure, "error parsing JSON to GithubRepositories")

          // TODO: we could do more error handling here and provide a more useful response to the user
          if (response.statusCode == 403) {
            response.headerMap.get("X-RateLimit-Remaining").foreach(remaining =>
              if (remaining == "0") {
                val exception = new RateLimitException(response.headerMap.get("X-RateLimit-Reset").get.toLong)
                log.warning(exception.getMessage)
                throw exception
              }
            )
          }
          List.empty[GithubRepositories]
        })
    } yield repos).merge
  }

}
