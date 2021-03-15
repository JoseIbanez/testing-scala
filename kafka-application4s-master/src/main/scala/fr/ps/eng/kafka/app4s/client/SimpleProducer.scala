package fr.ps.eng.kafka.app4s.client


import java.util.Properties
import org.apache.kafka.clients.producer._

class Producer {

  def main(args: Array[String]): Unit = {
    writeToKafka("quick-start","value")
  }

  def writeToKafka(topic: String, msg:String): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, "key", msg)
    producer.send(record)
    producer.close()
  }
}


object SimpleProducer extends App {

    val producer = new Producer

    //producer.main(Nil)
    producer.writeToKafka("quick-start","hi there")

}
