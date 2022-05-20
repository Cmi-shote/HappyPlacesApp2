package com.example.happyplacesapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplacesapp.databinding.ActivityHappyPlaceDetailsBinding
import com.example.happyplacesapp.models.HappyPlacesModel

class HappyPlaceDetailsActivity : AppCompatActivity() {

    private var binding : ActivityHappyPlaceDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHappyPlaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var happyPlaceDetailModel : HappyPlacesModel? = null

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlacesModel
        }

        if(happyPlaceDetailModel != null){
            setSupportActionBar(binding?.toolbarHappyPlaceDetail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailModel.title
        }

        binding?.toolbarHappyPlaceDetail?.setNavigationOnClickListener{
            onBackPressed()
        }

        binding?.ivPlaceImage?.setImageURI(Uri.parse(happyPlaceDetailModel?.image))
        binding?.tvDescription?.text = happyPlaceDetailModel?.description
        binding?.tvLocation?.text = happyPlaceDetailModel?.location

        binding?.btnViewOnMap?.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, happyPlaceDetailModel)
            startActivity(intent)
        }
    }
}