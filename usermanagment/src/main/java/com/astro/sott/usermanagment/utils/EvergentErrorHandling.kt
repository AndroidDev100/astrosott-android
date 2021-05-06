package com.astro.sott.usermanagment.utils

import android.content.Context
import com.astro.sott.usermanagment.R
import com.astro.sott.usermanagment.modelClasses.ErrorModel
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem

class EvergentErrorHandling {

    fun getErrorMessage(errorBody: List<FailureMessageItem?>?, context: Context): ErrorModel {
        var message = ""
        var errorCode = ""

        if (errorBody != null && errorBody.size!! > 0) {
            // message = errorBody[0]?.errorMessage!!
            errorCode = errorBody[0]?.errorCode!!
        }

        when (errorCode) {
            "111111111" -> message = context.resources.getString(R.string.authentication_failed)
            "eV1259" -> message = context.resources.getString(R.string.account_already_exist)
            "eV4161" -> message = context.resources.getString(R.string.account_already_exist_sp_account)
            "eV1268" -> message = context.resources.getString(R.string.no_zone)
            "eV1062" -> message = context.resources.getString(R.string.user_name_exist)
            "eV2160" -> message = context.resources.getString(R.string.alternate_username_exist)
            "eV2793" -> message = context.resources.getString(R.string.duplicate_account_rejected)
            "eV1270" -> message = context.resources.getString(R.string.invalid_account)
            "eV1044" -> message = context.resources.getString(R.string.invalid_salutation)
            "eV1047" -> message = context.resources.getString(R.string.first_name_exceed)
            "eV1048" -> message = context.resources.getString(R.string.last_name_exceed)
            "eV1057" -> message = context.resources.getString(R.string.email_exceed)
            "eV1064" -> message = context.resources.getString(R.string.user_name_exceed)
            "eV1058" -> message = context.resources.getString(R.string.enter_valid_email)
            "eV1065" -> message = context.resources.getString(R.string.minimum_user_name_length)
            "eV1066" -> message = context.resources.getString(R.string.user_name_space)
            "eV1069" -> message = context.resources.getString(R.string.minimum_password_length)
            "eV1068" -> message = context.resources.getString(R.string.password_exceed)
            "eV2019" -> message = context.resources.getString(R.string.social_type_exceed)
            "eV1634" -> message = context.resources.getString(R.string.invalid_country)
            "eV1246" -> message = context.resources.getString(R.string.invalid_zipcode)
            "eV2791" -> message = context.resources.getString(R.string.donot_meet_international_standard)
            "eV1244" -> message = context.resources.getString(R.string.invalid_state)
            "eV2362" -> message = context.resources.getString(R.string.social_login_id_exists)
            "20003" -> message = context.resources.getString(R.string.missing_param)
            "eV2327" -> message = context.resources.getString(R.string.no_account_found)
            "eV2371" -> message = context.resources.getString(R.string.verify_account_and_login)
            "eV2134" -> message = context.resources.getString(R.string.wrong_username_or_password)
            "eV2767" -> message = context.resources.getString(R.string.invalid_refresh_token)
            "eV2291" -> message = context.resources.getString(R.string.country_mandatory)
            "eV2646" -> message = context.resources.getString(R.string.email_mobile_provided)
            "eV2138" -> message = context.resources.getString(R.string.genric_failure)
            "eV3345" -> message = context.resources.getString(R.string.something_went_wrong)
            "eV2006" -> message = context.resources.getString(R.string.country_invalid)
            "eV1075" -> message = context.resources.getString(R.string.cellphone_invalid)
            "eV2373" -> message = context.resources.getString(R.string.otp_cannot_empty)
            "eV2329" -> message = context.resources.getString(R.string.invalid_expire_otp)
            "eV1228" -> message = context.resources.getString(R.string.password_length_atleast)
            "eV2043" -> message = context.resources.getString(R.string.token_not_valid)
            "eV1003" -> message = context.resources.getString(R.string.invalid_sp)
            "eV1907" -> message = context.resources.getString(R.string.invalid_contactid)
            "eV2795" -> message = context.resources.getString(R.string.user_name_exist)
            "eV1051" -> message = context.resources.getString(R.string.invalid_gender)
            "eV1049" -> message = context.resources.getString(R.string.middle_name_exceeds)
            "eV1075" -> message = context.resources.getString(R.string.invalid_mobile)
            "eV2231" -> message = context.resources.getString(R.string.logout)
            "eV2124" -> message = context.resources.getString(R.string.invalid_session)
            "eV2380" -> message = context.resources.getString(R.string.password_change_failed)
            "eV1227" -> message = context.resources.getString(R.string.password_same)
            "eV2515" -> message = context.resources.getString(R.string.password_shorter)
            "eV2516" -> message = context.resources.getString(R.string.passward_given)
            "eV2517" -> message = context.resources.getString(R.string.password_character)
            "eV1733" -> message = context.resources.getString(R.string.device_dont_exist)
            "eV2268" -> message = context.resources.getString(R.string.device_detail_Required)
            "eV4482" -> message = context.resources.getString(R.string.password_policy)
            "eV4492" -> message = context.resources.getString(R.string.login_blocked)
            "eV4502" -> message = context.resources.getString(R.string.password_expired)
            "eV2143" -> message = context.resources.getString(R.string.device_exceeded)
            "ev1226" -> message = context.resources.getString(R.string.invalid_confirm_password)
            "ev1225" -> message = context.resources.getString(R.string.invalid_new_password)
            "eV2847" -> message = context.resources.getString(R.string.max_otp_limit)
            "eV2846" -> message = context.resources.getString(R.string.max_otp_limit)
            else -> message = errorBody?.get(0)?.errorMessage.toString()
        }

        var errorModel = ErrorModel()
        errorModel.errorCode = errorCode
        errorModel.errorMessage = message
        return errorModel
    }
}