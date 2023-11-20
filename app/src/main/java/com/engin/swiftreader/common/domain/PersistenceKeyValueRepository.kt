package com.engin.swiftreader.common.domain

interface PersistenceKeyValueRepository {
    suspend fun getString(key :String) :String?
    suspend fun getInt(key: String) : Int?
    suspend fun getBoolean(key : String ): Boolean?
    suspend fun putString(key :String,value :String)
    suspend fun putInt(key: String,value : Int)
    suspend fun putBoolean(key : String, value : Boolean)
    suspend fun removeValue(key : String)
}