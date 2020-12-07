package com.enveu.Logging

import android.util.Log

object EnveuLogs {

    fun printWarning(value : String){
        Log.w("EnveuWarning-->>",value)
    }

    fun printError(value : String){
        Log.w("EnveuError-->>",value)
    }
}