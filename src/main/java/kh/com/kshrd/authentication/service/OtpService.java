package kh.com.kshrd.authentication.service;

public interface OtpService {

    String generateOtp(String email);

    boolean validateOtp(String email, String inputOtp);

    boolean verifyOtp(String email, String inputOtp);
}
