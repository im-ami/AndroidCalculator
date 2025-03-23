package com.example.calculator

import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.Locale

private const val DISPLAY_KEY = "DISPLAY_VIEW"
private const val RESULT_KEY = "RESULT_VIEW"

enum class Operators(val symbol: Char) {
    PLUS('+'),
    MINUS('-'),
    MULTIPLY('*'),
    DIVIDE('/'),
    DOT('.')
}

class MainActivity : AppCompatActivity() {
    private lateinit var buttonOne: Button
    private lateinit var buttonTwo: Button
    private lateinit var buttonThree: Button
    private lateinit var buttonFour: Button
    private lateinit var buttonFive: Button
    private lateinit var buttonSix: Button
    private lateinit var buttonSeven: Button
    private lateinit var buttonEight: Button
    private lateinit var buttonNine: Button
    private lateinit var buttonZero: Button
    private lateinit var buttonClear: Button
    private lateinit var buttonUndo: Button
    private lateinit var buttonPlus: Button
    private lateinit var buttonMinus: Button
    private lateinit var buttonMultiply: Button
    private lateinit var buttonDivide: Button
    private lateinit var buttonEquals: Button
    private lateinit var buttonDecimal: Button
    private lateinit var display: TextView
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)
        result = findViewById(R.id.result)
        buttonOne = findViewById(R.id.buttonOne)
        buttonTwo = findViewById(R.id.buttonTwo)
        buttonThree = findViewById(R.id.buttonThree)
        buttonFour = findViewById(R.id.buttonFour)
        buttonFive = findViewById(R.id.buttonFive)
        buttonSix = findViewById(R.id.buttonSix)
        buttonSeven = findViewById(R.id.buttonSeven)
        buttonEight = findViewById(R.id.buttonEight)
        buttonNine = findViewById(R.id.buttonNine)
        buttonZero = findViewById(R.id.buttonZero)
        buttonDecimal = findViewById(R.id.button_decimal)
        buttonClear = findViewById(R.id.buttonClear)
        buttonUndo = findViewById(R.id.buttonUndo)
        buttonPlus = findViewById(R.id.buttonPlus)
        buttonMinus = findViewById(R.id.buttonMinus)
        buttonMultiply = findViewById(R.id.buttonMultiply)
        buttonDivide = findViewById(R.id.buttonDivide)
        buttonEquals = findViewById(R.id.buttonEquals)

        if (savedInstanceState != null) {
            display.text = savedInstanceState.getString(DISPLAY_KEY)
            result.text = savedInstanceState.getString(RESULT_KEY)
        }

        buttonClear.setOnClickListener {
            display.text = ""
            result.text = ""
        }

        buttonUndo.setOnClickListener {
            val text = display.editableText
            if (!text.isNullOrEmpty()) {
                text.delete(text.length - 1, text.length)
            }
            try {
                result.text = evaluateExpression()
            } catch (e: IllegalArgumentException) {
                result.text = ""
            } catch (_: ArithmeticException) {
            }
        }

        buttonEquals.setOnClickListener {
            if (checkLastCharIsOperator(display)) {
                Toast.makeText(this, "Invalid format", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    display.text = evaluateExpression()
                    result.text = ""
                } catch (e: IllegalArgumentException) {
                    result.text = ""
                } catch (e: ArithmeticException) {
                    Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val onClickNumber = OnClickListener { view ->
            val button = view as Button
            display.append(button.text.toString())

            try {
                result.text = evaluateExpression()
            } catch (e: IllegalArgumentException) {
                result.text = ""
            } catch (e: ArithmeticException) {
                Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show()
            }

        }

        val onClickOperator = OnClickListener { view ->
            if (checkLastCharIsOperator(display)) {
                Toast.makeText(this, "Invalid format", Toast.LENGTH_SHORT).show()
            } else {
                val button = view as Button
                display.append(button.text.toString())
            }
        }

        buttonOne.setOnClickListener(onClickNumber)
        buttonTwo.setOnClickListener(onClickNumber)
        buttonThree.setOnClickListener(onClickNumber)
        buttonFour.setOnClickListener(onClickNumber)
        buttonFive.setOnClickListener(onClickNumber)
        buttonSix.setOnClickListener(onClickNumber)
        buttonSeven.setOnClickListener(onClickNumber)
        buttonEight.setOnClickListener(onClickNumber)
        buttonNine.setOnClickListener(onClickNumber)
        buttonZero.setOnClickListener(onClickNumber)

        buttonPlus.setOnClickListener(onClickOperator)
        buttonMinus.setOnClickListener(onClickOperator)
        buttonMultiply.setOnClickListener(onClickOperator)
        buttonDivide.setOnClickListener(onClickOperator)
        buttonDecimal.setOnClickListener(onClickOperator)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DISPLAY_KEY, display.text.toString())
        outState.putString(RESULT_KEY, result.text.toString())
    }

    private fun evaluateExpression(): String {
        val expression = ExpressionBuilder(display.text.toString()).build()
        val res = expression.evaluate()

        return if (res % 1 == 0.0) {
            res.toLong().toString()
        } else {
            String.format(Locale.US,"%.2f", res)
        }
    }

    private fun checkLastCharIsOperator(display: TextView): Boolean {
        val text = display.text.toString()

        if ((text.isNotEmpty()) && (Operators.entries.any { it.symbol == text.last() })) {
            return true
        } else if (text.isEmpty()) {
            return true
        }
        return false
    }
}
