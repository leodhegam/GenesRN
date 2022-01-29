package com.pa2.genesrn.controller;

import com.pa2.genesrn.model.Usuario;
import com.pa2.genesrn.service.UsuarioService;
import com.pa2.genesrn.utils.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            usuarioService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "Enviamos um link de redefinição de senha para o seu e-mail. Por favor, verifique.");

//        }
//        catch (CustomerNotFoundException ex) {
//            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("no-reply@-doesangupet.com", "DoeSanguePet suporte");
        helper.setTo(recipientEmail);

        String subject = "Aqui está o link para redefinir sua senha";

        String content = "<p>Olá,</p>"
                + "<p>Você solicitou a redefinição de sua senha.</p>"
                + "<p>Clique no link abaixo para alterar sua senha:</p>"
                + "<p><a href=\"" + link + "\">Redefinir minha senha</a></p>"
                + "<br>"
                + "<p>Ignore este e-mail se você se lembrar de sua senha, "
                + "ou se você não fez o pedido.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Usuario usuario = usuarioService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (usuario == null) {
            model.addAttribute("message", "Token inválido");
            return "message";
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Usuario usuario = usuarioService.getByResetPasswordToken(token);


        if (usuario == null) {
            model.addAttribute("title", "Redefinir sua senha");
            model.addAttribute("message", "Token inválido");
            return "message";
        } else {
            usuarioService.updatePassword(usuario, password);

            model.addAttribute("message", "Senha alterada com sucesso!");
        }

        return "message";
    }
}
