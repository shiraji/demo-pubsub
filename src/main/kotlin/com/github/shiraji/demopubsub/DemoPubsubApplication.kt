package com.github.shiraji.demopubsub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoPubsubApplication

fun main(args: Array<String>) {
	runApplication<DemoPubsubApplication>(*args)
}

