# 💰 Control de Gastos e Ingresos

Aplicación móvil para Android, escrita en **Kotlin**, que permite registrar y consultar los **gastos** y **ingresos** personales. Los datos se guardan en el teléfono con **Room (SQLite)**, por lo que no se pierden al cerrar la app. 

---


## ✨ Funcionalidades

- Registrar **gastos** e **ingresos** con descripción, monto, categoría y fecha.
- **Resumen** en la parte superior: balance total, total de ingresos y total de gastos.
- Lista de movimientos en tarjetas, del más reciente al más antiguo.
  - Ingresos en **verde** con signo `+`, gastos en **rojo suave** con signo `-`.
- **Editar** un movimiento tocando su tarjeta.
- **Eliminar** un movimiento, con mensaje de confirmación.
- Categorías distintas según el tipo:
  - **Gasto:** Alimentación, Transporte, Servicios, Salud, Educación, Entretenimiento, Otros.
  - **Ingreso:** Salario, Ventas, Regalo, Otros.
- Selector de fecha con calendario (por defecto, la fecha de hoy).
- **Validaciones:** no permite guardar sin descripción ni con un monto menor o igual a 0.
- Montos en **quetzales** con formato `Q 1,234.50`.
- **Persistencia:** los datos permanecen después de cerrar la app.
- Tema **oscuro** fijo.

---

## 🗂️ Estructura del proyecto

```
ControlGastos/
├── app/src/main/
│   ├── java/umg/edu/gt/controlgastos/
│   │   ├── datos/
│   │   │   ├── Movimiento.kt         # Entidad: una fila de la tabla "movimientos"
│   │   │   ├── MovimientoDao.kt      # Operaciones: insertar, listar, buscar, actualizar, eliminar
│   │   │   └── AppDatabase.kt        # Base de datos Room
│   │   ├── ControlGastosApp.kt       # Application: abre la base al iniciar la app
│   │   ├── Dependencias.kt           # Acceso compartido al DAO
│   │   ├── MainActivity.kt           # Pantalla principal: resumen + lista
│   │   ├── FormularioActivity.kt     # Pantalla para crear y editar
│   │   ├── MovimientoAdapter.kt      # Adapter del RecyclerView
│   │   └── Utilidades.kt             # Formato de montos y fechas
│   ├── res/
│   │   ├── layout/                   # activity_main, activity_formulario, item_movimiento, item_spinner
│   │   └── values/                   # colors.xml, themes.xml (tema oscuro), strings.xml
│   └── AndroidManifest.xml
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🧠 Cómo funciona

1. Al iniciar, `ControlGastosApp` abre la base de datos `gastos.db` (la crea la primera vez).
2. Cada ingreso o gasto es un objeto `Movimiento`, con el campo `tipo` (`GASTO` o `INGRESO`).
3. `MainActivity` pide la lista al DAO cada vez que se muestra, calcula los totales y actualiza la pantalla.
4. `FormularioActivity` sirve para crear (`id = 0`) y para editar (`id` del movimiento tocado). Valida los datos y los guarda con el DAO.
5. Las operaciones de base de datos son `suspend` y se lanzan con `lifecycleScope`, así no bloquean la pantalla.

La fecha se guarda como texto `yyyy-MM-dd` para que el orden por fecha funcione, y se muestra como `dd/MM/yyyy`.

---

## 🚀 Cómo ejecutarlo

### Requisitos

- **Android Studio** compatible con AGP 9.1.1 (Otter 3 Feature Drop o más reciente).
- JDK 17 o superior (incluido en Android Studio).
- Conexión a internet en el primer Sync (descarga Gradle y las librerías).
- Un emulador o un celular Android 8.0 o superior.
