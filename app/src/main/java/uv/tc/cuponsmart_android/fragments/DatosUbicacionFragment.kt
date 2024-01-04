package uv.tc.cuponsmart_android.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import uv.tc.cuponsmart_android.OnFragmentInteractionListener
import uv.tc.cuponsmart_android.R
import uv.tc.cuponsmart_android.databinding.FragmentDatosUbicacionBinding
import uv.tc.cuponsmart_android.modelo.DAO.CatalogoDAO
import uv.tc.cuponsmart_android.modelo.poko.Estado
import uv.tc.cuponsmart_android.modelo.poko.Municipio
import kotlin.math.pow
import kotlin.math.roundToLong

class DatosUbicacionFragment(private val listener: OnFragmentInteractionListener) : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener,OnFragmentInteractionListener,
    AdapterView.OnItemSelectedListener {
    /---------- DECLARACION DE VARIABLES ------------//
    private var latitud: Double = 0.0
    private var longitud: Double=0.0
    private lateinit var binding: FragmentDatosUbicacionBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var estados: List<Estado>
    private lateinit var municipios: List<Municipio>
    private var idMunicipioSeleccionado: Int =0
    //---------- LAYOUTS PARA EFECTO DE CARGA -------//
    private lateinit var shimmerContainer: LinearLayout
    private lateinit var dataContainer: LinearLayout
    private var isInitialMapLoad = true

    //------- METODOS DEL FRAGMENT ----------//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = createLocationCallback()
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDatosUbicacionBinding.inflate(inflater, container, false)
        shimmerContainer = binding.vistaCargando
        dataContainer = binding.vistaCargada
        cargarSpiners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personalizarComponentes()
        Handler(Looper.getMainLooper()).postDelayed({
            showData()
        },5000)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    
    //-------- METODO PARA ESTABLECER LA VISIBILIDAD DE LAS LAYOUTS DE CARGA --------//
    private fun showData() {
        binding.vistaCargando.isVisible=false
        binding.vistaCargada.isVisible = true
    }

    //-------- METODO PARA PERSONALIZAR COMPONENTES Y EVENTOS ---------//
    private fun personalizarComponentes(){
        binding.etCalle.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.etCalle.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            }else{
                binding.etCalle.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }

        binding.etColonia.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.etColonia.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            }else{
                binding.etColonia.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        
        binding.etCodigoPostal.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.etCodigoPostal.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            }else{
                binding.etCodigoPostal.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
        
        binding.etNumeroCasa.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.etNumeroCasa.getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            }else{
                binding.etNumeroCasa.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    //--------- METODO PARA LLENAR EL SPINER ESTADO -------------//
    private fun cargarSpiners(){
        CatalogoDAO.obtenerEstados(requireContext(),"catalogo/obtenerEstados"){ estados ->
            if (estados != null) {
                this.estados = estados
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados)
                adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
                binding.spEstado.adapter = adapter
                binding.spEstado.onItemSelectedListener=this@DatosUbicacionFragment
            } else {
                Toast.makeText(context, "Error al obtener estados", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    //-------- METODO PARA LLENAR EL SPINNER DE LOS MUNICIPIOS ----------//
    private fun llenarMunicipios(idEstado : Int){
           CatalogoDAO.obtenerMunicipiosEstado(requireContext(), "catalogo/obtenerMunicipiosEstados/$idEstado"){
               municipios ->
               if (municipios !=null){
                   this.municipios = municipios
                   val adaptadorMunicipios = ArrayAdapter(requireActivity(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, municipios)
                   binding.spMunicipio.adapter = adaptadorMunicipios
                   binding.spMunicipio.onItemSelectedListener=this@DatosUbicacionFragment
               }
           }
    }

    //---------- METODO DE ACCIONES AL CARGAR EL MAPA ----------//
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener { latLng -> onMapClick(latLng) }
        // Habilitar la capa de mi ubicación
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            // Iniciar la actualización de la ubicación en tiempo real
            startLocationUpdates()
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    // Mover la cámara a la ubicación actual y hacer zoom
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
                }
            }
        } else {
            // Solicitar permisos de ubicación si no están concedidos
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getLastLocationAndZoom() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                // Mover la cámara a la ubicación actual y hacer zoom
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
            }
        }
    }

    //--------- METODO PARA ACTUALIZAR LA UBICACION EN TIEMPO REAK ------------//
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000 // Intervalo de actualización de ubicación en milisegundos
            fastestInterval = 2000 // La frecuencia más rápida en la que se recibirán actualizaciones
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions
            val REQUEST_CODE_LOCATION = 1
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_LOCATION)
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    //--------- METODO RELACIONADO CON LA CAMARA DEL MAPA ------------//
    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    // Si no es la carga inicial del mapa, no centrar automáticamente
                    if (!isInitialMapLoad) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        // Mover la cámara a la ubicación actual y hacer zoom
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
                    }
                }
            }
        }
    }

    //------------- METODO AL DAR CLIC EN EL MAPA -----------//
    override fun onMapClick(latLng: LatLng) {
        // Clear existing markers
        mMap.clear()

        // Get the address using Geocoding
        val context = requireContext()
        val geocoder = Geocoder(context.applicationContext)
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        val address = addresses!![0]
        val calle = address.getAddressLine(0).substring(0, address.getAddressLine(0).indexOf(","))
        var i = calle.length - 1
        while (i >= 0) {
            if (!calle[i].isDigit()) {
                break
            }
            i--
        }
        val nuevaCalle = calle.substring(0, i + 1)
        val colonia = address.locality
        val codigoPostal = address.postalCode

        //------- REDONDEAMOS LOS VALORES PARA QUE COINCIDAN CON 
        latitud = latLng.latitude.roundTo(8)
        longitud = latLng.longitude.roundTo(8)

        // Se asignan los datos a los campos de texto
        binding.etCalle.setText(nuevaCalle)
        binding.etColonia.setText(colonia)
        binding.etCodigoPostal.setText(codigoPostal)

        // Add a marker at the clicked location
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title(calle)
            .snippet("$colonia")

        mMap.addMarker(markerOptions)

    }
    fun Double.roundTo(decimals: Int): Double {
        val factor = 10.0.pow(decimals.toDouble())
        return (this * factor).roundToLong() / factor
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detener las actualizaciones de ubicación cuando el fragmento se destruye
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM = 15f
    }

    override fun obtenerDatos(): Map<String, String> {
        val datos = mapOf(
            "calle" to binding.etCalle.text.toString(),
            "codigoPostal" to binding.etCodigoPostal.text.toString(),
            "colonia" to binding.etColonia.text.toString(),
            "numero" to binding.etNumeroCasa.text.toString(),
            "longitud" to longitud.toString(),
            "latitud" to latitud.toString(),
            "idMunicipio" to idMunicipioSeleccionado.toString()


        )
        return datos as Map<String, String>
    }

    override fun validarCamposLlenos(): Boolean {
        var esCorrecto = true
        if (binding.etCalle.text.isNullOrBlank()){
            esCorrecto = false
            binding.etCalle.error = "Ingresa el nombre de tu calle"
        }
        if (binding.etColonia.text.isNullOrBlank()){
            esCorrecto = false
            binding.etColonia.error = "Ingresa tu colonia"
        }
        if (binding.etCodigoPostal.text.isNullOrBlank()){
            esCorrecto = false
            binding.etCodigoPostal.error = "Ingresa tu Código Postal"
        }
        if (binding.etNumeroCasa.text.isNullOrBlank()){
            esCorrecto = false
            binding.etNumeroCasa.error = "Ingresa el numero de tu casa"
        }
        return esCorrecto
    }

    override fun validarPassword(): Boolean {
        TODO("Not yet implemented")
    }

    override fun obtenerFragmentId(): Int {
        return 2
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id){
            R.id.sp_estado->{
                llenarMunicipios(estados[position].idEstado)
            }
            R.id.sp_municipio ->{
                idMunicipioSeleccionado =municipios[position].idMunicipio
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
