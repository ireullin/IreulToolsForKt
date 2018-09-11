package libs.http

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

import java.security.cert.X509Certificate
import javax.net.ssl.*

class SimpleSender
private constructor(url: String){

    private val httpcn: HttpURLConnection

    private class FakeX509TrustManager : X509TrustManager {
        override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {
        }

        override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, s: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate>? {
            return null
        }
    }


    private class FakeHostnameVerifier : HostnameVerifier {
        override fun verify(s: String, sslSession: SSLSession): Boolean {
            return true
        }
    }

    init {
        ignoreSslVerify()
        val u = URL(url)
        this.httpcn = u.openConnection() as HttpURLConnection
    }

    fun setReadTimeout(timeout: Int):SimpleSender {
        this.httpcn.readTimeout = timeout
        return this
    }

    fun setConnectTimeout(timeout: Int):SimpleSender {
        this.httpcn.connectTimeout = timeout
        return this
    }

    fun get(): Response {
        httpcn.requestMethod = "GET"
        val body = extractStream(httpcn.inputStream)
        val rsp = Response(body, httpcn.responseCode, httpcn.responseMessage)
        httpcn.disconnect()
        return rsp
    }

    fun post(data: String):Response {
        httpcn.requestMethod = "POST"
        httpcn.doOutput = true
        val output = httpcn.outputStream
        output.write(data.toByteArray(charset("UTF-8")))
        output.flush()

        val body = extractStream(httpcn.inputStream)
        val rsp = Response(body, httpcn.responseCode, httpcn.responseMessage)
        httpcn.disconnect()
        return rsp
    }

    fun setHeader(name: String, value: String):SimpleSender {
        this.httpcn.setRequestProperty(name, value)
        return this
    }


    private fun ignoreSslVerify():SimpleSender {

        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(fakeX509TrustManager)

        // Install the all-trusting trust manager
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(fakeHostnameVerifier)
        return this
    }

    private fun extractStream(stream: InputStream): String {

        val buff = StringBuilder(100)
        try {
            BufferedReader(InputStreamReader(stream, "UTF-8")).use { reader ->
                while (true) {
                    val s = reader.readLine() ?: break
                    buff.append(s)
                }
            }
        } catch (e: Exception) {
            throw e
        }

        return buff.toString()

    }

    companion object {
        private val fakeX509TrustManager = FakeX509TrustManager()
        private val fakeHostnameVerifier = FakeHostnameVerifier()

        fun of(url: String):SimpleSender {
            return SimpleSender(url)
        }

        fun get(url: String): String {
            return of(url).get().body
        }

        fun post(url: String, data: String): String {
            return of(url).post(data).body
        }
    }


}
