package com.example.zeraapp.apis

import com.example.zeraapp.utlis.SharePreference
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {
//    val BASE_URL="https://dashboard.zerafinancial.com:3006/api/"
//    val PrivicyPolicy="https://quadtricssolutions.com/aloe/privacy-policy"
//    val termscondition="https://quadtricssolutions.com/aloe/api/"
 val BASE_Image_URL = "https://dashboard.zerafinancial.com:3006/images/"

    companion object {
        private val BASE_URL = "https://dashboard.zerafinancial.com:3006/api/" //Live Url
         private var retrofit: Retrofit? = null
        fun getClient(): Retrofit? {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
            client.addInterceptor(Interceptor { chain ->
                var request = chain.request()
                request = request
                    .newBuilder()
                    .header(
                        "Accept",
                        "application/vnd.yourapi.v1.full+json"
                    )
                    .addHeader("api_key", "RQq0k95yeHJ5h0AEHy7A8bEAgfWrx3dJEdmAbX9Nvo9Scbq6bwQj9ksF5QMlfijv")
                    .build()
                chain.proceed(request)
            })


            val gson = GsonBuilder()
                .setLenient()
                 .create()



            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())

                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit
        }
        fun getClientToken(bearer: String): Retrofit? {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
            client.addInterceptor(Interceptor { chain ->
                var request = chain.request()
                request = request
                    .newBuilder()
                    .header(
                        "Accept",
                        "application/vnd.yourapi.v1.full+json"
                    )
                    .addHeader("api_key", "RQq0k95yeHJ5h0AEHy7A8bEAgfWrx3dJEdmAbX9Nvo9Scbq6bwQj9ksF5QMlfijv")
                    .addHeader("x-access-token",bearer)
                    .build()
                chain.proceed(request)
            })


            val gson = GsonBuilder()
                .setLenient()
                 .create()



            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())

                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit
        }



    }

}