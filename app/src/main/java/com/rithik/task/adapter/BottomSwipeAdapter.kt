package com.rithik.task.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.rithik.task.R
import com.rithik.task.db.Bottomwear

class BottomSwipeAdapter(private val mContext: Context, bottomwears: List<Bottomwear>) :
    PagerAdapter() {
    private var bottomwears: List<Bottomwear> = ArrayList<Bottomwear>()
    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val view: View = inflater.inflate(R.layout.images_layout, collection, false)
        val imageView = view.findViewById<ImageView>(R.id.imgData)
        Glide.with(mContext)
            .load(bottomwears[position].path)
            .into(imageView)
        collection.addView(view)
        return view
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return bottomwears.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    init {
        this.bottomwears = bottomwears
    }
}