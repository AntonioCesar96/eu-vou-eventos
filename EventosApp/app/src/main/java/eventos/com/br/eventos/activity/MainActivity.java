package eventos.com.br.eventos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.sql.SQLException;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.adapter.TabsAdapter;
import eventos.com.br.eventos.dao.FiltroDAO;
import eventos.com.br.eventos.fragments.FilterEventosDialog;
import eventos.com.br.eventos.model.Filtro;
import livroandroid.lib.utils.Prefs;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setupViewPagerTabs();

        // FAB
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack(v, "Exemplo de FAB Button.");
            }
        });
    }

    @Override
    protected void onResume() {
        setupNavDrawer();
        super.onResume();
    }

    // Configura o ViewPager + Tabs
    private void setupViewPagerTabs() {
        // ViewPager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new TabsAdapter(getContext(), getSupportFragmentManager()));
        // Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        // Cria as tabs com o mesmo adapter utilizado pelo ViewPager
        tabLayout.setupWithViewPager(viewPager);
        int cor = ContextCompat.getColor(getContext(), R.color.white);
        // Cor branca no texto (o fundo azul foi definido no layout)
        tabLayout.setTabTextColors(cor, cor);

        // Ao criar a view, mostra a última tab selecionada
        int tabIdx = Prefs.getInteger(getContext(), "tabIdx");

        viewPager.setCurrentItem(tabIdx);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // Salva o índice da página/tab selecionada
                Prefs.setInteger(getContext(), "tabIdx", viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.editar_config_ws) {
            Intent intent = new Intent(getApplicationContext(), ConfiguracaoActivity.class);
            startActivity(intent);

            return true;
        }

        if (id == R.id.filter_eventos) {
            openDialogFilter();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDialogFilter() {
        FilterEventosDialog.show(getSupportFragmentManager(), getActivity(), new FilterEventosDialog.Callback() {
            @Override
            public void onFilter(Filtro filtro) {

                try {
                    FiltroDAO dao = new FiltroDAO();
                    dao.save(filtro);

                    startActivity(new Intent(getContext(), MainActivity.class));
                    finish();
                } catch (SQLException e) {

                }
            }
        });
    }
}
