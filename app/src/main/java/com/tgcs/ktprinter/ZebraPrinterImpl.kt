package com.tgcs.ktprinter

import com.zebra.sdk.comm.BluetoothConnection
import com.zebra.sdk.comm.Connection
import com.zebra.sdk.comm.TcpConnection
import com.zebra.sdk.printer.*
import java.lang.Exception

class ZebraPrinterImpl(private val options: Map<String, String>) : EcpPrinter {
    private val connection: Connection
    private val printer: ZebraPrinter
    private val linkOsPrinter: ZebraPrinterLinkOs

    init {
        connection = buildConnection(options["connectionType"] ?: "bt")
        connection.open()
        printer = ZebraPrinterFactory.getInstance(connection)
        linkOsPrinter = ZebraPrinterFactory.createLinkOsPrinter(printer)
    }


    private fun buildConnection(connectionType: String): Connection {
        return when (connectionType) {
            "bt" -> BluetoothConnection(options["mac"])
            "tcp" -> TcpConnection(options["host"], Integer.parseInt(options["port"] ?: "1234"))
            else -> throw Exception("Unknown connection type $connectionType")
        }
    }

    override fun pollStatus(): Boolean {
        return linkOsPrinter.currentStatus.isReadyToPrint
    }

    override fun line(text: String) {

        val printerLanguage = printer.printerControlLanguage
        SGD.SET("device.languages", "zpl", connection)

        val command = when (printerLanguage) {
            PrinterLanguage.ZPL -> "^XA^CFA,40^FD$text^XZ".toByteArray()
            else -> throw Exception("Only ZPL is supported in this POC")
        }

        connection.write(command)
    }

    override fun cut() {
        val printerLanguage = printer.printerControlLanguage
        SGD.SET("device.languages", "zpl", connection)

        val command = when (printerLanguage) {
            PrinterLanguage.ZPL -> "^MMC".toByteArray()
            else -> throw Exception("Only ZPL is supported in this POC")
        }

        connection.write(command)
    }


}