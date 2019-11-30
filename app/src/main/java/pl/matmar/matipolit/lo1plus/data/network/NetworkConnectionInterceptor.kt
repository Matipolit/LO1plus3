package pl.matmar.matipolit.lo1plus.data.network

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import pl.matmar.matipolit.lo1plus.utils.NoInternetException

class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isInternetAvailable()){
            throw NoInternetException("Brak dostępu do internetu")
        }
        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable(): Boolean{
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }
}