package uz.gita.saiga_driver.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import uz.gita.saiga_driver.utils.SharedPreference
import javax.inject.Inject

// Created by Jamshid Isoqov on 12/13/2022
class MySharedPref @Inject constructor(
    private val ctx: Context,
    private val sharedPreferences: SharedPreferences
) : SharedPreference(ctx, sharedPreferences) {

    var token:String by Strings("")

    var firstName:String by Strings("")

    var lastName:String by Strings("")



}