package com.trios2024evdj.contactmanager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.trios2024evdj.contactmanager.databinding.ActivityMapsBinding
import com.trios2024evdj.contactmanager.models.ContactList

import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.trios2024evdj.contactmanager.adapter.BookmarkInfoWindowAdapter


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var list: ContactList
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!

        title = list.contact

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupLocationClient()
        setupPlacesClient()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setInfoWindowAdapter(BookmarkInfoWindowAdapter(this))

        getContactLocation()
        mMap.setOnPoiClickListener {
            displayPoi(it)
        }

    }

    private fun displayPoi(pointOfInterest: PointOfInterest) {
        displayPoiGetPlaceStep(pointOfInterest)

    }

    private fun displayPoiGetPlaceStep(pointOfInterest: PointOfInterest) {
        // 1
        val placeId = pointOfInterest.placeId

        // 2
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.PHONE_NUMBER,
            Place.Field.PHOTO_METADATAS,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG)

        // 3
        val request = FetchPlaceRequest
            .builder(placeId, placeFields)
            .build()

        // 4
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                // 5
                val place = response.place
                displayPoiGetPhotoStep(place)
//                Toast.makeText(this,
//                    "${place.name}, " +
//                            "${place.phoneNumber}",
//                    Toast.LENGTH_LONG).show()
            }.addOnFailureListener { exception ->
                // 6
                if (exception is ApiException) {
                    val statusCode = exception.statusCode
                    Log.e(TAG,
                        "Place not found: " +
                                exception.message + ", " +
                                "statusCode: " + statusCode)
                }
            }
    }

    private fun displayPoiGetPhotoStep(place: Place) {
        // 1
        val photoMetadata = place
            .getPhotoMetadatas()?.get(0)
        // 2
        if (photoMetadata == null) {
            // Next step here
            displayPoiDisplayStep(place, null)
            return
        }
        // 3
        val photoRequest = FetchPhotoRequest
            .builder(photoMetadata)
            .setMaxWidth(resources.getDimensionPixelSize(
                R.dimen.default_image_width))
            .setMaxHeight(resources.getDimensionPixelSize(
                R.dimen.default_image_height))
            .build()
        // 4
        placesClient.fetchPhoto(photoRequest)
            .addOnSuccessListener { fetchPhotoResponse ->
                val bitmap = fetchPhotoResponse.bitmap
                // Next step here
                displayPoiDisplayStep(place, bitmap)
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    val statusCode = exception.statusCode
                    Log.e(TAG,
                        "Place not found: " +
                                exception.message + ", " +
                                "statusCode: " + statusCode)
                }
            }
    }

    private fun displayPoiDisplayStep(place: Place, photo: Bitmap?) {
//        val iconPhoto = if (photo == null) {
//            BitmapDescriptorFactory.defaultMarker()
//        } else {
//            BitmapDescriptorFactory.fromBitmap(photo)
//        }

        val marker = mMap.addMarker(MarkerOptions()
            .position(place.latLng as LatLng)
            //.icon(iconPhoto)
            .title(place.name)
            .snippet(place.phoneNumber)
        )
        marker?.tag = photo
    }


    private fun setupPlacesClient() {
        Places.initialize(applicationContext,
            getString(R.string.google_maps_key))
        placesClient = Places.createClient(this)
    }

    private fun setupLocationClient() {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_LOCATION
        )
    }

    companion object {
        private const val REQUEST_LOCATION = 1
        private const val TAG = "MapsActivity"
    }

    private fun getCurrentLocation() {
        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                   PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
                    PackageManager.PERMISSION_GRANTED)
        ) {
            requestLocationPermissions()
        } else {
            fusedLocationClient.lastLocation.addOnCompleteListener {
                val location = it.result
                if (location != null)
                {
                    val latLng = LatLng(location.latitude, location.longitude)

                    mMap.addMarker(MarkerOptions().position(latLng).title("You are here!"))

                    val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)

                    mMap.moveCamera(update)
                }
                else
                {
                    Log.e(TAG, "No location found")
                }
            }
        }

    }

    private fun getContactLocation() {
        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
                    PackageManager.PERMISSION_GRANTED)
        ) {
            requestLocationPermissions()
        } else {

                val location = getLatLngFromAddress(this, list.address)
                if (location != null)
                {
                    val latLng = LatLng(location.latitude, location.longitude)

                    mMap.addMarker(MarkerOptions().position(latLng).title("${list.contact} is here"))

                    val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)

                    mMap.moveCamera(update)
                }
                else
                {
                    Log.e(TAG, "No location found")
                }
            }
        }



    fun getLatLngFromAddress(context: Context, mAddress: String): Address {
        val coder = Geocoder(context)
        lateinit var address: List<Address>

        address = coder.getFromLocationName(mAddress, 5)!!

        val location = address[0]
        return location

    }
}