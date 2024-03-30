package com.example.buscamines

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.LinkedList
import java.util.Queue
import java.util.Timer
import java.util.TimerTask


class joc : AppCompatActivity() {
    private var NOM: String =""
    private var PUNTUACIO: String=""
    private var UID: String=""
    private lateinit var visited_arr: IntArray // cols*rows array of each button id
    private lateinit var non_clicked_cell:String
    private lateinit var empty_cell:String
    private lateinit var mine_clicked:String
    private lateinit var flag:String
    private var rows : Int = 0
    private var columns : Int = 0
    private var mines : Int = 0
    private lateinit var mines_arr: IntArray // array of mines
    private lateinit var tvTime: TextView
    private lateinit var tvMines: TextView
    private var nivelSeleccionado: String? = null
    private var minesCounter = 0
    var digitResources = mutableMapOf<Int, String>()
    private var punts:Int = 0
    private lateinit var sp : SoundPool
    private var soundVictoriaId: Int = 0
    private var soundDerrotaId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joc)
        // Inicializar SoundPool
        initializeSoundPool()

        // Cargar el sonido


        nivelSeleccionado = intent.getStringExtra("nivel")
        var intent:Bundle? = intent.extras
        UID = intent?.get("UID").toString()
        NOM = intent?.get("NOM").toString()
        PUNTUACIO = intent?.get("PUNTUACIO").toString()
        if(nivelSeleccionado=="facil"){
            punts=5
        }else if(nivelSeleccionado=="medio"){
            punts=10
        }else if(nivelSeleccionado=="dificil"){
            punts=15
        }else if(nivelSeleccionado=="extremo"){
            punts=40
        }

        val nivelConfiguraciones = mapOf(
            "facil" to Triple("non_clicked_cell_facil", 5 to 4, 3),
            "medio" to Triple("non_clicked_cell_medio", 8 to 5, 6),
            "dificil" to Triple("non_clicked_cell_dificil", 11 to 7, 11),
            "extremo" to Triple("non_clicked_cell_extremo", 17 to 11, 28)
        )

        val digitPrefix = "digit_"
        val numDigits = 8 // Suponiendo que tienes 8 dígitos

        nivelConfiguraciones[nivelSeleccionado]?.let { (nonClickedCell, dimensions, numMines) ->
            non_clicked_cell = nonClickedCell
            rows = dimensions.first
            columns = dimensions.second
            mines = numMines

            empty_cell = "empty_cell_$nivelSeleccionado"
            flag = "flag_$nivelSeleccionado"
            mine_clicked = "mine_clicked_$nivelSeleccionado"

            for (i in 1..numDigits) {
                digitResources[i] = "$digitPrefix$i" + "_$nivelSeleccionado"
                Log.d("Array_DigitSources", "" + digitResources[0])

            }
        }
        tvTime = findViewById(R.id.time_id);
        tvMines=findViewById(R.id.minesLeft_id);
        tvMines.text = "Mines Left: ${mines}"
        val resourceId = resources.getIdentifier(non_clicked_cell, "drawable", packageName)
        var tableLayout: TableLayout
        var imageButton: ImageView
        mines_arr=generateMines(columns*rows)
        visited_arr = IntArray(columns * rows)
        minesCounter=mines
        // Crear contador de tiempo
        var secondsAfterStart = 0
        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    secondsAfterStart++
                    var strTime = "Time: "
                    val minutes = secondsAfterStart / 60
                    val seconds = secondsAfterStart % 60

                    if (minutes < 10) {
                        strTime += "0"
                    }
                    strTime += minutes

                    strTime += ":"

                    if (seconds < 10) {
                        strTime += "0"
                    }
                    strTime += seconds

                    tvTime.text = strTime
                }
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 1000)

        // create ImageView components (buttons) for game board
        //filas
        for (i in 0 until rows ) {
            tableLayout = findViewById<View>(R.id.buttonsPanel_id) as TableLayout
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            tableRow.gravity = Gravity.CENTER
            //columnas
            for (j in 0 until columns) {
                imageButton = ImageView(this)
                imageButton.setLayoutParams(
                    TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                )
                imageButton.setId(i * columns + j)
                imageButton.setImageResource(resourceId)
                imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER)
                // handler for CLICK on this image (button)
                imageButton.setOnClickListener { v ->
                    val iView = v as ImageView

                    if (visited_arr[iView.id] == 2 || visited_arr[iView.id] == 1) {
                        return@setOnClickListener
                    }

                    // check if clicked button contains mine and
                    // stop the game if it's true
                    if (checkIfMine(iView.id)) {
                        setIconToButton(iView, -3)
                        Handler().postDelayed({

                            // Código a ejecutar después de 2 segundos
                            timerTask.cancel()
                            val imageView2 = findViewById<ImageView>(R.id.imageView2)
                            imageView2.setImageResource(R.drawable.lost)
                            imageView2.visibility = View.VISIBLE
                            val continuar_btn = findViewById<Button>(R.id.continuar_btn)
                            continuar_btn.visibility = View.VISIBLE
                            tableLayout.visibility = View.GONE
                            // Inicializar SoundPool

                            // Cargar el sonido
                            soundDerrotaId = loadSound(R.raw.sonido_derrota)

                            // Reproducir el sonido
                            playSound(soundDerrotaId)

                            continuar_btn.setOnClickListener {
                                val intent = Intent(this, Menu::class.java)
                                startActivity(intent)
                                finish()
                            }

                            secondsAfterStart = 0
                        }, 1000) // 2000 milisegundos = 2 segundos
                    } else {
                        checkNeighbourCells(iView.id)
                    }

                    if (checkIfEnd()) {
                        timerTask.cancel()
                        pointsUpdate()
                        val imageView2=findViewById<ImageView>(R.id.imageView2)
                        imageView2.visibility = View.VISIBLE
                        val continuar_btn=findViewById<Button>(R.id.continuar_btn)
                        continuar_btn.visibility = View.VISIBLE
                        tableLayout.visibility=View.GONE
                        // Inicializar SoundPool


                        // Cargar el sonido
                        soundVictoriaId = loadSound(R.raw.sonido_victoria)

                        // Reproducir el sonido
                        playSound(soundVictoriaId)

                        continuar_btn.setOnClickListener {
                            val intent= Intent(this, Menu::class.java)

                            startActivity(intent)
                            finish()
                        }

                        secondsAfterStart = 0
                    }
                }
                // handler for LONG CLICK on this image (button)
                imageButton.setOnLongClickListener { v ->
                    val iView = v as ImageView
                    iView.isClickable = false

                    // if cell hasn't been clicked or visited
                    if (visited_arr[iView.id] != 2 && visited_arr[iView.id] != 1 && minesCounter > 0) {
                        setIconToButton(iView, -1)
                        minesCounter--
                        visited_arr[iView.id] = 2

                        // if cell has been already long-clicked - return one mine
                    } else if (visited_arr[iView.id] == 2) {
                        setIconToButton(iView, -2)
                        minesCounter++
                        visited_arr[iView.id] = 0
                    }

                    // change label text according to the number of mines left
                    tvMines.text = String.format("Mines Left: %d", minesCounter)

                    if (checkIfEnd()) {
                        timerTask.cancel()
                        pointsUpdate()
                        val imageView2=findViewById<ImageView>(R.id.imageView2)
                        imageView2.visibility = View.VISIBLE
                        val continuar_btn=findViewById<Button>(R.id.continuar_btn)
                        continuar_btn.visibility = View.VISIBLE
                        tableLayout.visibility=View.GONE
                        // Inicializar SoundPool


                        // Cargar el sonido
                        soundVictoriaId = loadSound(R.raw.sonido_victoria)

                        // Reproducir el sonido
                        playSound(soundVictoriaId)

                        continuar_btn.setOnClickListener {
                            val intent= Intent(this, Menu::class.java)

                            startActivity(intent)
                            finish()
                        }

                        secondsAfterStart = 0
                    }

                    true
                }


                tableRow.addView(imageButton)
            }


            tableLayout.addView(
                tableRow, TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            )
        }


    }
    private fun generateMines(square: Int): IntArray {
        mines_arr = IntArray(mines)
        val min = 1
        val max = square - 1
        for (i in 0 until mines) {
            var isUnique = false
            while (!isUnique) {
                val randNum = (min..max).random()
                isUnique = true
                for (k in 0..i) {
                    if (mines_arr[k] == randNum) {
                        isUnique = false
                        break
                    }
                }
                if (isUnique) {
                    mines_arr[i] = randNum
                }
            }
        }
        for (j in 0 until mines) {
            Log.d("CREATED_ARR", "" + mines_arr[j])
        }
        return mines_arr
    }
    fun checkIfEnd(): Boolean {
        for (i in 0 until rows * columns) {
            if (visited_arr[i] == 0) {
                return false
            }
        }
        return true
    }
    // Contar el número de minas en las celdas vecinas
    fun checkForMinesCount(firstParam: Int, num: Int): Int {
        if (!checkIfValidCoord(firstParam, num)) {
            return 10
        }
        for (i in 0 until mines) {
            if (mines_arr[i] == num) {
                return 1
            }
        }
        return 0
    }
    fun setIconToButton(imageView: ImageView?, minesNum: Int) {
        if (imageView == null) {
            Log.d("setIconToButton", "Button was not found!")
            return
        }
        val non_clicked_cell = resources.getIdentifier(non_clicked_cell, "drawable", packageName)
        val digit_1 = resources.getIdentifier(digitResources[1], "drawable", packageName)
        val digit_2 = resources.getIdentifier(digitResources[2], "drawable", packageName)
        val digit_3 = resources.getIdentifier(digitResources[3], "drawable", packageName)
        val digit_4 = resources.getIdentifier(digitResources[4], "drawable", packageName)
        val digit_5 = resources.getIdentifier(digitResources[5], "drawable", packageName)
        val digit_6 = resources.getIdentifier(digitResources[6], "drawable", packageName)
        val digit_7 = resources.getIdentifier(digitResources[7], "drawable", packageName)
        val digit_8 = resources.getIdentifier(digitResources[8], "drawable", packageName)
        val flag = resources.getIdentifier(flag, "drawable", packageName)
        val emptycell = resources.getIdentifier(empty_cell, "drawable", packageName)
        val mineclicked = resources.getIdentifier(mine_clicked, "drawable", packageName)



        when (minesNum) {
            0 -> imageView.setImageResource(emptycell)
            1 -> imageView.setImageResource(digit_1)
            2 -> imageView.setImageResource(digit_2)
            3 -> imageView.setImageResource(digit_3)
            4 -> imageView.setImageResource(digit_4)
            5 -> imageView.setImageResource(digit_5)
            6 -> imageView.setImageResource(digit_6)
            7 -> imageView.setImageResource(digit_7)
            8 -> imageView.setImageResource(digit_8)
            -1 -> imageView.setImageResource(flag)
            -2 -> imageView.setImageResource(non_clicked_cell)
            -3 -> imageView.setImageResource(mineclicked)
        }
    }

    fun checkIfMine(buttonNumInt: Int): Boolean {
        for (i in 0 until mines) {
            if (buttonNumInt == mines_arr[i]) {
                return true
            }
        }
        return false
    }


    fun checkIfValidCoord(firstParam: Int, num: Int): Boolean {
        // Transformar num en coordenadas x e y
        val x = num % columns
        val y = num / columns
        val xPrev = firstParam % columns

        // Verificar si la celda está dentro del rango
        return x >= 0 && y >= 0 && x < columns && y < rows &&
                (x != 0 || xPrev != (columns - 1)) && (x != (columns - 1) || xPrev != 0)
    }
    fun checkNeighbourCell(firstParam: Int, butNum: Int): Int {
        if (!checkIfValidCoord(firstParam, butNum)) {
            return 10
        }

        for (i in 0 until mines) {
            if (mines_arr[i] == butNum) {
                return 2
            }
        }

        val cols = columns

        val arr = IntArray(8)
        arr[0] = butNum - cols
        arr[1] = butNum - cols + 1
        arr[2] = butNum + 1
        arr[3] = butNum + cols + 1
        arr[4] = butNum + cols
        arr[5] = butNum + cols - 1
        arr[6] = butNum - 1
        arr[7] = butNum - cols - 1

        for (i in 0 until mines) {
            for (j in 0 until 8) {
                if (checkIfValidCoord(butNum, arr[j]) && mines_arr[i] == arr[j]) {
                    return 1
                }
            }
        }
        return 0
    }

    // draw route until cell with mine as neighbour is found
    fun checkNeighbourCells(butNum: Int) {
        val queue: Queue<Int> = LinkedList()
        queue.add(butNum)
        visited_arr[butNum] = 1

        val rows = rows
        val cols = columns
        val arr_of_neighbours = IntArray(8)

        // draw route to expand all needed cells
        while (queue.size > 0) {
            var butNum = queue.element()
            arr_of_neighbours[0] = butNum - cols
            arr_of_neighbours[1] = butNum - cols + 1
            arr_of_neighbours[2] = butNum + 1
            arr_of_neighbours[3] = butNum + cols + 1
            arr_of_neighbours[4] = butNum + cols
            arr_of_neighbours[5] = butNum + cols - 1
            arr_of_neighbours[6] = butNum - 1
            arr_of_neighbours[7] = butNum - cols - 1
            queue.remove()

            for (i in 0 until 8) {
                val res = checkNeighbourCell(butNum, arr_of_neighbours[i])
                if (res == 0 &&
                    visited_arr[arr_of_neighbours[i]] == 0
                ) {
                    queue.add(arr_of_neighbours[i])
                    visited_arr[arr_of_neighbours[i]] = 1
                } else if (res == 1 && visited_arr[arr_of_neighbours[i]] == 0) {
                    visited_arr[arr_of_neighbours[i]] = 1
                }
            }
        }

        var minesInNeighbourCells = 0

        // count mines in neighbour cells
        for (j in 0 until rows * cols) {
            if (visited_arr[j] == 1) {
                if (checkForMinesCount(j - cols, j - cols) == 1) {
                    minesInNeighbourCells++
                }
                if (((j % cols) != (cols - 1)) && checkForMinesCount(
                        j - cols + 1,
                        j - cols + 1
                    ) == 1
                ) {
                    minesInNeighbourCells++
                }
                if (((j % cols) != (cols - 1)) && checkForMinesCount(j + 1, j + 1) == 1) {
                    minesInNeighbourCells++
                }
                if (((j % cols) != (cols - 1)) && checkForMinesCount(
                        j + cols + 1,
                        j + cols + 1
                    ) == 1
                ) {
                    minesInNeighbourCells++
                }
                if (checkForMinesCount(j + cols, j + cols) == 1) {
                    minesInNeighbourCells++
                }
                if (((j % cols) != 0) && checkForMinesCount(
                        j + cols - 1,
                        j + cols - 1
                    ) == 1
                ) {
                    minesInNeighbourCells++
                }
                if (((j % cols) != 0) && checkForMinesCount(j - 1, j - 1) == 1) {
                    minesInNeighbourCells++
                }
                if (((j % cols) != 0) && checkForMinesCount(
                        j - cols - 1,
                        j - cols - 1
                    ) == 1
                ) {
                    minesInNeighbourCells++
                }
                val imageView: ImageView = findViewById(j)
                setIconToButton(imageView, minesInNeighbourCells)
            }
            minesInNeighbourCells = 0
        }
    }
    fun pointsUpdate(){
        var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://buscamines-11db7-default-rtdb.europe-west1.firebasedatabase.app/")
        var bdreference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
        val puntuaciojugador=PUNTUACIO.toInt()
        val puntsGuanyats=puntuaciojugador+punts
        bdreference.child(UID).child("Puntuacio").setValue(puntsGuanyats.toString())
    }
    private fun initializeSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            sp = SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            sp = SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }
        Log.d("SoundPool", "SoundPool initialized")

    }
    private fun playSound(soundId: Int) {
        sp.play(soundId, 1F, 1F, 1, 0, 1F)
    }
    private fun loadSound(@RawRes soundResId: Int): Int {
        val soundId = sp.load(this, soundResId, 1)
        sp.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                Log.d("SoundPool", "Sound loaded with ID: $sampleId")
                // Ahora el sonido está listo para ser reproducido
                if (sampleId == soundVictoriaId) {
                    // Reproducir el sonido de victoria una vez que esté listo
                    playSound(soundVictoriaId)
                } else if (sampleId == soundDerrotaId) {
                    // Reproducir el sonido de derrota una vez que esté listo
                    playSound(soundDerrotaId)
                }
            } else {
                Log.w("SoundPool", "Error loading sound with ID: $sampleId")
            }
        }
        return soundId
    }

}


