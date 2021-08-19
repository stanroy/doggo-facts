package com.stanroy.doggofacts.model.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset

class DogFactsApiServiceTest {

    private lateinit var service: DogFactsApiService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogFactsApiService::class.java)
    }

    private fun enqueueMockResponse(fileName: String) {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        val source = inputStream?.source()?.buffer()
        val mockResponse = MockResponse()
        source?.let {
            mockResponse.setBody(it.readString(Charset.defaultCharset()))
        }

        server.enqueue(mockResponse)
    }

    @Test
    fun test_callApi_fetchFacts_returnFactList() {
        runBlocking {
            enqueueMockResponse("dogApiResponse.json")
            val responseBody = service.fetch30RandomFacts().body()
            val request = server.takeRequest()

            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/dogs?number=30")
        }
    }

    @Test
    fun test_callApi_fetchFacts_returnsCorrectListSize() {
        runBlocking {
            enqueueMockResponse("dogApiResponse.json")
            val responseBody = service.fetch30RandomFacts().body()

            assertThat(responseBody).hasSize(30)
        }
    }

    @Test
    fun test_callApi_fetchFacts_returnsCorrectContent() {
        runBlocking {
            enqueueMockResponse("dogApiResponse.json")
            val responseBody = service.fetch30RandomFacts().body()
            val fact = responseBody?.get(0)?.fact

            assertThat(fact).isEqualTo("Spiked dog collars were used to protect dogs' throats from wolf attacks in ancient Greece.")
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}