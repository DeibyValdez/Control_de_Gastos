// Archivo de build del PROYECTO (raiz): solo declara los plugins, el modulo app los usa.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.devtools.ksp") version "2.3.12" apply false
}
