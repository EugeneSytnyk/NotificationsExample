package com.eugene.notificationsexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.eugene.notificationsexample.NotificationManager as MyNotificationManager

const val CHANNEL_ID = "Android test app"
const val ACTION_OPEN_FRAGMENT = "action_open_fragment"
const val INTENT_EXTRA = "PAGE_NUMBER"

class HomeActivity : AppCompatActivity(), FragmentNavigator, MyNotificationManager {

    private lateinit var viewPager : ViewPager
    private lateinit var pagerAdapter : ScreenSlidePagerAdapter
    private lateinit var mBroadcastReceiver : MyBroadcastReceiver
    private val activeNotifications = ArrayList<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        createNotificationChannel()

        viewPager = findViewById(R.id.viewPager)
        pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        pagerAdapter.addItem()
        viewPager.adapter = pagerAdapter

        mBroadcastReceiver = MyBroadcastReceiver{viewPager.setCurrentItem(it, true)}
        val filter = IntentFilter(ACTION_OPEN_FRAGMENT).apply {
            addAction(ACTION_OPEN_FRAGMENT)
        }
        registerReceiver(mBroadcastReceiver, filter)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun openNextFragment() {
        pagerAdapter.addItem()
        viewPager.setCurrentItem(pagerAdapter.count - 1, true)
    }

    override fun closeCurrentFragment() {
        val position = viewPager.currentItem
        pagerAdapter.deleteItem(position)
        viewPager.setCurrentItem(position - 1, true)
    }

    override fun createNotification(pageNumber: Int) {
        val notification = Notification(pageNumber)
        activeNotifications.add(notification)

        val intent = Intent(ACTION_OPEN_FRAGMENT)
        intent.putExtra(INTENT_EXTRA, pageNumber)

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.white_circle_button)
                .setContentTitle("Notification")
                .setContentText(pageNumber.toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notification.id, builder.build())
        }

    }

    override fun deleteNotificationsFor(pageNumber: Int) {
        activeNotifications.filter{it.pageNumber == pageNumber}.forEach { NotificationManagerCompat.from(this).cancel(it.id) }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private val pages : ArrayList<CreateNotificationFragment> = ArrayList()

        override fun getCount(): Int = pages.size

        override fun getItem(position: Int): Fragment = pages[position]

        fun addItem(){
            pages.add(CreateNotificationFragment.newInstance((findMaxPageNumber() ?: 0) + 1))
            notifyDataSetChanged()
        }

        fun deleteItem(position : Int) {
            pages.removeAt(position)
            notifyDataSetChanged()
        }

        override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

        private fun findMaxPageNumber() : Int? = pages.maxByOrNull(CreateNotificationFragment::getPageNumber)?.getPageNumber()
    }

    private class MyBroadcastReceiver(val openFragment : ((Int) -> Unit)? = null) : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val action : String? = intent?.action
            action?.let{
                if(it == ACTION_OPEN_FRAGMENT){
                    val pageNumber = intent.getIntExtra(INTENT_EXTRA, 1)
                    openFragment?.invoke(pageNumber)
                }
            }
        }

    }
}