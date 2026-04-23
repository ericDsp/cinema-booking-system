object AdminModule {

    fun startAdminFlow() {
        while (true) {
            println("\n--- ADMIN PANEL ---")
            println("1. View Films and Statistics")
            println("2. Add New Film")
            println("3. Adjust Prices (Price Factor)")
            println("4. Return to Main Menu")
            print("Choose an option: ")

            when (readLine()) {
                "1" -> viewFilmStats()
                "2" -> addNewFilm()
                "3" -> modifyTicketPricing()
                "4" -> break
                else -> println("Invalid option.")
            }
        }
    }

    private fun viewFilmStats() {
        println("\n--- FILM AND SALES REPORT ---")

        if (CinemaSystem.films.isEmpty()) {
            println("No films available.")
            return
        }

        CinemaSystem.films.forEach { film ->
            println("--------------------------------")
            println("Title: ${film.title} [Genre: ${film.genre}]")
            println("Current Ticket Price: £${"%.2f".format(film.basePrice)}")
            println("Total Tickets Sold: ${film.totalTicketsSold}")

            val screenings = CinemaSystem.getScreeningsForFilm(film.title)
            println("Available Screenings:")

            screenings.forEach { screening ->
                println(
                    "  - Hall ${screening.hallNumber} | ${screening.date} at ${screening.startTime} | " +
                            "Ticket Price: £${"%.2f".format(screening.film.basePrice)} | " +
                            "Takings: £${"%.2f".format(screening.totalTakings)}"
                )
            }
        }
    }

    private fun addNewFilm() {
        println("\n--- ADD NEW FILM ---")
        print("Title: ")
        val title = readLine() ?: ""

        print("Genre: ")
        val genre = readLine() ?: ""

        print("Base Price: ")
        val price = readLine()?.toDoubleOrNull() ?: 0.0

        val newFilm = Film(title, genre, price)
        CinemaSystem.films.add(newFilm)

        println("Now add at least one screening for this film:")
        print("Hall Number: ")
        val hall = readLine()?.toIntOrNull() ?: 1

        print("Date (YYYY-MM-DD): ")
        val date = readLine() ?: "2026-01-01"

        print("Time (HH:MM): ")
        val time = readLine() ?: "20:00"

        print("Is it a weekday? (Y/N): ")
        val isWeekday = (readLine() ?: "").uppercase() == "Y"

        val newScreening = Screening(newFilm, hall, date, time, isWeekday)
        CinemaSystem.screenings.add(newScreening)

        println("Film and screening added successfully.")
    }

    private fun modifyTicketPricing() {
        println("\n--- GLOBAL PRICE ADJUSTMENT ---")
        println("Enter the price factor (Example: 1.1 to increase by 10% or 0.9 to reduce by 10%):")
        val factor = readLine()?.toDoubleOrNull()

        if (factor == null || factor <= 0) {
            println("Invalid factor.")
            return
        }

        CinemaSystem.films.forEach { film ->
            film.basePrice *= factor
        }

        println("Prices updated successfully across all relevant films and screenings.")
    }
}