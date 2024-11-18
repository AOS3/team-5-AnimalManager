package com.lion.a066ex_animalmanager.viewmodel

import com.lion.a066ex_animalmanager.util.AnimalFood
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType

data class AnimalViewModel(
    var animalInx:Int,
    var animalType:AnimalType,
    var animalName:String,
    var animalAge:Int,
    var animalGender: AnimalGender,
    var animalFood: String
)