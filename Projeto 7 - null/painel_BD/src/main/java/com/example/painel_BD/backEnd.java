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
@CrossOrigin(origins = "*")
public class backEnd {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/t852")
    public ResponseEntity<?> t852() {
        String sql = """
                SELECT  to_char(log_date,'DD/MM/YY HH24:MI:SS') data, OWNER, JOB_NAME, status,
                        to_char(req_start_date,'DD/MM/YY HH24:MI:SS') ult_exec,
                        to_char(actual_start_date,'DD/MM/YY HH24:MI:SS') data_atual, 
                        to_char(RUN_DURATION) RUN_DURATION, 
                        additional_info
                  FROM dba_scheduler_job_run_details
                WHERE RMS.dateto_rms7(LOG_DATE) >= 1260514
                   AND STATUS = 'STOPPED'
                """;
        return gettingTables(sql);
    }

    @GetMapping("/t711")
    public ResponseEntity<?> t711() {
        String sql = """
                SELECT  to_char(log_date,'DD/MM/YY HH24:MI:SS') data, OWNER, JOB_NAME, status,
                        to_char(req_start_date,'DD/MM/YY HH24:MI:SS') ult_exec,
                        to_char(actual_start_date,'DD/MM/YY HH24:MI:SS') data_atual, 
                        to_char(RUN_DURATION) RUN_DURATION, 
                        additional_info
                  FROM dba_scheduler_job_run_details
                WHERE RMS.dateto_rms7(LOG_DATE) >= 1260514
                   AND STATUS = 'FAILED'
                """;
        return gettingTables(sql);
    }

//    @GetMapping("/t851")
//    public ResponseEntity<?> t851() {
//        String sql = """
//                select
//                ID_MOVIMENTO,
//                IDENTIFICADOR,
//                CODIGO_ARMAZEM,
//                CODIGO_ARMAZEM_DESTINO,
//                CODIGO_ITEM,
//                CODIGO_ITEM_DISPLAY,
//                CODIGO_CLIENTE,
//                CODIGO_TRANSACAO,
//                QUANTIDADE_PRE_AJUSTE,
//                QUANTIDADE_POS_AJUSTE,
//                QUANTIDADE_ALTERADA,
//                CTRL_MSG_ENVIO,
//                DATA_MOVIMENTO,
//                STATUS_PRE_INVENTARIO,
//                STATUS_POS_INVENTARIO,
//                CODIGO_MOTIVO,
//                ENDERECO_AJUSTE,
//                IDENTIFICADOR_KIT,
//                USUARIO_EXECUCAO,
//                NUMERO_LOTE,
//                HUID,
//                FORNECEDOR,
//
//
//
//
//
//                COD_INTEGRACAO,
//                COD_CONFIGURACAO,
//                CHAVE_INTEGRACAO,
//                CTRL_STATUS_ENVIO,
//                CTRL_COD_MSG_ENVIO,
//                CTRL_DATA_ENVIO,
//                RMS_CTRL_DESBLOQUEADO,
//                GS_COD_MSG,
//                GS_DSC_MSG,
//                GS_OBS_NOTIF_RMS,
//                ATTRIBUTE_1,
//                GET_ESTOQUE_ANTERIOR,
//                GET_ESTOQUE_POSTERIOR,
//                QUANTIDADE_BLOQ_BLOQ,
//                SALDO_TROCAS
//
//
//
//                from gsretail.gs_sinc_ajuste_estoque where codigo_armazem = 2097 and
//                trunc(data_movimento) >= (to_date(('2026-01-26' || ' 00:00:00'), 'YYYY-MM-DD HH24:MI:SS'))
//                and  usuario_execucao <> 'HJS'
//                and CODIGO_TRANSACAO = 851
//                and ctrl_status_envio = 9
//                FETCH FIRST 500 ROWS ONLY
//                """;
//        return gettingTables(sql);
//    }
//
//    @GetMapping("/t701")
//    public ResponseEntity<?> t701() {
//        String sql = """
//                select
//                ID_MOVIMENTO,
//                IDENTIFICADOR,
//                CODIGO_ARMAZEM,
//                CODIGO_ARMAZEM_DESTINO,
//                CODIGO_ITEM,
//                CODIGO_ITEM_DISPLAY,
//                CODIGO_CLIENTE,
//                CODIGO_TRANSACAO,
//                QUANTIDADE_PRE_AJUSTE,
//                QUANTIDADE_POS_AJUSTE,
//                QUANTIDADE_ALTERADA,
//                CTRL_MSG_ENVIO,
//                DATA_MOVIMENTO,
//                STATUS_PRE_INVENTARIO,
//                STATUS_POS_INVENTARIO,
//                CODIGO_MOTIVO,
//                ENDERECO_AJUSTE,
//                IDENTIFICADOR_KIT,
//                USUARIO_EXECUCAO,
//                NUMERO_LOTE,
//                HUID,
//                FORNECEDOR,
//
//
//
//
//
//                COD_INTEGRACAO,
//                COD_CONFIGURACAO,
//                CHAVE_INTEGRACAO,
//                CTRL_STATUS_ENVIO,
//                CTRL_COD_MSG_ENVIO,
//                CTRL_DATA_ENVIO,
//                RMS_CTRL_DESBLOQUEADO,
//                GS_COD_MSG,
//                GS_DSC_MSG,
//                GS_OBS_NOTIF_RMS,
//                ATTRIBUTE_1,
//                GET_ESTOQUE_ANTERIOR,
//                GET_ESTOQUE_POSTERIOR,
//                QUANTIDADE_BLOQ_BLOQ,
//                SALDO_TROCAS
//
//
//
//                from gsretail.gs_sinc_ajuste_estoque where codigo_armazem = 2097 and
//                trunc(data_movimento) >= (to_date(('2026-01-26' || ' 00:00:00'), 'YYYY-MM-DD HH24:MI:SS'))
//                and  usuario_execucao <> 'HJS'
//                and CODIGO_TRANSACAO = 701
//                and ctrl_status_envio = 9
//                FETCH FIRST 500 ROWS ONLY
//                """;
//        return gettingTables(sql);
//    }
//
//
//    @GetMapping("/t853")
//    public ResponseEntity<?> t853() {
//        String sql = """
//                select
//                ID_MOVIMENTO,
//                IDENTIFICADOR,
//                CODIGO_ARMAZEM,
//                CODIGO_ARMAZEM_DESTINO,
//                CODIGO_ITEM,
//                CODIGO_ITEM_DISPLAY,
//                CODIGO_CLIENTE,
//                CODIGO_TRANSACAO,
//                QUANTIDADE_PRE_AJUSTE,
//                QUANTIDADE_POS_AJUSTE,
//                QUANTIDADE_ALTERADA,
//                CTRL_MSG_ENVIO,
//                DATA_MOVIMENTO,
//                STATUS_PRE_INVENTARIO,
//                STATUS_POS_INVENTARIO,
//                CODIGO_MOTIVO,
//                ENDERECO_AJUSTE,
//                IDENTIFICADOR_KIT,
//                USUARIO_EXECUCAO,
//                NUMERO_LOTE,
//                HUID,
//                FORNECEDOR,
//
//
//
//
//
//                COD_INTEGRACAO,
//                COD_CONFIGURACAO,
//                CHAVE_INTEGRACAO,
//                CTRL_STATUS_ENVIO,
//                CTRL_COD_MSG_ENVIO,
//                CTRL_DATA_ENVIO,
//                RMS_CTRL_DESBLOQUEADO,
//                GS_COD_MSG,
//                GS_DSC_MSG,
//                GS_OBS_NOTIF_RMS,
//                ATTRIBUTE_1,
//                GET_ESTOQUE_ANTERIOR,
//                GET_ESTOQUE_POSTERIOR,
//                QUANTIDADE_BLOQ_BLOQ,
//                SALDO_TROCAS
//
//
//
//                from gsretail.gs_sinc_ajuste_estoque where codigo_armazem = 2097 and
//                trunc(data_movimento) >= (to_date(('2026-01-26' || ' 00:00:00'), 'YYYY-MM-DD HH24:MI:SS'))
//                and  usuario_execucao <> 'HJS'
//                and CODIGO_TRANSACAO = 853
//                and ctrl_status_envio = 9
//                FETCH FIRST 500 ROWS ONLY
//                """;
//        return gettingTables(sql);
//    }
//
//    @GetMapping("/t700")
//    public ResponseEntity<?> t700() {
//        String sql = """
//                select
//                ID_MOVIMENTO,
//                IDENTIFICADOR,
//                CODIGO_ARMAZEM,
//                CODIGO_ARMAZEM_DESTINO,
//                CODIGO_ITEM,
//                CODIGO_ITEM_DISPLAY,
//                CODIGO_CLIENTE,
//                CODIGO_TRANSACAO,
//                QUANTIDADE_PRE_AJUSTE,
//                QUANTIDADE_POS_AJUSTE,
//                QUANTIDADE_ALTERADA,
//                CTRL_MSG_ENVIO,
//                DATA_MOVIMENTO,
//                STATUS_PRE_INVENTARIO,
//                STATUS_POS_INVENTARIO,
//                CODIGO_MOTIVO,
//                ENDERECO_AJUSTE,
//                IDENTIFICADOR_KIT,
//                USUARIO_EXECUCAO,
//                NUMERO_LOTE,
//                HUID,
//                FORNECEDOR,
//
//
//
//
//
//                COD_INTEGRACAO,
//                COD_CONFIGURACAO,
//                CHAVE_INTEGRACAO,
//                CTRL_STATUS_ENVIO,
//                CTRL_COD_MSG_ENVIO,
//                CTRL_DATA_ENVIO,
//                RMS_CTRL_DESBLOQUEADO,
//                GS_COD_MSG,
//                GS_DSC_MSG,
//                GS_OBS_NOTIF_RMS,
//                ATTRIBUTE_1,
//                GET_ESTOQUE_ANTERIOR,
//                GET_ESTOQUE_POSTERIOR,
//                QUANTIDADE_BLOQ_BLOQ,
//                SALDO_TROCAS
//
//
//
//                from gsretail.gs_sinc_ajuste_estoque where codigo_armazem = 2097 and
//                trunc(data_movimento) >= (to_date(('2026-01-26' || ' 00:00:00'), 'YYYY-MM-DD HH24:MI:SS'))
//                and  usuario_execucao <> 'HJS'
//                and CODIGO_TRANSACAO = 700
//                and ctrl_status_envio = 9
//                FETCH FIRST 500 ROWS ONLY
//                """;
//        return gettingTables(sql);
//    }
//
//    @GetMapping("/tableSKU")
//    public ResponseEntity<?> tableSDK() {
//        String sql = """
//                select
//                ID_MOVIMENTO,
//                b.CODIGO_ITEM,
//                DATA_MOVIMENTO,
//                                IDENTIFICADOR,
//                                CODIGO_ARMAZEM,
//                                CODIGO_ARMAZEM_DESTINO,
//                                CODIGO_ITEM_DISPLAY,
//                                CODIGO_CLIENTE,
//                                CODIGO_TRANSACAO,
//                                QUANTIDADE_PRE_AJUSTE,
//                                QUANTIDADE_POS_AJUSTE,
//                                QUANTIDADE_ALTERADA,
//                                CTRL_MSG_ENVIO,
//                                STATUS_PRE_INVENTARIO,
//                                STATUS_POS_INVENTARIO,
//                                CODIGO_MOTIVO,
//                                ENDERECO_AJUSTE,
//                                IDENTIFICADOR_KIT,
//                                USUARIO_EXECUCAO,
//                                NUMERO_LOTE,
//                                HUID,
//                                FORNECEDOR,
//
//
//
//                                COD_INTEGRACAO,
//                                COD_CONFIGURACAO,
//                                CHAVE_INTEGRACAO,
//                                CTRL_STATUS_ENVIO,
//                                CTRL_COD_MSG_ENVIO,
//                                CTRL_DATA_ENVIO,
//                                RMS_CTRL_DESBLOQUEADO,
//                                GS_COD_MSG,
//                                GS_DSC_MSG,
//                                GS_OBS_NOTIF_RMS,
//                                ATTRIBUTE_1,
//                                GET_ESTOQUE_ANTERIOR,
//                                GET_ESTOQUE_POSTERIOR,
//                                QUANTIDADE_BLOQ_BLOQ,
//                                SALDO_TROCAS
//
//                from gsretail.gs_sinc_ajuste_estoque a,
//                (
//                    select
//                    CODIGO_ITEM,
//                    max(DATA_MOVIMENTO) DATA
//                     from gsretail.gs_sinc_ajuste_estoque
//                    where data_movimento >= (to_date(('2026-01-26' || ' 00:00:00'), 'YYYY-MM-DD HH24:MI:SS'))
//                    GROUP BY CODIGO_ITEM
//                    ) B
//                where codigo_armazem = 2097
//                and  usuario_execucao <> 'HJS'
//                and ctrl_status_envio = 9
//                and data_movimento = B.DATA
//                and a.CODIGO_ITEM = b.CODIGO_ITEM
//                order by data_movimento desc
//                """;
//        return gettingTables(sql);
//    }

    @GetMapping("/totaisAdjust")
    public ResponseEntity<?> totaisAdjust() {
        String sql = """
                SELECT STATUS, COUNT(*) TOTAL FROM (
                SELECT  to_char(log_date,'DD/MM/YY HH24:MI:SS') data, OWNER, JOB_NAME, status,
                        to_char(req_start_date,'DD/MM/YY HH24:MI:SS') ult_exec,
                        to_char(actual_start_date,'DD/MM/YY HH24:MI:SS') data_atual, 
                        to_char(RUN_DURATION) RUN_DURATION, 
                        additional_info
                  FROM dba_scheduler_job_run_details
                WHERE RMS.dateto_rms7(LOG_DATE) >= 1260514
                   AND STATUS != 'SUCCEEDED')
                   GROUP BY STATUS
                """;
        return gettingTables(sql);
    }

    @GetMapping("/totalSKU")
    public ResponseEntity<?> totalSDK() {
        String sql = """
                SELECT COUNT(*) TOTAL FROM (
                SELECT  to_char(log_date,'DD/MM/YY HH24:MI:SS') data, OWNER, JOB_NAME, status,
                        to_char(req_start_date,'DD/MM/YY HH24:MI:SS') ult_exec,
                        to_char(actual_start_date,'DD/MM/YY HH24:MI:SS') data_atual, 
                        to_char(RUN_DURATION) RUN_DURATION, 
                        additional_info
                  FROM dba_scheduler_job_run_details
                WHERE RMS.dateto_rms7(LOG_DATE) >= 1260514
                   AND STATUS != 'SUCCEEDED')
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