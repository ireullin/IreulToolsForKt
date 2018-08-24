package libs.http


class Response(val body: String, val statusCode: Int, val message: String){
    override fun toString(): String {
        return StringBuilder(100)
                .append("StatusCode: ").append(statusCode).append("\n")
                .append("Message: ").append(message).append("\n")
                .append("Body: ").append(body)
                .toString()
    }
}