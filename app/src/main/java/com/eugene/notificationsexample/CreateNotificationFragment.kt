package com.eugene.notificationsexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

var lastFragmentId : Int = 0

class CreateNotificationFragment : Fragment(), CreateNotificationView {

    private lateinit var btnPlus : Button
    private lateinit var btnMinus : Button
    private lateinit var btnCreateNotification : Button
    private lateinit var tvPageNumber : TextView
    private var pageNumberInteger : Int = 1
    private val fragmentId : Int

    init{
        fragmentId = lastFragmentId + 1
        lastFragmentId += 1
    }

    companion object {

        fun newInstance(pageNumber: Int): CreateNotificationFragment {
            val instance = CreateNotificationFragment()
            instance.pageNumberInteger = pageNumber
            return instance
        }
    }

    private val createNotificationPresenter = CreateNotificationPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_create_notification, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnMinus = view.findViewById(R.id.btnMinus)
        btnPlus = view.findViewById(R.id.btnPlus)
        tvPageNumber = view.findViewById(R.id.tvPageNumber)
        btnCreateNotification = view.findViewById(R.id.btnCreateNotification)

        createNotificationPresenter.setView(this, requireActivity() as FragmentNavigator, requireActivity() as NotificationManager)

        btnMinus.setOnClickListener{createNotificationPresenter.btnMinusClick()}
        btnPlus.setOnClickListener{createNotificationPresenter.btnPlusClick()}
        btnCreateNotification.setOnClickListener{createNotificationPresenter.btnNewNotificationClick()}
    }

    override fun setPageNumber(number: Int) {
        tvPageNumber.text = number.toString()
    }

    override fun getPageNumber(): Int = pageNumberInteger

    override fun hideMinusButton() {
        btnMinus.visibility = View.GONE
    }

    override fun getFragmentId(): Int = fragmentId

}



