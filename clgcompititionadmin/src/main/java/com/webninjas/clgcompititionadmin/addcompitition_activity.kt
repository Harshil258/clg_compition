package com.webninjas.clgcompititionadmin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class addcompitition_activity : AppCompatActivity() {

    lateinit var starttime: TextView
    lateinit var endtime: TextView
    lateinit var DATE: TextView
    lateinit var OK: TextView
    lateinit var competition_name: EditText
    lateinit var logourl: EditText
    var sttime: String = ""
    var edtime: String = ""
    var date: String = ""

    lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcompitition)

        db = FirebaseFirestore.getInstance()
        starttime = findViewById(R.id.starttime)
        endtime = findViewById(R.id.endtime)
        DATE = findViewById(R.id.DATE)
        OK = findViewById(R.id.OK)
        competition_name = findViewById(R.id.competition_name)
        logourl = findViewById(R.id.logourl)


        starttime.setOnClickListener {
            timepicker("sttime")
        }
        endtime.setOnClickListener {
            timepicker("edtime")
        }

        DATE.setOnClickListener {
            datepicker()
        }


        OK.setOnClickListener {
            if (competition_name.text.toString() == "") {
                Toast.makeText(this, "enter Compitition name first", Toast.LENGTH_SHORT).show()
            } else if (logourl.text.toString() == "") {
                Toast.makeText(this, "enter LOGO  URL first", Toast.LENGTH_SHORT).show()
            } else if (sttime == "") {
                Toast.makeText(this, "choose start time first", Toast.LENGTH_SHORT).show()
            } else if (edtime == "") {
                Toast.makeText(this, "choose end time first", Toast.LENGTH_SHORT).show()
            } else if (date == "") {
                Toast.makeText(this, "choose date first", Toast.LENGTH_SHORT).show()
            } else {
                var map = hashMapOf(
                    "starttime" to sttime.toString(),
                    "endtime" to edtime,
                    "DATE" to date,
                    "competition_name" to competition_name.text.toString(),
                    "logourl" to logourl.text.toString()
                )

                db.collection("competitions").document(competition_name.text.toString()).set(map)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Done!!!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Something went wrong!!!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
    }

    private fun datepicker() {
        val calendar: Calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)
        Log.d("saegsargsrgh", month.toString())

        val mDatePicker: DatePickerDialog =
            DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    date = String.format("%d/%d/%d", dayOfMonth, month + 1, year)
                    Log.d("saegsargsrgh", date)
                    Log.d("saegsargsrgh", year.toString())
                    Log.d("saegsargsrgh", month.toString())
                    Log.d("saegsargsrgh", dayOfMonth.toString())
                    var d1: DateFormat = SimpleDateFormat("dd/M/yyyy")
                    var dd1 = d1.parse(date)
                    Log.d("saegsargsrgh", dd1.toString())

                    val time = dd1
                    val sdf1 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
                    val date1 = sdf1.parse(time.toString())

                    val sdf = SimpleDateFormat("dd-MM-yyyy")
                    val s = sdf.format(date1.time)

                    date = s.toString()
                    DATE.setText(date)
                }

            }, year, month, day)
        mDatePicker.show()
    }

    fun timepicker(st: String) {
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//                selectedTime.setText(String.format("%d : %d", hourOfDay, minute))
                Log.d("saegsargsrgh", String.format("%d:%d", hourOfDay, minute))
                if (st == "sttime") {
                    sttime = String.format("%d:%d", hourOfDay, minute)
//                    sttime = SimpleDateFormat("hh:mm a").parse(sttime).toString()
                    val f1: DateFormat = SimpleDateFormat("HH:mm")
                    val d: Date = f1.parse(sttime)
                    val f2: DateFormat = SimpleDateFormat("h:mm a")
                    Log.d("saegsargsrgh", f2.format(d))
                    sttime = f2.format(d).toUpperCase()
                    starttime.setText(sttime)

                } else if (st == "edtime") {
                    edtime = String.format("%d:%d", hourOfDay, minute)

                    val f1: DateFormat = SimpleDateFormat("HH:mm")
                    val d: Date = f1.parse(edtime)
                    val f2: DateFormat = SimpleDateFormat("h:mm a")
                    edtime = f2.format(d).toUpperCase()
                    endtime.setText(edtime)
                }
            }
        }, hour, minute, false)

        mTimePicker.show()
    }
}