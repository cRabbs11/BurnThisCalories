package com.ekochkov.burnthiscalories.util

import android.util.Log
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Product
import com.ekochkov.burnthiscalories.data.entity.Profile

class CaloriesCalculatorOld() {

    var isRunning = false
    var stepsInStart = 0

    private lateinit var profile : Profile
    private var calories = 0

    private var stepDistance = 0.0
    private var weight = 0.0
    private var stepsCount = 0
    private var caloriesBurned = 0.0

    fun setProducts(list: List<Product>) {
        calories = setCalories(list)
        println("all calories: $calories")
    }

    fun setProfile(profile: Profile) {
        this.profile = profile
        stepDistance = profile.height.toDouble()*0.41 //считаем длину шага: 41% oт pocтa
        weight = profile.weight.toDouble()

    }

    fun getCaloriesLeft(stepsCount: Int): Int {
        this.stepsCount = stepsCount
        calculateBunredCalories()
        return (calories-caloriesBurned).toInt()
    }

    private fun calculateBunredCalories() {
        //количесвто сожженных калорий нв километр: 0,5 х вec чeлoвeka (kг) х paccтoяниe (km) = coжжeнныe Kkaл
        caloriesBurned=weight*0.5*(stepDistance*stepsCount/10000);
        //Log.d("BMTH", "calories burned = ${caloriesBurned}")
        Log.d("BMTH", "all calories = ${calories}")
    }

    private fun setCalories(list: List<Product>): Int {
        var result = 0
        list.forEach {
            result += it.calory
        }
        return result
    }
}