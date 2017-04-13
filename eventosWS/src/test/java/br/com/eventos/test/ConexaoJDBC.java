package br.com.eventos.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConexaoJDBC {
	String url;
	String dataBaseName;
	String user;
	String password;
	Connection conn;
	PreparedStatement stmt = null;
	Connection con;

	public ConexaoJDBC() {
		this.url = "jdbc:postgresql://localhost:5432/banco";
		this.user = "postgres";
		this.password = "12345";
	}

	public Connection getConnection() {
		try {
			return DriverManager.getConnection(this.url, this.user, this.password);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		ConexaoJDBC con = new ConexaoJDBC();
		Connection cn = con.getConnection();

		List<String> listaTabelas = new ArrayList<String>();
		DatabaseMetaData dbMd = cn.getMetaData();
		ResultSet res = dbMd.getTables(null, null, null, new String[] { "TABLE" });
		
		while (res.next()) {
			listaTabelas.add(res.getString(3));
		}

		System.out.println(listaTabelas);
	}
}
