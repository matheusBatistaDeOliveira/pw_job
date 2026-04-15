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

    @GetMapping("/tableNAFILA")
    public ResponseEntity<?> tableFILA() {
        String sql = """
                select DISTINCT
                a.CNPJ_FORNECEDOR  AS ORIGEM,
                a.CNPJ_TOMADOR AS DESTINO,
                a.NUMERO_NOTA AS NOTA,
                a.SERIE,
                a.EMISSAO,
                a.CONDICAOPAGTO,
                a.VALOR_TOTAL AS TOTAL,
                a.CTRL_RMS_DATA_PROC AS DATA,
                a.CTRL_GEDI_DATA_IMPORTACAO AS IMPORTACÃO,
                a.CTRL_RMS_MENSAGEM,
                a.PROTOCOLO_GEDI,
                a.FIGURA_FISCAL,
                a.CFOP,
                a.PORTADOR
                from gsretail.gs_integra_gedi_nf a,RMS.aa1cfisc b,(
                select
                NUMERO_NOTA AS NOTA,
                SERIE,
                VALOR_TOTAL AS TOTAL,
                MAX(CTRL_RMS_DATA_PROC) AS DATA,
                FIGURA_FISCAL,
                PORTADOR
                from gsretail.gs_integra_gedi_nf, RMS.aa1cfisc
                where numero_nota = fis_nro_nota(+)
                    and trim(serie)=trim(fis_serie(+))
                    and CTRL_RMS_DATA_PROC > sysdate -10
                group by SERIE, NUMERO_NOTA, VALOR_TOTAL, FIGURA_FISCAL, PORTADOR
                order by NUMERO_NOTA desc
                ) c
                where a.numero_nota = b.fis_nro_nota(+)
                and trim(a.serie)=trim(b.fis_serie(+))
                and a.CTRL_RMS_DATA_PROC = c.DATA
                order by a.CTRL_RMS_DATA_PROC desc
                FETCH FIRST 500 ROWS ONLY
                """;
        return gettingTables(sql);
    }

    @GetMapping("/totaisAdjust")
    public ResponseEntity<?> totaisAdjust() {
        String sql = """
                SELECT COUNT(DISTINCT NUMERO_NOTA) AS TOTAL
                FROM gsretail.gs_integra_gedi_nf, RMS.aa1cfisc
                WHERE numero_nota = fis_nro_nota(+)
                  AND TRIM(serie) = TRIM(fis_serie(+))
                  AND CTRL_RMS_DATA_PROC > SYSDATE - 10
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
}