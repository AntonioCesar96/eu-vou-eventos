package eventos.com.br.eventos.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.model.Cidade;

/**
 * Created by antonio on 25/02/17.
 */

public class CidadesAdapter extends BaseAdapter {
    private Context context;
    private List<Cidade> cidades;

    public CidadesAdapter(Context context, List<Cidade> cidades) {
        this.context = context;
        this.cidades = cidades;
    }

    @Override
    public int getCount() {
        return this.cidades != null ? this.cidades.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return this.cidades != null ? this.cidades.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return this.cidades != null && this.cidades.size() > 0 ? this.cidades.get(i).getId() : 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Cidade cidade = cidades.get(i);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, viewGroup, false);

        TextView text = (TextView) inflate.findViewById(android.R.id.text1);
        text.setText(cidade.getNome());
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        if (i == 0) {
            text.setTextColor(ContextCompat.getColor(context, R.color.color_black_transparente_96));
        }

        return inflate;
    }

    public void updateSpinner(List<Cidade> cidadeList) {
        this.cidades = cidadeList;
        notifyDataSetChanged();
    }
}
