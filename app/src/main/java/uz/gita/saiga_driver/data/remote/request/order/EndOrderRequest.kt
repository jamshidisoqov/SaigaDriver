package uz.gita.saiga_driver.data.remote.request.order

data class EndOrderRequest(

    val OrderLengthOfWay: Int,
    val orderId: Int,
    val orderMoney: Int
)