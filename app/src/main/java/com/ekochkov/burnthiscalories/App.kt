package com.ekochkov.burnthiscalories

import android.app.Application
import com.ekochkov.burnthiscalories.di.AppComponent
import com.ekochkov.burnthiscalories.di.DaggerAppComponent
import com.ekochkov.burnthiscalories.di.modules.DataModule
import com.ekochkov.burnthiscalories.di.modules.DomainModule

class App: Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        dagger = DaggerAppComponent.builder()
            .dataModule(DataModule(this))
            .domainModule(DomainModule())
            .build()
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}