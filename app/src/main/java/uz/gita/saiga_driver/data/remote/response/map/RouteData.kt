package uz.gita.saiga_driver.data.remote.response.map

import com.directions.route.Route


// Created by Jamshid Isoqov an 11/20/2022
data class RouteData(
    val route: ArrayList<Route>?,
    val shortestRouteIndex: Int,
)
