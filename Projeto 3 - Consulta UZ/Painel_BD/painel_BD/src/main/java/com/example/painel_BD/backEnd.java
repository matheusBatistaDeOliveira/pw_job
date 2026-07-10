package com.example.painel_BD;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class backEnd {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/valuesOfSearch")
    public ResponseEntity<?> tabela(
            @RequestParam(required = false, defaultValue = "") String uz,
            @RequestParam(required = false, defaultValue = "") String order
    ) {
        String sql = """
               select\s
               id_mst_in ID,
               warehouseid ORIGEM,
               ordernumber ORDER_KORBER,
               printer IMPRESSORA,
               userid USUARIO,
               uz,
               gs_status,
               gs_status_msg,
               transactioncode,
               nfnumber_out NOTA,
               gs_data_retorno_wms,
               substr(ordernumber,10,8) MAPA,
               (
                select max(pale_destino)
                from gsretail.GS_INTEGRA_TMS_CAPA_WA
                where to_number(pale_carga) = to_number(substr(ordernumber,10,8))
               ) destino
                 from gsretail.GS_INTEGRA_WMS_MST_NODE_IN
                where trunc(gs_data_retorno_wms) >= trunc(sysdate - 50)
                 and warehouseid = 2097
               """;


        Object[] params;

        if (!order.isEmpty() && !uz.isEmpty()) {
            params = new Object[]{uz, order};
            sql += " and uz = ? and ordernumber = ? order by gs_data_retorno_wms desc";
        } else if(!uz.isEmpty()) {
            params = new Object[]{uz};
            sql += " and uz = ? order by gs_data_retorno_wms desc";
        }else{
            sql += " and ordernumber = ? order by gs_data_retorno_wms desc";
            params = new Object[]{order};
        }

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, params
        );

        return ResponseEntity.ok(resultList);
    }
}