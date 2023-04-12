package com.example.seniorproject.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.seniorproject.R
import com.example.seniorproject.databinding.FragmentLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


class LocationFragment : Fragment() {
    private lateinit var binding: FragmentLocationBinding
    private var client: FusedLocationProviderClient? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        binding.locationToolbar.setNavigationOnClickListener {
            val detailsForSellingFragment = DetailsForSellingFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, detailsForSellingFragment).commit()
            fragmentTransaction.addToBackStack(null)
        }
        client = LocationServices.getFusedLocationProviderClient(requireContext())
        binding.llUseCurrentLocation.setOnClickListener {
            getLocation()
        }

        return binding.root
    }

    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String {
        var strAdd = ""
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w("My Current location", strReturnedAddress.toString())
            } else {
                Log.w("My Current location ", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("My Current location", "Canont get Address!")
        }
        return strAdd
    }


    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions()
                    return
                }
                client?.lastLocation?.addOnCompleteListener(requireActivity()) { task ->
                    run {
                        val location: Location? = task.result
                        if (location == null) {
                            Toast.makeText(requireContext(), "Null received", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(requireContext(), "Get success", Toast.LENGTH_LONG)
                                .show()
                            binding.tvLatitude.text = location.latitude.toString()
                            binding.tvLongitude.text = location.longitude.toString()

                            val strAdd = getCompleteAddressString(binding.tvLatitude.text.toString()
                                .toDouble(),
                                binding.tvLongitude.text.toString().toDouble())

                            val preferences = this.requireActivity()
                                .getSharedPreferences("locationCoordinates", Context.MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = preferences.edit()

                            editor.putString("address", strAdd)
                            editor.apply()

                            val detailsForSellingFragment = DetailsForSellingFragment()
                            val fragmentManager: FragmentManager =
                                requireActivity().supportFragmentManager
                            val fragmentTransaction: FragmentTransaction =
                                fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.frameLayout, detailsForSellingFragment)
                                .commit()
                            fragmentTransaction.addToBackStack(null)
                        }
                    }
                }

                client?.lastLocation?.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude,
                                location.longitude,
                                1) as List<Address>
                        binding.tvLatitude.text = "Latitude\n${list[0].latitude}"
                        binding.tvLongitude.text = "Longitude\n${list[0].longitude}"

                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (requestCode == 100) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show()
                getLocation()
            }
        } else {
            Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val mgr = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return mgr.isProviderEnabled(LocationManager.GPS_PROVIDER) || mgr.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            100
        )
    }

}