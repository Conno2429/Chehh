package io.github.conno2429.chehh

import java.io.PrintStream

fun main() {
    System.setOut(PrintStream(System.out, true, "UTF-8"))
    GameManager.start()
}