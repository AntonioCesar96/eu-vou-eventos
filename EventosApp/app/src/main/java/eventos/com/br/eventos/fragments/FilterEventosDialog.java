package eventos.com.br.eventos.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eventos.com.br.eventos.R;

public class FilterEventosDialog extends DialogFragment {
    private Context context;
    private Callback callback;
    private TextView tNome;

    // Método utilitário para criar o dialog
    public static void show(FragmentManager fm, Context context, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("filter_eventos");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        FilterEventosDialog frag = new FilterEventosDialog();
        frag.callback = callback;
        frag.context = context;

        frag.show(ft, "filter_eventos");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }

        // Atualiza o tamanho do dialog
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final int width = metrics.widthPixels;
        final int height = metrics.heightPixels;

        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(width, width);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_filter, container, false);

        view.findViewById(R.id.btnFiltrar).setOnClickListener(onClickAtualizar());

        tNome = (TextView) view.findViewById(R.id.tNome);


        return view;
    }

    private View.OnClickListener onClickAtualizar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();

                if (callback != null) {
                    callback.onFilter();
                }

                // Fecha o DialogFragment
                dismiss();
            }
        };
    }

    // Interface para retornar o resultado
    public interface Callback {
        void onFilter();
    }
}
