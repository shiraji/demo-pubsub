package com.github.shiraji.demopubsub

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.cloud.gcp.pubsub.integration.AckMode
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler

@Configuration
class PubSubConfig {

    @Bean
    fun pubsubInputChannel(): MessageChannel {
        println("*** pubsubInputChannel ***")
        return DirectChannel()
    }

    @Bean
    fun messageChannelAdapter(
            @Qualifier("pubsubInputChannel") inputChannel: MessageChannel,
            pubSubTemplate: PubSubTemplate
    ): PubSubInboundChannelAdapter {
        println("*** messageChannelAdapter ***")
        val adapter = PubSubInboundChannelAdapter(pubSubTemplate, "[*** REPLACE THIS ***]")
        adapter.outputChannel = inputChannel
        adapter.ackMode = AckMode.MANUAL
        return adapter
    }

    @Bean
    @ServiceActivator(inputChannel = "pubsubInputChannel")
    fun messageReceiver(): MessageHandler {
        println("*** messageReceiver ***")
        return MessageHandler {
            println("*** MessageHandler ***")
            val consumer = it.headers[GcpPubSubHeaders.ORIGINAL_MESSAGE] as BasicAcknowledgeablePubsubMessage
            val payload = String(it.payload as ByteArray)
            println("Receided : $payload")
            consumer.ack()
        }
    }

}