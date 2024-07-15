package com.sportzinteractive.network.helper

interface EntityMapper<E, D> {
    fun toDomain(entity: E): D
}