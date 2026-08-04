package com.cobasi.etiquetas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List; 
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://172.16.0.191:3003", allowCredentials = "true")
public class labelController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/buscarNota")
    public ResponseEntity<?> buscarNota(@RequestParam String nota, @RequestParam String origem) {
        String sql = "select distinct nfi_origem || rms.dac(nfi_origem) origem,\r\n"
        		+ "                (select tip.tip_nome_fantasia\r\n"
        		+ "                   from rms.aa2ctipo tip\r\n"
        		+ "                  where tip.tip_codigo || tip.tip_digito =\r\n"
        		+ "                        nfi_origem || rms.dac(nfi_origem)) desc_origem,\r\n"
        		+ "                nfi_nota nota,\r\n"
        		+ "                nfi_serie,\r\n"
        		+ "                nfi_agenda agenda,\r\n"
        		+ "                rms.rms7to_date(nfi_data_agenda) data,\r\n"
        		+ "nfi_destino || rms.dac(nfi_destino) loja_destino,\r\n"
        		+ "                (select tip.tip_nome_fantasia\r\n"
        		+ "                   from rms.aa2ctipo tip\r\n"
        		+ "                  where tip.tip_codigo || tip.tip_digito =\r\n"
        		+ "                        nfi_destino || rms.dac(nfi_destino)) desc_destino\r\n"
        		+ "  from rms.ag1lgnfi, aa1ctcon\r\n"
        		+ " where nfi_nota = ?\r\n"
        		+ "   and nfi_origem || rms.dac(nfi_origem) = ?\r\n"
        		+ "   and nfi_agenda = tbc_agenda\r\n"
        		+ "   and tbc_intg_3 = 'S'";
        try {
            
            List<Map<String, Object>> labelResults = jdbcTemplate.queryForList(sql, nota,  origem);

            if (labelResults.isEmpty()) {
               
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of());
            } else {
             
                return ResponseEntity.ok(labelResults);
            }
        } catch (DataAccessException e) {
            System.err.println("Erro ao acessar o banco de dados: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno ao buscar os eventos.", "detalhe", e.getMessage()));
        }
    }
    
    @GetMapping("/buscarChave")
    public ResponseEntity<?> buscarChave(@RequestParam String chave, @RequestParam String origem) {
    	String sql = "select distinct nfi_origem || rms.dac(nfi_origem) origem,\r\n"
    			+ "       \r\n"
    			+ "       (select tip.tip_nome_fantasia\r\n"
    			+ "          from rms.aa2ctipo tip\r\n"
    			+ "         where tip.tip_codigo || tip.tip_digito =\r\n"
    			+ "               nfi_origem || rms.dac(nfi_origem)) desc_origem,\r\n"
    			+ "       \r\n"
    			+ "       nfi_nota nota,\r\n"
    			+ "       \r\n"
    			+ "       nfi_serie,\r\n"
    			+ "       \r\n"
    			+ "       nfi_agenda agenda,\r\n"
    			+ "       \r\n"
    			+ "       rms.rms7to_date(nfi_data_agenda) data,\r\n"
    			+ "       nfi_destino || rms.dac(nfi_destino) destino,\r\n"
    			+ "       (select tip.tip_nome_fantasia\r\n"
    			+ "          from rms.aa2ctipo tip\r\n"
    			+ "         where tip.tip_codigo || tip.tip_digito =\r\n"
    			+ "               nfi_destino || rms.dac(nfi_destino)) desc_destino\r\n"
    			+ "\r\n"
    			+ "  from rms.ag1lgnfi, aa1ctcon\r\n"
    			+ "\r\n"
    			+ " where nfi_chave_nfe = ?\r\n"
    			+ "\r\n"
    			+ " and nfi_origem || rms.dac(nfi_origem) = ?"
    			+ "and nfi_agenda = tbc_agenda\r\n"
    			+ "   and tbc_intg_3 = 'S'";
    	try {
    		
    		List<Map<String, Object>> labelResults = jdbcTemplate.queryForList(sql, chave,  origem);
    		
    		if (labelResults.isEmpty()) {
    			
    			return ResponseEntity.status(HttpStatus.NOT_FOUND)
    					.body(Map.of());
    		} else {
    			
    			return ResponseEntity.ok(labelResults);
    		}
    	} catch (DataAccessException e) {
    		System.err.println("Erro ao acessar o banco de dados: " + e.getMessage());
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    				.body(Map.of("erro", "Erro interno ao buscar os eventos.", "detalhe", e.getMessage()));
    	}
    }
}