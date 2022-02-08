package com.tgcs.ktprinter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tgcs.ktprinter.databinding.FragmentFirstBinding
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var printer: EcpPrinter
    private val scope = CoroutineScope(SupervisorJob())
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    init {
        /*
        val map = mapOf(
            "deviceType" to "zebra",
            "connectionType" to "bt",
            "mac" to "48:A4:93:6D:4E:09"
        )

        val factory = EcpPrinterFactory()
        printer = factory.create(map)
        */
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scope = CoroutineScope(SupervisorJob())
        binding.buttonFirst.setOnClickListener {

            val txtMac = view.findViewById<TextInputEditText>(R.id.txtMACInput)

            val map = mapOf(
                "deviceType" to "zebra",
                "connectionType" to "bt",
                "mac" to txtMac.text.toString()
            )

            val factory = EcpPrinterFactory()
            printer = factory.create(map)

            scope.launch {
                for (i in 1..1000) {
                    try {
                        println("Status: " + printer.pollStatus())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    delay(5)
                }
            }

//            printer.line("Printed with Kotlin!")
//            printer.cut()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}