package utils.actions

import play.api.Logger
import play.api.mvc.{ActionBuilder, Request, Result}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object LoggingAction extends ActionBuilder[Request] {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    Logger.info("Request: " + request)
    val responseFuture: Future[Result] = block(request)
    responseFuture.map { response =>
      Logger.info("Response: " + response)
    }
    responseFuture
  }
}
