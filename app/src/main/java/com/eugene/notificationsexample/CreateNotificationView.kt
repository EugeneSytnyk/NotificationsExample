package com.eugene.notificationsexample

interface CreateNotificationView {

    fun setPageNumber(number : Int)

    fun getPageNumber() : Int

    fun hideMinusButton()

    fun getFragmentId() : Int
}