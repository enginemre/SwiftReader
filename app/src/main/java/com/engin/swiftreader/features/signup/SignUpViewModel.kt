package com.engin.swiftreader.features.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(

) : ViewModel() {

    fun signUpUser(
        email: String,
        password: String,
        name : String
    ) {

    }

}