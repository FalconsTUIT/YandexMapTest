package com.example.pharmaciestest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ClusterListener {

    private lateinit var map: Map
    private lateinit var clustersCollection: ClusterizedPlacemarkCollection
    private val points = mutableMapOf<Point, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("379478c8-b152-486f-ba05-659b7dd607a9")
        MapKitFactory.initialize(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPoints()
        setupMap()
        setupClusters()
    }

    private fun setupPoints() {
        PointsGenerator.getPoints().forEach {
            points[it] = false
        }
    }

    private fun setupMap() {
        map = map_view.map
        map.move(
            CameraPosition(
                points.keys.first(),
                15f,
                0f,
                0f
            )
        )
    }

    private fun setupClusters() {
        clustersCollection = map.mapObjects.addClusterizedPlacemarkCollection(this)
        clustersCollection.addPlacemarks(points.keys.toList(), ImageProvider.fromResource(this, R.drawable.ic_pin_filter_gray), IconStyle())
        /*val temp: List<PlacemarkMapObject> = clustersCollection.addEmptyPlacemarks(points.keys.toList())
        temp.forEach { placeMark ->
            placeMark.setIcon(ImageProvider.fromResource(this, R.drawable.ic_pin_filter_blue)) {
                if (points.contains(placeMark.geometry)) {
                    if (points[placeMark.geometry] == true) {
                        placeMark.setIcon(ImageProvider.fromResource(this, R.drawable.ic_pin_filter_gray))
                    } else {
                        placeMark.setIcon(ImageProvider.fromResource(this, R.drawable.ic_pin_filter_blue))
                    }
                    points[placeMark.geometry] = points[placeMark.geometry]?.not() ?: false
                }
            }
        }*/
        clustersCollection.clusterPlacemarks(60.0, 15)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onClusterAdded(cluster: Cluster) {
        cluster.appearance.setIcon(ClusterImage(cluster.size.toString(), this))
    }
}