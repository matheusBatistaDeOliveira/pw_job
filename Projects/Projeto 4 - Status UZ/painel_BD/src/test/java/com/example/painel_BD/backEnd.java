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
                WITH ultima_data AS (
                    SELECT
                        ordernumber,
                        MAX(gs_data_retorno_wms) AS max_data
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN
WHERE trunc(gs_data_retorno_wms) >= trunc(sysdate -30) 
                      AND warehouseid = 2097 
                      AND userid <> 'HJS'
                    GROUP BY ordernumber
                ),
                
                base_recente AS (
                    SELECT
                        a.id_mst_in            AS ID,
                        a.warehouseid          AS ORIGEM,
                        a.ordernumber          AS ORDER_KORBER,
                        a.printer              AS IMPRESSORA,
                        a.userid               AS USUARIO,
                        a.uz,
                        a.gs_status,
                        a.gs_status_msg,
                        a.transactioncode,
                        a.nfnumber_out         AS NOTA,
                        a.gs_data_retorno_wms,
                        substr(a.ordernumber, 10, 8) AS MAPA,
                
                        (
                            SELECT pale_destino
                            FROM gsretail.GS_INTEGRA_TMS_CAPA_WA
                            WHERE to_number(pale_carga) =
                                  to_number(substr(a.ordernumber, 10, 8))
                        ) AS destino
                
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN a
                    JOIN ultima_data u
                      ON u.ordernumber = a.ordernumber
                     AND u.max_data    = a.gs_data_retorno_wms
                     WHERE a.transactioncode = 342
                     
                     
                ),
                
                loja_final AS (
                    SELECT
                        b.*,
                        CASE
                            WHEN gs_status = -1 THEN 'FILA'
                            WHEN gs_status =  2 THEN 'EM FATURAMENTO'
                            WHEN gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                            WHEN gs_status =  4 THEN 'NF AUTORIZADA'
                            WHEN gs_status =  9 THEN 'ERRO'
                            WHEN gs_status =  7 THEN 'ENVIADA' ELSE 'WTF'
                        END AS sts
                    FROM base_recente b
                ),
                
                filtrado AS (
                    SELECT *
                    FROM loja_final lf
                    WHERE
                        lf.sts = 'ENVIADA'
                        OR NOT EXISTS (
                            SELECT 1
                            FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN x
                            WHERE x.ordernumber = lf.ORDER_KORBER
                              AND x.gs_status = 7
                        )
                )
                
                SELECT *
                FROM filtrado
                WHERE sts = 'FILA'
                ORDER BY gs_data_retorno_wms desc
                
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableEMFATURAMENTO")
    public ResponseEntity<?> tableEMFATURAMENTO() {
        String sql = """
                WITH ultima_data AS (
                    SELECT
                        ordernumber,
                        MAX(gs_data_retorno_wms) AS max_data
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN
WHERE trunc(gs_data_retorno_wms) >= trunc(sysdate -30) 
                      AND warehouseid = 2097 
                      AND userid <> 'HJS'
                    GROUP BY ordernumber
                ),
                
                base_recente AS (
                    SELECT
                        a.id_mst_in            AS ID,
                        a.warehouseid          AS ORIGEM,
                        a.ordernumber          AS ORDER_KORBER,
                        a.printer              AS IMPRESSORA,
                        a.userid               AS USUARIO,
                        a.uz,
                        a.gs_status,
                        a.gs_status_msg,
                        a.transactioncode,
                        a.nfnumber_out         AS NOTA,
                        a.gs_data_retorno_wms,
                        substr(a.ordernumber, 10, 8) AS MAPA,
                
                        (
                            SELECT pale_destino
                            FROM gsretail.GS_INTEGRA_TMS_CAPA_WA
                            WHERE to_number(pale_carga) =
                                  to_number(substr(a.ordernumber, 10, 8))
                        ) AS destino
                
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN a
                    JOIN ultima_data u
                      ON u.ordernumber = a.ordernumber
                     AND u.max_data    = a.gs_data_retorno_wms
                     WHERE a.transactioncode = 342
                     
                     
                ),
                
                loja_final AS (
                    SELECT
                        b.*,
                        CASE
                            WHEN gs_status = -1 THEN 'FILA'
                            WHEN gs_status =  2 THEN 'EM FATURAMENTO'
                            WHEN gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                            WHEN gs_status =  4 THEN 'NF AUTORIZADA'
                            WHEN gs_status =  9 THEN 'ERRO'
                            WHEN gs_status =  7 THEN 'ENVIADA' ELSE 'WTF'
                        END AS sts
                    FROM base_recente b
                ),
                
                filtrado AS (
                    SELECT *
                    FROM loja_final lf
                    WHERE
                        lf.sts = 'ENVIADA'
                        OR NOT EXISTS (
                            SELECT 1
                            FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN x
                            WHERE x.ordernumber = lf.ORDER_KORBER
                              AND x.gs_status = 7
                        )
                )
                
                SELECT *
                FROM filtrado
                WHERE sts = 'EM FATURAMENTO'
                ORDER BY gs_data_retorno_wms desc
                
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableAGUARDANDOAUTORIZACAO")
    public ResponseEntity<?> tableAGUARDANDOAUTORIZACAO(){
        String sql = """
                WITH ultima_data AS (
                    SELECT
                        ordernumber,
                        MAX(gs_data_retorno_wms) AS max_data
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN
WHERE trunc(gs_data_retorno_wms) >= trunc(sysdate -30) 
                      AND warehouseid = 2097 
                      AND userid <> 'HJS'
                    GROUP BY ordernumber
                ),
                
                base_recente AS (
                    SELECT
                        a.id_mst_in            AS ID,
                        a.warehouseid          AS ORIGEM,
                        a.ordernumber          AS ORDER_KORBER,
                        a.printer              AS IMPRESSORA,
                        a.userid               AS USUARIO,
                        a.uz,
                        a.gs_status,
                        a.gs_status_msg,
                        a.transactioncode,
                        a.nfnumber_out         AS NOTA,
                        a.gs_data_retorno_wms,
                        substr(a.ordernumber, 10, 8) AS MAPA,
                
                        (
                            SELECT pale_destino
                            FROM gsretail.GS_INTEGRA_TMS_CAPA_WA
                            WHERE to_number(pale_carga) =
                                  to_number(substr(a.ordernumber, 10, 8))
                        ) AS destino
                
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN a
                    JOIN ultima_data u
                      ON u.ordernumber = a.ordernumber
                     AND u.max_data    = a.gs_data_retorno_wms
                     WHERE a.transactioncode = 342
                     
                ),
                
                loja_final AS (
                    SELECT
                        b.*,
                        CASE
                            WHEN gs_status = -1 THEN 'FILA'
                            WHEN gs_status =  2 THEN 'EM FATURAMENTO'
                            WHEN gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                            WHEN gs_status =  4 THEN 'NF AUTORIZADA'
                            WHEN gs_status =  9 THEN 'ERRO'
                            WHEN gs_status =  7 THEN 'ENVIADA' ELSE 'WTF'
                        END AS sts
                    FROM base_recente b
                ),
                
                filtrado AS (
                    SELECT *
                    FROM loja_final lf
                    WHERE
                        lf.sts = 'ENVIADA'
                        OR NOT EXISTS (
                            SELECT 1
                            FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN x
                            WHERE x.ordernumber = lf.ORDER_KORBER
                              AND x.gs_status = 7
                        )
                )
                
                SELECT *
                FROM filtrado
                WHERE sts = 'AGUARDANDO AUTORIZACAO NF'
                AND transactioncode = 342
                ORDER BY gs_data_retorno_wms desc
                
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableAUTORIZADA")
    public ResponseEntity<?> tableAUTORIZADA(){
        String sql = """
                WITH ultima_data AS (
                    SELECT
                        ordernumber,
                        MAX(gs_data_retorno_wms) AS max_data
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN
WHERE trunc(gs_data_retorno_wms) >= trunc(sysdate -30) 
                      AND warehouseid = 2097 
                      AND userid <> 'HJS'
                    GROUP BY ordernumber
                ),
                
                base_recente AS (
                    SELECT
                        a.id_mst_in            AS ID,
                        a.warehouseid          AS ORIGEM,
                        a.ordernumber          AS ORDER_KORBER,
                        a.printer              AS IMPRESSORA,
                        a.userid               AS USUARIO,
                        a.uz,
                        a.gs_status,
                        a.gs_status_msg,
                        a.transactioncode,
                        a.nfnumber_out         AS NOTA,
                        a.gs_data_retorno_wms,
                        substr(a.ordernumber, 10, 8) AS MAPA,
                
                        (
                            SELECT pale_destino
                            FROM gsretail.GS_INTEGRA_TMS_CAPA_WA
                            WHERE to_number(pale_carga) =
                                  to_number(substr(a.ordernumber, 10, 8))
                        ) AS destino
                
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN a
                    JOIN ultima_data u
                      ON u.ordernumber = a.ordernumber
                     AND u.max_data    = a.gs_data_retorno_wms
                     WHERE a.transactioncode = 342
                     
                     
                ),
                
                loja_final AS (
                    SELECT
                        b.*,
                        CASE
                            WHEN gs_status = -1 THEN 'FILA'
                            WHEN gs_status =  2 THEN 'EM FATURAMENTO'
                            WHEN gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                            WHEN gs_status =  4 THEN 'NF AUTORIZADA'
                            WHEN gs_status =  9 THEN 'ERRO'
                            WHEN gs_status =  7 THEN 'ENVIADA' ELSE 'WTF'
                        END AS sts
                    FROM base_recente b
                ),
                
                filtrado AS (
                    SELECT *
                    FROM loja_final lf
                    WHERE
                        lf.sts = 'ENVIADA'
                        OR NOT EXISTS (
                            SELECT 1
                            FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN x
                            WHERE x.ordernumber = lf.ORDER_KORBER
                              AND x.gs_status = 7
                        )
                )
                
                SELECT *
                FROM filtrado
                WHERE sts = 'NF AUTORIZADA'
                ORDER BY gs_data_retorno_wms desc
                
                """;
        return gettingTables(sql);
    }



    @GetMapping("/tableEnviado")
    public ResponseEntity<?> tableEnviado(){
        String sql = """
                WITH ultima_data AS (
                    SELECT
                        ordernumber,
                        MAX(gs_data_retorno_wms) AS max_data
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN
    WHERE trunc(gs_data_retorno_wms) >= trunc(sysdate -30) 
                      AND warehouseid = 2097 
                      AND userid <> 'HJS'
                    GROUP BY ordernumber
                ),
                
                base_recente AS (
                    SELECT
                        a.id_mst_in            AS ID,
                        a.warehouseid          AS ORIGEM,
                        a.ordernumber          AS ORDER_KORBER,
                        a.printer              AS IMPRESSORA,
                        a.userid               AS USUARIO,
                        a.uz,
                        a.gs_status,
                        a.gs_status_msg,
                        a.transactioncode,
                        a.nfnumber_out         AS NOTA,
                        a.gs_data_retorno_wms,
                        substr(a.ordernumber, 10, 8) AS MAPA,
                
                        (
                            SELECT pale_destino
                            FROM gsretail.GS_INTEGRA_TMS_CAPA_WA
                            WHERE to_number(pale_carga) =
                                  to_number(substr(a.ordernumber, 10, 8))
                        ) AS destino
                
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN a
                    JOIN ultima_data u
                      ON u.ordernumber = a.ordernumber
                     AND u.max_data    = a.gs_data_retorno_wms
                     WHERE a.transactioncode = 342
                     
                     
                ),
                
                loja_final AS (
                    SELECT
                        b.*,
                        CASE
                            WHEN gs_status = -1 THEN 'FILA'
                            WHEN gs_status =  2 THEN 'EM FATURAMENTO'
                            WHEN gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                            WHEN gs_status =  4 THEN 'NF AUTORIZADA'
                            WHEN gs_status =  9 THEN 'ERRO'
                            WHEN gs_status =  7 THEN 'ENVIADA' ELSE 'WTF'
                        END AS sts
                    FROM base_recente b
                ),
                
                filtrado AS (
                    SELECT *
                    FROM loja_final lf
                    WHERE
                        lf.sts = 'ENVIADA'
                        OR NOT EXISTS (
                            SELECT 1
                            FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN x
                            WHERE x.ordernumber = lf.ORDER_KORBER
                              AND x.gs_status = 7
                        )
                )
                
                SELECT *
               FROM filtrado
                WHERE sts = 'ENVIADA'
                ORDER BY gs_data_retorno_wms desc
                
                
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableErro")
    public ResponseEntity<?> tableErro(){
        String sql = """
                WITH ultima_data AS (
                    SELECT
                        ordernumber,
                        MAX(gs_data_retorno_wms) AS max_data
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN
                WHERE trunc(gs_data_retorno_wms) >= trunc(sysdate -30)
                      AND warehouseid = 2097 
                      AND userid <> 'HJS'
                    GROUP BY ordernumber
                ),
                
                base_recente AS (
                    SELECT
                        a.id_mst_in            AS ID,
                        a.warehouseid          AS ORIGEM,
                        a.ordernumber          AS ORDER_KORBER,
                        a.printer              AS IMPRESSORA,
                        a.userid               AS USUARIO,
                        a.uz,
                        a.gs_status,
                        a.gs_status_msg,
                        a.transactioncode,
                        a.nfnumber_out         AS NOTA,
                        a.gs_data_retorno_wms,
                        substr(a.ordernumber, 10, 8) AS MAPA,
                
                        (
                            SELECT pale_destino
                            FROM gsretail.GS_INTEGRA_TMS_CAPA_WA
                            WHERE to_number(pale_carga) =
                                  to_number(substr(a.ordernumber, 10, 8))
                        ) AS destino
                
                    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN a
                    JOIN ultima_data u
                      ON u.ordernumber = a.ordernumber
                     AND u.max_data    = a.gs_data_retorno_wms
                     WHERE a.transactioncode = 342
                ),
                
                loja_final AS (
                    SELECT
                        b.*,
                        CASE
                            WHEN gs_status = -1 THEN 'FILA'
                            WHEN gs_status =  2 THEN 'EM FATURAMENTO'
                            WHEN gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                            WHEN gs_status =  4 THEN 'NF AUTORIZADA'
                            WHEN gs_status =  9 THEN 'ERRO'
                            WHEN gs_status =  7 THEN 'ENVIADA' ELSE 'WTF'
                        END AS sts
                    FROM base_recente b
                ),
                
                filtrado AS (
                    SELECT *
                    FROM loja_final lf
                    WHERE
                        lf.sts = 'ENVIADA'
                        OR NOT EXISTS (
                            SELECT 1
                            FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN x
                            WHERE x.ordernumber = lf.ORDER_KORBER
                              AND x.gs_status = 7
                        )
                )
                
                SELECT *
                FROM filtrado
                WHERE sts = 'ERRO'
                ORDER BY gs_data_retorno_wms desc
                
                """;
        return gettingTables(sql);
    }

    @GetMapping("/totaisAdjust")
    public ResponseEntity<?> totaisAdjust(){
        String sql = """
WITH ultima_data AS (
    SELECT
        ordernumber,
        MAX(gs_data_retorno_wms) AS max_data
    FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN
WHERE trunc(gs_data_retorno_wms) >= trunc(sysdate -30) 
      AND warehouseid = 2097 
      AND userid <> 'HJS'
    GROUP BY ordernumber
),

base_recente AS (
    SELECT
        a.id_mst_in            AS ID,
        a.warehouseid          AS ORIGEM,
        a.ordernumber          AS ORDER_KORBER,
        a.printer              AS IMPRESSORA,
        a.userid               AS USUARIO,
        a.uz,
        a.gs_status,
        a.gs_status_msg,
        a.transactioncode,
        a.nfnumber_out         AS NOTA,
        a.gs_data_retorno_wms,
        substr(a.ordernumber, 10, 8) AS MAPA,

        (
            SELECT pale_destino
            FROM gsretail.GS_INTEGRA_TMS_CAPA_WA
            WHERE to_number(pale_carga) =
                  to_number(substr(a.ordernumber, 10, 8))
        ) AS destino

        FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN a
                    JOIN ultima_data u
                      ON u.ordernumber = a.ordernumber
                     AND u.max_data    = a.gs_data_retorno_wms
                     WHERE a.transactioncode = 342
                     AND userid <> 'HJS'
                     
                ),
                
                loja_final AS (
                    SELECT
                        b.*,
                        CASE
                            WHEN gs_status = -1 THEN 'FILA'
                            WHEN gs_status =  2 THEN 'EM FATURAMENTO'
                            WHEN gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                            WHEN gs_status =  4 THEN 'NF AUTORIZADA'
                            WHEN gs_status =  9 THEN 'ERRO'
                            WHEN gs_status =  7 THEN 'ENVIADA' ELSE 'WTF'
                        END AS sts
                    FROM base_recente b
                ),
                
                filtrado AS (
                    SELECT *
                    FROM loja_final lf
                    WHERE
                        lf.sts = 'ENVIADA'
                        OR NOT EXISTS (
                            SELECT 1
                            FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN x
                            WHERE x.ordernumber = lf.ORDER_KORBER
                              AND x.gs_status = 7
                        )
                )
                
                SELECT sts, COUNT(*) AS TOTAL
                FROM filtrado
                GROUP BY sts
                """;

        return gettingTables(sql);
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