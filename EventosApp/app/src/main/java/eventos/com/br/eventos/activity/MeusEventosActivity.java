package eventos.com.br.eventos.activity;

import android.content.Intent;
import android.os.Bundle;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.fragments.EventosFragment;
import eventos.com.br.eventos.util.TipoBusca;

public class MeusEventosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_eventos);
        setUpToolbar();
        setupNavDrawer();

        if (savedInstanceState == null) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("tipoDeBusca", TipoBusca.USUARIO);

            EventosFragment frag = new EventosFragment();
            frag.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().add(R.id.eventosFragment, frag).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == MainActivity.RECRIAR_ACTIVITY){
            startActivity(new Intent(getContext(), MeusEventosActivity.class));
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
