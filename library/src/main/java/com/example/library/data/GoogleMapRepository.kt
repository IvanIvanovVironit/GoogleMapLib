package com.example.library.data

import com.example.library.common.Result
import com.example.library.api.GoogleMapApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


class GoogleMapRepository (private val api: GoogleMapApi, private val KEY: String)  {

    suspend fun getInfo(id: String): Result<PlaceInfo> {
        return try {
            val response = api.getInfo(id, KEY)
            val data = response.result
            Result.Success(data)
        } catch (exception: IOException) {
            Result.Failure(exception)
        } catch (exception: HttpException) {
            Result.Failure(exception)
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }

    suspend fun getRoutes(origin: String, destination: String): Result<List<Routes>> {
        return try {
            val response = api.getRoutes(origin, destination, KEY)
            val data = response.routes
            Result.Success(data)
        } catch (exception: IOException) {
            Result.Failure(exception)
        } catch (exception: HttpException) {
            Result.Failure(exception)
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}