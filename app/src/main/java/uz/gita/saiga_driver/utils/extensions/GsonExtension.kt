package uz.gita.saiga_driver.utils.extensions

import com.google.gson.Gson
import java.lang.reflect.Type

// Created by Jamshid Isoqov on 2/18/2023


inline fun <reified T> Gson.toJsonData(data: T): String {
    return this.toJson(data)
}

inline fun <reified T> Gson.fromJsonData(jsonString: String, type: Type): T {
    return this.fromJson(jsonString, type)
}

