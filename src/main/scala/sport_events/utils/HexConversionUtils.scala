package sport_events.utils

import sport_events.errors.{DomainServiceError, HexToBinaryConversionError}

import scala.util.Try

object HexConversionUtils {

  private val BinaryLength = 12

 val convertHexStringToBin: String => Either[DomainServiceError, String] = (hex: String) => {
  val hexWithoutPrefix = hex.replace("0x", "")
  Try(Integer.parseInt(hexWithoutPrefix, 16)).toOption match {
    case Some(value) => Right(convertToBinary(value))
    case None => Left(HexToBinaryConversionError(hex))
  }
 }

  val convertBinToDecimal: String => Option[Int] = (binary: String) =>
    Try(Integer.parseInt(binary, 2)).toOption

  private def convertToBinary(i: Int): String = {
    String.format("%" + BinaryLength + "s", Integer.toBinaryString(i)).replaceAll(" ", "0")

  }

}
