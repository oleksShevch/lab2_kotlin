package com.svvar.lab2

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.svvar.lab2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fuelTypes = resources.getStringArray(R.array.fuel_types)
        val fuelTypeAdapter = ArrayAdapter(this, R.layout.spinner, fuelTypes)
        fuelTypeAdapter.setDropDownViewResource(R.layout.spinner)
        binding.fuelTypeSpinner.adapter = fuelTypeAdapter


        binding.calculateButton.setOnClickListener {
            val fuelType = binding.fuelTypeSpinner.selectedItem.toString()
            val volume = binding.volumeInput.text.toString().toFloat()
            val ashEfficiency = binding.ashEfficiency.text.toString().toFloat()
            val QLower = binding.Qlower.text.toString().toFloat()

            val emissions = calculateEmissions(fuelType, volume, ashEfficiency, QLower)

            binding.emissionOutput1.text = String.format("${getString(R.string.particle_emission)}: %.1f г/ГДж", emissions.first)
            binding.emissionOutput2.text = String.format("${getString(R.string.total_emission)}: %.1f т", emissions.second)
        }
    }


    private fun calculateEmissions(fuelType: String, volume: Float, ashEfficiency: Float, QLower: Float): Pair<Float, Float> {
        var ash = 0f
        var a = 0f
        var g = 0f

        if (fuelType == "Вугілля") {ash = 25.2f; g = 1.5f; a = 0.8f}
        if (fuelType == "Мазут") { ash = 0.15f;  g = 0f; a = 1f}
        if (fuelType == "Природний газ") { ash = 0f; g = 0f; a = 0f}

        val kSolid = (1000000 / QLower) * a * (ash / (100 - g)) * (1 - ashEfficiency)
        val E = (1f / 1000000) * kSolid * QLower * volume

        return Pair(kSolid, E)
    }
}
