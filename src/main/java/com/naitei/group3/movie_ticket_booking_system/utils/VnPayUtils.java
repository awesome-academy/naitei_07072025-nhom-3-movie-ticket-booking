package com.naitei.group3.movie_ticket_booking_system.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VnPayUtils {

    private static final Logger logger = LoggerFactory.getLogger(VnPayUtils.class);
    private static final String HMAC_SHA512 = "HmacSHA512";
    private static final String CHARSET = StandardCharsets.UTF_8.name();

    public static String getPaymentUrl(Map<String, String> params, String hashSecret, String baseUrl) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        try {
            for (int i = 0; i < fieldNames.size(); i++) {
                String name = fieldNames.get(i);
                String value = params.get(name);
                if (value != null && !value.isEmpty()) { // check null/empty
                    String encodedValue = URLEncoder.encode(value, CHARSET);
                    hashData.append(name).append('=').append(encodedValue);
                    query.append(URLEncoder.encode(name, CHARSET)).append('=').append(encodedValue);
                    if (i != fieldNames.size() - 1) {
                        hashData.append('&');
                        query.append('&');
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Lỗi mã hóa URL khi tạo payment URL: {}", e.getMessage(), e);
        }

        String secureHash = hmacSHA512(hashSecret, hashData.toString());
        logger.info("Generated vnp_SecureHash: {}", secureHash);

        return baseUrl + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    public static String hmacSHA512(String key, String data) {
        if (key == null || data == null) {
            logger.error("Key hoặc data null khi tạo HMAC SHA512");
            return "";
        }
        try {
            Mac mac = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA512);
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            String hashResult = hash.toString();

            System.out.println("HMAC SHA512 result: " + hashResult);
            return hash.toString();
        } catch (Exception e) {
            logger.error("Lỗi tạo HMAC SHA512: {}", e.getMessage(), e);
            return "";
        }
    }

    public static boolean verifyPayment(Map<String, String> params, String secureHash, String hashSecret) {
        if (params == null || secureHash == null || hashSecret == null) {
            logger.error("Tham số verifyPayment null");
            return false;
        }
        Map<String, String> fields = new HashMap<>(params);
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        String signData = buildData(fields);
        String generatedHash = hmacSHA512(hashSecret, signData);

        logger.info("Generated vnp_SecureHash for verification: {}", generatedHash);
        logger.info("Received vnp_SecureHash: {}", secureHash);

        return generatedHash.equalsIgnoreCase(secureHash);
    }

    private static String buildData(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();

        try {
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = fields.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) { // check null/empty
                    sb.append(fieldName).append('=')
                      .append(URLEncoder.encode(fieldValue, CHARSET));
                    if (i != fieldNames.size() - 1) {
                        sb.append('&');
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Lỗi mã hóa URL khi build data verify hash: {}", e.getMessage(), e);
        }

        return sb.toString();
    }
}
