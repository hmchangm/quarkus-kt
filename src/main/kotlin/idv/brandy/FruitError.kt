package idv.brandy

sealed class FruitError {
    data class DatabaseProblem(val e :Throwable):FruitError()
    data class NoThisFruit(val uuid : String): FruitError()
}