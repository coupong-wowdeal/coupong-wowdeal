package coupong.nbc.coupongwowdeal.exception

data class AccessDeniedException(
    private val text: String
) : SecurityException(
    "Access Denied: $text"
)