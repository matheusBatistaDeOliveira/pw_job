package com.cobasi.etiquetas;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://172.16.0.191:3003", allowCredentials = "true")
public class printingController {
    @PostMapping("/api/print")
    public ResponseEntity<String> printLabel(@RequestBody PrintRequest request) {
        try {
            
            Path tempFile = Files.createTempFile("etiqueta", ".txt");
            Files.write(tempFile, request.getZpl().getBytes());

            // Comando de cópia para impressora
            String command = "cmd /c copy " + tempFile.toString() + " " + request.getPrinter();
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            // Deleta o arquivo após envio
            Files.deleteIfExists(tempFile);

            if (exitCode == 0) {
                return ResponseEntity.ok("Impresso com sucesso.");
            } else {
                return ResponseEntity.status(500).body("Erro ao imprimir.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro interno no servidor.");
        }
    }

    static class PrintRequest {
        private String zpl;
        private String printer;

        public String getZpl() { return zpl; }
        public void setZpl(String zpl) { this.zpl = zpl; }

        public String getPrinter() { return printer; }
        public void setPrinter(String printer) { this.printer = printer; }
    }
}
