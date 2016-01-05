package com.tegonal.highscoreservice

import akka.actor.ActorSystem
import akka.event.{ Logging, LoggingAdapter }
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.ActorMaterializer
import scala.concurrent.Future
import org.joda.time.DateTime
import com.github.nscala_time.time.Implicits.DateTimeOrdering

trait HighscoreDataSource {
  def currentHighscores(): Future[Either[String, List[Score]]]

  def scoresHistory(): Future[Either[String, List[Score]]]

  def add(score: Score): Future[Either[String, Score]]
}

object InMemoryHighscoreDataSource extends HighscoreDataSource {
  var scores = List[Score]()

  def currentHighscores = Future.successful(Right(scores.sortBy(_.value).reverse.take(10)))

  def scoresHistory = Future.successful(Right(scores.sortBy(_.timestamp).reverse))

  def add(score: Score) = Future.successful(Right {
    score.timestamp match {
      case Some(timestamp) =>
        scores = score :: scores
        score
      case None =>
        val withTimestamp = score.copy(timestamp = Some(new DateTime))
        scores = withTimestamp :: scores
        withTimestamp
    }
  })

}

trait HighscoreDataSourceComponent {
  val highscoreDataSource: HighscoreDataSource
}

trait InMemoryHighscoreDataSourceComponent extends HighscoreDataSourceComponent {
  val highscoreDataSource = InMemoryHighscoreDataSource
}