package com.example.seniorproject.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.ProductViewModel
import com.example.seniorproject.R
import com.example.seniorproject.adapter.ProductAdapter
import com.example.seniorproject.databinding.FragmentVehicleBinding
import com.example.seniorproject.interfaces.ProductRecyclerViewInterface
import com.example.seniorproject.interfaces.Products
import com.example.seniorproject.productClasses.*
import kotlinx.android.synthetic.main.fragment_dialog.*


class ProductFragment : Fragment(), ProductRecyclerViewInterface {
    var car = arrayListOf<Car>()
    var products = arrayListOf<Products>()
    private var mobilePhones = arrayListOf<MobilePhone>()
    private var electronics = arrayListOf<Electronics>()
    private var sportsEquipments = arrayListOf<SportsEquipments>()
    private var pets = arrayListOf<Pet>()
    private var fashionAndBeauty = arrayListOf<FashionAndBeauty>()
    private var homeFurnitureAndDecor = arrayListOf<HomeFurnitureAndDecor>()
    private val viewModel by activityViewModels<ProductViewModel>()
    private var data = arrayListOf<Products>()
    private lateinit var binding: FragmentVehicleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentVehicleBinding.inflate(inflater, container, false)

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
            "categoryNameAndImage", AppCompatActivity.MODE_PRIVATE
        )
        val categoryName = sharedPreferences.getString("categoryName", "Vehicles")
        if (categoryName != null) {
            chooseCategory(categoryName)
        }
        when (categoryName) {
            "Vehicles" -> {
                car = Car.createCarCategory()
                setUpRecyclerView(car, binding)
            }
            "Mobile Phones" -> {
                mobilePhones = MobilePhone.createPhoneCategory()
                setUpRecyclerView(mobilePhones, binding)
            }
            "Electronics" -> {
                electronics = Electronics.createElectronicsCategory()
                setUpRecyclerView(electronics, binding)
            }
            "Sports & Equipment" -> {
                sportsEquipments = SportsEquipments.createSportCategory()
                setUpRecyclerView(sportsEquipments, binding)
            }
            "Pets" -> {
                pets = Pet.createPetCategory()
                setUpRecyclerView(pets, binding)
            }
            "Fashion & Beauty" -> {
                fashionAndBeauty = FashionAndBeauty.createFashionAndBeautyCategory()
                setUpRecyclerView(fashionAndBeauty, binding)
            }
            "Home Furniture & Decor" -> {
                homeFurnitureAndDecor = HomeFurnitureAndDecor.createHomeFurnitureAndDecorCategory()
                setUpRecyclerView(homeFurnitureAndDecor, binding)
            }

        }


        binding.categoryToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.categoryToolbar.setNavigationOnClickListener {
            val categoryFragment = CategoryFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, categoryFragment).commit()
            fragmentTransaction.addToBackStack(null)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

            @SuppressLint("FragmentLiveDataObserve")
            override fun onQueryTextSubmit(query: String?): Boolean {


                when (categoryName) {
                    "Vehicles" -> {
                        car.forEach {
                            if (it.brands.lowercase().contains(query.toString())) {
                                data.add(it)
                            }
                            if (query.toString().isEmpty()) {
                                setUpRecyclerView(car, binding)
                            }
                            return@forEach
                        }

                        setUpRecyclerView(data, binding)

                        data = arrayListOf()
                    }
                    "Mobile Phones" -> {
                        mobilePhones.forEach {
                            if (it.brands.lowercase().contains(query.toString())) {
                                data.add(it)
                            }
                            if (query.toString().isEmpty()) {
                                setUpRecyclerView(mobilePhones, binding)
                            }
                            return@forEach
                        }

                        setUpRecyclerView(data, binding)

                        data = arrayListOf()
                    }
                    "Electronics" -> {
                        electronics.forEach {
                            if (it.brands.lowercase().contains(query.toString())) {
                                data.add(it)
                            }
                            if (query.toString().isEmpty()) {
                                setUpRecyclerView(electronics, binding)
                            }
                            return@forEach
                        }

                        setUpRecyclerView(data, binding)

                        data = arrayListOf()
                    }
                    "Sports & Equipment" -> {
                        sportsEquipments.forEach {
                            if (it.brands.lowercase().contains(query.toString())) {
                                data.add(it)
                            }
                            if (query.toString().isEmpty()) {
                                setUpRecyclerView(sportsEquipments, binding)
                            }
                            return@forEach
                        }

                        setUpRecyclerView(data, binding)

                        data = arrayListOf()
                    }
                    "Pets" -> {
                        pets.forEach {
                            if (it.brands.lowercase().contains(query.toString())) {
                                data.add(it)
                            }
                            if (query.toString().isEmpty()) {
                                setUpRecyclerView(pets, binding)
                            }
                            return@forEach
                        }

                        setUpRecyclerView(data, binding)

                        data = arrayListOf()
                    }
                    "Fashion & Beauty" -> {
                        fashionAndBeauty.forEach {
                            if (it.brands.lowercase().contains(query.toString())) {
                                data.add(it)
                            }
                            if (query.toString().isEmpty()) {
                                setUpRecyclerView(fashionAndBeauty, binding)
                            }
                            return@forEach
                        }

                        setUpRecyclerView(data, binding)

                        data = arrayListOf()
                    }
                    else -> {
                        homeFurnitureAndDecor.forEach {
                            if (it.brands.lowercase().contains(query.toString())) {
                                data.add(it)
                            }
                            if (query.toString().isEmpty()) {
                                setUpRecyclerView(homeFurnitureAndDecor, binding)
                            }
                            return@forEach
                        }

                        setUpRecyclerView(data, binding)

                        data = arrayListOf()
                    }
                }

                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                if (categoryName == "Vehicles") {
                    car.forEach {
                        if (it.brands.lowercase().contains(newText.toString())) {
                            data.add(it)
                        }
                        if (newText.toString().isEmpty()) {
                            setUpRecyclerView(car, binding)
                        }
                        return@forEach
                    }
                    setUpRecyclerView(data, binding)
                    data = arrayListOf()
                } else if (categoryName == "Mobile Phones") {
                    mobilePhones.forEach {
                        if (it.brands.lowercase().contains(newText.toString())) {
                            data.add(it)
                        }
                        if (newText.toString().isEmpty()) {
                            setUpRecyclerView(mobilePhones, binding)
                        }
                        return@forEach
                    }

                    setUpRecyclerView(data, binding)

                    data = arrayListOf()
                } else if (categoryName == "Electronics") {
                    electronics.forEach {
                        if (it.brands.lowercase().contains(newText.toString())) {
                            data.add(it)
                        }
                        if (newText.toString().isEmpty()) {
                            setUpRecyclerView(electronics, binding)
                        }
                        return@forEach
                    }

                    setUpRecyclerView(data, binding)

                    data = arrayListOf()
                } else if (categoryName == "Sports & Equipment") {
                    sportsEquipments.forEach {
                        if (it.brands.lowercase().contains(newText.toString())) {
                            data.add(it)
                        }
                        if (newText.toString().isEmpty()) {
                            setUpRecyclerView(sportsEquipments, binding)
                        }
                        return@forEach
                    }

                    setUpRecyclerView(data, binding)

                    data = arrayListOf()
                } else if (categoryName == "Pets") {
                    pets.forEach {
                        if (it.brands.lowercase().contains(newText.toString())) {
                            data.add(it)
                        }
                        if (newText.toString().isEmpty()) {
                            setUpRecyclerView(pets, binding)
                        }
                        return@forEach
                    }

                    setUpRecyclerView(data, binding)

                    data = arrayListOf()
                } else if (categoryName == "Fashion & Beauty") {
                    fashionAndBeauty.forEach {
                        if (it.brands.lowercase().contains(newText.toString())) {
                            data.add(it)
                        }
                        if (newText.toString().isEmpty()) {
                            setUpRecyclerView(fashionAndBeauty, binding)
                        }
                        return@forEach
                    }

                    setUpRecyclerView(data, binding)

                    data = arrayListOf()
                } else {
                    homeFurnitureAndDecor.forEach {
                        if (it.brands.lowercase().contains(newText.toString())) {
                            data.add(it)
                        }
                        if (newText.toString().isEmpty()) {
                            setUpRecyclerView(homeFurnitureAndDecor, binding)
                        }
                        return@forEach
                    }

                    setUpRecyclerView(data, binding)

                    data = arrayListOf()
                }
                return false
            }

        })

        return binding.root
    }


    private fun setUpRecyclerView(anyProduct: List<Products>, binding: FragmentVehicleBinding) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this.activity)
        binding.recyclerView.adapter = ProductAdapter(anyProduct, this)
    }

    private fun chooseCategory(categoryName: String) {
        binding.categoryToolbar.title = categoryName
    }

    override fun onItemClick(product: Products) {
        val shar: SharedPreferences = requireActivity().getSharedPreferences(
            "categoryNameAndImage", AppCompatActivity.MODE_PRIVATE
        )
        val categoryName = shar.getString("categoryName", "Vehicles")
        val detailsForSellingFragment = DetailsForSellingFragment()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
            "CategoryAndBrand", AppCompatActivity.MODE_PRIVATE
        )

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("productBrand", product.brands)
        editor.putString("productCategory", categoryName)
        editor.apply()

        fragmentTransaction.replace(R.id.frameLayout, detailsForSellingFragment).commit()
        fragmentTransaction.addToBackStack(null)
    }

}
