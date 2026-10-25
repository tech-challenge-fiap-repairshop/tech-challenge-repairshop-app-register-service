package com.cao.repairshop.core.exception

import java.util.UUID

object ErrorMessages {

    object Customer {
        const val NOT_FOUND = "Customer not found."
        const val DUPLICATE_DOCUMENT = "Customer with this document already exists."
        const val DUPLICATE_EMAIL = "Customer with this email already exists."
        const val HAS_SERVICE_ORDERS = "Cannot delete customer with existing service orders."
        const val USER_NOT_FOUND_FOR_EMAIL = "No registered user found for the provided email. The customer must have an active user account."
        const val USER_NOT_CUSTOMER_ROLE = "The user associated with this email does not have the CUSTOMER role."
    }

    object Vehicle {
        const val NOT_FOUND = "Vehicle not found."
        const val DUPLICATE_PLATE = "Vehicle with this plate already exists."
        const val HAS_SERVICE_ORDERS = "Cannot delete vehicle with existing service orders."
    }

    object ServiceOrder {
        const val NOT_FOUND = "Service order not found."
        const val NOT_ALL_FINALIZED = "Cannot finalize OS: not all services are FINALIZED."
    }

    object Execution {
        const val NOT_FOUND = "Execution not found."
    }

    object Insume {
        const val NOT_FOUND = "Insume not found."
        fun notFoundById(id: UUID) = "Insume not found: $id"
    }

    object Invoice {
        const val NOT_FOUND = "Invoice not found."
        const val DUPLICATE_SERVICE_ORDER = "Invoice already exists for this service order."
        const val DUPLICATE_NUMBER = "Invoice with this number already exists."
    }

    object User {
        const val DUPLICATE_EMAIL = "User with this email already exists."
        const val DUPLICATE_CPF = "User with this CPF already exists."
        const val INVALID_CREDENTIALS = "Invalid credentials."
        const val PASSWORD_ENCODING_FAILED = "Password encoding failed."
    }

    object Document {
        const val INVALID_FORMAT = "Document must be a valid CPF (11 digits) or CNPJ (14 digits)."
        const val INVALID_CPF = "Invalid CPF."
        const val INVALID_CNPJ = "Invalid CNPJ."
    }

    object Plate {
        const val INVALID_FORMAT = "Invalid plate format. Expected ABC-1234 or ABC1D23."
    }

    object StateTransition {
        fun invalid(from: Any, to: Any) = "Invalid status transition from $from to $to."
    }
}
