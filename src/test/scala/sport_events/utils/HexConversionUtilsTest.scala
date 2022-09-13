package sport_events.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sport_events.errors.HexToBinaryConversionError
import sport_events.utils.HexConversionUtils.{convertBinToDecimal, convertHexStringToBin}

import scala.language.postfixOps

class HexConversionUtilsTest extends AnyFlatSpec with Matchers {

  "convertHexStringToBin" should "convert a hex string to its bin representation" in {
    convertHexStringToBin("0xf81016") shouldBe Right("111110000001000000010110")
    convertHexStringToBin("0x1d8102f") shouldBe Right("1110110000001000000101111")
  }

  "convertHexStringToBin" should "return exception for malformed input" in {
    convertHexStringToBin("81zz016") shouldBe Left(HexToBinaryConversionError("81zz016"))
    convertHexStringToBin("dxx8102f") shouldBe Left(HexToBinaryConversionError("dxx8102f"))
  }

  "convertBinToDecimal" should "return correct decimal value" in {
    convertBinToDecimal("001") should contain( 1)
    convertBinToDecimal("011") should contain( 3)
    convertBinToDecimal("10011") should contain(19)
  }

  "convertBinToDecimal" should "return None for non decimal value" in {
    convertBinToDecimal("001s") shouldBe None
  }

}
