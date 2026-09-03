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
                WHERE RMS.dateto_rms7(LOG_DATE) >= RMS.dateto_rms7(sysdate-10)
                   AND STATUS = 'STOPPED'
                   AND JOB_NAME = 'JOB_PROC_IMPORTA_CAPTURA_SAIDA'
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
                WHERE RMS.dateto_rms7(LOG_DATE) >= RMS.dateto_rms7(sysdate-10)
                   AND STATUS = 'FAILED'
                   AND JOB_NAME = 'JOB_PROC_IMPORTA_CAPTURA_SAIDA'
                """;
        return gettingTables(sql);
    }

    @GetMapping("/tableSucesso")
    public ResponseEntity<?> tableSucesso() {
        String sql = """
                SELECT  to_char(log_date,'DD/MM/YY HH24:MI:SS') data, OWNER, JOB_NAME, status,
                        to_char(req_start_date,'DD/MM/YY HH24:MI:SS') ult_exec,
                        to_char(actual_start_date,'DD/MM/YY HH24:MI:SS') data_atual,
                        to_char(RUN_DURATION) RUN_DURATION,
                        additional_info
                  FROM dba_scheduler_job_run_details
                WHERE RMS.dateto_rms7(LOG_DATE) >= RMS.dateto_rms7(sysdate)
                   AND STATUS = 'SUCCEEDED'
                   AND JOB_NAME = 'JOB_PROC_IMPORTA_CAPTURA_SAIDA'
                   order by data desc
                   FETCH FIRST 500 ROWS ONLY
                """;
        return gettingTables(sql);
    }

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
                WHERE RMS.dateto_rms7(LOG_DATE) >= RMS.dateto_rms7(sysdate-10)
                   AND STATUS != 'SUCCEEDED'
                   AND JOB_NAME = 'JOB_PROC_IMPORTA_CAPTURA_SAIDA'
                   )
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
                WHERE RMS.dateto_rms7(LOG_DATE) >= RMS.dateto_rms7(sysdate-10)
                AND STATUS != 'SUCCEEDED'
                AND JOB_NAME = 'JOB_PROC_IMPORTA_CAPTURA_SAIDA'
                )
                """;
        return gettingTables(sql);
    }

    @GetMapping("/totalSucesso")
    public ResponseEntity<?> totalSucesso() {
        String sql = """
                SELECT COUNT(*) TOTAL FROM (
                SELECT  to_char(log_date,'DD/MM/YY HH24:MI:SS') data, OWNER, JOB_NAME, status,
                        to_char(req_start_date,'DD/MM/YY HH24:MI:SS') ult_exec,
                        to_char(actual_start_date,'DD/MM/YY HH24:MI:SS') data_atual,
                        to_char(RUN_DURATION) RUN_DURATION,
                        additional_info
                  FROM dba_scheduler_job_run_details
                WHERE RMS.dateto_rms7(LOG_DATE) >= RMS.dateto_rms7(sysdate)
                   AND STATUS = 'SUCCEEDED'
                   AND JOB_NAME = 'JOB_PROC_IMPORTA_CAPTURA_SAIDA'
                   order by data desc)
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