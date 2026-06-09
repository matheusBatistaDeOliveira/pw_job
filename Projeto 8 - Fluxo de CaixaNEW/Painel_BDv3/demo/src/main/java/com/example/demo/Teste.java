package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
//allowOriginPatterns = "*"
//origins = "http://192.168.0.51:1812", allowCredentials = "true"
public class Teste {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /* Total Atualizadas */
    @GetMapping("/totalAtualizadas")
    public ResponseEntity<?> lojasImportaramOperadores() {



        String sql = """
                select 'lojas importaram operadores' Descricao, count(*) qtde
                  from rms.COB_LOGTESOUR
                where log_data = rms.dateto_rms7(sysdate - 1)   and log_status like '1-4%'
        """;

        return gettingTotals(sql);
    }

    /* Total Lojas */
    @GetMapping("/totalPendentes")
    public ResponseEntity<?> lojasNaoImportaramOperadores() {

        String sql = """
        select 'lojas não importaram operadores' Descricao, count(*) qtde
                  from rms.COB_LOGTESOUR
                where log_data = rms.dateto_rms7(sysdate - 1)
                and log_status like '1-%'  and log_status not like '1-4%'
        """;

        return gettingTotals(sql);
    }

    /* Total Coleta */
    @GetMapping("/totalColeta")
    public ResponseEntity<?> lojasImportaramColetas() {



        String sql = """
        select 'lojas importaram coletas' Descricao, count(*) qtde
                  from rms.COB_LOGTESOUR
                where log_data = rms.dateto_rms7(sysdate - 1)   and log_status like '2-5-Captura Com sucesso'
        """;

        return gettingTotals(sql);
    }

    /* Total Criticadas */
    @GetMapping("/totalCriticadas")
    public ResponseEntity<?> lojasNaoImportaramColetas() {



        String sql = """
        select 'lojas nao importaram coletas' descricao, count(log_loja) lojas from (
                select distinct log_loja
                    from rms.COB_LOGTESOUR
                where log_data = rms.dateto_rms7(sysdate - 1)
                and log_status like '2-%'  and log_status not like '2-5%'  )
        """;

        return gettingTotals(sql);
    }

    /* Total Pendentes */
    @GetMapping("/totalLojas")
    public ResponseEntity<?> totalPendentes() {



        String sql = """
     
     """;

        return gettingTotals(sql);
    }

    @GetMapping("/tabela_atualizadas")
    public ResponseEntity<?> tabela_atualizadas() {

        String sql = """
                
        """;

        return gettingTables(sql);
    }


    @GetMapping("/tabela_criticadas")



    public ResponseEntity<?> tabela_criticadas() {

    	String sql ="""
                
    	 """;

        return gettingTables(sql);
    }

    @GetMapping("/tabela_coleta")
    public ResponseEntity<?> tabela_coleta() {

        String sql = """
                
        """;

        return gettingTables(sql);
    }
    @GetMapping("/tabela_pendentes")
    public ResponseEntity<?> tabela_pendentes() {

        String sql ="""
                
        """;

        return gettingTables(sql);
    }

    /* Total Ajuste */
    @GetMapping("/totalAjuste")
    public ResponseEntity<?> totalAjuste() {



        String sql = """
                
        """;


        return gettingTables(sql);
    }

    ResponseEntity<?> gettingTables(String sql) {
        try {
            List<Map<String, Object>> get = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(get);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao consultar o banco de dados: " + e.getMessage());
        }
    }

    ResponseEntity<?> gettingTotals(String sql) {
        try {
            Map<String, Object> get = jdbcTemplate.queryForMap(sql);
            return ResponseEntity.ok(get);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao consultar o banco de dados: " + e.getMessage());
        }
    }
}