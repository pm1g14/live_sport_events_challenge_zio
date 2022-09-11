package sport_events.errors

sealed trait DomainServiceError {
  val message:String
}

case class EventsNotStreamedError(errorCode:String) extends DomainServiceError {
  override val message: String = s"Cannot stream events due to error: $errorCode"
}

case class SourceNotFound(path: String) extends DomainServiceError {
  override val message: String  = s"Source at the path specified: {$path} was not found"
}

case class HexToBinaryConversionError(i: String) extends DomainServiceError {
  override val message: String = s"Cannot convert hex to binary for given value $i"
}

case class ToDomainConversionError(i: String) extends DomainServiceError {
  override val message: String = s"Cannot convert given $i to domain"
}