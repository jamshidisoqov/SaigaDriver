package uz.gita.saiga_driver.data.remote.request.order

data class OrderRequest(
    val direction: DirectionRequest,
    val amountOfMoney: String,
    val timeWhen:String,
    val comment: String? = null,
)