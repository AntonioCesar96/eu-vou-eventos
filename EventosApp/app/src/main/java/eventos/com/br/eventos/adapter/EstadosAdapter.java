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
import eventos.com.br.eventos.model.Estado;

/**
 * Created by antonio on 25/02/17.
 */

public class EstadosAdapter extends BaseAdapter {
    private final Context context;
    private final List<Estado> estados;

    public EstadosAdapter(Context context, List<Estado> estados) {
        this.context = context;
        this.estados = estados;
    }

    @Override
    public int getCount() {
        return this.estados != null ? this.estados.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return this.estados != null ? this.estados.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return this.estados != null ? this.estados.get(i).getId() : 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Estado estado = estados.get(i);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, viewGroup, false);


        TextView text = (TextView) inflate.findViewById(android.R.id.text1);
        text.setText(estado.getNome());
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        if (i == 0) {
            text.setTextColor(ContextCompat.getColor(context, R.color.cinzaBBB));
        }

        return inflate;
    }
}
