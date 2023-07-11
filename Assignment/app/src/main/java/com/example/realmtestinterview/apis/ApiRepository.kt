package com.example.realmtestinterview.apis

class ApiRepository {
     val retrofitService=RetrofitService.getInstance()
    suspend fun getAllMovies() = retrofitService.getAllMovies()

}