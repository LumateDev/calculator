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
                prevActiveNumber = ""; numberView.text = "" // сложно
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
        if((view is Button) && (prevActiveNumber != "") && (activeOperation != "")){
            activeNumber = checkNumber(editTextInput.text.toString())
            calculate()
            activeOperation = ""
            restart()
            operationView.text = ""
        }
    }

    fun eventButtonNumber(view: View) {
        if(view is Button){
            editTextInput.append(view.text)
        }
    }
    // Cлушатель нажатия на кнопки операций
    fun eventButtonOperation(view: View) {
        if(view is Button){
            checkOper(view.text.toString()) // Проверяем корректность операции
            operationView.text = activeOperation
        }
    }

    private fun checkOper(textOperation:String){
        
        if(editTextInput.text.toString() == "" && activeOperation== ""){
            showError("Введите сначала число")
            return Unit
        }
        if (editTextInput.text.toString() == ""){
            activeOperation = textOperation
            return Unit
        }
        if(activeOperation == ""){
            prevActiveNumber = checkNumber(editTextInput.text.toString())
            numberView.text = prevActiveNumber
            activeOperation = textOperation
            editTextInput.text.clear()
        }
        else if(prevActiveNumber != ""){
            activeNumber = checkNumber(editTextInput.text.toString())
            calculate()
            editTextInput.text.clear()
            activeOperation = textOperation
        }
    }
    private fun checkNumber(textNumber:String):String{
        if(textNumber[0] == '.')
            return "0".plus(textNumber)
        if(textNumber[textNumber.length-1] == '.')
            return textNumber.plus('0')
        return textNumber
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

        if(activeNumber == "")
            showError("11111")
        prevActiveNumber = when(activeOperation){
            "+" -> { (prevActiveNumber.toDouble() + activeNumber.toDouble()).toString() }
            "-" -> { (prevActiveNumber.toDouble() - activeNumber.toDouble()).toString() }
            "*" -> { (prevActiveNumber.toDouble() * activeNumber.toDouble()).toString() }
            "/" -> {
                val result = if (activeNumber.toDouble() == 0.0){
                    editTextInput.text.clear()
                    activeNumber = ""; prevActiveNumber = ""; activeOperation = ""
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