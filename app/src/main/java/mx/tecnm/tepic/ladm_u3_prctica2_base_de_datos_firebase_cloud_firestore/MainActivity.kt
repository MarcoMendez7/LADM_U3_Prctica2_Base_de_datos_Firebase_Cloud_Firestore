package mx.tecnm.tepic.ladm_u3_prctica2_base_de_datos_firebase_cloud_firestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var baseremota=FirebaseFirestore.getInstance()
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
    var datalista = ArrayList<String>()
    var listaID = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        guardar.setOnClickListener {
            insertar()
        }
        consultar.setOnClickListener {
            if (telefono1.text.toString()==""){
                consultartodo()
            }else {
                consulta()
            }

        }

    }
    private fun insertar() {
        //para insertar el metodo a usar es ADD
        //ADD espera todos los campos del documento
        //Con formato CLAVE VALOR
        var opcion= false
        if(si.isChecked){
            opcion=true
        }else{
            opcion=false
        }

/*
        baseremota.collection("PEDIDOS").document(telefono.text.toString())
            .set(datosInsertar as Any)
            .addOnSuccessListener {
                alerta("SE INSERTO CORRECTAMENTE EN LA NUBE")
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }

 */
        var intent = Intent(this, MainActivity2::class.java)
        intent.putExtra("idElegido",telefono.text.toString())
        intent.putExtra("CELULAR",telefono.text.toString())
        intent.putExtra("ENTREGO",opcion.toString())
        intent.putExtra("FECHA",(sdf.format(Date())).toString())
        intent.putExtra("NOMBRE",nombre.text.toString())

        startActivity(intent)
        nombre.setText("")
        telefono.setText("")

    }
    private fun consultartodo(){
        baseremota.collection("PEDIDOS")
            .addSnapshotListener { querySnapshot, error ->
                if(error != null)
                {
                    mensaje(error.message!!)
                    return@addSnapshotListener
                }

                datalista.clear()
                listaID.clear()
                for (document in querySnapshot!!){
                    var cadena = " ${document.getString("CELULAR")} -- ${document.get("NOMBRE")} -- ${document.get("ENTREGADO")}-- ${document.get("TOTAL")}"
                    datalista.add(cadena)

                    listaID.add(document.id.toString())
                }

                listacontacto.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, datalista)
                listacontacto.setOnItemClickListener { parent, view, posicion, i ->
                    dialogoActualizar(posicion)
                }
            }
    }

    private fun consulta(){

        baseremota.collection("PEDIDOS").document(telefono1.text.toString()).get()
            .addOnSuccessListener {

                datalista.clear()
                listaID.clear()
                    var cadena = " ${it.get("CELULAR")as String?} -- ${it.get("NOMBRE")as String?}-- ${it.get("ENTREGADO")as String?}-- ${it.get("TOTAL")as Double?}"
                    datalista.add(cadena)

                    listaID.add(it.id.toString())


                listacontacto.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, datalista)
                listacontacto.setOnItemClickListener { parent, view, posicion, i ->
                    dialogoActualizar(posicion)
                }
            }
    }
    private fun dialogoActualizar(posicion: Int) {
        var idElegido = listaID.get(posicion)
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage("¿QUE YA SE ENTREGO EL PEDIDO DE \n${datalista.get(posicion)}?\n" +
                    "${listaID.get(listaID.size-1)}?")
            .setPositiveButton("ENTREGADO") { d, i ->
                baseremota.collection("PEDIDOS")
                    .document(idElegido)
                    .update("ENTREGADO",   true)
                    .addOnSuccessListener {
                        alerta("EXITO, SE ACTUALIZO ")
                    }
                    .addOnFailureListener {
                        mensaje("ERROR, NO SE PUDO ACTUALIZAR")
                    }
            }

            .setNegativeButton("NO ENTREGADO"){d,i->
                baseremota.collection("PEDIDOS")
                    .document(idElegido)
                    .update("ENTREGADO",   false)
                    .addOnSuccessListener {
                        alerta("EXITO, SE ACTUALIZO ")
                    }
                    .addOnFailureListener {
                        mensaje("ERROR, NO SE PUDO ACTUALIZAR")
                    }
            }

            .show()
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