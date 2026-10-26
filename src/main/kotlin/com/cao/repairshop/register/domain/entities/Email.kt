package com.cao.repairshop.register.domain.entities

data class Email(val value: String) {

    init {
        require(value.isNotBlank() && EMAIL_REGEX.matches(value)) {
            "Invalid email format."
        }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
