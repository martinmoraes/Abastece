package br.com.appviral.abastece;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.appviral.abastece.Adaptador.AdaptadorAbastecimento;
import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Fragment.AbastecimentoFragment;

@SuppressWarnings("deprecation")
public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AbastecimentoFragment abastecimentoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setItemIconTintList(null);
        navigationView.getMenu().findItem(R.id.nav_abastecimentos).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);


        // FRAGMENT
        criaAbreFragmento("nav_abastecimentos", AdaptadorAbastecimento.COM_CLICK);
    }

    private void criaAbreFragmento(String tag, boolean incluirClick) {
        abastecimentoFragment = (AbastecimentoFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (abastecimentoFragment == null) {
            abastecimentoFragment = AbastecimentoFragment.newInstance(incluirClick, tag);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rl_fragment_container, abastecimentoFragment, tag);
        ft.commit();
    }


    public void abreRegistraAbastecimento(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistraAbastecimentoActivity.class);
        intent.putExtra("OPERACAO", Abastecimento.INSERIR);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sobre) {
            startActivity(new Intent(getApplicationContext(), SobreActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //   @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_abastecimentos:
                criaAbreFragmento("nav_abastecimentos", AdaptadorAbastecimento.COM_CLICK);
                break;
            case R.id.nav_sobre:
                startActivity(new Intent(getApplicationContext(), SobreActivity.class));
                break;
            case R.id.nav_mesal:
                criaAbreFragmento("nav_mesal", AdaptadorAbastecimento.SEM_CLICK);
                break;
            case R.id.nav_bimestral:
                criaAbreFragmento("nav_bimestral", AdaptadorAbastecimento.SEM_CLICK);
                break;
            case R.id.nav_trimestral:
                criaAbreFragmento("nav_trimestral", AdaptadorAbastecimento.SEM_CLICK);
                break;
            case R.id.nav_semestral:
                criaAbreFragmento("nav_semestral", AdaptadorAbastecimento.SEM_CLICK);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
