package br.com.hbsis.Tools.ExportImport;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ExportCSV {

    public void writerHeader(HttpServletResponse response, String header, String name) throws IOException {
        String arquivoCSV = name+".csv";
        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", arquivoCSV);
        response.setHeader(headerKey, headerValue);
        PrintWriter printWriter = response.getWriter();
        printWriter.println(header);
    }

}
