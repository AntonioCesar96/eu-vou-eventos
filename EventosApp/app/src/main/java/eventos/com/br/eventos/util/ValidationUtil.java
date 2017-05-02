package eventos.com.br.eventos.util;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import eventos.com.br.eventos.model.Cidade;
import eventos.com.br.eventos.model.Estado;
import eventos.com.br.eventos.model.Faculdade;

/**
 * Created by antonio on 27/02/17.
 */

public class ValidationUtil {

    public static boolean validaSpinnerFaculdade(Spinner spFaculdades) {
        Object obj = spFaculdades.getSelectedItem();

        if (obj != null && obj instanceof Faculdade) {
            Faculdade f = (Faculdade) obj;

            if (Long.MAX_VALUE == f.getId()) {

                View selectedView = spFaculdades.getSelectedView();
                if (selectedView != null && selectedView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView;
                    selectedTextView.setError("Selecione uma faculdade");
                    spFaculdades.setFocusable(true);
                    spFaculdades.requestFocus();
                }

                return false;
            }
        }
        return true;
    }

    public static boolean validaSpinnerCidade(Spinner spCidades) {
        Object obj = spCidades.getSelectedItem();

        if (obj != null && obj instanceof Cidade) {
            Cidade c = (Cidade) obj;

            if (Long.MAX_VALUE == c.getId()) {

                View selectedView = spCidades.getSelectedView();
                if (selectedView != null && selectedView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView;
                    selectedTextView.setError("Selecione uma cidade");
                    spCidades.setFocusable(true);
                    spCidades.requestFocus();
                }

                return false;
            }
        }
        return true;
    }

    public static boolean validaSpinnerEstado(Spinner spEstados) {
        Object obj = spEstados.getSelectedItem();

        if (obj != null && obj instanceof Estado) {
            Estado e = (Estado) obj;

            if (Long.MAX_VALUE == e.getId()) {

                View selectedView = spEstados.getSelectedView();
                if (selectedView != null && selectedView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView;
                    selectedTextView.setError("Selecione um estado");
                    spEstados.setFocusable(true);
                    spEstados.requestFocus();
                }

                return false;
            }
        }
        return true;
    }

    public static boolean validaSpinnerEstadoFiltro(Spinner spEstados) {
        Object obj = spEstados.getSelectedItem();

        if (obj != null && obj instanceof Estado) {
            Estado e = (Estado) obj;

            if (Long.MAX_VALUE == e.getId()) {
                return true;
            }
        }
        return false;
    }

    public static boolean validaSpinnerFaculdadeFiltro(Spinner spFaculdades) {
        Object obj = spFaculdades.getSelectedItem();

        if (obj != null && obj instanceof Faculdade) {
            Faculdade f = (Faculdade) obj;

            if (Long.MAX_VALUE == f.getId()) {

                return false;
            }
        }
        return true;
    }

    public static boolean validateNotNull(List<EditText> editTexts) {
        for (EditText edText : editTexts) {

            Editable text = edText.getText();
            if (text != null) {
                String textoInput = text.toString();

                // Valida campo vazio
                if (TextUtils.isEmpty(textoInput)) {

                    edText.setError("Campo não pode estar vazio");
                    edText.setFocusable(true);
                    edText.requestFocus();
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean validateName(EditText edText) {

        Editable text = edText.getText();
        if (text != null) {
            String textoInput = text.toString();

            if (!(textoInput.length() >= 3)) {
                edText.setError("Campo deve conter no minímo 3 caracteres");
                edText.setFocusable(true);
                edText.requestFocus();
                return false;
            }
        }

        return true;
    }

    public static boolean validateEmail(EditText edText) {

        Editable text = edText.getText();
        if (text != null) {
            String textoInput = text.toString();

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(textoInput).matches()) {
                edText.setError("E-mail inválido");
                edText.setFocusable(true);
                edText.requestFocus();
                return false;
            }
        }

        return true;
    }

    public static boolean validateSenha(EditText edText) {

        Editable text = edText.getText();
        if (text != null) {
            String textoInput = text.toString();

            if (!(textoInput.length() >= 6)) {
                edText.setError("Senha deve conter no minímo 6 caracteres");
                edText.setFocusable(true);
                edText.requestFocus();
                return false;
            }
        }

        return true;
    }

    public static boolean validateDate(EditText edText) {

        Editable text = edText.getText();


        if (text != null) {
            String textoInput = text.toString();

            // Valida campo DATE
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                format.parse(textoInput);
            } catch (ParseException pe) {
                edText.setError("Formato de data inválido");
                edText.setFocusable(true);
                edText.requestFocus();
                return false;
            }
        }

        edText.setError(null);
        edText.setFocusable(false);
        return true;
    }

    public static boolean validateTime(EditText edText) {

        Editable text = edText.getText();
        if (text != null) {
            String textoInput = text.toString();

            // Valida campo DATE TIME
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            try {
                format.parse(textoInput);
            } catch (ParseException pe) {
                edText.setError("Formato de hora inválido");
                edText.setFocusable(true);
                edText.requestFocus();
                return false;
            }
        }

        edText.setError(null);
        edText.setFocusable(false);
        return true;
    }
}
