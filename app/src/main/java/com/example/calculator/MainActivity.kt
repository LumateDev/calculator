package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.ConsoleMessage
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var numberView: TextView
    private lateinit var operationView: TextView
    private lateinit var editTextInput: EditText
    private lateinit var buttonClear: Button
    private lateinit var buttonDelete: Button

    private var activeNumber: String = ""
    private var prevActiveNumber : String = " "
    private var activeOperation: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberView = findViewById(R.id.textViewNumber)
        operationView = findViewById(R.id.textViewOperation)
        editTextInput = findViewById(R.id.editTextInput)
        buttonClear = findViewById(R.id.buttonClear)
        buttonDelete = findViewById(R.id.buttonDelete)



        buttonClear.setOnClickListener {
            numberView.text = ""; operationView.text = ""
            activeNumber = "";prevActiveNumber = ""; activeOperation = ""
            editTextInput.text.clear()
        }

        buttonDelete.setOnClickListener {
            var number = editTextInput.text.toString()
            if(number != ""){
                number = number.dropLast(1)
                editTextInput.text.clear(); editTextInput.append(number)
                return@setOnClickListener
            }
            if(activeOperation != ""){
                activeOperation = ""
                restart()
                prevActiveNumber = ""; numberView.text = ""
                return@setOnClickListener
            }
            if(prevActiveNumber != ""){
                prevActiveNumber = prevActiveNumber.dropLast(1)
                restart()
                return@setOnClickListener
            }
        }


    }
    fun buttonResult(view: View) {

        if((view is Button) && (prevActiveNumber != "") && (activeOperation != "") && editTextInput.text.toString() != ""){


            if (editTextInput.text.toString().startsWith("." ) ||editTextInput.text.toString().startsWith("-." ) )
                showError("Число не может начинаться с точки")
            else if (editTextInput.text.toString() == "-"){

                showError("Введите второе число")
            }

            else{
                activeNumber = editTextInput.text.toString()
                calculate()
                restart()
                activeOperation = ""
                operationView.text = ""
                numberView.text = ""
                editTextInput.text.append(prevActiveNumber)
            }

        }

        else{
            if((editTextInput.text.toString() == "" && activeOperation == "") || (editTextInput.text.toString()== "-" && activeOperation == ""))
            {
                showError("Нужно ввести первое число")
            }
            else if (  activeOperation !=  "" && editTextInput.text.toString() == ""){
                showError("Введите второе число")

             }
            else if(activeOperation == "")
                showError("Нужно ввести операцию")
        }
    }

    fun eventButtonNumber(view: View) {
        if(view is Button){
            if(view.text == "." && (editTextInput.text.toString().isBlank() || editTextInput.text.toString() == "-")){
                showError("Число не может начинаться с точки")
                return Unit
            }
            editTextInput.append(view.text)
        }
    }
    fun eventButtonOperation(view: View) {
        if(view is Button){


            checkOper(view.text.toString())
            operationView.text = activeOperation
        }
    }


    private fun checkOper(textOperation:String) {

        if(editTextInput.text.toString() == "" && numberView.text.toString() == "" && textOperation != "-"){
            showError("Введите число")
            return Unit
        }
        if(editTextInput.text.toString() == "-" && numberView.text == ""){
            showError("Введите число")
            return Unit
        }
        if(editTextInput.text.toString() == "" &&numberView.text != "" && operationView.text != "-" && textOperation == "-" ){
            editTextInput.append(textOperation)
            return Unit
        }
        if(editTextInput.text.toString() == "-" && numberView.text != "" && operationView.text != "-" && textOperation == "-" ){
            operationView.text = "-"
            editTextInput.text.clear()
        }
        if(editTextInput.text.toString() == "-" && numberView.text != "" && operationView.text != "-" && textOperation != "-" ){
            editTextInput.text.clear()
            activeOperation = textOperation
        }
        if(editTextInput.text.toString() == "" && numberView.text.toString() == "" && textOperation == "-"){
            editTextInput.append(textOperation)
            return Unit
        }
        if (editTextInput.text.toString() == ""){
            activeOperation = textOperation
            return Unit
        }

        if(activeOperation == ""){
            prevActiveNumber = editTextInput.text.toString()
            numberView.text = prevActiveNumber
            activeOperation = textOperation
            editTextInput.text.clear()
        }

        else if(prevActiveNumber != ""){
            if (editTextInput.text.toString().startsWith("." ) ||editTextInput.text.toString().startsWith("-." ) )
                showError("Число не может начинаться с точки")
            else if(editTextInput.text.toString() == "0" && operationView.text == "/"){
                editTextInput.text.clear()
                activeNumber = ""; prevActiveNumber = ""; activeOperation = ""; operationView.text= "";
                restart()
                showError("Деление на 0")
            }
            else{
                activeNumber = editTextInput.text.toString()
                calculate()
                editTextInput.text.clear()
                activeOperation = textOperation
            }


        }
    }

    private fun showError(message:String){
        editTextInput.error = message
        editTextInput.requestFocus()
    }

    private fun restart(){
        editTextInput.text.clear(); editTextInput.append(activeNumber)
        operationView.text = activeOperation; numberView.text = prevActiveNumber
    }

    private fun calculate(){


        prevActiveNumber = when(activeOperation){
            "+" -> { (prevActiveNumber.toDouble() + activeNumber.toDouble()).toString() }
            "-" -> { (prevActiveNumber.toDouble() - activeNumber.toDouble()).toString() }
            "*" -> { (prevActiveNumber.toDouble() * activeNumber.toDouble()).toString() }
            "/" -> {
                val result = if (activeNumber.toDouble() == 0.0){
                    editTextInput.text.clear()
                    activeNumber = ""; prevActiveNumber = ""; activeOperation = ""; operationView.text= "";
                    restart()
                    showError("Деление на 0")
                    ""
                }
                else{
                    (prevActiveNumber.toDouble() / activeNumber.toDouble()).toString()
                }
                result
            }
            else -> {
                showError("Error")
                "0"
            }
        }
        activeNumber = ""
        numberView.text = prevActiveNumber
    }


}