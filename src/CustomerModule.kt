object CustomerModule {

    fun startCustomerFlow() {
        println("\n--- CUSTOMER MENU ---")
        println("How would you like to search for a film?")
        println("1. By Title")
        println("2. By Genre")
        println("3. View All Films")
        print("Choose an option: ")

        val option = readLine()

        val results = when (option) {
            "1" -> {
                print("Enter the film title: ")
                val search = readLine() ?: ""
                CinemaSystem.films.filter { it.title.contains(search, ignoreCase = true) }
            }
            "2" -> {
                print("Enter the genre: ")
                val search = readLine() ?: ""
                CinemaSystem.films.filter { it.genre.contains(search, ignoreCase = true) }
            }
            "3" -> CinemaSystem.films
            else -> {
                println("Invalid option.")
                return
            }
        }

        if (results.isEmpty()) {
            println("No films found.")
            return
        }

        println("\nFilms Found:")
        results.forEachIndexed { index, film ->
            println("${index + 1}. ${film.title} (${film.genre}) - Base Price: £${"%.2f".format(film.basePrice)}")
        }

        print("\nSelect the film number to view screenings: ")
        val choice = readLine()?.toIntOrNull()

        if (choice == null || choice !in 1..results.size) {
            println("Invalid film selection.")
            return
        }

        val selectedFilm = results[choice - 1]
        showScreenings(selectedFilm)
    }

    private fun showScreenings(film: Film) {
        val screenings = CinemaSystem.getScreeningsForFilm(film.title)

        if (screenings.isEmpty()) {
            println("No screenings are available for this film.")
            return
        }

        println("\nScreenings for ${film.title}:")
        screenings.forEachIndexed { index, screening ->
            println("${index + 1}. Date: ${screening.date} | Time: ${screening.startTime} | Hall: ${screening.hallNumber}")
        }

        print("Choose a screening: ")
        val choice = readLine()?.toIntOrNull()

        if (choice == null || choice !in 1..screenings.size) {
            println("Invalid screening selection.")
            return
        }

        val selectedScreening = screenings[choice - 1]
        processBooking(selectedScreening)
    }

    private fun processBooking(screening: Screening) {
        println("\n--- SEAT MAP ---")
        screening.seats.forEach { seat ->
            val status = if (seat.isAvailable) "[ ${seat.number} ]" else "[ X ]"
            print("$status ")
        }
        println("\n(X = Booked)")

        print("\nHow many tickets would you like to buy? ")
        val quantity = readLine()?.toIntOrNull()

        if (quantity == null || quantity <= 0) {
            println("Invalid ticket quantity.")
            return
        }

        val selectedSeats = mutableListOf<Int>()

        for (i in 1..quantity) {
            print("Choose the seat number for ticket $i: ")
            val seatNumber = readLine()?.toIntOrNull()

            if (seatNumber == null) {
                println("Invalid seat number.")
                continue
            }

            val seat = screening.seats.find { it.number == seatNumber && it.isAvailable }

            if (seat != null) {
                selectedSeats.add(seatNumber)
                seat.isAvailable = false
            } else {
                println("Seat $seatNumber is unavailable or invalid.")
            }
        }

        if (selectedSeats.isEmpty()) {
            println("No valid seats were selected. Booking cancelled.")
            return
        }

        val total = screening.film.basePrice * selectedSeats.size

        println("\nTotal to pay: £${"%.2f".format(total)}")
        print("Enter virtual payment amount: ")
        val payment = readLine()?.toDoubleOrNull() ?: 0.0

        if (payment >= total) {
            screening.film.totalTicketsSold += selectedSeats.size
            screening.totalTakings += total
            printTicket(screening, selectedSeats, total)
        } else {
            println("Insufficient payment. Booking cancelled.")
            selectedSeats.forEach { seatNumber ->
                screening.seats[seatNumber - 1].isAvailable = true
            }
        }
    }

    private fun printTicket(screening: Screening, seats: List<Int>, price: Double) {
        println("\n*******************************")
        println("CINEMA MARCOS")
        println("Film: ${screening.film.title}")
        println("Date: ${screening.date}")
        println("Time: ${screening.startTime}")
        println("Seats: ${seats.joinToString(", ")}")
        println("Price: £${"%.2f".format(price)}")
        println("*******************************")
    }
}