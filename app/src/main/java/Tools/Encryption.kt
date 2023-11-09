package Tools

import android.util.Base64
import java.nio.charset.StandardCharsets

class Encryption {
    companion object{
        fun encryptData(password: String): String {
            val encode = password.toByteArray(StandardCharsets.UTF_8)
            return Base64.encodeToString(encode, Base64.DEFAULT).trim()
        }
    }
}