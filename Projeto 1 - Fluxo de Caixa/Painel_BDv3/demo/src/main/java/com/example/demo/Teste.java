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
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
                select count(*) ATUALIZADAS_TOTAL from (
                select
                  'ATUALIZADA' AS STATUS,
                  b.ctr_loja,
                  TO_DATE(b.ctr_data_mov, 'yy/MM/dd') as DATA,
                  a.CTR_NUMSEQ_INI as MAPA,
                  a.CTR_AUTONOMIA as AUTONOMIA,
                  c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
                  c.tip_estado as UF,
                  c.TIP_NOME_FANTASIA as NOME
                  from (
                   select
                    ctr_loja,
                    max(ctr_data_mov) ctr_data_mov
                     from RMS.AG2VCTRL
                    GROUP BY ctr_loja
                    ) b, RMS.AG2VCTRL a, RMS.AA2CTIPO c--, RMS.AA2CTABE d
                     WHERE b.ctr_data_mov = a.ctr_data_mov
                      AND b.ctr_loja = a.ctr_loja
                      AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                      AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                      AND a.ctr_data_mov BETWEEN ? AND ?
                      --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
                      AND CTR_DATA <> 0
                     GROUP BY
                      b.ctr_loja,
                      b.ctr_data_mov,
                      a.CTR_NUMSEQ_INI,
                      a.CTR_AUTONOMIA,
                      c.TIP_CODIGO || '-' || c.TIP_DIGITO,
                      c.tip_estado,
                      c.TIP_NOME_FANTASIA
                     ORDER BY b.CTR_DATA_MOV desc)
        """;

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    /* Total Lojas */
    @GetMapping("/totalLojas")
    public ResponseEntity<?> totalLojas() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
                select count(*) TOTAL_LOJAS from (
                select
                 CASE
                    WHEN a.CTR_FLAG_ATDC = 'K' THEN 'COLETA'
                    WHEN a.CTR_FLAG_ATDC = 'P' THEN 'ATUALIZADA'
                    WHEN a.CTR_FLAG_CRIT = 'F' AND a.CTR_FLAG_ATDC <> 'P' THEN 'CRITICADA'
                    WHEN a.CTR_FLAG_ATDC = ' ' AND a.CTR_FLAG_CRIT = ' ' THEN 'PENDENTE'
                  END AS STATUS,
                  b.ctr_loja,
                  TO_DATE(b.ctr_data_mov, 'yy/MM/dd') as DATA,
                  a.CTR_NUMSEQ_INI as MAPA,
                  a.CTR_AUTONOMIA as AUTONOMIA,
                  c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
                  c.tip_estado as UF,
                  c.TIP_NOME_FANTASIA as NOME
                  from (
                   select
                    ctr_loja,
                    max(ctr_data_mov) ctr_data_mov
                     from RMS.AG2VCTRL
                    GROUP BY ctr_loja
                    ) b, RMS.AG2VCTRL a, RMS.AA2CTIPO c--, RMS.AA2CTABE d
                     WHERE b.ctr_data_mov = a.ctr_data_mov
                      AND b.ctr_loja = a.ctr_loja
                      AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                      AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                      AND a.ctr_data_mov BETWEEN ? AND ?
                      --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
                     GROUP BY
                     CASE
                         WHEN a.CTR_FLAG_ATDC = 'K' THEN 'COLETA'
                         WHEN a.CTR_FLAG_ATDC = 'P' THEN 'ATUALIZADA'
                         WHEN a.CTR_FLAG_CRIT = 'F' AND a.CTR_FLAG_ATDC <> 'P' THEN 'CRITICADA'
                         WHEN a.CTR_FLAG_ATDC = ' ' AND a.CTR_FLAG_CRIT = ' ' THEN 'PENDENTE'
                      END,
                      b.ctr_loja,
                      b.ctr_data_mov,
                      a.CTR_NUMSEQ_INI,
                      a.CTR_AUTONOMIA,
                      c.TIP_CODIGO || '-' || c.TIP_DIGITO,
                      c.tip_estado,
                      c.TIP_NOME_FANTASIA
                     ORDER BY b.CTR_DATA_MOV desc)
        """;

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    /* Total Coleta */
    @GetMapping("/totalColeta")
    public ResponseEntity<?> totalColeta() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
                select count(*) COLETADAS_TOTAL from (
                select
                  'COLETA' AS STATUS,
                  b.ctr_loja,
                  TO_DATE(b.ctr_data_mov, 'yy/MM/dd') as DATA,
                  a.CTR_NUMSEQ_INI as MAPA,
                  a.CTR_AUTONOMIA as AUTONOMIA,
                  c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
                  c.tip_estado as UF,
                  c.TIP_NOME_FANTASIA as NOME
                  from (
                   select
                    ctr_loja,
                    max(ctr_data_mov) ctr_data_mov
                     from RMS.AG2VCTRL
                    GROUP BY ctr_loja
                    ) b, RMS.AG2VCTRL a, RMS.AA2CTIPO c--, RMS.AA2CTABE d
                     WHERE b.ctr_data_mov = a.ctr_data_mov
                      AND b.ctr_loja = a.ctr_loja
                      AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                      AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                      AND a.ctr_data_mov BETWEEN ? AND ?
                      --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
                      AND CTR_DATA = 0
                      AND CTR_FLAG_ATDC in ('K', ' ')
                      AND CTR_FLAG_CRIT NOT in ('F')
                      AND CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = ' '
                     GROUP BY
                      b.ctr_loja,
                      b.ctr_data_mov,
                      a.CTR_NUMSEQ_INI,
                      a.CTR_AUTONOMIA,
                      c.TIP_CODIGO || '-' || c.TIP_DIGITO,
                      c.tip_estado,
                      c.TIP_NOME_FANTASIA
                     ORDER BY b.CTR_DATA_MOV desc
                     )
        """;

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    /* Total Criticadas */
    @GetMapping("/totalCriticadas")
    public ResponseEntity<?> totalCriticadas() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
          select count(*) CRITICADAS_TOTAL from (
          select
          'CRITICADA' AS STATUS,
          a.ctr_loja,
          TO_DATE(a.ctr_data_mov, 'yy/MM/dd') as DATA,
          a.CTR_NUMSEQ_INI as MAPA,
          a.CTR_AUTONOMIA as AUTONOMIA,
          c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
          c.tip_estado as UF,
          c.TIP_NOME_FANTASIA as NOME
          from (
             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA =  RMS.DATETO_RMS7(SYSDATE - 1)
             union
             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA = 0
          ) a, RMS.AA2CTIPO c
             WHERE a.ctr_data_mov = a.ctr_data_mov
              AND a.ctr_loja = a.ctr_loja
              AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
              AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
              AND a.ctr_data_mov BETWEEN ? AND ?
              --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
              AND CTR_DATA = 0
              AND CTR_FLAG_ATDC <> 'P'
              AND CTR_FLAG_CRIT = 'F'
             GROUP BY
              a.ctr_loja,
              a.ctr_data_mov,
              a.CTR_NUMSEQ_INI,
              a.CTR_AUTONOMIA,
              c.TIP_CODIGO || '-' || c.TIP_DIGITO,
              c.tip_estado,
              c.TIP_NOME_FANTASIA
             ORDER BY a.CTR_DATA_MOV desc
                     )
        """;

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    /* Total Pendentes */
    @GetMapping("/totalPendentes")
    public ResponseEntity<?> totalPendentes() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
          select count(*) pendentes_Total from (
          select
          'PENDENTE' AS STATUS,
          a.ctr_loja,
          TO_DATE(a.ctr_data_mov, 'yy/MM/dd') as DATA,
          a.CTR_NUMSEQ_INI as MAPA,
          a.CTR_AUTONOMIA as AUTONOMIA,
          c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
          c.tip_estado as UF,
          c.TIP_NOME_FANTASIA as NOME
          from (
             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA =  RMS.DATETO_RMS7(SYSDATE - 1)
             union
             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA = 0
          ) a, RMS.AA2CTIPO c
             WHERE a.ctr_data_mov = a.ctr_data_mov
              AND a.ctr_loja = a.ctr_loja
              AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
              AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
              AND a.ctr_data_mov BETWEEN ? AND ?
              --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
              AND CTR_DATA = 0
              AND CTR_FLAG_ATDC = ' '
              AND CTR_FLAG_CRIT = ' '
              AND ((CTR_FLAG_INTG = ' ' AND CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
              --AND ((CTR_FLAG_INTG = ' ') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
              --AND ((CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = 'I'))
             GROUP BY
              a.ctr_loja,
              a.ctr_data_mov,
              a.CTR_NUMSEQ_INI,
              a.CTR_AUTONOMIA,
              c.TIP_CODIGO || '-' || c.TIP_DIGITO,
              c.tip_estado,
              c.TIP_NOME_FANTASIA
             ORDER BY a.CTR_DATA_MOV desc
                     )
     """;

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, dataAnterior, dataHoje);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tabela_atualizadas")
    public ResponseEntity<?> tabela_atualizadas() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataAnterior = quinzeDiasAtras.format(formatter);
        String dataHoje = hoje.format(formatter);

        String sql = """
                select
                          'ATUALIZADA' AS STATUS,
                          b.ctr_loja,
                          TO_DATE(b.ctr_data_mov, 'yy/MM/dd') as DATA,
                          a.CTR_NUMSEQ_INI as MAPA,
                          a.CTR_AUTONOMIA as AUTONOMIA,
                          c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
                          c.tip_estado as UF,
                          c.TIP_NOME_FANTASIA as NOME
                          from (
                           select
                            ctr_loja,
                            max(ctr_data_mov) ctr_data_mov
                             from RMS.AG2VCTRL
                            GROUP BY ctr_loja
                            ) b, RMS.AG2VCTRL a, RMS.AA2CTIPO c--, RMS.AA2CTABE d
                             WHERE b.ctr_data_mov = a.ctr_data_mov
                              AND b.ctr_loja = a.ctr_loja
                              AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                              AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                              AND a.ctr_data_mov BETWEEN ? AND ?
                              --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
                              AND CTR_DATA <> 0
                             GROUP BY
                              b.ctr_loja,
                              b.ctr_data_mov,
                              a.CTR_NUMSEQ_INI,
                              a.CTR_AUTONOMIA,
                              c.TIP_CODIGO || '-' || c.TIP_DIGITO,
                              c.tip_estado,
                              c.TIP_NOME_FANTASIA
                             ORDER BY b.CTR_DATA_MOV desc
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
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataAnterior = quinzeDiasAtras.format(formatter);
        String dataHoje = hoje.format(formatter);

        String sql ="""
          select
          'CRITICADA' AS STATUS,
          a.ctr_loja,
          TO_DATE(a.ctr_data_mov, 'yy/MM/dd') as DATA,
          a.CTR_NUMSEQ_INI as MAPA,
          a.CTR_AUTONOMIA as AUTONOMIA,
          c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
          c.tip_estado as UF,
          c.TIP_NOME_FANTASIA as NOME
          from (
             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA =  RMS.DATETO_RMS7(SYSDATE - 1)
             union
             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA = 0
          ) a, RMS.AA2CTIPO c
             WHERE a.ctr_data_mov = a.ctr_data_mov
              AND a.ctr_loja = a.ctr_loja
              AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
              AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
              AND a.ctr_data_mov BETWEEN ? AND ?
              --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
              AND CTR_DATA = 0
              AND CTR_FLAG_ATDC <> 'P'
              AND CTR_FLAG_CRIT = 'F'
             GROUP BY
              a.ctr_loja,
              a.ctr_data_mov,
              a.CTR_NUMSEQ_INI,
              a.CTR_AUTONOMIA,
              c.TIP_CODIGO || '-' || c.TIP_DIGITO,
              c.tip_estado,
              c.TIP_NOME_FANTASIA
             ORDER BY a.CTR_DATA_MOV desc
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
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataAnterior = quinzeDiasAtras.format(formatter);
        String dataHoje = hoje.format(formatter);

        String sql = """
                select
                select
                  'COLETA' AS STATUS,
                  b.ctr_loja,
                  TO_DATE(b.ctr_data_mov, 'yy/MM/dd') as DATA,
                  a.CTR_NUMSEQ_INI as MAPA,
                  a.CTR_AUTONOMIA as AUTONOMIA,
                  c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
                  c.tip_estado as UF,
                  c.TIP_NOME_FANTASIA as NOME
                  from (
                   select
                    ctr_loja,
                    max(ctr_data_mov) ctr_data_mov
                     from RMS.AG2VCTRL
                    GROUP BY ctr_loja
                    ) b, RMS.AG2VCTRL a, RMS.AA2CTIPO c--, RMS.AA2CTABE d
                     WHERE b.ctr_data_mov = a.ctr_data_mov
                      AND b.ctr_loja = a.ctr_loja
                      AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                      AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                      AND a.ctr_data_mov BETWEEN ? AND ?
                      --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
                      AND CTR_DATA = 0
                      AND CTR_FLAG_ATDC in ('K', ' ')
                      AND CTR_FLAG_CRIT NOT in ('F')
                      AND CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = ' '
                     GROUP BY
                      b.ctr_loja,
                      b.ctr_data_mov,
                      a.CTR_NUMSEQ_INI,
                      a.CTR_AUTONOMIA,
                      c.TIP_CODIGO || '-' || c.TIP_DIGITO,
                      c.tip_estado,
                      c.TIP_NOME_FANTASIA
                     ORDER BY b.CTR_DATA_MOV desc
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
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataAnterior = quinzeDiasAtras.format(formatter);
        String dataHoje = hoje.format(formatter);

        String sql ="""
          select
          'PENDENTE' AS STATUS,
          a.ctr_loja,
          TO_DATE(a.ctr_data_mov, 'yy/MM/dd') as DATA,
          a.CTR_NUMSEQ_INI as MAPA,
          a.CTR_AUTONOMIA as AUTONOMIA,
          c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
          c.tip_estado as UF,
          c.TIP_NOME_FANTASIA as NOME
          from (
             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA =  RMS.DATETO_RMS7(SYSDATE - 1)
             union
             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA = 0
          ) a, RMS.AA2CTIPO c
             WHERE a.ctr_data_mov = a.ctr_data_mov
              AND a.ctr_loja = a.ctr_loja
              AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
              AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
              AND a.ctr_data_mov BETWEEN ? AND ?
              --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
              AND CTR_DATA = 0
              AND CTR_FLAG_ATDC = ' '
              AND CTR_FLAG_CRIT = ' '
              AND ((CTR_FLAG_INTG = ' ' AND CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
              --AND ((CTR_FLAG_INTG = ' ') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
              --AND ((CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = 'I'))
             GROUP BY
              a.ctr_loja,
              a.ctr_data_mov,
              a.CTR_NUMSEQ_INI,
              a.CTR_AUTONOMIA,
              c.TIP_CODIGO || '-' || c.TIP_DIGITO,
              c.tip_estado,
              c.TIP_NOME_FANTASIA
             ORDER BY a.CTR_DATA_MOV desc
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

    @GetMapping("/tabelas_adjuste")
    public ResponseEntity<?> tabela_Adjuste() {

        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataAnterior = quinzeDiasAtras.format(formatter);
        String dataHoje = hoje.format(formatter);

        String sql ="""
                select
                          case
                              WHEN
                                  CTR_DATA = 0
                                  AND CTR_FLAG_CRIT = ' '
                                  AND ((CTR_FLAG_INTG = ' ' AND CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
                              THEN 'PENDENTE'
                              WHEN
                                  CTR_DATA = 0
                                  AND CTR_FLAG_ATDC in ('K', ' ')
                                  AND CTR_FLAG_CRIT NOT in ('F')
                                  AND CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = ' '
                                  THEN 'COLETA'
                              WHEN
                                  CTR_DATA = 0
                                  AND CTR_FLAG_ATDC <> 'P'
                                  AND CTR_FLAG_CRIT = 'F'
                                  THEN 'CRITICADA'
                              ELSE 'NADA'
                              END AS STATUS,
                          a.ctr_loja,
                          TO_DATE(a.ctr_data_mov, 'yy/MM/dd') as DATA,
                          a.CTR_NUMSEQ_INI as MAPA,
                          a.CTR_AUTONOMIA as AUTONOMIA,
                          c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
                          c.tip_estado as UF,
                          c.TIP_NOME_FANTASIA as NOME
                          from (
                             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA =  RMS.DATETO_RMS7(SYSDATE - 1)
                             union
                             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA = 0
                          ) a, RMS.AA2CTIPO c
                             WHERE a.ctr_data_mov = a.ctr_data_mov
                              AND a.ctr_loja = a.ctr_loja
                              AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                              AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                              AND a.ctr_data_mov BETWEEN ? AND ?
                              --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
                              AND CTR_DATA = 0
                             GROUP BY
                             case
                              WHEN
                                  a.CTR_DATA = 0
                                  AND CTR_FLAG_CRIT = ' '
                                  AND ((CTR_FLAG_INTG = ' ' AND CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
                              THEN 'PENDENTE'
                              WHEN
                                  a.CTR_DATA = 0
                                  AND CTR_FLAG_ATDC in ('K', ' ')
                                  AND CTR_FLAG_CRIT NOT in ('F')
                                  AND CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = ' '
                                  THEN 'COLETA'
                              WHEN
                                  a.CTR_DATA = 0
                                  AND CTR_FLAG_ATDC <> 'P'
                                  AND CTR_FLAG_CRIT = 'F'
                                  THEN 'CRITICADA'
                              ELSE 'NADA'
                              END,
                              a.ctr_loja,
                              a.ctr_data_mov,
                              a.CTR_NUMSEQ_INI,
                              a.CTR_AUTONOMIA,
                              c.TIP_CODIGO || '-' || c.TIP_DIGITO,
                              c.tip_estado,
                              c.TIP_NOME_FANTASIA
                              order by ctr_data_mov desc
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
        LocalDate quinzeDiasAtras = hoje.minusDays(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dataHoje = hoje.format(formatter);
        String dataAnterior = quinzeDiasAtras.format(formatter);

        String sql = """
                select
                                 STATUS,
                                 count (*) TOTAL from (
                          select
                          case
                              WHEN
                                  CTR_DATA = 0
                                  AND CTR_FLAG_CRIT = ' '
                                  AND ((CTR_FLAG_INTG = ' ' AND CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
                              THEN 'PENDENTE'
                              WHEN
                                  CTR_DATA = 0
                                  AND CTR_FLAG_ATDC in ('K', ' ')
                                  AND CTR_FLAG_CRIT NOT in ('F')
                                  AND CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = ' '
                                  THEN 'COLETA'
                              WHEN
                                  CTR_DATA = 0
                                  AND CTR_FLAG_ATDC <> 'P'
                                  AND CTR_FLAG_CRIT = 'F'
                                  THEN 'CRITICADA'
                              ELSE 'NADA'
                              END AS STATUS,
                          a.ctr_loja,
                          TO_DATE(a.ctr_data_mov, 'yy/MM/dd') as DATA,
                          a.CTR_NUMSEQ_INI as MAPA,
                          a.CTR_AUTONOMIA as AUTONOMIA,
                          c.TIP_CODIGO || '-' || c.TIP_DIGITO as FILIAL,
                          c.tip_estado as UF,
                          c.TIP_NOME_FANTASIA as NOME
                          from (
                             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA =  RMS.DATETO_RMS7(SYSDATE - 1)
                             union
                             SELECT * FROM RMS.AG2VCTRL WHERE CTR_DATA = 0
                          ) a, RMS.AA2CTIPO c
                             WHERE a.ctr_data_mov = a.ctr_data_mov
                              AND a.ctr_loja = a.ctr_loja
                              AND c.TIP_CODIGO = TRUNC(a.CTR_LOJA / 10)
                              AND c.TIP_DIGITO = RMS.DAC(TRUNC(a.CTR_LOJA / 10))
                              AND a.ctr_data_mov BETWEEN ? AND ?
                              --AND TO_CHAR(a.CTR_NUMSEQ_INI) NOT LIKE '%4391%'
                              AND CTR_DATA = 0
                             GROUP BY
                             case
                              WHEN
                                  a.CTR_DATA = 0
                                  AND CTR_FLAG_CRIT = ' '
                                  AND ((CTR_FLAG_INTG = ' ' AND CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
                              THEN 'PENDENTE'
                              WHEN
                                  a.CTR_DATA = 0
                                  AND CTR_FLAG_ATDC in ('K', ' ')
                                  AND CTR_FLAG_CRIT NOT in ('F')
                                  AND CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = ' '
                                  THEN 'COLETA'
                              WHEN
                                  a.CTR_DATA = 0
                                  AND CTR_FLAG_ATDC <> 'P'
                                  AND CTR_FLAG_CRIT = 'F'
                                  THEN 'CRITICADA'
                              ELSE 'NADA'
                              END,
                              a.ctr_loja,
                              a.ctr_data_mov,
                              a.CTR_NUMSEQ_INI,
                              a.CTR_AUTONOMIA,
                              c.TIP_CODIGO || '-' || c.TIP_DIGITO,
                              c.tip_estado,
                              c.TIP_NOME_FANTASIA)
                              GROUP BY STATUS
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