package com.eugene.notificationsexample

interface NotificationManager {

    fun createNotification(pageNumber : Int)

    fun deleteNotificationsFor(pageNumber: Int)

}