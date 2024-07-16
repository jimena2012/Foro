package com.alura.foro.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<Topico> criarTopico(@RequestBody TopicoRequest request, HttpServletRequest req) {
        String token = jwtTokenProvider.resolveToken(req);
        String email = jwtTokenProvider.getEmail(token);
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        Topico topico = new Topico();
        topico.setTitulo(request.getTitulo());
        topico.setMensagem(request.getMensagem());
        topico.setCurso(request.getCurso());
        topico.setUsuario(usuario);
        topicoRepository.save(topico);
        return ResponseEntity.status(201).body(topico);
    }

    @GetMapping
    public List<Topico> listarTopicos() {
        return topicoRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topico> atualizarTopico(@PathVariable Long id, @RequestBody TopicoRequest request) {
        Topico topico = topicoRepository.findById(id).orElseThrow();
        topico.setTitulo(request.getTitulo());
        topico.setMensagem(request.getMensagem());
        topico.setCurso(request.getCurso());
        topicoRepository.save(topico);
        return ResponseEntity.ok(topico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTopico(@PathVariable Long id) {
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
