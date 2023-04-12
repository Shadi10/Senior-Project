package com.example.seniorproject.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import androidx.fragment.app.DialogFragment
import com.example.seniorproject.ProductViewModel
import com.example.seniorproject.R
import com.example.seniorproject.data.product.Product
import com.example.seniorproject.databinding.FragmentDialogBinding
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

open class DialogFragment : DialogFragment() {
    private lateinit var binding: FragmentDialogBinding
    private var images: ArrayList<Uri?>? = null
    private var position = 0
    private val PICK_IMAGES_CODE = 0
    private val viewModel by activityViewModels<ProductViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDialogBinding.inflate(inflater, container, false)
        val sharedPreferences = requireActivity().getSharedPreferences(
            "productEdit", MODE_PRIVATE
        )
        // Inflate the layout for this fragment
        images = ArrayList()
        binding.updateImageSwitcher.setFactory { ImageView(activity?.applicationContext) }
        binding.updateCardView.setOnClickListener {
            images = arrayListOf()
            pickImagesIntent()
        }

        binding.layoutPrice.editText?.setText(sharedPreferences.getString("productPrice", "0"))
        binding.adTitleLayout.editText?.setText(sharedPreferences.getString("productTitle", "null"))
        binding.productDescriptionLayout.editText?.setText(
            sharedPreferences.getString(
                "productDescription", ""
            )
        )
        binding.nextText.setOnClickListener {
            switchTonextText()
        }
        binding.previousImage.setOnClickListener {
            switchToPreviousImage()
        }

        binding.updateLocationLl.setOnClickListener {
            val supportFragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val dialogLocationFragment = DialogLocationFragment()
            this.dismiss()
            dialogLocationFragment.show(supportFragmentManager, "DialogLocationFragment")
        }
        val sharedPreferencesLocation: SharedPreferences? = this.activity?.getSharedPreferences(
            "UpdatedlocationCoordinates", AppCompatActivity.MODE_PRIVATE
        )
        val address = sharedPreferencesLocation?.getString("address", "Choose Location")
        if (address !== null) binding.tvUpdateChosenLocation.text = address

        binding.successBtn.setOnClickListener {
            if (validateInputs()) {
                updatePost()
                dismiss()
            }
        }
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext()) {
            override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    val v: View? = activity?.currentFocus
                    if (v is EditText) {
                        val outRect = Rect()
                        v.getGlobalVisibleRect(outRect)
                        if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                            v.clearFocus()
                            val imm: InputMethodManager =
                                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                        }
                    }
                }
                return super.dispatchTouchEvent(ev)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updatePost() {
        val calendar = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("EEE, MMM d ,h:mm a ")
        val date = dateFormat.format(calendar.time)
        val productPrice = binding.updateProductPrice.text.toString().toDouble()
        val productTitle = binding.etUpdateTitle.text.toString()
        val productDescription = binding.etUpdateProductDescription.text.toString()
        val pref: SharedPreferences =
            requireActivity().getSharedPreferences("productIdPref", AppCompatActivity.MODE_PRIVATE)

        val productId = pref.getInt("productId", 0)

        val sharedPreferences: SharedPreferences? =
            this.activity?.getSharedPreferences("usernameAndId", AppCompatActivity.MODE_PRIVATE)

        val id = sharedPreferences?.getInt("userId", 0)
        val userName = sharedPreferences?.getString("userName", "username")
        val sharedPref =
            this.activity?.getSharedPreferences("CategoryAndBrand", AppCompatActivity.MODE_PRIVATE)

        val categoryName = sharedPref?.getString("productCategory", "null")
        val updatedProduct = userName?.let { it ->
            id?.let { id ->
                images?.get(0)?.let { it1 ->
                    Product(
                        productId,
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

        if (updatedProduct != null) {
            viewModel.updateProduct(updatedProduct)
        }
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
            binding.updateImageSwitcher.setImageURI(images!![position])
        } else {
            Toast.makeText(requireContext(), "No more images...", Toast.LENGTH_LONG).show()
        }
    }

    private fun switchTonextText() {
        if (position < images!!.size - 1) {
            position++
            binding.updateImageSwitcher.setImageURI(images!![position])
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
        if (binding.updateProductPrice.text?.isEmpty() == true) {
            binding.layoutPrice.error = "Please enter the price"
            isValid = false
            if (binding.updateProductPrice.text.toString() < "1") {
                //Error message for example
                binding.layoutPrice.error = "Value should be at least 1."
            }
        } else {
            binding.layoutPrice.isErrorEnabled = false
        }
        if (binding.etUpdateProductDescription.text?.isEmpty() == true) {
            binding.productDescriptionLayout.error = "Enter Description"
            isValid = false
        } else {
            binding.productDescriptionLayout.isErrorEnabled = false
        }
        if (binding.etUpdateTitle.text?.isEmpty() == true) {
            binding.adTitleLayout.error = "Enter Title"
            isValid = false
        } else {
            binding.adTitleLayout.isErrorEnabled = false
        }
        return isValid
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
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
                    binding.updateImageSwitcher.setImageURI(this.images!![0])
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
                    binding.updateImageSwitcher.setImageURI(imageUri)
                    0
                }
                binding.tvUpdateAddImages.text = null
                binding.updateCardView.background = null
            }
        }
    }
}