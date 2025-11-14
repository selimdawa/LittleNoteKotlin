package com.flatcode.littlenote.Unit

import com.flatcode.littlenote.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

object DATA {
    //Database
    const val PARENT_PATH = "notes"
    const val CHILD_PATH = "myNotes"
    const val ID_PATH = "noteId"

    //Others
    const val TITLE = "title"
    const val CONTENT = "content"
    const val COLOR = "code"
    const val DEFAULT_COLOR = 0
    const val DELAY_LOG = 2000
    const val EDIT = "Edit"
    const val DELETE = "Delete"
    const val ERR_PASS = "Password Do not Match."

    //Shared
    var COLOR_OPTION = "color_option"

    //Other
    val AUTH = FirebaseAuth.getInstance()
    val FIREBASE_USER = AUTH.currentUser
    val FirebaseUserUid = FIREBASE_USER!!.uid
    val FIREBASE_STORE = FirebaseFirestore.getInstance()
    val randomColor: Int
        get() {
            val colorCode: MutableList<Int> = ArrayList()
            colorCode.add(R.color.color1)
            colorCode.add(R.color.color2)
            colorCode.add(R.color.color3)
            colorCode.add(R.color.color4)
            colorCode.add(R.color.color5)
            colorCode.add(R.color.color6)
            colorCode.add(R.color.color7)
            colorCode.add(R.color.color8)
            colorCode.add(R.color.color9)
            colorCode.add(R.color.color10)
            colorCode.add(R.color.color11)
            colorCode.add(R.color.color12)
            colorCode.add(R.color.color13)
            colorCode.add(R.color.color14)
            colorCode.add(R.color.color15)
            colorCode.add(R.color.color16)
            colorCode.add(R.color.color17)
            colorCode.add(R.color.color18)
            colorCode.add(R.color.color19)
            colorCode.add(R.color.color20)
            colorCode.add(R.color.color21)
            colorCode.add(R.color.color22)
            colorCode.add(R.color.color23)
            colorCode.add(R.color.color24)
            colorCode.add(R.color.color25)
            colorCode.add(R.color.color26)
            colorCode.add(R.color.color27)
            colorCode.add(R.color.color28)
            colorCode.add(R.color.color29)
            colorCode.add(R.color.color30)
            colorCode.add(R.color.color31)
            colorCode.add(R.color.color32)
            colorCode.add(R.color.color33)
            colorCode.add(R.color.color34)
            colorCode.add(R.color.color35)
            colorCode.add(R.color.color36)
            colorCode.add(R.color.color37)
            colorCode.add(R.color.color38)
            colorCode.add(R.color.color39)
            colorCode.add(R.color.color40)
            colorCode.add(R.color.color41)
            colorCode.add(R.color.color42)
            colorCode.add(R.color.color43)
            colorCode.add(R.color.color44)
            colorCode.add(R.color.color45)
            colorCode.add(R.color.color46)
            colorCode.add(R.color.color47)
            colorCode.add(R.color.color48)
            colorCode.add(R.color.color49)
            colorCode.add(R.color.color50)
            colorCode.add(R.color.color51)
            colorCode.add(R.color.color52)
            colorCode.add(R.color.color53)
            colorCode.add(R.color.color54)
            colorCode.add(R.color.color55)
            colorCode.add(R.color.color56)
            colorCode.add(R.color.color57)
            colorCode.add(R.color.color58)
            colorCode.add(R.color.color59)
            colorCode.add(R.color.color60)
            colorCode.add(R.color.color61)
            colorCode.add(R.color.color62)
            colorCode.add(R.color.color63)
            colorCode.add(R.color.color64)
            colorCode.add(R.color.color65)
            colorCode.add(R.color.color66)
            colorCode.add(R.color.color67)
            colorCode.add(R.color.color68)
            colorCode.add(R.color.color69)
            colorCode.add(R.color.color70)
            colorCode.add(R.color.color71)
            colorCode.add(R.color.color72)
            colorCode.add(R.color.color73)
            colorCode.add(R.color.color74)
            colorCode.add(R.color.color75)
            colorCode.add(R.color.color76)
            colorCode.add(R.color.color77)
            colorCode.add(R.color.color78)
            colorCode.add(R.color.color79)
            colorCode.add(R.color.color80)
            colorCode.add(R.color.color81)
            colorCode.add(R.color.color82)
            colorCode.add(R.color.color83)
            colorCode.add(R.color.color84)
            colorCode.add(R.color.color85)
            colorCode.add(R.color.color86)
            colorCode.add(R.color.color87)
            colorCode.add(R.color.color88)
            colorCode.add(R.color.color89)
            colorCode.add(R.color.color90)
            colorCode.add(R.color.color91)
            colorCode.add(R.color.color92)
            colorCode.add(R.color.color93)
            colorCode.add(R.color.color94)
            colorCode.add(R.color.color95)
            colorCode.add(R.color.color96)
            colorCode.add(R.color.color97)
            colorCode.add(R.color.color98)
            colorCode.add(R.color.color99)
            colorCode.add(R.color.color100)
            colorCode.add(R.color.color101)
            colorCode.add(R.color.color102)
            colorCode.add(R.color.color103)
            colorCode.add(R.color.color104)
            colorCode.add(R.color.color105)
            colorCode.add(R.color.color106)
            colorCode.add(R.color.color107)
            val randomColor = Random()
            val number = randomColor.nextInt(colorCode.size)
            return colorCode[number]
        }
}