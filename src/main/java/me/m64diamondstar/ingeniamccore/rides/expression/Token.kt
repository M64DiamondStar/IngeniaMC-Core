package me.m64diamondstar.ingeniamccore.rides.expression

import java.util.Stack

sealed class Token
data class Variable(val name: String) : Token()
data class Constant(val value: Any) : Token()
data class Operator(val op: String) : Token()
object LeftParenthesis : Token()
object RightParenthesis : Token()

/**
 * Read an expression with
 */
object Expression {
    fun tokenize(expression: String): List<Token> {
        val tokens = mutableListOf<Token>()
        val regex = Regex("""\s*(\(|\)|&&|\|\||==|!=|>=|<=|>|<|[A-Za-z_][A-Za-z0-9_]*|"[^"]*"|\d+\.\d+|\d+)\s*""")
        regex.findAll(expression).forEach { match ->
            val token = match.value.trim()
            tokens.add(
                when {
                    token == "(" -> LeftParenthesis
                    token == ")" -> RightParenthesis
                    token == "&&" || token == "||" || token == "==" || token == "!=" || token == ">" ||
                            token == "<" || token == ">=" || token == "<=" -> Operator(token)

                    token.matches(Regex("\"[^\"]*\"")) -> Constant(token.removeSurrounding("\""))
                    token.matches(Regex("\\d+\\.\\d+")) -> Constant(token.toDouble())
                    token.matches(Regex("\\d+")) -> Constant(token.toInt())
                    else -> Variable(token)
                }
            )
        }
        return tokens
    }

    fun evaluateExpression(tokens: List<Token>, variables: Map<String, Any>): Boolean {
        val outputQueue = mutableListOf<Token>()
        val operatorStack = Stack<Token>()

        val precedence = mapOf("&&" to 1, "||" to 1, ">" to 2, "<" to 2, ">=" to 2, "<=" to 2, "==" to 3, "!=" to 3)

        fun precedenceOf(op: String) = precedence[op] ?: 0

        // Shunting Yard Algorithm for parsing
        for (token in tokens) {
            when (token) {
                is Variable, is Constant -> outputQueue.add(token)
                is Operator -> {
                    while (operatorStack.isNotEmpty() && operatorStack.peek() is Operator &&
                        precedenceOf((operatorStack.peek() as Operator).op) >= precedenceOf(token.op)
                    ) {
                        outputQueue.add(operatorStack.pop())
                    }
                    operatorStack.push(token)
                }

                is LeftParenthesis -> operatorStack.push(token)
                is RightParenthesis -> {
                    while (operatorStack.isNotEmpty() && operatorStack.peek() !is LeftParenthesis) {
                        outputQueue.add(operatorStack.pop())
                    }
                    if (operatorStack.isNotEmpty()) operatorStack.pop()
                }
            }
        }
        while (operatorStack.isNotEmpty()) {
            outputQueue.add(operatorStack.pop())
        }

        // Evaluate RPN Expression
        val evaluationStack = Stack<Any>()

        fun evaluate(op: String, left: Any, right: Any): Boolean {
            return when (op) {
                "&&" -> (left as Boolean) && (right as Boolean)
                "||" -> (left as Boolean) || (right as Boolean)
                ">" -> (left as Comparable<Any>) > right
                "<" -> (left as Comparable<Any>) < right
                ">=" -> (left as Comparable<Any>) >= right
                "<=" -> (left as Comparable<Any>) <= right
                "==" -> left == right
                "!=" -> left != right
                else -> throw IllegalArgumentException("Unknown operator $op")
            }
        }

        for (token in outputQueue) {
            when (token) {
                is Constant -> evaluationStack.push(token.value)
                is Variable -> {
                    val value = variables[token.name]
                        ?: throw IllegalArgumentException("Unknown variable ${token.name}")
                    evaluationStack.push(value)
                }

                is Operator -> {
                    val right = evaluationStack.pop()
                    val left = evaluationStack.pop()
                    evaluationStack.push(evaluate(token.op, left, right))
                }

                LeftParenthesis -> TODO()
                RightParenthesis -> TODO()
            }
        }
        return evaluationStack.pop() as Boolean
    }
}