package uz.gita.saiga_driver.data.remote.request.order

data class EndOrderRequest(
    val OrderLengthOfWay: Double,
    val orderId: Long,
    val orderMoney: Double,
    val startedTimeOfWay:String,
    val finishedTimeOfWay:String
)