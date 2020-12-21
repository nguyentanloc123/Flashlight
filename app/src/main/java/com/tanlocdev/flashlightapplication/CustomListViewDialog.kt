package com.tanlocdev.flashlightapplication

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.popup_tutorial.*

class CustomListViewDialog(var activity: Activity, internal var adapter: RecyclerView.Adapter<*>) : Dialog(
    activity
) {
    var dialog: Dialog? = null
    internal var recyclerView: RecyclerView? = null
    // internal var recyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_tutorial)
        val gridLayoutManager = GridLayoutManager(context, 4)
        recycle_view!!.layoutManager = gridLayoutManager // set LayoutManager to RecyclerView
        mLayoutManager = LinearLayoutManager(activity)
        recycle_view?.layoutManager = gridLayoutManager
        recycle_view?.adapter = adapter

        button2.setOnClickListener{
            dismiss()
        }

    }

}
