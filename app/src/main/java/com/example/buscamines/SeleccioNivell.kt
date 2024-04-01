package com.example.buscamines

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SeleccioNivell : AppCompatActivity() {
    lateinit var easyLevelButton: Button
    lateinit var mediumLevelButton: Button
    lateinit var hardLevelButton: Button
    lateinit var extremeLevelButton: Button
    lateinit var backButton: Button
    private var NAME: String =""
    private var SCORE: String=""
    private var UID: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccio_nivell)

        // Load custom font
        val tf = Typeface.createFromAsset(assets, "fonts/Fredoka-Medium.ttf")

        // Retrieve values
        var intent:Bundle? = getIntent().extras
        UID = intent?.get("UID").toString()
        NAME = intent?.get("NAME").toString()
        SCORE = intent?.get("SCORE").toString()

        // Initialize buttons
        easyLevelButton = findViewById<Button>(R.id.facil_button)
        mediumLevelButton = findViewById<Button>(R.id.intermedi_button)
        hardLevelButton = findViewById<Button>(R.id.dificil_button)
        extremeLevelButton = findViewById<Button>(R.id.extrema_button)
        backButton=findViewById<Button>(R.id.back_btn)

        // Set custom font to buttons
        easyLevelButton.setTypeface(tf)
        mediumLevelButton.setTypeface(tf)
        hardLevelButton.setTypeface(tf)
        extremeLevelButton.setTypeface(tf)

        // Set onClickListeners to buttons
        easyLevelButton.setOnClickListener(){
            startGame("facil")
        }
        mediumLevelButton.setOnClickListener(){
            startGame("medio")
        }
        hardLevelButton.setOnClickListener(){
            startGame("dificil")
        }
        extremeLevelButton.setOnClickListener(){
            startGame("extremo")
        }
        backButton.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun startGame(selectedLevel: String) {
        val intent = Intent(this, joc::class.java)
        intent.putExtra("level", selectedLevel)
        intent.putExtra("UID",UID)
        intent.putExtra("NAME",NAME)
        intent.putExtra("SCORE",SCORE)
        startActivity(intent)
        finish()
    }
}
