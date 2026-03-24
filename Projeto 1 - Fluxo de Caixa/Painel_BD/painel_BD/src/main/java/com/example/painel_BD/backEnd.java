package com.example.painel_BD;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class backEnd {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/total")
    public ResponseEntity<?> total() {
        String sql = """
                SELECT count (distinct CTR_LOJA) TOTAL
                  FROM RMS.AG2VCTRL
                WHERE CTR_DATA_MOV
                    BETWEEN 251021
                    AND 251107
                """;
        return getting(sql);
    }

    @GetMapping("/totalColeta")
    public ResponseEntity<?> totalColeta() {
        String sql = """
                SELECT count (distinct CTR_LOJA) TOTAL
                  FROM RMS.AG2VCTRL
                WHERE CTR_DATA_MOV
                    BETWEEN 251021
                    AND 251107
                    AND CTR_FLAG_INTG = ' '
                    AND CTR_FLAG_QDOC = ' '
                    AND (CTR_FLAG_ATDC = 'K' OR CTR_FLAG_ATDC = ' ')
                """;
        return getting(sql);
    }

    @GetMapping("/totalCriticada")
    public ResponseEntity<?> totalCriticada() {
        String sql = """
                SELECT count (distinct ctr_loja) TOTAL
                  FROM RMS.AG2VCTRL
                WHERE CTR_FLAG_ATDC <> 'P'
                    AND CTR_FLAG_CRIT = 'F'
                    AND CTR_DATA_MOV
                    BETWEEN 251021
                    AND 251107
                """;
        return getting(sql);
    }

    @GetMapping("/totalPendente")
    public ResponseEntity<?> totalPendente() {
        String sql = """
                SELECT count (distinct ctr_loja) TOTAL
                  FROM RMS.AG2VCTRL
                WHERE CTR_FLAG_ATDC = ' '
                    AND CTR_FLAG_CRIT = ' '
                    AND CTR_DATA_MOV
                    BETWEEN 251021
                    AND 251107
                    AND ((CTR_FLAG_INTG = ' ') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
                    AND ((CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = 'I'))
                """;
        return getting(sql);
    }

    @GetMapping("/totalAtualizada")
    public ResponseEntity<?> totalAtualizada() {
        String sql = """
                SELECT count (distinct ctr_loja) TOTAL
                  FROM RMS.AG2VCTRL
                WHERE CTR_FLAG_ATDC = 'P'
                    AND CTR_DATA_MOV
                    BETWEEN 251021
                    AND 251107
                """;
        return getting(sql);
    }

    @GetMapping("/tableColeta")
    public ResponseEntity<?> tableColeta() {
        String sql = """
                SELECT case CTR_FLAG_ATDC
                         when 'K' THEN
                          'COL'
                         when 'P' THEN
                          'ATU'
                         when ' ' THEN
                          'PEND'
                       end SITUACAO,
                       CTR_FLAG_INTG as I,
                       CTR_FLAG_QDOC as D,
                       case CTR_FLAG_CRIT
                         when 'F' THEN
                          'C'
                       end as C,
                       TIP_CODIGO || '-' || TIP_DIGITO as FILIAL,
                       TIP_NOME_FANTASIA as NOME,
                       tip_estado as UF,
                       to_date(CTR_DATA_MOV, 'yy/MM/dd') as DATA,
                       CTR_NUMSEQ_INI as MAPA,
                       CTR_AUTONOMIA as AUTONOMIA
                  FROM RMS.AG2VCTRL, RMS.AA2CTIPO, RMS.AA2CTABE
                WHERE TAB_CODIGO(+) = 45
                    AND TAB_ACESSO(+) = to_char(TRUNC(CTR_LOJA / 10), 'fm0000000') || 'PV2'
                    AND TIP_CODIGO <= 500
                    AND CTR_DATA_MOV
                    BETWEEN 251021
                    AND 251107
                    AND CTR_FLAG_INTG = ' '
                    AND CTR_FLAG_QDOC = ' '
                    AND TIP_CODIGO = TRUNC(CTR_LOJA / 10)
                    AND TIP_DIGITO = RMS.DAC(TRUNC(CTR_LOJA / 10))
                    AND (CTR_FLAG_ATDC = 'K' OR CTR_FLAG_ATDC = ' ')
                  ORDER BY RMS.adiciona_seculo(CTR_DATA_MOV) desc
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableCriticada")
    public ResponseEntity<?> tableCriticada() {
        String sql = """
                SELECT case CTR_FLAG_ATDC
                        when 'K' THEN 'COL'
                        when 'P' THEN 'ATU'
                        when ' ' THEN 'PEND'
                      end SITUACAO,
                      CTR_FLAG_INTG as I,
                      CTR_FLAG_QDOC as D,
                      case CTR_FLAG_CRIT
                        when 'F' THEN 'C'
                      end as C,
                      TIP_CODIGO || '-' || TIP_DIGITO as FILIAL,
                      TIP_NOME_FANTASIA as NOME,
                      tip_estado as UF,
                      to_date(CTR_DATA_MOV, 'YY/MM/DD') as DATA,
                      CTR_NUMSEQ_INI as MAPA,
                      CTR_AUTONOMIA as AUTONOMIA
                  FROM RMS.AG2VCTRL, RMS.AA2CTIPO, RMS.AA2CTABE
                WHERE TAB_CODIGO(+) = 45
                    AND TAB_ACESSO(+) = to_char(TRUNC(CTR_LOJA / 10), 'fm0000000') || 'PV2'
                    AND TIP_CODIGO <= 500
                    AND CTR_DATA_MOV
                    BETWEEN 251021
                    AND 251107
                    AND TIP_CODIGO = TRUNC(CTR_LOJA / 10)
                    AND TIP_DIGITO = RMS.DAC(TRUNC(CTR_LOJA / 10))
                    AND CTR_FLAG_ATDC <> 'P'
                    AND CTR_FLAG_CRIT = 'F'
                  ORDER BY RMS.adiciona_seculo(CTR_DATA_MOV) desc
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tablePendente")
    public ResponseEntity<?> tablePendente(){
        String sql = """
                SELECT case CTR_FLAG_ATDC
                         when 'K' THEN
                          'COL'
                         when 'P' THEN
                          'ATU'
                         when ' ' THEN
                          'PEND'
                       end SITUACAO,
                       CTR_FLAG_INTG as I,
                       CTR_FLAG_QDOC as D,
                       case CTR_FLAG_CRIT
                         when 'F' THEN
                          'C'
                       end as C,
                       TIP_CODIGO || '-' || TIP_DIGITO as FILIAL,
                       TIP_NOME_FANTASIA as NOME,
                       tip_estado as UF,
                       to_date(CTR_DATA_MOV, 'yy/MM/dd') as DATA,
                       CTR_NUMSEQ_INI as MAPA,
                       CTR_AUTONOMIA as AUTONOMIA
                  FROM RMS.AG2VCTRL, RMS.AA2CTIPO, RMS.AA2CTABE
                WHERE TAB_CODIGO(+) = 45
                   AND TAB_ACESSO(+) = to_char(TRUNC(CTR_LOJA / 10), 'fm0000000') || 'PV2'
                   AND TIP_CODIGO <= 500
                    AND CTR_DATA_MOV
                    BETWEEN 251021
                    AND 251107
                    AND TIP_CODIGO = TRUNC(CTR_LOJA / 10)
                    AND TIP_DIGITO = RMS.DAC(TRUNC(CTR_LOJA / 10))
                    AND CTR_FLAG_ATDC = ' '
                    AND CTR_FLAG_CRIT = ' '
                    AND ((CTR_FLAG_INTG = ' ') OR (CTR_FLAG_INTG = 'I' AND CTR_FLAG_QDOC = 'D'))
                    AND ((CTR_FLAG_QDOC = 'D') OR (CTR_FLAG_QDOC = ' ' AND CTR_FLAG_INTG = 'I'))
                  ORDER BY RMS.adiciona_seculo(CTR_DATA_MOV) desc
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableAtualizada")
    public ResponseEntity<?> tableAtualizada(){
        String sql = """
                SELECT case CTR_FLAG_ATDC
                         when 'K' THEN 'COL'
                         when 'P' THEN 'ATU'
                         when ' ' THEN 'PEND'
                       end SITUACAO,
                       CTR_FLAG_INTG as I,
                       CTR_FLAG_QDOC as D,
                       case CTR_FLAG_CRIT
                         when 'F' THEN 'C'
                       end as C,
                       TIP_CODIGO || '-' || TIP_DIGITO as FILIAL,
                       TIP_NOME_FANTASIA as NOME,
                       tip_estado as UF,
                       to_date(CTR_DATA_MOV, 'YY/MM/DD') as DATA,
                       CTR_NUMSEQ_INI as MAPA,
                       CTR_AUTONOMIA as AUTONOMIA
                  FROM RMS.AG2VCTRL, RMS.AA2CTIPO, RMS.AA2CTABE
                WHERE TAB_CODIGO(+) = 45
                    AND TAB_ACESSO(+) = to_char(TRUNC(CTR_LOJA / 10), 'fm0000000') || 'PV2'
                    AND TIP_CODIGO <= 500
                    AND CTR_DATA_MOV
                    BETWEEN 251021
                    AND 251107
                    AND TIP_CODIGO = TRUNC(CTR_LOJA / 10)
                    AND TIP_DIGITO = RMS.DAC(TRUNC(CTR_LOJA / 10))
                    AND CTR_FLAG_ATDC = 'P'
                  ORDER BY RMS.adiciona_seculo(CTR_DATA_MOV) desc
                """;
        return gettingTables(sql);
    }

    ResponseEntity<?> getting(String sql) {
        try {
            Map<String, Object> get = jdbcTemplate.queryForMap(sql);
            return ResponseEntity.ok(get);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao consultar o banco de dados: " + e.getMessage());
        }
    }

    ResponseEntity<?> gettingTables(String sql) {
        try {
            List<Map<String, Object>> get = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(get);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao consultar o banco de dados: " + e.getMessage());
        }
    }
}