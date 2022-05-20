package com.example.happyplacesapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplacesapp.adapters.HappyPlaceAdapter
import com.example.happyplacesapp.database.DatabaseHandler
import com.example.happyplacesapp.databinding.ActivityMainBinding
import com.example.happyplacesapp.models.HappyPlacesModel
import com.example.happyplacesapp.utils.SwipeToEdit
import com.happyplaces.utils.SwipeToDeleteCallback

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabAddHappyPlace?.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNewPlacesActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_CODE)
        }

        getHappyPlaceListFromLocalDB()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ADD_PLACE_ACTIVITY_CODE){
            if(resultCode == Activity.RESULT_OK){
                getHappyPlaceListFromLocalDB()
            }else{
                Log.e("Activity", "cancelled by user")
            }
        }
    }


    private fun getHappyPlaceListFromLocalDB(){
        val dbHandler = DatabaseHandler(this@MainActivity)
        val getHappyPlaces = dbHandler.getHappyPlacesList()

        if(getHappyPlaces.size > 0){
            binding?.rvHappyPlaces?.visibility = View.VISIBLE
            binding?.tvNoRecordsYet?.visibility = View.GONE
            setupHappyPlacesRecyclerView(getHappyPlaces)
        }else{
            binding?.rvHappyPlaces?.visibility = View.GONE
            binding?.tvNoRecordsYet?.visibility = View.VISIBLE
        }
    }


    private fun setupHappyPlacesRecyclerView(
        happyPlaceList : ArrayList<HappyPlacesModel>){

        binding?.rvHappyPlaces?.layoutManager = LinearLayoutManager(this@MainActivity)

        binding?.rvHappyPlaces?.setHasFixedSize(true)

        val placesAdapter = HappyPlaceAdapter(this@MainActivity, happyPlaceList)
        binding?.rvHappyPlaces?.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : HappyPlaceAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlacesModel) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailsActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEdit(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding?.rvHappyPlaces?.adapter as HappyPlaceAdapter
                adapter.notifyEditItem(this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_CODE)
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding?.rvHappyPlaces)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding?.rvHappyPlaces?.adapter as HappyPlaceAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlaceListFromLocalDB()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding?.rvHappyPlaces)

    }

    companion object{
        var ADD_PLACE_ACTIVITY_CODE = 1
        var EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}