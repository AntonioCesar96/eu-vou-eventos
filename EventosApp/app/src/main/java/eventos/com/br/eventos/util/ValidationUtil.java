package eventos.com.br.eventos.util;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

            View selectedView = spFaculdades.getSelectedView();
            if (Long.MAX_VALUE == f.getId()) {

                if (selectedView != null && selectedView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView;
                    selectedTextView.setError("Selecione uma faculdade");
                    spFaculdades.setFocusable(true);
                    spFaculdades.requestFocus();
                    Toast.makeText(spFaculdades.getContext(), "Selecione uma faculdade", Toast.LENGTH_SHORT).show();
                }

                return false;
            } else {
                if (selectedView != null && selectedView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView;
                    selectedTextView.setError(null);
                }
            }
        }
        return true;
    }

    public static boolean validaSpinnerCidade(Spinner spCidades) {
        Object obj = spCidades.getSelectedItem();

        if (obj != null && obj instanceof Cidade) {
            Cidade c = (Cidade) obj;

            View selectedView = spCidades.getSelectedView();
            if (Long.MAX_VALUE == c.getId()) {

                if (selectedView != null && selectedView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView;
                    selectedTextView.setError("Selecione uma cidade");
                    spCidades.setFocusable(true);
                    spCidades.requestFocus();
                    Toast.makeText(spCidades.getContext(), "Selecione uma cidade", Toast.LENGTH_SHORT).show();
                }

                return false;
            } else {
                if (selectedView != null && selectedView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView;
                    selectedTextView.setError(null);
                }
            }
        }
        return true;
    }

    public static boolean validaSpinnerEstado(Spinner spEstados) {
        Object obj = spEstados.getSelectedItem();

        if (obj != null && obj instanceof Estado) {
            Estado e = (Estado) obj;
            View selectedView = spEstados.getSelectedView();

            if (Long.MAX_VALUE == e.getId()) {

                if (selectedView != null && selectedView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView;
                    selectedTextView.setError("Selecione um estado");
                    spEstados.setFocusable(true);
                    spEstados.requestFocus();
                    Toast.makeText(spEstados.getContext(), "Selecione um estado", Toast.LENGTH_SHORT).show();
                }
                return false;
            } else {
                if (selectedView != null && selectedView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView;
                    selectedTextView.setError(null);
                }
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
                } else {
                    edText.setError(null);
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
            } else {
                edText.setError(null);
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
            } else {
                edText.setError(null);
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
            } else {
                edText.setError(null);
            }
        }

        return true;
    }

    public static boolean validateDate(Button edText) {

        CharSequence text = edText.getText();


        if (text != null) {
            String textoInput = text.toString();

            // Valida campo DATE
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                format.parse(textoInput);
            } catch (ParseException pe) {
                edText.setError("Formato de data inválido");
                edText.requestFocus();
                Toast.makeText(edText.getContext(), "Defina a data do evento", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        edText.setError(null);
        return true;
    }

    public static boolean validateTime(Button edText) {

        CharSequence text = edText.getText();
        if (text != null) {
            String textoInput = text.toString();

            // Valida campo DATE TIME
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            try {
                format.parse(textoInput);
            } catch (ParseException pe) {
                edText.setError("Formato de hora inválido");
                edText.requestFocus();
                Toast.makeText(edText.getContext(), "Defina o horário do evento", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        edText.setError(null);
        return true;
    }
}
