package com.tegonal.highscoreservice

import scala.concurrent.ExecutionContextExecutor

import akka.actor.ActorSystem
import akka.event.Logging
import akka.event.LoggingAdapter
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.Materializer

trait BaseService extends Protocol with SprayJsonSupport with Config {
  protected def serviceName: String
  protected def system: ActorSystem
  protected implicit def materializer: Materializer
  protected def log: LoggingAdapter = Logging(system, serviceName)
  protected implicit def executor: ExecutionContextExecutor
}
