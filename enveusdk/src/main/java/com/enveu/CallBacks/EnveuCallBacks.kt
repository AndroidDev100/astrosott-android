package com.enveu.CallBacks

import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory

interface EnveuCallBacks{

    fun success(status: Boolean, baseCategory : List <BaseCategory>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}