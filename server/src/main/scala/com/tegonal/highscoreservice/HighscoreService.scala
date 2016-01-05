package com.tegonal.highscoreservice

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.{ HttpResponse, HttpRequest }
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import scala.concurrent.duration._

trait HighscoreService extends BaseService with HighscoreDataSourceComponent {

  protected val serviceName = "highscoreservice"

  protected val routes =
    logRequestResult("highscoreservice") {
      pathPrefix("highscores") {
        (get & path("history")) {
          complete {
            highscoreDataSource.scoresHistory().map[ToResponseMarshallable] {
              case Right(scores)      => scores
              case Left(errorMessage) => BadRequest -> errorMessage
            }
          }
        } ~
          get {
            complete {
              highscoreDataSource.currentHighscores().map[ToResponseMarshallable] {
                case Right(scores)      => scores
                case Left(errorMessage) => BadRequest -> errorMessage
              }
            }
          } ~
          (post & entity(as[Score])) { score =>
            complete {
              highscoreDataSource.add(score).map[ToResponseMarshallable] {
                case Right(score)       => score
                case Left(errorMessage) => BadRequest -> errorMessage
              }
            }
          }
      }
    }
}
