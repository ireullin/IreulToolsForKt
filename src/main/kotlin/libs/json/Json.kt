package libs.json

import com.fasterxml.jackson.databind.ObjectMapper

class Json(val parserInstance:ObjectMapper){
    companion object {
        fun stringify(obj:Any) = Json().stringify(obj)
        fun parse(s:String) = Json().stringify(s)
    }
    constructor():this(ObjectMapper())
    fun stringify(obj:Any) = parserInstance.writeValueAsString(obj)
    fun parse(s:String) = parserInstance.readTree(s)
}