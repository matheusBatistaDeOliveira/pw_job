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
     SELECT count (distinct ctr_loja) pendentes_Total
       FROM RMS.AG2VCTRL
      WHERE CTR_FLAG_ATDC = ' '
        AND CTR_FLAG_CRIT = ' '
        AND Ctr_data_mov >= 
        AND Ctr_data_mov <= 
        AND ((CTR_FLAG_INTG = ' ') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
        AND ((CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = 'I'))
     """;

        return gettingTotals(sql);
    }

    @GetMapping("/tabela_atualizadas")
    public ResponseEntity<?> tabela_atualizadas() {

        String sql = """
                WITH ultima_data AS (
                  SELECT
                    a.CTR_LOJA,
                    MAX(a.CTR_DATA_MOV) AS max_data
                  FROM RMS.AG2VCTRL a
                  WHERE a.CTR_DATA_MOV BETWEEN  AND 
                  GROUP BY a.CTR_LOJA
                ),
                base_recente AS (
                  SELECT
                    a.*,
                    t.TIP_CODIGO,
                    t.TIP_DIGITO,
                    t.TIP_NOME_FANTASIA,
                    t.TIP_ESTADO
                  FROM RMS.AG2VCTRL a
                  JOIN ultima_data u
                    ON u.CTR_LOJA = a.CTR_LOJA
                   AND u.max_data = a.CTR_DATA_MOV
                  JOIN RMS.AA2CTIPO t
                    ON t.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                   AND t.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                LEFT JOIN RMS.AA2CTABE b
                  ON b.TAB_CODIGO = 45
                 AND b.TAB_ACESSO = TO_CHAR(TRUNC(a.CTR_LOJA / 10), 'fm0000000') || 'PV2'
                )
                SELECT * FROM(
                SELECT
                  CASE
                    WHEN CTR_FLAG_ATDC = 'K' THEN 'COLETA'
                    WHEN CTR_FLAG_ATDC = 'P' THEN 'ATUALIZADA'
                    WHEN CTR_FLAG_CRIT = 'F' AND CTR_FLAG_ATDC <> 'P' THEN 'CRITICADA'
                    WHEN CTR_FLAG_ATDC = ' ' AND CTR_FLAG_CRIT = ' ' THEN 'PENDENTE'
                  END AS STATUS,
                  TIP_CODIGO || '-' || TIP_DIGITO AS FILIAL,
                  TIP_NOME_FANTASIA AS NOME,
                  TIP_ESTADO AS UF,
                  TO_DATE(CTR_DATA_MOV, 'yy/MM/dd') AS DATA,
                  CTR_NUMSEQ_INI AS MAPA,
                  CTR_AUTONOMIA AS AUTONOMIA
                FROM base_recente
                ORDER BY STATUS)
                WHERE STATUS = 'ATUALIZADA'
                ORDER BY DATA desc
        """;

        return gettingTables(sql);
    }


    @GetMapping("/tabela_criticadas")



    public ResponseEntity<?> tabela_criticadas() {

    	String sql ="""
                WITH ultima_data AS (
                  SELECT
                    a.CTR_LOJA,
                    MAX(a.CTR_DATA_MOV) AS max_data
                  FROM RMS.AG2VCTRL a
                  WHERE a.CTR_DATA_MOV BETWEEN  AND 
                  GROUP BY a.CTR_LOJA
                ),
                base_recente AS (
                  SELECT
                    a.*,
                    t.TIP_CODIGO,
                    t.TIP_DIGITO,
                    t.TIP_NOME_FANTASIA,
                    t.TIP_ESTADO
                  FROM RMS.AG2VCTRL a
                  JOIN ultima_data u
                    ON u.CTR_LOJA = a.CTR_LOJA
                   AND u.max_data = a.CTR_DATA_MOV
                  JOIN RMS.AA2CTIPO t
                    ON t.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                   AND t.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                LEFT JOIN RMS.AA2CTABE b
                  ON b.TAB_CODIGO = 45
                 AND b.TAB_ACESSO = TO_CHAR(TRUNC(a.CTR_LOJA / 10), 'fm0000000') || 'PV2'
                )
                SELECT * FROM(
                SELECT
                  CASE
                    WHEN CTR_FLAG_ATDC = 'K' THEN 'COLETA'
                    WHEN CTR_FLAG_ATDC = 'P' THEN 'ATUALIZADA'
                    WHEN CTR_FLAG_CRIT = 'F' AND CTR_FLAG_ATDC <> 'P' THEN 'CRITICADA'
                    WHEN CTR_FLAG_ATDC = ' ' AND CTR_FLAG_CRIT = ' ' THEN 'PENDENTE'
                  END AS STATUS,
                  TIP_CODIGO || '-' || TIP_DIGITO AS FILIAL,
                  TIP_NOME_FANTASIA AS NOME,
                  TIP_ESTADO AS UF,
                  TO_DATE(CTR_DATA_MOV, 'yy/MM/dd') AS DATA,
                  CTR_NUMSEQ_INI AS MAPA,
                  CTR_AUTONOMIA AS AUTONOMIA
                FROM base_recente
                ORDER BY STATUS)
                WHERE STATUS = 'CRITICADA'
                ORDER BY DATA desc
    	 """;

        return gettingTables(sql);
    }

    @GetMapping("/tabela_coleta")
    public ResponseEntity<?> tabela_coleta() {

        String sql = """
                WITH ultima_data AS (
                  SELECT
                    a.CTR_LOJA,
                    MAX(a.CTR_DATA_MOV) AS max_data
                  FROM RMS.AG2VCTRL a
                  WHERE a.CTR_DATA_MOV BETWEEN  AND 
                  GROUP BY a.CTR_LOJA
                ),
                base_recente AS (
                  SELECT
                    a.*,
                    t.TIP_CODIGO,
                    t.TIP_DIGITO,
                    t.TIP_NOME_FANTASIA,
                    t.TIP_ESTADO
                  FROM RMS.AG2VCTRL a
                  JOIN ultima_data u
                    ON u.CTR_LOJA = a.CTR_LOJA
                   AND u.max_data = a.CTR_DATA_MOV
                  JOIN RMS.AA2CTIPO t
                    ON t.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                   AND t.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                LEFT JOIN RMS.AA2CTABE b
                  ON b.TAB_CODIGO = 45
                 AND b.TAB_ACESSO = TO_CHAR(TRUNC(a.CTR_LOJA / 10), 'fm0000000') || 'PV2'
                )
                SELECT * FROM(
                SELECT
                  CASE
                    WHEN CTR_FLAG_ATDC = 'K' THEN 'COLETA'
                    WHEN CTR_FLAG_ATDC = 'P' THEN 'ATUALIZADA'
                    WHEN CTR_FLAG_CRIT = 'F' AND CTR_FLAG_ATDC <> 'P' THEN 'CRITICADA'
                    WHEN CTR_FLAG_ATDC = ' ' AND CTR_FLAG_CRIT = ' ' THEN 'PENDENTE'
                  END AS STATUS,
                  TIP_CODIGO || '-' || TIP_DIGITO AS FILIAL,
                  TIP_NOME_FANTASIA AS NOME,
                  TIP_ESTADO AS UF,
                  TO_DATE(CTR_DATA_MOV, 'yy/MM/dd') AS DATA,
                  CTR_NUMSEQ_INI AS MAPA,
                  CTR_AUTONOMIA AS AUTONOMIA
                FROM base_recente
                ORDER BY STATUS)
                WHERE STATUS = 'COLETA'
                ORDER BY DATA desc
        """;

        return gettingTables(sql);
    }
    @GetMapping("/tabela_pendentes")
    public ResponseEntity<?> tabela_pendentes() {

        String sql ="""
                WITH ultima_data AS (
                  SELECT
                    a.CTR_LOJA,
                    MAX(a.CTR_DATA_MOV) AS max_data
                  FROM RMS.AG2VCTRL a
                  WHERE a.CTR_DATA_MOV BETWEEN  AND 
                  GROUP BY a.CTR_LOJA
                ),
                base_recente AS (
                  SELECT
                    a.*,
                    t.TIP_CODIGO,
                    t.TIP_DIGITO,
                    t.TIP_NOME_FANTASIA,
                    t.TIP_ESTADO
                  FROM RMS.AG2VCTRL a
                  JOIN ultima_data u
                    ON u.CTR_LOJA = a.CTR_LOJA
                   AND u.max_data = a.CTR_DATA_MOV
                  JOIN RMS.AA2CTIPO t
                    ON t.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                   AND t.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                LEFT JOIN RMS.AA2CTABE b
                  ON b.TAB_CODIGO = 45
                 AND b.TAB_ACESSO = TO_CHAR(TRUNC(a.CTR_LOJA / 10), 'fm0000000') || 'PV2'
                )
                SELECT * FROM(
                SELECT
                  CASE
                    WHEN CTR_FLAG_ATDC = 'K' THEN 'COLETA'
                    WHEN CTR_FLAG_ATDC = 'P' THEN 'ATUALIZADA'
                    WHEN CTR_FLAG_CRIT = 'F' AND CTR_FLAG_ATDC <> 'P' THEN 'CRITICADA'
                    WHEN CTR_FLAG_ATDC = ' ' AND CTR_FLAG_CRIT = ' ' THEN 'PENDENTE'
                  END AS STATUS,
                  TIP_CODIGO || '-' || TIP_DIGITO AS FILIAL,
                  TIP_NOME_FANTASIA AS NOME,
                  TIP_ESTADO AS UF,
                  TO_DATE(CTR_DATA_MOV, 'yy/MM/dd') AS DATA,
                  CTR_NUMSEQ_INI AS MAPA,
                  CTR_AUTONOMIA AS AUTONOMIA
                FROM base_recente
                ORDER BY STATUS)
                WHERE STATUS = 'PENDENTE'
                ORDER BY DATA desc
        """;

        return gettingTables(sql);
    }

    /* Total Ajuste */
    @GetMapping("/totalAjuste")
    public ResponseEntity<?> totalAjuste() {



        String sql = """
                WITH ultima_data AS (
                                                 SELECT
                                                   a.CTR_LOJA,
                                                   MAX(a.CTR_DATA_MOV) AS max_data
                                                 FROM RMS.AG2VCTRL a
                                                 WHERE a.CTR_DATA_MOV BETWEEN  AND 
                                                 GROUP BY a.CTR_LOJA
                                               ),
                
                                               base_recente AS (
                                                 SELECT
                                                   a.*,
                                                   t.TIP_CODIGO,
                                                   t.TIP_DIGITO,
                                                   t.TIP_NOME_FANTASIA,
                                                   t.TIP_ESTADO
                                                 FROM RMS.AG2VCTRL a
                
                                                 JOIN ultima_data u
                                                   ON u.CTR_LOJA = a.CTR_LOJA
                                                  AND u.max_data = a.CTR_DATA_MOV
                
                                                 JOIN RMS.AA2CTIPO t
                                                   ON t.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                                                  AND t.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                
                                               LEFT JOIN RMS.AA2CTABE b
                                                 ON b.TAB_CODIGO = 45
                                                AND b.TAB_ACESSO = TO_CHAR(TRUNC(a.CTR_LOJA / 10), 'fm0000000') || 'PV2'
                                               )
                
                                               SELECT
                                                 STATUS,
                                                 COUNT(*) AS TOTAL
                                               FROM (
                                                 SELECT
                                                   CASE
                                                     WHEN CTR_FLAG_ATDC = 'K' THEN 'COLETA'
                                                     WHEN CTR_FLAG_ATDC = 'P' THEN 'ATUALIZADA'
                                                     WHEN CTR_FLAG_CRIT = 'F' AND CTR_FLAG_ATDC <> 'P' THEN 'CRITICADA'
                                                     WHEN CTR_FLAG_ATDC = ' ' AND CTR_FLAG_CRIT = ' ' THEN 'PENDENTE'
                                                   END AS STATUS
                                                 FROM base_recente
                                               )
                                               WHERE STATUS IS NOT NULL
                                               GROUP BY STATUS
                                               ORDER BY STATUS
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