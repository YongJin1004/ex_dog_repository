package com.pcwk.ehr.kakaoPay;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080),0);

        server.createContext("/success", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "결제가 성공적으로 완료되었습니다.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.createContext("/cancel", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "결제가 취소되었습니다.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.createContext("/fail", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "결제가 실패하였습니다.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.setExecutor(null); // 기본 executor 사용
        server.start();
        System.out.println("서버가 8080 포트에서 실행 중입니다.");
    }
}
