package com.eduardoomarson.quizpdm.ui.utils

import com.eduardoomarson.quizpdm.R

fun getCategoryImageRes(category: String): Int {
    return when (category) {
        "Ciências"   -> R.drawable.cat_ciencias
        "História"   -> R.drawable.cat_historia
        "Esportes"   -> R.drawable.cat_esportes
        "Geral"      -> R.drawable.cat_geral
        "Tecnologia" -> R.drawable.cat_tecnologia
        "Direito"    -> R.drawable.cat_direito
        "Inglês"     -> R.drawable.cat_ingles
        "Japonês"    -> R.drawable.cat_japones
        else         -> R.drawable.cat_geral
    }
}