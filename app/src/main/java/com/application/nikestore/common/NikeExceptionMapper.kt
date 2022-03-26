package com.application.nikestore.common

import com.application.nikestore.R
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

class NikeExceptionMapper {
    companion object {
        fun map(throwable: Throwable): NikeException {
            if (throwable is HttpException) {
                try {
                    val errorJsonObject = JSONObject(throwable.response()?.errorBody()!!.string())
                    val errorMessage = errorJsonObject.getString("message")
                    return NikeException(
                        if (throwable.code() == 401) NikeException.Type.AUT else NikeException.Type.SIMPLE,
                        serverMessage = errorMessage,
                    )
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
            return NikeException(NikeException.Type.SIMPLE, R.string.unknownError)

        }
    }
}