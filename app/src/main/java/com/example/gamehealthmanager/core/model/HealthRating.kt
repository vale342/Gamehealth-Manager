package com.example.gamehealthmanager.core.model

enum class HealthRating(val description: String) {
    GREEN("Saludable: Experiencia positiva y equilibrada"),
    YELLOW("Precaución: Requiere moderación"),
    RED("Atención: Puede afectar tu bienestar"),

    NONE("Sin diagnóstico")
}