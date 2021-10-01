package com.tgcs.ktprinter

import java.lang.Exception

class EcpPrinterFactory {

    fun create(options: Map<String, String>): EcpPrinter {
        return when (val type = options["deviceType"] ?: "zebra") {
            "zebra" -> handleZebraPrinter(options)
            else -> throw Exception("DeviceType $type is not supported")
        }
    }

    private fun handleZebraPrinter(options: Map<String, String>): EcpPrinter {
        return ZebraPrinterImpl(options)
    }
}