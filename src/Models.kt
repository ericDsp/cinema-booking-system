data class Film(
    val title: String,
    val genre: String,
    var basePrice: Double,
    var totalTicketsSold: Int = 0
)

data class Seat(
    val number: Int,
    var isAvailable: Boolean = true
)

data class Screening(
    val film: Film,
    val hallNumber: Int,
    val date: String,
    val startTime: String,
    val isWeekday: Boolean,
    val seats: List<Seat> = List(10) { index -> Seat(index + 1) },
    var totalTakings: Double = 0.0
)