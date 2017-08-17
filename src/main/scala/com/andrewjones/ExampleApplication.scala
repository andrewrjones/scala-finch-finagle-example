package com.andrewjones

import com.andrewjones.models.{GithubRepositories, Message}
import com.andrewjones.services.{ExampleService, GithubService}
import com.twitter.app.Flag
import com.twitter.finagle.Http
import com.twitter.server.TwitterServer
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch.{Endpoint, _}
import io.finch.circe._

object ExampleApplication extends TwitterServer {
  val port: Flag[Int] = flag("port", 8081, "TCP port for HTTP server")

  val exampleService = new ExampleService
  val githubService = new GithubService

  def hello: Endpoint[Message] = get("hello") {
    exampleService.getMessage().map(Ok)
  }

  def acceptedMessage: Endpoint[Message] = jsonBody[Message]

  def accept: Endpoint[Message] = post("accept" :: acceptedMessage) { incomingMessage: Message =>
    exampleService.acceptMessage(incomingMessage).map(Ok)
  }

  def repositories: Endpoint[List[GithubRepositories]] = get("repositories" :: string) { username: String =>
    githubService.getRepositories(username).map(Ok)
  }

  val api = (hello :+: accept :+: repositories).handle {
    case e: Exception => InternalServerError(e)
  }

  def main(): Unit = {
    log.info(s"Serving the application on port ${port()}")

    val server =
      Http.server
        .withStatsReceiver(statsReceiver)
        .serve(s":${port()}", api.toServiceAs[Application.Json])
    closeOnExit(server)

    Await.ready(adminHttpServer)
  }
}
