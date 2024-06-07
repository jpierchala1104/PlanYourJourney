package com.example.planyourjourney.feature_planing.domain.util

fun Double.millimetersToInches(): Double {
    return this * 0.0393700787
}
fun Double.inchesToMillimeters(): Double {
    return this * 25.4
}
fun Double.celsiusToFahrenheit(): Double{
    return (this * 1.8) + 32
}
fun Double.fahrenheitToCelsius(): Double{
    return (this - 32) / 1.8
}
fun Double.kilometersPerHourToMetersPerSecond(): Double{
    return this * 5 / 18
}
fun Double.kilometersPerHourToMilesPerHour(): Double{
    return this * 0.621371192
}
fun Double.kilometersPerHourToKnots(): Double{
    return this * 0.539957
}
fun Double.metersPerSecondToKilometersPerHour(): Double{
    return this * 18 / 5
}
fun Double.metersPerSecondToMilesPerHour(): Double{
    return this * 2.236936
}
fun Double.metersPerSecondToKnots(): Double{
    return this * 1.943844
}
fun Double.milesPerHourToKilometersPerHour(): Double{
    return this * 1.60934
}
fun Double.milesPerHourToMetersPerSecond(): Double{
    return this * 0.44704
}
fun Double.milesPerHourToKnots(): Double{
    return this * 0.868976
}
fun Double.knotsToKilometersPerHour(): Double{
    return this * 1.852
}
fun Double.knotsToMetersPerSecond(): Double{
    return this * 0.514444
}
fun Double.knotsToMilesPerHour(): Double{
    return this * 1.150779
}
