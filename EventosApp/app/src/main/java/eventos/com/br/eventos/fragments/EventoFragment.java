package eventos.com.br.eventos.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.config.EventosApplication;
import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.dao.EventoDAO;
import eventos.com.br.eventos.dao.NotificacaoDAO;
import eventos.com.br.eventos.model.Evento;
import eventos.com.br.eventos.model.Notificacao;
import eventos.com.br.eventos.tasks.BuscarEventoTask;
import eventos.com.br.eventos.tasks.DownloadImagemTask;
import eventos.com.br.eventos.tasks.MapaTask;
import eventos.com.br.eventos.util.AlarmEventoUtil;

import static eventos.com.br.eventos.R.id.map;

public class EventoFragment extends BaseFragment {

    private GoogleMap mMap;
    private Evento evento;
    private TextView txtDesc, txtCep, txtCidade;
    private TextView txtLocal;
    private TextView txtRuaMostra;
    private TextView txtNumeroMostra;
    private TextView txtLocalBairro;
    private TextView txtDataEvento;
    private FloatingActionButton fabFavorito;
    public boolean flagClickFab;
    private DataBaseHelper dataBaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evento, container, false);

        dataBaseHelper = EventosApplication.getInstance().getDataBaseHelper();
        evento = (Evento) getArguments().getSerializable("evento");
        setHasOptionsMenu(true);

        initFields(view);
        configEventoFavoritado(evento);

        return view;
    }

    private void configEventoFavoritado(Evento evento) {
        try {
            EventoDAO eventoDAO = new EventoDAO(dataBaseHelper.getConnectionSource());
            Evento eventoBanco = eventoDAO.getById(evento.getId());

            if (eventoBanco != null) {
                flagClickFab = true;
                fabFavorito.setImageResource(R.drawable.ic_turned_in_not_padding);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void initFields(View view) {
        txtDesc = (TextView) view.findViewById(R.id.txtDesc);
        txtLocal = (TextView) view.findViewById(R.id.txtLocal);
        txtDataEvento = (TextView) view.findViewById(R.id.txtDataEvento);
        txtRuaMostra = (TextView) view.findViewById(R.id.txtRuaMostra);
        txtNumeroMostra = (TextView) view.findViewById(R.id.txtNumeroMostra);
        txtLocalBairro = (TextView) view.findViewById(R.id.txtLocalBairro);
        txtCep = (TextView) view.findViewById(R.id.txtCep);
        txtCidade = (TextView) view.findViewById(R.id.txtCidade);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buscaEvento();
    }

    private void buscaEvento() {
        new BuscarEventoTask(getAppCompatActivity(), onCallbackBuscarEvento()).execute(evento.getId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_evento, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_share) {
            compartilharEvento();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void compartilharEvento() {
        new DownloadImagemTask(getAppCompatActivity(), onCallbackDownloadImagem()).execute(evento.getEnderecoImagem());
    }

    public DownloadImagemTask.CallbackDownloadImagem onCallbackDownloadImagem() {
        return new DownloadImagemTask.CallbackDownloadImagem() {
            @Override
            public void onCallbackDownloadImagem(File imagem) {

                Uri uri = Uri.fromFile(imagem);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                getAppCompatActivity().startActivity(Intent.createChooser(shareIntent, "Compartilhar Evento"));
            }
        };
    }

    public void setFabButton(FloatingActionButton fabFavorito) {
        this.fabFavorito = fabFavorito;
        this.fabFavorito.setOnClickListener(clickFabFavoritoSemBuscarEventoDoWebService());
    }

    private View.OnClickListener clickFabFavoritoSemBuscarEventoDoWebService() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Não é possível favoritar enquanto o evento não for totalmente carregado",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    private BuscarEventoTask.CallbackBuscarEvento onCallbackBuscarEvento() {
        return new BuscarEventoTask.CallbackBuscarEvento() {
            @Override
            public void onCallbackBuscarEvento(Evento e) {
                if (e != null) {
                    evento = e;

                    SimpleDateFormat formatData = new SimpleDateFormat("EEE',' dd 'de' MMMM 'às' HH:mm", Locale.getDefault());
                    String data = formatData.format(evento.getDataHora().getTime()).toUpperCase();

                    txtDataEvento.setText(data);
                    txtDesc.setText(evento.getDescricao());
                    txtLocal.setText(evento.getLocal().getNome());
                    txtRuaMostra.setText(evento.getLocal().getRua());
                    txtNumeroMostra.setText(evento.getLocal().getNumero());
                    txtLocalBairro.setText(evento.getLocal().getBairro());
                    txtCep.setText(evento.getLocal().getCep());

                    if (evento.getLocal().getCidade() != null) {
                        txtCidade.setText(evento.getLocal().getCidade().getNome());
                    }

                    fabFavorito.setOnClickListener(clickFabFavorito());

                    inicializarMapa(evento);
                }
            }
        };
    }

    private void inicializarMapa(Evento evento) {

        /*
        if (evento.getLocal().getLatitude() != null) {
            Double v1 = Double.parseDouble(evento.getLocal().getLatitude());
            Double v2 = Double.parseDouble(evento.getLocal().getLongitude());
            LatLng latLng = new LatLng(v1, v2);

            mostrarMapa(latLng);
        } else {
        */

        new MapaTask(getAppCompatActivity(), onMapCallback()).execute(evento);
    }

    private MapaTask.CallbackMap onMapCallback() {
        return new MapaTask.CallbackMap() {
            @Override
            public void onCallbackMap(LatLng latLng) {
                mostrarMapa(latLng);
            }
        };
    }


    private View.OnClickListener clickFabFavorito() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flagClickFab) {
                    fabFavorito.setImageResource(R.drawable.ic_turned_in_not_not_padding);
                    flagClickFab = false;
                    desFavoritar();
                    return;
                }

                fabFavorito.setImageResource(R.drawable.ic_turned_in_not_padding);
                flagClickFab = true;
                favoritar();
            }
        };
    }

    private void favoritar() {
        try {
            EventoDAO eventoDAO = new EventoDAO(dataBaseHelper.getConnectionSource());
            eventoDAO.save(evento);

            agendarAlarm(evento.getId(), evento.getDataHora());

            Toast.makeText(getContext(), evento.getNome() + " adicionado aos favoritos", Toast.LENGTH_SHORT).show();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void desFavoritar() {
        try {
            EventoDAO eventoDAO = new EventoDAO(dataBaseHelper.getConnectionSource());
            eventoDAO.remove(evento);

            cancelarAlarm(evento.getId());

            Toast.makeText(getContext(), evento.getNome() + " removido dos favoritos", Toast.LENGTH_SHORT).show();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void agendarAlarm(Long idEvento, Calendar data) {

        long diaNotificacao = pegarMilisegundosDiaDaNotificacao(data);

        try {
            Notificacao n = criarNotificacao(idEvento, diaNotificacao);
            new NotificacaoDAO(dataBaseHelper.getConnectionSource()).save(n);

            AlarmEventoUtil alarmEventoUtil = new AlarmEventoUtil(getContext(), dataBaseHelper);
            alarmEventoUtil.agendarAlarm(idEvento, diaNotificacao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelarAlarm(Long idEvento) {
        AlarmEventoUtil alarmEventoUtil = new AlarmEventoUtil(getContext(), dataBaseHelper);
        alarmEventoUtil.cancelarAlarm(idEvento);
    }

    private Notificacao criarNotificacao(Long idEvento, long dia) {
        Notificacao n = new Notificacao();
        n.setDataEvento(dia);
        n.setIdEvento(idEvento);
        n.setPrimeiraNotificacao(true);
        return n;
    }

    private long pegarMilisegundosDiaDaNotificacao(Calendar data) {

        if (data != null) {
            data.add(Calendar.DAY_OF_MONTH, -1);

            Log.e("DATA", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data.getTime()));

            return data.getTimeInMillis();
        }

        return 0;
    }

    private void mostrarMapa(LatLng latLng) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(onMapReadyCallback(latLng));
    }


    private OnMapReadyCallback onMapReadyCallback(final LatLng latLng) {
        return new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                if (latLng != null) {

                    // Configura o tipo do mapa
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    final CameraPosition position = new CameraPosition.Builder()
                            .target(latLng)    // Localização
                            .bearing(130)        // Direção em que a cÂmera está apontando em graus
                            .tilt(0)            // Ângulo que a cÂmera está posicionada em graus (0 a 90)
                            .zoom(17)            // Zoom
                            .build();

                    CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);

                    // Centraliza o mapa com animação de 10 segundos
                    mMap.animateCamera(update);

                    // Adiciona os marcadores
                    adicionarMarcador(mMap, latLng);
                    return;
                }
                Toast.makeText(getContext(), "Não foi possível localizar o local onde o evento acontecerá", Toast.LENGTH_SHORT).show();
            }
        };
    }

    // Adiciona um marcador
    private void adicionarMarcador(GoogleMap map, LatLng latLng) {
        SimpleDateFormat formatData = new SimpleDateFormat("EEE',' dd 'de' MMMM 'às' HH:mm", Locale.getDefault());
        String data = formatData.format(evento.getDataHora().getTime()).toUpperCase();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title(data);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        Marker marker = map.addMarker(markerOptions);

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
                LinearLayout linear = new LinearLayout(getContext());
                linear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linear.setOrientation(LinearLayout.VERTICAL);

                TextView t = new TextView(getContext());
                t.setText(evento.getNome());
                t.setTextColor(Color.BLACK);
                t.setGravity(Gravity.CENTER);
                linear.addView(t);

                TextView tTitle = new TextView(getContext());
                tTitle.setText(marker.getTitle());
                tTitle.setTextColor(Color.BLACK);
                linear.addView(tTitle);

                return linear;
            }
        });
    }
}

