package com.tegonal.highscoreservice

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import akka.stream.ActorMaterializer

object Main extends App with Config with HighscoreService with InMemoryHighscoreDataSourceComponent {
  override protected implicit val system: ActorSystem = ActorSystem()
  override protected implicit val materializer: Materializer = ActorMaterializer()
  override protected implicit val executor = system.dispatcher

  Http().bindAndHandle(routes, httpInterface, httpPort)
}
