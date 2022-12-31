package com.haniifac.capstonepesaing_revisited.domain.entity

import com.google.android.gms.maps.model.LatLng

data class TokoFireStore(
    var id: String,
    var nama: String,
    var lokasi : LatLng
)