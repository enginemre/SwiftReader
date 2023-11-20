package com.engin.swiftreader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engin.swiftreader.common.domain.PersistenceKeyValueRepository
import com.engin.swiftreader.common.util.DataStorePreferences
import com.engin.swiftreader.common.util.Destination
import com.engin.swiftreader.common.util.KeyConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    @DataStorePreferences private val keyValueRepository: PersistenceKeyValueRepository,
) : ViewModel() {

    private val _signInWithGoogle = MutableSharedFlow<Boolean>(replay = 1)
    val signInWithGoogle = _signInWithGoogle.asSharedFlow()

    private val _signUpWithGoogle = MutableSharedFlow<Unit>(replay = 1)
    val signUpWithGoogle = _signUpWithGoogle.asSharedFlow()

    private val _destination = MutableSharedFlow<Destination>(replay = 1)
    val destination = _destination.asSharedFlow()

    fun isUserLoggedIn() =
        runBlocking { keyValueRepository.getBoolean(KeyConstants.LOGIN) ?: false }

    fun isUserGoogleLoggedIn() {
        viewModelScope.launch {
            _signInWithGoogle.emit(true)
        }
    }

    fun loginUser(email: String? = null, value: Boolean) = runBlocking {
        // TODO Get user Id FROM BE with given email
        keyValueRepository.putBoolean(KeyConstants.LOGIN, value)
        if (value)
            _destination.tryEmit(Destination.Home)
    }

    fun signUpWithGoogle() {
        _signUpWithGoogle.tryEmit(Unit)
    }

    fun signUpUser(email: String?, name: String?) {
        if (email != null && name != null) {
            // TODO sign up user with random password to BE
        } else {
            // TODO show error
        }
    }

    fun singOutWithGoogle(){
        viewModelScope.launch {
            _signInWithGoogle.emit(false)
        }
    }

    fun signOut() = runBlocking {
        // TODO Get user Id FROM BE with given email
        keyValueRepository.putBoolean(KeyConstants.LOGIN, false)
        _destination.tryEmit(Destination.Login)
    }
}