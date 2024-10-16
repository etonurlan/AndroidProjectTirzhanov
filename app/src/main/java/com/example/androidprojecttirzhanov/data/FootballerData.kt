// FootballerData.kt
package com.example.androidprojecttirzhanov.data

data class Footballer(
    val name: String,
    val position: String,
    val goals: Int,
    val team: String,
    val height: Double,
    val weight: Double
)

object FootballerData {
    val footballers = listOf(
        Footballer("Lionel Messi", "Forward", 672, "Inter Miami", 1.70, 72.0),
        Footballer("Cristiano Ronaldo", "Forward", 819, "Al Nassr", 1.87, 83.0),
        Footballer("Kylian Mbappé", "Forward", 229, "PSG", 1.78, 73.0),
        Footballer("Robert Lewandowski", "Forward", 344, "Barcelona", 1.85, 80.0),
        Footballer("Kevin De Bruyne", "Midfielder", 93, "Manchester City", 1.81, 70.0),
        Footballer("Neymar Jr", "Forward", 118, "Al Hilal", 1.75, 68.0)
    )
}
