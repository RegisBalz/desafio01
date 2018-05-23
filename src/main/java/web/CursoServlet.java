package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CursoServlet extends HttpServlet {
    Connection connection;
    
    public CursoServlet() throws SQLException {
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/erpcolegio", "sa", "sa");
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        req.setCharacterEncoding("UTF-8");
        PrintWriter saida = resp.getWriter();
        
        try {
            PreparedStatement p = connection.prepareStatement("SELECT * FROM CURSO");
            ResultSet rs = p.executeQuery();
            
            while (rs.next()) {
                saida.write("<ul>");
                saida.write("<li>");
                saida.write("-> " + rs.getInt("id") + "<br>");
                saida.write("-> " + rs.getString("curso") + "<br>");
                saida.write("-> " + rs.getString("turno") + "<br>");
                saida.write("-> " + rs.getInt("qtde_estudantes") + "<br>");
                saida.write("-> " + rs.getDate("data_cadastro") + "<br>");
                saida.write("</li>");
                saida.write("</ul>");
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        req.setCharacterEncoding("UTF-8");
        
        String id = req.getParameter("id");
        String curso = req.getParameter("curso");
        String turno = req.getParameter("turno");
        String qtdeEstudantes = req.getParameter("qtde_estudantes");
        String dataCadastro = req.getParameter("data_cadastro");
        
        try {
            String SQL = "INSERT INTO CURSO VALUES (?, ?, ?, ?, ?)";
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setInt(1, Integer.parseInt(id));
            p.setString(2, curso);
            p.setString(3, turno);
            p.setInt(4, Integer.parseInt(qtdeEstudantes));
            p.setDate(5, Date.valueOf(dataCadastro));
            p.execute();
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
        
        PrintWriter saida = resp.getWriter();
        saida.write("Concluído");
    }
}