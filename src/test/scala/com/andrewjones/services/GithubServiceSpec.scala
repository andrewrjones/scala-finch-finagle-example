package com.andrewjones.services

import com.andrewjones.models.GithubRepositories
import com.twitter.finagle.Service
import com.twitter.finagle.http.Response.Ok
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Await, Future}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

class GithubServiceSpec extends FlatSpec with MockitoSugar with Matchers {

  behavior of "the github repositories service"

  it should "get repositories" in {
    val mockedService = mock[Service[Request, Response]]
    val githubService = new GithubService(mockedService)

    // mock the reply from GitHub
    val reply = new Ok()
    reply.contentString = scala.io.Source.fromFile("src/test/resources/repos.json").mkString
    when(mockedService(any[Request])).thenReturn(Future.value(reply))

    val response = Await.result(githubService.getRepositories("andrewrjones"))

    response shouldBe
      List(
        GithubRepositories("andrewrjones/perl5-App-MP4Meta", 115),
        GithubRepositories("andrewrjones/perl5-AtomicParsley-Command", 6624),
        GithubRepositories("andrewrjones/perl5-Dist-Zilla-Plugin-Test-Fixme", 116)
      )
  }

}
