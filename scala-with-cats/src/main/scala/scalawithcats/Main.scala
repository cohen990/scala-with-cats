package scalawithcats

import cats.implicits._

object Main extends App {
  println("Goodbye " |+| "Cats!".toUpperCase())
  println(Json.toJson("hello"))
  println(Json.toJson(Person("Dave", "dave@cats.com")))
}

sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
final case object JsNull extends Json

trait JsonWriter[A] {
  def write(value: A): Json
}

final case class Person(name: String, email: String)

object JsonWriterInstances {
  implicit val stringWriter: JsonWriter[String] =
  new JsonWriter[String] {
    def write(value: String): Json =
      JsString(value)
  }

  implicit val personWriter: JsonWriter[Person] = 
    new JsonWriter[Person] {
      def write(value: Person): Json = 
        JsObject(Map(
          "name" -> JsString(value.name),
          "email" -> JsString(value.email)
        ))
    }
}

object Json {
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = 
    w.write(value)
}