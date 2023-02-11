package uz.gita.saiga_driver.utils.extensions

// Created by Jamshid Isoqov on 12/17/2022
fun Double.getFinanceType(): String {
    return "$this".combine("sum")
}

fun Double.getFormat(count: Int): String {
    val formatterString = this.toString()
    val index = formatterString.indexOf('.')
    if (index != -1) {
        val dis = formatterString.lastIndex - index
        if (dis > count) {
            return formatterString.substring(0, index + count + 1)
        }
    }
    return formatterString
}