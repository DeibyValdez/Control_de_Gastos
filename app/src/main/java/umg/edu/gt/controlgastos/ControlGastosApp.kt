package umg.edu.gt.controlgastos

import android.app.Application

class ControlGastosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Dependencias.iniciar(this)
    }
}
