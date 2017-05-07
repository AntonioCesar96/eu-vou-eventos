package eventos.com.br.eventos.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import eventos.com.br.eventos.R;

public class BuscaLocalActivity extends BaseActivity {
    int PLACE_PICKER_REQUEST = 1;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_local);
        setUpToolbar();
        setUpNavigation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(onMapReadyCallback());


        // Places
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: obter informações sobre o local selecionado.
                Log.e("TAG", "Place: " + place.getName());
                Log.e("TAG", "Place: " + place.getAddress());
                Log.e("TAG", "Place: " + place.getAttributions());
                Log.e("TAG", "Place: " + place.getLatLng());
                Log.e("TAG", "Place: " + place.getPhoneNumber());
            }

            @Override
            public void onError(Status status) {
                // TODO: Solucionar o erro.
                Log.i("TAG", "Ocorreu um erro: " + status);
            }
        });
    }

    private OnMapReadyCallback onMapReadyCallback() {
        return new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                // Configura o tipo do mapa
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                // Localização do mapa (Av. Paulista - SP)
                LatLng latLng = new LatLng(-23.564224, -46.653156);
                LatLng latLng2 = new LatLng(-23.555696, -46.662627);

                final CameraPosition position = new CameraPosition.Builder()
                        .target(latLng)    // Localização
                        .bearing(130)        // Direção em que a cÂmera está apontando em graus
                        .tilt(0)            // Ângulo que a cÂmera está posicionada em graus (0 a 90)
                        .zoom(17)            // Zoom
                        .build();

                CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);

                // Centraliza o mapa com animação de 10 segundos
                mMap.animateCamera(update);

                // Eventos
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        CameraUpdate update = CameraUpdateFactory.newLatLng(latLng);
                        Toast.makeText(getBaseContext(), "Click: " + latLng, Toast.LENGTH_SHORT).show();
                        mMap.animateCamera(update);
                    }
                });

                // Adiciona os marcadores
                adicionarMarcador(mMap, latLng);
                adicionarMarcador(mMap, latLng2);

                // Localização
                LivroAndroidLocationSource locationSource = new LivroAndroidLocationSource();
                mMap.setMyLocationEnabled(true);
                mMap.setLocationSource(locationSource);
                locationSource.setLocation(latLng);
            }
        };
    }

    // Adiciona um marcador
    private void adicionarMarcador(GoogleMap map, LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title("Meu Marcador").snippet("Livro Android");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        Marker marker = map.addMarker(markerOptions);

//		map.setOnMarkerClickListener(new OnMarkerClickListener() {
//			@Override
//			public boolean onMarkerClick(Marker marker) {
//				LatLng lartLng = marker.getPosition();
//				Toast.makeText(getBaseContext(), "Marcador: " + marker.getTitle() + " > " + lartLng, Toast.LENGTH_SHORT).show();
//				return false;
//			}
//		});

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng lartLng = marker.getPosition();
                Toast.makeText(getBaseContext(), "Clicou no: " + marker.getTitle() + " > " + lartLng, Toast.LENGTH_SHORT).show();
            }
        });

        // Customiza a janela ao clicar em um marcador
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                LinearLayout linear = (LinearLayout) this.getInfoContents(marker);
                // Borda imagem 9-patch
                linear.setBackgroundResource(R.drawable.janela_marker);
                return linear;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // View com o conteúdo
                LinearLayout linear = new LinearLayout(getBaseContext());
                linear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linear.setOrientation(LinearLayout.VERTICAL);

                TextView t = new TextView(getBaseContext());
                t.setText("*** View customizada *** ");
                t.setTextColor(Color.BLACK);
                t.setGravity(Gravity.CENTER);
                linear.addView(t);

                TextView tTitle = new TextView(getBaseContext());
                tTitle.setText(marker.getTitle());
                tTitle.setTextColor(Color.RED);
                linear.addView(tTitle);

                TextView tSnippet = new TextView(getBaseContext());
                tSnippet.setText(marker.getSnippet());
                tSnippet.setTextColor(Color.BLUE);
                linear.addView(tSnippet);

                return linear;
            }
        });
    }

    public void selecionarLocal(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}