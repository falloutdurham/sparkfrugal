import org.scalatest.{FlatSpec, BeforeAndAfter}
import co.oxfordcomma.demo.frugal.Frugal1BitStreaming

class Frugal1BitStreamingSpec extends FlatSpec with BeforeAndAfter {

  "frugal1bit" should "initialize to zero" in {
    val estimate = Frugal1BitStreaming.frugal1bit("test-key", Seq(0), None)
    assert(estimate == Some(0L))
  }
}

