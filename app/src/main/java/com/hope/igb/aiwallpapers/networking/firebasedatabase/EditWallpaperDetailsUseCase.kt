package com.hope.igb.aiwallpapers.networking.firebasedatabase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditWallpaperDetailsUseCase(private val wallpaperId: String) {

        private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference


        fun editUsages(newValue: Int) {
            reference.child("all_wallpapers")
                .child(wallpaperId)
                .child("usageTimes")
                .setValue(newValue)
        }

}