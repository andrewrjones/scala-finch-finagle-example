package com.andrewjones.services

import com.andrewjones.models.Message
import com.twitter.util.Future

class ExampleService {
  val message = Message("Hello, world!")

  def getMessage(): Future[Message] = {
    Future.value(message)
  }

  def acceptMessage(incomingMessage: Message): Future[Message] = {
    Future.value(incomingMessage)
  }
}
