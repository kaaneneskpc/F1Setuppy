package com.kaaneneskpc.f1setupinstructor.domain.usecase

import com.kaaneneskpc.f1setupinstructor.domain.model.Setup

interface SaveFavorite {
    suspend operator fun invoke(setup: Setup)
}
