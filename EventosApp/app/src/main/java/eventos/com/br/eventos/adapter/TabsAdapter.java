package eventos.com.br.eventos.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.fragments.EventosFragment;
import eventos.com.br.eventos.fragments.ProximosEventosFragment;
import eventos.com.br.eventos.util.TipoBusca;

public class TabsAdapter extends FragmentPagerAdapter {
    private Context context;

    public TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.opcao1);
        }
        return context.getString(R.string.opcao2);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == 0) {
            f = new ProximosEventosFragment();
        } else if (position == 1) {
            f = EventosFragment.newInstance(TipoBusca.FAVORITOS);
        }
        return f;
    }
}
