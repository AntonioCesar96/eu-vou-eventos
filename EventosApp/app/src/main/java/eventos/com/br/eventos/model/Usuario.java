package eventos.com.br.eventos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "usuario")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true, generatedId = false)
    private Long id;

    @DatabaseField
    private String nome;

    @DatabaseField
    private String email;

    @DatabaseField
    private String senha;

    @DatabaseField
    private boolean queroSerAdmin;

    @DatabaseField
    private boolean administrador;

    @DatabaseField
    private String fotoPerfil;

    @DatabaseField
    private boolean donoDoCelular;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Faculdade faculdade;

    public Usuario() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public boolean isQueroSerAdmin() {
        return queroSerAdmin;
    }

    public void setQueroSerAdmin(boolean queroSerAdmin) {
        this.queroSerAdmin = queroSerAdmin;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public boolean isDonoDoCelular() {
        return donoDoCelular;
    }

    public void setDonoDoCelular(boolean donoDoCelular) {
        this.donoDoCelular = donoDoCelular;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", queroSerAdmin=" + queroSerAdmin +
                ", administrador=" + administrador +
                ", fotoPerfil='" + fotoPerfil + '\'' +
                ", faculdade=" + faculdade +
                '}';
    }
}
