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
                select
                (
                CASE
                WHEN B.gs_status = -1 THEN 'FILA'
                WHEN B.gs_status =  2 THEN 'EM FATURAMENTO'
                WHEN B.gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                WHEN B.gs_status =  4 THEN 'NF AUTORIZADA'
                WHEN B.gs_status =  9 THEN 'ERRO'
                WHEN B.gs_status =  7 THEN 'ENVIADA'
                ELSE 'NADA'
                END
                ) AS status,
                B.id_mst_in ID ,
                B.warehouseid ORIGEM ,
                B.ordernumber ORDER_KORBER,
                B.printer IMPRESSORA,
                B.userid USUARIO,
                B.uz,
                B.gs_status,
                B.gs_status_msg ,
                B.transactioncode,
                B.nfnumber_out NOTA,
                B.gs_data_retorno_wms,
                substr(B.ordernumber,10,8) MAPA,
                A.destino
                
                  from  (
                select
                Y.warehouseid ORIGEM ,
                Y.ordernumber ORDER_KORBER,
                Y.gs_status,
                Y.gs_status_msg ,
                Y.transactioncode,
                max(Y.gs_data_retorno_wms) gs_data_retorno_wms,
                substr(Y.ordernumber,10,8) MAPA,
                Z.pale_destino  destino
                 from gsretail.GS_INTEGRA_WMS_MST_NODE_IN Y, gsretail.GS_INTEGRA_TMS_CAPA_WA Z
                where trunc(gs_data_retorno_wms)>=trunc(sysdate -10)
                 and warehouseid=2097
                 and userid <> 'HJS'
                 and to_number(pale_carga) = to_number(substr(ordernumber,10,8))
                 and transactioncode = 342
                 and gs_status = -1
                and NOT EXISTS (SELECT 1 FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN X
                            WHERE Y.gs_status = 7
                            AND X.ordernumber = ordernumber)
                
                group by  warehouseid,ordernumber,gs_status,gs_status_msg,transactioncode, pale_destino
                
                   )  A, gsretail.GS_INTEGRA_WMS_MST_NODE_IN B
                where A.ORDER_KORBER  = B.ordernumber
                  and A.gs_data_retorno_wms = B.gs_data_retorno_wms
                  order by B.gs_data_retorno_wms desc
                  
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableEMFATURAMENTO")
    public ResponseEntity<?> tableEMFATURAMENTO() {
        String sql = """
                select
                (
                CASE
                WHEN B.gs_status = -1 THEN 'FILA'
                WHEN B.gs_status =  2 THEN 'EM FATURAMENTO'
                WHEN B.gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                WHEN B.gs_status =  4 THEN 'NF AUTORIZADA'
                WHEN B.gs_status =  9 THEN 'ERRO'
                WHEN B.gs_status =  7 THEN 'ENVIADA'
                ELSE 'NADA'
                END
                ) AS status,
                B.id_mst_in ID ,
                B.warehouseid ORIGEM ,
                B.ordernumber ORDER_KORBER,
                B.printer IMPRESSORA,
                B.userid USUARIO,
                B.uz,
                B.gs_status,
                B.gs_status_msg ,
                B.transactioncode,
                B.nfnumber_out NOTA,
                B.gs_data_retorno_wms,
                substr(B.ordernumber,10,8) MAPA,
                A.destino
                
                  from  (
                select
                Y.warehouseid ORIGEM ,
                Y.ordernumber ORDER_KORBER,
                Y.gs_status,
                Y.gs_status_msg ,
                Y.transactioncode,
                max(Y.gs_data_retorno_wms) gs_data_retorno_wms,
                substr(Y.ordernumber,10,8) MAPA,
                Z.pale_destino  destino
                    from gsretail.GS_INTEGRA_WMS_MST_NODE_IN Y, gsretail.GS_INTEGRA_TMS_CAPA_WA Z
                    where trunc(gs_data_retorno_wms)>=trunc(sysdate -10)  and warehouseid=2097
                
                and userid <> 'HJS'
                and to_number(pale_carga) = to_number(substr(ordernumber,10,8))
                and transactioncode = 342
                and gs_status = 2
                and NOT EXISTS (SELECT 1 FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN X
                            WHERE Y.gs_status = 7
                            AND X.ordernumber = ordernumber)
                
                group by  warehouseid,ordernumber,gs_status,gs_status_msg,transactioncode, pale_destino
                   )  A, gsretail.GS_INTEGRA_WMS_MST_NODE_IN B
                where A.ORDER_KORBER  = B.ordernumber
                  and A.gs_data_retorno_wms = B.gs_data_retorno_wms
                  order by B.gs_data_retorno_wms desc
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableAGUARDANDOAUTORIZACAO")
    public ResponseEntity<?> tableAGUARDANDOAUTORIZACAO() {
        String sql = """
                select
                (
                CASE
                WHEN B.gs_status = -1 THEN 'FILA'
                WHEN B.gs_status =  2 THEN 'EM FATURAMENTO'
                WHEN B.gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                WHEN B.gs_status =  4 THEN 'NF AUTORIZADA'
                WHEN B.gs_status =  9 THEN 'ERRO'
                WHEN B.gs_status =  7 THEN 'ENVIADA'
                ELSE 'NADA'
                END
                ) AS status,
                B.id_mst_in ID ,
                B.warehouseid ORIGEM ,
                B.ordernumber ORDER_KORBER,
                B.printer IMPRESSORA,
                B.userid USUARIO,
                B.uz,
                B.gs_status,
                B.gs_status_msg ,
                B.transactioncode,
                C.NRO_NOTA NOTA,
                E.DESCRICAO_SITUACAO SITUACAO,
                B.gs_data_retorno_wms,
                substr(B.ordernumber,10,8) MAPA,
                A.destino
                
                  from  (
                select
                Y.warehouseid ORIGEM ,
                Y.ordernumber ORDER_KORBER,
                Y.gs_status,
                Y.gs_status_msg ,
                Y.transactioncode,
                max(Y.gs_data_retorno_wms) gs_data_retorno_wms,
                substr(Y.ordernumber,10,8) MAPA,
                Z.pale_destino  destino
                    from gsretail.GS_INTEGRA_WMS_MST_NODE_IN Y, gsretail.GS_INTEGRA_TMS_CAPA_WA Z
                    where trunc(gs_data_retorno_wms)>=trunc(sysdate -10)  and warehouseid=2097
                
                and userid <> 'HJS'
                and to_number(pale_carga) = to_number(substr(ordernumber,10,8))
                AND transactioncode = 342
                and gs_status = 3
                and NOT EXISTS (SELECT 1 FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN X
                            WHERE Y.gs_status = 7
                            AND X.ordernumber = ordernumber)
                
                group by  warehouseid,ordernumber,gs_status,gs_status_msg,transactioncode, pale_destino
                   )  A, gsretail.GS_INTEGRA_WMS_MST_NODE_IN B, gsretail.gs_integra_wms_mst_node_nf C, RMS.ag1lgnfi D, RMS.nfe_controle E
                where A.ORDER_KORBER  = B.ordernumber
                  and A.gs_data_retorno_wms = B.gs_data_retorno_wms
                  and B.id_mst_in = C.id_mst_in
                  and D.nfi_chave_nfe = E.chave_acesso_nfe(+)
                  and D.nfi_data_agenda = C.DTA_AGENDA
                  and D.nfi_nota  = C.NRO_NOTA
                  and D.nfi_origem = C.LOJ_ORG
                  and D.nfi_agenda  = C.AGENDA
                  order by B.gs_data_retorno_wms desc
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableAUTORIZADA")
    public ResponseEntity<?> tableAUTORIZADA() {
        String sql = """
                select
                (
                CASE
                WHEN B.gs_status = -1 THEN 'FILA'
                WHEN B.gs_status =  2 THEN 'EM FATURAMENTO'
                WHEN B.gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                WHEN B.gs_status =  4 THEN 'NF AUTORIZADA'
                WHEN B.gs_status =  9 THEN 'ERRO'
                WHEN B.gs_status =  7 THEN 'ENVIADA'
                ELSE 'NADA'
                END
                ) AS status,
                B.id_mst_in ID ,
                B.warehouseid ORIGEM ,
                B.ordernumber ORDER_KORBER,
                B.printer IMPRESSORA,
                B.userid USUARIO,
                B.uz,
                B.gs_status,
                B.gs_status_msg ,
                B.transactioncode,
                C.NRO_NOTA NOTA,
                E.DESCRICAO_SITUACAO,
                B.gs_data_retorno_wms,
                substr(B.ordernumber,10,8) MAPA,
                A.destino
                
                  from  (
                select
                Y.warehouseid ORIGEM ,
                Y.ordernumber ORDER_KORBER,
                Y.gs_status,
                Y.gs_status_msg ,
                Y.transactioncode,
                max(Y.gs_data_retorno_wms) gs_data_retorno_wms,
                substr(Y.ordernumber,10,8) MAPA,
                Z.pale_destino  destino
                    from gsretail.GS_INTEGRA_WMS_MST_NODE_IN Y, gsretail.GS_INTEGRA_TMS_CAPA_WA Z
                    where trunc(gs_data_retorno_wms)>=trunc(sysdate -10)  and warehouseid=2097
                
                and userid <> 'HJS'
                and to_number(pale_carga) = to_number(substr(ordernumber,10,8))
                AND transactioncode = 342
                and gs_status = 4
                and NOT EXISTS (SELECT 1 FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN X
                            WHERE Y.gs_status = 7
                            AND X.ordernumber = ordernumber)
                
                group by  warehouseid,ordernumber,gs_status,gs_status_msg,transactioncode, pale_destino
                   )  A, gsretail.GS_INTEGRA_WMS_MST_NODE_IN B, gsretail.gs_integra_wms_mst_node_nf C, RMS.ag1lgnfi D, RMS.nfe_controle E
                where A.ORDER_KORBER  = B.ordernumber
                  and A.gs_data_retorno_wms = B.gs_data_retorno_wms
                  and B.id_mst_in = C.id_mst_in
                  and D.nfi_chave_nfe = E.chave_acesso_nfe(+)
                  and D.nfi_data_agenda = C.DTA_AGENDA
                  and D.nfi_nota  = C.NRO_NOTA
                  and D.nfi_origem = C.LOJ_ORG
                  and D.nfi_agenda  = C.AGENDA
                  order by B.gs_data_retorno_wms desc
                """;
        return gettingTables(sql);
    }


    @GetMapping("/tableEnviado")
    public ResponseEntity<?> tableEnviado() {
        String sql = """
                select
                (
                CASE
                WHEN B.gs_status = -1 THEN 'FILA'
                WHEN B.gs_status =  2 THEN 'EM FATURAMENTO'
                WHEN B.gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                WHEN B.gs_status =  4 THEN 'NF AUTORIZADA'
                WHEN B.gs_status =  9 THEN 'ERRO'
                WHEN B.gs_status =  7 THEN 'ENVIADA'
                ELSE 'NADA'
                END
                ) AS status,
                B.id_mst_in ID ,
                B.warehouseid ORIGEM ,
                B.ordernumber ORDER_KORBER,
                B.printer IMPRESSORA,
                B.userid USUARIO,
                B.uz,
                B.gs_status,
                B.gs_status_msg ,
                B.transactioncode,
                B.nfnumber_out NOTA,
                B.gs_data_retorno_wms,
                substr(B.ordernumber,10,8) MAPA,
                A.destino
                
                  from  (
                select
                Y.warehouseid ORIGEM ,
                Y.ordernumber ORDER_KORBER,
                Y.gs_status,
                Y.gs_status_msg ,
                Y.transactioncode,
                max(Y.gs_data_retorno_wms) gs_data_retorno_wms,
                substr(Y.ordernumber,10,8) MAPA,
                Z.pale_destino  destino
                    from gsretail.GS_INTEGRA_WMS_MST_NODE_IN Y, gsretail.GS_INTEGRA_TMS_CAPA_WA Z
                    where trunc(gs_data_retorno_wms)>=trunc(sysdate -10)  and warehouseid=2097
                
                and userid <> 'HJS'
                and to_number(pale_carga) = to_number(substr(ordernumber,10,8))
                AND transactioncode = 342
                and gs_status = 7
                
                group by  warehouseid,ordernumber,gs_status,gs_status_msg,transactioncode, pale_destino
                   )  A, gsretail.GS_INTEGRA_WMS_MST_NODE_IN B
                where A.ORDER_KORBER  = B.ordernumber
                  and A.gs_data_retorno_wms = B.gs_data_retorno_wms
                  order by B.gs_data_retorno_wms desc
                  FETCH FIRST 500 ROWS ONLY
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableErro")
    public ResponseEntity<?> tableErro() {
        String sql = """
                select
                (
                CASE
                WHEN B.gs_status = -1 THEN 'FILA'
                WHEN B.gs_status =  2 THEN 'EM FATURAMENTO'
                WHEN B.gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                WHEN B.gs_status =  4 THEN 'NF AUTORIZADA'
                WHEN B.gs_status =  9 THEN 'ERRO'
                WHEN B.gs_status =  7 THEN 'ENVIADA'
                ELSE 'NADA'
                END
                ) AS status,
                B.id_mst_in ID ,
                B.warehouseid ORIGEM ,
                B.ordernumber ORDER_KORBER,
                B.printer IMPRESSORA,
                B.userid USUARIO,
                B.uz,
                B.gs_status,
                B.gs_status_msg ,
                B.transactioncode,
                B.nfnumber_out NOTA,
                B.gs_data_retorno_wms,
                substr(B.ordernumber,10,8) MAPA,
                A.destino
                
                  from  (
                select
                Y.warehouseid ORIGEM ,
                Y.ordernumber ORDER_KORBER,
                Y.gs_status,
                Y.gs_status_msg ,
                Y.transactioncode,
                max(Y.gs_data_retorno_wms) gs_data_retorno_wms,
                substr(Y.ordernumber,10,8) MAPA,
                Z.pale_destino  destino
                    from gsretail.GS_INTEGRA_WMS_MST_NODE_IN Y, gsretail.GS_INTEGRA_TMS_CAPA_WA Z
                    where trunc(gs_data_retorno_wms)>=trunc(sysdate -10)  and warehouseid=2097
                
                and userid <> 'HJS'
                and to_number(pale_carga) = to_number(substr(ordernumber,10,8))
                and transactioncode = 342
                and gs_status = 9
                and NOT EXISTS (SELECT 1 FROM gsretail.GS_INTEGRA_WMS_MST_NODE_IN X
                            WHERE Y.gs_status = 7
                            AND X.ordernumber = ordernumber)
                
                group by  warehouseid,ordernumber,gs_status,gs_status_msg,transactioncode, pale_destino
                   )  A, gsretail.GS_INTEGRA_WMS_MST_NODE_IN B
                where A.ORDER_KORBER  = B.ordernumber
                  and A.gs_data_retorno_wms = B.gs_data_retorno_wms
                  order by B.gs_data_retorno_wms desc
                """;
        return gettingTables(sql);
    }

    @GetMapping("/totaisAdjust")
    public ResponseEntity<?> totaisAdjust() {
        String sql = """
                select
                (
                CASE
                WHEN B.gs_status = -1 THEN 'FILA'
                WHEN B.gs_status =  2 THEN 'EM FATURAMENTO'
                WHEN B.gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                WHEN B.gs_status =  4 THEN 'NF AUTORIZADA'
                WHEN B.gs_status =  9 THEN 'ERRO'
                WHEN B.gs_status =  7 THEN 'ENVIADA'
                ELSE 'NADA'
                END
                ) AS status,
                COUNT (*) TOTAL
                
                  from  (
                select
                Y.warehouseid ORIGEM ,
                Y.ordernumber ORDER_KORBER,
                Y.gs_status,
                Y.gs_status_msg ,
                Y.transactioncode,
                max(Y.gs_data_retorno_wms) gs_data_retorno_wms,
                substr(Y.ordernumber,10,8) MAPA,
                Z.pale_destino  destino
                    from gsretail.GS_INTEGRA_WMS_MST_NODE_IN Y, gsretail.GS_INTEGRA_TMS_CAPA_WA Z
                    where trunc(gs_data_retorno_wms)>=trunc(sysdate -10)  and warehouseid=2097
                
                and userid <> 'HJS'
                and to_number(pale_carga) = to_number(substr(ordernumber,10,8))
                AND transactioncode = 342
                
                group by  warehouseid,ordernumber,gs_status,gs_status_msg,transactioncode, pale_destino
                   )  A, gsretail.GS_INTEGRA_WMS_MST_NODE_IN B
                where A.ORDER_KORBER  = B.ordernumber
                  and A.gs_data_retorno_wms = B.gs_data_retorno_wms
                
                  GROUP BY B.gs_status
                """;
        return gettingTables(sql);
    }

    @GetMapping("/comColunas")
    public ResponseEntity<?> comColunas() {
        String sql = """
                select
                (
                CASE
                WHEN B.gs_status = -1 THEN 'FILA'
                WHEN B.gs_status =  2 THEN 'EM FATURAMENTO'
                WHEN B.gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                WHEN B.gs_status =  4 THEN 'NF AUTORIZADA'
                WHEN B.gs_status =  9 THEN 'ERRO'
                WHEN B.gs_status =  7 THEN 'ENVIADA'
                ELSE 'NADA'
                END
                ) AS status,
                B.id_mst_in ID ,
                B.warehouseid ORIGEM ,
                B.ordernumber ORDER_KORBER,
                B.printer IMPRESSORA,
                B.userid USUARIO,
                B.uz,
                B.gs_status,
                B.gs_status_msg ,
                B.transactioncode,
                B.nfnumber_out NOTA,
                B.gs_data_retorno_wms,
                substr(B.ordernumber,10,8) MAPA,
                A.destino
                  from  (
                select
                Y.warehouseid ORIGEM ,
                Y.ordernumber ORDER_KORBER,
                Y.gs_status,
                Y.gs_status_msg ,
                Y.transactioncode,
                max(Y.gs_data_retorno_wms) gs_data_retorno_wms,
                substr(Y.ordernumber,10,8) MAPA,
                Z.pale_destino  destino
                    from gsretail.GS_INTEGRA_WMS_MST_NODE_IN Y, gsretail.GS_INTEGRA_TMS_CAPA_WA Z
                    where trunc(gs_data_retorno_wms)>=trunc(sysdate -10)  and warehouseid=2097
                
                and userid <> 'HJS'
                and to_number(pale_carga) = to_number(substr(ordernumber,10,8))
                AND transactioncode = 342
                
                group by  warehouseid,ordernumber,gs_status,gs_status_msg,transactioncode, pale_destino
                   )  A, gsretail.GS_INTEGRA_WMS_MST_NODE_IN B, gsretail.gs_integra_wms_mst_node_nf C, RMS.ag1lgnfi D, RMS.nfe_controle E
                where A.gs_data_retorno_wms = B.gs_data_retorno_wms
                  
                  
                  
                  and D.nfi_chave_nfe = E.chave_acesso_nfe(+)
                  and D.nfi_nota  = C.NRO_NOTA
                  and B.id_mst_in = C.id_mst_in
                  
                  and D.nfi_data_agenda = C.DTA_AGENDA
                  and D.nfi_origem = C.LOJ_ORG
                  and D.nfi_agenda  = C.AGENDA
                  
                  
                  and A.ORDER_KORBER  = B.ordernumber
                  and B.gs_status = 3
                
                  order by B.gs_data_retorno_wms desc
                """;
        return gettingTables(sql);
    }

    @GetMapping("/semColunas")
    public ResponseEntity<?> semColunas() {
        String sql = """
                select
                (
                CASE
                WHEN B.gs_status = -1 THEN 'FILA'
                WHEN B.gs_status =  2 THEN 'EM FATURAMENTO'
                WHEN B.gs_status =  3 THEN 'AGUARDANDO AUTORIZACAO NF'
                WHEN B.gs_status =  4 THEN 'NF AUTORIZADA'
                WHEN B.gs_status =  9 THEN 'ERRO'
                WHEN B.gs_status =  7 THEN 'ENVIADA'
                ELSE 'NADA'
                END
                ) AS status,
                B.id_mst_in ID ,
                B.warehouseid ORIGEM ,
                B.ordernumber ORDER_KORBER,
                B.printer IMPRESSORA,
                B.userid USUARIO,
                B.uz,
                B.gs_status,
                B.gs_status_msg ,
                B.transactioncode,
                B.nfnumber_out NOTA,
                B.gs_data_retorno_wms,
                substr(B.ordernumber,10,8) MAPA,
                A.destino
                  from  (
                select
                Y.warehouseid ORIGEM ,
                Y.ordernumber ORDER_KORBER,
                Y.gs_status,
                Y.gs_status_msg ,
                Y.transactioncode,
                max(Y.gs_data_retorno_wms) gs_data_retorno_wms,
                substr(Y.ordernumber,10,8) MAPA,
                Z.pale_destino  destino
                    from gsretail.GS_INTEGRA_WMS_MST_NODE_IN Y, gsretail.GS_INTEGRA_TMS_CAPA_WA Z
                    where trunc(gs_data_retorno_wms)>=trunc(sysdate -10)  and warehouseid=2097
                
                and userid <> 'HJS'
                and to_number(pale_carga) = to_number(substr(ordernumber,10,8))
                AND transactioncode = 342
                
                group by  warehouseid,ordernumber,gs_status,gs_status_msg,transactioncode, pale_destino
                   )  A, gsretail.GS_INTEGRA_WMS_MST_NODE_IN B
                where A.ORDER_KORBER  = B.ordernumber
                  and A.gs_data_retorno_wms = B.gs_data_retorno_wms
                  
                  
                  
                 
                  and B.gs_status = 3
                
                  order by B.gs_data_retorno_wms desc
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