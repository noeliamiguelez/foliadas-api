package com.foliadas.foliadas_api.Service;

import com.foliadas.foliadas_api.DTO.FoliadaDTO;
import com.foliadas.foliadas_api.DTO.GrupoDTO;
import com.foliadas.foliadas_api.DTO.ProvinciaDTO;
import com.foliadas.foliadas_api.DTO.UsuarioDTO;
import com.foliadas.foliadas_api.Model.Foliada;
import com.foliadas.foliadas_api.Model.Usuario;
import com.foliadas.foliadas_api.Repository.FoliadaRepository;
import com.foliadas.foliadas_api.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final FoliadaRepository foliadaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, FoliadaRepository foliadaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.foliadaRepository = foliadaRepository;
    }

    // ----------------------------
    // MÉTODOS DE USUARIO
    // ----------------------------
    @Override
    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO getById(int id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return toDTO(u);
    }

    @Override
    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioOpt= usuarioRepository.findByEmail(usuarioDTO.getEmail());
        if (usuarioOpt.isPresent()){
            throw new RuntimeException("YA EXISTE EL MAIL");
        }else {
            Usuario u = new Usuario();
            u.setNome(usuarioDTO.getNombre());
            u.setEmail(usuarioDTO.getEmail());

            String passwordEncriptada = passwordEncoder.encode(usuarioDTO.getPassword());
            u.setPassword(passwordEncriptada);
            Usuario saved = usuarioRepository.save(u);
            return toDTO(saved);
        }
    }

    @Override
    public void delete(int id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario update(int id, Usuario usuario) {
        Usuario existente = usuarioRepository.findById(id).orElse(null);
        if (existente == null) return null;

        existente.setNome(usuario.getNome());
        existente.setEmail(usuario.getEmail());

        return usuarioRepository.save(existente);
    }

    @Override
    public Usuario login(String email, String password) {
        Usuario usuario= usuarioRepository.findByEmail(email).orElse(null);

        if (usuario==null){
            return null;
        }
        boolean coincide= passwordEncoder.matches(password, usuario.getPassword());
        if (coincide){
            return usuario;
        }else{
            return null;
        }
    }

    // ----------------------------
    // FAVORITAS
    // ----------------------------
    @Override
    public Set<FoliadaDTO> getFavoritas(int usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return new HashSet<>();

        return usuario.getFavoritas().stream()
                .map(this::toFoliadaDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public void addFavorita(int usuarioId, int foliadaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        Foliada foliada = foliadaRepository.findById(foliadaId).orElse(null);
        if (usuario != null && foliada != null) {
            usuario.getFavoritas().add(foliada);
            usuarioRepository.save(usuario);
        }
    }

    @Override
    public void removeFavorita(int usuarioId, int foliadaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        Foliada foliada = foliadaRepository.findById(foliadaId).orElse(null);
        if (usuario != null && foliada != null) {
            usuario.getFavoritas().remove(foliada);
            usuarioRepository.save(usuario);
        }
    }

    // ----------------------------
    // MÉTODOS PRIVADOS PARA MAPEAR A DTO
    // ----------------------------
    private UsuarioDTO toDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO(u.getId(), u.getNome(), u.getEmail());

        // Mapear favoritas
        dto.setFavoritas(u.getFavoritas().stream()
                .map(this::toFoliadaDTO)
                .collect(Collectors.toSet()));

        return dto;
    }

    private FoliadaDTO toFoliadaDTO(Foliada f) {
        FoliadaDTO fDTO = new FoliadaDTO();
        fDTO.setId(f.getId());
        fDTO.setNombre(f.getNome());
        fDTO.setFecha(f.getFecha());
        fDTO.setHora(f.getHora());
        fDTO.setLugar(f.getLugar());
        fDTO.setDescripcion(f.getDescripcion());
        fDTO.setLatitude(f.getLatitude());
        fDTO.setLonxitude(f.getLonxitude());
        fDTO.setImaxe(f.getImaxe());
        fDTO.setProvincia(new ProvinciaDTO(f.getProvincia().getId(), f.getProvincia().getNombre()));

        // Mapear grupos
        fDTO.setGrupos(f.getGrupos().stream()
                .map(gr -> new GrupoDTO(gr.getGrupo_id(), gr.getNome(), gr.getTipo(), gr.getOrixen()))
                .collect(Collectors.toSet()));

        return fDTO;
    }
}