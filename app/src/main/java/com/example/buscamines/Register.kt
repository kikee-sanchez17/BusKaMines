import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.buscamines.R
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date

class Register : AppCompatActivity() {
    private  val TAG = "RegisterActivity"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val db = Firebase.firestore

        // Access a collection
        val usersCollection = db.collection("Users")

        // Create an instance of User and set the data
        val user = User(
            "mrenric488",
            "123",
            "enricmontoya@gmail.com"
        )
        val nestedData = hashMapOf(
            "Username" to user.username,
            "Password" to user.password,
            "JoinDate" to user.joinDate,
            "email"    to user.email
        )
        // Use the username as the document ID
        val documentId = user.username ?: ""
        // Add the user data to Firestore using the username as the document ID
        usersCollection.document("tcMznXBXcz2skxAIg9bK").set(nestedData)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }


    data class User(
        val username: String? = null,
        val password: String? = null,
        val email: String? = null,
        val joinDate: String = getDateTime()
    ) {
        companion object {
            private fun getDateTime(): String {
                try {
                    // Obtener la fecha y hora actual
                    val netDate = Date()

                    // Formatear la fecha en el formato deseado
                    val sdf = SimpleDateFormat("MM/dd/yyyy")
                    return sdf.format(netDate)
                } catch (e: Exception) {
                    // Manejar cualquier excepción que pueda ocurrir durante el proceso
                    return e.toString()
                }
            }
        }
    }

}