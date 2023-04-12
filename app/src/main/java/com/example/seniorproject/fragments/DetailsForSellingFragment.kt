package com.example.seniorproject.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.seniorproject.MainActivity
import com.example.seniorproject.ProductViewModel
import com.example.seniorproject.R
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.databinding.FragmentDetailsForSellingBinding
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_details_for_selling.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DetailsForSellingFragment : Fragment() {

    private lateinit var binding: FragmentDetailsForSellingBinding
    private var images: ArrayList<Uri?>? = null
    private var position = 0
    private val PICK_IMAGES_CODE = 0
    private val viewModel by activityViewModels<ProductViewModel>()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailsForSellingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        binding.detailsToolBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)

        binding.detailsToolBar.setNavigationOnClickListener {
            val productFragment = ProductFragment()
            switchBetweenFragments(productFragment)
        }
        images = ArrayList()
        binding.imageSwitcher.setFactory { ImageView(activity?.applicationContext) }

        binding.cardView.setOnClickListener {
            images = arrayListOf()
            pickImagesIntent()

        }
        binding.nextText.setOnClickListener {
            switchTonextText()
        }
        binding.previousImage.setOnClickListener {
            switchToPreviousImage()
        }
        binding.chooseLocationLl.setOnClickListener {
            val locationFragment = LocationFragment()
            switchBetweenFragments(locationFragment)
        }

        val sharedPreferencesLocation: SharedPreferences? = this.activity?.getSharedPreferences(
            "locationCoordinates", AppCompatActivity.MODE_PRIVATE
        )

        val address = sharedPreferencesLocation?.getString("address", "Choose Location")
        if (address !== null) binding.tvChosenLocation.text = address

        val sharedPreferences = requireActivity().getSharedPreferences(
            "categoryNameAndImage", AppCompatActivity.MODE_PRIVATE
        )
        val sharedPreferencesCarBrand =
            this.activity?.getSharedPreferences("CategoryAndBrand", AppCompatActivity.MODE_PRIVATE)

        val categoryImage = sharedPreferences.getInt("categoryImage", 0)
        val productBrand = sharedPreferencesCarBrand?.getString("productBrand", "null")
        val categoryName = sharedPreferencesCarBrand?.getString("productCategory", "null")
        binding.tvBrand.text = productBrand
        binding.tvCategoryName.text = categoryName
        binding.categoryImage.setImageResource(categoryImage)

        binding.publishProductBtn.setOnClickListener {
            if (validateInputs()) {
                createPost()
            }
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun createPost() {

        val calendar = Calendar.getInstance();
        val dateFormat: DateFormat = SimpleDateFormat("EEE, MMM d ,h:mm a ")
        val date = dateFormat.format(calendar.time)
        val productDescription = binding.etProductDescription.text.toString()
        val productPrice = binding.productPrice.text.toString().toDouble()
        val productTitle = binding.adTitleEt.text.toString()
        val preferences =
            this.requireActivity().getSharedPreferences("saveUserInput", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putFloat("productPrice", productPrice.toFloat())
        editor.putString("productTitle", productTitle)
        editor.putString("productDescription", productDescription)
        editor.apply()
        val sharedPreferences: SharedPreferences? =
            this.activity?.getSharedPreferences("usernameAndId", AppCompatActivity.MODE_PRIVATE)
        val sharedPref =
            this.activity?.getSharedPreferences("CategoryAndBrand", AppCompatActivity.MODE_PRIVATE)
        val categoryName = sharedPref?.getString("productCategory", "null")
        val id = sharedPreferences?.getInt("userId", 0)
        val userName = sharedPreferences?.getString("userName", "username")

        val product = userName?.let {
            id?.let { id ->
                images?.get(position)?.let { it1 ->
                    Product(
                        0,
                        it,
                        it1,
                        productTitle,
                        productPrice,
                        id,
                        date,
                        categoryName!!,
                        productDescription
                    )
                }
            }
        }
        if (product != null) {
            viewModel.addProduct(product)
        }
        val i = Intent(activity, MainActivity::class.java)
        startActivity(i)
    }


    private fun switchBetweenFragments(fragment: Fragment) {
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit()
        fragmentTransaction.addToBackStack(null)
    }

    private fun switchToPreviousImage() {
        if (position > 0) {
            position--
            binding.imageSwitcher.setImageURI(images!![position])
        } else {
            Toast.makeText(requireContext(), "No more images...", Toast.LENGTH_LONG).show()
        }
    }

    private fun switchTonextText() {
        if (position < images!!.size - 1) {
            position++
            binding.imageSwitcher.setImageURI(images!![position])
        } else {
            Toast.makeText(requireContext(), "No more images...", Toast.LENGTH_LONG).show()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

//        if (binding.tvChosenLocation.text == "Choose Location") {
//            binding.productLocationLayout.error = "Select your location"
//            isValid = false
//
//        }
        if (images!!.size == 0) {
            binding.productImageLayout.error = "Please upload at least one image"
            isValid = false
        } else {
            binding.productImageLayout.isErrorEnabled = false
        }
        if (binding.productPrice.text?.isEmpty() == true) {
            binding.layoutPrice.error = "Please enter the price"
            isValid = false
            if (binding.productPrice.text.toString() < "1") {
                //Error message for example
                binding.layoutPrice.error = "Value should be at least 1."
            }
        } else {
            binding.layoutPrice.isErrorEnabled = false
        }
        if (binding.etProductDescription.text?.isEmpty() == true) {
            binding.productDescriptionLayout.error = "Enter Description"
            isValid = false
        } else {
            binding.productDescriptionLayout.isErrorEnabled = false
        }
        if (binding.adTitleEt.text?.isEmpty() == true) {
            binding.adTitleLayout.error = "Enter Title"
            isValid = false
        } else {
            binding.adTitleLayout.isErrorEnabled = false
        }
        return isValid
    }

    private fun pickImagesIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE)
    }

    @SuppressLint("WrongConstant")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.clipData
                position = if (data!!.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        images!!.add(imageUri)
                    }
                    binding.imageSwitcher.setImageURI(this.images!![0])
                    0
                } else {
                    val imageUri = data.data
                    if (imageUri != null) {
                        requireContext().contentResolver.takePersistableUriPermission(
                            imageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                    }
                    images?.add(imageUri)
                    binding.imageSwitcher.setImageURI(imageUri)
                    0
                }
                binding.tvAddImages.text = null
                binding.cardView.background = null
            }
        }
    }
}