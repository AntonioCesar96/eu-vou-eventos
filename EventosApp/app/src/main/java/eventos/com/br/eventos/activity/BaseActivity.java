package eventos.com.br.eventos.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.dao.DataBaseHelper;
import eventos.com.br.eventos.model.Usuario;
import eventos.com.br.eventos.dao.UsuarioDAO;
import eventos.com.br.eventos.util.AlertUtils;
import eventos.com.br.eventos.util.ImageUtils;

public class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    // Configura a Toolbar
    protected void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    // Configura seta de voltar
    public void setUpNavigation() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // Configura o Nav Drawer
    protected void setupNavDrawer() {
        // Drawer Layout
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Ícone do menu do nav drawer
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            if (navigationView != null && drawerLayout != null) {
                // Atualiza os dados do header do Navigation View
                configNav(navigationView);
            }
        }
    }

    private void configNav(NavigationView navigationView) {
        Usuario usuario = null;
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
            UsuarioDAO dao = new UsuarioDAO(dataBaseHelper.getConnectionSource());
            usuario = dao.getUsuario();
        } catch (SQLException e) {
            Log.i("Error", e.getMessage());
        }

        if (usuario != null) {
            usuarioLogado(navigationView, usuario);
        } else {
            usuarioSemLogin(navigationView);
        }
    }

    private void usuarioSemLogin(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);

        if (headerView == null) {
            navigationView.inflateHeaderView(R.layout.nav_drawer_header);
            headerView = navigationView.getHeaderView(0);
        } else {
            navigationView.removeHeaderView(headerView);
            navigationView.getMenu().clear();

            navigationView.inflateHeaderView(R.layout.nav_drawer_header);
            headerView = navigationView.getHeaderView(0);
        }

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void usuarioLogado(NavigationView navigationView, final Usuario usuario) {
        View headerView = navigationView.getHeaderView(0);

        if (headerView == null) {
            navigationView.inflateHeaderView(R.layout.nav_drawer_header_logado);
            navigationView.inflateMenu(R.menu.nav_drawer_menu);

            headerView = navigationView.getHeaderView(0);
        } else {

            navigationView.removeHeaderView(headerView);
            navigationView.getMenu().clear();

            navigationView.inflateHeaderView(R.layout.nav_drawer_header_logado);
            navigationView.inflateMenu(R.menu.nav_drawer_menu);

            headerView = navigationView.getHeaderView(0);
        }

        if (!usuario.isAdministrador()) {
            Menu menu = navigationView.getMenu();
            menu.removeItem(R.id.c_evento);
            menu.removeItem(R.id.l_eventos);
        }

        TextView tNome = (TextView) headerView.findViewById(R.id.tNome);
        TextView tEmail = (TextView) headerView.findViewById(R.id.tEmail);
        ImageView imgView = (ImageView) headerView.findViewById(R.id.img);

        tNome.setText(usuario.getNome());
        tEmail.setText(usuario.getEmail());
        ImageUtils.setImagemPerfil(getContext(), usuario.getFotoPerfil(), imgView);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PhotoViewActivity.class);
                intent.putExtra("url", usuario.getFotoPerfil());
                startActivity(intent);
            }
        });

        // Trata o evento de clique no menu.
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Seleciona a linha
                        menuItem.setChecked(true);
                        // Fecha o menu
                        drawerLayout.closeDrawers();
                        // Trata o evento do menu
                        switch (menuItem.getItemId()) {
                            case R.id.c_evento:
                                startActivity(new Intent(getAppCompatActivity(), EventoPUActivity.class));
                                break;
                            case R.id.l_eventos:
                                startActivity(new Intent(getAppCompatActivity(), MeusEventosActivity.class));
                                break;
                            case R.id.l_sair:
                                finalizarSessao();
                                break;
                            case R.id.l_config:
                                startActivity(new Intent(getAppCompatActivity(), AlterarDadosUsuarioActivity.class));
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void finalizarSessao() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Fazer logout");
        builder.setMessage("Você realmente deseja sair?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                    UsuarioDAO dao = new UsuarioDAO(dataBaseHelper.getConnectionSource());
                    dao.deletar();
                    usuarioSemLogin(navigationView);
                } catch (SQLException e) {
                    Log.i("Error", e.getMessage());
                }

                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Adiciona o fragment no centro da tela
    protected void replaceFragment(Fragment frag) {
        toast("OK!");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Trata o clique no botão que abre o menu.
                if (drawerLayout != null) {
                    openDrawer();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    // Abre o menu lateral
    protected void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    // Fecha o menu lateral
    protected void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    protected AppCompatActivity getAppCompatActivity() {
        return this;
    }


    private static final String TAG = BaseActivity.class.getSimpleName();

    protected Context getContext() {
        return this;
    }

    protected AppCompatActivity getActivity() {
        return this;
    }

    protected void log(String msg) {
        Log.d(TAG, msg);
    }

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void toast(int msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void alert(String msg) {
        AlertUtils.alert(this, msg);
    }

    protected void alert(String title, String msg) {
        AlertUtils.alert(this, title, msg);
    }

    protected void alert(int msg) {
        AlertUtils.alert(this, getString(msg));
    }

    protected void alert(int title, int msg) {
        AlertUtils.alert(this, getString(title), getString(msg));
    }

    protected void snack(View view, int msg, Runnable runnable) {
        this.snack(view, this.getString(msg), runnable);
    }

    protected void snack(View view, int msg) {
        this.snack(view, this.getString(msg), (Runnable) null);
    }

    protected void snack(View view, String msg) {
        this.snack(view, msg, (Runnable) null);
    }

    protected void snack(View view, String msg, final Runnable runnable) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
            public void onClick(View v) {
                if (runnable != null) {
                    runnable.run();
                }

            }
        }).show();
    }
}
