package com.cao.repairshop.core.validator.impl

import com.cao.repairshop.core.validator.annotation.SafeString
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class SafeStringValidator : ConstraintValidator<SafeString, String> {

    private val dangerousPatterns = arrayOf(
        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("src[\r\n]*=[\r\n]*'(.*?)'", Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL),
        Pattern.compile("src[\r\n]*=[\r\n]*\"(.*?)\"", Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL),
        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL),
        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL),
        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL),
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL),
        Pattern.compile("<(.*?)>", Pattern.CASE_INSENSITIVE)
    )

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrBlank()) return true

        for (pattern in dangerousPatterns) {
            if (pattern.matcher(value).find()) return false
        }

        return true
    }
}
