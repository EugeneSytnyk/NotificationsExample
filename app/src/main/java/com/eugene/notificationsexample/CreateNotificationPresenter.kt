package com.eugene.notificationsexample

class CreateNotificationPresenter {

    private lateinit var createNotificationView : CreateNotificationView
    private lateinit var fragmentNavigator: FragmentNavigator
    private lateinit var notificationManager: NotificationManager

    fun btnPlusClick() {
        fragmentNavigator.openNextFragment()
    }

    fun btnMinusClick() {
        notificationManager.deleteNotificationsFor(createNotificationView.getPageNumber())
        fragmentNavigator.closeCurrentFragment()
    }

    fun btnNewNotificationClick() {
        notificationManager.createNotification(createNotificationView.getPageNumber())
    }

    fun setView(view: CreateNotificationView, fragmentNavigator: FragmentNavigator, notificationManager: NotificationManager) {
        createNotificationView = view
        this.fragmentNavigator = fragmentNavigator
        this.notificationManager = notificationManager
        val pageNumber = createNotificationView.getPageNumber()
        createNotificationView.setPageNumber(pageNumber)
        if (pageNumber == 1) createNotificationView.hideMinusButton()
    }
}