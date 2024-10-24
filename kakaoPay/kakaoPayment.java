package com.pcwk.ehr.kakaoPay;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pcwk.ehr.book.BookingMovie;
import com.pcwk.ehr.movie.MovieDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class kakaoPayment {

    private static final String ADMIN_KEY = "b4b2c04ae7e82f90dd52b235a41d8c24";

    public static String preparePayment(String amount) throws IOException {
        if (amount == null || amount.isEmpty() || amount.equals("0")) {
            throw new IllegalArgumentException("결제 금액이 유효하지 않습니다: " + amount);
        }

        String apiUrl = "https://kapi.kakao.com/v1/payment/ready";

        String params = "cid=TC0ONETIME" +
                "&partner_order_id=1001" +
                "&partner_user_id=test_user" +
                "&item_name=영화 티켓" +
                "&quantity=1" +
                "&total_amount=" + amount +
                "&tax_free_amount=0" +
                "&approval_url=https://f3f2-218-144-130-138.ngrok-free.app/success" +
                "&cancel_url=https://f3f2-218-144-130-138.ngrok-free.app/cancel" +
                "&fail_url=https://f3f2-218-144-130-138.ngrok-free.app/fail";

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "KakaoAK " + ADMIN_KEY);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            String jsonResponse = response.toString();
            String paymentUrl = jsonResponse.split("\"next_redirect_pc_url\":\"")[1].split("\"")[0];

            return paymentUrl;

        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            throw new IOException("HTTP error code : " + responseCode + " " + response.toString());
        }
    }
}
