package com.flatcode.littlenote.Unit

import com.flatcode.littlenote.Activity.AddNote
import com.flatcode.littlenote.Activity.EditNote
import com.flatcode.littlenote.Activity.Home
import com.flatcode.littlenote.Activity.NoteDetails
import com.flatcode.littlenote.Activity.Splash
import com.flatcode.littlenote.Auth.ForgetPassword
import com.flatcode.littlenote.Auth.Login
import com.flatcode.littlenote.Auth.Register

object CLASS {
    val HOME: Class<Home> = Home::class.java
    val SPLASH: Class<Splash> = Splash::class.java
    val REGISTER: Class<Register> = Register::class.java
    val LOGIN: Class<Login> = Login::class.java
    val FORGET_PASSWORD: Class<ForgetPassword> = ForgetPassword::class.java
    val ADD: Class<AddNote> = AddNote::class.java
    val EDIT: Class<EditNote> = EditNote::class.java
    val DETAILS: Class<NoteDetails> = NoteDetails::class.java
}