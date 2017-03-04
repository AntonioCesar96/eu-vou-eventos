package eventos.com.br.eventos.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import eventos.com.br.eventos.R;
import eventos.com.br.eventos.config.EventosApplication;

public class ConfiguracaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
    }

    public void salvarEndereco(View view) {

        EditText editText = (EditText) findViewById(R.id.editEndereco);
        String endereco = editText.getText().toString();
        EventosApplication.setURL(endereco, getApplicationContext());

        Toast.makeText(getApplicationContext(), EventosApplication.getURL(getApplicationContext()), Toast.LENGTH_SHORT).show();
    }

    public void verEderecoAtual(View view) {
        Toast.makeText(getApplicationContext(), EventosApplication.getURL(getApplicationContext()), Toast.LENGTH_SHORT).show();
    }
}
