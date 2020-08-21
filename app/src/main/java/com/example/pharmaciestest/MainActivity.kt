package com.example.pharmaciestest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ClusterListener {

    private lateinit var map: Map
    private lateinit var clustersCollection: ClusterizedPlacemarkCollection
    private val points = mutableMapOf<Point, Boolean>()
    private val mapObjectsPoints = mutableMapOf<PlacemarkMapObject, Point>()
    private lateinit var grayPlaceMark: PharmacyPlaceMark
    private lateinit var bluePlaceMark: PharmacyPlaceMark

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("379478c8-b152-486f-ba05-659b7dd607a9")
        MapKitFactory.initialize(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPlaceMarks()
        setupPoints()
        setupMap()
        setupClusters()
    }

    private fun setupPlaceMarks() {
        grayPlaceMark = PharmacyPlaceMark(this, PharmacyPlaceMark.PlaceMarkColors.GRAY)
        bluePlaceMark = PharmacyPlaceMark(this, PharmacyPlaceMark.PlaceMarkColors.BLUE)
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
        points.keys.forEach {
            val mapObject = clustersCollection.addPlacemark(it, grayPlaceMark)
            mapObjectsPoints[mapObject] = it
            mapObject.addTapListener(tapListener)
        }
        clustersCollection.clusterPlacemarks(60.0, 15)
    }

    private val tapListener = MapObjectTapListener { mapObject, _ ->
        val p = mapObjectsPoints[mapObject]
        p?.let { point ->
            handleTap(mapObject, point)
        }
        return@MapObjectTapListener true
    }

    private fun handleTap(mapObject: MapObject, point: Point) {
        val placeMarkMapObject = mapObject as PlacemarkMapObject
        val isSelectedObject = points[point] == true
        points[point] = !isSelectedObject
        if (isSelectedObject) {
            placeMarkMapObject.setIcon(grayPlaceMark)
        } else {
            placeMarkMapObject.setIcon(bluePlaceMark)
        }
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