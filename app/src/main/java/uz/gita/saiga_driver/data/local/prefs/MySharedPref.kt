package uz.gita.saiga_driver.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.SharedPreference
import uz.gita.saiga_driver.utils.extensions.getCurrentDate
import javax.inject.Inject

// Created by Jamshid Isoqov on 12/13/2022
class MySharedPref @Inject constructor(
    @ApplicationContext ctx: Context, sharedPreferences: SharedPreferences
) : SharedPreference(ctx, sharedPreferences) {

    var token: String by Strings("")

    var firstName: String by Strings("")

    var lastName: String by Strings("")

    var birthDay: String by Strings(getCurrentDate())

    var phoneNumber: String by Strings("")

    var language: Int by Ints(2)

    var isVerify: Boolean by Booleans(false)

    var userId: Long by Longs()

    var mapType: Int by Ints(0)

    var introFinished: Boolean by Booleans(false)

    var lat: String by Strings(NUKUS.latitude.toString())

    var lon: String by Strings(NUKUS.longitude.toString())

    fun getCurrentLatLng(): LatLng = LatLng(lat.toDouble(), lon.toDouble())

    var minPrice: String by Strings("8000")

    var baseUrl: String by Strings("http://77.232.136.6:5001/")

    var socketBaseUrl by Strings("ws://77.232.136.6:5001/ws")

    var appIsIntro: Boolean by Booleans(true)

    var minWayCalculation: String by Strings("2.0")

    var pricePerKm: String by Strings("1000.0")

    var waitingFirstTimePrice: String by Strings("2000.0")

    var waitingPricePerMin: String by Strings("500.0")

    var waitingTimeFirst: Long by Longs(120000L)

}