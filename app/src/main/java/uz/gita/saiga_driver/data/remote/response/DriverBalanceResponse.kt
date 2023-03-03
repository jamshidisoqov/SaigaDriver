package uz.gita.saiga_driver.data.remote.response

data class DriverBalanceResponse(
    val balance: Double,
    val balanceOut: Double,
    val benefit: Double
)