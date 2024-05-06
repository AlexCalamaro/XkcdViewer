package com.squidink.xkcdviewer.extensions

// Returns a random value from the array
fun <T> Array<T>.getRandom(): T {
    return this[indices.random()]
}