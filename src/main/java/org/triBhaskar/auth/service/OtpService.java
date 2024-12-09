package org.triBhaskar.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.security.SecureRandom;

@Service
public class OtpService {
    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    private static final String OTP_PREFIX = "otp_";
    private static final long OTP_VALIDITY_SECONDS = 10 * 60; // 10 minutes

    @Autowired
    private JedisPooled client;

    public void saveOtp(String email, String otp) {
        String key = OTP_PREFIX + email;
        client.setex(key, OTP_VALIDITY_SECONDS, otp);
    }

    public String getOtp(String email) {
        String key = OTP_PREFIX + email;
        return client.get(key);
    }

    public void deleteOtp(String email) {
        String key = OTP_PREFIX + email;
        client.del(key);
    }
    public String generateOtp() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
