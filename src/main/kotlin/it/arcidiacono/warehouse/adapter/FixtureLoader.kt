package it.arcidiacono.warehouse.adapter

import java.nio.charset.StandardCharsets

object FixtureLoader {
    fun readFile(path: String): String = this.javaClass.getResource(path).readText(StandardCharsets.UTF_8)
}