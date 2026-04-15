package org.etec.ProjetoGenesisAPI.batata.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class Get {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/1")
    public Object tables(){
        String sql = """
                SELECT * FROM gsretail.gs_integra_gedi_nf
                FETCH FIRST 5 ROWS ONLY
                """;
        return jdbcTemplate.queryForList(sql);
    }
    @GetMapping("/2")
    public Object totais(){
        String sql = """
                SELECT COUNT(*) AS TOTAL FROM gsretail.gs_integra_gedi_nf
                """;
        return jdbcTemplate.queryForList(sql);
    }
}
