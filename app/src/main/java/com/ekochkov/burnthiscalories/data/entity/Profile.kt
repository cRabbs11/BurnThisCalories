package com.ekochkov.burnthiscalories.data.entity

import java.io.Serializable

data class Profile (
    val name: String,
    val height: String,
    val weight: String,
    val age: Int,
    val sex: Int
): Serializable {
    companion object {
        val MALE = 0
        val FEMALE = 1
    }
}

fun Profile.toProfileDB() = ProfileDB(
    name = name,
    height = height,
    weight = weight,
    age = age,
    sex = sex)



