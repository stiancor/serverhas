package serverhas

import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Jetty
import org.http4k.server.asServer
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import kotlin.system.measureTimeMillis

fun main() {
    App().startServer()
}

class App {

    private val logger: Logger = Logger.getLogger(javaClass.name)

    fun startServer() {
        val app: HttpHandler = {request: Request ->
            handleRequest(request)
        }
        app.asServer(Jetty(8080)).start()
    }

    private val gson = Gson()
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    fun handleRequest(request: Request): Response {
        val proxyUrl = System.getenv("PROXY_URL")
        val waitTime = System.getenv("WAIT_TIME")

        return when {
            proxyUrl != null -> callProxy(proxyUrl)
            waitTime != null -> doSleepAndRespond( waitTime.toLong())
            else -> throw IllegalStateException("Server is misconfigured. Missing environment variables")
        }
    }

    private fun callProxy(url: String): Response {
        logger.info("Proxying request to $url")
        val stopwatch = Stopwatch()
        stopwatch.start()
        val httpRequest = okhttp3.Request.Builder()
            .url(url)
            .build()

        val response = httpClient.newCall(httpRequest).execute()
        val body = response.body?.string() ?: ""
        val elapsedTime = stopwatch.elapsedMillis()
        logger.info("Call to fetch data took $elapsedTime ms")

        return Response(OK).body(body)
    }

    private fun doSleepAndRespond(millis: Long): Response {
        val elapsedTime = measureTimeMillis {
            logger.info("Sleeping for $millis ms")
            Thread.sleep(millis)
        }
        logger.info("Just woke up after sleeping for $elapsedTime ms")

        val jsonResponse = gson.toJson("Hello, World!")

        return Response(OK).body(jsonResponse)
    }

    class Stopwatch {
        private var startTime: Long = 0

        fun start() {
            startTime = System.currentTimeMillis()
        }

        fun elapsedMillis(): Long {
            return System.currentTimeMillis() - startTime
        }
    }
}

