package uz.gita.saiga_driver.utils

import kotlinx.coroutines.flow.SharedFlow

// Created by Jamshid Isoqov on 12/14/2022
interface BaseViewModel {

    val loadingSharedFlow: SharedFlow<Boolean>

    val messageSharedFlow:SharedFlow<String>

    val errorSharedFlow:SharedFlow<String>

}