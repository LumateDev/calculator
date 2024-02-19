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

    private var activeNumber: String = "" // 2 число
    private var prevActiveNumber : String = " " // 1 число
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

        if((view is Button) && (prevActiveNumber != "") && (activeOperation != "")){

            if(editTextInput.text.toString() == "-" )

                showError("Вы не ввели второе число")

            else if (editTextInput.text.toString().startsWith("." ) ||editTextInput.text.toString().startsWith("-." ) )
                showError("Число не может начинаться с точки")
            else{
                activeNumber = editTextInput.text.toString()
                calculate()
                activeOperation = ""
                restart()
                operationView.text = ""
            }

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


            if (view.text == "-" && editTextInput.text.toString() == "" && activeOperation== "" ){
                editTextInput.append("-");
                return Unit
            }
            if(view.text == "-" && editTextInput.text.toString() == "" && activeOperation != ""){
                editTextInput.append("-");
                return Unit
            }
            checkOper(view.text.toString()) // Проверяем корректность операции
            operationView.text = activeOperation
        }
    }
    fun returnNumber (view: View){
        if (view is TextView){
            if(view.text != "")
                editTextInput.append(view.text.toString())
        }
    }

    private fun checkOper(textOperation:String) {
        if (editTextInput.text.toString() == "" && activeOperation == "-"){
            editTextInput.append(activeOperation)
            return Unit
    }
        
        if(editTextInput.text.toString() == "" && activeOperation== ""){
            showError("Введите сначала число")
            return Unit
        }
        if (editTextInput.text.toString() == ""){
            activeOperation = textOperation
            return Unit
        }
        if(activeOperation == ""){
            if (editTextInput.text.toString().startsWith("." ) ||editTextInput.text.toString().startsWith("-." ) )
                showError("Число не может начинаться с точки")

            else{
                prevActiveNumber = editTextInput.text.toString()
                numberView.text = prevActiveNumber
                activeOperation = textOperation
                editTextInput.text.clear()
            }

        }
        else if(prevActiveNumber != ""){
            if (editTextInput.text.toString().startsWith("." ) ||editTextInput.text.toString().startsWith("-." ) )
                showError("Число не может начинаться с точки")
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