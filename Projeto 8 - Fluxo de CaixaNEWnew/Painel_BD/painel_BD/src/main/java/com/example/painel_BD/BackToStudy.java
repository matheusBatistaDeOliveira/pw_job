//package com.example.painel_BD;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController //acesso ao banco
//@CrossOrigin    //acesso a hospedagem
//public class BackToStudy {
//    @Autowired
//    private JdbcTemplate jdbcTemplate; //login ao banco
//
//    @GetMapping("/total") //API http
//    public ResponseEntity<?> total() {
//        String sql = "SELECT COUNT (distinct CTR_LOJA) TOTAL FROM RMS.AG2VCTRL WHERE CTR_DATA_MOV BETWEEN 240901 AND 241120";
//        return getting(sql);
//    }
//
//    ResponseEntity<?> getting(String sql) {
//        try {
//            Map<String, Object> get = jdbcTemplate.queryForMap(sql);
//            return ResponseEntity.ok(get);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Erro ao consultar o banco de dados: " + e.getMessage());
//        }
//    }
//}