package com.flatcode.littlenote.utils

import com.flatcode.littlenote.ui.auth.ForgetPasswordActivity
import com.flatcode.littlenote.ui.auth.LoginActivity
import com.flatcode.littlenote.ui.auth.RegisterActivity
import com.flatcode.littlenote.ui.home.HomeActivity
import com.flatcode.littlenote.ui.note.AddNoteActivity
import com.flatcode.littlenote.ui.note.EditNoteActivity
import com.flatcode.littlenote.ui.note.NoteDetailsActivity
import com.flatcode.littlenote.ui.splash.SplashActivity

object CLASS {
    val HOME: Class<HomeActivity> = HomeActivity::class.java
    val SPLASH: Class<SplashActivity> = SplashActivity::class.java
    val REGISTER: Class<RegisterActivity> = RegisterActivity::class.java
    val LOGIN: Class<LoginActivity> = LoginActivity::class.java
    val FORGET_PASSWORD: Class<ForgetPasswordActivity> = ForgetPasswordActivity::class.java
    val ADD: Class<AddNoteActivity> = AddNoteActivity::class.java
    val EDIT: Class<EditNoteActivity> = EditNoteActivity::class.java
    val DETAILS: Class<NoteDetailsActivity> = NoteDetailsActivity::class.java
}