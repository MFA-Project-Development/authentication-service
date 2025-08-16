package kh.com.kshrd.authentication.service.serviceimpl;

import jakarta.mail.internet.MimeMessage;
import kh.com.kshrd.authentication.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @SneakyThrows
    @Override
    public void sendMail(String optCode, String email) {
        Context context = new Context();
        context.setVariable("otp", optCode);
        String process = templateEngine.process("index", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject("Verify your email with otp");
        mimeMessageHelper.setText(process, true);
        mimeMessageHelper.setTo(email);
        javaMailSender.send(mimeMessage);
    }

}
