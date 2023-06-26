package com.ekochkov.burnthiscalories.util
import com.ekochkov.burnthiscalories.data.entity.Profile

class CaloriesCalculator() {

    private lateinit var profile : Profile

    var isRunning = false
    var stepsInStart = 0

    private var stepDistance = 0.0
    private var weight = 0.0
    private val SM_TO_KM_KOEF = 10000 //коэф для пересчета расстояния из см в км
    private val STEP_LENGTH_KOEF = 0.41

    fun setProfile(profile: Profile) {
        this.profile = profile
        stepDistance = profile.height.toDouble()*STEP_LENGTH_KOEF //считаем длину шага в см: 41% oт pocтa
        weight = profile.weight.toDouble()
    }

    fun getBurnedCalories(steps: Int): Int {
        //количесвто сожженных килокалорий нв километр: 0,5 х вec чeлoвeka (kг) х paccтoяниe (km) = coжжeнныe Kkaл
        val caloriesBurned = weight*0.5*(stepDistance*steps)/SM_TO_KM_KOEF
        return caloriesBurned.toInt()
    }
}