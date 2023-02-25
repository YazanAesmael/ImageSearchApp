package com.example.idea.components

import com.example.idea.network.Hit

data class MainState(
    val isLoading:Boolean=false,
    val data:List<Hit> = emptyList(),
    val error:String="",
)