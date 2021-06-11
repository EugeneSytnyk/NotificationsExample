package com.eugene.notificationsexample

private var nextId = 0

class Notification(val pageNumber : Int) {
    val id : Int
    init{
        id = nextId
        nextId++
    }
}