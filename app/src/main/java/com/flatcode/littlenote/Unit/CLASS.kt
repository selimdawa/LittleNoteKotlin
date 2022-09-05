package com.flatcode.littlenote.Unit

import com.flatcode.littlenote.Activity.*
import com.flatcode.littlenote.Auth.ForgetPassword
import com.flatcode.littlenote.Auth.Login
import com.flatcode.littlenote.Auth.Register

object CLASS {
    var HOME: Class<*> = Home::class.java
    var SPLASH: Class<*> = Splash::class.java
    var REGISTER: Class<*> = Register::class.java
    var LOGIN: Class<*> = Login::class.java
    var FORGET_PASSWORD: Class<*> = ForgetPassword::class.java
    var ADD: Class<*> = AddNote::class.java
    var EDIT: Class<*> = EditNote::class.java
    var DETAILS: Class<*> = NoteDetails::class.java
}