package com.auth3.authteste3.config;

import com.auth3.authteste3.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Filtro que roda UMA VEZ em toda requisição, antes de chegar no controller.
// Ele olha se veio um token e, se for válido, marca a requisição como "autenticada".
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // O cliente manda o token no header: Authorization: Bearer <token>
        String header = request.getHeader("Authorization");

        // Só tenta ler se o header existe e começa com "Bearer "
        if (header != null && header.startsWith("Bearer ")) {

            // Remove os 7 caracteres de "Bearer " e sobra só o token
            String token = header.substring(7);

            try {
                // Se o token for válido, devolve o e-mail do usuário
                String email = jwtService.extractEmail(token);

                // Cria o objeto que diz "essa requisição é do usuário X".
                // O 3º parâmetro seria a lista de permissões (roles); vazia por enquanto.
                var auth = new UsernamePasswordAuthenticationToken(email, null, List.of());

                // Guarda no contexto de segurança: a partir daqui, o Spring
                // considera a requisição AUTENTICADA
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException | IllegalArgumentException e) {
                // Token inválido, adulterado ou expirado: não autentica.
                // A requisição segue sem login e o Security responde 401.
            }
        }

        // Passa a requisição para o próximo filtro / controller
        chain.doFilter(request, response);
    }
}