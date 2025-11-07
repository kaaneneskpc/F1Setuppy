package com.kaaneneskpc.f1setupinstructor.domain.usecase

import com.kaaneneskpc.f1setupinstructor.domain.model.Setup

interface GetSetupDetail {
    suspend operator fun invoke(sourceUrl: String): Setup
}
