package mx.tecnm.tepic.ladm_u3_prctica2_base_de_datos_firebase_cloud_firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*

class MainActivity2 : AppCompatActivity() {
    var baseremota = FirebaseFirestore.getInstance()
    var id = ""
    var cont=1
     var celular=""
    var nombre=""
    var fecha=""
    var entregado=""
    var items = ArrayList<String>()
    var total=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        var extra = intent.extras
        id = extra!!.getString("idElegido")!!
        celular = extra!!.getString("CELULAR")!!
        nombre= extra!!.getString("NOMBRE")!!
        entregado= extra!!.getString("ENTREGO")!!
        fecha = extra!!.getString("FECHA")!!

        button.setOnClickListener {
            insertar()
        }
    }
    private fun insertar(){
        var producto = hashMapOf(
                "CANTIDAD" to cantidad.text.toString().toInt(),
                "DESCRIPCION" to descripcion.text.toString(),
                "PRECIO" to precio.text.toString().toDouble())
        total =total+((cantidad.text.toString().toInt())*(precio.text.toString().toDouble()))

        if(cont==1) {



            var datosInsertar = hashMapOf(
                    "CELULAR" to celular,
                    "ENTREGADO" to entregado,
                    "FECHA" to fecha,
                    "NOMBRE" to nombre,
                    "ITEM1" to producto as Map<String, Any>,
                    "TOTAL" to total)


            baseremota.collection("PEDIDOS").document(id)
                    .set(datosInsertar as Any)
                    .addOnSuccessListener {
                        alerta("SE INSERTO CORRECTAMENTE EN LA NUBE")
                    }
                    .addOnFailureListener {
                        mensaje("ERROR: ${it.message!!}")
                    }

            cont++
        }else {
            baseremota.collection("PEDIDOS")
                    .document(id)
                    .update("ITEM"+cont,producto as  Map<String, Any> ,"TOTAL",total)
        cont++
        }
    }


    private fun alerta(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_LONG).show()
    }
    fun mensaje (n:String) {
        AlertDialog.Builder(this)
                .setTitle("ATENCION")
                .setMessage(n)
                .setPositiveButton("ok") { d, i -> }
                .show()


    }

}