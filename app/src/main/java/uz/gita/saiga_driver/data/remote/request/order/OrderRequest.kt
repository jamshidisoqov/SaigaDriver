package uz.gita.saiga_driver.data.remote.request.order

data class OrderRequest(
    val directionRequest: DirectionRequest,
    val amountOfMoney: Double? = null,
    val comment: String? = null
)