package com.rithik.task.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.rithik.task.R
import com.rithik.task.adapter.BottomSwipeAdapter
import com.rithik.task.adapter.TopSwipeAdapter
import com.rithik.task.databinding.ActivityDashboardBinding
import com.rithik.task.db.Bottomwear
import com.rithik.task.db.MasterDataBase
import com.rithik.task.db.Topwear
import com.rithik.task.db.Wishlist
import com.rithik.task.extras.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors

open class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding
    private val PERMISSION_REQUEST_CODE = 200
    var flag = 0
    var swipeAdapter: TopSwipeAdapter? = null
    var swipeAdapter2: BottomSwipeAdapter? = null
    var mPhotoFile: File? = null
    private var topwearList: List<Topwear>? = null
    private var bottomwearList: List<Bottomwear>? = null
    private var wishData: Wishlist? = null
    lateinit var database: MasterDataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
//        getInstance(this)
//        database = Room.databaseBuilder(applicationContext, MasterDataBase::class.java, "LocationDB")

        database =
            Room.databaseBuilder(
                applicationContext, MasterDataBase::class.java, "Rithik123"
            )
                .fallbackToDestructiveMigration()
                .build()




        clickAndPageListener()
        displayViewPagers()


    }

    private fun clickAndPageListener() {
        binding.shuffleImg.setOnClickListener { view ->
            Collections.shuffle(topwearList)
            Collections.shuffle(bottomwearList)
            swipeAdapter = TopSwipeAdapter(applicationContext, topwearList!!)
            binding.topPager.adapter = swipeAdapter
            swipeAdapter2 = BottomSwipeAdapter(applicationContext, bottomwearList!!)
            binding.bottomPager.adapter = swipeAdapter2
            getWishData()
        }

        binding.wishImg.setOnClickListener {
            if (topwearList!!.size != 0 && bottomwearList!!.size != 0) {
                val topPos = binding.topPager.currentItem
                val bottomPos = binding.bottomPager.currentItem
                if (wishData == null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        database?.let {
                            database.allDao()!!.insertWishlist(
                                Wishlist(
                                    0,
                                    topwearList!![topPos].id,
                                    bottomwearList!![bottomPos].id
                                )
                            )
                        }
                    }

                    binding.wishImg.setImageDrawable(resources.getDrawable(R.drawable.wish))
                    Toast.makeText(applicationContext, "Item added to wishlist", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    database.allDao().deleteWishlist(wishData)
                    binding.wishImg.setImageDrawable(resources.getDrawable(R.drawable.notwish))
                    Toast.makeText(
                        applicationContext,
                        "Item removed from wishlist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.topImgPager.setOnClickListener { view ->
            ImageDialog()
            flag = 0
        }

        binding.bottomImgPager.setOnClickListener { view ->
            ImageDialog()
            flag = 1
        }



        binding.topPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                getWishData()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.bottomPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                getWishData()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    private fun displayViewPagers() {

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute {
            topwearList = database.allDao().getTopWear() as List<Topwear>?
            bottomwearList = database.allDao().getBottomWear() as List<Bottomwear>?
            runOnUiThread {
                if (topwearList!!.size !== 0) {
                    swipeAdapter = TopSwipeAdapter(applicationContext, topwearList!!)
                    binding.topPager.adapter = swipeAdapter
                    getWishData()
                }


                if (bottomwearList!!.size !== 0) {
                    swipeAdapter2 = BottomSwipeAdapter(applicationContext, bottomwearList!!)
                    binding.bottomPager.adapter = swipeAdapter2
                    getWishData()
                }

            }
        }


    }

    private fun getWishData() {

        if (topwearList!!.size != 0 && bottomwearList!!.size != 0) {
            val topPos: Int = binding.topPager.getCurrentItem()
            val bottomPos: Int = binding.bottomPager.getCurrentItem()

            val executorService = Executors.newSingleThreadExecutor()
            executorService.execute {
                database.allDao().getWishData(
                    topwearList!!.get(topPos).id,
                    bottomwearList!!.get(bottomPos).id
                )

                runOnUiThread {
                    fun onChanged(wishlist: Wishlist) {
                        wishData = wishlist
                        if (wishlist == null) {
                            binding.wishImg.setImageDrawable(resources.getDrawable(R.drawable.notwish))
                        } else {
                            binding.wishImg.setImageDrawable(resources.getDrawable(R.drawable.wish))
                        }
                    }
//                    if (wishData == null) {
//                        binding.wishImg.setImageDrawable(resources.getDrawable(R.drawable.notwish))
//                    } else {
//                        binding.wishImg.setImageDrawable(resources.getDrawable(R.drawable.wish))
//                    }

                }
            }
//            CoroutineScope(Dispatchers.IO).launch {
//                database?.let {
//                    database.allDao().getWishData(
//                    topwearList!!.get(topPos).id,
//                    bottomwearList!!.get(bottomPos).id
//                )
//                }
//            }


//            mainDb!!.getWishData(
//                topwearList!!.get(topPos).id,
//                bottomwearList!!.get(bottomPos).id
//            )
            Log.d("rithik1", "getWishData: " + wishData)

            if (wishData == null) {
                binding.wishImg.setImageDrawable(resources.getDrawable(R.drawable.notwish))
            } else {
                binding.wishImg.setImageDrawable(resources.getDrawable(R.drawable.wish))
            }
//                .observe(this@MainActivity,
//                Observer<Any> { wishlist ->
//                    wishData = wishlist
//                    if (wishlist == null) {
//                        binding.wishImg.setImageDrawable(resources.getDrawable(R.drawable.notwish))
//                    } else {
//                        binding.wishImg.setImageDrawable(resources.getDrawable(R.drawable.wish))
//                    }
//                })
        }
    }

    private fun ImageDialog() {
        if (Utils.checkPermission(this)) {
            val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
            val builder = AlertDialog.Builder(this@DashboardActivity)
            builder.setTitle("Select Option")
            builder.setItems(options) { dialog, item ->
                if (options[item] == "Take Photo") {
                    dialog.dismiss()
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent.resolveActivity(packageManager) != null) {
                        var photoFile: File? = null
                        try {
                            photoFile = Utils.createImageFile(applicationContext)
                        } catch (ex: IOException) {
                            ex.printStackTrace()
                        }
                        if (photoFile != null) {
                            val photoURI = FileProvider.getUriForFile(
                                this@DashboardActivity,
                                "${packageName}.provider",
                                photoFile
                            )
                            mPhotoFile = photoFile
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, 0)
                        }
                    }
                } else if (options[item] == "Choose From Gallery") {
                    dialog.dismiss()
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            }
            builder.show()
            displayViewPagers()
        } else {
            Utils.requestPermission(this, PERMISSION_REQUEST_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK) {
                val selectedImage = Uri.fromFile(mPhotoFile)
                if (flag == 0) {
                    CoroutineScope(Dispatchers.IO).launch {
                        database?.let {
                            database.allDao().insertTopWear(Topwear(0, selectedImage.toString()))
                        }
                    }
//                    database.allDao()!!
//                        .insertTopWear(Topwear(0, selectedImage.toString()))
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        database?.let {
                            database.allDao()!!
                                .insertBottomWear(Bottomwear(0, selectedImage.toString()))
                        }
                    }
//                    database.allDao()!!
//                        .insertBottomWear(Bottomwear(0, selectedImage.toString()))
                }
            }
            1 -> if (resultCode == RESULT_OK) {
                val selectedImage = data!!.data
                if (flag == 0) {
                    CoroutineScope(Dispatchers.IO).launch {
                        database?.let {
                            database.allDao()
                                .insertTopWear(Topwear(0, selectedImage.toString()))
                        }
                    }

//                    database.allDao()!!
//                        .insertTopWear(Topwear(0, selectedImage.toString()))
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        database?.let {
                            database.allDao()
                                .insertBottomWear(Bottomwear(0, selectedImage.toString()))
                        }
                    }
//                    database.allDao()!!
//                        .insertBottomWear(Bottomwear(0, selectedImage.toString()))
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImageDialog()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        Utils.showMessageOKCancel(
                            this, "You need to allow access permissions"
                        ) { dialog, which ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Utils.requestPermission(this, PERMISSION_REQUEST_CODE)
                            }
                        }
                    } else if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        Utils.showMessageOKCancel(
                            this, "You need to allow access permissions"
                        ) { dialog, which ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Utils.requestPermission(this, PERMISSION_REQUEST_CODE)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        clickAndPageListener()
        displayViewPagers()
    }


}

