package com.p2mw.trashcaredashboard.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.p2mw.trashcaredashboard.model.user.User
import com.p2mw.trashcaredashboard.utils.Constants.ADMIN
import com.p2mw.trashcaredashboard.utils.FieldsState
import com.p2mw.trashcaredashboard.utils.UiState
import com.p2mw.trashcaredashboard.utils.Validation
import com.p2mw.trashcaredashboard.utils.validateEmail
import com.p2mw.trashcaredashboard.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>> = _login

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>> = _register

    private val _resetPassword = MutableLiveData<UiState<String>>()
    val resetPassword: LiveData<UiState<String>> = _resetPassword

    private val _userExist = MutableLiveData<Boolean>()
    val userExist: LiveData<Boolean> = _userExist

    private val _validation = Channel<FieldsState>()
    val validation = _validation.receiveAsFlow()

    init {
        userCheck()
    }

    private fun userCheck() {
        _userExist.value = auth.currentUser != null
    }

    fun login(email: String, password: String) {
        if (checkValidation(email, password)) {
            _login.value = UiState.Loading()
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _login.value = UiState.Success("Login success")
                }
                .addOnFailureListener {
                    _login.value = UiState.Error(it.message.toString())
                }
        } else {
            val registerFieldsState = FieldsState(
                validateEmail(email), validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    fun resetPassword(email: String) {
        _resetPassword.value = UiState.Loading()
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                _resetPassword.value =
                    UiState.Success("Link reset password telah dikirim ke email anda")
            }
            .addOnFailureListener {
                _resetPassword.value = UiState.Error(it.message.toString())
            }
    }

    fun register(user: User, password: String) {
        if (checkValidation(user.email, password)) {
            _register.value = UiState.Loading()
            auth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener {
                it.user?.let { firebaseUser ->
                    saveUserInfo(firebaseUser.uid, user)
                }
            }.addOnFailureListener {
                _register.value = UiState.Error(it.message.toString())
            }
        } else {
            val registerFieldsState = FieldsState(
                validateEmail(user.email), validatePassword(password)
            )
            runBlocking { _validation.send(registerFieldsState) }
        }
    }

    private fun saveUserInfo(uid: String, user: User) {
        firestore.collection(ADMIN).document(uid).set(user)
            .addOnSuccessListener {
                _register.value = UiState.Success("User registered successfully")
            }.addOnFailureListener {
                _register.value = UiState.Error(it.message.toString())
            }
    }


    private fun checkValidation(
        email: String, password: String
    ): Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        return emailValidation is Validation.Success && passwordValidation is Validation.Success
    }

}