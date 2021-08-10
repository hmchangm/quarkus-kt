package idv.brandy

sealed class FruitError {
    object DatabaseProblem: FruitError()
    data class NoThisFruit(val uuid : String): FruitError()
}

typealias DatabaseProblem = FruitError.DatabaseProblem
typealias NoThisFruit = FruitError.NoThisFruit
