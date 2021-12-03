package com.tgcs.ktprinter

interface EcpPrinter {
    fun pollStatus() : Boolean
    fun line(text: String)
    fun cut()
}