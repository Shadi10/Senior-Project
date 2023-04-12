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
import androidx.fragment.app.FragmentManager
import com.example.seniorproject.databinding.FragmentDialogLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class DialogLocationFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogLocationBinding
    private var client: FusedLocationProviderClient? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        client = LocationServices.getFusedLocationProviderClient(requireContext())
        binding = FragmentDialogLocationBinding.inflate(inflater, container, false)

        binding.llUseCurrentLocation.setOnClickListener {
            getLocation()
        }

        binding.locationToolbar.setNavigationOnClickListener {
            dismiss()
        }
        return binding.root
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

    @SuppressLint("SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
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

                            val strAdd = getCompleteAddressString(
                                binding.tvLatitude.text.toString().toDouble(),
                                binding.tvLongitude.text.toString().toDouble()
                            )

                            val preferences = this.requireActivity().getSharedPreferences(
                                "UpdatedlocationCoordinates", Context.MODE_PRIVATE
                            )
                            val editor: SharedPreferences.Editor = preferences.edit()

                            editor.putString("address", strAdd)
                            editor.apply()

                            val supportFragmentManager: FragmentManager =
                                requireActivity().supportFragmentManager
                            val dialogFragment = DialogFragment()
                            this.dismiss()
                            dialogFragment.show(supportFragmentManager, "DialogFragment")
                        }
                    }
                }

                client?.lastLocation?.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> = geocoder.getFromLocation(
                            location.latitude, location.longitude, 1
                        ) as List<Address>
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

    private fun isLocationEnabled(): Boolean {
        val mgr = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return mgr.isProviderEnabled(LocationManager.GPS_PROVIDER) || mgr.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), 100
        )
    }
}