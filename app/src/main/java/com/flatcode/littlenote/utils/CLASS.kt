package com.flatcode.littlenote.utils

import com.flatcode.littlenote.activity.AddNote
import com.flatcode.littlenote.activity.EditNote
import com.flatcode.littlenote.activity.Home
import com.flatcode.littlenote.activity.NoteDetails
import com.flatcode.littlenote.activity.Splash
import com.flatcode.littlenote.auth.ForgetPassword
import com.flatcode.littlenote.auth.Login
import com.flatcode.littlenote.auth.Register

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