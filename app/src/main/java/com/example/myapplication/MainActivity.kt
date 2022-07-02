package com.example.myapplication

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Half.toFloat
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FieldValue

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.lang.Double.parseDouble
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var buttonAddAmount = findViewById<Button>(R.id.buttonAddAmount)
        var buttonRemoveAmount = findViewById<Button>(R.id.buttonRemoveAmount)
        var amount = findViewById<EditText>(R.id.inputAmount)
        var buttonAddRestaurant = findViewById<Button>(R.id.buttonAddRestaurant)
        var buttonRemoveRestaurant = findViewById<Button>(R.id.buttonRemoveRestaurant)
        var inputRestaurant = findViewById<EditText>(R.id.inputRestaurant)






        buttonAddAmount.setOnClickListener {
            val userAmount = amount.text.toString()
            val isAdd = "True"
            updateFireStoreAmount(userAmount, isAdd)
        }

        buttonRemoveAmount.setOnClickListener {
            val userAmount = amount.text.toString()
            val isAdd = "False"
            updateFireStoreAmount(userAmount, isAdd)
        }

        buttonAddRestaurant.setOnClickListener {
            val restaurant = inputRestaurant.text.toString()
            addRestaurant(restaurant)
        }

        buttonRemoveRestaurant.setOnClickListener {
            val restaurant = inputRestaurant.text.toString()
            removeRestaurant(restaurant)
        }

    }
    fun updateFireStoreAmount(userAmount: String, isAdd: String) {
        val db = FirebaseFirestore.getInstance()
        val database = db.collection("data").document("user_data")

        db.collection("data")
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                    if(it.isSuccessful) {
                        for(document in it.result!!) {
                            println(result.toString())
                            result.append(document.data.getValue("ChickFilA"))
                        }
                        if (isAdd == "True") {
                            database
                                .update(
                                    "ChickFilA",
                                    String.format("%.2f", parseDouble(result.toString()) + userAmount.toFloat())
                                )
                                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                        } else {
                            database
                                .update(
                                    "ChickFilA",
                                    String.format("%.2f", parseDouble(result.toString()) - userAmount.toFloat())
                                )
                                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                        }
                    }
                }
            }

    fun addRestaurant(restaurant: String) {
        val db = FirebaseFirestore.getInstance()
        val database = db.collection("data").document("user_data")

        val hashMap = hashMapOf<String,Any>(
            restaurant to 0
        )

        db.collection("data").document("user_data")
            .set(hashMap, SetOptions.merge())
    }

    fun removeRestaurant(restaurant: String) {
        val db = FirebaseFirestore.getInstance()
        val database = db.collection("data").document("user_data")

        val updates = hashMapOf<String, Any>(
            restaurant to FieldValue.delete()
        )

        database.update(updates)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

    }

}
