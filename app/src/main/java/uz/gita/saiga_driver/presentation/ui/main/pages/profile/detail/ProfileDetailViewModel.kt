package uz.gita.saiga_driver.presentation.ui.main.pages.profile.detail

import kotlinx.coroutines.flow.StateFlow
import uz.gita.saiga_driver.utils.BaseViewModel

// Created by Jamshid Isoqov on 12/17/2022
interface ProfileDetailViewModel : BaseViewModel {

    val firstNameFlow: StateFlow<String>

    val lastNameFlow: StateFlow<String>

    val birthdayFlow: StateFlow<String>

    val phoneFlow: StateFlow<String>

    fun saveData(firstName: String, lastName: String, birthDay: String)

    fun getData()
}