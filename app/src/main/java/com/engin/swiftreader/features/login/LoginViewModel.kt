package com.engin.swiftreader.features.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.engin.swiftreader.common.domain.PersistenceKeyValueRepository
import com.engin.swiftreader.common.util.DataStorePreferences
import com.engin.swiftreader.common.util.Destination
import com.engin.swiftreader.common.util.KeyConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val saveStateHandle: SavedStateHandle,
    @DataStorePreferences private val keyValueRepository: PersistenceKeyValueRepository,
) : ViewModel(){

    private val _destination =  Channel<Destination>()
    val destination = _destination.receiveAsFlow()

    fun login(){
        saveStateHandle[KeyConstants.LOGIN] = true
        runBlocking {
            keyValueRepository.putBoolean(KeyConstants.LOGIN,true)
        }
        _destination.trySend(Destination.Home)
    }

    fun routeToSignUp() {
        _destination.trySend(Destination.SignUp)
    }


    fun signInWithGoogle() {
        _destination.trySend(Destination.SignInWithGoogle)
    }

}