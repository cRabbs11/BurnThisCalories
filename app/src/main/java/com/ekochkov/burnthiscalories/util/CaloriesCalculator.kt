package com.ekochkov.burnthiscalories.util
import com.ekochkov.burnthiscalories.data.entity.BurnEvent
import com.ekochkov.burnthiscalories.data.entity.Profile

class CaloriesCalculator(private val profile: Profile, private val burnEvent: BurnEvent) {

    private var isRunning = false
    var stepsInStart = 0

    private var stepDistance = 0.0
    private var weight = 0.0
    private val SM_TO_KM_KOEF = 10000 //коэф для пересчета расстояния из см в км
    private val STEP_LENGTH_KOEF = 0.41
    private var allCalories = 0

    init {
        stepDistance = profile.height.toDouble()*STEP_LENGTH_KOEF //считаем длину шага в см: 41% oт pocтa
        weight = profile.weight.toDouble()

        allCalories = 0
        this.burnEvent.productsId.forEach {
            allCalories+=it.calory
        }
    }

    fun isRunning(): Boolean {
        return isRunning
    }

    fun getAllCalories(): Int {
        return allCalories
    }

    private fun fromStepsToCalories(steps: Int): Int {
        //количесвто сожженных килокалорий нв километр: 0,5 х вec чeлoвeka (kг) х paccтoяниe (km) = coжжeнныe Kkaл

        val caloriesBurned = weight*0.5*(stepDistance*steps)/SM_TO_KM_KOEF
        return caloriesBurned.toInt()
    }

    fun getCaloriesLeft(steps: Int): Int {
        val burnedCalories = fromStepsToCalories(steps-stepsInStart)
        return allCalories - burnedCalories
    }

    fun getCaloriesBurned(steps: Int): Int {
        return fromStepsToCalories(steps-stepsInStart)
    }

    fun setStartedStep(step: Int) {
        isRunning = true
        stepsInStart = step
    }

    interface CaloriesCalculatorBuilder {
        fun setProfile(profile: Profile) : CaloriesCalculatorBuilder
        fun setBurnEvent(burnEvent: BurnEvent) : CaloriesCalculatorBuilder
        fun build() : CaloriesCalculator
    }

    class Builder(): CaloriesCalculatorBuilder {

        private lateinit var profile: Profile
        private lateinit var burnEvent: BurnEvent

        override fun setProfile(profile: Profile): CaloriesCalculatorBuilder {
            this.profile=profile
            return this
        }

        override fun setBurnEvent(burnEvent: BurnEvent): CaloriesCalculatorBuilder {
            this.burnEvent=burnEvent
            return this
        }

        override fun build(): CaloriesCalculator {
            val caloriesCalculator = CaloriesCalculator(profile, burnEvent)
            return caloriesCalculator
        }

    }
}