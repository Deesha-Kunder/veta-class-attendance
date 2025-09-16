package com.ahseed.veta.utils

object ValidationUtil {
    fun validateUsername(username:String): ValidateResult{
        return when{
            username.isEmpty() -> ValidateResult.Valid
            username.length > 20 -> ValidateResult.Invalid("Username cannot exceed 20 characters")
            username.length < 3 -> ValidateResult.Invalid("Username cannot be less than 3 characters")
            !username.matches(Regex("^[a-zA-Z0-9_]+$")) -> ValidateResult.Invalid("Username can only contain letters, numbers, and underscores")
            else -> ValidateResult.Valid
        }
    }
    fun validateEmail(email: String): ValidateResult {
        return when {
            email.isEmpty() -> ValidateResult.Valid
            !isValidEmailFormat(email) -> ValidateResult.Invalid("Please enter a valid email address")
            else -> ValidateResult.Valid
        }
    }
    fun validatePassword(password: String): ValidateResult {
        return when {
            password.isEmpty() -> {
                ValidateResult.Valid
            }
            password.length < 8 -> {
                ValidateResult.Invalid("Password must be at least 8 characters")
            }
            password.length > 50 -> {
                ValidateResult.Invalid("Password must be 50 characters or less")
            }
            !password.matches(Regex(".*[A-Za-z].*")) -> {
                ValidateResult.Invalid("Password must contain at least one letter")
            }
            !password.matches(Regex(".*[0-9].*")) -> {
                ValidateResult.Invalid("Password must contain at least one number")
            }
            else -> {
                ValidateResult.Valid
            }
        }
    }
    private fun isValidEmailFormat(email: String): Boolean {
        val emailPattern = Regex(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        )

        // Basic format check
        if (!emailPattern.matches(email)) return false

        // Additional checks
        val parts = email.split("@")
        if (parts.size != 2) return false

        val localPart = parts[0]
        val domainPart = parts[1]

        // Local part validations
        if (localPart.isEmpty() || localPart.length > 64) return false
        if (localPart.startsWith(".") || localPart.endsWith(".")) return false
        if (localPart.contains("..")) return false

        // Domain part validations
        if (domainPart.isEmpty() || domainPart.length > 255) return false
        if (domainPart.startsWith(".") || domainPart.endsWith(".")) return false
        if (domainPart.contains("..")) return false
        if (!domainPart.contains(".")) return false

        // Check for common valid TLDs
        val tld = domainPart.substringAfterLast(".")
        val commonTlds = setOf(
            "com", "org", "net", "edu", "gov", "mil", "int", "co", "uk", "ca", "au",
            "de", "fr", "jp", "cn", "ru", "br", "in", "mx", "es", "it", "nl", "se",
            "no", "pl", "tr", "kr", "za", "ar", "cl", "info", "biz", "name", "pro"
        )
        if (tld.length < 2 || (!commonTlds.contains(tld.lowercase()) && tld.length < 2)) return false

        return email.length <= 254
    }

}

sealed class ValidateResult {
    object Valid : ValidateResult()
    data class Invalid(val errorMessage: String) : ValidateResult()
}

fun ValidateResult.isValid(): Boolean = this is ValidateResult.Valid
fun ValidateResult.getMessage(): String? =
    if (this is ValidateResult.Invalid) errorMessage else null