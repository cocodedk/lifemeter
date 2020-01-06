package com.example.first.data

import com.example.first.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            if(username.equals("babak") && password.equals("Password")) {
                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Babak")
                return Result.Success(fakeUser)
            }

            return Result.Error(IOException("Error logging in", Throwable("Wrong username or password")));
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

