package com.steamer.capas.business.service.impl;

import com.steamer.capas.business.service.EmailSender;
import com.steamer.capas.business.service.UserService;
import com.steamer.capas.common.exception.UserException;
import com.steamer.capas.domain.document.ConfirmationToken;
import com.steamer.capas.domain.document.PasswordResetToken;
import com.steamer.capas.domain.dto.response.AuthenticationResponse;
import com.steamer.capas.domain.document.Role;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import com.steamer.capas.persistence.PasswordResetTokenRepository;
import com.steamer.capas.persistence.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    public void register(SignUpRequest request) {

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        String token = signUpUser(
                new User(
                        request.getUserName(),
                        request.getEmail(),
                        request.getCountry(),
                        request.getPassword()

                )
        );
        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + token;
        emailSender.send(
                request.getEmail(),
                buildEmail(request.getUserName(), link));

    }
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "Correo no encontrado"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(resetToken);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        emailSender.send(email, buildPasswordResetEmail(user.getUserName(), resetLink));
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);

        User user = resetToken.getUser();
        user.setPassword(passwordEncoderService.hashPassword(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }

    private String buildPasswordResetEmail(String name, String link) {
        return "<div>Hola " + name + ",</div>"
                + "<div>Para restablecer tu contraseña, haz clic en el siguiente enlace: "
                + "<a href=\"" + link + "\">Restablecer contraseña</a></div>";
    }

    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(User user) {
        boolean emailExists = userRepository
                .findByEmail(user.getEmail())
                .isPresent();
        if (emailExists) {
            throw new UserException(HttpStatus.FORBIDDEN, "Email ya registrado");
        }

        if (userRepository.existsByUserName(user.getUserName())) {
            throw new UserException(HttpStatus.FORBIDDEN, "Usuario ya registrado");
        }

        user.setPassword(passwordEncoderService.hashPassword(user.getPassword()));
        user.setRole(Role.USER);
        user.setAccountEnabled(false);

        User savedUser = userService.create(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                savedUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

        return token;
    }

    @Transactional
    public String confirmToken(String token, HttpServletResponse response ) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        /*if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }*/

        confirmationTokenService.updateConfirmedAt(token, LocalDate.now().atStartOfDay());
        userService.enableUser(
                confirmationToken.getUser());

        try {
            response.sendRedirect("http://localhost:3000/confirm-email");
        } catch (Exception e) {

            return "redirect failed";
        }

        return "redirect:confirmed";
    }

    private String buildEmail(String name, String link) {
        return "<div id=\":jw\" class=\"a3s aiL msg4080604597912816771 adM\"><div class=\"HOEnZb\"><div class=\"adm\"><div id=\"q_21\" class=\"ajR h4\" data-tooltip=\"Ocultar contenido ampliado\" aria-label=\"Ocultar contenido ampliado\" aria-expanded=\"true\"><div class=\"ajT\"></div></div></div><div class=\"im\"><u></u>\n" +
                "\n" +
                "\n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "\n" +
                "        \n" +
                "    \n" +
                "    \n" +
                "\n" +
                "    \n" +
                "\n" +
                "\n" +
                "\n" +
                "<div style=\"padding:0!important;margin:0 auto!important;display:block!important;min-width:100%!important;width:100%!important;background:#ffffff\">\n" +
                "<center>\n" +
                "    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0;padding:0;width:100%;height:100%\" bgcolor=\"#ffffff\" class=\"m_4080604597912816771gwfw\">\n" +
                "        <tbody><tr>\n" +
                "            <td style=\"margin:0;padding:0;width:100%;height:100%\" align=\"center\" valign=\"top\">\n" +
                "                <table width=\"775\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"m_4080604597912816771m-shell\">\n" +
                "                    <tbody><tr>\n" +
                "                        <td class=\"m_4080604597912816771td\" style=\"width:775px;min-width:775px;font-size:0pt;line-height:0pt;padding:0;margin:0;font-weight:normal\">\n" +
                "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                \n" +
                "                                <tbody><tr>\n" +
                "                                    <td class=\"m_4080604597912816771mpy-35 m_4080604597912816771mpx-15\" bgcolor=\"#212429\" style=\"padding:80px\">\n" +
                "                                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "\n" +
                "                                            \n" +
                "                                            <tbody><tr>\n" +
                "                                                <td style=\"font-size:0pt;line-height:0pt;text-align:left;padding-bottom:45px\">\n" +
                "                                                    <a href=\n\""+ link + "\" target=\"_blank\" data-saferedirecturl=\n \"" + link + "\">\n" +
                "                                                        <img src=\"https://ci3.googleusercontent.com/meips/ADKq_NZ9VaflsCh1ddbhLqbwx_I2JgwSVe7geJxRqDIy2XKgdDfSxWn3CETHHi-3w2HEvQLlX35Py3ADciq7jJNS-3dnDBV3bm3Wk-so2EAd2mkTZkZ7Po6QjCSSoB4kKCBWprLB=s0-d-e1-ft#https://store.cloudflare.steamstatic.com/public/shared/images/email/logo.png\" width=\"615\" height=\"88\" border=\"0\" alt=\"Steam\" class=\"CToWUd\" data-bit=\"iit\">\n" +
                "                                                    </a>\n" +
                "\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                            \n" +
                "\n" +
                "                                            \n" +
                "                                            <tr>\n" +
                "                                                <td>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\t\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "\t\t\t\t<tbody><tr>\n" +
                "\t\t\t\t\t<td style=\"font-size:28px;line-height:36px;font-family:Arial,sans-serif,'Motiva Sans';text-align:left;color:#ffffff;padding-bottom:30px\">\n" +
                "\t\t\t\t\t\t<strong>Para continuar con la creación de tu nueva cuenta de Steam, verifica tu dirección de correo electrónico abajo.</strong>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</tbody></table>\n" +
                "\t\t\t\n" +
                "\n" +
                "\n" +
                "\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "\t\t\t\t<tbody><tr>\n" +
                "\t\t\t\t\t<td style=\"padding-bottom:20px\">\n" +
                "\t\t\t\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#17191c\">\n" +
                "\t\t\t\t\t\t\t<tbody><tr>\n" +
                "\t\t\t\t\t\t\t\t<td class=\"m_4080604597912816771mpx-20\" align=\"center\" style=\"padding-top:35px;padding-bottom:35px;padding-left:56px;padding-right:56px\">\n" +
                "\t\t\t\t\t\t\t\t\t<table width=\"400\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"m_4080604597912816771mw-auto\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<tbody><tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<td class=\"m_4080604597912816771btn-18 m_4080604597912816771l-grey4\" bgcolor=\"#235ecf\" style=\"font-size:18px;line-height:22px;font-family:Arial,sans-serif,'Motiva Sans';text-align:center;border-radius:5px;letter-spacing:1px;background:linear-gradient(90deg,#3a9bed 0%,#235ecf 100%);color:#f1f1f1;text-transform:uppercase\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<a href=\n\""+link+"\" style=\"display:block;padding:13px 35px;text-decoration:none;color:#f1f1f1\" target=\"_blank\" data-saferedirecturl=\n\""+link+"\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"text-decoration:none;color:#f1f1f1\">Verificar mi dirección de correo electrónico&nbsp;&nbsp;&nbsp;</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t</tbody></table>\n" +
                "\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t</tbody></table>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</tbody></table>\n" +
                "\t\t\t\n" +
                "\n" +
                "\n" +
                "\n" +
                "\t\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "\t\t\t\t<tbody><tr>\n" +
                "\t\t\t\t\t<td style=\"font-size:18px;line-height:25px;font-family:Arial,sans-serif,'Motiva Sans';text-align:left;color:#dbdbdb;padding-bottom:30px\">Steam necesita una dirección de email verificada para que puedas aprovechar al máximo características como la seguridad de Steam Guard, el Mercado de la Comunidad de Steam y el sistema de intercambio de Steam, y también para que puedas recuperar tu cuenta de forma segura en el futuro.</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</tbody></table>\n" +
                "\t\t\t\n" +
                "\n" +
                "\n" +
                "\t\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "\t\t\t\t<tbody><tr>\n" +
                "\t\t\t\t\t<td style=\"font-size:18px;line-height:25px;font-family:Arial,sans-serif,'Motiva Sans';text-align:left;color:#ffffff;font-weight:bold;padding-bottom:10px\">Gestionar preferencias de correo electrónico</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</tbody></table>\n" +
                "\t\t\t\n" +
                "\n" +
                "\n" +
                "\t\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "\t\t\t\t<tbody><tr>\n" +
                "\t\t\t\t\t<td style=\"font-size:18px;line-height:25px;font-family:Arial,sans-serif,'Motiva Sans';text-align:left;color:#dbdbdb;padding-bottom:30px\">Valve también podría enviarte correos electrónicos ocasionalmente para informarte sobre juegos y eventos en Steam. En caso de no querer recibir dichos mensajes o si deseas administrar el método de contacto una vez ya creada la cuenta, puedes establecer tus preferencias de correo electrónico <a href=\"https://store.steampowered.com/account/emailoptout\" style=\"text-decoration:underline;color:#ffffff\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://store.steampowered.com/account/emailoptout&amp;source=gmail&amp;ust=1715302690285000&amp;usg=AOvVaw2cdOUaOw6mZhVNhZ13_upB\"><span style=\"text-decoration:underline;color:#ffffff\">aquí</span></a>.</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</tbody></table>\n" +
                "\t\t\t\n" +
                "\n" +
                "\n" +
                "\t            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                <tbody><tr>\n" +
                "                    <td style=\"font-size:18px;line-height:25px;font-family:Arial,sans-serif,'Motiva Sans';text-align:left;color:#7abefa;padding-bottom:40px\">Si no has intentado crear una nueva cuenta con este correo electrónico recientemente, puedes ignorar este mensaje.</td>\n" +
                "                </tr>\n" +
                "            </tbody></table>\n" +
                "            \n" +
                "\n" +
                "\n" +
                "\n" +
                "                                                                                                \n" +
                "                                                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                    <tbody><tr>\n" +
                "                                                        <td style=\"padding-top:30px\">\n" +
                "                                                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                <tbody><tr>\n" +
                "                                                                    <td width=\"3\" bgcolor=\"#3a9aed\" style=\"font-size:0pt;line-height:0pt;text-align:left\"></td>\n" +
                "                                                                    <td width=\"37\" style=\"font-size:0pt;line-height:0pt;text-align:left\"></td>\n" +
                "                                                                    <td>\n" +
                "                                                                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                            <tbody><tr>\n" +
                "                                                                                                                                                                    <td style=\"font-size:16px;line-height:22px;font-family:Arial,sans-serif,'Motiva Sans';text-align:left;padding-top:20px;padding-bottom:20px;color:#f1f1f1\">\n" +
                "                                                                                        Saludos,<br>\n" +
                "el equipo de Steam                                                                                    </td>\n" +
                "                                                                                                                                                            </tr>\n" +
                "                                                                        </tbody></table>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                            </tbody></table>\n" +
                "                                                        </td>\n" +
                "                                                    </tr>\n" +
                "                                                </tbody></table>\n" +
                "                                                \n" +
                "                                                \n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "\n" +
                "                                        </tbody></table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                \n" +
                "\n" +
                "                                \n" +
                "                                <tr>\n" +
                "                                    <td class=\"m_4080604597912816771mpy-40 m_4080604597912816771mpx-15\" style=\"padding-top:60px;padding-bottom:60px;padding-left:90px;padding-right:90px\">\n" +
                "                                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "\n" +
                "                                                                                        <tbody><tr>\n" +
                "                                                <td class=\"m_4080604597912816771mpb-40\" style=\"font-size:18px;line-height:25px;color:#000001;font-family:Arial,sans-serif,'Motiva Sans';text-align:left;padding-bottom:60px\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t                                                    <br><br>\n" +
                "\t\t\t                                        Este correo electrónico se genera automáticamente. Por favor, no respondas. Si necesitas ayuda adicional, por favor, visita el Soporte de Steam.                                                </td>\n" +
                "                                            </tr>\n" +
                "                                            \n" +
                "                                                                                        \n" +
                "                                                                                        <tr>\n" +
                "                                            <td style=\"padding-bottom:60px\">\n" +
                "                                                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                    <tbody><tr>\n" +
                "                                                        <th class=\"m_4080604597912816771column\" width=\"270\" style=\"font-size:0pt;line-height:0pt;padding:0;margin:0;font-weight:normal\">\n" +
                "                                                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                <tbody><tr>\n" +
                "                                                                    <td class=\"m_4080604597912816771mpb-40\" style=\"font-size:18px;line-height:25px;color:#000001;font-family:Arial,sans-serif,'Motiva Sans';text-align:left\">\n" +
                "                                                                        <a href=\n\""+link+"\" style=\"text-decoration:underline;color:#000001\" target=\"_blank\" data-saferedirecturl=\n\""+link+"\">\n" +
                "                                                                            <span style=\"text-decoration:underline;color:#000001\">https://help.steampowered.com</span>\n" +
                "                                                                        </a>\n" +
                "                                                                    </td>\n" +
                "                                                                </tr>\n" +
                "                                                            </tbody></table>\n" +
                "                                                        </th>\n" +
                "                                                                                                            </tr>\n" +
                "                                                </tbody></table>\n" +
                "                                            </td>\n" +
                "                                            </tr>\n" +
                "                                            \n" +
                "\n" +
                "                                            \n" +
                "                                                                                            <tr>\n" +
                "                                                    <td style=\"padding-bottom:50px\">\n" +
                "                                                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                            <tbody><tr>\n" +
                "                                                                <th class=\"m_4080604597912816771column\" width=\"270\" valign=\"top\" style=\"font-size:0pt;line-height:0pt;padding:0;margin:0;font-weight:normal;vertical-align:top\">\n" +
                "                                                                    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                        <tbody><tr>\n" +
                "                                                                            <td class=\"m_4080604597912816771mpt-0\" style=\"font-size:0pt;line-height:0pt;text-align:left\">\n" +
                "                                                                                <a href=\n"+link+"\" target=\"_blank\" data-saferedirecturl=\n"+link+"\"><img src=\"https://ci3.googleusercontent.com/meips/ADKq_NZwFmqDmz8iF1nXsWdJv6vFRF3U4mi4IssLjl9Ogw-VxQtPo9yO34G1ugMDLWLfi6Gj1nRftyWC77yln7FAcczFBRdrjdAp8YKCOC0hIIW4COjDKn6TbJvFj7V2Asi7y3R2tose9LydLg=s0-d-e1-ft#https://store.cloudflare.steamstatic.com/public/shared/images/email/logo_footer.png\" width=\"165\" height=\"50\" border=\"0\" alt=\"\" class=\"CToWUd\" data-bit=\"iit\"></a>\n" +
                "                                                                            </td>\n" +
                "                                                                        </tr>\n" +
                "                                                                    </tbody></table>\n" +
                "                                                                </th>\n" +
                "                                                                <th class=\"m_4080604597912816771column-top m_4080604597912816771mpb-40\" width=\"15\" style=\"font-size:0pt;line-height:0pt;padding:0;margin:0;font-weight:normal;vertical-align:top\"></th>\n" +
                "                                                                <th class=\"m_4080604597912816771column\" style=\"font-size:0pt;line-height:0pt;padding:0;margin:0;font-weight:normal\">\n" +
                "                                                                    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                                                                                                    <tbody><tr>\n" +
                "                                                                                <td style=\"font-size:12px;line-height:18px;color:#000001;font-family:Arial,sans-serif,'Motiva Sans';text-align:left\">\n" +
                "                                                                                                                                                                            Para descargar el cliente de escritorio de Steam y aprender más sobre Steam, por favor, visita Acerca de Steam.                                                                                        <br><br>\n" +
                "                                                                                        <a href=\n"+link+"\" style=\"text-decoration:underline;color:#000001\" target=\"_blank\" data-saferedirecturl=\n"+link+"\">\n" +
                "                                                                                            <span style=\"text-decoration:underline;color:#000001\"><strong>Acerca de Steam</strong>\n" +
                "                                                                                            </span>\n" +
                "                                                                                        </a>\n" +
                "                                                                                                                                                                    </td>\n" +
                "                                                                            </tr>\n" +
                "                                                                                                                                            </tbody></table>\n" +
                "                                                                </th>\n" +
                "                                                            </tr>\n" +
                "                                                        </tbody></table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "\n" +
                "                                            \n" +
                "\n" +
                "                                            \n" +
                "                                            <tr>\n" +
                "                                                <td>\n" +
                "                                                    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                        <tbody><tr>\n" +
                "                                                            <th class=\"m_4080604597912816771column-top\" valign=\"top\" width=\"270\" style=\"font-size:0pt;line-height:0pt;padding:0;margin:0;font-weight:normal;vertical-align:top\">\n" +
                "                                                                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                    <tbody><tr>\n" +
                "                                                                        <td style=\"font-size:0pt;line-height:0pt;text-align:left\">\n" +
                "                                                                            <a href=\n"+link+"\" target=\"_blank\" data-saferedirecturl=\n"+link+"\">\n" +
                "                                                                                                                                                                <img src=\"https://ci3.googleusercontent.com/meips/ADKq_NasrLuYhkYZNA2nDp8qFHsvfSTLc5kpQZiHLPVQZqvHxu8kUUdQiFNWiDLDqtE3hygwf6LW0ZSPDUVaV96Esy8xu7ek8NZYXEKbfDOpKN9UTiDOYyqrn3vJVEMy29PUvHppuI_2dT8z=s0-d-e1-ft#https://store.cloudflare.steamstatic.com/public/shared/images/email/logo_valve.jpg\" width=\"165\" height=\"48\" border=\"0\" alt=\"\" class=\"CToWUd\" data-bit=\"iit\">\n" +
                "                                                                                                                                                            </a>\n" +
                "                                                                        </td>\n" +
                "                                                                    </tr>\n" +
                "                                                                </tbody></table>\n" +
                "                                                            </th>\n" +
                "                                                            <th class=\"m_4080604597912816771column-top m_4080604597912816771mpb-40\" valign=\"top\" width=\"15\" style=\"font-size:0pt;line-height:0pt;padding:0;margin:0;font-weight:normal;vertical-align:top\"></th>\n" +
                "                                                            <th class=\"m_4080604597912816771column-top\" valign=\"top\" style=\"font-size:0pt;line-height:0pt;padding:0;margin:0;font-weight:normal;vertical-align:top\">\n" +
                "                                                                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                                    <tbody><tr>\n" +
                "                                                                        <td style=\"font-size:12px;line-height:18px;color:#000001;font-family:Arial,sans-serif,'Motiva Sans';text-align:left;padding-bottom:30px\">\n" +
                "\t                                                                                                                                                        <strong>© Valve Corporation</strong>\n" +
                "                                                                                <br>\n" +
                "                                                                                <strong>PO Box 1688 Bellevue, WA 98009 (USA)</strong>\n" +
                "                                                                                <br><br>\n" +
                "\t\t                                                                        Todos los derechos reservados. Todas las marcas registradas pertenecen a sus respectivos dueños en EE. UU. y otros países.\t                                                                                                                                                </td>\n" +
                "                                                                    </tr>\n" +
                "                                                                    \n" +
                "                                                                </tbody></table>\n" +
                "                                                            </th>\n" +
                "                                                        </tr>\n" +
                "                                                    </tbody></table>\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                        </tbody></table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            \n" +
                "                            </tbody></table>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </tbody></table>\n" +
                "</center>\n" +
                "\n" +
                "    <center style=\"font-family:Arial,sans-serif,'Motiva Sans';color:#000000;font-size:11px;margin-bottom:4px\">\n" +
                "        ¿Tienes problemas para ver este mensaje?        <a href=\n"+link+"\" style=\"font-family:Arial,sans-serif,'Motiva Sans';color:#000000;font-size:11px;margin-bottom:4px\" target=\"_blank\" data-saferedirecturl=\n"+link+"\">\n" +
                "            Haz clic aquí        </a>\n" +
                "    </center>\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "</div></div></div>";
    }

    // Metodo enableUser(String email) : Usa el repositorio de user

    public boolean isAuthenticated(String authToken){
        return jwtService.isTokenValid(authToken);
    }
    public AuthenticationResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println("username:"+ username);
        System.out.println("password:"+ password);

        User user = userRepository.findByUserName(username); // Busca por email

        if (user == null) {
            throw new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        System.out.println("user:" + user.getUserName());
        System.out.println("user:" + user.getEmail());
        System.out.println("user:" + user.getId());
        System.out.println("user:" + user.isEnabled());
        if(!user.isAccountEnabled()){
            throw new UserException(HttpStatus.NOT_ACCEPTABLE, "Mail no confirmado");
        }


        if (!passwordEncoderService.matches(user.getPassword(),password)) {
            throw new UserException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


}
