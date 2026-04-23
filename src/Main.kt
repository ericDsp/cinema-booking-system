fun main() {
    CinemaSystem.seedData()

    while (true) {
        println("\n==================================")
        println("         CINEMA SYSTEM            ")
        println("==================================")
        println("1. Customer Area")
        println("2. Administrator Area")
        println("3. Exit")
        print("Choose an option: ")

        when (readLine()) {
            "1" -> CustomerModule.startCustomerFlow()
            "2" -> AdminModule.startAdminFlow()
            "3" -> {
                println("Exiting... Goodbye!")
                break
            }
            else -> println("Invalid option. Please try again.")
        }
    }
}