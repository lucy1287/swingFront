import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("pwd") val pwd: String,
    @SerializedName("memberType") val memberType: Int,
)

data class RegisterResponse(
    @SerializedName("message") val message: String
)
