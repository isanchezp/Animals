package com.ivan.animals.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.ivan.animals.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //navController = Navigation.findNavController(this, R.id.fragmentContainerView)
        //NavigationUI.setupActionBarWithNavController(this, navController)
    }

   /* override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }*/


}