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
    public ResponseEntity<?> totalAtualizadas() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(45);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
        SELECT count (distinct ctr_loja) atualizadas_Total
          FROM RMS.AG2VCTRL 
         WHERE CTR_FLAG_ATDC = 'P' 
           AND Ctr_data_mov >= ? 
           AND Ctr_data_mov <= ?
        """;

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    /* Total Lojas */
    @GetMapping("/totalLojas")
    public ResponseEntity<?> totalLojas() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(45);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
        SELECT count (distinct CTR_LOJA) TOTAL_LOJAS
          FROM RMS.AG2VCTRL
         WHERE Ctr_data_mov >= ?
           AND Ctr_data_mov <= ?
        """;

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    /* Total Coleta */
    @GetMapping("/totalColeta")
    public ResponseEntity<?> totalColeta() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(45);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
        SELECT count (distinct ctr_loja) coletadas_Total
         FROM RMS.AG2VCTRL
        WHERE Ctr_data_mov >= ?
          AND Ctr_data_mov <= ?
          AND CTR_FLAG_INTG = ' '
          AND CTR_FLAG_QDOC = ' '
          AND CTR_FLAG_CRIT <> 'F'
          AND (CTR_FLAG_ATDC = 'K' OR CTR_FLAG_ATDC = ' ')
        """;

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    /* Total Criticadas */
    @GetMapping("/totalCriticadas")
    public ResponseEntity<?> totalCriticadas() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(45);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
        SELECT count (distinct ctr_loja) CRITICADAS_TOTAL 
          FROM RMS.AG2VCTRL 
         WHERE CTR_FLAG_ATDC <> 'P' 
           AND CTR_FLAG_CRIT = 'F' 
           AND Ctr_data_mov >= ?
           AND Ctr_data_mov <= ?
        """;

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    /* Total Pendentes */
    @GetMapping("/totalPendentes")
    public ResponseEntity<?> totalPendentes() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(45);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
     SELECT count (distinct ctr_loja) pendentes_Total
       FROM RMS.AG2VCTRL
      WHERE CTR_FLAG_ATDC = ' '
        AND CTR_FLAG_CRIT = ' '
        AND Ctr_data_mov >= ?
        AND Ctr_data_mov <= ?
        AND ((CTR_FLAG_INTG = ' ') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
        AND ((CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = 'I'))
     """;

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tabela_atualizadas")
    public ResponseEntity<?> tabela_atualizadas() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(45);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataAnterior = quinzeDiasAtras.format(formatter);
        String dataHoje = hoje.format(formatter);

        String sql = """
                WITH ultima_data AS (
                  SELECT
                    a.CTR_LOJA,
                    MAX(a.CTR_DATA_MOV) AS max_data
                  FROM RMS.AG2VCTRL a
                  WHERE a.CTR_DATA_MOV BETWEEN ? AND ?
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

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, dataAnterior, dataHoje
        );

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Map<String, Object>> formattedList = resultList.stream().map(row -> {
            Map<String, Object> newRow = new HashMap<>(row);
            Object dataObj = row.get("DATA");
            if (dataObj != null) {
                LocalDate date = null;
                if (dataObj instanceof java.sql.Date) {
                    date = ((java.sql.Date) dataObj).toLocalDate();
                } else if (dataObj instanceof java.sql.Timestamp) {
                    date = ((java.sql.Timestamp) dataObj).toLocalDateTime().toLocalDate();
                } else if (dataObj instanceof java.util.Date) {
                    date = ((java.util.Date) dataObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                newRow.put("DATA", date != null ? date.format(outputFormatter) : null);
            } else {
                newRow.put("DATA", null);
            }
            return newRow;
        }).toList();

        return ResponseEntity.ok(formattedList);
    }


    @GetMapping("/tabela_criticadas")



    public ResponseEntity<?> tabela_criticadas() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(45);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataAnterior = quinzeDiasAtras.format(formatter);
        String dataHoje = hoje.format(formatter);

    	String sql ="""
                WITH ultima_data AS (
                  SELECT
                    a.CTR_LOJA,
                    MAX(a.CTR_DATA_MOV) AS max_data
                  FROM RMS.AG2VCTRL a
                  WHERE a.CTR_DATA_MOV BETWEEN ? AND ?
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

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, dataAnterior, dataHoje
        );

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Map<String, Object>> formattedList = resultList.stream().map(row -> {
            Map<String, Object> newRow = new HashMap<>(row);
            Object dataObj = row.get("DATA");
            if (dataObj != null) {
                LocalDate date = null;
                if (dataObj instanceof java.sql.Date) {
                    date = ((java.sql.Date) dataObj).toLocalDate();
                } else if (dataObj instanceof java.sql.Timestamp) {
                    date = ((java.sql.Timestamp) dataObj).toLocalDateTime().toLocalDate();
                } else if (dataObj instanceof java.util.Date) {
                    date = ((java.util.Date) dataObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                newRow.put("DATA", date != null ? date.format(outputFormatter) : null);
            } else {
                newRow.put("DATA", null);
            }
            return newRow;
        }).toList();

        return ResponseEntity.ok(formattedList);
    }

    @GetMapping("/tabela_coleta")
    public ResponseEntity<?> tabela_coleta() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(45);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataAnterior = quinzeDiasAtras.format(formatter);
        String dataHoje = hoje.format(formatter);

        String sql = """
                WITH ultima_data AS (
                  SELECT
                    a.CTR_LOJA,
                    MAX(a.CTR_DATA_MOV) AS max_data
                  FROM RMS.AG2VCTRL a
                  WHERE a.CTR_DATA_MOV BETWEEN ? AND ?
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

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, dataAnterior, dataHoje
        );

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Map<String, Object>> formattedList = resultList.stream().map(row -> {
            Map<String, Object> newRow = new HashMap<>(row);
            Object dataObj = row.get("DATA");
            if (dataObj != null) {
                LocalDate date = null;
                if (dataObj instanceof java.sql.Date) {
                    date = ((java.sql.Date) dataObj).toLocalDate();
                } else if (dataObj instanceof java.sql.Timestamp) {
                    date = ((java.sql.Timestamp) dataObj).toLocalDateTime().toLocalDate();
                } else if (dataObj instanceof java.util.Date) {
                    date = ((java.util.Date) dataObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                newRow.put("DATA", date != null ? date.format(outputFormatter) : null);
            } else {
                newRow.put("DATA", null);
            }
            return newRow;
        }).toList();

        return ResponseEntity.ok(formattedList);
    }
    @GetMapping("/tabela_pendentes")
    public ResponseEntity<?> tabela_pendentes() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(45);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataAnterior = quinzeDiasAtras.format(formatter);
        String dataHoje = hoje.format(formatter);

        String sql ="""
                WITH ultima_data AS (
                  SELECT
                    a.CTR_LOJA,
                    MAX(a.CTR_DATA_MOV) AS max_data
                  FROM RMS.AG2VCTRL a
                  WHERE a.CTR_DATA_MOV BETWEEN ? AND ?
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

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, dataAnterior, dataHoje
        );

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Map<String, Object>> formattedList = resultList.stream().map(row -> {
            Map<String, Object> newRow = new HashMap<>(row);
            Object dataObj = row.get("DATA");
            if (dataObj != null) {
                LocalDate date = null;
                if (dataObj instanceof java.sql.Date) {
                    date = ((java.sql.Date) dataObj).toLocalDate();
                } else if (dataObj instanceof java.sql.Timestamp) {
                    date = ((java.sql.Timestamp) dataObj).toLocalDateTime().toLocalDate();
                } else if (dataObj instanceof java.util.Date) {
                    date = ((java.util.Date) dataObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
                newRow.put("DATA", date != null ? date.format(outputFormatter) : null);
            } else {
                newRow.put("DATA", null);
            }
            return newRow;
        }).toList();

        return ResponseEntity.ok(formattedList);
    }

    /* Total Ajuste */
    @GetMapping("/totalAjuste")
    public ResponseEntity<?> totalAjuste() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(45);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
                WITH ultima_data AS (
                                                 SELECT
                                                   a.CTR_LOJA,
                                                   MAX(a.CTR_DATA_MOV) AS max_data
                                                 FROM RMS.AG2VCTRL a
                                                 WHERE a.CTR_DATA_MOV BETWEEN ? AND ?
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


        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    private ResponseEntity<?> executeCountQuery(String sql) {
        try {
            Map<String, Object> resultado = jdbcTemplate.queryForMap(sql);
            return ResponseEntity.ok(resultado);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("count", 0));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno ao buscar os dados.", "detalhe", e.getMessage()));
        }
    }
}