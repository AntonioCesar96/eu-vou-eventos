package eventos.com.br.eventos.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.fragments.MeusEventosFragment;
import eventos.com.br.eventos.util.AndroidUtils;

public class MeusEventosActivity extends BaseActivity {
    private SwipeRefreshLayout swipeLayout;
    MeusEventosFragment meusEventosFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_eventos);
        setUpToolbar();
        setupNavDrawer();

        if (savedInstanceState == null) {
            meusEventosFragment = new MeusEventosFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.eventosFragment, meusEventosFragment).commit();
        }

        // Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (AndroidUtils.isNetworkAvailable(getContext())) {

                    getSupportFragmentManager().beginTransaction().remove(meusEventosFragment).commit();
                    meusEventosFragment = new MeusEventosFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.eventosFragment, meusEventosFragment).commit();

                    swipeLayout.setRefreshing(false);
                } else {
                    swipeLayout.setRefreshing(false);
                    //snack(recyclerView, R.string.msg_error_conexao_indisponivel);
                }
            }
        };
    }
}
