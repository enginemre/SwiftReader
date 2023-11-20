package com.engin.swiftreader.common.util

sealed interface Destination {
    data object Login : Destination
    data object SignUp : Destination
    data object Home :Destination
    data object SignInWithGoogle : Destination
}