package com.cobasi.registerManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controllerManager {

    @Autowired
    private DataSource dataSource;

    // A abordagem com PreparedStatement é sempre a recomendada.

    @CrossOrigin(origins = "http://192.168.0.92:3001/", allowCredentials = "true")
    @GetMapping("/dataManager")
    public ResponseEntity<?> getDadosSat(@RequestParam int dias) {

        Map<String, Object> dados = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            String queryBase = "select count(*) from (SELECT TO_CHAR(DTH_INCLUSAO, 'DD/MM/YYYY HH24:MI:SS') AS DTH_INCLUSAO"
                    + ",TO_CHAR(DTH_PROCESSAMENTO, 'DD/MM/YYYY HH24:MI:SS') AS DTH_PROCESSAMENTO, CHAVE, STATUS, CASE STATUS "
                    + "WHEN 0 THEN 'INTEGRAÇÃO PENDENTE DE PROCESSAMENTO' WHEN -1 THEN 'EM PROCESSAMENTO' WHEN 1 THEN 'INTEGRADO/PROCESSADO' "
                    + "WHEN 2 THEN 'PROCESSADO E CONFIRMADO PELO ERP' WHEN 3 THEN 'ERRO INTEGRAÇÃO' END AS \"DESCRICAO STATUS\", XML_INTEGRACAO "
                    + "FROM TAB_INTEGRACAO_GENERICA "
                    // AQUI ESTÁ A LÓGICA CORRIGIDA:
                    + "WHERE trunc(DTH_INCLUSAO) >= sysdate - " + dias + " and status = %d " 
                    + "ORDER BY DTH_INCLUSAO, DTH_PROCESSAMENTO DESC)";

            // --- Consulta para STATUS = 0 ---
            try (Statement st = connection.createStatement();
                 ResultSet rs = st.executeQuery(String.format(queryBase, 0))) {
                if (rs.next()) {
                    dados.put("pendente_processamento", rs.getInt(1));
                }
            }

            // --- Consulta para STATUS = -1 ---
            try (Statement st = connection.createStatement();
                 ResultSet rs = st.executeQuery(String.format(queryBase, -1))) {
                if (rs.next()) {
                    dados.put("em_processamento", rs.getInt(1));
                }
            }
            
            // --- Consulta para STATUS = 1 ---
            try (Statement st = connection.createStatement();
                 ResultSet rs = st.executeQuery(String.format(queryBase, 1))) {
                if (rs.next()) {
                    dados.put("integrado_processado", rs.getInt(1));
                }
            }

            // --- Consulta para STATUS = 2 ---
            try (Statement st = connection.createStatement();
                 ResultSet rs = st.executeQuery(String.format(queryBase, 2))) {
                if (rs.next()) {
                    dados.put("confimadoERP", rs.getInt(1));
                }
            }

            // --- Consulta para STATUS = 3 ---
            try (Statement st = connection.createStatement();
                 ResultSet rs = st.executeQuery(String.format(queryBase, 3))) {
                if (rs.next()) {
                    dados.put("erroIntegracao", rs.getInt(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", "Falha ao executar consultas: " + e.getMessage()));
        }

        return ResponseEntity.ok(dados);
    }
}