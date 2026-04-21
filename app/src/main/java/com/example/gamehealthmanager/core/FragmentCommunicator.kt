package com.example.gamehealthmanager.core

import com.example.gamehealthmanager.databinding.ActivityMainBinding

//Definir el contrato para comunicarse entre fragmentos
interface FragmentCommunicator {
    fun manageLoader (isVisible: Boolean)
}
//Quien lo implementará será el activityMain, porque ahi está el loader