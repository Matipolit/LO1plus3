package pl.matmar.matipolit.lo1plus.data.network

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import pl.matmar.matipolit.lo1plus.utils.NoInternetException

class NetworkConnectionInterceptor(context: Context) : Interceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isNetworkAvailable){
            throw NoInternetException("Brak dostępu do internetu")
        }
        return chain.proceed(chain.request())
    }

    /*private fun isInternetAvailable(): Boolean{
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }*/

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.activeNetworkInfo.also {
                return it != null && it.isConnected
            }
        }
}

private lateinit var INSTANCE: NetworkConnectionInterceptor
fun getInterceptor(context: Context) : NetworkConnectionInterceptor{
    synchronized(NetworkConnectionInterceptor::class.java) {
        if(!::INSTANCE.isInitialized){
            INSTANCE = NetworkConnectionInterceptor(context)
        }
    }
    return INSTANCE
}