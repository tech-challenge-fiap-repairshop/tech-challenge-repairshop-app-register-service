package com.cao.repairshop.register.domain.entities

import com.cao.repairshop.core.exception.ErrorMessages
import com.cao.repairshop.core.exception.InvalidDocumentException

data class Document(val value: String) {

    init {
        val digits = value.replace(Regex("[^0-9]"), "")
        if (digits.length != 11 && digits.length != 14)
            throw InvalidDocumentException(ErrorMessages.Document.INVALID_FORMAT)
        if (digits.length == 11) validateCpf(digits)
        else validateCnpj(digits)
    }

    val normalized: String get() = value.replace(Regex("[^0-9]"), "")

    val type: DocumentType
        get() = if (normalized.length == 11) DocumentType.CPF else DocumentType.CNPJ

    enum class DocumentType { CPF, CNPJ }

    companion object {
        private fun validateCpf(digits: String) {
            if (digits.all { it == digits[0] })
                throw InvalidDocumentException(ErrorMessages.Document.INVALID_CPF)

            val weights1 = intArrayOf(10, 9, 8, 7, 6, 5, 4, 3, 2)
            val weights2 = intArrayOf(11, 10, 9, 8, 7, 6, 5, 4, 3, 2)

            val d1 = checkDigit(digits, weights1)
            val d2 = checkDigit(digits, weights2)

            if (digits[9].digitToInt() != d1 || digits[10].digitToInt() != d2)
                throw InvalidDocumentException(ErrorMessages.Document.INVALID_CPF)
        }

        private fun validateCnpj(digits: String) {
            if (digits.all { it == digits[0] })
                throw InvalidDocumentException(ErrorMessages.Document.INVALID_CNPJ)

            val weights1 = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
            val weights2 = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

            val d1 = checkDigit(digits, weights1)
            val d2 = checkDigit(digits, weights2)

            if (digits[12].digitToInt() != d1 || digits[13].digitToInt() != d2)
                throw InvalidDocumentException(ErrorMessages.Document.INVALID_CNPJ)
        }

        private fun checkDigit(digits: String, weights: IntArray): Int {
            val sum = weights.indices.sumOf { digits[it].digitToInt() * weights[it] }
            val remainder = sum % 11
            return if (remainder < 2) 0 else 11 - remainder
        }
    }
}
