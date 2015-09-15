package co.oxfordcomma.demo.frugal

import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.streaming._

object Frugal1BitStreaming {

  val quantilePercentage = 0.75

  def frugal1bit(key: String, values: Seq[Long], state: Option[Long]): Option[Long] = {
    var quantile = state.getOrElse(0L)
    values.map( value => {
      val r = scala.util.Random.nextFloat
      value match {
        case x if x > quantile && r > 1 - quantilePercentage => quantile += 1L
        case x if x < quantile && r > quantilePercentage => quantile -= 1L
        case _ => quantile
      }
    })
    Some(quantile)
  }

  def frugalUpdate(iterator: Iterator[(String, Seq[(Long)], Option[(Long)])]) = {
    iterator.flatMap { t => frugal1bit(t._1, t._2, t._3).map(s => (t._1, s)) }
  }

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("FrugalStreaming1Bit") 
    val ssc = new StreamingContext(sparkConf, Seconds(10))
    ssc.checkpoint("./checkpoint")

    val input = ssc.socketTextStream(args(0), args(1).toInt)

    val stream = input.map( x => ("quantile", x.toLong))

    val quantileStream = stream.updateStateByKey[Long](frugalUpdate _,
                         new HashPartitioner(ssc.sparkContext.defaultParallelism),
                         true)
    quantileStream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}