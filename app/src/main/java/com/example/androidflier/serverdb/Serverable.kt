package com.example.androidflier.serverdb

interface Serverable<T> {
    fun getAll(): T
    }