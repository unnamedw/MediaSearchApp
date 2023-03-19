package com.doach.mediasearchapp.android.data.remote.retrofit

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ResultCall<T> constructor(
    private val callDelegate: Call<T>
): Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        return callDelegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback.onResponse(
                        this@ResultCall,
                        Response.success(
                            response.code(),
                            Result.success(response.body()!!)
                        )
                    )
                } else {
                    callback.onResponse(
                        this@ResultCall,
                        Response.success(
                            Result.failure(HttpException(response))
                        )
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val errorMessage = when (t) {
                    is java.io.IOException -> "No internet connection"
                    is HttpException -> "Something went wrong!"
                    else -> t.localizedMessage
                }
                callback.onResponse(
                    this@ResultCall,
                    Response.success(Result.failure(RuntimeException(errorMessage, t)))
                )
            }
        })
    }

    override fun clone(): Call<Result<T>> {
        return ResultCall(callDelegate.clone())
    }

    override fun execute(): Response<Result<T>> {
        throw UnsupportedOperationException("ResponseCall does not support execute.")
    }

    override fun isExecuted(): Boolean {
        return callDelegate.isExecuted
    }

    override fun cancel() {
        return callDelegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return callDelegate.isCanceled
    }

    override fun request(): Request {
        return callDelegate.request()
    }

    override fun timeout(): Timeout {
        return callDelegate.timeout()
    }
}